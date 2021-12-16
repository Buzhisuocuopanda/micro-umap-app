package com.mkst.umap.base.core.util;

import com.mkst.mini.systemplus.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName Dateutil
 * @Description Umap工具类
 * @Author WangYong
 * @Date 2020-06-12 14:22
 */
@Slf4j
public class UmapDateUtils extends DateUtils {

    public static String HH_MM_SS = "HH:mm:ss";
    public static String HH_MM = "HH:mm";

    public UmapDateUtils() {
    }

    /**
     * 获取未来 任意天内的日期数组
     *
     * @param intervals intervals天内
     * @return 日期数组
     */
    public static ArrayList<Date> getFutureDaysList(int intervals) {
        ArrayList<Date> fetureDaysList = new ArrayList<>();
        for (int i = 1; i < intervals + 1; i++) {
            fetureDaysList.add(getFutureDate(i));
        }
        return fetureDaysList;
    }

    /**
     * 获取未来 任意天内的日期数组
     *
     * @param startDay 开始
     * @param dayNum   天数
     * @return 日期数组
     */
    public static ArrayList<Date> getFutureDaysList(int startDay, int dayNum) {

        ArrayList<Date> futureDaysList = new ArrayList<>();

        if (startDay < 0 || dayNum < 0) {
            log.info("startDay或dayNum不可小于零", new IllegalStateException());
            return futureDaysList;
        }

        for (int i = startDay; i < startDay + dayNum; i++) {
            futureDaysList.add(getFutureDate(i));
        }
        return futureDaysList;
    }

    /**
     * @描述 获取未来 第 feture 天的日期
     * @方法名 getFetureDate
     * @参数 [feture]
     * @返回值 java.util.Date
     * @创建人 Baldwin
     * @创建时间 2020-06-19
     * @修改人和其它信息
     */
    public static Date getFutureDate(int feture) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + feture);
        return calendar.getTime();
    }


    /**
     * @param date 提供日期的对象
     * @param time 提供时间的对象
     * @描述
     * @方法名 combine
     * @返回值 java.util.Date
     * @创建人 Baldwin
     * @创建时间 2020-06-12
     * @修改人和其它信息
     */
    public static Date combineDateTime(Date date, Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        return cal.getTime();
    }

    /**
     * @描述 将两个date按照默认格式转成Str（09:30-10:00），详情查看@combine2Str
     * @方法名 combine2Str
     * @参数 [startTime, endTime]
     * @返回值 java.lang.String
     * @创建人 Baldwin
     * @创建时间 2020-06-18
     * @修改人和其它信息
     */
    public static String combine2Str(Date startTime, Date endTime) {
        return combine2Str(startTime, endTime, HH_MM, "-");
    }

    /**
     * @描述 将两个date按照格式转成Str，然后拼接
     * @方法名 combine2Str
     * @参数 [startTime, endTime, format, separator]
     * @返回值 java.lang.String
     * @创建人 Baldwin
     * @创建时间 2020-06-18
     * @修改人和其它信息
     */
    public static String combine2Str(Date startTime, Date endTime, String format, String separator) {

        String startStr = parseDateToStr(format, startTime);
        String endStr = parseDateToStr(format, endTime);
        return startStr + separator + endStr;
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    public static boolean isToday(Date date) {
        String now = parseDateToStr(YYYY_MM_DD, getNowDate());
        String cDate = parseDateToStr(YYYY_MM_DD, date);
        return now.equals(cDate);
    }

    /**
     * @return java.lang.String
     * @Author wangyong
     * @Description 判断某个事件的进行状态
     * @Date 14:58 2020-08-11
     * @Param [startTime, endTime]
     */
    public static String eventNowStatus(Date startTime, Date endTime) {
        Date nowDate = DateUtils.getNowDate();
        //
        if (nowDate.after(endTime) || nowDate.equals(endTime)) {
            return "2";
        }

        if (nowDate.before(startTime)) {
            return "0";
        }

        return "1";
    }

    public static long getTimeGap(Date checkTime, Date target) {
        return target.getTime() - checkTime.getTime();
    }


    public static boolean happenInDuration(Date checkTime, long duration) {
        long timeGap = getTimeGap(checkTime, getNowDate());
        return timeGap < 0 && Math.abs(timeGap) < duration;
    }

    /**
     * 获取最近七天日期
     * @return
     */
    public static String getSevenDay() {
        Calendar now = Calendar.getInstance();
        now.add(5, -7);
        String endDate = (new SimpleDateFormat(YYYY_MM_DD)).format(now.getTime());
        return endDate;
    }

    /**
     * 获取最近N个月
     * 日期格式 2020年8月
     * @return
     */
    public static List<String> getTwelveMonth(int months){
        /*String[] months = new String[12];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        // 加一行代码,否则3月重复
        cal.set(Calendar.DATE,1);
        for (int i = 0; i < 12; i++) {
            months[11 - i] = cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1+"月");
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        }
        List<String> list = Arrays.asList(months);
        return list;*/
        List<String> list = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        for (int i = months; i >= 0; i--) {

            Calendar calendar = Calendar.getInstance();
            //获取当前时间的前6个月
            calendar.add(Calendar.MONTH, -i);
            //将calendar装换为Date类型
            Date date = calendar.getTime();

            System.out.println(sdf.format(date));
            list.add(sdf.format(date));
        }
        return list;
    }

    /**
     * 获取最近N年 往前
     * @param years
     * @return
     */
    public static List<String> getYear(int years){
        List<String> list = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年");
        for (int i = years; i >= 0; i--) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -i);
            //将calendar装换为Date类型
            Date date = calendar.getTime();

            System.out.println(sdf.format(date));
            list.add(sdf.format(date));
        }
        return list;
    }

    /**
     * @Author wangyong
     * @Description 获取过去n月内的月list
     * @Date 9:57 2020-11-05
     * @Param [monthNum, format, date]
     * @return java.util.List<java.lang.String>
     */
    public static List<String> getLastMonthList(int monthNum, String format, Date date){
        ArrayList<String> result = new ArrayList<>();
        for(int i = monthNum - 1; i > -1 ;i--){
            result.add(getLastMonth(i,format,date));
        }
        return result;
    }

    /**
     * @Author wangyong
     * @Description 获取过去n月
     * @Date 9:54 2020-11-05
     * @Param [i, format, date]
     * @return java.lang.String
     */
    public static String getLastMonth(int i, String format, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        return sdf.format(m);
    }

}
