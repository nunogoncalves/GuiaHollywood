package com.numicago.guiahollywood.objects;

import com.numicago.guiahollywood.R;

public enum EnumBackground 
{
	DEFAULT(0, 
			"default", 
			R.drawable.list_item_fundo, 
			EnumSeason.NORMAL.getId()), 
	EMPTY(1, 
			"empty", 
			R.drawable.list_item_fundo_st_empty, 
			EnumSeason.NORMAL.getId()), 
	XMAS(2, 
			"xmas", 
			R.drawable.list_item_fundo_xmas, 
			EnumSeason.NATAL.getId()), 
	LOVE(3, 
			"love", 
			R.drawable.list_item_fundo_st_valentine, 
			EnumSeason.S_VALENTIM.getId());
	
	private int id;
	private String name;
	private int resource;	
	private int seasonId;
	
	private EnumBackground(int id, String name, int resource, int season)
	{
		this.id = id;
		this.name = name;
		this.resource = resource;
		this.seasonId = season;
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
	
	public int getResource() {
		return resource;
	}
	
	public void setResource(int resource) {
		this.resource = resource;
	}
	
		
	public int getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(int seasonId) {
		this.seasonId = seasonId;
	}

	public static EnumBackground getEnumFromSeason(int seasonId) {
		for (EnumBackground fundo: EnumBackground.values())
		{
			if (fundo.getSeasonId() == seasonId)
			{
				if(seasonId == fundo.getSeasonId())
				return fundo;
			}
		}
		return EnumBackground.DEFAULT;
	}
	
	public static int getResourceFromId(int id)
	{
		for (EnumBackground fundo: EnumBackground.values())
		{
			if (fundo.getId() == id)
			{
				if(id == EnumBackground.XMAS.getId())
					return ((Math.random()*6 < 3)? fundo.getResource() : EnumBackground.DEFAULT.getResource());
				return fundo.getResource();
			}
		}
		return EnumBackground.DEFAULT.getResource();
	}
	
	public static int getResourceFromSeason(int seasonId)
	{
		for (EnumBackground fundo: EnumBackground.values())
		{
			if (fundo.getSeasonId() == seasonId)
			{
				return fundo.getResource();
			}
		}
		return EnumBackground.DEFAULT.getResource();
	}
	
	public static String getNameFromId(int id)
	{
		for (EnumBackground fundo: EnumBackground.values())
		{
			if (fundo.getId() == id)
				return fundo.getName();
		}
		return EnumBackground.DEFAULT.getName();	
	}
	
	/**
	 * Retorna a resource para o fundo do dia. Se a época não for festiva retorna -1 
	 * pois existem mais do que uma possibilidade para épocas sem festividades.
	 * Como default, ou sem fundo..
	 * @return resourceID ou -1 se época normal do ano
	 */
	public static int getSeasonResource()
	{
		int seasonId = EnumSeason.returnSeasonId();
		if(seasonId == EnumSeason.NORMAL.getId())
			return -1;
		else
			return getResourceFromSeason(seasonId);
	}

	/***
	 * Retorna o id do botão correspondente, ou então -1 se a data for normal.
	 * @return
	 */
	public static int getIdFromSeason()
	{
		int seasonId = EnumSeason.returnSeasonId();
		if(seasonId == EnumSeason.NORMAL.getId())
			return -1;
		else
			return getIdFromSeason(seasonId);
	}
	
	private static int getIdFromSeason(int seasonId)
	{
		for (EnumBackground fundo: EnumBackground.values())
		{
			if (fundo.getSeasonId() == seasonId)
				return fundo.getId();
		}
		return EnumBackground.DEFAULT.getId();
	}
}
