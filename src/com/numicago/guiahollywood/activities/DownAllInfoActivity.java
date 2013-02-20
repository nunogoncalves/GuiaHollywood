package com.numicago.guiahollywood.activities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.htmlHelperFilmInfo;
import com.numicago.guiahollywood.objects.EnumCountryCanal;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.objects.UserPreferences;
import com.numicago.guiahollywood.provider.DBUtils;

public class DownAllInfoActivity extends Activity {

	private LinearLayout movieNamesLayout;

	private ScrollView scroller;

	private TextView nMoviesTV;

	private Button startButton;

	private Button cancelButton;

	private Handler progressHandler;

	private Movie currentMovie;

	private int currMovId;

	private int totalNMovies;

	private boolean canceled = false;

	List<Movie> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.down_all_layout);

		list = AllMoviesActivity.listMoviesToPass;
		totalNMovies = list.size();

		movieNamesLayout = (LinearLayout) findViewById(R.id.down_all_movie_id_layout);
		nMoviesTV = (TextView)findViewById(R.id.down_all_n_movies);
		nMoviesTV.setText("Número de filmes: " + totalNMovies);
		scroller = (ScrollView) findViewById(R.id.down_all_scroller);
		startButton = (Button) findViewById(R.id.down_all_ok);
		cancelButton = (Button) findViewById(R.id.down_all_cancel);

		cancelButton.setOnClickListener(new OnClickListener() {

			public void onClick(View paramView) {
				canceled = true;
			}
		});

		startButton.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				canceled = false;
				paramView.setEnabled(false);
				new DownAllInfo().execute("");
				progressHandler = new Handler() {
					public void handleMessage(Message msg) {
						if (currentMovie != null) {
							nMoviesTV.setText("A descarregar filme " + currMovId + " de " + totalNMovies);
							TextView tv = new TextView(DownAllInfoActivity.this);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
							tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
							tv.setLayoutParams(params);
							tv.setText("Filme: " + currentMovie.getLocalName() 
									+ " descarregado");
							tv.setTextColor(Color.WHITE);
							movieNamesLayout.addView(tv);
							scroller.fullScroll(ScrollView.FOCUS_DOWN);
						}
					}
				};
			}
		});

	}

	//Parse do site
	class DownAllInfo extends AsyncTask<String, Void, Integer> {
		protected Integer doInBackground(String... arg) { 
			for (int i = 0; i < list.size(); i++) {
				Movie movie = list.get(i);
				currMovId =i;
				//				for (Movie movie : list) {
				if (canceled) {
					break;
				}
				if (movie.getDescription() != null) {
					continue;
				}
				htmlHelperFilmInfo hh = null;
				try {
					UserPreferences userPreferences = GuiaHollywoodActivity.userPreferences;
					String site = EnumCountryCanal.getEnumFromId((userPreferences.getDisplayChannel())).getSiteBase() + movie.getHollywoodUrl();
					hh = new htmlHelperFilmInfo(new URL(site));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currentMovie = new Movie();
				currentMovie.setLocalName(movie.getLocalName());
				currentMovie.setOriginalName(movie.getOriginalName());
				currentMovie.setImageBigUrl(hh.getImageURL("foto-ficha-prog_"));
				currentMovie.setDescription(hh.getDescription("texto_principal_"));

				Integer year;
				try {
					year = Integer.parseInt(hh.getYear("ficha_prg_der"));
				} catch (NumberFormatException ex) {
					year = 0;
				}
				if (year != null) {
					currentMovie.setYear(year);				
				}
				Movie mvie = hh.getInfoSecundaria("texto_datos_");
				currentMovie.setDirector(mvie.getDirector());
				DBUtils.updateMovie(DownAllInfoActivity.this, currentMovie);
				progressHandler.sendMessage(progressHandler.obtainMessage());
			}
			return 0;
		}
		protected void onPostExecute(Integer output) {		
			startButton.setEnabled(true);
			cancelButton.setEnabled(false);
		}
	}
}
