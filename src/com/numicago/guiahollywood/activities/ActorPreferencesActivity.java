package com.numicago.guiahollywood.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.numicago.guiahollywood.R;

public class ActorPreferencesActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.actors_preferences_layout);

	}
}
