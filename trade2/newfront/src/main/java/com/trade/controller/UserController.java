package com.trade.controller;

import com.trade.Enum.CountLimitTypeEnum;
import com.trade.Enum.UserStatusEnum;
import com.trade.comm.ConstantMap;
import com.trade.dto.OperateUserActionDTO;
import com.trade.dto.ResultBean;
import com.trade.dto.UserDto;
import com.trade.model.Flog;
import com.trade.model.Fuser;
import com.trade.mq.MessageQueueService;
import com.trade.mq.QueueConstants;
import com.trade.service.admin.LogService;
import com.trade.service.front.FrontUserService;
import com.trade.service.front.FrontValidateService;
import com.trade.util.Constants;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class UserController extends BaseController {
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private LogService logService;

    @RequestMapping(value = "/v1/login", method = RequestMethod.POST)
    public Object newLogin(@RequestParam String name,
                           @RequestParam String pwd) {

        if (!StringUtils.hasText(name)) {
            return forFailureResult(1);
        }
        if (!StringUtils.hasText(pwd)) {
            return forFailureResult(2);
        }

        String ip = getIpAddr();
        int limitedCount = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.LOGIN_PASSWORD);

        if (limitedCount <= 0) {
            return forFailureResult(3);
        }

        Fuser fuser = this.frontUserService.findForLogin(name, pwd);
        if (Objects.isNull(fuser)) {
            this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
            return forFailureResult(4, limitedCount - 1, null);
        }

        if (fuser.getFstatus() == UserStatusEnum.FORBBIN_VALUE) {
            return forFailureResult(5);
        }

        ResultBean resultBean = validateDevice(fuser);
        if(SUCCESS != resultBean.getCode()){
            return resultBean;
        }
        return loginSuccess(fuser);
    }

    private ResultBean validateDevice(Fuser fuser) {
        String deviceValue = Utils.getCookie(getRequest().getCookies(), Constants.LOGIN_COOKIE);
        if(StringUtils.hasText(deviceValue)) {
            List<Flog> flogs = logService.findGroupByUser(fuser.getFid());
            boolean is_exist = false;
            for (Flog log: flogs) {
                if(deviceValue.equals(log.getFkey5())){
                    is_exist = true;
                }
            }
            if(!CollectionUtils.isEmpty(flogs)){
                if(!is_exist){
                    return this.secValidate(fuser); //二次认证
                }
            }
        }else{
            List<Flog> flogs = logService.findGroupByUser(fuser.getFid());
            if(!CollectionUtils.isEmpty(flogs)){
                return this.secValidate(fuser); //二次认证
            }
        }
        return forSuccessResult();
    }

    private ResultBean secValidate(Fuser fuser){
        getSession().setAttribute(Constants.USER_LOGIN_VALIDATE_SESSION, fuser);
        if(StringUtils.isEmail(fuser.getFloginName())){
            return forFailureResult(6);
        }else{
            return forFailureResult(7);
        }
    }

    @RequestMapping(value = "/v1/secLogin", method = RequestMethod.POST)
    public Object secLogin(@RequestParam String code) {
        Fuser fuser = getValidateFuser();
        if(fuser == null){
            return forFailureResult(1);
        }
        ResultBean resultBean = codeLimit(fuser.getFloginName(), code);
        if(SUCCESS != resultBean.getCode()){
            return resultBean;
        }
        getSession().removeAttribute(Constants.USER_LOGIN_VALIDATE_SESSION);
        return loginSuccess(fuser);
    }

    private ResultBean loginSuccess(Fuser fuser){
        // 清理估值缓存
        getSession().removeAttribute(Constants.USER_ASSET);
        // 保留设备号
        String deviceValue = Utils.getCookie(getRequest().getCookies(), Constants.LOGIN_COOKIE);
        if(!StringUtils.hasText(deviceValue)) {
            deviceValue = Utils.UUID();
        }
        //uid
        loginCookie(deviceValue, fuser);
        // 保存登录状态
        setLoginUser(fuser);
        // 清除登录错误限制
        String ip = getIpAddr();
        this.frontValidateService.deleteCountLimite(ip, CountLimitTypeEnum.LOGIN_PASSWORD);
        // 登录日志
        logService.addLoginLog(fuser, ip, "", deviceValue, getSession().getId());//插入日志
        // 登录后台处理线程
        messageQueueService.publish(QueueConstants.USER_LOGINED_BACK_QUEUE, new UserDto(fuser.getFid(), new Date()));
        return forSuccessResult(fuser.getFid());
    }

    private void loginCookie(String deviceValue, Fuser fuser){
        Cookie loginCookie = new Cookie(Constants.LOGIN_COOKIE, deviceValue);
        loginCookie.setPath("/");
        String cookieDomain = constantMap.getString("cookie_domain");
        if(!"null".equals(cookieDomain)){
            loginCookie.setDomain(cookieDomain);
        }
        loginCookie.setMaxAge(365 * 24 * 60 * 60);
        loginCookie.setHttpOnly(true);

        Cookie uidCookie = new Cookie("uid", String.valueOf(fuser.getFid()));
        uidCookie.setPath("/");
        loginCookie.setHttpOnly(true);

        getResponse().addCookie(loginCookie);
        getResponse().addCookie(uidCookie);
    }

    @RequestMapping(value = "/v1/logout")
    public Object logout(HttpServletRequest request) {
        Fuser fuser = getFuser();
        if(Objects.nonNull(fuser)){
            request.getSession().removeAttribute(fuser.getFid() + "trade");
            request.getSession().removeAttribute(Constants.USER_LOGIN_SESSION);
        }
        return forSuccessResult(null);
    }

    @RequestMapping(value = "/v1/register", method = RequestMethod.POST)
    public Object doRegister(HttpServletRequest request, @RequestParam() String name,
                      @RequestParam() String pwd,
                      @RequestParam(required = false,defaultValue = "0086") String areaCode,@RequestParam(required = false) String inviteCode,
                      @RequestParam() String code) {

        if (StringUtils.isEmpty(name)) {
            return forFailureResult(1);
        }
        if(userNameIsExist(name)){
            return forFailureResult(2);
        }
        if (StringUtils.isEmpty(pwd)) {
            return forFailureResult(3);
        }
        ResultBean result = codeLimit(name, code);
        if(SUCCESS != result.getCode()){
            return result;
        }
        // 注册
        Fuser fuser = new Fuser();
        if(StringUtils.isEmail(name)){
            fuser.setFemail(name);
            fuser.setFnickName(name.split("@")[0]);
        }else{
            fuser.setFtelephone(name);
            fuser.setFnickName(name);
        }
        fuser.setFloginName(name);
        fuser.setFareaCode(areaCode);
        fuser.setFloginPassword(Utils.MD5(pwd));
        fuser.setFregisterIp(getIpAddr());// 注册IP
        fuser.setFsourceUrl(request.getHeader("Referer"));// 来源url
        //推广会员逻辑 无需用户手动输入推荐人的信息 系统自动从链接中获取
        String url=request.getHeader("Referer");
        if(url!=null&&url.contains("?")){
            String str = url.substring(url.indexOf("?")+1);
            String inviteCodeBySpread= str.split("=")[1];
            String typeName=  str.split("=")[0];
            if(StringUtils.hasText(inviteCodeBySpread)&&"inviteCode".equals(typeName)){
                inviteCode=inviteCodeBySpread;
            }
        }



        try {
            if(StringUtils.hasText(inviteCode) && "1".equals(constantMap.getString("frontInvite"))){
                Fuser user = frontUserService.findById(Integer.valueOf(inviteCode));
                if (null != user) {
                    fuser.setfIntroUser_id(user);
                }else{
                    return forFailureResult(4);
                }
            }
            frontUserService.saveRegister(fuser);
            return loginSuccess(fuser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forFailureResult(UNKNOWN_ERROR);
    }

    @RequestMapping(value = "/v1/findPwd", method = RequestMethod.POST)
    public Object findPassword(@RequestParam() String name, @RequestParam() String code, @RequestParam()String pwd) {
        if(StringUtils.isEmpty(name)){
            return forFailureResult(1);
        }
        if(StringUtils.isEmpty(code)){
            return forFailureResult(2);
        }
        if(StringUtils.isEmpty(pwd)){
            return forFailureResult(3);
        }
        if(!userNameIsExist(name)){
            return forFailureResult(4);
        }
        ResultBean result = codeLimit(name, code);
        if(SUCCESS != result.getCode()){
            return result;
        }
        Fuser fuser = frontUserService.findByLoginName(name);
        fuser.setFloginPassword(Utils.MD5(pwd));
        // 邮件通知
        frontUserService.updateFuser(fuser);
        //强制下线  有问题
        //messageQueueService.bpublish(QueueConstants.FORCE_OFFLINE_QUEUE, new OperateUserActionDTO(fuser.getFid(), UserStatusEnum.FORBBIN_VALUE, Utils.getTimestamp()));
        return forSuccessResult();
    }
}