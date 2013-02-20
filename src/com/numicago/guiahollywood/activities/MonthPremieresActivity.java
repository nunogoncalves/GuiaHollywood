package com.numicago.guiahollywood.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.adapters.ExpadableAdapter;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.provider.DBUtils;

public class MonthPremieresActivity extends Activity {
	
	private ExpandableListView expLv;
	private ExpadableAdapter mAdapter;	
	private List<Movie> listMovies;
	
	private NumiCal cal;
	
	private TextView yearTV;
	
	private TextView monthTV;
	
	private TextView nFilmesTV;
	
	private int monthId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.premieres);
		
		// Array of choices
		final String months[] = getResources().getStringArray(R.array.monthNames);;
//		final String months[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", 
//				"Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

		cal = new NumiCal();
		monthId = cal.getMonth();
		
		listMovies = DBUtils.getMonthPremieres(this, cal,  
				GuiaHollywoodActivity.userPreferences.getDisplayChannel(),
				GuiaHollywoodActivity.userPreferences.getAscDesc());
		
		nFilmesTV = (TextView) findViewById(R.id.premieres_n_filmes);
		nFilmesTV.setText("Número de filmes: " + listMovies.size());
		
		expLv = (ExpandableListView) findViewById(R.id.premiereExpandable);	
		mAdapter = new ExpadableAdapter(this, listMovies);
		expLv.setAdapter(mAdapter);
		expLv.setCacheColorHint(0); //fundo transparante do listview
		expLv.setGroupIndicator(null);
		
		yearTV = (TextView) findViewById(R.id.premieres_year_tv);
		monthTV = (TextView) findViewById(R.id.premieres_month_tv);
		yearTV.setText(cal.getYear() + "");
		monthTV.setText(months[monthId]);
		
		
		ImageButton addYear = (ImageButton) findViewById(R.id.premieres_right_year);
		addYear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cal.addYears(1);
				yearTV.setText("" + cal.getYear());
				updateList();
			}
		});
		
		ImageButton subYear = (ImageButton) findViewById(R.id.premieres_left_year);
		subYear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cal.addYears(-1);
				yearTV.setText("" + cal.getYear());
				updateList();
			}
		});
		
		ImageButton addMonth = (ImageButton) findViewById(R.id.premieres_right_month);
		addMonth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				monthId++;
				if (monthId > 11) {
					monthId = 0;
					cal.addYears(1);
				}
				cal.setMonth(monthId);
				yearTV.setText("" + cal.getYear());
				monthTV.setText(months[monthId]);
				updateList();
			}
		});
		
		ImageButton subMonth = (ImageButton) findViewById(R.id.premieres_left_month);
		subMonth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				monthId--;
				if (monthId < 0) {
					monthId = 11;
					cal.addYears(-1);
				}
				cal.setMonth(monthId);
				yearTV.setText("" + cal.getYear());
				monthTV.setText(months[monthId]);
				updateList();
			}
		});
	}
	
	public void updateList() {
		listMovies = DBUtils.getMonthPremieres(MonthPremieresActivity.this, cal,  
				GuiaHollywoodActivity.userPreferences.getDisplayChannel(),
				GuiaHollywoodActivity.userPreferences.getAscDesc());
			nFilmesTV.setText("Número de filmes: " + listMovies.size());
			mAdapter = new ExpadableAdapter(MonthPremieresActivity.this, listMovies);
			expLv.setAdapter(mAdapter);
	}

}
