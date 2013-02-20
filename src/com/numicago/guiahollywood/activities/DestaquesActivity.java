/*
 * Autor: Nuno Gonçalves
 * Criado: 06-12-2011 
 */
package com.numicago.guiahollywood.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.numicago.guiahollywood.DestaquesPortugal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.adapters.DestaqueItemAdapter;
import com.numicago.guiahollywood.dateslider.CalendarUtils;
import com.numicago.guiahollywood.objects.Destaque;

public class DestaquesActivity extends ListActivity
{
//	private String TAG = "DestaquesActivity";
	
	private ProgressDialog pd;
	private static final String DESTAQUES_HOLLYWOOD = "http://canalhollywood.pt/destaques/";
	private ListView lv;
	private List<Destaque> listaDestaques;
	private Activity thisActivity;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.destaques);
		
		ImageView mainLogoImage = (ImageView)findViewById(R.id.destaques_main_imageview);
		if(CalendarUtils.XmasSeason())
			mainLogoImage.setImageResource(R.drawable.logoprincipalxmas);
		else if(CalendarUtils.stValentineSeason())
			mainLogoImage.setImageResource(R.drawable.logoprincipal_st_valentine);			
		else
			mainLogoImage.setImageResource(R.drawable.logoprincipal);
			
		
		thisActivity = DestaquesActivity.this;
		pd = ProgressDialog.show(DestaquesActivity.this, "Aguarde", "A carregar lista de destaques...", true, true);
		lv = getListView();
		

		new parseSite().execute(DESTAQUES_HOLLYWOOD);
	}
	
	private class parseSite extends AsyncTask<String, Void, List<String>> 
	{		
		protected List<String> doInBackground(String... arg) 
		{
			DestaquesPortugal destaques = new DestaquesPortugal();
			listaDestaques = destaques.getDestaques(DESTAQUES_HOLLYWOOD);
			return new ArrayList<String>();
		}
		
		protected void onPostExecute(List<String> output) 
		{		
			pd.dismiss();
				lv.setAdapter(new DestaqueItemAdapter(thisActivity, listaDestaques));
		}
	}
}
