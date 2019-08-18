package com.mengyunzhi.core.utils;

import java.sql.Timestamp;
import java.util.Calendar;

public class DateUtils {
    /**
     * 时间戳转Calendar
     *
     * @param timestamp 时间戳
     * @return Calendar
     */
    public static  Calendar timestampToCalendar(Timestamp timestamp) {
        if (timestamp != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp.getTime());
            return calendar;
        } else {
            return null;
        }
    }

    /**
     * 日历转时间戳
     *
     * @param calendar
     * @return
     */
    public static Timestamp calendarToTimestamp(Calendar calendar) {
        if (calendar != null) {
            Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
            return timestamp;
        } else {
            return null;
        }
    }
}
