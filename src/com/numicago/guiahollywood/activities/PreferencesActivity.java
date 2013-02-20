package com.numicago.guiahollywood.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.objects.UserPreferences;

public class PreferencesActivity extends PreferenceActivity {

	SharedPreferences.OnSharedPreferenceChangeListener spChanged;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_layout);

		spChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences arg0, String prefsChanged) {
				if (prefsChanged.equals(UserPreferences.LANGUAGE_PREF_KEY)) {
					GuiaHollywoodActivity.userPreferences.setLocale();
				}
			}
		};

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefs.registerOnSharedPreferenceChangeListener(spChanged);
	}
}
