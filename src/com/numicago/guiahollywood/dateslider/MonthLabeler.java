package com.numicago.guiahollywood.dateslider;

import java.util.Calendar;


/**
 * A Labeler that displays months
 */
public class MonthLabeler extends Labeler {
    private final String mFormatString;

    public MonthLabeler(String formatString) {
        super(180, 60);
        mFormatString = formatString;
    }

    @Override
    public TimeObject add(long time, int val) {
        return timeObjectfromCalendar(CalendarUtils.addMonths(time, val));
    }

    @Override
    protected TimeObject timeObjectfromCalendar(Calendar c) {
        return CalendarUtils.getMonth(c, mFormatString);
    }
}