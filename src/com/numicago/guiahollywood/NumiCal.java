package com.numicago.guiahollywood;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;


public class NumiCal extends GregorianCalendar {
	/**
	 * Class id.
	 */
	private static final long serialVersionUID = 1L;

	/** Number of hours in a day. */
	public static final int HOURS_IN_DAY = 24;
	
	/** Number of minutes in an hour. */
	public static final int MINS_IN_HOUR = 60;
	
	/** Number of seconds in a minute.*/
	public static final int SECS_IN_MIN = MINS_IN_HOUR;
	
	/** Number of millis in a second. */
	public static final int MILLIS_IN_SEC = 1000;
	
	/** Number of millis in a minute. */
	public static final int MILLIS_IN_MINUTE = MILLIS_IN_SEC * SECS_IN_MIN; 
	
	/** Number of millis in an hour. */
	public static final int MILLIS_IN_HOUR = MILLIS_IN_MINUTE * MINS_IN_HOUR;
	
	/** Number of millis in a day. */
	public static final int MILLIS_IN_DAY = MILLIS_IN_HOUR * HOURS_IN_DAY;
	
	/** Hash map with the api calendar id for the day and name. */
	private static final HashMap<Integer, String> DAYS_NAMES
	=  new HashMap<Integer, String>();

	/* These names could very well be avoided, and use the java API calendar
	 * names. The problem is that we can't user Locale both from client and 
	 * server sides, so we need to hard code the names of the days. 
	 */
	static { 
		DAYS_NAMES.put(Calendar.MONDAY, "MONDAY");
		DAYS_NAMES.put(Calendar.TUESDAY, "TUESDAY");
		DAYS_NAMES.put(Calendar.WEDNESDAY, "WEDNESDAY");
		DAYS_NAMES.put(Calendar.THURSDAY, "THURSDAY");
		DAYS_NAMES.put(Calendar.FRIDAY, "FRIDAY");
		DAYS_NAMES.put(Calendar.SATURDAY, "SATURDAY");
		DAYS_NAMES.put(Calendar.SUNDAY, "SUNDAY");
	}
	
	/**
	 * Instantiates a new portalcare calendar.
	 * The constructor instantiates a new object, to avoid problems with
	 * object reference.
	 *
	 * @param date the date
	 */
	public NumiCal(final Date date) {
		super();
		setTime(date);
	}
	
	/**
	 * Instantiates a new portalcare calendar.
	 */
	public NumiCal() {
		super();
	}

	/**
	 * Instantiates a new portalcare calendar. From a calendar passed as 
	 * argument.
	 *
	 * @param cal the cal
	 */
	public NumiCal(final Calendar cal) {
		super();
		setTime(cal.getTime());
	}
	
	public NumiCal(final long millis) {
		super();
		setTimeInMillis(millis);
	}
	
	/**
	 * Instantiates a new portalcare calendar and sets its time according to 
	 * all date parameters. If you wish to reset only year, month and day 
	 * consider using the proper constructor.
	 *
	 * @param year the year
	 * @param month the month
	 * @param dayOfMonth the day of month
	 * @param hour the hour
	 * @param minutes the minutes
	 * @param second the second
	 * @param milliseconds the milliseconds
	 */
	public NumiCal(final int year, final int month, 
			final int dayOfMonth, final int hour, final int minutes, 
			final int second, final int milliseconds) {
		super();
		setYear(year);
		setMonth(month);
		setDayOfMonth(dayOfMonth);
		resetTime(hour, minutes, second, milliseconds);
	}
	
	/**
	 * Instantiates a new portalcareCalendar and sets its time according to 
	 * parameters till minutes, disregarding seconds and milliseconds.
	 *
	 * @param year the year
	 * @param month the month
	 * @param dayOfMonth the day of month
	 * @param hour the hour
	 * @param minutes the minutes
	 */
	public NumiCal(final int year, final int month, 
			final int dayOfMonth, final int hour, final int minutes) {
		super();
		setYear(year);
		setMonth(month);
		setDayOfMonth(dayOfMonth);
		resetTime(hour, minutes);
	}
	
	/**
	 * Instantiates a new portalcareCalendar and sets its date, disregarding the
	 * time parameters. For a full instantiation, consider other constructors.
	 *
	 * @param year the year
	 * @param month the month (zero based)
	 * @param dayOfMonth the day of month
	 */	
	public NumiCal(final int year, final int month, 
			final int dayOfMonth) {
		super();
		setYear(year);
		setMonth(month);
		setDayOfMonth(dayOfMonth);
	}
	
	/**
	 * Gets the year of the calendar.
	 *
	 * @return the year
	 */
	public final int getYear() {
		return get(YEAR);
	}
	
	/**
	 * Sets the year.
	 *
	 * @param year the new year
	 */
	public final void setYear(final int year) {
		set(YEAR, year);
	}
	
	/**
	 * Adds the amount of years to the current date.
	 *
	 * @param years the years to be added
	 */
	public final void addYears(final int years) {
		add(YEAR, years);
	}
	
	/**
	 * Checks if the calendar passed as argument has the same year.
	 *
	 * @param cal the cal
	 * @return true, if is same year
	 */
	public final boolean isSameYear(final NumiCal cal) {
		return getYear() == cal.get(YEAR);
	}
	
	/**
	 * Checks if the calendar passed as argument has the same year.
	 *
	 * @param cal the cal
	 * @return true, if is same year
	 */
	public final boolean isSameYear(final Calendar cal) {
		return getYear() == cal.get(YEAR);
	}
	
	/**
	 * Checks if the date passed as argument has the same year.
	 *
	 * @param date the date
	 * @return true, if is same year
	 */
	public final boolean isSameYear(final Date date) {
		Calendar temp = new GregorianCalendar();
		temp.setTime(date);
		return getYear() == temp.get(YEAR);
	}
	
	/**
	 * Gets the month of the calendar.
	 *
	 * @return the month
	 */
	public final int getMonth() {
		return get(MONTH);
	}
	
	/**
	 * Sets the month.
	 *
	 * @param month the new month
	 */
	public final void setMonth(final int month) {
		set(MONTH, month);
	}
	
	/**
	 * Adds the amount of months to the current date.
	 *
	 * @param months the months
	 */
	public final void addMonths(final int months) {
		add(MONTH, months);
	}
	
	/**
	 * Checks if the calendar passed as argument has the same month.
	 *
	 * @param cal the cal
	 * @return true, if is same month
	 */
	public final boolean isSameMonth(final NumiCal cal) {
		return getMonth() == cal.getMonth();
	}
	
	/**
	 * Checks if the calendar passed as argument has the same month.
	 *
	 * @param cal the cal
	 * @return true, if is same month
	 */
	public final boolean isSameMonth(final Calendar cal) {
		return getMonth() == cal.get(MONTH);
	}
	
	/**
	 * Checks if the date passed as argument has the same month.
	 *
	 * @param theDate the date
	 * @return true, if is same month
	 */
	public final boolean isSameMonth(final Date theDate) {
		Calendar temp = new GregorianCalendar();
		temp.setTime(theDate);
		return getMonth() == temp.get(MONTH);
	}
	
	/**
	 * Gets the week of month of the calendar.
	 *
	 * @return the week of month
	 */
	public final int getWeekOfMonth() {
		return get(WEEK_OF_MONTH);
	}
	
	/**
	 * Gets the week of year of the calendar.
	 *
	 * @return the week of year
	 */
	public final int getWeekOfYear() {
		return get(WEEK_OF_YEAR);
	}
	

	/**
	 * Adds the amount of weeks to the current date.
	 *
	 * @param weeks the weeks
	 */
	public final void addWeeks(final int weeks) {
		add(WEEK_OF_YEAR, weeks);
	}
	
	/**
	 * Gets the day of year of the calendar.
	 *
	 * @return the day of year
	 */
	public final int getDayOfYear() {
		return get(DAY_OF_YEAR);
	}
	
	/**
	 * Gets the day of month of the calendar.
	 *
	 * @return the day of month
	 */
	public final int getDayOfMonth() {
		return get(DAY_OF_MONTH);
	}

	
	/**
	 * Gets the number of days of the current month.
	 *
	 * @return the month days
	 */
	public final int getMonthDays() {
		return getActualMaximum(DAY_OF_MONTH);
	}
	
	/**
	 * Sets the day of month.
	 *
	 * @param dayOfMonth the new day of month
	 */
	public final void setDayOfMonth(final int dayOfMonth) {
		set(DAY_OF_MONTH, dayOfMonth);
	}
	
	/**
	 * Gets the day of week of the calendar.
	 *
	 * @return the day of week
	 */
	public final int getDayOfWeek() {
		return get(DAY_OF_WEEK);
	}
	
	/**
	 * Get the ordinal number of the day of the week within the current month.
	 * Together with the <code>DAY_OF_WEEK</code> field, this uniquely specifies
	 * a day
	 * within a month.  Unlike <code>WEEK_OF_MONTH</code> and
	 * <code>WEEK_OF_YEAR</code>, this field's value does <em>not</em> depend on
	 * <code>getFirstDayOfWeek()</code> or
	 * <code>getMinimalDaysInFirstWeek()</code>.  <code>DAY_OF_MONTH 1</code>
	 * through <code>7</code> always correspond to <code>DAY_OF_WEEK_IN_MONTH
	 * 1</code>; <code>8</code> through <code>14</code> correspond to
	 * <code>DAY_OF_WEEK_IN_MONTH 2</code>, and so on.
	 * <code>DAY_OF_WEEK_IN_MONTH 0</code> indicates the week before
	 * <code>DAY_OF_WEEK_IN_MONTH 1</code>.  Negative values count back from the
	 * end of the month, so the last Sunday of a month is specified as
	 * <code>DAY_OF_WEEK = SUNDAY, DAY_OF_WEEK_IN_MONTH = -1</code>.  Because
	 * negative values count backward they will usually be aligned differently
	 * within the month than positive values.  For example, if a month has 31
	 * days, <code>DAY_OF_WEEK_IN_MONTH -1</code> will overlap
	 * <code>DAY_OF_WEEK_IN_MONTH 5</code> and the end of <code>4</code>.
	 *
	 * @return the day of week in month
	 * @see #DAY_OF_WEEK
	 * @see #WEEK_OF_MONTH
	 */
	public final int getDayOfWeekInMonth() {
		return get(DAY_OF_WEEK_IN_MONTH);
	}

	/**
	 * Returns a String representing the occurrence of the day in the month.
	 * <br>eg. 4TH. The name is in English format.
	 *
	 * @return the day of month occurrence
	 */
	public final String getDayOfMonthOccurrence() {
		return getDayOfWeekInMonth() 
				+ DAYS_NAMES.get(getDayOfWeek()).substring(0, 2);
	}
	
	/**
	 * Adds the amount of days to the current date.
	 *
	 * @param days the days
	 */
	public final void addDays(final int days) {
		add(DAY_OF_MONTH, days);
	}
	
	/**
	 * Checks if this date is the same day as the one passed as argument.
	 *
	 * @param cal the cal
	 * @return true, if is same day
	 */
	public final boolean isSameDay(final NumiCal cal) {
		if (getYear() == cal.getYear()) {
			return getDayOfYear() == cal.getDayOfYear();
		}
		return false;
	}
	
	/**
	 * Checks if this date is the same day as the one passed as argument.
	 *
	 * @param cal the cal
	 * @return true, if is same day
	 */
	public final boolean isSameDay(final Calendar cal) {
		return isSameDay(new NumiCal(cal));
	}
	
	/**
	 * Checks if this date is the same day as the one passed as argument.
	 *
	 * @param date the date
	 * @return true, if is same day
	 */
	public final boolean isSameDay(final Date date) {
		return isSameDay(new NumiCal(date));
	}
	
	/**
	 * Checks if the day is Monday.
	 *
	 * @return true, if is Monday
	 */
	public final boolean isMonday() {
		return getDayOfWeek() == MONDAY;
	}
	
	/**
	 * Checks if the day is Tuesday.
	 *
	 * @return true, if is Tuesday
	 */
	public final boolean isTuesday() {
		return getDayOfWeek() == TUESDAY;
	}
	
	/**
	 * Checks if the day is Wednesday.
	 *
	 * @return true, if is Wednesday
	 */
	public final boolean isWednesday() {
		return getDayOfWeek() == WEDNESDAY;
	}
	
	/**
	 * Checks if the day is Thursday.
	 *
	 * @return true, if is Thursday
	 */
	public final boolean isThursday() {
		return getDayOfWeek() == THURSDAY;
	}
	
	/**
	 * Checks if the day is Friday.
	 *
	 * @return true, if is Friday
	 */
	public final boolean isFriday() {
		return getDayOfWeek() == FRIDAY;
	}
	
	/**
	 * Checks if the day is Saturday.
	 *
	 * @return true, if is Saturday
	 */
	public final boolean isSaturday() {
		return getDayOfWeek() == SATURDAY;
	}
	
	/**
	 * Checks if the day is Sunday.
	 *
	 * @return true, if is Sunday
	 */
	public final boolean isSunday() {
		return getDayOfWeek() == SUNDAY;
	}
	
	/**
	 * Checks if the calendar represents today.
	 *
	 * @return true, if is today
	 */
	public final boolean isToday() {
		NumiCal today = new NumiCal();
		return isSameDay(today);
	}
	
	/**
	 * Checks if this instance if today after 6am and tomorrow before 6am.
	 *
	 * @return true, if the instance is today after six and tomorrow before six.
	 */
	public final boolean isTodayAfterSixAndTomorrowBeforeSix() {
		NumiCal today = new NumiCal();
		if(isSameYear(today)) {
			if(isSameDay(today)) {
				return today.getHourOfDay() > 6;
			}
			if((today.getDayOfYear()) == getDayOfYear() + 1) {
				return today.getHourOfDay() < 6;
			}
		}
		return false;   
	}
	
	/**
	 * Checks if the day of the date is last day of the month.
	 *
	 * @return true, if is last day of month
	 */
	public final boolean isLastDayOfMonth() {
		//Call actual maximum says it doesn't exist on run time.
		final int lastDayFebNonLeap = 28;
		final int lastDayFebLeap = 28;
		final int lastDayShort = 30;
		if (getDayOfMonth() < lastDayFebNonLeap) {
			return false;
		} else {
			if (getDayOfMonth() == lastDayFebNonLeap && isLeapYear()) {
				return false;
			} else {
				if (getDayOfMonth() == lastDayFebLeap && isLeapYear() 
						&& getMonth() == FEBRUARY) {
					return true;
				}
				return !(getDayOfMonth() == lastDayShort && (
						getMonth() == JANUARY || getMonth() == JANUARY 
						|| getMonth() == MARCH || getMonth() == MAY 
						|| getMonth() == JULY || getMonth() == AUGUST 
						|| getMonth() == OCTOBER || getMonth() == DECEMBER));
			}
		}
	}

	/**
	 * Gets the number of days of the year of the current date..
	 *
	 * @return the year days
	 */
	public final int getYearDays() {
		final int leapYearDays = 366;
		final int nonLeapYearDays = 365;
		return isLeapYear() ? leapYearDays : nonLeapYearDays;
	}
	
	/**
	 * Gets the difference in days between the current date, and the one passed
	 * as argument. <b>WARNING:</b> this method returns the absolute value. 
	 * Doesn't matter for the method which date occurs first.
	 *
	 * @param cal the cal
	 * @return the difference in hours
	 */
	public final float getDifferenceInDays(final NumiCal cal) {
		return getDifferenceInHours(cal) / HOURS_IN_DAY;
	}
	
	/**
	 * Gets the difference in days between the current date, and the one passed
	 * as argument. <b>WARNING:</b> this method returns the absolute value. 
	 * Doesn't matter for the method which date occurs first.
	 *
	 * @param cal the cal
	 * @return the difference in hours
	 */
	public final float getDifferenceInDays(final Calendar cal) {
		return getDifferenceInDays(new NumiCal(cal));
	}
	
	/**
	 * Gets the difference in days between the current date, and the one passed
	 * as argument. <b>WARNING:</b> this method returns the absolute value.
	 * Doesn't matter for the method which date occurs first.
	 *
	 * @param date the date
	 * @return the difference in hours
	 */
	public final float getDifferenceInDays(final Date date) {
		return getDifferenceInDays(new NumiCal(date));		
	}
	
	/**
	 * Gets the hour of the calendar.
	 *
	 * @return the hour of day
	 */
	public final int getHourOfDay() {
		return get(HOUR_OF_DAY);
	}
	
	/**
	 * Adds the amount of hours passed as argument to the calendar.
	 *
	 * @param hours the hours to add to the calendar
	 */
	public final void addHours(final int hours) {
		add(HOUR_OF_DAY, hours);
	}
	
	/**
	 * Gets the difference in hours between the current date, and the one passed
	 * as argument. <b>WARNING:</b> this method returns the absolute value. 
	 * Doesn't matter for the method which date occurs first.
	 *
	 * @param cal the cal
	 * @return the difference in hours
	 */
	public final long getDifferenceInHours(final NumiCal cal) {
		return Math.abs((cal.getTimeInMillis() - this.getTimeInMillis())	
				/ MILLIS_IN_HOUR);
	}
	
	/**
	 * Gets the difference in hours between the current date, and the one passed
	 * as argument. <b>WARNING:</b> this method returns the absolute value. 
	 * Doesn't matter for the method which date occurs first.
	 *
	 * @param cal the cal
	 * @return the difference in hours
	 */
	public final long getDifferenceInHours(final Calendar cal) {
		return getDifferenceInHours(new NumiCal(cal));		
	}
	
	/**
	 * Gets the difference in hours.
	 *
	 * @param date the date
	 * @return the difference in hours
	 */
	public final long getDifferenceInHours(final Date date) {
		return getDifferenceInHours(new NumiCal(date));
	}
	
	/**
	 * Gets the minute of the calendar.
	 *
	 * @return the minute
	 */
	public final int getMinute() {
		return get(MINUTE);
	}
	
	/**
	 * Adds the amount of minutes passed as argument to the calendar.
	 *
	 * @param minutes the minutes to add to the calendar
	 */
	public final void addMinutes(final int minutes) {
		add(MINUTE, minutes);
	}
	
	/**
	 * Gets the second of the calendar.
	 *
	 * @return the second
	 */
	public final int getSecond() {
		return get(SECOND);
	}
	
	/**
	 * Gets the milliseconds of the calendar.
	 *
	 * @return the millisecond
	 */
	public final int getMilisecond() {
		return get(MILLISECOND);
	}
	
	/**
	 * Sets the time up until the seconds.
	 *
	 * @param hours the hours
	 * @param minutes the minutes
	 * @param seconds the seconds
	 */
	public final void setTime(final int hours, final int minutes,
			final int seconds) {
		setTime(hours, minutes);
		set(SECOND, seconds);
	}
	
	/**
	 * Sets the time up until the minutes.
	 *
	 * @param hours the hours
	 * @param minutes the minutes
	 */
	public final void setTime(final int hours, final int minutes) {
		set(HOUR_OF_DAY, hours);
		set(MINUTE, minutes);
	}
	
	/**
	 * Sets the time from a date. Extracts the hours, minutes and seconds and 
	 * set them to the calendar.
	 *
	 * @param date the new time from date
	 */
	public final void setTimeFromDate(final Date date) {
		setTime(new NumiCal(date));
	}
	
	/**
	 * Sets the time from a date. Extracts the hours and minutes (disregards the
	 * rest) and set them to the calendar.
	 *
	 * @param date the new time from date
	 */
	public final void setTimeFromDateToMinutes(final Date date) {
		NumiCal cal = new NumiCal(date);
		setTime(cal.getHourOfDay(), cal.getMinute());
	}
	
	/**
	 * Sets the time from a calendar. Extracts the hours and minutes (
	 * disregards the rest) and set them to the calendar.
	 *
	 * @param cal the new time from date to minutes
	 */
	public final void setTimeFromCalToMinutes(final Calendar cal) {
		NumiCal temp = new NumiCal(cal);
		setTime(temp.getHourOfDay(), temp.getMinute());
	}
	
	/**
	 * Sets the time from a NumiCal. Extracts the hours and minutes
	 * (disregards the rest) and set them to the calendar.
	 *
	 * @param cal the new time from date to minutes
	 */
	public final void setTimeFromCalToMinutes(
			final NumiCal cal) {
		setTime(cal.getHourOfDay(), cal.getMinute());
	}
	
	/**
	 * Sets the time from a calendar. Extracts the hours, minutes and seconds 
	 * and set them to the calendar.
	 *
	 * @param cal the new time from calendar
	 */
	public final void setTime(final Calendar cal) {
		setTime(new NumiCal(cal));
	}
	
	/**
	 * Sets the time from a date. Extracts the hours, minutes and seconds and 
	 * set them to the calendar.
	 *
	 * @param cal the new time from NumiCal
	 */
	public final void setTime(final NumiCal cal) {
		setTime(cal.getHourOfDay(), cal.getMinute(), cal.getSecond());
	}
	
	/**
	 * Check if the calendar passed as argument represents the same time as the
	 * this. The comparison checks the time in milliseconds.
	 *
	 * @param cal the calendar to compare with this.
	 * @return true, if is same date
	 */
	public final boolean isSameDateToMilis(final NumiCal cal) {
		return getTimeInMillis() == cal.getTimeInMillis();
	}
	
	/**
	 * Check if the calendar passed as argument represents the same time as the
	 * this. The comparison checks the time in milliseconds.
	 *
	 * @param cal the calendar to compare with this.
	 * @return true, if is same date
	 */
	public final boolean isSameDateToMilis(final Calendar cal) {
		return getTimeInMillis() == cal.getTimeInMillis();
	}
	
	/**
	 * Check if the date passed as argument represents the same time as the
	 * this. The comparison checks the time in milliseconds.
	 *
	 * @param date the date to compare with this
	 * @return true, if is same date
	 */
	public final boolean isSameDateToMilis(final Date date) {
		NumiCal cal = new NumiCal(date);
		return getTimeInMillis() == cal.getTimeInMillis();
	}
	
	/**
	 * Compares if the two calendars represent the same date, the comparison is 
	 * made until the minutes field. Which means that even if second or 
	 * milliseconds are different from each other, this method will return true.
	 * For the full comparison, consider one of
	 * the <code>isSameDateToMilis()</code> methods.
	 *
	 * @param cal the calendar
	 * @return true, if the calendars represent the same date (to minutes).
	 */
	public final boolean isSameDateToMinutes(final NumiCal cal) {
		if (getYear() == cal.getYear()) {
			if (getDayOfYear() == cal.getDayOfYear()) {
				if (getHourOfDay() == cal.getHourOfDay()) {
					if (getMinute() == cal.getMinute()) {
						return true;
					}
				}
			}
		} 
		return false;
	}
	
	/**
	 * Compares if the two calendars represent the same date, the comparison is 
	 * made until the minutes field. Which means that even if second or 
	 * milliseconds are different from each other, this method will return true.
	 * For the full comparison, consider one of 
	 * the <code>isSameDateToMilis()</code> methods.
	 *
	 * @param cal the calendar
	 * @return true, if the calendars represent the same date (to minutes).
	 */
	public final boolean isSameDateToMinutes(final Calendar cal) {
		return isSameDateToMinutes(new NumiCal(cal));
	}
	
	/**
	 * Compares if the two calendars represent the same date, the comparison is 
	 * made until the minutes field. Which means that even if second or 
	 * milliseconds are different from each other, this method will return true.
	 * For the full comparison, consider one of
	 * the <code>isSameDateToMilis()</code> methods.
	 *
	 * @param date the date
	 * @return true, if the calendars represent the same date (to minutes).
	 */
	public final boolean isSameDateToMinutes(final Date date) {
		return isSameDateToMinutes(new NumiCal(date));
	}
	
	/**
	 * * Checks if the first date's day is before or the same as
	 * the second date's day.
	 * <br> <b>ATTENTION:</b> This method only cares about date, not hours.
	 * If Date 1 is one hour
	 * ealier than Date 2, it still returns false.
	 * @param cal the cal
	 * @return true if the first date is before or same day
	 */
	public final boolean isSameOrPastDay(final NumiCal cal) {
		if (cal == null) {
			return false;
		}

		if (getYear() > cal.getYear()) {
			return false;
		} else if (getYear() < cal.getYear()) {
			return true;
		} else if (getYear() == cal.getYear()) {
			if (getDayOfYear() <= cal.getDayOfYear()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the first date's day is before the second date's day.
	 *
	 * @param cal the cal
	 * @return true if the first date is previous to the second
	 */
	public final boolean isPastDay(final NumiCal cal) {
		if (cal == null) {
			return false;
		}
		
		if (getYear() > cal.getYear()) {
			return false;
		} else if (getYear() < cal.getYear()) {
			return true;
		} else if (isSameYear(cal)) {
			if (getDayOfYear() < cal.getDayOfYear()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the first date's day is before the second date's day.
	 *
	 * @param cal the cal
	 * @return true if the first date is previous to the second
	 */
	public final boolean isPastDay(final Calendar cal) {
		return isPastDay(new NumiCal(cal));
	}
	
	/**
	 * Checks if the first date's day is before the second date's day.
	 *
	 * @param date the date
	 * @return true if the first date is previous to the second
	 */
	public final boolean isPastDay(final Date date) {
		return isPastDay(new NumiCal(date));
	}
	
	/**
	 * * Checks if the first date's day is before or the same as
	 * the second date's day.
	 * <br> ATTENTION: This method only cares about date, not hours.
	 * If Date 1 is one hour
	 * ealier than Date 2, it still returns false.
	 *
	 * @param cal the cal
	 * @return true if the first date is before or same day
	 */
	public final boolean isSameOrPastDay(final Calendar cal) {
		return isSameOrPastDay(new NumiCal(cal));
	}
	
	/**
	 * * Checks if the first date's day is before or the same as
	 * the second date's day.
	 * <br> ATTENTION: This method only cares about date, not hours.
	 * If Date 1 is one hour
	 * ealier than Date 2, it still returns false.
	 *
	 * @param date the date
	 * @return true if the first date is before or same day
	 */
	public final boolean isSameOrPastDay(final Date date) {
		return isSameOrPastDay(new NumiCal(date));
	}
	
	/**
	 * Checks if the year is leap.
	 *
	 * @return true, if is leap year
	 */
	public final boolean isLeapYear() {
		final int fourHundred = 400;
		final int hundred = 100;
		final int four = 4;
		
		if (getYear() % fourHundred == 0) {  
		   return true;
		} else if (getYear() % hundred == 0) { 
		   return false;
		}
		
		return (getYear() % four == 0); 
	}
	
	@Override
	public final String toString() {
		return getDate().toString();
	}
	
	/**
	 * Returns a string representation of the calendar according to the pattern.
	 *
	 * @param pattern the pattern
	 * @return the string
	 */
	public final String toString(final String pattern) {
		SimpleDateFormat dtf = new SimpleDateFormat(pattern);
		return dtf.format(this.getTime());
	}
	
	/**
	 * Sets the calendar.
	 * @param calendar the calendar to set
	 */
	public final void setCalendar(final Calendar calendar) {
		setTime(calendar.getTime());
	}

	/**
	 * Gets the date. This method returns the date inside this panel.
	 * @return the date
	 */
	public final Date getDate() {
		return getTime();
	}

	/**
	 * Sets the date. Only instantiates new objects if the contructor used was 
	 * the no-arg one.
	 * @param date the date to set
	 */
	public final void setDate(final Date date) {
		setTime(date);
	}
	
	/**
	 * Resets the time of the calendar. With regard to all time fields.
	 * If you don't wish to reset all fields, consider one of the other 
	 * resetTime methods.
	 *
	 * @param hour the hour
	 * @param minutes the minutes
	 * @param seconds the seconds
	 * @param milliseconds the milliseconds
	 */
	public final void resetTime(final int hour, final int minutes, 
			final int seconds, final int milliseconds) {
		resetTime(hour, minutes);
		set(SECOND, seconds);
		set(MILLISECOND, milliseconds);
	}
	
	/**
	 * Reset the time of the calendar. No regard to seconds and milliseconds. 
	 * To reset all time fields, call: <br><br>
	 * {@link <code>resetTime(hour, minutes, 
			seconds, milliseconds)</code> } or resetTime with a calendar or date
			as argument.
	 *
	 * @param hour the hour
	 * @param minutes the minutes
	 */
	public final void resetTime(final int hour, final int minutes) {
		set(HOUR_OF_DAY, hour);
		set(MINUTE, minutes);
	}
	
	/**
	 * Reset the time of the calendar.
	 * If you don't wish to reset all fields, consider one of the other 
	 * resetTime methods.
	 *
	 * @param cal the calendar
	 */
	public final void resetTime(final Calendar cal) {
		NumiCal temp = new NumiCal(cal);
		resetTime(temp.getHourOfDay(), temp.getMinute(), temp.getSecond(),
				temp.getMilisecond());
	}
	
	/**
	 * Reset the time of the calendar. With regard to all time fields.
	 * If you don't wish to reset all fields, consider one of the other 
	 * resetTime methods.
	 *
	 * @param cal the cal
	 */
	public final void resetTime(final NumiCal cal) {
		NumiCal temp = new NumiCal(cal);
		resetTime(temp.getHourOfDay(), temp.getMinute(), temp.getSecond(),
				temp.getMilisecond());
	}
	
	/**
	 * Reset the time of the calendar. With regard to all time fields.
	 * If you don't wish to reset all fields, consider one of the other 
	 * resetTime methods.
	 *
	 * @param date the date
	 */
	public final void resetTime(final Date date) {
		NumiCal temp = new NumiCal(date);
		resetTime(temp.getHourOfDay(), temp.getMinute(), temp.getSecond(),
				temp.getMilisecond());	
	}
	
	/**
	 * This method checks if the current selected day is the fourth occurrence
	 * on the month. And if that month has only four occurrences of the day.
	 * <br>eg. check if Monday is the fourth Monday on the month, and if
	 * the month has "only" 4 Mondays.
	 *
	 * @return return true if both scenarios are true, false otherwise.
	 */
	public final boolean isDayFourthOnFourWeeksMonth() {
		final int fourthWeek = 4;
		//First check if the current day is the fourth occurrence of the month 
		if (getDayOfWeekInMonth() == fourthWeek) {
			//if the day is the fourth, then check if there are 4 or 5 weeks. 
			//add a new week and then check if the week of month is one, or not
			addWeeks(1);
			//means we are on a new month
			return getDayOfWeekInMonth() == 1 ? true : false;
		} else {
			return false;
		}
	}
	
	public static int calculateAge(Date date) {
		int age = 0;

		NumiCal current = new NumiCal();
		
	    //calendar 1 with birthdate
		NumiCal bdaycal = new NumiCal(date); //original bday date
	    NumiCal tempCal = new NumiCal(date); //this year with month and day of bday date
	    
	    tempCal.setYear(current.getYear());
	    
	    age = (current.getYear() - bdaycal.getYear()) - 1;
	    
	    if (tempCal.getDayOfYear() <= current.getDayOfYear()) {
	    	age++;
	    }
	    return age;
	}
}
