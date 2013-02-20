package com.numicago.guiahollywood.objects;

import java.text.DecimalFormat;

import com.numicago.guiahollywood.NumiCal;

public class DayMovie {

	public DayMovie() {
		movie = new Movie();
		day = new Day();
	}
	
	private Movie movie;

	private Day day;

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public String getLocalName() {
		return movie.getLocalName();
	}
	
	public String getOriginalName() {
		return movie.getOriginalName();
	}
	
	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public boolean isOnAir() {
		NumiCal now = new NumiCal();
		 return (day.getStart().getTimeInMillis() < now.getTimeInMillis()
					&& day.getEnd().getTimeInMillis() > now.getTimeInMillis());
	}
	
	public long getDurationInMillis() {
		return day.getDurationInMillis();
	}
	
	public long getDurationInMinutes() {
		return day.getDurationInMinutes();
	}
	
	public long getMoviePassed() {
		NumiCal cal = new NumiCal();
		return cal.getTimeInMillis() - day.getStart().getTimeInMillis();
	}
	
	public String getElapsedPercentage()
	{
		long beginTime = day.getStart().getTimeInMillis();
    	long endTime = day.getEnd().getTimeInMillis();
    	long now = System.currentTimeMillis();
    	
    	float moviePassed = (now-beginTime)/1000/60;
    	float movieLenght = (endTime-beginTime)/1000/60;
    	DecimalFormat df = new DecimalFormat("#.#");
    	float percentage = (moviePassed * 100 / movieLenght);
    	
    	return day.minutesToHours((int)moviePassed) +
    			" de " + day.minutesToHours((int)movieLenght) + " (" + df.format(percentage) + "%)";
	}

	public String getTimeTable(String from, String to) {
		return from + " " + day.getStart().toString("HH:mm") + " " + 
				to +  " " + day.getEnd().toString("HH:mm");
	}

	public boolean isBeforeNow() {
		return (day.getStart().before(new NumiCal().getDate()));
	}
	
	@Override
	public String toString() {
		return "Movie id: " + movie.getId() 
				+ movie.getOriginalName() 
				+ "\n" + day.getStart().toString("yyyy-MM-dd HH:mm") 
				+ " às " 
				+ day.getEnd().toString("HH:mm");
	}
}

