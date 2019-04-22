package com.lzyc.ybtappcal.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.lzyc.ybtappcal.R;
import com.lzyc.ybtappcal.constant.Contants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：xujm
 * 时间：2015/12/29
 * 备注：字符串工具类
 */
public class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }

    /**
     * is null or its length is 0 or it is made by space
     * <p/>
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return
     * true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * is null or its length is 0
     * <p/>
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0, return true, else return
     * false.
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    /**
     * compare two string
     *
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtils.isEquals(actual, expected);
    }

    /**
     * get length of CharSequence
     * <p/>
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     *
     * @param str
     * @return if str is null or empty, return 0, else return
     * {@link CharSequence#length()}.
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * null Object to empty string
     * <p/>
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str
                .toString()));
    }

    /**
     * capitalize first letter
     * <p/>
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str
                : new StringBuilder(str.length())
                .append(Character.toUpperCase(c))
                .append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8
     * <p/>
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(
                        "UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     * <p/>
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return <ul>
     * <li>if href is null, return ""</li>
     * <li>if not match regx, return source</li>
     * <li>return the last string that match regx</li>
     * </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern
                .compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * process special char in html
     * <p/>
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source : source
                .replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     * <p/>
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     * <p/>
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * 判断字符串是否乱码
     *
     * @param strName
     * @return
     */
    public static boolean isGarbledCode(String strName) {
        if (null == strName || 0 == strName.trim().length()) {
            return true;
        }

        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {

                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }

        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否是电话号码
     *
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
//        Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
//        Matcher m = p.matcher(email);
//        return m.matches();
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 用****替换手机号码中间4位
     */
    public static String replacePhone(String phone) {
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 用****替换邮箱
     */
    public static String replaceEmail(String email) {
        String[] emails = email.split("@");
        if (3 >= emails[0].length()) {
            return email;
        } else {
            String left = emails[0].substring(0, 3);
            String xing = "****";
            String leftStr = left + xing;
            return leftStr + "@" + emails[1];
        }
//        return email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
    }

    /**
     * 获取参保职位类型
     *
     * @return
     */
    public static String getYBJobType(Context context) {
        String strYbJob = "在职职工";
        String[] typeJobs = context.getResources().getStringArray(R.array.type_job);
        String[] typeJobsType = context.getResources().getStringArray(R.array.type_job_status);
        String typeJob = (String) SharePreferenceUtil.get(context, SharePreferenceUtil.KEY_OBJ_TYPE_JOB, Contants.STR_TYPE_JOB);
        for (int i = 0; i < typeJobs.length; i++) {
            if (typeJob.equals(typeJobsType[i])) {
                strYbJob = typeJobs[i];
                break;
            }
        }
        return strYbJob;
    }

    /**
     * 不过直辖市含有市，不显示市
     *
     * @param context
     * @param city
     * @return
     */
    public static String getCity(Context context, String city) {
//        //获取直辖市列表,如果是直辖市不显示市
//        String[] directCityArray=context.getResources().getStringArray(R.array.direct_city_array);
//        for (int i = 0; i < directCityArray.length; i++) {
//            String directCity=directCityArray[i];
//            if(city.contains(directCity)){
//                city=directCity;
//            }
//        }
        return city;
    }

    /**
     * 如果省份含有市，不显示市
     *
     * @param provice
     * @return
     */
    public static String getProvice(String provice) {
        if (provice.contains("市")) {//如果省份包含市，不显示市
            provice = provice.substring(0, provice.length() - 1);
        }
        LogUtil.y("###截取的provice" + provice);
        return provice;
    }

    /**
     * 如果省份含有市，不显示
     * 如果含有省，不显示
     * 如果含有自治区不显示
     * 如果含有特别行政区，不显示
     *
     * @param provice
     * @return
     */
    public static String getSinpleProvice(String provice) {
        if (provice.contains("市")) {//如果省份包含市，不显示市
            provice = provice.substring(0, provice.length() - 1);
        }
        return provice;
    }

    /**
     * 获取分段的手机号
     *
     * @param phone
     * @return
     */
    public static String getSubsectionPhone(String phone) {
        StringBuilder stringBuilder = new StringBuilder(phone);
        stringBuilder.insert(3, "-");
        stringBuilder.insert(8, "-");
        return stringBuilder.toString();
    }

    /**
     * 判断字符串是否否是真想
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 返现的钱
     * 如果返回了小数点，小数点后两位都是零的话，判断为整数
     * 如果小数点后两位的数字大于零，那么都显示出来
     * @param str
     * @return
     */
    public static String getReturnMoney(String str) {
        boolean isInt = false;
        String returnMoney = "";
        int dIndex = 0;
        if (str.contains(".")) {//如果字符串包含点，有可能是伪小数和真小数
            int len = str.length() - str.indexOf(".") - 1;//获取小数点后面的小数位数
            dIndex = str.indexOf(".")+1;//小数点的下标
            if (len == 2) {//如果只有两位小数
                String xiaoshuStr = str.substring(dIndex, dIndex + len);
                for (int i = 0; i < xiaoshuStr.length(); i++) {
                    char charShu =xiaoshuStr.charAt(i);
                    int intShu=Character.getNumericValue(charShu);
                    if (intShu > 0) {
                        isInt = false;
                    } else {
                        isInt = true;
                    }
                }
            }else{//不只两位小数
                LogUtil.y("*******不只两位小数");
            }
        } else {
            isInt = true;
        }
        if (isInt && str.contains(".")) {
            returnMoney = str.substring(0, dIndex-1);
        } else {
            returnMoney = str;
        }
        return returnMoney;
    }

    /**
     * 设置内部字体颜色
     *
     * @param text
     * @return
     */
    public static SpannableStringBuilder getSpannableText(String text, String speci) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(speci);
        AbsoluteSizeSpan sizeSpan=new AbsoluteSizeSpan(DensityUtils.sp2px(12));
        spannableStringBuilder2.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), 0, speci.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder2.setSpan(sizeSpan,0,speci.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.append(spannableStringBuilder2);
        return spannableStringBuilder;
    }

    /**
     * 设置内部字体颜色
     *
     * @param text
     * @return
     */
    public static SpannableStringBuilder getSpannableText(String text, String speci, int rightSize) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(speci);
        AbsoluteSizeSpan sizeSpan=new AbsoluteSizeSpan(DensityUtils.sp2px(rightSize));
        spannableStringBuilder2.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), 0, speci.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder2.setSpan(sizeSpan,0,speci.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.append(spannableStringBuilder2);
        return spannableStringBuilder;
    }
    /**
     * 设置内部字体颜色
     *
     * @param text
     * @return
     */
    public static SpannableStringBuilder getSpannableText(String text, String speci, int rightSize,String color) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(speci);
        AbsoluteSizeSpan sizeSpan=new AbsoluteSizeSpan(DensityUtils.sp2px(rightSize));
        spannableStringBuilder2.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), 0, speci.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder2.setSpan(sizeSpan,0,speci.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.append(spannableStringBuilder2);
        return spannableStringBuilder;
    }

    /**
     *
     * @param price
     * @param color
     * @param qianfuhaoSize
     * @param size
     * @return
     */
    public static SpannableStringBuilder getPrice(String price,int color,int qianfuhaoSize,int size) {
        SpannableStringBuilder qianfuhaoSSB = new SpannableStringBuilder("￥");
        AbsoluteSizeSpan fuhaoSizeSpan=new AbsoluteSizeSpan(DensityUtils.sp2px(size));
        qianfuhaoSSB.setSpan(fuhaoSizeSpan,0,1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        SpannableStringBuilder priceSSB= new SpannableStringBuilder(price);
        AbsoluteSizeSpan priceSizeSpan=new AbsoluteSizeSpan(DensityUtils.sp2px(qianfuhaoSize));
        priceSSB.setSpan(new ForegroundColorSpan(color), 0, price.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        priceSSB.setSpan(priceSizeSpan,0,price.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        qianfuhaoSSB.append(priceSSB);
        return qianfuhaoSSB;
    }
    
}
