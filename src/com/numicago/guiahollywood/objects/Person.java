package com.numicago.guiahollywood.objects;

import java.util.Date;
import java.util.List;

public class Person {

	int id;
	
	String name;
	
	Date bday;
	
	String description;
	
	List<Movie> moviesList;
	
	boolean favourite;
	
	Person() {
		
	}
	
	Person(int id, String name, boolean favourite, List<Movie> moviesList) {
		this.id = id;
		this.name = name;
		this.moviesList = moviesList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Date getBday() {
		return bday;
	}

	public void setBday(Date bday) {
		this.bday = bday;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isFavourite() {
		return favourite;
	}

	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}

	public List<Movie> getMoviesList() {
		return moviesList;
	}

	public void setMoviesList(List<Movie> moviesList) {
		this.moviesList = moviesList;
	}
}
