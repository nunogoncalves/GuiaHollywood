package com.numicago.guiahollywood.dateslider;

import java.util.Calendar;


/**
 * A Labeler that displays days
 */
public class DayLabeler extends Labeler {
    private final String mFormatString;

    public DayLabeler(String formatString) {
        super(150, 60);
        mFormatString = formatString;
    }

    @Override
    public TimeObject add(long time, int val) {
        return timeObjectfromCalendar(CalendarUtils.addDays(time, val));
    }

    @Override
    protected TimeObject timeObjectfromCalendar(Calendar c) {
        return CalendarUtils.getDay(c, mFormatString);
    }
}