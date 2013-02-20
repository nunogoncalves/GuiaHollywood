package com.numicago.guiahollywood.objects;

import java.util.Locale;

import android.content.Context;

import com.numicago.guiahollywood.R;


public enum EnumLanguage 
{
	DEFAULT(0, R.string.defaultLang, Locale.UK.getISO3Country(), "EN"),
	PORTUGAL(1, R.string.portuguese, "PT", "pt"),
	ESPANHA(2, R.string.espanha, "ES", "es");
	
	private int id;
	private int resId;
	private String country;
	private String language;
	
	EnumLanguage(int id, int langResourceStringId, String country, String language)
	{
		this.id = id;
		this.resId = langResourceStringId;
		this.country = country;
		this.language = language;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public static EnumLanguage getEnumFromId(int id)
	{
		for(EnumLanguage ec: EnumLanguage.values())
			if(ec.getId() == id)
				return ec;
		return EnumLanguage.DEFAULT;
	}
	
	public static EnumLanguage getEnumFromLanguage(String lang, Context context) {
		for(EnumLanguage ec: EnumLanguage.values()) {
			if(context.getString(ec.getResId()).equals(lang))
				return ec;
		}
		return EnumLanguage.DEFAULT;
	}
}
