package com.trade.controller;

import com.trade.code.AuthCode;
import com.trade.dto.LatestDealData;
import com.trade.dto.ResultBean;
import com.trade.model.Fuser;
import com.trade.service.front.FrontUserService;
import com.trade.service.front.FrontValidateService;
import com.trade.util.Constants;
import com.trade.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BaseController {
    @Autowired(required = false)
    private HttpServletRequest request;
    @Autowired(required = false)
    private HttpServletResponse response;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private FrontUserService frontUserService;

    protected final static int SUCCESS = 200;                 // 处理成功
    protected final static int UNKNOWN_ERROR = -1;           // 未知错误

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public HttpSession getSession() {
        return this.request.getSession(true);
    }

    public Fuser getFuser() {
        return (Fuser) this.getSession().getAttribute(Constants.USER_LOGIN_SESSION);
    }

    public Fuser getValidateFuser() {
        return (Fuser) this.getSession().getAttribute(Constants.USER_LOGIN_VALIDATE_SESSION);
    }

    protected ResultBean codeLimit(String name, String code){
        AuthCode authCode = getAuthCode();
        if(authCode == null){
            return forFailureResult(100);
        }
        String ip = getIpAddr();
        int _limit = this.frontValidateService.getLimitCount(ip, authCode.getActionType());
        if (_limit <= 0) {
            return forFailureResult(101);
        } else {
            if (!authCode.isEnabled(name, code)) {
                this.frontValidateService.updateLimitCount(ip, authCode.getActionType());
                return forFailureResult(102, _limit - 1);
            } else {
                this.frontValidateService.deleteCountLimite(ip, authCode.getActionType());
            }
            removeAuthCode();
        }
        return forSuccessResult();
    }

    public void setLoginUser(Fuser fuser) {
        this.getSession().setAttribute(Constants.USER_LOGIN_SESSION, fuser);
    }

    protected ResultBean forSuccessResult(Object data){
        return forSuccessResult(data, SUCCESS);
    }

    protected ResultBean forSuccessResult(){
        return forSuccessResult(null, 0);
    }

    protected ResultBean forSuccessResult(Object data, int totalCount){
        return new ResultBean(SUCCESS, data, totalCount);
    }

    protected ResultBean forFailureResult(int code, Object data, String message){
        return new ResultBean(code, data, message);
    }

    protected ResultBean forFailureResult(int code){
        return forFailureResult(code, null, null);
    }

    protected ResultBean forFailureResult(int code, Object data){
        return forFailureResult(code, data, null);
    }

    /**
     * api异常
     *
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected Object exceptionHandler(Exception e){
        e.printStackTrace();
        return forFailureResult(UNKNOWN_ERROR, null, "系统异常");
    }

    /**
     * 登录用户
     *
     * @param session
     * @return
     */
    protected Fuser getLoginUser(HttpSession session){
        return (Fuser) session.getAttribute(Constants.USER_LOGIN_SESSION);
    }

    protected AuthCode getAuthCode() {
        return (AuthCode) getSession().getAttribute(Constants.SESSION_CAPTCHA_CODE);
    }

    protected void setAuthCode(AuthCode authCode) {
        getRequest().getSession().setAttribute(Constants.SESSION_CAPTCHA_CODE, authCode);
    }

    protected void removeAuthCode() {
        getSession().removeAttribute(Constants.SESSION_CAPTCHA_CODE);
    }

    public Fuser getSessionUser(HttpServletRequest request){
        return (Fuser) request.getSession().getAttribute(Constants.USER_LOGIN_SESSION);
    }

    protected AuthCode getImageAuthCode() {
        return (AuthCode) getSession().getAttribute(Constants.IMAGE_CODE_KEY);
    }

    protected void removeImageAuthCode() {
        getSession().removeAttribute(Constants.IMAGE_CODE_KEY);
    }

    public String getIpAddr() {
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.split(",")[0];
    }

    protected boolean userNameIsExist(String name){
        return frontUserService.isEmailExist(name) || frontUserService.isMobileExists(name);
    }

    protected void sortCoinMap(List<Map> list) {
        list.sort((e1, e2) -> {
            Integer order1 = (Integer) e1.get("order");
            Integer order2 = (Integer) e2.get("order");
            if(order1 == 0 && order2 != 0){
                return 1;
            }else if(order1 != 0 && order2 == 0){
                return -1;
            } else {
                return order1.compareTo(order2);
            }
        });
    }

    public void sortCoinRank(List<LatestDealData> dealDatas, int sort){
        switch (sort){
            case 1:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 最新成交价升序
                    return Double.valueOf(o1.getLastDealPrize()).compareTo(o2.getLastDealPrize());
                });
                break;
            case 2:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 最新成交价降序
                    return Double.valueOf(o2.getLastDealPrize()).compareTo(o1.getLastDealPrize());
                });
                break;
            case 3:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 24h成交量升序
                    return Double.valueOf(o1.getVolumn()).compareTo(o2.getVolumn());
                });
                break;
            case 4:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 24h成交量降序
                    return Double.valueOf(o2.getVolumn()).compareTo(o1.getVolumn());
                });
                break;
            case 5:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 总市值升序
                    return Double.valueOf(o1.getFmarketValue()).compareTo(o2.getFmarketValue());
                });
                break;
            case 6:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 总市值降序
                    return Double.valueOf(o2.getFmarketValue()).compareTo(o1.getFmarketValue());
                });
                break;
            case 7:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 日涨跌幅升序
                    return Double.valueOf(o1.getFupanddown()).compareTo(o2.getFupanddown());
                });
                break;
            case 8:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 日涨跌幅降序
                    return Double.valueOf(o2.getFupanddown()).compareTo(o1.getFupanddown());
                });
                break;
            case 9:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 周涨跌幅升序
                    return Double.valueOf(o1.getFupanddownweek()).compareTo(o2.getFupanddownweek());
                });
                break;
            case 10:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 周涨跌幅降序
                    return Double.valueOf(o2.getFupanddownweek()).compareTo(o1.getFupanddownweek());
                });
                break;
            case 13:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 买价升序
                    return Double.valueOf(o1.getLowestSellPrize()).compareTo(o2.getLowestSellPrize());
                });
                break;
            case 14:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 买价降序
                    return Double.valueOf(o2.getLowestSellPrize()).compareTo(o1.getLowestSellPrize());
                });
                break;
            case 15:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 卖价升序
                    return Double.valueOf(o1.getHigestBuyPrize()).compareTo(o2.getHigestBuyPrize());
                });
                break;
            case 16:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 卖价降序
                    return Double.valueOf(o2.getHigestBuyPrize()).compareTo(o1.getHigestBuyPrize());
                });
                break;
            case 17:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 成交额升序
                    return Double.valueOf(o1.getFentrustValue()).compareTo(o2.getFentrustValue());
                });
                break;
            case 18:
                Collections.sort(dealDatas, (o1, o2) -> {
                    // 成交额降序
                    return Double.valueOf(o2.getFentrustValue()).compareTo(o1.getFentrustValue());
                });
                break;
            case 19:
                // 首页排序
                Collections.sort(dealDatas, (o1, o2) -> {
                    int val = 0;
                    if(o1.getHomeOrder() == 0 && o2.getHomeOrder() != 0){
                        val = 1;
                    }else if(o1.getHomeOrder() != 0 && o2.getHomeOrder() == 0){
                        val = -1;
                    } else {
                        val = Integer.valueOf(o1.getHomeOrder()).compareTo(o2.getHomeOrder());
                    }
                    return val;
                });
                break;
            case 20:
                // 板块排序
                Collections.sort(dealDatas, (o1, o2) -> {
                    int val = 0;
                    if(o1.getTypeOrder() == 0 && o2.getTypeOrder() != 0){
                        val = 1;
                    }else if(o1.getTypeOrder() != 0 && o2.getTypeOrder() == 0){
                        val = -1;
                    } else {
                        val = Integer.valueOf(o1.getTypeOrder()).compareTo(o2.getTypeOrder());
                    }
                    return val;
                });
                break;
            default:
                // 默认排序
                Collections.sort(dealDatas, (o1, o2) -> {
                    if(o1.getTotalOrder() == 0 && o2.getTotalOrder() != 0){
                        return 1;
                    }else if(o1.getTotalOrder() != 0 && o2.getTotalOrder() == 0){
                        return -1;
                    } else {
                        return Integer.valueOf(o1.getTotalOrder()).compareTo(o2.getTotalOrder());
                    }
                });
                break;
        }
    }
}
