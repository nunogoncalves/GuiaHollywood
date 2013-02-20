package com.numicago.guiahollywood.objects;

import java.util.Calendar;

public enum EnumSeason 
{
	NORMAL(0, "Sem época festiva"),
	S_VALENTIM(1, "S. Valentim"),
//	CARNAVAL(1, "Carnaval"),
	ABRIL_25(2, "25 de Abril"),
	HALLOWEEN(3, "Halloween"),
	NATAL(4, "Natal");
	
	private int id;
	private String name;
	
	EnumSeason(int id, String name)
	{
		this.id = id;
		this.name = name;
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
	
	public static int returnSeasonId()
	{
		Calendar today = Calendar.getInstance();
		//S VALENTIM
		if(today.get(Calendar.MONTH) == Calendar.FEBRUARY && today.get(Calendar.DAY_OF_MONTH) >= 1 && today.get(Calendar.DAY_OF_MONTH) < 15)
			return S_VALENTIM.getId();
		//25 de ABRIL
		if(today.get(Calendar.MONTH) == Calendar.FEBRUARY && today.get(Calendar.DAY_OF_MONTH) >= 1 && today.get(Calendar.DAY_OF_MONTH) < 15)
			return S_VALENTIM.getId();
		//HALLOWEEN
		if(today.get(Calendar.MONTH) == Calendar.OCTOBER && today.get(Calendar.DAY_OF_MONTH) >= 20)
			return HALLOWEEN.getId();
		//NATAL
		if(today.get(Calendar.MONTH) == Calendar.DECEMBER)
			return NATAL.getId();
		if(today.get(Calendar.MONTH) == Calendar.NOVEMBER && today.get(Calendar.DAY_OF_MONTH) >= 18)
			return NATAL.getId();
		if(today.get(Calendar.MONTH) == Calendar.JANUARY && today.get(Calendar.DAY_OF_MONTH) <= 6)
			return NATAL.getId();
		
		return NORMAL.getId();
	}
	
}
