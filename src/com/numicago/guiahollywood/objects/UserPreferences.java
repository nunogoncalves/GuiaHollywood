package com.numicago.guiahollywood.objects;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.Utils;

public class UserPreferences {

	public static final String PREFERENCES_FILE = "estilo_file";
	
	private static final String PAIS_SELECCIONADO = "paisSeleccionado";
	
	private static final String TEMA = "theme";
	
	private static final String ALERT = "alarm_time";
	
	private static final String SEASON_PREVAILS = "seasonPrevails";
	
	private static final String SORT_ORDER = "ordenacao";
	
	private static final String SORT_ASC_DESC = "ascDesc";
	
	private static final String SEARCH_BY = "procura";
	
	public static final String CHANNEL_PREF_KEY = "channel";
	
	public static final String LANGUAGE_PREF_KEY = "languageV1";
	
	public static final int SORT_LOCAL_NAME = 0;
	public static final int SORT_ORIGINAL_NAME = 1;
	public static final int SORT_ID = 2;
	public static final int SORT_COUNT = 3;
	
	public static final int SEARCH_NOME_LOCAL = 0;
	public static final int SEARCH_NOME_ORIGINAL = 1;
	
	public static final int NO_INTERNET_CONNECTION = -1;
	public static final int WIFI_CONNECTION = 1;
	public static final int MOBILE_CONNECTION = 0;
	
	public static final String ACTOR_SORT_ORDER = "actorSortOrder";
	public static final int SORT_ACTOR_BY_NAME = 0;
	public static final int SORT_ACTOR_BY_AGE = 1;
	public static final int SORT_ACTOR_BY_N_MOVIES = 2;
	
	private static final String DATA_PLAN_KEY = "data_plan_options"; 
	
	private EnumLanguage enumLanguage;
	
	SharedPreferences settings;
	
	Context context;
	static Context staticContext;
	
	ConnectivityManager cm;

	static SharedPreferences staticSettings;
	
	/** Hash map with the api calendar id for the day and name. */
	private static final HashMap<String, Integer> ALERT_TIME =  new HashMap<String, Integer>();

	public static final HashMap<String, Integer> DISPLAY_CHANNEL =  new HashMap<String, Integer>();
	
//	private static final HashMap<String, Integer> APP_LANGUAGE =  new HashMap<String, Integer>();

	private static final HashMap<String, Integer> THEME_HASH =  new HashMap<String, Integer>();
	
	public UserPreferences(Context context) {
		this.context = context;
		
		staticContext = context;

		settings = PreferenceManager.getDefaultSharedPreferences(context);
		staticSettings = PreferenceManager.getDefaultSharedPreferences(context);
		cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (context instanceof Activity) {
			setLocale();
		}
		
		ALERT_TIME.put(context.getString(R.string.oneMin), 1);
		ALERT_TIME.put(context.getString(R.string.tenMin), 10);
		ALERT_TIME.put(context.getString(R.string.fifteenMin), 15);
		ALERT_TIME.put(context.getString(R.string.twentyMin), 20);
		ALERT_TIME.put(context.getString(R.string.thirtyMin), 30);
		ALERT_TIME.put(context.getString(R.string.oneHour), 60);
		
		DISPLAY_CHANNEL.put(context.getString(R.string.canalPortugal), 0);
		DISPLAY_CHANNEL.put(context.getString(R.string.canalEspanha), 1);
		
		THEME_HASH.put(context.getString(R.string.defaultTheme), 0);
		THEME_HASH.put(context.getString(R.string.emptytheme), 1);
		THEME_HASH.put(context.getString(R.string.xmasTheme), 2);
		THEME_HASH.put(context.getString(R.string.stValentineTheme), 3);
		THEME_HASH.put(context.getString(R.string.whitetheme), 4);
		
//		settings = context.getSharedPreferences(PREFERENCES_FILE, 0);
	}

	public void setLocale() {
		
		//This try catch is for the possibility of the aplication having a string instead of an int. 
		try {
			settings.getString(LANGUAGE_PREF_KEY, context.getString(
					com.numicago.guiahollywood.R.string.defaultLang));
		} catch (Exception e) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt(LANGUAGE_PREF_KEY, EnumLanguage.DEFAULT.getId()); 
			editor.commit();
		}
		
		EnumLanguage enumLang;
		enumLang = EnumLanguage.getEnumFromId(Integer.parseInt(settings.getString(LANGUAGE_PREF_KEY, "0")));
//		if (context != null) {
//			enumLang = EnumLanguage.getEnumFromLanguage(
//					settings.getString(LANGUAGE_PREF_KEY, context.getString(
//							com.numicago.guiahollywood.R.string.defaultLang)), context);
//		} else {
//			enumLang = EnumLanguage.getEnumFromId(settings.getInt(LANGUAGE_PREF_KEY, 0));
//			enumLang = EnumLanguage.getEnumFromId(
//					settings.getInt(LANGUAGE_PREF_KEY, Integer.parseInt(context.getString(
//							com.numicago.guiahollywood.R.string.defaultLang)), (Activity) staticContext)));
//		}
		
		Locale locale = new Locale(enumLang.getCountry(), enumLang.getLanguage());
//		Locale locale = new Locale("PT", "pt");
//		Locale locale = new Locale("GB", "en");
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		((Activity) context).getBaseContext().getResources().updateConfiguration(
				config, ((Activity) context).getBaseContext().getResources().getDisplayMetrics());
	}
	
	public void setLanguage(EnumLanguage ec) {
		this.enumLanguage = ec;
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(PAIS_SELECCIONADO, ec.getId()); 
		editor.commit();
	}
	
	public int getdBVersion() {
		return settings.getInt(Utils.DATABASE_VERSION_STRING, 1);
	}

	public EnumLanguage getEnumLanguage() {
		return enumLanguage;
	}
	
	public int getTheme() {
		Integer themeVal = THEME_HASH.get(settings.getString(TEMA, 
				context.getString(com.numicago.guiahollywood.R.string.defaultTheme)));
		if (themeVal == null) {
			Editor editor = settings.edit();
			editor.putString(TEMA, context.getString(R.string.defaultTheme));
			editor.commit();
			return 0;
		}
		return themeVal;
	}
	
	public boolean seasonPrevails() {
		return settings.getBoolean(SEASON_PREVAILS, false);
	}
	
	public int getActorSortId() {
		return EnumActorSort.getIdFromSortName(
				settings.getString(UserPreferences.ACTOR_SORT_ORDER, 
						context.getString(R.string.actorName)));
	}
	
	public int getDisplayChannel() {
		return DISPLAY_CHANNEL.get(settings.getString(CHANNEL_PREF_KEY, 
				context.getString(com.numicago.guiahollywood.R.string.canalPortugal)));
	}

	public int getAlarmTimeBefore() {
		Integer alarm = ALERT_TIME.get(settings.getString(ALERT, 
				context.getString(com.numicago.guiahollywood.R.string.oneMin)));
		if (alarm == null) {
			Editor editor = settings.edit();
			editor.putString(ALERT, context.getString(R.string.tenMin));
			editor.commit();
			return 0;
		}
		return alarm;
	}

	public boolean isConnected() {
		return cm.getActiveNetworkInfo() != null 
				&& cm.getActiveNetworkInfo().isAvailable() 
				&& cm.getActiveNetworkInfo().isConnected(); 
	}

	public int getConnectionType() {
		// se ligado ver tipo de ligação
		if(isConnected()) {
			if (cm.getActiveNetworkInfo().isAvailable() 
					&& cm.getActiveNetworkInfo().isConnected()) {
				if(cm.getActiveNetworkInfo().getType() == WIFI_CONNECTION) {
					return WIFI_CONNECTION;
				} else if (cm.getActiveNetworkInfo().getType() == MOBILE_CONNECTION) {
					return MOBILE_CONNECTION;
				} else {
					return NO_INTERNET_CONNECTION;
				}
			} else { 
				return NO_INTERNET_CONNECTION;
			}
		} else {
			return NO_INTERNET_CONNECTION;
		}
	}

	
	/**
	 * Checks if is connected and can load images
	 *
	 *	if is connected and (conection type = wifi or (connection = mobile and data plan download all)) 
	 *
	 * @return true, if is connected and image load
	 */
	public boolean isConnectedAndImageLoad() {
		return isConnected() &&
				(getConnectionType() == WIFI_CONNECTION || (getConnectionType() == MOBILE_CONNECTION 
        		&& getDataPlanOption() == EnumDataPlan.DOWN_ALL));
	}
	
	public void setdBVersion(int dBVersion) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(Utils.DATABASE_VERSION_STRING, dBVersion); 
		editor.commit();
	}

	public String getSortBy() {
		return settings.getString(SORT_ORDER, "");
	}

	public String getSearchBy() {
		return settings.getString(SEARCH_BY, "");
	}
	
	public Boolean getAscDesc() {
		return settings.getBoolean(SORT_ASC_DESC, false);
	}
	
	public EnumDataPlan getDataPlanOption() {
		return EnumDataPlan.getEnumFromString(staticSettings.getString(DATA_PLAN_KEY, 
				staticContext.getString(com.numicago.guiahollywood.R.string.dataPlanNoImages)));
	}
	
	public enum EnumDataPlan {
		DOWN_ALL(0, staticContext.getString(com.numicago.guiahollywood.R.string.dataPlanRegular)),
		DOWN_TEXT(1, staticContext.getString(com.numicago.guiahollywood.R.string.dataPlanNoImages)),
		DOWN_NOTHING(2, staticContext.getString(com.numicago.guiahollywood.R.string.dataPlanNoInternet));

		int id;
		String description;
		
		private EnumDataPlan(int id, String description) {
			this.id = id;
			this.description = description;
		}
		
		public static EnumDataPlan getEnumFromString(String description) {
			for (EnumDataPlan item : EnumDataPlan.values()) {
				if(item.getDescription().equals(description)) {
					return item;
				}
			}
			return EnumDataPlan.DOWN_TEXT;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}
}
