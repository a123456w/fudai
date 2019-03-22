package com.qlzx.mylibrary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

/**
 * User: howie Date: 13-5-11 Time: 下午4:09
 */
public class TimeUtil {


    static String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿", "万亿"};
    static char[] numArray = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};


    /**
     * 将整数转换成汉字数字
     *
     * @param num 需要转换的数字
     * @return 转换后的汉字
     */
    public static String formatInteger(int num) {
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == val[i - 1]) {
                    continue;
                } else {
                    sb.append(numArray[n]);
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        return sb.toString();
    }


    /**
     * 判断用户的设备时区是否为东八区（中国）
     *
     * @return
     */
    public static boolean isInEasternEightZones() {
        boolean defaultVaule = true;
        if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08"))
            defaultVaule = true;
        else
            defaultVaule = false;
        return defaultVaule;
    }

    /**
     * 根据不同时区，转换时间
     *
     * @param
     * @return
     */
    public static Date transformTime(Date date, TimeZone oldZone, TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime())
                    - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;
    }


    /**
     * 将时间转换为时间戳
     *
     * @param time
     * @return
     */
    public static String dateToStamp(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = simpleDateFormat.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }


    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }


    /**
     * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String dataOne(String time) {
//                                                            2018-4-14 18-29
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }


    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getOldDate(int distanceDay) {

        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LogUtil.d("前7天==" + dft.format(endDate));
        return dft.format(endDate);
    }


    /**
     * 根据当前日期获得是星期几
     *
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(time));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "周天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "周一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "周二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "周三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "周四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "周五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "周六";
        }
        return Week;
    }


    /**
     * 根据提供的年月获取该月份的最后一天
     *
     * @param year
     * @param monthOfYear
     * @return
     * @Description: (这里用一句话描述这个方法的作用)
     * @Author: gyz
     * @Since: 2017-1-9下午2:29:38
     */
    public static Date getSupportEndDayofMonth(int year, int monthOfYear) {
        Calendar cal = Calendar.getInstance();
        // 不加下面2行，就是取当前时间前一个月的第一天及最后一天
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDate = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDate = cal.getTime();
        return lastDate;
    }


    /**
     * @param timeStr 时间戳格式：1488896693000
     * @return
     */
    public static String timeAgo(long timeStr) {
//		Date date = null;
//		try {
//			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z");
//			date = format.parse(timeStr);
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return "";
//		}
//
//		long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();
        long seconds = (currentTimeStamp - timeStr) / 1000;

        long minutes = Math.abs(seconds / 60);
        long hours = Math.abs(minutes / 60);
        long days = Math.abs(hours / 24);

        if (seconds <= 15) {
            return "刚刚";
        } else if (seconds < 60) {
            return seconds + "秒前";
        } else if (seconds < 120) {
            return "1分钟前";
        } else if (minutes < 60) {
            return minutes + "分钟前";
        } else if (minutes < 120) {
            return "1小时前";
        } else if (hours < 24) {
            return hours + "小时前";
        } else if (hours < 24 * 2) {
            return "1天前";
        } else if (days < 30) {
            return days + "天前";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            String dateString = formatter.format(timeStr);
            return dateString;
        }

    }

    public static long dateDiff(String startTime, String endTime, String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            System.out.println("时间相差：" + day + "天" + hour + "小时" + min
                    + "分钟" + sec + "秒。");
            if (day >= 1) {
                return day;
            } else {
                if (day == 0) {
                    return 1;
                } else {
                    return 0;
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }


    /**
     * 转换成java的时间戳
     *
     * @param phpTimeStamp
     * @return
     */
    public static long getJavaTime(String phpTimeStamp) {
        try {
            return Long.parseLong(phpTimeStamp + "000");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取php简单的时间格式 时间精确到分钟
     *
     * @param phpTimeStamp
     * @return
     */
    public static String getSimpleTimeMini(String phpTimeStamp) {
        Date date = null;
        try {
            date = new Date(Long.parseLong(phpTimeStamp + "000"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String format2 = format.format(date);
        return format2;
    }

    /**
     * 获取php简单的时间格式 时间精确到秒
     *
     * @param phpTimeStamp
     * @return
     */
    public static String getSimpleTimeSecond(String phpTimeStamp) {
        Date date = null;
        try {
            date = new Date(Long.parseLong(phpTimeStamp + "000"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String format2 = format.format(date);
        return format2;
    }

    /**
     * 获取系统时间戳
     *
     * @return
     */
    public static String getTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        return str;
    }

    /**
     * 随机数
     *
     * @return
     */
    public static String getRandomTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        int min = 10;
        int max = 99;
        Random random = new Random();
        int num = random.nextInt(max) % (max - min + 1) + min;
        return (str + num);
    }


    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14  16:09:00"）
     *
     * @param time
     * @return
     */
    public static String timedate(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }


    /**
     * 获取简单的时间格式 时间到日期 yyyy-MM-dd
     *
     * @param phpTimeStamp
     * @return
     */
    public static String getSimpleDate(String phpTimeStamp) {
        Date date = null;
        try {
            date = new Date(Long.parseLong(phpTimeStamp + "000"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = format.format(date);
        return format2;
    }

    /**
     * 根据传入格式转换时间
     *
     * @param phpTimeStamp
     * @param pattern
     * @return
     */
    public static String getFormatData(String phpTimeStamp, String pattern) {
        Date date = null;
        try {
            date = new Date(Long.parseLong(phpTimeStamp + "000"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String format2 = format.format(date);
        return format2;
    }

    /**
     * 根据时间戳获得 MM月dd日
     *
     * @param phpTimeStamp
     * @return
     */
    public static String getMonthAndDay(String phpTimeStamp) {
        Date date = null;
        try {
            date = new Date(Long.parseLong(phpTimeStamp + "000"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        String format2 = format.format(date);
        return format2;
    }

    /**
     * 返回 yyyy-MM-dd  HH:mm
     *
     * @param javaTimeStamp
     * @return
     */
    public static String getDateTimeMini(String javaTimeStamp) {
        Date date = null;
        try {
            date = new Date(Long.parseLong(javaTimeStamp));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.getDefault());
        String format2 = format.format(date);
        return format2;
    }

    /**
     * 根据php时间戳返回剩余时间 yyyy/MM/dd HH:mm:ss
     *
     * @param phpTimeStamp
     * @return
     */
    public static String timeLeftPhpTime(String phpTimeStamp) {
        Date date = null;
        try {
            date = new Date(Long.parseLong(phpTimeStamp + "000"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ");
        String format2 = format.format(date);
        System.out.println(format2);
        long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();

        long total_seconds = (timeStamp - currentTimeStamp) / 1000;

        if (total_seconds <= 0) {
            return "";
        }

        long days = Math.abs(total_seconds / (24 * 60 * 60));

        long hours = Math.abs((total_seconds - days * 24 * 60 * 60) / (60 * 60));
        long minutes = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60) / 60);
        long seconds = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60));
        String leftTime;
        if (days > 0) {
            leftTime = days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (hours > 0) {
            leftTime = hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (minutes > 0) {
            leftTime = minutes + "分" + seconds + "秒";
        } else if (seconds > 0) {
            leftTime = seconds + "秒";
        } else {
            leftTime = "0秒";
        }

        return leftTime;
    }

    /**
     * 根据传入时间计算显示 传入时间格式2015-06-27 19:58:12.0 小于1分钟，显示为刚刚 1分钟<=t<10分钟，显示为10分钟前
     * 10分钟<=t<60分钟，显示为1小时前 1小时<=t<当天，显示为今天 日期是昨天的，显示为昨天 超过2天，显示具体日期 2015-10-10
     *
     * @param timeStr
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String timeLeftFormatTime_bak(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return timeStr;
        }

        long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();
        long seconds = (currentTimeStamp - timeStamp) / 1000;

        long minutes = Math.abs(seconds / 60);
        long hours = Math.abs(minutes / 60);
        long days = Math.abs(hours / 24);
        Date now = new Date(System.currentTimeMillis());
        if (seconds < 60) { // 小于1分钟，显示为刚刚
            return "刚刚";
        } else if (minutes < 10) { // 1分钟<=t<10分钟，显示为10分钟前
            return "10分钟前";
        } else if (minutes < 60) { // 10分钟<=t<60分钟，显示为1小时前
            return "1小时前";
        } else if (date.getDate() == now.getDate()) { // 1小时<=t<当天，显示为今天
            return "今天";
        } else if (days < 2) { // 日期是昨天的，显示为昨天
            return "昨天";
        } else { // 超过2天，显示具体日期 2015-10-10
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateString = formatter.format(date);
            return dateString;
        }
    }

    /**
     * 计算时间 第一个区间，时间戳在当天的，就显示HH:MM 第二个区间，时间戳不在当天的，显示MM/DD HH:MM
     *
     * @param timeStr
     * @return
     */
    public static String timeLeftFormatTime(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return timeStr;
        }
        Date now = new Date(System.currentTimeMillis());
        if (date.getDate() == now.getDate()) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String dateString = formatter.format(date);
            return dateString;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
            String dateString = formatter.format(date);
            return dateString;
        }
    }

    /**
     * 返回 yyyy-MM-dd HH:mm:ss日期格式
     *
     * @return
     */
    public static String getCurrentTime1() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date now = new Date(System.currentTimeMillis());
            return format.format(now);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回 yyyyMMddHHmmss日期格式
     *
     * @return
     */
    public static String getCurrentTime2() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            Date now = new Date(System.currentTimeMillis());
            return format.format(now);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回 yyyy/MM/dd/HH/mm/ss日期格式
     *
     * @return
     */
    public static String getCurrentTime3() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss", Locale.getDefault());
            Date now = new Date(System.currentTimeMillis());
            return format.format(now);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回 yyyy/MM/dd/HH/mm/ss/日期格式
     *
     * @return
     */
    public static String getCurrentTime4() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss/", Locale.getDefault());
            Date now = new Date(System.currentTimeMillis());
            return format.format(now);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取小时和分钟
     *
     * @return
     */
    public static String getHourMini(String javaTimeStamp) {
        Date date = null;
        try {
            date = new Date(Long.parseLong(javaTimeStamp));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat format = null;
        if (now.getDate() == date.getDate()) {
            //今天
            format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else {
            format = new SimpleDateFormat("MM-dd", Locale.getDefault());
        }
        String format2 = format.format(date);
        return format2;
    }

    /**
     * 传过来的时间格式是yyyy-MM-dd HH:mm:ss
     *
     * @param timeStr
     * @return
     */
    public static String getShowTime(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return timeStr;
        }
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat format = null;
        if (now.getDate() == date.getDate()) {
            //今天
            format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else {
            format = new SimpleDateFormat("MM-dd", Locale.getDefault());
        }
        String format2 = format.format(date);
        return format2;
    }

    /**
     * 当前日期是否为今天
     *
     * @param timeStr
     * @return
     */
    public static boolean isToday(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        Date now = new Date(System.currentTimeMillis());
        return date.getDate() == now.getDate();
    }

    public static String timeLeft(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();

        long total_seconds = (timeStamp - currentTimeStamp) / 1000;

        if (total_seconds <= 0) {
            return "";
        }

        long days = Math.abs(total_seconds / (24 * 60 * 60));

        long hours = Math.abs((total_seconds - days * 24 * 60 * 60) / (60 * 60));
        long minutes = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60) / 60);
        long seconds = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60));
        String leftTime;
        if (days > 0) {
            leftTime = days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (hours > 0) {
            leftTime = hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (minutes > 0) {
            leftTime = minutes + "分" + seconds + "秒";
        } else if (seconds > 0) {
            leftTime = seconds + "秒";
        } else {
            leftTime = "0秒";
        }

        return leftTime;
    }

    public static String timeLeftByMillisecond(int timeStr) {

        long total_seconds = timeStr / 1000;

        if (total_seconds <= 0) {
            return "";
        }

        long days = Math.abs(total_seconds / (24 * 60 * 60));

        long hours = Math.abs((total_seconds - days * 24 * 60 * 60) / (60 * 60));
        long minutes = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60) / 60);
        long seconds = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60));
        String leftTime;
        if (days > 0) {
            leftTime = days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (hours > 0) {
            leftTime = hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (minutes > 0) {
            leftTime = minutes + "分" + seconds + "秒";
        } else if (seconds > 0) {
            leftTime = seconds + "秒";
        } else {
            leftTime = "0秒";
        }

        return leftTime;
    }
}
