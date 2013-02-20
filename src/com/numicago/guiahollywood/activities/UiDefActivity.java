package com.numicago.guiahollywood.activities;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.numicago.guiahollywood.MyLog;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.objects.EnumBackground;
import com.numicago.guiahollywood.objects.EnumCountryCanal;
import com.numicago.guiahollywood.objects.EnumLanguage;
import com.numicago.guiahollywood.objects.UserPreferences;

public class UiDefActivity extends Activity
{
	private int estiloListView = -1; // Estilo
	private int linguaSeleccionada = -1; //Lingua
	private int canalSeleccionado = -1; //Canal
	
	private boolean seasonPrevailsValue;
	
	private int timeBeforeInMinutes;
	
	private UserPreferences userPreferences; 
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_def);
	
		userPreferences = GuiaHollywoodActivity.userPreferences;
		seasonPrevailsValue = userPreferences.seasonPrevails();
		estiloListView = userPreferences.getTheme();
		linguaSeleccionada = userPreferences.getEnumLanguage().getId();
		canalSeleccionado = userPreferences.getDisplayChannel();
		timeBeforeInMinutes = userPreferences.getAlarmTimeBefore();
		
		final RadioGroup rgFundo = (RadioGroup)findViewById(R.id.ui_fundo_radio_group);
		final RadioGroup rgLingua = (RadioGroup)findViewById(R.id.ui_pais_radiogroup);
		final RadioGroup rgCanalPais = (RadioGroup)findViewById(R.id.ui_canal_pais_radiogroup);
		final CheckBox checkBoxEpoca = (CheckBox)findViewById(R.id.ui_epoca_maior_escolha_check_button);
		checkBoxEpoca.setChecked(seasonPrevailsValue);
		
		final int [] array = {1, 10, 15, 20, 30, 60};
		final Spinner timeBeforeSpinner = (Spinner) findViewById(R.id.ui_time_spinner);
		ArrayAdapter<CharSequence> adapterArrray = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_dropdown_item);
		timeBeforeSpinner.setAdapter(adapterArrray);
		MyLog.p("int = " + timeBeforeInMinutes);
		for (int i = 0; i < array.length; i++) {
			if (array[i] == timeBeforeInMinutes) {
				MyLog.p("i selected = " + i);
				timeBeforeSpinner.setSelection(i, false);
			}
		}
		timeBeforeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				timeBeforeInMinutes = array[arg2];
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		if(seasonPrevailsValue)
			((RadioButton)findViewById(EnumBackground.getSeasonButton())).setChecked(true);
		else
			((RadioButton)findViewById(EnumBackground.getRadioButtonFromId(estiloListView))).setChecked(true);
		
		rgFundo.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				if(checkBoxEpoca.isChecked())
				{
					estiloListView = EnumBackground.getIdFromSeason();
					if(estiloListView == -1)
						estiloListView = EnumBackground.getIdFromButton(checkedId);
				}
				else
					estiloListView = EnumBackground.getIdFromButton(checkedId);
			}
		});
		
		((RadioButton)findViewById(EnumLanguage.getEnumFromId(linguaSeleccionada).getButtonId())).setChecked(true);
		
		rgLingua.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				linguaSeleccionada = EnumLanguage.getEnumFromButtonId(checkedId).getId();
				Log.d("UIDEF", EnumLanguage.getEnumFromButtonId(checkedId).getCountry());
			}
		});
		
		checkBoxEpoca.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				seasonPrevailsValue = checkBoxEpoca.isChecked();
				if(checkBoxEpoca.isChecked())
				{
					estiloListView = EnumBackground.getIdFromSeason();
					if(estiloListView == -1)
						estiloListView = EnumBackground.DEFAULT.getId();
				}
				else
					estiloListView = EnumBackground.DEFAULT.getId();
			}
		});
		
		((RadioButton)findViewById(EnumCountryCanal.getEnumFromId(canalSeleccionado).getButtonId())).setChecked(true);

		rgCanalPais.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				canalSeleccionado = EnumCountryCanal.getEnumFromButtonId(checkedId).getId();
				Log.d("UIDEF", "canal = " + EnumCountryCanal.getEnumFromButtonId(checkedId).getSiteBase());
			}
		});
		
		Spinner spinner = (Spinner) findViewById(R.id.ui_time_spinner);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.time_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);

		
	}
	
	public void clickHandler(View targetButton)
	{
		switch (targetButton.getId())
		{
		case R.id.setStyleButton:
			if(estiloListView != -1)
			{
//				userPreferences.setSeasonPrevails(seasonPrevailsValue);
//				userPreferences.setTheme(estiloListView);
//				userPreferences.setLanguage(EnumLanguage.getEnumFromId(linguaSeleccionada));
//				userPreferences.setDisplayChannel(canalSeleccionado);
//				userPreferences.setAlarmTimeBefore(timeBeforeInMinutes);
//				userPreferences.saveAllFields();
//			    
				setResult(GuiaHollywoodActivity.REQUEST_THEME_LAYOUT);				
				finish();
			}
			else
			{
				Toast.makeText(getBaseContext(), "Tem de seleccionar um estilo", Toast.LENGTH_LONG).show();
			}				
			break;
		case R.id.cancel_style_button:
			finish();
			break;
		}
	}
}
