package com.lzyc.ybtappcal.constant;

/**
 * 静态常量类
 */
public class Contants {
    //关键字:key
    public static final String KEY_PAGE = "page";
    public static final String KEY_PAGE_DRUG = "page_drug";
    public static final String KEY_PAGE_SEARCH = "page_search";
    public static final String KEY_PAGE_SCAN = "page_scan";
    public static final String KEY_PAGE_SEARCH_DUGLIST = "page_search_druglist";
    public static final String KEY_PAGE_BIYAO_HISTORY = "page_biyao_history";
    public static final String KEY_PAGE_CITY_CHOOSE = "page_city_choose";
    public static final String KEY_PAGE_PWD = "page_pwd";
    public static final String KEY_PAGE_YIYUANWEIZHI = "page_drug";//医院位置
    public static final String KEY_PAGE_PAYMENT ="page_payment";
    /*此类字段解释：只要去地址管理界面的需要做下一步处理的，都传入此key*/
    public static final String KEY_PAGE_ADDRESS_MANAGER = "page_address_manager";//地址管理界面
    public static final String KEY_PAGE_ADDRESS_BUILD = "page_address_build";//地址创建界面
    public static final String KEY_PAGE_RESULTS = "page_results";//结果

    public static final String KEY_OBJ_DRUG_DETAIL="goods_drug_detail";
    public static final String KEY_OBJ_HOSPITALBEAN = "hospitalbean";
    public static final String KEY_OBJ_PLACEDATA = "placeddatabean";
    public static final String KEY_OBJ_DRUGBEAN = "drugbean";
    public static final String KEY_OBJ_CITY_CURRENT = "city_current";//当前定位城市，异地报销选择，城市
    public static final String KEY_OBJ_PAYMENT_WECHAT = "payment_wechat";
    public static final String KEY_OBJ_PAYMENT_ALIPY= "payment_alipy";
    public static final String KEY_OBJ_ORDERINFO= "orderInfo";
    public static final String KEY_GOODS_LIST= "goodslist";
    public static final String KEY_MEMBER_EDIT= "member_edit";
    public static final String KEY_MEMBER_ID= "member_id";
    public static final String KEY_IMAGE_LIST= "imagelist";
    public static final String KEY_IMAGE= "image";
    public static final String KEY_BUY_FROM_ORDERBY= "buy_from_orderby";//从购买药品页直接购买
    public static final String KEY_GOODS_PRICE_ALL= "goods_price_all";//总价
    public static final String KEY_GOODS_PRICE_RETURN= "goods_price_return";//在返现
    public static final String KEY_CHACHE_OBJ_MINEINFO = "cache_mine_info";//对象mine_info的缓存
    public static final String KEY_CHACHE_OBJ_REFFUNDBRIEF= "cache_refundbrief";//对象mine_info的缓存
    public static final String KEY_CHACHE_OBJ_BANNER_BIYAO= "cache_banner_biyao";//对象药品查询，比药界面banner的缓存
    public static final String KEY_CHACHE_OBJ_ISMESSAGE= "cache_ismessage";
    public static final String KEY_OBJ_MINE_INFO= "mine_info";
    public static final String KEY_OBJ_ADDRESS_INFO= "addressInfo";
    public static final String KEY_BOL_CART_REFRSH= "cart_refresh";
    public static final String KEY_STR_KEYWORD= "keyword";
    public static final String KEY_STR_KEYWORD_CODE= "keyword_can_code";
    public static final String KEY_STR_URL= "url";
    public static final String KEY_STR_YYID= "yyId";
    public static final String KEY_STR_TITLE= "title";
    public static final String KEY_OBJ_RECOMMEND="key_recommend";

    public static final String KEY_PAYEMNT_NAME ="payment_name";
    public static final String KEY_ORDER_ID ="orderId";
    public static final String KEY_POSITION ="position";
    public static final String KEY_PAYEMNT_PRICE ="payment_price";

    public static final String GOODS_DRUG_INFO_BEAN = "goodsDrugInfoBean";

    public static final String KEY_PHARMACY = "pharmacy";
    public static final String KEY_PHARMACY_TITLE = "pharmacy_title";
    public static final String KEY_STR_MESSAGE_STATE ="ismessage";
    public static final String KEY_DRUG_ID= "drug_id";
    public static final String KEY_DRUG_ID2="drugId";
    public static final String KEY_DRUG_NAME_ID="drugNameID";


    public static final String KEY_HOME_TITLE = "home_title";
    public static final String KEY_HOME_TYPE = "home_type";
    public static final String KEY_HOME_ID = "home_id";
    public static final String KEY_ADDRESS_DELETE_ID = "address_delete_id";
    public static final String KEY_ADDRESS_ID = "address_id";
    public static final String KEY_HINT_SEARCH = "hint_search";
    public static final String KEY_ADDRESS_INFO = "key_address_info";//如果界面跳转需要传AddressInfo对象，以这个key
    public static final String KEY_ADDRESS_EMPTY= "key_address_empty";//如果地址管理界面的地址是empty
    public static final String KEY_ADDRESS_ACTION= "key_address_action";//地址操作，操作事件
    public static final String KEY_BROWSE_ACTION="key_browse_action";
    public static final String KEY_MEDICINE_BEAN= "key_medicine_bean";//家庭药箱
    public static final String KEY_DRUGNAME_ID= "drugNameID";
    public static final String KEY_VENDER_ID= "venderID";
    public static final String KEY_SPECIFICATION_ID= "specificationsID";
    public static final String KEY_INTPUT="keyinput";
    public static final String KEY_PHONE="phone";
    public static final String KEY_CODE_INVITE="invitecode";


    //关键字:value
    public static final int VAL_PAGE = 0;//界面存储重置使用
    public static final int VAL_PAGE_SEARCH_DURUG = 100;//比药搜索
    public static final int VAL_PAGE_SEARCH_REIMBURSEMENT = 101;
    public static final int VAL_PAGE_SEARCH_DRUGLIST = 102;
    public static final int VAL_PAGE_SEARCH_HISTORY = 103;
    public static final int VAL_PAGE_SEARCH_SCAN = 104;
    public static final int VAL_PAGE_MINEPLAN = 200;
    public static final int VAL_PAGE_CITY_CHOOSE_TITLE = 300;
    public static final int VAL_PAGE_CITY_CHOOSE_REIMBURSEMENT = 301;
    public static final int VAL_PAGE_FORGET = 302;
    public static final int VAL_PAGE_REGISTER = 303;
    public static final int VAL_PAGE_SAOMA = 400;
    public static final int VAL_PAGE_ADDRESSMANAGER =500;//地址管理界面
    public static final int VAL_PAGE_HOONBAO =600;//红包

    public static final int VAL_PAGE_YIYUANWEIZHI_SRESULT = 600;//搜索结果界面
    public static final int VAL_PAGE_PAYMENT_MINE =700;//支付，我的相关
    public static final int VAL_PAGE_PAYMENT_PURCHASE =701;//支付，购买相关
    public static final int VAL_PAGE_PAYMENT_DETAIL =702;//支付，订单详情
    public static final int VAL_PAGE_PAYMENT_CHECK =703;//支付，检查订单
    public static final int VAL_PAGE_PAYMENT_CART =704;//购物车
    public static final int VAL_PAGE_ADVERTIMENT= 705;//广告
    public static final int VAL_PAGE_SEARCH_DRUG_ADD = 800;//添加药品
    public static final int VAL_PAGE_RESULTS = 900;//结果界面

    //关键字tab value
    public static final int TAB_NONE = -1;
    public static final int TAB_HOME = 0;
    public static final int TAB_DRUG_SEACH = 1;
    public static final int TAB_DRUGS_QUERY = 2;
    public static final int TAB_NEWS = 3;
    public static final int TAB_MINE = 4;
    public static final int TAB_ORDER_ALL = 0;
    public static final int TAB_ORDER_PENGING_PAYMENT = 1;//待付款
    public static final int TAB_ORDER_PENGING_GOODS = 2;//代发货
    public static final int TAB_ORDER_PENGING_RECEIPT= 3;//待收货
    //
    public static boolean isFirstEnter = true;

    //关键字：常量字符串
    public static final String STR_APPID_WECHAT="wx38061c74a670facd";
    public static final String STR_APPID_WEIBO="2013377845";
    public static final String STR_APPID_QQ="1104912596";
    public static final String STR_SECRET_WECHAT="7f01cc817566ddc70b92b2be91c46cb0";
    public static final String STR_SECRET_WEIBO="89e004317d19466d3b964c0bf701c807";
    public static final String STR_SECRET_QQ="JY5jtjn4mLW2BsFp";
    public static final String STR_PROMPT="提示";
    public static final String STR_ACCOUNT_NOINFO="未查询到此用户信息";
    public static final String STR_GOT_IT="知道了";
    public static final String STR_ACCOUNT_INFO_EDIT="点击编辑资料";
    public static final String STR_ACCOUNT_INFO_LOGIN="请点击头像登录";
    public static final String STR_DRUG_TYPE_A = "甲类";//当药品信息为"",则认为是自费
    public static final String STR_DRUG_TYPE_B = "乙类";
    public static final String STR_DRUG_TYPE_AB = "甲乙类";
    public static final String STR_TYPE_JOB = "ZZ00";
    public static final String STR_TITLE_ORDER_BY = "购买药品";
    public static final String STR_TITLE_FAMILY_MEDICINE = "家庭药箱";
    public static final String STR_TITLE_ORDER_DETAIL = "订单详情";
    public static final String STR_TITLE_ORDER_CHECK = "检查订单";
    public static final String STR_TITLE_ORDER_MINE = "我的订单";
    public static final String STR_TITLE_BALANCE_DETAILS = "余额明细";
    public static final String STR_TITLE_MEMBER_ADD = "添加成员";
    public static final String STR_TITLE_MEMBER_EDIT = "编辑成员";
    public static final String STR_TITLE_RESULTS = "查询结果";
    public static final String STR_SELECTED_ALL= "全选";
    public static final String STR_SELECTED_ALL_NONE= "全不选";
    public static final String STR_ADDRESS_CHOOSE = "请选择地址";
    public static final String STR_WECHAT_INSTALLED="微信未安装或版本过低";
    public static final String STR_ADDRESS_ID = "address_id";
    public static final String KEY_COUNT="count";
    public static final String KEY_COUNT2="count2";
    public static final String KEY_TABVALUE="tabvalue";
    public static final String KEY_POSITION_CUT="midData";//切割的位置
    public static final String KEY_RESULT_CODE="resultcode";//码

    public static final String STR_0= "0";
    public static final String STR_1= "1";
    public static final String STR_2= "2";
    public static final String STR_3= "3";
    public static final int INT_MINUS3= -3;
    public static final int INT_MINUS2= -2;
    public static final int INT_MINUS1= -1;
    public static final int INT_0= 0;
    public static final int INT_1= 1;
    public static final int INT_2= 2;
    public static final int INT_3= 3;

    //关键字：静态
    public static String oldType = STR_TYPE_JOB;
    //关键字：广播
//    public static final String ACTION_NAME_SCAN = "results_refresh";//定位返回到结果界面
    public static final String ACTION_NAME_PUSH_READ_NO = "push.comment.noread";//评论未读消息
    public static final String ACTION_NAME_PAYMENT_ALIPY= "payment_alipay";//支付宝支付回调
    public static final String ACTION_NAME_PAYMENT_WECHAT= "payment_wechat";//微信支付回调
    public static final String ACTION_NAME_PAYMENT_SUCCESS= "payment_success";//支付成功
    public static final String ACTION_NAME_PAYMENT_ERROR= "payment_error";//支付失败
    public static final String ACTION_NAME_DRUG_ADD_FINISH= "drugadd_finish";//添加用药关闭
    public static final String ACTION_NAME_DRUG_ADD_REFRESH= "drugadd_refresh";//添加用药刷新
    public static final String ACTION_NAME_MEDICINE_PERSON_REFRESH= "medicine_person_refresh";//个人药箱刷新
    public static final String ACTION_NAME_MEDICINEPERSION_FINISH= "medicinePerson_finish";//个人药箱关闭
    public static final String ACTION_NAME_SCAN_ZBAR_FINISH = "scan_zbar_finish";//zbar扫码关闭
    public static final String ACTION_NAME_BROWSE_SELECTED="browse_selected";
    public static final String ACTION_NAME_BROWSE="browse";
    public static final String ACTION_ORDER_INSTRU_REFRESH="instructions_refresh";
    public static final String ACTION_LOGION_SUCCESS="success";

    /**
     * 扫描选择相册图片,判断是否压缩的临界值
     */
    public static final Long SCAN_DEFAULT_SIZE = (long) 4000000;

    /**
     * 首页加载的时候动画
     */
    public static final int FLAG_START_HANDLER_ANIM = 666;
    public static final int FLAG_END_HANDLER_ANIM = 667;

    /**
     * TopFragment  动画适配 机型
     * 99000662367896  小米Note
     */
    public static final String FLAG_PHONE_TYPE_XIAOMINOTE = "MI NOTE LTE";
    public static final String FLAG_PHONE_TYPE_LETVX501 = "Letv X501";

    /**
     * 适配乐视 max2  1440 * 2560
     */
    public static final int FLAG_PHONE_WIDTH = 1440;
    public static final int FLAG_PHONE_HEIGHT = 2560;

    public static final String DRUG_TYPE_RX="rx";

    /**
     * 申请权限
     */
    public static final int REQUEST_PERMISSION_CAMERA=100;//相机，摄像头，闪光灯
    public static final int REQUEST_PERMISSION_READ=101;//读取，写入文件
    public static final int REQUEST_PERMISSION_IMEI=102;//读取手机imei码
    public static final int REQUEST_PERMISSION_LOACTION=103;//定位相关权限

}
