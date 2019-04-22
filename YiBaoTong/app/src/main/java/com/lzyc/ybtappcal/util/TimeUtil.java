package com.lzyc.ybtappcal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 作者：xujm
 * 时间：2015/12/29
 * 备注：日期工具类
 */
public class TimeUtil {
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
            "yyyy-MM-dd");
    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

    /**
     * 将calendar转换成String日期
     *
     * @param calendar
     * @return String
     * @author Lucifer 2015-5-16 下午1:35:16
     */
    public static String calendarChangeString(Calendar calendar) {
        String date = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + " "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE);
        return date;
    }

    /**
     * 将String 转换成 Calendar
     *
     * @param date
     * @return Calendar
     * @author Lucifer 2015-5-16 下午1:58:55
     */
    public static Calendar StringChangeCalendar(String date) {
        Calendar calendar = Calendar.getInstance();
        int year = Integer.parseInt(RegularExpressionUtil.getAfterDate(date,
                "-", 0));
        int month = Integer.parseInt(RegularExpressionUtil.getAfterDate(date,
                "-", 1));
        int day = Integer.parseInt(RegularExpressionUtil
                .getDate(date, "-", " "));
        int hour = Integer.parseInt(RegularExpressionUtil.getDate(date, " ",
                ":"));
        int minute = Integer.parseInt(RegularExpressionUtil.getAfterDate(date,
                ":"));
        calendar.set(year, month, day, hour, minute);
        return calendar;
    }

    /**
     * 将毫秒数转换date
     *
     * @param time
     * @return String
     * @author Administrator 2015-5-24 下午5:40:15
     */
    public static String MilChangeDate(String time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(time));
        String date = calendarChangeString(calendar);
        return date;
    }

    /**
     * 获取时间差,距离当前（秒/分/小时/天）
     *
     * @param time
     * @return String
     * @author Administrator 2015-5-31 下午9:10:20
     */
    public static String TimeDifference(long time) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Calendar calendar = Calendar.getInstance();

        long between = (calendar.getTimeInMillis() - cal.getTimeInMillis()) / 1000;

        long day = between / 3600 / 24;

        long hour = between / 3600;

        long minute = between % 3600 / 60;

        long second = between % 60;

        if (day != 0) {
            return day + "天";
        } else if (hour != 0) {
            return hour + "小时";
        } else if (minute != 0) {
            return minute + "分钟";
        } else {
            return second + "秒";
        }
    }

    /**
     * 获取当前时间
     *
     * @return String
     * @author Lucifer 2015-4-14 下午11:24:42
     */
    public static String getNowTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return year + "-" + (month < 10 ? "0" + month : month) + "-" + day
                + " " + (hour < 10 ? "0" + hour : hour) + ":"
                + (minute < 10 ? "0" + minute : minute);
    }

    /**
     * 获取当前年龄
     *
     * @param time
     * @return String
     * @author Lucifer 2015-6-8 下午9:51:30
     */
    public static String getAge(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        Calendar calendar = Calendar.getInstance();

        String between = (calendar.get(Calendar.YEAR) - cal.get(Calendar.YEAR))
                + "";
        return between;
    }

    /**
     * 获取年级
     *
     * @param time (xxxx-xx-xx)
     * @return String
     * @author Administrator 2015-6-8 下午10:04:42
     */
    public static String getAge(String time) {
        int year = Integer.parseInt(RegularExpressionUtil.getAfterDate(time,
                "-", 0));
        int month = Integer.parseInt(RegularExpressionUtil.getAfterDate(time,
                "-", 1));
        int day = Integer.parseInt(RegularExpressionUtil.getAfterDate(time,
                "-", 2));

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        String age = getAge(calendar.getTimeInMillis());
        return age;
    }

    /**
     * 获取星座
     * @param time
     * @return
     */
    public static String getConstellation(String time) {
        String constellation = "";// 星座
        int month = Integer.parseInt(RegularExpressionUtil.getAfterDate(time,
                "-", 1));
        int day = Integer.parseInt(RegularExpressionUtil.getAfterDate(time,
                "-", 2));
        switch (month) {
            case 1:
                if (day <= 19) {
                    constellation = "魔蝎座";
                } else {
                    constellation = "水瓶座";
                }
                break;
            case 2:
                if (day <= 18) {
                    constellation = "水平座";
                } else {
                    constellation = "双鱼座";
                }
                break;
            case 3:
                if (day <= 20) {
                    constellation = "双鱼座";
                } else {
                    constellation = "白羊座";
                }
                break;
            case 4:
                if (day <= 19) {
                    constellation = "白羊座";
                } else {
                    constellation = "金牛座";
                }
                break;
            case 5:
                if (day <= 20) {
                    constellation = "金牛座";
                } else {
                    constellation = "双子座";
                }
                break;
            case 6:
                if (day <= 21) {
                    constellation = "双子座";
                } else {
                    constellation = "巨蟹座";
                }
                break;
            case 7:
                if (day <= 22) {
                    constellation = "巨蟹座";
                } else {
                    constellation = "狮子座";
                }
                break;
            case 8:
                if (day <= 22) {
                    constellation = "狮子座";
                } else {
                    constellation = "处女座";
                }
                break;
            case 9:
                if (day <= 22) {
                    constellation = "处女座";
                } else {
                    constellation = "天平座";
                }
                break;
            case 10:
                if (day <= 23) {
                    constellation = "天平座";
                } else {
                    constellation = "天蝎座";
                }
                break;
            case 11:
                if (day <= 22) {
                    constellation = "天蝎座";
                } else {
                    constellation = "射手座";
                }
                break;
            case 12:
                if (day <= 21) {
                    constellation = "射手座";
                } else {
                    constellation = "魔蝎座";
                }
                break;
            default:
                break;
        }
        return constellation;
    }

    /**
     * 时间毫秒值转换指定格式时间
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * 时间毫秒值转换默认格式时间 {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 获取时间毫秒数
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * 获取默认时间格式的时间 {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * 获取指定时间格式的时间
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 将时间unix转换为xx-xx类型
     *
     * @param timeString
     * @return
     */
    public static String getDateToString(String timeString) {
        String time = timeString.replace(":", "-");
        return time;
    }

    /**
     * 将时间unix转换为xx/xx类型
     *
     * @param timeString
     * @return
     */
    public static String getDateToStringShu(String timeString) {
        String time = timeString.replace(":", "/");
        return time;
    }

    /**
     * 时间分隔符-换成。
     *
     * @param timeString
     * @return
     */
    public static String getDateToStringDian(String timeString) {
        String time = timeString.replace("-", ".");
        return time;
    }
    /**
     * 24小时制转化成12小时制
     *
     * @param strDay
     */
    public static String timeFormatStr(Calendar calendar,String strDay)
    {
        String tempStr = "";
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 11)
        {
            tempStr = "下午"+" " + strDay;
        }
        else
        {
            tempStr = "上午"+" " + strDay;
        }
        return tempStr;
    }

    /**
     * 今天,昨天
     * @param timeStamp
     * @return
     */
    public static String multiSendTimeToStr(long timeStamp) {

        if (timeStamp==0) return "";
        Calendar inputTime = Calendar.getInstance();
        String timeStr = timeStamp + "";
        if(timeStr.length() == 10){
            timeStamp = timeStamp*1000;
        }
        inputTime.setTimeInMillis(timeStamp);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        if (calendar.before(inputTime)){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return "昨天";
        }else {
            calendar.add(Calendar.DAY_OF_MONTH, -5);
            if (calendar.before(inputTime)){
                return getWeekDayStr(inputTime.get(Calendar.DAY_OF_WEEK));
            } else{
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                if (calendar.before(inputTime)){
                    SimpleDateFormat sdf = new SimpleDateFormat("M"+"/"+"d"+" ");
                    String temp1 = sdf.format(currenTimeZone);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                    String temp2 = sdf1.format(currenTimeZone);
                    return temp1+temp2;
                }else{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy"+"/"+"M"+"/"+"d"+" ");
                    String temp1 = sdf.format(currenTimeZone);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                    String temp2 = sdf1.format(currenTimeZone);
                    return temp1+temp2;
                }
            }
        }
    }
    /**
     * 时间转化为星期
     *
     * @param indexOfWeek   星期的第几天
     */
    public static String getWeekDayStr(int indexOfWeek){
        String weekDayStr = "";
        switch (indexOfWeek)
        {
            case 1:
                weekDayStr = "星期日";
                break;
            case 2:
                weekDayStr = "星期一";
                break;
            case 3:
                weekDayStr = "星期二";
                break;
            case 4:
                weekDayStr = "星期三";
                break;
            case 5:
                weekDayStr = "星期四";
                break;
            case 6:
                weekDayStr = "星期五";
                break;
            case 7:
                weekDayStr = "星期六";
                break;
        }
        return weekDayStr;
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentTime(){
      String time=getTime(System.currentTimeMillis(),DATE_FORMAT_DATE);
        return time;
    }

    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }


    public static String getTime(String time){
        long timeLong= 0;
        try {
            timeLong = stringToLong(time,"yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeFinal=multiSendTimeToStr(timeLong);
        return timeFinal;
    }

    /**
     * 判断是否是今天
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断是否为昨天
     * @throws ParseException
     */
    public static boolean IsYesterday(String day) throws ParseException {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

}

