package com.trade.util;

public interface Constants {

	public boolean isRelease = true ;//must change when release
	public String WEBROOT = Configuration.getInstance().getValue("WEBROOT") ;
	public String Domain = Configuration.getInstance().getValue("Domain") ;
	public String GoogleAuthName = Configuration.getInstance().getValue("GoogleAuthName") ;
	public int RobotID = Integer.parseInt(Configuration.getInstance().getValue("RobotID")) ;
	public boolean ConnectUserDbFlag = Boolean.parseBoolean(Configuration.getInstance().getValue("ConnectUserDbFlag")) ;
	public String AdminUrl = Configuration.getInstance().getValue("AdminUrl") ;
	public String AdminAllowIp = Configuration.getInstance().getValue("AdminAllowIp") ;

	public boolean closeLimit = false ;
	/*
	 * 分页数量
	 * */
	// 统一使用
	int PAGE_ITEM_COUNT_10 = 10;
	int PAGE_ITEM_COUNT_20 = 20;
	int PAGE_ITEM_COUNT_30 = 30;
	int PAGE_ITEM_COUNT_40 = 40;

	public int CapitalRecordPerPage = 20 ;//充值记录分页
	public int QuestionRecordPerPage = 20 ;//问题记录分页
	public int EntrustPlanRecordPerPage = 20 ;//计划委托分页
	public int EntrustRecordPerPage = 20 ;//委托分页
	public int TradeRecordPerPage = 20 ;//账单明细分页
	public int GameLogPerPage = 10 ;//游戏记录
	public int DeductPerPage = 20 ;//游戏记录
	public int LendEntrustRecordPerPage = 15 ;//借贷功能分页
	public int LotteryEggRewardPerPage = 20 ;//砸金蛋获奖记录单页
	public int ZhongDouPerPage = 20 ;//借贷功能分页
	public int IntrolPerPage = 5 ;//app分页数量
	public int AppRecordPerPage = 10 ;//app分页数量
	public int PayCodeRecordPerPage = 10 ;//app分页数量
	public int ShopLogPerPage = 12 ;//app分页数量
	public int DivideCountPerPage = 10 ;//app分页数量
	public int BalanceFlowPerPage = 10 ;//app分页数量


	public boolean TradeSelf = true ;//
	public boolean CombinedDepth = true ;//

	public String AdminShopDirectory = "upload"+"/"+"shop" ;
	public String AdminUploadDirectory = "upload"+"/"+"others" ;
	public String AdminUploadInformationDirectory = "upload"+"/"+"information" ;
	public String IdentityPicDirectory =  "upload"+"/"+"identity_picture" ;
	public String AdminArticleDirectory = "upload"+"/"+"admin_article" ;
	public String HeadImgDirectory = "upload"+"/"+"head_img" ;
	public String DataBaseDirectory = "upload"+"/"+"dataBase" ;
	public String EmailReg = "^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";//邮箱正则
	public int ErrorCountLimit = 10 ;//错误N次之后需要等待2小时才能重试
	public int ErrorCountAdminLimit = 5 ;//后台登陆错误

	public static int task_virtualcoin_id = 6 ;//完成任务奖励虚拟币类型

	// Mongo Collection Name
	public static final String FENTRUST_DATA = "FentrustData";                      // 委托单数据
	public static final String FENTRUST_LIMIT_DATA = "FentrustLimitData";
	public static final String FPERIOD_DATA = "FperiodData";                        // K线数据
	public static final String ONE_MINITE_DATA = "OneMiniteData";                   // 当前分钟K线数据

	// Redis cache Key
	public static final byte[] REDIS_CACHE_LAST = "cache:latest".getBytes();
	public static final byte[] REDIS_CACHE_COUNT = "cache:count".getBytes();
	public static final byte[] REDIS_CACHE_LAST_UPDATE_TIME = "cache:lastUpdateTime".getBytes();

	public final static String WECHAT_ORDER_NO_PREFIX = "10000";      // 商户订单号前缀

	String UTF8_CHARSET = "UTF-8";

	//Redis Weixin Article
	public static final String REDIS_WEIXIN_ARTICLE = "cache:weixin";
	// 3天价格趋势前缀
	String THREE_DAY_PRICE_TREND = "cache:trend:price:3d";
	// 文章列表缓存key前缀
	String CMS_CONTENT_LIST_PREFIX = "cache:cms:list:";

	// 验证码过期时间
	int CAPTCHA_TIME_OUT = 30 * 60 * 1000;
	
	// user session
	String USER_LOGIN_SESSION = "login_user";
	
	// second login session
	String USER_SECOND_LOGIN_SESSION = "second_login_user";
	
	// 表单token
	String FORM_TOKEN = "form_token";
	
	// 验证码
	String SESSION_CAPTCHA_CODE = "captcha";

	// 找回密码用户
	String FIND_PASSWORD_USER = "password_user";

	// 跳转url
	String FORWARD_URL = "forward_url";

	// 推广
	String INTRO_COOKIE = "_intro_";

	// 资产估值
	String USER_ASSET = "user_asset";

	// 资产时间
	String USER_ASSET_TIME = "user_asset_time";

	// 第三方登录用户session信息key
	String APP_OPEN_USER_INFO = "appOpenUserInfo";

	// 用户id
	String USER_ID = "user_id";
	//自选交易对  收藏
	int collectSelf =1;
	//自选交易对  不收藏
	int noCollectSelf=0;

	String WECHAT_ACCESS_TOKEN = "wechat:access:token";

	// 微信openid
	String WECHAT_OPEN_ID = "wechat_openid";

	// 非会员发送验证码
	String NONMEMBER_SEND_CODE = "nonmember_send_code";

	String IMAGE_CODE_KEY = "checkcode";

	//登录验证
	String USER_LOGIN_VALIDATE_SESSION  = "login_validate_user";
	String LOGIN_COOKIE  = "login_device";


	String LOGIN_ADMIN = "login_admin";
	int BTC_WALLET_ID = 1;

	//kst
	String BIGIMAGE = "bigImage";
	String ENBIGIMAGE = "enBigImage";
	String MOD_EMAIL_SESSION = "mod_email_session";
	String MOD_MOBILE_SESSION = "mod_mobile_session";
	String KLINE_SESSION="kline_session";
}
