/*
 * Autor: Nuno Gonçalves
 * Ultima Actualização: 2011-10-18
 */
package com.numicago.guiahollywood.objects;

import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The Class Movie.
 */
public class Movie {
	
	/** The id. */
	private int id;
	
	/** The local name. */
	private String localName;
	
	/** The original name. */
	private String originalName;
	
	/** The year. */
	private int year;
	
	/** The description. */
	private String description;
	
	/** The genre. */
	private String genre;
	
	/** The director. */
	private String director;
	
	private Director directorObjc;
	
	/** The length. */
	private String length;
	
	/** The favorite. */
	private boolean favourite;
	
	/** The watch list. */
	private boolean watchList;
	
	private float rating;
	
	/** The imagem small url. */
	private String imageSmallUrl;
	
	/** The image big blob. */
	private byte[] imageBigBlob;
	
	/** The imagem big url. */
	private String imageBigUrl;
	
	/** The image small blob. */
	private byte[] imageSmallBlob;
	
	/** The hollywood url. */
	private String hollywoodUrl;
	
	/** The channel id. */
	private int channelId;
	
	/** The count. */
	private int count;

	/** The is viewed. */
	private boolean isViewed;
	
	private List<Actor> actors;
	
	/**
	 * Movie.
	 */
	public Movie(){
		
	}
	
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}



	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}



	/**
	 * Gets the local name.
	 *
	 * @return the local name
	 */
	public String getLocalName() {
		return localName;
	}



	/**
	 * Sets the local name.
	 *
	 * @param localName the new local name
	 */
	public void setLocalName(String localName) {
		this.localName = localName;
	}



	/**
	 * Gets the original name.
	 *
	 * @return the original name
	 */
	public String getOriginalName() {
		return originalName;
	}



	/**
	 * Sets the original name.
	 *
	 * @param originalName the new original name
	 */
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}



	/**
	 * Gets the year.
	 *
	 * @return the year
	 */
	public int getYear() {
		return year;
	}



	/**
	 * Sets the year.
	 *
	 * @param year the new year
	 */
	public void setYear(int year) {
		this.year = year;
	}



	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}



	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}



	/**
	 * Gets the genre.
	 *
	 * @return the genre
	 */
	public String getGenre() {
		return genre;
	}



	/**
	 * Sets the genre.
	 *
	 * @param genre the new genre
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}



	/**
	 * Gets the director.
	 *
	 * @return the director
	 */
	public String getDirector() {
		return director;
	}



	/**
	 * Sets the director.
	 *
	 * @param director the new director
	 */
	public void setDirector(String director) {
		this.director = director;
	}



	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public String getLength() {
		return length;
	}



	/**
	 * Sets the length.
	 *
	 * @param length the new length
	 */
	public void setLength(String length) {
		this.length = length;
	}



	/**
	 * Checks if is favourite.
	 *
	 * @return true, if is favourite
	 */
	public boolean isFavourite() {
		return favourite;
	}



	/**
	 * Sets the favourite.
	 *
	 * @param favourite the new favourite
	 */
	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}



	/**
	 * Checks if is watch list.
	 *
	 * @return true, if is watch list
	 */
	public boolean isWatchList() {
		return watchList;
	}



	/**
	 * Sets the watch list.
	 *
	 * @param watchList the new watch list
	 */
	public void setWatchList(boolean watchList) {
		this.watchList = watchList;
	}


	/**
	 * Gets the rating.
	 *
	 * @return the rating
	 */
	public Float getRating() {
		return rating;
	}

	/**
	 * Sets the rating.
	 *
	 * @param rating the new rating
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}


	/**
	 * Gets the imagem small url.
	 *
	 * @return the imagem small url
	 */
	public String getImageSmallUrl() {
		return imageSmallUrl;
	}



	/**
	 * Sets the imagem small url.
	 *
	 * @param imagemSmallUrl the new imagem small url
	 */
	public void setImagemSmallUrl(String imagemSmallUrl) {
		this.imageSmallUrl = imagemSmallUrl;
	}



	public byte[] getImageBigBlob() {
		return imageBigBlob;
	}


	public void setImageBigBlob(byte[] imageBigBlob) {
		this.imageBigBlob = imageBigBlob;
	}


	/**
	 * Gets the imagem big url.
	 *
	 * @return the imagem big url
	 */
	public String getImageBigUrl() {
		return imageBigUrl;
	}



	/**
	 * Sets the imagem big url.
	 *
	 * @param imagemBigUrl the new imagem big url
	 */
	public void setImageBigUrl(String imagemBigUrl) {
		this.imageBigUrl = imagemBigUrl;
	}



	public byte[] getImageSmallBlob() {
		return imageSmallBlob;
	}


	public void setImageSmallBlob(byte[] imageSmallBlob) {
		this.imageSmallBlob = imageSmallBlob;
	}


	/**
	 * Gets the hollywood url.
	 *
	 * @return the hollywood url
	 */
	public String getHollywoodUrl() {
		return hollywoodUrl;
	}



	/**
	 * Sets the hollywood url.
	 *
	 * @param hollywoodUrl the new hollywood url
	 */
	public void setHollywoodUrl(String hollywoodUrl) {
		this.hollywoodUrl = hollywoodUrl;
	}



	/**
	 * Gets the channel id.
	 *
	 * @return the channel id
	 */
	public int getChannelId() {
		return channelId;
	}



	/**
	 * Sets the channel id.
	 *
	 * @param channelId the new channel id
	 */
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}



	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public int getCount() {
		return count;
	}


	public void setViewed(boolean isViewed) {
		this.isViewed = isViewed;
	}


	public boolean isViewed() {
		return isViewed;
	}

	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "id: " + id + ", Movie: " + localName;
	}
}
