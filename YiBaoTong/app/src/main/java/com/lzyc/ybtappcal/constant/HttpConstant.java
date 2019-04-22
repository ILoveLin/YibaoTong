package com.lzyc.ybtappcal.constant;

import com.lzyc.ybtappcal.util.MD5ChangeUtil;

/**
 * 网络相关的静态常量类
 */
public class HttpConstant {
//     public static final String BASE_URL = "http://ybt.yibaotongapp.com";
    public static final String BASE_URL = "http://new.yibaotongapp.com";    //内测
    public static final String SINA_SHARE_URL="http://sns.whalecloud.com";
    //api
    public static final String APP_URL = BASE_URL + "/api";
    public static final String HEALTH_URL = BASE_URL + "/StatPage/index?";      //医保计算器
    public static final String APP_SECRET = "yibaotongapi";                     //秘钥
    public static final String ZHIFUBAO_APPID = "2017031006147400";
    /**
     * 支付宝RSA私钥
     */
    public static final String ZHIFUBAO_RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCD59DLlG9WKcO7dXU2URLBPvXjbDY7hMuJynkoftW9e2OJOpQtF+foixWXuMiEvyBzBp0dPXx4q0ZaKDhAmSE48Vz2wiILrkxKwPSkZ8JB2TugKyHgcR/xF7LlIkpbW2B3ZwgKjaWaCfjTRfkOXaAQZcAdSFcD2u1Jj7Akh03Sc9/JQ2lVxE34JsuYVR/VxDA1VjTZLwRqO/lTQTeu2tTUqdDNnbkZHEjCR1OLkC8elwGY+MxJ9rSr0mdoCic9CiUYLl6Bw/g1CR+b3V47K8BJgZ4FpQNP0PgKbEWV8YZV8udWDbuuSpN9Os3rjirrNq4Tc2bI4HvioSe/pdmyi31bAgMBAAECggEASCadRz5CSFdAAWIWY0elgp710Rh38caJry1ZaJU4RRUhNQcd1iylZcXuYbdpt3L0RPbxKfVgCIFnUhl965fXXFZqC2/VP55vjNRO/T7kOzCgNNRjC2nVlwLnj/5Zve8VrTpmY59nXgLmYGp9+SWtUErX8yyGrE0sTniSWOjJ4LtH76XRWyGOsntpl4xyhR01XRKXM6s1nIoVt22Dag6KiAECR8nlLDAE2XStiEgkDy0Ebfz2vLOGW4uS5HBywMSoBUu+E8JFg0O7sswZgd0B7JSGTOcAu18vphDWnyRZX6PyKejqygWvCXX9zCWDfXaYVFKp+t6D56OPloP6HCGMAQKBgQDCJSTXKh/zwrPRuUBLhQb4F5shsBFDAcpGKhDnMlLpLHzxSyuwa3xuFsEgMEiMNOkQEl9V9egACQrJaCEy0PGKHXArjOpTCczzym6K8fleC5jxZUdOt+qt0YxBIu3mQf3ziJfaDPYq1mW8nv7n4FYQRY+Dyg13CFWkO+E8Xxw7wQKBgQCt7kpTUhHblGJhFZj03k853vHyRB8eEyasAkKTY40WLBA8DfwGWX3pV4g1F5jmycsN10HZGCs/snoLsHmrzRt0zXgTidfO1ZaO8wbf+70u0DrrzVjAhD9VjbW1Jx/JaWKvuAPCTUm3yHIuP66Dv+hm4aLFP4yWFzMRS/vMOp0wGwKBgDg82jcZlLffpocRynYVyrcC34Uao2Vtl1JLzSmrgijZIjgjNjycZibdoIPaeKlkij6Fk+gqm1GnTOCasUMvE0iWzm9PGnGDXUMhWHQXM8zyPxNt6wH6dJh1ItObcp5Ib2+kGZioOIagJEhHJj+01DlSbQgX916Lo4M3dT8vrdYBAoGAMqRxS7VXscGQq4foU1PvaYA6njmrfcxBtE+sJtIToGMQDwxbV1XzTV/JLvxB7uDOmKcWaymYXiGPW5v0sUs1NVsmHTpDddvGzLSCSfE4ckr6/PF7aAKZlkV+pNJRdQmUTGmd3pM2ZaadaE3ueDoGZv5HsHsCaikzG1QuapaecgUCgYEAg7+cy6+Zplm7EI0agEiEErV4eU6AvGxiNmyW7rU+IFOkcx2+pKj4YuJX3PJ+hKlhCOnjmI8tL4AuySqEP4IJCsSGIjmmqo4ljOfaUt7p1EH0/wV8CjlOi19jqHNk0bhWWemgfv3Gdg/qhqvVlgFM9Q2oR40ZMedX8AvL286bgxY=";
    public static final String WEXIN_PAYMENT_REQUEST = "https://api.mch.weixin.qq.com/pay/unifiedorder";//微信支付请求接口
    public static final String WX_CALL_BACK = BASE_URL + "/wxpayCallback.php";
    public static final String ZFB_CALL_BACK = BASE_URL + "/alipayNotify.php";

    //登录注册
    public static final int REGISTER = 12;
    public static final int SET_PW = 13;
    public static final int GET_CODE = 14;
    public static final int FULL_INFO = 15;
    public static final int GET_REGION = 16;
    public static final int GET_CITY = 17;

    //首页
    public static final int SEARCH_RECOMMEND = 21;
    public static final int SEARCH_TOP = 22;
    public static final int TOP_SCAN_RESULT_ADD_PLAN = 26;
    public static final int TOP_DRUG_DETAIL = 27;
    public static final int TOP_CONFIRM = 29;
    public static final int TOP_HOSPITAL = 30;
    public static final int TOP_HOSPITAL_DETAIL = 31;
    public static final int TOP_CITY_LIST = 32;
    public static final int TOP_SCAN_INFO = 33;
    public static final int TOP_CLICKSEARCH_RECOMMENDED = 34;
    public static final int TOP_COMMENT_HOSPITAL = 10010;//基数从10000开始，create by yang
    public static final int TOP_COMMENT_HOSPITAL_LIST = 10011;
    public static final int TOP_COMMENT_HOSPITAL_REPLY_LIST = 10012;    //评价详情界面 数据
    public static final int TOP_COMMENT_HOSPITAL_LIST_LIKE = 10020;
    public static final int TOP_COMMENT_HOSPITAL_LIST_ONE = 10021;
    public static final int TOP_COMMENT_HOSPITAL_LIST_TWO = 10022;      //评价详情界面 交流(回复评价)
    public static final int TOP_COMMENT_HOSPITAL_LIST_HEADER_THREE = 10030;    //评价详情界面    头布局评论
    public static final int TOP_COMMENT_HOSPITAL_LIST_HEADER_LIKE = 10031;      //评价详情界面    头布局点赞
    public static final int TOP_RESULTS_INFO = 10033;
    public static final int MESSAGE_LIST_DYN = 20001;//评论动态
    public static final int MESSAGE_NICKNAME = 30000;//昵称检查
    public static final int BIYAO_BANNER = 40000;//比药的banner


    //异地报销
    public static final int REMOTE_RECOMMEND = 40;
    public static final int REMOTE_PROVINCE = 41;
    public static final int REMOTE_CITY = 42;
    public static final int REMOTE_HOSPITAL_CITY = 43;
    public static final int REMOTE_HOSPITAL_SEARCH = 44;
    public static final int REMOTE_DRUGS_SEARCH = 45;
    public static final int REMOTE_DRUGS_SAVE = 46;

    //推荐模块
    public static final int RECOMMEND_LIST = 60;
    public static final int RECOMMEND_LIST_EMPTY = 61;

    //个人中心
    public static final int MY_INFO = 70;
    public static final int MY_ADDRESS_LIST = 71;
    public static final int MY_ADDRESS_DELETE = 72;
    public static final int MY_ADDRESS_ADD = 73;
    public static final int MY_BIND_PHONE = 74;
    public static final int MY_CHANGE_PASSWORD = 75;
    public static final int MY_FEEDBACK = 76;
    public static final int MY_MESSAGE_LIST = 77;
    public static final int MY_DELETE_MESSAGE = 78;
    public static final int MY_GET_MESSAGE = 87;
    public static final int MY_PLAN_LIST = 80;
    public static final int MY_AREA_LIST = 81;
    public static final int MY_AREA_PLAN_LIST = 82;
    public static final int MY_DELETE_PLAN = 83;
    public static final int MY_DRUG_TYPE = 84;
    public static final int MY_DRUG_LIST = 85;
    public static final int MY_DRUG_DELETE = 86;
    //三级列表    热门城市
    public static final int HOT_CITY = 100;

    //订单
    public static final int BUY_ORDERBY = 1001;//订单详情
    public static final int BUY_ORDERBY_02 = 1102;//订单详情
    public static final int BUY_CHECK = 1002;//检查订单
    public static final int BUY_SUBMIT = 1003;//提交订单
    public static final int BUY_SUBMIT_SHOP_CARD = 1302;//提交订单
    public static final int BUY_SUBMIT_SECOND = 1004;//订单二次验证
    public static final int WHTHDRAW = 1005;//提现
    public static final int WHTHDRAW_LIST = 1015;//提现列表
    public static final int WHTHDRAW_RETURN = 1007;//返现记录
    public static final int BAOXIAO_INFO = 1009;//报销详情
    public static final int ADOPT_HOSPITAL = 1008;//报销详情


    public static final int HOME_PAGE_LIST = 1006;//首页列表
    public static final int HOME_PAGE_LIST2 = 1602;//首页列表
    public static final int HOME_PAGE_BANNER = 1010;//首页列表
    public static final int BUY_ORDERBY_ADD_SHOP_CARD = 1100;//加入购物车
    public static final int HOME_TYPE_ALL = 1011;//首页-->全部分类
    public static final int HOME_TYPE = 1012;//首页-->分类
    public static final int BUY_ORDERBY_SHOP_CARD_NUM = 1013;//购物车数量

    public static final int HOME_MEDICINE_CHEST_MEMBER_LIST = 1014;//家庭药箱--家庭成员
    public static final int HOME_MEDICINE_CHEST_MEMBER_DEL = 1015;//家庭药箱--删除成员
    public static final int HOME_MEDICINE_CHEST_MEMBER_ADD = 1016;//家庭药箱--添加成员
    public static final int HOME_MEDICINE_LIST = 1017;//家庭药箱--药品列表
    public static final int HOME_MEDICINE_DEL = 1018;//家庭药箱--删除药品

    public static final int HOME_SERCH = 1100;//家庭药箱--删除药品
    public static final int RESULTS_RANKING = 1200;//查询结果
    public static final int DRUG_CURRENT = 1201;//当前药品
    public static final int ORDER_ADDRESS_MANAGER = 2000;//地址管理
    public static final int ORDER_ADDRESS_ADD = 2001;//地址添加
    public static final int ORDER_ADDRESS_CLEAR = 2002;//地址删除
    public static final int ORDER_ADDRESS_SETDEFAULT = 2003;//地址设置默认
    public static final int ORDER_ADDRESS_EDIT = 2004;//地址编辑
    public static final int ORDER_MINE_LIST = 3000;//我的订单列表
    public static final int ORDER_MINE_CANCEL = 3001;//我的订单,取消订单
    public static final int ORDER_MINE_DELETE = 3002;//我的订单,删除订单
    public static final int ORDER_PAYMENT_QUERENSHOUHUO = 3003;//我的订单,确认收货
    public static final int ORDER_MINE_DETAIL = 3004;//我的订单,订单详情
    public static final int ORDER_PAYMENT_UPDATE = 3005;//；立即支付
    public static final int MINE_INFO_REFUND = 4000;//个人信息，返现
    public static final int MINE_BROWSE_HISTORYLIST = 4010;//我的浏览记录列表
    public static final int MINE_BROWSE_DELETE = 4011;//删除浏览记录
    public static final int MINE_DURG_BAG_ADD = 4020;//我的药箱添加用药
    public static final int SHOP_CART_LIST = 5000;//购物车列表
    public static final int SHOP_CART_DEL = 5001;//购物车，删除
    public static final int GOODS_SERACH_DIM = 6000;//商品的联想搜索
    public static final int GOODS_SERACH_LIST = 6001;//商品搜索列表
    public static final int GOODS_SERACH_HOT = 6002;//商品热门搜索标签
    public static final int SEARCH_DRUG_LIST = 7000;//药品搜索
    public static final int SEARCH_DRUG_DIM = 7001;//药品联想搜索
    public static final int SEARCH_DRUG_HOT = 7002;//药品热门搜索标签
    public static final int ADVERTISEMENT = 7003;//广告
    public static final int MESSAGE_LIST_NOREAD = 8000;//未读消息
    public static final int MESSAGE_LIST_SYS = 8001;//系统消息列表
    public static final int MESSAGE_DELETE = 8003;//系统消息删除
    public static final int DESIGNATED_NEAR_LIST = 9000;//选择定点医院，获取附近
    public static final int DESIGNATED_ADD = 9001;//选择定点医院
    public static final int DESIGNATED_DIM_LIST = 9002;//选择定点医院搜索的联想词
    public static final int DESIGNATED_LIST = 9003;//定点医院列表
    public static final int DESIGNATED_DELETE = 9004;//定点医院删除
    public static final int TOP_SCAN_RESULT = 9010;
    public static final int RESULTS_HOSPITAL_LIST = 9011;//社区医院列表
    public static final int FEEDBACK_HELP = 9012;//帮助与反馈
    public static final int LOGIN = 9100;

    //关键字：param 网络提交参数 key
    public static String PARAM_KEY_SIGN = "sign";
    public static String PARAM_KEY_APP = "app";
    public static String PARAM_KEY_CLASS = "class";
    public static String PARAM_KEY_CODE = "code";
    public static String PARAM_KEY_CODE2 = "Code";
    public static String PARAM_KEY_PHONE = "phone";
    public static String PARAM_KEY_PHONE_NEW = "newPhone";
    public static String PARAM_KEY_PHONE2 = "Phone";
    public static String PARAM_KEY_PWD= "password";
    public static String PARAM_KEY_TOKEN = "token";
    public static String PARAM_KEY_KEYWORD = "keyword";
    public static String PARAM_KEY_KEYWORD2 = "Keyword";
    public static String PARAM_KEY_PAGEINDEX = "pageIndex";
    public static String PARAM_KEY_PAGEINDEX2 = "PageIndex";
    public static String PARAM_KEY_ID_DRUG = "drug_id";
    public static String PARAM_KEY_ID_DRUG2 = "DrugID";
    public static String PARAM_KEY_ID_DRUGNAME = "DrugNameID";
    public static String PARAM_KEY_ID_SPECIFICATIONS = "SpecificationsID";
    public static String PARAM_KEY_ID_VENDER = "VenderID";
    public static String PARAM_KEY_PROVINCE = "province";
    public static String PARAM_KEY_PROVINCE2 = "Province";
    public static String PARAM_KEY_LAT = "lat";
    public static String PARAM_KEY_LAT2 = "currentLat";
    public static String PARAM_KEY_LNG = "lng";
    public static String PARAM_KEY_LNG2 = "currentLng";
    public static String PARAM_KEY_TYPE = "type";
    public static String PARAM_KEY_TYPE2 = "Type";
    public static String PARAM_KEY_YBTYPE = "ybtype";
    public static String PARAM_KEY_OS = "os";
    public static String PARAM_KEY_AREA = "area";
    public static String PARAM_KEY_STREET = "street";
    public static String PARAM_KEY_YIBAO = "yibao";
    public static String PARAM_KEY_SORT = "sort";
    public static String PARAM_KEY_UUID = "uuid";
    public static String PARAM_KEY_EVENT_CODE = "eventCode";
    public static String PARAM_KEY_IMG = "img";
    public static String PARAM_KEY_ADDRESS_REGION = "AddressRegion";
    public static String PARAM_KEY_ID = "ID";
    public static String PARAM_KEY_DEFAULT= "Default";

    public static final String MSG = "msg";
    public static final String STATUS = "status";
    public static final String DATA = "data";
    public static final String IS_MESSAGE = "is_message";
    public static final String INFO = "Info";
    public static final String COUNT = "Count";
    public static final String CURRENT = "Current";
    public static final String LIST = "List";
    public static final String LIST2 = "list";
    public static final String AREA_AND_STREET = "areaAndStreet";
    public static final String HOSLIST = "hosList";
    public static final String ORDER_INFO= "OrderInfo";
    public static final String ADDRESS_INFO= "AddressInfo";
    public static final String MERCHANT_INFO= "MerchantInfo";
    public static final String GOODSLIST= "GoodsList";
    public static final String STATUS_CODE= "statusCode";
    public static final String MESSAGE= "message";

    //接口字段
    public static final String APP_UID = "UID";
    public static final String IDS = "IDS";
    public static final String ID = "ID";
    public static final String ID_SMALL = "id";
    public static final String DEFAULT = "Default";
    public static final String OS_ANDROID = "android";
    public static final String PASSWD = "passwd";
    public static final String PASSWD_OLD = "oldpasswd";
    public static final String PARAM_KEY_NICKNAME = "nickname";
    public static final String PARAM_KEY_SEX = "sex";
    public static final String PARAM_KEY_AGE = "age";
    public static final String PARAM_KEY_CITYID = "cityId";
    public static final String PARAM_KEY_REGIONID = "regionId";
    public static final String PARAM_KEY_DELTYPE = "DelType";
    public static final String PARAM_KEY_UID = "UID";
    public static final String PARAM_KEY_NAME = "Name";
    public static final String PARAM_KEY_ADDRESS_DETAIL = "AddressDetail";
    public static final String PARAM_KEY_ADDRESS_DEFAULT= "Default";
    public static final String PARAM_KEY_ORDERID= "OrderID";
    public static final String PARAM_KEY_PAYTYPE= "PayType";
    //
    public static final String APP_UCENTER = "Ucenter";
    public static final String APP_USER = "User";
    public static final String APP_STATS = "Stats";

    public static final String STATS_EVENT_CLZ = "EventStats";
    public static final String STATS_EVENT_SIGN = MD5ChangeUtil.Md5_32(APP_STATS + STATS_EVENT_CLZ + APP_SECRET);

    /**
     * 商品
     */
    public static final String APP_SHOP = "Shop";//商品
    public static final String APP_HOME = "Home";
    //商品首页列表
    public static final String SHOP_HOME_CLZ = "GoodsHomePageList";
    public static final String SHOP_HOME_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_HOME_CLZ + APP_SECRET);

    /**
     * 新首页 2017/5/12
     */
    public static final String SHOP_HOME_CLZ2 = "GoodsHomePageList2";
    public static final String SHOP_HOME_SIGN2 = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_HOME_CLZ2 + APP_SECRET);

    /**
     * 首页-->全部分类
     */
    public static final String SHOP_HOME_CATEGORY_CLZ = "GoodsGetCategory";
    public static final String SHOP_HOME_CATEGORY_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_HOME_CATEGORY_CLZ + APP_SECRET);

    /**
     * 首页-->通过分类或属性获取药品列表
     */
    public static final String SHOP_HOME_GOODS_DRUG_LIST = "GoodsDrugList";
    public static final String SHOP_HOME_GOODS_DRUG_LIST_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_HOME_GOODS_DRUG_LIST + APP_SECRET);

    public static final String SHOP_HOME_BANNER_CLZ = "PageStyle";
    public static final String SHOP_HOME_BANNER_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + SHOP_HOME_BANNER_CLZ + APP_SECRET);
    public static final String SHOP_HOME_PARAM_OS = "os";
    public static final String SHOP_HOME_PARAM_NAME = "name";

    public static final String SHOP_HOME_PARAM_PAGE_ANDROID = "android";
    public static final String SHOP_HOME_PARAM_PAGE_HOME_BANNER = "home_banner";
    //创建地址，添加地址
    public static final String SHOP_ADDRESS_ADD_CLZ ="AddressAdd";
    public static final String SHOP_ADDRESS_ADD_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_ADDRESS_ADD_CLZ + APP_SECRET);
    //编辑地址
    public static final String SHOP_ADDRESS_UPDATE_CLZ = "AddressUpdate";//编辑地址
    public static final String SHOP_ADDRESS_UPDATE_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_ADDRESS_UPDATE_CLZ + APP_SECRET);
    //地址列表，管理地址
    public static final String SHOP_ADDRESS_LIST_CLZ="AddressList";
    public static final String SHOP_ADDRESS_LIST_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_ADDRESS_LIST_CLZ + APP_SECRET);
    //删除地址
    public static final String SHOP_ADDRESS_DEL_CLZ="AddressDel";
    public static final String SHOP_ADDRESS_DEL_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_ADDRESS_DEL_CLZ + APP_SECRET);
    //设置默认地址
    public static final String SHOP_ADDRESS_DEFAULT_CLZ="AddressDefault";
    public static final String SHOP_ADDRESS_DEFAULT_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_ADDRESS_DEFAULT_CLZ + APP_SECRET);
    //删除订单
    public static final String SHOP_GOODS_ORDER_DELETE_CLZ="GoodsOrderDelete";
    public static final String SHOP_GOODS_ORDER_DELETE_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_GOODS_ORDER_DELETE_CLZ + APP_SECRET);
    //立即付款 支付方式修改
    public static final String SHOP_GOODS_ORDER_UPDATE_CLZ="GoodsOrderUpdate";
    public static final String SHOP_GOODS_ORDER_UPDATE_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_GOODS_ORDER_UPDATE_CLZ + APP_SECRET);
    //确认收货
    public static final String SHOP_GOODS_ORDER_COMPLETE_CLZ="GoodsOrderComplete";
    public static final String SHOP_GOODS_ORDER_COMPLETE_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_GOODS_ORDER_COMPLETE_CLZ + APP_SECRET);
    //获取订单详情
    public static final String SHOP_GOODS_ORDER_DETAIL_CLZ="GoodsOrderDetail2";
    public static final String SHOP_GOODS_ORDER_DETAIL_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_GOODS_ORDER_DETAIL_CLZ + APP_SECRET);
    //取消订单
    public static final String SHOP_GOODS_ORDER_CANCEL_CLZ="GoodsOrderCancel";
    public static final String SHOP_GOODS_ORDER_CANCEL_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_GOODS_ORDER_CANCEL_CLZ + APP_SECRET);

    //搜索
    public static final String HOME_SEARCH_CLZ = "Search3";
    public static final String HOME_SEARCH_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_SEARCH_CLZ + APP_SECRET);

    //联想搜索
    public static final String HOME_SEARCH_DIM_CLZ = "DimSearch";
    public static final String HOME_SEARCH_DIM_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_SEARCH_DIM_CLZ + APP_SECRET);

    public static final String HOME_GET_DURG_HOT_CLZ = "GetDrugsHot2";
    public static final String HOME_GET_DURG_HOT_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_GET_DURG_HOT_CLZ + APP_SECRET);


    //
    public static final String HOME_PAGE_STYLE_CLZ = "PageStyle";
    public static final String HOME_PAGE_STYLE_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_PAGE_STYLE_CLZ + APP_SECRET);

    //推荐
    public static final String HOME_RECOMMEND_CLZ = "Recommend";
    public static final String HOME_RECOMMEND_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_RECOMMEND_CLZ + APP_SECRET);

    //设置消息是否提醒
    public static final String UCENTER_SETMESSAGE_CLZ = "SetMessage";
    public static final String UCENTER_SETMESSAGE_SIGN = MD5ChangeUtil.Md5_32(APP_UCENTER + UCENTER_SETMESSAGE_CLZ + APP_SECRET);

    public static final String UCENTER_UPPHONE_CLZ = "UpPhone";
    public static final String UCENTER_UPPHONE_SIGN = MD5ChangeUtil.Md5_32(APP_UCENTER + UCENTER_UPPHONE_CLZ + APP_SECRET);

    public static final String UCENTER_UPPHONE_SENDSMS_CLZ = "UpPhoneSendSms";
    public static final String UCENTER_UPPHONE_SENDSMS_SIGN = MD5ChangeUtil.Md5_32(APP_UCENTER + UCENTER_UPPHONE_SENDSMS_CLZ + APP_SECRET);

    public static final String UCENTER_UPDATE_PASSWD_CLZ = "UpdatePasswd";
    public static final String UCENTER_UPDATE_PASSWD_SIGN = MD5ChangeUtil.Md5_32(APP_UCENTER + UCENTER_UPDATE_PASSWD_CLZ + APP_SECRET);


    //浏览记录
    public static final String HOME_BROWSE_HISTORYLIST_CLZ = "BrowseHistoryList";
    public static final String HOME_BROWSE_HISTORYLIST_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_BROWSE_HISTORYLIST_CLZ + APP_SECRET);
    public static final String HOME_BROWSE_DELETE_CLZ = "BrowseHistoryDel";
    public static final String HOME_BROWSE_DELETE_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_BROWSE_DELETE_CLZ + APP_SECRET);

    //添加用药
    public static final String HOME_DRUG_ADD_CLZ = "MedicineChestDetailAdd";
    public static final String HOME_DRUG_ADD_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_DRUG_ADD_CLZ + APP_SECRET);
    public static final String MEMBERID = "MemberID";
    public static final String DRUGID = "DrugID";

    //医院社区列表，刷选查看，导航
    public static final String HOME_RESULTS_HOSPITAL_CLZ = "ResultsHospital";
    public static final String HOME_RESULTSHOSPITAL_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_RESULTS_HOSPITAL_CLZ + APP_SECRET);

    //商品详情
    public static final String SHOP_GOODS_CLZ = "GoodsDrugDetail3";
    public static final String SHOP_GOODS_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_GOODS_CLZ + APP_SECRET);
    public static final String SHOP_GOODS_PARAM_DKID = "DKID";
    public static final String SHOP_GOODS_PARAM_DRUG_ID = "drug_id";

    //获取购物车数量
    public static final String SHOP_CARD_NUM_CLZ = "ShopingCartCount";
    public static final String SHOP_CARD_NUM_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_CARD_NUM_CLZ + APP_SECRET);

    //加入购物车
    public static final String SHOP_CARD_ADD_CLZ = "ShopingCartAdd";
    public static final String SHOP_CARD_ADD_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_CARD_ADD_CLZ + APP_SECRET);
    public static final String SHOP_CARD_ADD_GOODS_ID = "GoodsID";
    public static final String SHOP_CARD_ADD_COUNT = "GoodsCount";

    //检查订单
    public static final String SHOP_ORDER_CHECK_CLZ = "GoodsCheckOrder";
    public static final String SHOP_ORDER_CHECK_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_ORDER_CHECK_CLZ + APP_SECRET);
    public static final String SHOP_ORDER_PARAM_CHECK_UID = "UID";
    public static final String SHOP_ORDER_PARAM_CHECK_DKID = "DKID";

    //我的订单
    public static final String SHOP_GOODS_ORDER_LIST_CLZ = "GoodsOrderList2";
    public static final String SHOP_GOODS_ORDER_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_GOODS_ORDER_LIST_CLZ + HttpConstant.APP_SECRET);

    //家庭药箱--家庭成员列表
    public static final String HOME_MEDICINE_CHEST_MEMBER_LIST_CLZ = "MedicineChestMemberList";
    public static final String HOME_MEDICINE_CHEST_MEMBER_LIST_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_MEDICINE_CHEST_MEMBER_LIST_CLZ + APP_SECRET);

    //家庭药箱--删除成员
    public static final String HOME_MEDICINE_CHEST_MEMBER_DEL_CLZ = "MedicineChestMemberDel";
    public static final String HOME_MEDICINE_CHEST_MEMBER_DEL_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_MEDICINE_CHEST_MEMBER_DEL_CLZ + APP_SECRET);

    //家庭药箱--添加成员
    public static final String HOME_MEDICINE_CHEST_MEMBER_ADD_CLZ = "MedicineChestMemberAdd";
    public static final String HOME_MEDICINE_CHEST_MEMBER_ADD_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_MEDICINE_CHEST_MEMBER_ADD_CLZ + APP_SECRET);

    //家庭药箱--编辑成员
    public static final String HOME_MEDICINE_CHEST_MEMBER_EDIT_CLZ = "MedicineChestMemberEdit";
    public static final String HOME_MEDICINE_CHEST_MEMBER_EDIT_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_MEDICINE_CHEST_MEMBER_EDIT_CLZ + APP_SECRET);

    //家庭药箱  添加／编辑成员参数
    public static final String HOME_MEMBER_ADD_PARAM_NICKNAME = "Nickname";
    public static final String HOME_MEMBER_ADD_PARAM_AGE = "Age";
    public static final String HOME_MEMBER_ADD_PARAM_SEX = "Sex";
    public static final String HOME_MEMBER_ADD_PARAM_ALLERGY = "Allergy";
    public static final String HOME_MEMBER_ADD_PARAM_NOTE = "Note";

    //家庭药箱--药品列表
    public static final String HOME_MEDICINE_CHEST_DETAIL_LIST_CLZ = "MedicineChestDetailList";
    public static final String HOME_MEDICINE_CHEST_DETAIL_LIST_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_MEDICINE_CHEST_DETAIL_LIST_CLZ + APP_SECRET);
    public static final String HOME_MEDICINE_CHEST_DETAIL_LIST_MEMBERID = "MemberID";
    public static final String HOME_MEDICINE_CHEST_DETAIL_LIST_OFFSET = "Offset";//列表总数

    //家庭药箱--删除药品
    public static final String HOME_MEDICINE_CHEST_DEL_CLZ = "MedicineChestDetailDel";
    public static final String HOME_MEDICINE_CHEST_DEL_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + HOME_MEDICINE_CHEST_DEL_CLZ + APP_SECRET);

    //提交订单 -->  直接购买
    public static final String SHOP_SUBMIT_CLZ = "GoodsSubmitOrder";
    public static final String SHOP_SUBMIT_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_SUBMIT_CLZ + APP_SECRET);
    public static final String SHOP_SUBMIT_PARAM_TYPE = "Type";
    public static final String SHOP_SUBMIT_PARAM_DKID = "DKID";
    public static final String SHOP_SUBMIT_PARAM_COUNT = "Count";
    public static final String SHOP_SUBMIT_PARAM_ADDRESS_ID = "AddressID";
    public static final String SHOP_SUBMIT_PARAM_PAY_TYPE = "PayType";
    public static final String SHOP_SUBMIT_PARAM_BALANCE = "Balance";
    public static final String SHOP_SUBMIT_PARAM_PAY_PRICE = "PayPrice";
    public static final String SHOP_SUBMIT_PARAM_RETURN_MONEY = "ReturnMoney";
    public static final String SHOP_SUBMIT_PARAM_SHOPINGCART = "ShopingCart";
    public static final String SHOP_SUBMIT_PARAM_PHONE = "Phone";

    //提交订单 -->  购物车购买
    public static final String SHOP_SUBMIT_SHOP_CARD_CLZ = "GoodsSubmitOrder2";
    public static final String SHOP_SUBMIT_SHOP_CARD_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_SUBMIT_SHOP_CARD_CLZ + APP_SECRET);
    public static final String SHOP_SUBMIT_SHOP_CARD_TOTAL_PRICE = "TotalPrice";
    public static final String SHOP_SUBMIT_SHOP_CARD_GOODS_INFO = "GoodsInfo";

    //购物车
    public static final String SHOP_CART_LIST_CLZ = "ShopingCartList";//购物车列表
    public static final String SHOP_CART_DEL_CLZ = "ShopingCartDel";//购物车删除商品
    public static final String SHOP_CART_LIST_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_CART_LIST_CLZ + APP_SECRET);
    public static final String SHOP_CART_DEL_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_CART_DEL_CLZ + APP_SECRET);

    public static final String SHOP_ADDRESS_UPDATE_NAME = "Name";
    public static final String SHOP_ADDRESS_UPDATE_REGION = "AddressRegion";
    public static final String SHOP_ADDRESS_UPDATE_DETAIL = "AddressDetail";
    //首页搜索,购物相关搜索，商品搜索
    public static final String SHOP_SEARCH_GOODS_DIM_CLZ = "GoodsDrugDimSearch";//联想搜索
    public static final String SHOP_SEARCH_GOODS_DIM_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_SEARCH_GOODS_DIM_CLZ + APP_SECRET);
    public static final String SHOP_SEARCH_GOODS_LIST_CLZ = "GoodsDrugSearch";//商品搜索列表
    public static final String SHOP_SEARCH_GOODS_LIST_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_SEARCH_GOODS_LIST_CLZ + APP_SECRET);
    public static final String SHOP_SEARCH_GOODS_HOT_CLZ = "ShopGoodsHot";//热门搜索标签
    public static final String SHOP_SEARCH_GOODS_HOT_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_SEARCH_GOODS_HOT_CLZ + APP_SECRET);


    //首页结果（扫码，搜索）,药品信息
    public static final String RESULRS_CLZ_INFO = "Results4";
    public static final String RESULRS_SIGN_INFO = MD5ChangeUtil.Md5_32(APP_HOME + RESULRS_CLZ_INFO + APP_SECRET);

    //首页结果（扫码，搜索），药品排名列表
    public static final String RESULRS_CLZ_RANKING = "DrugRanking3";
    public static final String RESULRS_SIGN_RANKING = MD5ChangeUtil.Md5_32(APP_HOME + RESULRS_CLZ_RANKING + APP_SECRET);

    public static final String DRUG_CURRENRT_CLZ = "DrugCurrent";
    public static final String DRUG_CURRENRT_SIGN = MD5ChangeUtil.Md5_32(APP_HOME + DRUG_CURRENRT_CLZ + APP_SECRET);
//
//    public static final String WX_APP_ID = "wx38061c74a670facd";
//    public static final String WX_APP_SECRET = "7f01cc817566ddc70b92b2be91c46cb0";
    //订单二次验证
    public static final String SHOP_SUBMIT_SECOND_CLZ = "GoodsValidateOrder";
    public static final String SHOP_SUBMIT_SECOND_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + SHOP_SUBMIT_SECOND_CLZ + APP_SECRET);
    public static final String SHOP_SUBMIT_SECOND_ORDER_ID = "OrderID";


    //提现
    public static final String PERSON_WITHDRAW_CLZ = "PersonWithdraw";
    public static final String PERSON_WITHDRAW_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + PERSON_WITHDRAW_CLZ + APP_SECRET);
    public static final String PERSON_WITHDRAW_BALANCE = "Balance";
    public static final String PERSON_WITHDRAW_ALIPAY = "Alipay";
    public static final String PERSON_WITHDRAW_ALINAME = "AliName";

    //提现列表
    public static final String PERSON_WITHDRAW_LIST_CLZ = "PersonWithdrawList";
    public static final String PERSON_WITHDRAW_LIST_SIGN = MD5ChangeUtil.Md5_32(APP_SHOP + PERSON_WITHDRAW_LIST_CLZ + APP_SECRET);

    /**
     * 提现界面
     */
    public static final String MINE_WITHDRAW_CLZ = "Balance";
    public static final String MINE_WITHDRAW_CLZ_SMALL = "balance";
    public static final String MINE_WITHDRAW_SIGN = MD5ChangeUtil.Md5_32(APP_UCENTER + MINE_WITHDRAW_CLZ + APP_SECRET);
    public static final String MINE_WITHDRAW_PARAM_WITHDRAWCOUNT = "withdrawCount";

    /**
     * 提现记录
     */
    public static final String PERSON_WITHDRAW_RETURN_CLZ = "BalanceDetail";
    public static final String PERSON_WITHDRAW_RETURN_SIGN = MD5ChangeUtil.Md5_32(APP_UCENTER + PERSON_WITHDRAW_RETURN_CLZ + APP_SECRET);
    public static final String PERSON_WITHDRAW_RETURN_PARAM_TYPE = "Type";


    //用户余额接口
    public static final String UCENTER_BALANCE_CLZ = "Balance";
    public static final String UCENTER_BALANCE_SIGN = MD5ChangeUtil.Md5_32(APP_UCENTER + UCENTER_BALANCE_CLZ + APP_SECRET);
    //获取个人信息
    public static final String UCENTER_INDEX_CLZ = "Index";
    public static final String UCENTER_INDEX_SIGN = MD5ChangeUtil.Md5_32(APP_UCENTER + UCENTER_INDEX_CLZ + APP_SECRET);
    // 获取消息提醒状态
    public static final String UCENTER_GETMESSAGE_CLZ = "GetMessage";
    public static final String UCENTER_GETMESSAGE_SIGN = MD5ChangeUtil.Md5_32(APP_UCENTER + UCENTER_GETMESSAGE_CLZ + APP_SECRET);
    //帮助与反馈
    public static final String UCENTER_FEEDBACK_HELP_CLZ = "FeedbackHelp";
    public static final String UCENTER_FEEDBACK_HELP_SIGN = MD5ChangeUtil.Md5_32(APP_UCENTER + UCENTER_FEEDBACK_HELP_CLZ + APP_SECRET);

    public static final String UCENTER_NEWMESSAGE_CLZ = "NewMessage";
    public static final String UCENTER_NEWMESSAGE_SIGN = MD5ChangeUtil.Md5_32(APP_UCENTER + UCENTER_NEWMESSAGE_CLZ + APP_SECRET);


    //药品详情  报销
    public static final String APP_BAOXIAO = "Baoxiao";

    public static final String BAOXIAO_DETAIL_CLZ = "Info";
    public static final String BAOXIAO_DETAIL_SIGN = MD5ChangeUtil.Md5_32(APP_BAOXIAO + BAOXIAO_DETAIL_CLZ + APP_SECRET);

    //报销查询  详情
    public static final String BAOXIAO_DETAIL_JZCITY = "JzCity";
    public static final String BAOXIAO_DETAIL_CBCITY = "CbCity";
    public static final String BAOXIAO_DETAIL_TBTYPE = "YbType";

    //医院列表
    public static final String HOSPITAL_LIST_CLZ = "pageIndex";
    public static final String HOSPITAL_LIST_SIGN = MD5ChangeUtil.Md5_32(APP_UCENTER + UCENTER_BALANCE_CLZ + APP_SECRET);

    public static final String HOSPITAL_LIST_PAGEINDEX = "AdoptHospital";
    public static final String HOSPITAL_LIST_ADOPTION_HOSPITAL = "AdoptionHospital";
    public static final String HOSPITAL_LIST_HOME = "Home";
    //验证昵称
    public static final String USER_VALIDATE_NICKNAME_CLZ = "ValidateNickname";
    public static final String USER_VALIDATE_NICKNAME_SIGN = MD5ChangeUtil.Md5_32(APP_USER + USER_VALIDATE_NICKNAME_CLZ + APP_SECRET);
    public static final String USER_VALIDATE_PARAM_NICKNAME = "nickname";
    //忘记密码
    public static final String USER_FORGOTPASSWD_CLZ = "ForgotPasswd";
    public static final String USER_FORGOTPASSWD_SIGN = MD5ChangeUtil.Md5_32(APP_USER + USER_FORGOTPASSWD_CLZ + APP_SECRET);

    public static final String USER_VERIFYCODE_CLZ = "VerifyCode";
    public static final String USER_VERIFYCODE_SIGN = MD5ChangeUtil.Md5_32(APP_USER + USER_VERIFYCODE_CLZ + APP_SECRET);

    public static final String USER_REGISTER_SEND_SMS_CLZ = "RegisterSendSms";
    public static final String USER_REGISTER_SEND_SMS_SIGN = MD5ChangeUtil.Md5_32(APP_USER + USER_REGISTER_SEND_SMS_CLZ + APP_SECRET);


    public static final String USER_PERFECTINFO_CLZ = "PerfectInfo";
    public static final String USER_PERFECTINFO_SIGN = MD5ChangeUtil.Md5_32(APP_USER + USER_PERFECTINFO_CLZ + APP_SECRET);

    //登录
    public static final String USER_LOGIN_CLZ = "Login";
    public static final String USER_LOGIN_SIGN = MD5ChangeUtil.Md5_32(APP_USER + USER_LOGIN_CLZ + APP_SECRET);


    //获取token
    public static final String APP_WAP = "Wap";

    public static final String WAP_DEVICESTOKEN_CLZ = "DevicesToken";
    public static final String WAP_DEVICESTOKEN_SIGN = MD5ChangeUtil.Md5_32(APP_WAP + WAP_DEVICESTOKEN_CLZ + APP_SECRET);

    public static final String WAP_HOTCITY_CLZ = "HotCity";
    public static final String WAP_HOTCITY_SIGN = MD5ChangeUtil.Md5_32(APP_WAP + WAP_HOTCITY_CLZ + APP_SECRET);


}
