package com.numicago.guiahollywood.dateslider;

import java.util.Calendar;


/**
 * A Labeler that displays months
 */
public class YearLabeler extends Labeler 
{
    private final String mFormatString;

    public YearLabeler(String formatString) {
        super(200, 60);
        mFormatString = formatString;
    }

    @Override
    public TimeObject add(long time, int val) 
    {
        return timeObjectfromCalendar(CalendarUtils.addYears(time, val));
    }

    @Override
    protected TimeObject timeObjectfromCalendar(Calendar c) 
    {
        return CalendarUtils.getYear(c, mFormatString);
    }
}