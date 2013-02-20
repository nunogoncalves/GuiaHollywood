package com.numicago.guiahollywood.objects;



public enum EnumCountryCanal 
{
	PORTUGAL(0, "http://www.canalhollywood.pt", "http://canalhollywood.pt/programacion/", "yyyy-MM-dd"),
	ESPANHA(1, "http://www.canalhollywood.es", "http://www.canalhollywood.es/guia-tv/", "yyyy/MM/dd");
	
	private int id;
	private String siteBase;
	private String siteGuiaTv;
	private String dateFormat;
	
	EnumCountryCanal(int id, String siteBase, String siteGuiaTv, String dateFormat)
	{
		this.id = id;
		this.siteBase = siteBase;
		this.siteGuiaTv = siteGuiaTv;
		this.dateFormat = dateFormat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
	public String getSiteBase() {
		return siteBase;
	}

	public void setSiteBase(String siteBase) {
		this.siteBase = siteBase;
	}

	public String getSiteGuiaTv() {
		return siteGuiaTv;
	}

	public void setSiteGuiaTv(String siteGuiaTv) {
		this.siteGuiaTv = siteGuiaTv;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public static EnumCountryCanal getEnumFromId(int id)
	{
		for(EnumCountryCanal ec: EnumCountryCanal.values())
			if(ec.getId() == id)
				return ec;
		return EnumCountryCanal.PORTUGAL;
	}
}
