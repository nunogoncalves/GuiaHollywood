package com.numicago.guiahollywood.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.ProgramacaoCanal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.objects.DayMovie;
import com.numicago.guiahollywood.objects.EnumCountryCanal;
import com.numicago.guiahollywood.provider.DBUtils;

public class MonthMoviesActivity extends Activity {

	private TextView progressTextView;

	private ProgressBar pb;

	private Button startButton;

	private Button cancelButton;

	private String site;

	private Chronometer chronometer;

	private LinearLayout layout;

	private Handler progressHandler;

	List<DayMovie> programacaoDoDia;

	private boolean canceled;

	private boolean started = false;

//	private Spinner monthSpinner;
	
	private ImageButton prevYear;
	private ImageButton nextYear;
	private ImageButton prevMonth;
	private ImageButton nextMonth;

	private NumiCal cal;

	private int newTotal = 0;

	private int oldTotal = 0;

	private TextView yearTV;
	
	private TextView monthTV;
	
	private boolean mesVazio;

	private int monthId;
	
	private int year;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fetch_month_movies);
		site = EnumCountryCanal.getEnumFromId(
				GuiaHollywoodActivity.userPreferences.getDisplayChannel()).getSiteGuiaTv();
		progressTextView = (TextView) findViewById(R.id.fetch_month_movies_progress_text_view);
		pb = (ProgressBar) findViewById(R.id.fetch_month_movies_pb);
		startButton = (Button) findViewById(R.id.fetch_month_movies_start_button);
		cancelButton = (Button) findViewById(R.id.fetch_month_movies_cancelar_button);
		chronometer = (Chronometer) findViewById(R.id.fetch_month_movies_cronometer);
		layout = (LinearLayout) findViewById(R.id.fetch_movies_scrollView);
		
		prevYear = (ImageButton) findViewById(R.id.fetch_movies_left_year);
		nextYear = (ImageButton) findViewById(R.id.fetch_movies_right_year);
		prevMonth = (ImageButton) findViewById(R.id.fetch_movies_left_month);
		nextMonth = (ImageButton) findViewById(R.id.fetch_movies_right_month);
		
		canceled = false;

		// Array of choices
		final String months[] = getResources().getStringArray(R.array.monthNames);

		// Application of the Array to the Spinner
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, months);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww

		cal = new NumiCal();
		monthId = cal.getMonth();
		year = cal.getYear();
		
		final SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM");
		final SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");

		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				canceled = true;
				started = false;
				enableButtons();
			}
		});
		
		yearTV = (TextView) findViewById(R.id.fetch_month_movies_year_et);
		monthTV = (TextView) findViewById(R.id.fetch_movies_month_tv);
		
		yearTV.setText(cal.getYear() + "");
		monthTV.setText(months[monthId]);
		
		
		nextYear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				year++;
				yearTV.setText(year + "");
				cal.setYear(year);
			}
		});
		
		prevYear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				year--;
				yearTV.setText(year + "");
				cal.setYear(year);
			}
		});
					
		nextMonth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				monthId ++;
				if (monthId > 11) {
					monthId = 0;
					year++;
				}
				yearTV.setText(year + "");
				monthTV.setText(months[monthId]);
				cal.setYear(year);
				cal.setMonth(monthId);
			}
		});
		
		prevMonth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				monthId --;
				if (monthId < 0) {
					monthId = 11;
					year--;
				}
				yearTV.setText(year + "");
				monthTV.setText(months[monthId]);
				cal.setYear(year);
				cal.setMonth(monthId);
			}
		});
		
		startButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (GuiaHollywoodActivity.userPreferences.isConnected()) {
					cal.setMonth(monthId);
					cal.setDayOfMonth(1);
					if (canceled == true) canceled = false;
					started = true;
					enableButtons();
					chronometer.start();
					progressTextView.setText("A descarregar filmes do dia 01");
					new parseSite().execute("");

					progressHandler = new Handler() 
					{
						private int seconds;

						public void handleMessage(Message msg) 
						{
							TextView tv = new TextView(MonthMoviesActivity.this);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
							tv.setGravity(Gravity.CENTER | Gravity.BOTTOM);
							tv.setLayoutParams(params);
							SimpleDateFormat cronoFormat = new SimpleDateFormat("mm:ss");
							try {
								NumiCal cronoCal = new NumiCal(cronoFormat.parse(chronometer.getText().toString()));
								int min = cronoCal.getMinute();
								int sec = cronoCal.getSecond();
								newTotal = min * NumiCal.SECS_IN_MIN + sec;
							} catch (ParseException e) {
								e.printStackTrace();
							}
							seconds = newTotal - oldTotal;
							oldTotal = newTotal;
							tv.setText("Day " + sdf.format(cal.getDate()) + ": " + chronometer.getText() + 
									" (" + seconds + " seconds)");
							tv.setBackgroundResource(android.R.color.white);
							tv.setTextColor(Color.BLACK);
							layout.addView(tv);

							if (programacaoDoDia == null || programacaoDoDia.isEmpty()) { 
								if ( mesVazio) {
									Toast.makeText(MonthMoviesActivity.this, "O mês não tem programação", Toast.LENGTH_LONG).show();
								} else {
									NumiCal temp = new NumiCal(cal);
									temp.addDays(1);
									progressTextView.setText("A descarregar filmes do dia " + sdf.format(temp.getDate()));
								}
							} else {
								NumiCal temp = new NumiCal(cal);
								temp.addDays(1);
								progressTextView.setText("A descarregar filmes do dia " + sdf.format(temp.getDate()));

								for (DayMovie filme : programacaoDoDia) {
									TextView filmeTV = new TextView(MonthMoviesActivity.this);
									filmeTV.setText(sdf1.format(filme.getDay().getStart().getTime()) 
											+ " - " + filme.getMovie().getLocalName());
									layout.addView(filmeTV);
									((ScrollView) layout.getParent()).fullScroll(ScrollView.FOCUS_DOWN);
								}
							}
						}
					};
				} else {
					Toast.makeText(getBaseContext(), "Não está ligado à internet para poder descarregar os filmes", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private void enableButtons() {
		nextYear.setEnabled(!started);
		nextMonth.setEnabled(!started);
		prevYear.setEnabled(!started);
		prevMonth.setEnabled(!started);
		startButton.setEnabled(!started);
		cancelButton.setEnabled(started);
	}
	
	//Parse do site
	private class parseSite extends AsyncTask<String, Void, Integer> 
	{		
		protected Integer doInBackground(String... arg) 
		{
			cal.setDayOfMonth(1);
			int month = cal.getMonth();
			pb.setMax(cal.getMonthDays());
			while (cal.getMonth() == month && !canceled) {
				if (!DBUtils.moviesOfDayExist(MonthMoviesActivity.this, cal, GuiaHollywoodActivity.userPreferences.getDisplayChannel())) {
					StringBuilder sb = new StringBuilder(site);
					String dateformat = EnumCountryCanal.getEnumFromId(GuiaHollywoodActivity.userPreferences.getDisplayChannel()).getDateFormat();
					int countryId = EnumCountryCanal.getEnumFromId(GuiaHollywoodActivity.userPreferences.getDisplayChannel()).getId();
					SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
					String dateString = sdf.format(cal.getDate());
					StringBuilder fullUrl = sb.append(dateString);
					ProgramacaoCanal programacaoCanal = new ProgramacaoCanal(fullUrl.toString(), countryId); //objecto que vai buscar informação ao site ao ser inicializado
					programacaoDoDia = programacaoCanal.getProgramacaoDoDia();
					if (programacaoDoDia == null || programacaoDoDia.isEmpty()) {
						canceled = true;
						mesVazio = true;
					} else {
						DBUtils.addMoviesOfDay(MonthMoviesActivity.this, programacaoDoDia, cal);
					}
				} else {
					if (programacaoDoDia != null) {
						programacaoDoDia.clear();
					} else {
						programacaoDoDia = new ArrayList<DayMovie>();
					}
				}
				progressHandler.sendMessage(progressHandler.obtainMessage());
				pb.setProgress(cal.getDayOfMonth());
				cal.addDays(1);
			}
			return 0;
		}

		protected void onPostExecute(Integer output) 
		{		
			chronometer.stop();
			startButton.setEnabled(true);
			if (canceled) {
				Toast.makeText(MonthMoviesActivity.this, "Canceled", Toast.LENGTH_LONG).show();				
			} else {
				Toast.makeText(MonthMoviesActivity.this, "Done", Toast.LENGTH_LONG).show();		
				progressTextView.setText("Done");
				canceled = true;
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (!canceled && started) {
			Toast.makeText(this, "Cancele primeiro para sair", Toast.LENGTH_LONG).show();
		} else {
			super.onBackPressed();
		}
	}
}
