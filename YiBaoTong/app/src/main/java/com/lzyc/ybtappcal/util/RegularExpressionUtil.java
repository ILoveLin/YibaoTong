package com.lzyc.ybtappcal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：xujm
 * 时间：2015/12/29
 * 备注：正则表达工具类
 */
public class RegularExpressionUtil {

    /**
     * 取规则值
     *
     * @param split1 规则1
     * @param split2 规则2
     * @return
     */
    public static String getDate(String date, String split1, String split2) {
        String[] strs1 = date.split(split1);
        String[] strs2 = strs1[1].split(split2);
        return strs2[0];
    }

    /**
     * 取规则值,取分割之前的值
     *
     * @param split 规则
     * @return
     */
    public static String getBeforeDate(String date, String split) {
        String[] strs1 = date.split(split);
        return strs1[0];
    }

    /**
     * 取规则值,取分割之后的值
     *
     * @param split 规则
     * @return
     */
    public static String getAfterDate(String date, String split) {
        String[] strs1 = date.split(split);
        return strs1[1];
    }

    /**
     * 取规则值,取分割之后的值
     *
     * @param date
     * @param split
     * @param num
     * @return
     */
    public static String getAfterDate(String date, String split, int num) {
        String[] strs1 = date.split(split);
        return strs1[num];
    }

    /**
     * 验证是否为空,空返回"".否则直接返回
     *
     * @param str
     * @return String
     * @author Administrator 2015-5-25 下午8:50:06
     */
    public static String checkNull(String str) {
        return str == null ? "" : str;
    }

    /***
     * 判断是否为中文
     *
     * @param str
     * @return
     */
    public static boolean isCn(String str) {

        if (str == null || str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]+$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;

        }
        return true;
    }

    /***
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumer(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /***
     * 判断是否为字母
     *
     * @param str
     * @return
     */
    public static boolean isLetters(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 验证Email
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证身份证号码
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex,idCard);
    }

//    /**
//     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
//     * @param mobile 移动、联通、电信运营商的号码段
//     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
//     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
//     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
//     *<p>电信的号段：133、153、180（未启用）、189</p>
//     * @return 验证成功返回true，验证失败返回false
//     */
//    public static boolean checkMobile(String mobile) {
//        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
//        return Pattern.matches(regex,mobile);
//    }

    /**
     * 验证固定电话号码
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     * <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *  数字之后是空格分隔的国家（地区）代码。</p>
     * <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     * 对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     * <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }
    /**
     * 验证手机和座机（包括小灵通）
     *
     * @param num
     * @return
     */
    public static boolean isLegalPhone(String num) {
        // String
        // template="^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$|(^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\\d{8}$)$";
        String template = "^1(3[0-9]|5[0-35-9]|8[025-9])\\d{8}$|^0(10|2[0-5789]|\\d{3})\\d{7,8}$";
        Pattern pattern = Pattern.compile(template);
        Matcher matcher = pattern.matcher(num);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    /**
     *
     * @param qqNum 传入的QQ
     * @return 如果匹配正确，return true， else return false
     */
    public static boolean checkQQNumber(String qqNum){
        //QQ号匹配结果
        boolean isQQNum_matcher = qqNum.matches("[1-9]\\d{2,11}");
        if(isQQNum_matcher)
            return true;
        return false;
    }
    /**
     *
     * @param pwd 传入的是 密码
     * @return 如果匹配正确，满足密码规则，return true， else return false
     */
    public static boolean checkPassWord(String pwd){
        //密码匹配结果
        boolean isPassWord_matcher = pwd.matches("[0-9a-zA-Z_@$@!！#]{6,16}");
        if(isPassWord_matcher)
            return true;
        return false;
    }


    /**
     * 验证整数（正整数和负整数）
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex,digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex,decimals);
    }

    /**
     * 验证空白字符
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex,blankSpace);
    }

    /**
     * 验证中文
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex,chinese);
    }

    /**
     * 验证日期（年月日）
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex,birthday);
    }

    /**
     * 验证URL地址
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkURL(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }

    /**
     * 匹配中国邮政编码
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }
}
