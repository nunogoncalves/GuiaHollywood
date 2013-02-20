package com.numicago.guiahollywood.objects;

public enum EnumInternetConnection 
{
	NO_INTERNET_CONNECTION (-1, "No internet connection"),
	WIFI_CONNECTION (1, "Wifi"),
	MOBILE_CONNECTION (0, "Mobile connection");
	
	int id;
	String name;
	
	EnumInternetConnection(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	
}
