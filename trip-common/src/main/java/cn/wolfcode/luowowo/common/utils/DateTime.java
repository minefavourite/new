package cn.wolfcode.luowowo.common.utils;

import java.util.Calendar;
import java.util.Date;

public class DateTime {

    public static long getDate(Date d1,Date d2){
        return Math.abs((d1.getTime()-d2.getTime())/1000);
    }

    public static  Date getEndDate(Date date){
        if (date==null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        return calendar.getTime();
    }
}
