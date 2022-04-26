package com.bonc.jibei.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

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
    //获取当前季度
    public static Integer curQuarter() {
        LocalDate date=LocalDate.now();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date datePar = sdf.parse(date.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datePar);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int mouth = datePar.getMonth()+1;
        if(mouth>=1 && mouth<=3){
            return 1;
        }else if(mouth>=4 && mouth<=6){
            return 2;
        }else if(mouth>=7 && mouth<=9){
            return 3;
        }else if(mouth>=10 && mouth<=12){
            return 4;
        }else{
            return null;
        }
    }
    /**
     * 上个季度的开始时间
     * @param
     */
    public static String lastQrtStart(){
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

    public static void main(String[] args) {
       ;// System.out.println(getDateQrt(true));
       // System.out.println(DateUtil.getDateQrt(true).toString());
        System.out.println(lastQrtStart());
        System.out.println(lastQrtEnd());
        System.out.println(curQuarter());
    }
}
