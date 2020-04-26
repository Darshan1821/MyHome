package com.dexter.myhome.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Boolean isDateEqual(Date date, Date date1) {

        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

        calendar.setTime(date);
        calendar1.setTime(date1);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        return calendar.getTime().compareTo(calendar1.getTime()) == 0;
    }

    public static Boolean isDateBetween(Date fromDate, Date toDate, Date date) {

        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar.setTime(date);
        calendar1.setTime(fromDate);
        calendar2.setTime(toDate);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        return calendar.getTime().compareTo(calendar1.getTime()) >= 0 && calendar.getTime().compareTo(calendar2.getTime()) <= 0;
    }
}
