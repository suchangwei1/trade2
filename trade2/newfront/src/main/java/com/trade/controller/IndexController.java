package com.trade.controller;

import com.alibaba.fastjson.JSON;
import com.trade.Enum.*;
import com.trade.cache.data.RealTimeArticleService;
import com.trade.cache.data.RealTimeDataService;
import com.trade.cache.data.RealTimePriceService;
import com.trade.code.AuthCode;
import com.trade.comm.ConstantMap;
import com.trade.dto.ArticleDTO;
import com.trade.dto.ArticleItemDTO;
import com.trade.dto.LatestDealData;
import com.trade.dto.ResultBean;
import com.trade.model.*;
import com.trade.service.admin.ArticleService;
import com.trade.service.front.*;
import com.trade.util.Constants;
import com.trade.util.FluentHashMap;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api")
public class IndexController extends BaseController {
    private static String EMAIL = "email";
    private static String MOBILE = "mobile";
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private RealTimePriceService realTimePriceService;
    @Autowired
    private FrontQuestionService frontQuestionService;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private RealTimeArticleService realTimeArticleService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;

    @RequestMapping(value = "/v1/session")
    public Object userAbout(){
        Fuser fuser = getFuser();
        if(Objects.isNull(fuser)){
            return forFailureResult(1);
        }
        boolean flag1 = fuser.getFhasRealValidate()? true: false;
        boolean flag2 = fuser.getfIdentityStatus() == 2? true: false;
        if(!flag1 || !flag2){
            fuser = frontUserService.findById(fuser.getFid());
            getSession().setAttribute(Constants.USER_LOGIN_SESSION, fuser) ;
        }
        Map map = new HashMap();
        map.put("authDeep", fuser.getfIdentityStatus() == 2? true: false);
        map.put("authDeepPost", fuser.getfIdentityStatus() == 1? true: false);
        map.put("auth", fuser.getFhasRealValidate()? true: false);
        map.put("post", fuser.getFpostRealValidate()? true: false);
        map.put("mobile", fuser.getFtelephone() != null ? true: false);
        map.put("mobile_num", fuser.getFtelephone());
        map.put("email_num", fuser.getFemail());
        map.put("email", fuser.getFemail() != null? true: false);
        map.put("safeword", fuser.getFtradePassword() != null? true: false);
        map.put("nick", fuser.getFnickName());
        map.put("loginName", fuser.getFloginName());
        map.put("real", fuser.getFrealName());
        map.put("bank", fuser.getBankAccount());
        map.put("id", fuser.getFid());
        map.put("has_otc_set", fuser.isHasOtcSet());
        Map<String, Object> hashMap = new HashMap();
        hashMap.put("fstatus", MessageStatusEnum.NOREAD_VALUE);
        hashMap.put("freceiver.fid", getFuser().getFid());
        int count = frontQuestionService.findFmessageByParamCount(hashMap);
        return forSuccessResult(map, count);
    }
    @RequestMapping(value = "/v1/banner")
    public Object banner(){
        String lang = Utils.getCookie(getRequest().getCookies(), "lang");
        if("cn".equals(lang)){
            return constantMap.get(Constants.BIGIMAGE);
        }else {
            return constantMap.get(Constants.ENBIGIMAGE);
        }
    }
    @RequestMapping(value = "/v1/frontSession")
    public Object frontSession(){
        Fuser fuser = getFuser();
        if(Objects.isNull(fuser)){
            return forFailureResult(1);
        }
        boolean flag1 = fuser.getFhasRealValidate()? true: false;
        boolean flag2 = fuser.getfIdentityStatus() == 2? true: false;
        if(!flag1 || !flag2){
            fuser = frontUserService.findById(fuser.getFid());
            flag1 = fuser.getFhasRealValidate()? true: false;
            flag2 = fuser.getfIdentityStatus() == 2? true: false;
            getSession().setAttribute(Constants.USER_LOGIN_SESSION, fuser) ;
        }
        Map map = new HashMap();
        map.put("auth", flag1);
        map.put("no", fuser.getFidentityNo());
        map.put("name", fuser.getFrealName());
        map.put("authDeep", flag2);
        map.put("authDeepPost", fuser.getfIdentityStatus() == 1? true: false);
        map.put("loginName", fuser.getFloginName());
        map.put("email", fuser.getFemail());
        map.put("mobile", fuser.getFtelephone());
        return forSuccessResult(map);
    }

    /**
     * 首页资产估值
     *
     */
    @RequestMapping("/v1/asset")
    public ResultBean getAsset() {
        Fuser fuser = getFuser();
        if (Objects.isNull(fuser)) {
            return forSuccessResult(0);
        }

        // 如果Session已存在则不再查询
        if (null != getSession().getAttribute(Constants.USER_ASSET)) {
            long lastUpdateTime = NumberUtils.toLong(getSession().getAttribute(Constants.USER_ASSET_TIME) + "");
            if (System.currentTimeMillis() - lastUpdateTime < 60 * 1000) {
                return forSuccessResult(getSession().getAttribute(Constants.USER_ASSET));
            }
        }

        // 虚拟钱包
        Map<Integer, Fvirtualwallet> fvirtualwallets = this.frontUserService.findVirtualWallet(fuser.getFid());

        // 估计总资产
        // （币种+冻结币种）* 买一价
        double asset = 0;
        List<LatestDealData> list = realTimeDataService.getLatestDealDataList();
        for (LatestDealData dealData : list) {
            Fvirtualwallet fvirtualwallet = fvirtualwallets.get(dealData.getFid());
            if (Objects.nonNull(fvirtualwallet)) {
                double highestBuyPrice =  realTimeDataService.getHighestBuyPrize(dealData.getFid());
                asset += (fvirtualwallet.getFtotal() + fvirtualwallet.getFfrozen()) * highestBuyPrice;

            }
        }
        Fvirtualwallet ethWallet = fvirtualwallets.get(Constants.BTC_WALLET_ID);
        asset += (ethWallet.getFtotal() + ethWallet.getFfrozen());
        getSession().setAttribute(Constants.USER_ASSET, asset);
        getSession().setAttribute(Constants.USER_ASSET_TIME, System.currentTimeMillis());
        return forSuccessResult(asset);
    }

    /**
     * 币币交易Json
     *
     * @return
     */
    @RequestMapping("/v1/deal_data")
    public Object indexJson(@RequestParam(required = false, defaultValue = "10") int size){
        List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
        List<LatestDealData> ajaxDatas = new ArrayList<>(dealDatas);

        for (Iterator<LatestDealData> iter = ajaxDatas.iterator(); iter.hasNext();) {
            LatestDealData dealData = iter.next();
            if (!dealData.isHomeShow()) {
                iter.remove();
            }
        }

        ajaxDatas.sort((o1, o2) -> {
            int val;
            if(o1.getHomeOrder() == 0 && o2.getHomeOrder() != 0){
                val = 1;
            }else if(o1.getHomeOrder() != 0 && o2.getHomeOrder() == 0){
                val = -1;
            } else {
                val = Integer.valueOf(o1.getHomeOrder()).compareTo(o2.getHomeOrder());
            }
            return val;
        });
        return forSuccessResult(ajaxDatas.subList(0, Math.min(size, ajaxDatas.size())));
    }


    /**
     * banner图
     *
     * @return
     */
    @RequestMapping("/v1/indexBanner")
    public Object indexBanner(){
        List imgs = new ArrayList<>(10);
        for(int i=1; i<=10; i++){
            String img = constantMap.getString("bigImage" + i);
            if(StringUtils.hasText(img) && !"#".equals(img)){
                Map map = new HashMap<>();
                map.put("img_url", img);
                imgs.add(map);
            }
        }
        return forSuccessResult(imgs);
    }

    /**
     * 获取交易币信息
     *
     * @param symbol
     * @return
     */
    @RequestMapping("/v1/coinTradeInfo")
    public Object coinTradeInfo(int symbol){
        LatestDealData dealData = realTimeDataService.getLatestDealData(symbol);

        Map map = new HashMap<>();
        if(null != dealData){
            map.put("fid", dealData.getFid());
            map.put("fname", dealData.getFname());
            map.put("group", dealData.getGroup());
            map.put("fShortName", dealData.getfShortName());
            map.put("furl", dealData.getFurl());
        }

        return forSuccessResult(map);
    }

    @RequestMapping("/v1/webInfo")
    public Object websiteInfo(){
        Map map = new HashMap<>();
        String[] arr = new String[]{
            "email",
            "qq",
            "logo",
            "logo_opacity",
            "param",
            "cn_title",
            "cn_keywords" ,
            "cn_desc" ,
            "cn_copyRight" ,
            "en_title" ,
            "en_keywords" ,
            "en_desc" ,
            "en_copyRight"
        };
        for (int i = 0; i< arr.length; i++){
            map.put(arr[i], constantMap.getString(arr[i]));
        }
        map.putAll((Map)constantMap.get("frontMap"));
        return forSuccessResult(map);
    }

    @RequestMapping("/v2/webInfo")
    public Object webInfo(){
        return forSuccessResult(constantMap.get("frontMap"));
    }

    private void sendCode(String name, String areaCode) {
        if(StringUtils.isEmail(name)){
            AuthCode authCode = new AuthCode(name, Utils.randomInteger(6), CountLimitTypeEnum.EMAIL);
            setAuthCode(authCode);
            Fvalidateemail fvalidateemail = new Fvalidateemail();
            fvalidateemail.setEmail(name);
            fvalidateemail.setFtitle("邮箱验证码");
            Map params = new FluentHashMap<>().
                    fluentPut("templ", constantMap.get("emailTemplate")).
                    fluentPut("fromName", constantMap.get("fromName")).
                    fluentPut("params",
                            new FluentHashMap<>().fluentPut("to", new String[]{name}).
                            fluentPut("sub",
                                    new FluentHashMap<>().fluentPut("%code%", new String[]{authCode.getCode()})));
            fvalidateemail.setFcontent(JSON.toJSONString(params));
            fvalidateemail.setFcreateTime(authCode.getDateTime());
            fvalidateemail.setFstatus(ValidateMailStatusEnum.Not_send);
            fvalidateemail.setFuser(getFuser());
            this.frontValidateService.addFvalidateemail(fvalidateemail);
        }else{
          AuthCode authCode = new AuthCode(name, Utils.randomInteger(6), CountLimitTypeEnum.TELEPHONE);
            setAuthCode(authCode);
            // 保存发送记录
            Fvalidatemessage fvalidatemessage = new Fvalidatemessage() ;
            if((areaCode+"").indexOf("86") != -1){
                fvalidatemessage.setFcontent(constantMap.getString("messageSign").replace("{s6}", authCode.getCode()));
            }else{
                fvalidatemessage.setFcontent(constantMap.getString("messageEnSign").replace("{s6}", authCode.getCode()));
            }
            fvalidatemessage.setFcreateTime(authCode.getDateTime()) ;
            fvalidatemessage.setFphone(areaCode + name) ;
            fvalidatemessage.setFstatus(ValidateMessageStatusEnum.Not_send) ;
            this.frontValidateService.addFvalidateMessage(fvalidatemessage) ;
        }
    }


    @RequestMapping("/v1/account/sendCode")
    public Object sendVerify() {
        Fuser fuser = getFuser();
        sendCode(fuser.getFloginName(), fuser.getFareaCode());
        return forSuccessResult();
    }

    @RequestMapping("/v1/account/sendBindCode")
    public Object sendBindCode(@RequestParam(required =true)String name, Integer check, String areaCode) {
        if(org.apache.commons.lang3.StringUtils.isEmpty(name)){
            return forFailureResult(1);
        }
        if(check != null ){
            if(userNameIsExist(name)){
                return forFailureResult(4);
            }
            sendCode(name, areaCode);
        }else{
            sendCode(name, getFuser().getFareaCode());
        }
        return forSuccessResult();
    }

    @RequestMapping("/v1/sendLoginCode")
    public Object sendLoginCode() {
        Fuser fuser = getValidateFuser();
        if (Objects.isNull(fuser)) {
            return forFailureResult(1);//非法操作
        }
        sendCode(fuser.getFloginName(), fuser.getFareaCode());
        return forSuccessResult();
    }

    @RequestMapping("/v1/sendRegCode")
    public Object sendRegCode(@RequestParam String name, @RequestParam String code, String areaCode) {
        if (!StringUtils.isEmail(name) && !StringUtils.isMobile(name)) {
            return forFailureResult(2);//不是正确的手机或邮箱
        }
        AuthCode authCode = getImageAuthCode();
        if (Objects.isNull(authCode) || !authCode.imageCodeIsEnabled(code)) {
            return forFailureResult(3); // 图形验证码不正确
        }
        if (userNameIsExist(name)) {
            return forFailureResult(4);// 邮箱已存在
        }
        sendCode(name, areaCode);
        removeImageAuthCode();
        return forSuccessResult();
    }

    @RequestMapping("/v1/sendFindCode")
    public Object sendFindCode(@RequestParam String name, String areaCode) {
        if (!StringUtils.isEmail(name) && !StringUtils.isMobile(name)) {
            return forFailureResult(2);//不是正确的手机或邮箱
        }
        Fuser fuser = frontUserService.findByLoginName(name);
        if (Objects.isNull(fuser)) {
            return forFailureResult(5);//该账号不存在
        }
        if (UserStatusEnum.FORBBIN_VALUE == fuser.getFstatus()) {
            return forFailureResult(6);//您已被禁用，请主动联系客服
        }
        sendCode(name, fuser.getFareaCode());
        return forSuccessResult();
    }

    @RequestMapping("/v1/coins")
    public Object coins() {
        List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
        HashMap map = null;
        List list = new ArrayList(dealDatas.size());
        for (LatestDealData dealData : dealDatas) {
            if (VirtualCoinTypeStatusEnum.Abnormal == dealData.getStatus()) {
                continue;
            }
            if (Objects.isNull(map)) {
                map = new HashMap();
            } else {
                map = (HashMap) map.clone();
            }
            map.put("id", dealData.getFid());
            map.put("name", dealData.getfShortName());
            map.put("key", dealData.getGroup());
            map.put("url", dealData.getFurl());
            map.put("order", dealData.getTotalOrder());
            list.add(map);
        }
        return forSuccessResult(list);
    }

    @ResponseBody
    @RequestMapping("/trend")
    public Object priceTrend(){
        Map<String, List<Object[]>> trendMap = realTimePriceService.getHourPriceTrendData();
        return trendMap;
    }

    @RequestMapping("/v1/coinList")
    public Object coinList() {
        List<Fvirtualcointype> coins = frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
        HashMap map = null;
        List list = new ArrayList(coins.size());
        for (Fvirtualcointype coin : coins) {
            if (Objects.isNull(map)) {
                map = new HashMap();
            } else {
                map = (HashMap) map.clone();
            }
            map.put("id", coin.getFid());
            map.put("name", coin.getfShortName());
            map.put("isRecharge", coin.isFIsRecharge());
            map.put("isWithdraw", coin.isFIsWithDraw());
            map.put("confirmations", coin.getConfirmTimes());
            list.add(map);
        }
        return forSuccessResult(list);
    }

    @RequestMapping("/v1/indexArticle")
    public Object indexArticle(@RequestParam int[] types, @RequestParam(required = false, defaultValue = "10") int size) {
        Map map = new HashMap();
        for (int i = 0; i < types.length; i++) {
            map.put(types[i], realTimeArticleService.getArticleList(types[i], size));
        }
        return forSuccessResult(map);
    }

    @RequestMapping("/v1/articleList")
    public Object articleList(@RequestParam int type, @RequestParam(required = false, defaultValue = "1") int page, int pageSize) {
        List<Farticle> list = articleService.findFarticle(type, (page - 1) * pageSize, pageSize);
        List<ArticleItemDTO> items = ArticleItemDTO.convert(list);
        int totalCount = this.articleService.findFarticleCount(type);
        return forSuccessResult(items, totalCount);
    }

    @RequestMapping("/v1/articleDetail")
    public Object articleDetail(@RequestParam int id) {
        Farticle farticle = articleService.findById(id);
        if (Objects.isNull(farticle)) {
            return forFailureResult(101);
        }
        ArticleDTO dto = ArticleDTO.convert(farticle);
        return forSuccessResult(dto);
    }

    @RequestMapping("/v1/version")
    public Object articleDetail(@RequestParam String version, @RequestParam String platform) {
        String[] updateVersion;
        if("IOS".equals(platform)){
            updateVersion = constantMap.getString("iosVersion").split(",");
        }else{
            updateVersion = constantMap.getString("androidVersion").split(",");
        }
        Map map = new HashMap();
        map.put("link", updateVersion[2]);
        String lang = Utils.getCookie(getRequest().getCookies(), "lang");
        if("cn".equals(lang)){
            map.put("content", updateVersion[3]);
        }else {
            map.put("content", updateVersion[4]);
        }
        if(version.compareTo(updateVersion[0]) < 0){
            map.put("status", 2);
        }else if(version.compareTo(updateVersion[1]) < 0){
            map.put("status", 1);
        }else {
            map.put("status", 0);
        }
        return forSuccessResult(map);

    }
}




