package com.numicago.guiahollywood.objects;

import com.numicago.guiahollywood.NumiCal;

public class Day {

	private int id;
	
	private int movieId;
	
	private NumiCal start;
	
	private NumiCal end;
	
	private boolean alarm;
	
	private Long lengthMin;
	
	private Long lengthMillis;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	
	public NumiCal getStart() {
		return start;
	}

	public void setStart(NumiCal start) {
		this.start = start;
	}

	public NumiCal getEnd() {
		return end;
	}

	public void setEnd(NumiCal end) {
		this.end = end;
	}

	public boolean hasAlarm() {
		return alarm;
	}

	public void setHasAlarm(boolean alarm) {
		this.alarm = alarm;
	}
	
	public long getDurationInMinutes() {
		if (lengthMin  == null) {
			lengthMin = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000 / 60;
		}
		return lengthMin;
	}
	
	public long getDurationInMillis() {
		if (lengthMillis  == null) {
			lengthMillis = (end.getTimeInMillis() - start.getTimeInMillis());
		}
		return lengthMillis;
	}
	
	/**
	 * Retorna string no formato HH:mm
	 * @param minutes
	 * @return
	 */
	public String minutesToHours(int minutes)
	{
		int hours = 0;
		int min = 0;
		for(int i = 0; i < minutes; i++)
		{
			min = min + 1;
			if (min == 60)
			{
				hours = hours + 1;
				min = 0;
			}
		}
		String hourString = hours < 10 ? "0" + hours : "" + hours;
		String minString = min < 10 ? "0" + min : "" + min;
		return hourString+"h"+minString;
	}
}
