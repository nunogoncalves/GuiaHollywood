package com.numicago.guiahollywood.activities;

import com.numicago.guiahollywood.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class EstatisticaPreferencesActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.sort_preferences_layout);
	}
}
