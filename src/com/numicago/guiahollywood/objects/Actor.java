package com.numicago.guiahollywood.objects;

import java.util.List;

public class Actor extends Person {

	private byte[] smallImageBlob;
	private byte[] bigImageBlob;
	
	private String bigImageUrl;
	private String smalImageUrl;
	
	private String imdbUrl;
	
	private boolean favourite;
	
	public Actor() {
		super();
	}
	
	public Actor(int id, String name, boolean favourite, List<Movie> movieList) {
		super(id, name, favourite, movieList);
	}

	public byte[] getSmallImageBlob() {
		return smallImageBlob;
	}

	public void setSmallImageBlob(byte[] smallImageBlob) {
		this.smallImageBlob = smallImageBlob;
	}

	public String getBigImageUrl() {
		return bigImageUrl;
	}

	public byte[] getBigImageBlob() {
		return bigImageBlob;
	}

	public void setBigImageBlob(byte[] bigImageBlob) {
		this.bigImageBlob = bigImageBlob;
	}

	public String getSmalImageUrl() {
		return smalImageUrl;
	}

	public void setSmalImageUrl(String smalImageUrl) {
		this.smalImageUrl = smalImageUrl;
	}

	public void setBigImageUrl(String bigImageUrl) {
		this.bigImageUrl = bigImageUrl;
	}

	public void setBigImageBlob(String bigImageBlob) {
		this.bigImageUrl = bigImageBlob;
	}

	public boolean isFavourite() {
		return favourite;
	}

	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}
	
	public String getImdbUrl() {
		return imdbUrl;
	}

	public void setImdbUrl(String imdbUrl) {
		this.imdbUrl = imdbUrl;
	}

	
}
