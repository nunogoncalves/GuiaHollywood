/*
 *	Nuno Gonçalves
 *	2011-10-21
 *	Figueiró dos Vinhos 
 */
package com.numicago.guiahollywood.objects;

import java.util.Calendar;

public class Alarme 
{
	private int id;
	private int filmeId;
	private String filmeNome;
	private Calendar alarmeData;
	
	public Alarme()
	{
		
	}
	
	public Alarme(int id, int filmeId, String filmeNome, Calendar alarmeData)
	{
		this.id = id;
		this.filmeId = filmeId;
		this.filmeNome = filmeNome;
		this.alarmeData = alarmeData;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFilmeId() {
		return filmeId;
	}

	public void setFilmeId(int filmeId) {
		this.filmeId = filmeId;
	}

	public String getFilmeNome() {
		return filmeNome;
	}

	public void setFilmeNome(String filmeNome) {
		this.filmeNome = filmeNome;
	}

	public Calendar getAlarmeData() {
		return alarmeData;
	}

	public void setAlarmeData(Calendar alarmeData) {
		this.alarmeData = alarmeData;
	}
	
	
}
