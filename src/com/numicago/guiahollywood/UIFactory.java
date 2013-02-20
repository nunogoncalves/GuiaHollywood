package com.numicago.guiahollywood;

import com.numicago.guiahollywood.dateslider.CalendarUtils;
import com.numicago.guiahollywood.objects.EnumBackground;

/**
 * Class with components to build up the UI according to user's choises
 * @author Para
 *
 */
public class UIFactory 
{
	public int mainImageBackgroud(EnumBackground fundo)
	{
		
		if(CalendarUtils.XmasSeason())
			return R.drawable.logoprincipalxmas;
		else if(CalendarUtils.stValentineSeason())
			return R.drawable.logoprincipal_st_valentine;			
		else
			return R.drawable.logoprincipal;
	}
}
