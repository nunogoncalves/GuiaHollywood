package com.numicago.guiahollywood.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.numicago.guiahollywood.DayMovieHelper;
import com.numicago.guiahollywood.DrawableManager;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.Utils;
import com.numicago.guiahollywood.objects.DayMovie;
import com.numicago.guiahollywood.objects.UserPreferences;
import com.numicago.guiahollywood.objects.UserPreferences.EnumDataPlan;

public class FilmeDaDBActivity extends Activity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filme_da_db_info);
		
		DayMovie movieAlarm = DayMovieHelper.dayMovieAsBundle;
		
		TextView nomeOriginal = (TextView)findViewById(R.id.filme_da_db_info_titulo_original_textView);
		nomeOriginal.setText(movieAlarm.getOriginalName());
		
		TextView nomePT = (TextView)findViewById(R.id.filme_da_db_info_titulo_en_textView);
		nomePT.setText(movieAlarm.getLocalName());
		
		DayMovieHelper.dayMovieAsBundle = null;
		
		ImageView image = (ImageView)findViewById(R.id.imagem_filme_da_db);
		image.setScaleType(ScaleType.FIT_XY);
		
		boolean net = GuiaHollywoodActivity.userPreferences.isConnected() 
				&& (GuiaHollywoodActivity.userPreferences.getConnectionType() == UserPreferences.WIFI_CONNECTION 
				|| (GuiaHollywoodActivity.userPreferences.getConnectionType() == UserPreferences.MOBILE_CONNECTION 
					&& GuiaHollywoodActivity.userPreferences.getDataPlanOption() == EnumDataPlan.DOWN_ALL));
		
		if (movieAlarm.getMovie().getImageBigBlob() == null) {
			DrawableManager dm = new DrawableManager();
			dm.fetchDrawableOnThread(movieAlarm.getMovie().getImageBigUrl(), image, net);
		}
		
		TextView descricao = (TextView)findViewById(R.id.filme_da_db_info_descricao_textView);
		descricao.setText(movieAlarm.getMovie().getDescription());
		
		TextView data = (TextView)findViewById(R.id.agenda_filme_da_db_filme_textView);
		data.setText(Utils.UppercaseFirstLetters(
				movieAlarm.getDay().getStart().toString("EEEE, dd 'de' MMMM 'de' yyyy")));
		
		TextView hora = (TextView)findViewById(R.id.filme_da_db_horario_textView);
		hora.setText((String) "Das " + movieAlarm.getDay().getStart().toString("kk:mm ") + "às "
		+ movieAlarm.getDay().getEnd().toString("kk:mm"));  
	}
}
