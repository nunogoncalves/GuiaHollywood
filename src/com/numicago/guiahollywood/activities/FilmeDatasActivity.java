package com.numicago.guiahollywood.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.objects.FilmeData;

public class FilmeDatasActivity extends Activity {

	LinearLayout mainLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.filme_datas);
		
		mainLayout = (LinearLayout) findViewById(R.id.filme_datas_main);
		
		int movieId = getIntent().getExtras().getInt("MovieId");
		
		List<FilmeData> listDatas = null;//DBUtils.getFilmeDatas(this, movieId);
		
		TextView filmeNome = (TextView) findViewById(R.id.filme_datas_filme_nome_tv);
		filmeNome.setText(listDatas.get(0).getNomePt());
		
		for (FilmeData filmeData : listDatas) {
			TextView tv = new TextView(this);
			tv.setText(filmeData.getInicio().toString("yyyy-MM-dd, EEE, 'das' HH:mm") + " às " 
			+ filmeData.getFim().toString("HH:mm"));
			mainLayout.addView(tv);
		}
	}
	
}
