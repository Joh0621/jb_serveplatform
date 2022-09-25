package com.bonc.jibei.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/25 16:40
 * @Description: TODO
 */
public class DateUtil {
    /**
    * 获取当前日期所在季度的开始日期和结束日期
    * 季度一年四季， 第一季度：1月-3月， 第二季度：4月-6月， 第三季度：7月-9月， 第四季度：10月-12月
    * @param isFirst  true表示查询本季度开始日期  false表示查询本季度结束日期
    * @return
    */
    public static LocalDate getDateQrt(Boolean isFirst){
        LocalDate today=LocalDate.now();
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        Month month = today.getMonth();
        Month firstMonthOfQuarter = month.firstMonthOfQuarter();
        Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        if (isFirst) {
            resDate = LocalDate.of(today.getYear(), firstMonthOfQuarter, 1);
        } else {
            resDate = LocalDate.of(today.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(today.isLeapYear()));
      }
            return resDate;
     }
    //获取当前季度
    public static String getQuarterByDate(String date) throws ParseException {
        if(date == ""|| "".equals(date)){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date datePar = sdf.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datePar);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int mouth = datePar.getMonth()+1;
        if(mouth>=1 && mouth<=3){
            return year + "年第一季度";
        }else if(mouth>=4 && mouth<=6){
            return year + "年第二季度";
        }else if(mouth>=7 && mouth<=9){
            return year + "年第三季度";
        }else if(mouth>=10 && mouth<=12){
            return year + "年第四季度";
        }else{
            return "";
         }
     }

    /**
    * 获取上一季度所在年份
    **/
    public static Integer lastQrtYear() {
        int n= LocalDate.now().getMonthValue();
        int y=LocalDate.now().getYear();
        int m=(n-1)/3;
        return m>0?y:y-1;
    }
    /**
     * 获取上一季度
     **/
    public static Integer lastQrt() {
        int n= LocalDate.now().getMonthValue();
        n=((n-1)/3+3)%4+1;
        return n;
    }
    /**
     * 上个季度的开始时间
     * @param
     */
    public static String lastQrtStart(){
        /**
         * LocalDate now = LocalDate.of(2021,1,2);
         *         Month month = now.getMonth().firstMonthOfQuarter();
         *         LocalDate endTime = LocalDate.of(now.getYear(), month.getValue(), 1);
         *         LocalDate startTime = endTime.minusMonths(3);
         */
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, ((int) startCalendar.get(Calendar.MONTH) / 3 - 1) * 3);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMinTime(startCalendar);
        return format.format(startCalendar.getTime());
    }
    /**
     * 上个季度的结束时间
     * @param
     */
    public static String lastQrtEnd(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, ((int) endCalendar.get(Calendar.MONTH) / 3 - 1) * 3 + 2);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);
        Date d=endCalendar.getTime();
        return format.format(endCalendar.getTime());
    }
    /**
     * 最小时间
     *
     * @param calendar
     */
    private static void setMinTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 最大时间
     *
     * @param calendar
     */
    private static void setMaxTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
    }

    /**
     * @param year:指定年
     * @param qrt：指定季度
     *  desc: 取指定年和季度的开始和结束时间
     */

    public static Map<String,String> getStartByYearQrt(Integer year,Integer qrt) {
        Map<String,String> dateMap=new HashMap<>();
        String startDateTime="";
        String endDateTime="";
        switch (qrt){
            case 1:
                startDateTime=year+"-01-01 00:00:00";
                endDateTime=year+"-03-31 23:59:59";
                break;
            case 2:
                startDateTime=year+"-04-01 00:00:00";
                endDateTime=year+"-06-30 23:59:59";
                break;
            case 3:
                startDateTime=year+"-07-01 00:00:00";
                endDateTime=year+"-09-30 23:59:59";
                break;
            case 4:
                startDateTime=year+"-10-01 00:00:00";
                endDateTime=year+"-12-31 23:59:59";
                break;
            default:
                startDateTime=year+"-10-01 00:00:00";
                endDateTime=year+"-12-31 23:59:59";
        }
        dateMap.put("startDate",startDateTime);
        dateMap.put("endDate",endDateTime);
        return dateMap;
    }
    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(Long lt,String formatType){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatType);
        Date date = new Date(lt);
        String res = simpleDateFormat.format(date);
        return res;
    }
    /*
     * 获取今年月份集合
     */
    public static List<String> getMonthOfYear(String year) {

        Calendar calendar = Calendar.getInstance();
        List<String> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MM");
        //判断是否本年
        if(year.equals(String.valueOf(calendar.get(Calendar.YEAR)))){
            System.out.println("222");
            String firstday = format.format(calendar.getTime());
            System.out.println(firstday);
            for (int i = 1; i <= Double.valueOf(firstday); i++) {
                if (i < 10) {
                    String month = "0" + i;
                    list.add(month);
                }

            }
        }else {
            list = Arrays.asList("01","02","03","04","05","06","07","08","09","10","11","12");
        }
        Date date = null;
        try {
            date = format.parse(year);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        return list;
    }
}
