package com.numicago.guiahollywood.activities;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.numicago.guiahollywood.DayMovieHelper;
import com.numicago.guiahollywood.DrawableManager;
import com.numicago.guiahollywood.MyLog;
import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.ProgramacaoCanal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.htmlHelperFilmInfo;
import com.numicago.guiahollywood.alarm.MovieAlarm;
import com.numicago.guiahollywood.objects.Actor;
import com.numicago.guiahollywood.objects.Day;
import com.numicago.guiahollywood.objects.DayMovie;
import com.numicago.guiahollywood.objects.EnumCountryCanal;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.objects.UserPreferences;
import com.numicago.guiahollywood.objects.UserPreferences.EnumDataPlan;
import com.numicago.guiahollywood.provider.DBUtils;
import com.numicago.guiahollywood.widgets.ActionBar;
import com.numicago.guiahollywood.widgets.ActionBar.Action;
import com.numicago.guiahollywood.widgets.ActionBar.IntentAction;
import com.numicago.guiahollywood.widgets.ActionItem;
import com.numicago.guiahollywood.widgets.QuickAction;


public class FilmeInfoActivity extends Activity implements OnClickListener
{
	private RelativeLayout relLayoutProgress;
	private ProgressBar pb;
	private TextView filmeProgressTV;
	private ProgressBar pbMovieNow;
	private TextView nomeOriginalTV;
	private TextView descricaoTv;
	private TextView anoTv;
	private TextView duracaoTv;
	private TextView realizadorTv;
	private TextView horarioTv;
	private ImageView filmeImagem;
//	private ImageButton addAlarmButton;
	private List<String> outrasDatas;
//	private String genero;
	
	private ToggleButton favButton;
	
	private ToggleButton seenButton;
	
	private ToggleButton watchListButton;
	
	private RatingBar rating;
	
	private Activity thisActivity;
	
	private String elapsedPercentage;

	private boolean backPressed = false;
	private Thread background;
//	private String TAG = "Filme Info Activity";
	private UserPreferences userPreferences;
	
	DayMovie dayMovie;
	
	boolean loadPics;
	
	private QuickAction quickAction;
	
	List<Day> futureDates;
	
	LinearLayout actorsImagesScroller;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filme_info);
		thisActivity = FilmeInfoActivity.this;
		
		outrasDatas = new ArrayList<String>();
		futureDates = new ArrayList<Day>();
		
		userPreferences = GuiaHollywoodActivity.userPreferences;
		if (userPreferences == null) {
			userPreferences = new UserPreferences(thisActivity);
		}
		loadPics = userPreferences.getConnectionType() == UserPreferences.WIFI_CONNECTION 
				|| userPreferences.getDataPlanOption() == EnumDataPlan.DOWN_ALL;
		
		dayMovie = DayMovieHelper.dayMovieAsBundle;
		
		LinearLayout outrasDatasLayout = (LinearLayout) findViewById(R.id.filme_info_other_dates_layout);
		List<Day> movieDays = DBUtils.getMovieDays(thisActivity, dayMovie.getDay().getMovieId());
		for (Day day : movieDays) {
			if (new NumiCal().getTime().before(day.getStart().getDate())) {
				futureDates.add(day);
			}
			if (!day.getStart().isSameDateToMilis(dayMovie.getDay().getStart())) {
				TextView tv = new TextView(thisActivity);
				String data = day.getStart().toString(getString(R.string.dateFormat) + " 'às' HH:mm");
				tv.setText(data);
				if (new NumiCal().isPastDay(day.getStart())) {
					outrasDatas.add(tv.getText().toString());
				}
				outrasDatasLayout.addView(tv);
			}
		}
		
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new FinishAction());
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Informação do filme");
		
		actionBar.addAction(new AddActorAction());
		
		//action with no intent
		actionBar.addAction(new CreatePopUpForAlarm());
		
		//action with intent
		final Action shareAction = new IntentAction(this, createShareIntent(), R.drawable.ic_title_share_default);
		actionBar.addAction(shareAction);
		
        
		TextView agendaFilmeTv = (TextView)findViewById(R.id.agenda_filme_textView);
		agendaFilmeTv.setText(dayMovie.getDay().getStart().toString(getString(R.string.dateFormat)));
		nomeOriginalTV = (TextView)findViewById(R.id.filme_info_titulo_original_textView);
		nomeOriginalTV.setText(dayMovie.getMovie().getOriginalName());
		TextView nomePtTV = (TextView)findViewById(R.id.filme_info_titulo_en_textView);
		nomePtTV.setText(dayMovie.getLocalName());
		
		filmeImagem = (ImageView)findViewById(R.id.imagem_filme);
		filmeImagem.setScaleType(ScaleType.FIT_XY);
		
		ImageButton imdbButton = (ImageButton) findViewById(R.id.imdb_button);
		imdbButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (isUriAvailable(thisActivity, "imdb:///find")) {
					Uri uri = Uri.parse("imdb:///find?q=" + dayMovie.getLocalName());
					Intent test = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(test);
				} else {
					Uri uri = Uri.parse("http://m.imdb.com/find?q=" + dayMovie.getLocalName());
					Intent test = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(test);
				}
			}
		});
		
		ImageButton youtubeButton = (ImageButton) findViewById(R.id.youtube_button);
		youtubeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEARCH);
				intent.setPackage("com.google.android.youtube");
				String movieName = dayMovie.getOriginalName() != null ? dayMovie.getOriginalName() : 
					dayMovie.getLocalName();
				intent.putExtra("query", movieName + " trailer");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				if (thisActivity.getPackageManager().resolveActivity(intent, 0) != null) {
					startActivity(intent);
					System.out.println("sarch");
				} else {
					Intent intentSearch = new Intent(Intent.ACTION_VIEW,
							Uri.parse("http://www.youtube.com/results?search_query=" + 
							movieName.replace(" ", "%20") + "%20trailer"));
					startActivity(intentSearch);
					System.out.println("web");
				}
			}
		});
		
		if (dayMovie.getMovie().getImageBigBlob() != null) {
			DrawableManager dm = new DrawableManager();
			dm.fetchDrawableOnThread(dayMovie.getMovie().getImageBigUrl(), filmeImagem, false);
		}
		pb = (ProgressBar)findViewById(R.id.progress_bar_big_image);
		pb.setVisibility(View.GONE);
		
		seenButton = (ToggleButton) findViewById(R.id.filme_info_seen_image_button);
		seenButton.setOnClickListener(this);
		seenButton.setChecked(dayMovie.getMovie().isViewed());
		
		favButton  = (ToggleButton) findViewById(R.id.filme_info_favourite_image_button);
		favButton.setChecked(dayMovie.getMovie().isFavourite());
		favButton.setOnClickListener(this);
		watchListButton  = (ToggleButton) findViewById(R.id.filme_info_watchlist_image_button);
		watchListButton.setChecked(dayMovie.getMovie().isWatchList());
		watchListButton.setOnClickListener(this);
		
		rating = (RatingBar) findViewById(R.id.filme_info_rating);
		Float movRating = dayMovie.getMovie().getRating();
		if (movRating !=null) {
			rating.setRating(movRating);
		}
		rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				DBUtils.updateMovieRating(thisActivity, dayMovie.getMovie().getLocalName(), rating);
			}
		});
		
		ImageView alarmImage = (ImageView)findViewById(R.id.alarmOverImage);
		alarmImage.setImageResource(dayMovie.getDay().hasAlarm() ? R.drawable.alarm_clock_big : 0);
		
		anoTv = (TextView)findViewById(R.id.filme_ano_textView);
		realizadorTv = (TextView)findViewById(R.id.filme_realizador_textView);
		duracaoTv = (TextView)findViewById(R.id.filme_duracao_textView);
		descricaoTv = (TextView)findViewById(R.id.filme_info_descricao_textView);
		horarioTv = (TextView)findViewById(R.id.filme_horario_textView);
		horarioTv.setText(dayMovie.getTimeTable(getString(R.string.das), getString(R.string.as)));
		filmeImagem = (ImageView)findViewById(R.id.imagem_filme);
		filmeImagem.setScaleType(ScaleType.FIT_XY);
		
		if(userPreferences.isConnected()) {
			if (dayMovie.getMovie().getImageBigBlob() != null) {
				DrawableManager dm = new DrawableManager();
				dm.fetchDrawableOnThread(dayMovie.getMovie().getImageBigUrl(), filmeImagem, loadPics);
			}
			filmeImagem.setOnLongClickListener(new OnLongClickListener() {
				
				public boolean onLongClick(View paramView) {
					Toast.makeText(thisActivity, "LONG CLICK", Toast.LENGTH_LONG).show();
					return false;
				}
			});
			pb.setVisibility(View.VISIBLE);
			relLayoutProgress = (RelativeLayout)findViewById(R.id.filme_info_progress_relative_layout);
			if(dayMovie.isOnAir()) {
				filmeProgressTV = (TextView)findViewById(R.id.filme_info_percentage_movie);
				relLayoutProgress.setVisibility(View.VISIBLE);
				pbMovieNow = (ProgressBar)findViewById(R.id.filme_info_movie_progree_bar);
				pbMovieNow.setMax((int) dayMovie.getDurationInMillis());
				pbMovieNow.setProgress((int) dayMovie.getMoviePassed());
				filmeProgressTV.setText("" + dayMovie.getElapsedPercentage());
				
				final Handler progressHandler = new Handler() 
	        	{
					public void handleMessage(Message msg) 
	        		{
	        			filmeProgressTV.setText("" + elapsedPercentage);
	        		}
	        	};
	        	
	        	background = new Thread (new Runnable() {
	            	public void run() {
	            		try {
	            			while (pbMovieNow.getProgress()<= pbMovieNow.getMax() && !backPressed) {
	            				Thread.sleep(5000);
	            				pbMovieNow.setMax((int) dayMovie.getDurationInMillis());
	            				pbMovieNow.setProgress((int) dayMovie.getMoviePassed());
	            				elapsedPercentage = dayMovie.getElapsedPercentage();
	                            progressHandler.sendMessage(progressHandler.obtainMessage());
	            			}
	            		} catch (java.lang.InterruptedException e) {
	            		}
	            	}
	            });
	        	background.start();
	        	// handler for the background updating
			}
			
			int canalExibicaoID = userPreferences.getDisplayChannel();
			//Ir à net buscar a restante informação do filme (Descrição e imagem em tamanho grande)
			String site = (EnumCountryCanal.getEnumFromId(canalExibicaoID).getSiteBase() + dayMovie.getMovie().getHollywoodUrl());
			
			if (dayMovie.getMovie().getDescription() != null) {
				ImageView alarmClock = (ImageView)findViewById(R.id.alarmOverImage);
				alarmClock.setVisibility(View.VISIBLE);
				DrawableManager dm = new DrawableManager();
				dm.fetchDrawableOnThread(dayMovie.getMovie().getImageBigUrl(), filmeImagem, loadPics);
				descricaoTv.setText(dayMovie.getMovie().getDescription());	
				nomeOriginalTV.setText(dayMovie.getMovie().getOriginalName());
				realizadorTv.setText(getString(R.string.realizador) + " " + dayMovie.getMovie().getDirector());
				anoTv.setText("Ano: " + dayMovie.getMovie().getYear());
				duracaoTv.setText(getString(R.string.duracao) + dayMovie.getDay().getDurationInMinutes() + " min");
				DBUtils.updateMovie(this, dayMovie.getMovie());
				pb.setVisibility(View.GONE);
			} else {
				new SiteParser().execute(site);
				setProgressBarVisibility(true);
				pb.setVisibility(View.VISIBLE);
			}
			if (dayMovie.getMovie().getImageSmallUrl().equals("http://multicanaltv.com/images/hollywoodpt/sinimagen_th.jpg")) {
				String dateFormat = EnumCountryCanal.getEnumFromId(userPreferences.getDisplayChannel()).getDateFormat();
				String mainUrl = EnumCountryCanal.getEnumFromId(userPreferences.getDisplayChannel()).getSiteGuiaTv();
				NumiCal date = new NumiCal(dayMovie.getDay().getStart());
				if (date.getHourOfDay() < 6 && date.getHourOfDay() >= 0) {
					date.addDays(-1);
				}
				new SmallImageParse().execute(mainUrl + date.toString(dateFormat));
			}
			if ("http://multicanaltv.com/images/hollywoodpt/sinimagen.jpg".equals(dayMovie.getMovie().getImageBigUrl()) ||
					"http://multicanaltv.com/images/hollywoodpt/sinimagen_th.jpg".equals(dayMovie.getMovie().getImageBigUrl())) {
				String mainUrl = EnumCountryCanal.getEnumFromId(userPreferences.getDisplayChannel()).getSiteBase();
				new SiteParser().execute(mainUrl + dayMovie.getMovie().getHollywoodUrl());
			}
			
			List<Actor> movieActors = DBUtils.getMovieActors(thisActivity, dayMovie.getMovie().getId());
			
			actorsImagesScroller = (LinearLayout) findViewById(R.id.actors_h_scroll);
			DrawableManager dm = new DrawableManager();
			for (int i = 0; i < movieActors.size() ; i ++) {
				ImageView actorImage = new ImageView(thisActivity);
				actorsImagesScroller.addView(actorImage);
				String imageUrl = movieActors.get(i).getBigImageUrl();
				if (imageUrl != null) {
					dm.fetchDrawableOnThread(movieActors.get(i).getBigImageUrl(), actorImage, true);
				}
			}
		} else {
			Toast.makeText(this, "Não possui uma ligação à internet activa. Active e tente novamente", Toast.LENGTH_LONG).show();
			if (dayMovie.getMovie().getDescription() != null) {
				ImageView alarmClock = (ImageView)findViewById(R.id.alarmOverImage);
				alarmClock.setVisibility(View.VISIBLE);
				DrawableManager dm = new DrawableManager();
				dm.fetchDrawableOnThread(dayMovie.getMovie().getImageBigUrl(), filmeImagem, loadPics);
				descricaoTv.setText(dayMovie.getMovie().getDescription());	
				nomeOriginalTV.setText(dayMovie.getMovie().getOriginalName());
				realizadorTv.setText(getString(R.string.realizador) + " " + dayMovie.getMovie().getDirector());
				anoTv.setText("Ano: " + dayMovie.getMovie().getYear());
				duracaoTv.setText(getString(R.string.duracao) + dayMovie.getDay().getDurationInMinutes() + " min");
				DBUtils.updateMovie(this, dayMovie.getMovie());
				pb.setVisibility(View.GONE);
			}
		}
	}
	

	/**
	 * Metodo que faz o parsing to site no background e guarda a informação do filme a mostrar
	 * @author Para
	 *
	 */
	private class SiteParser extends AsyncTask<String, Void, String> 
	{
		protected String doInBackground(String... arg) 
		{
			try {
				htmlHelperFilmInfo hh = new htmlHelperFilmInfo(new URL(arg[0]));
				dayMovie.getMovie().setImageBigUrl(hh.getImageURL("foto-ficha-prog_"));
				dayMovie.getMovie().setDescription(hh.getDescription("texto_principal_"));
				
				Integer year = Integer.parseInt(hh.getYear("ficha_prg_der"));
				if (year != null) {
					dayMovie.getMovie().setYear(year);					
				}
				Movie movie = hh.getInfoSecundaria("texto_datos_");
				dayMovie.getMovie().setDirector(movie.getDirector());
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			DBUtils.updateMovie(FilmeInfoActivity.this, dayMovie.getMovie());
			
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			pb.setVisibility(View.GONE);
			ImageView alarmClock = (ImageView)findViewById(R.id.alarmOverImage);
			alarmClock.setVisibility(View.VISIBLE);
			DrawableManager dm = new DrawableManager();
			dm.fetchDrawableOnThread(dayMovie.getMovie().getImageBigUrl(), filmeImagem, loadPics);
			descricaoTv.setText(dayMovie.getMovie().getDescription());	
			nomeOriginalTV.setText(dayMovie.getMovie().getOriginalName());
			anoTv.setText("Ano: " + dayMovie.getMovie().getYear());
			realizadorTv.setText(getString(R.string.realizador) + " " + dayMovie.getMovie().getDirector());
			duracaoTv.setText(getString(R.string.duracao) + dayMovie.getDay().getDurationInMinutes() + " min");
			super.onPostExecute(result);
		}
	}
	
	private class SmallImageParse extends AsyncTask<String, Void, List<String>> 
	{
		protected List<String> doInBackground(String... arg) 
		{
			ProgramacaoCanal listaConteudos; // objecto com toda a informação da
			// programação do dia (nomes,
			// horas, etc)
			// objecto que vai buscar informação ao site ao ser inicializado
			listaConteudos = new ProgramacaoCanal(arg[0], userPreferences.getDisplayChannel()); 
			List<DayMovie> listaFilmesDoDiaDaDB = listaConteudos.getProgramacaoDoDia();
			for (DayMovie movie : listaFilmesDoDiaDaDB) {
				if (movie.getLocalName().equals(dayMovie.getMovie().getLocalName())) {
					dayMovie.getMovie().setImagemSmallUrl(movie.getMovie().getImageSmallUrl());
					DBUtils.updateMovieSmallImageUrl(FilmeInfoActivity.this, dayMovie.getMovie());
					break;
				}
			}
			return new ArrayList<String>();
		}
		
		protected void onPostExecute(List<String> output) 
		{
			
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		List<Actor> movieActors = DBUtils.getMovieActors(thisActivity, dayMovie.getMovie().getId());
		
		actorsImagesScroller.removeAllViews();
		DrawableManager dm = new DrawableManager();
		for (int i = 0; i < movieActors.size() ; i ++) {
			ImageView actorImage = new ImageView(thisActivity);
			actorsImagesScroller.addView(actorImage);
			//					actorImage.setBackgroundResource(R.drawable.megan);
			String imageUrl = movieActors.get(i).getBigImageUrl();
			if (imageUrl != null) {
				dm.fetchDrawableOnThread(movieActors.get(i).getBigImageUrl(), actorImage, true);
			}
		}
	}
	
	public void onBackPressed()
	{
		backPressed = true;
		super.onBackPressed();
		finish();
	}
	
	public static boolean isUriAvailable(Context context, String uri) {
	    Intent test = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
	    return context.getPackageManager().resolveActivity(test, 0) != null;
	}
	
	private Intent createShareIntent() {
		String emailDescription = "<h1 style=\"font-size:50px;\">Não percas no teu canal Hollywood o filme " + 
				dayMovie.getMovie().getLocalName() + "</h1>\n No dia " + 
				dayMovie.getDay().getStart().toString("dd 'de' MMMM 'às' HH:mm") + ".\n";
		if (dayMovie.getMovie().getDescription() != null) {
			emailDescription = emailDescription.concat(dayMovie.getMovie().getDescription());
		}
		if (outrasDatas.size() > 0) {
			emailDescription = emailDescription.concat( 
					"\n\nSe perderes este podes ver nas datas seguintes:\n\n");					
		}
		for (String data : outrasDatas) {
			emailDescription = emailDescription.concat(data + "1n");			
		}
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String site = EnumCountryCanal.PORTUGAL.getSiteBase() + "/" + dayMovie.getMovie().getHollywoodUrl();
        emailDescription = new String("<html><body><br/>" + emailDescription +"<br/><br/>"
        + "Só no teu canal hollywood.<br/>"+"<a href=\"" + site + "\">" + "Mais info aqui:" + "</a>");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Alertas Canal Hollywood " + dayMovie.getMovie().getLocalName());
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailDescription));
        return Intent.createChooser(intent, "Share");
    }
	
	public static Intent createIntent(Context context) {
        Intent i = new Intent(context, GuiaHollywoodActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
	
	private class FinishAction extends ActionBar.AbstractAction {
        public FinishAction() {
            super(R.drawable.ic_title_home_default);
        }

        public void performAction(View view) {
            finish();
        }
    }
	
	private class AddActorAction extends ActionBar.AbstractAction {
		public AddActorAction(){
			super(R.drawable.actor);
		}

		@Override
		public void performAction(View view) {
			Intent addActorIntent = new Intent(FilmeInfoActivity.this, AddActorsToMovieActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("movieId", dayMovie.getMovie().getId());
			addActorIntent.putExtras(bundle);
			startActivity(addActorIntent);
		}
	}
	
	private class CreatePopUpForAlarm extends ActionBar.AbstractAction {
		public CreatePopUpForAlarm() {
			super(R.drawable.ic_alarm_clock_icon);
		}

		public void performAction(View view) {
			
			List<Day> movieDays = DBUtils.getMovieDays(thisActivity, dayMovie.getMovie().getId());
			MyLog.p(""+movieDays.size());
			//create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout 
			//orientation
			quickAction = new QuickAction(thisActivity, QuickAction.VERTICAL);
			
			for (int i = 0; i < futureDates.size(); i++) {
				ActionItem item = new ActionItem(i, futureDates.get(i).getStart().toString("EEEE, dd 'de' MMMM 'às' HH:mm"),
						getResources().getDrawable(R.drawable.menu_down_arrow));
				quickAction.addActionItem(item);				
			}
	        
	        //Set listener for action item clicked
			quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
				public void onItemClick(QuickAction source, int pos, int actionId) {				
					addAlarm(futureDates.get(pos).getStart().getDate());
				}
			});
			
			//set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
			//by clicking the area outside the dialog.
//			quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {			
//				public void onDismiss() {
//				}
//			});
			quickAction.show(view);			
		}
	}
	
	public void addAlarm(Date date) {
		Bundle bundle0 = new Bundle();
		bundle0.putString("pt", dayMovie.getMovie().getLocalName());
		bundle0.putString("data", dayMovie.getDay().getStart()
				.toString("yyyy-MM-dd"));
		bundle0.putString("imagem", dayMovie.getMovie().getImageBigUrl());
		bundle0.putString("original", dayMovie.getMovie().getOriginalName());
		bundle0.putString("descricao", dayMovie.getMovie().getDescription());
		bundle0.putString("realizador", dayMovie.getMovie().getDirector());
		bundle0.putInt("year", dayMovie.getMovie().getYear());
		bundle0.putString("horario",
				dayMovie.getDay().getStart()
				.toString(getString(R.string.dateFormat)));
		bundle0.putInt("duracao", (int) dayMovie.getDurationInMillis());
		bundle0.putLong("init", dayMovie.getDay().getStart().getTimeInMillis());
		bundle0.putLong("fim", dayMovie.getDay().getEnd().getTimeInMillis());
		NumiCal calendar = new NumiCal(date.getTime());
		calendar.addMinutes(-GuiaHollywoodActivity.timeBeforeInMinutes);
		Toast.makeText(thisActivity, "Adding alarm to " + calendar, Toast.LENGTH_LONG).show();
		new MovieAlarm(thisActivity, bundle0, calendar);
		DBUtils.addAlarmToDB(thisActivity, dayMovie.getDay().getId());
		Toast.makeText(thisActivity, "Alarm set", Toast.LENGTH_LONG).show();
	}

	public void onClick(View v) {
		if (v == seenButton) {
			DBUtils.setViewed(this, dayMovie.getMovie().getLocalName(), seenButton.isChecked());
			return;
		}
		if (v == watchListButton) {
			DBUtils.setWatchList(this, dayMovie.getMovie().getLocalName(), watchListButton.isChecked());
			return;
		}
		if (v == favButton) {
			DBUtils.setFavourite(this, dayMovie.getMovie().getLocalName(), favButton.isChecked());			
			return;
		}
	}
}
