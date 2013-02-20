/* 
 * 2011-09-15
 * Desenvolvido por Nuno Gonçalves
 * numicago@gmail.com 
 */
package com.numicago.guiahollywood.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.numicago.guiahollywood.DayMovieHelper;
import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.ProgramacaoCanal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.Utils;
import com.numicago.guiahollywood.activities.AlertUtils.EnumUseType;
import com.numicago.guiahollywood.adapters.CustomAdapter;
import com.numicago.guiahollywood.alarm.MovieAlarm;
import com.numicago.guiahollywood.dateslider.DateSlider;
import com.numicago.guiahollywood.objects.DayMovie;
import com.numicago.guiahollywood.objects.EnumCountryCanal;
import com.numicago.guiahollywood.objects.EnumSeason;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.objects.UserPreferences;
import com.numicago.guiahollywood.provider.DBUtils;
import com.numicago.guiahollywood.widgets.QuickAction;
import com.numicago.guiahollywood.widgets.menu.SatelliteMenu;
import com.numicago.guiahollywood.widgets.menu.SatelliteMenu.SateliteClickedListener;
import com.numicago.guiahollywood.widgets.menu.SatelliteMenuItem;

public class GuiaHollywoodActivity extends ListActivity {
	// Views
	private ListView lv;
	private ProgressDialog pd;
	private TextView dataListaTV;

	private int internetState; // Indica em runtime qual é o tipo de ligação

	private static NumiCal mainCal; // Calendário que contém o dia que está a ser
								// mostrado

	private Activity thisActivity;

	/** Definido em lerBundle() **/
	private String site;

	private int numFilmesDia;
	private List<DayMovie> listaFilmesDoDiaDaDB;
	public static final int REQUEST_THEME_LAYOUT = 1; // Chave de verificação de
														// resultset da
														// actividade de UI.

	private static int estiloEmUso; // Inicialmente a listView começa com fundo

	public static int timeBeforeInMinutes;

	private boolean reinicio = false; // Este boleano serve para indicar à
										// actividade quando esta é iniciada
										// pela primeira vez ou não

	public static Integer databaseVersion;

	public static UserPreferences userPreferences;

	TextView onairTV;
	ImageButton wifiButton;

	QuickAction quickAction;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		userPreferences = new UserPreferences(this);

		estiloEmUso = userPreferences.getTheme();
		timeBeforeInMinutes = userPreferences.getAlarmTimeBefore();
		databaseVersion = userPreferences.getdBVersion();

		SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);
		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
		items.add(new SatelliteMenuItem(1, R.drawable.package_icon));
		items.add(new SatelliteMenuItem(2, R.drawable.settings_icon));
		items.add(new SatelliteMenuItem(3, R.drawable.calendar_icon1));
		items.add(new SatelliteMenuItem(4, R.drawable.alarm_icon));
		items.add(new SatelliteMenuItem(5, R.drawable.statistics_icon));
		items.add(new SatelliteMenuItem(6, R.drawable.destaques_icon));
		menu.addItems(items);        

		menu.setOnItemClickedListener(new SateliteClickedListener() {
			public void eventOccured(int id) {
				if (id == 1) {
					Intent todosIntent = new Intent(getBaseContext(), AllMoviesActivity.class);
					thisActivity.startActivity(todosIntent);						
				} else if (id == 2) {
					Intent UIIntent = new Intent(getBaseContext(), PreferencesActivity.class);
					thisActivity.startActivityForResult(UIIntent, REQUEST_THEME_LAYOUT);
				} else if (id == 3) {
					Intent getMonthList = new Intent(getBaseContext(), MonthMoviesActivity.class);
					thisActivity.startActivity(getMonthList);
				} else if (id == 4) {
					viewAlarmList();
				} else if (id == 5) {
					Intent estatisticaInt = new Intent(getBaseContext(), EstatisticaActivity.class);
					thisActivity.startActivityForResult(estatisticaInt, REQUEST_THEME_LAYOUT);
				} else if (id == 6) {
					Intent destaquesIntent = new Intent(getBaseContext(), DestaquesActivity.class);
					thisActivity.startActivityForResult(destaquesIntent, REQUEST_THEME_LAYOUT);
				}
			}
		});
		
		thisActivity = GuiaHollywoodActivity.this;
		/*Actor travolta = new Actor();
		travolta.setId(22);
		travolta.setName("John Travolta");
		travolta.setImdbUrl("http://www.imdb.com/name/nm0001774/");
		travolta.setBday(new NumiCal(1954, 1, 18).getDate());
		travolta.setBigImageUrl("https://dl.dropbox.com/u/2001692/johnTravolta.jpg");
//		DBUtils.addNewActor(thisActivity, travolta);
		DBUtils.updateActor(thisActivity, travolta);*/
		
		
		onairTV = (TextView) findViewById(R.id.main_on_air_marqee_text);
		wifiButton = (ImageButton) findViewById(R.id.goToWifiOptions);
		dataListaTV = (TextView) findViewById(R.id.data_lista_filmes_text_view);
		

		if (!reinicio) {
			List<DayMovie> listAlarms = DBUtils.getAllAlarms(this);
			if (listAlarms.size() > 1)
				Toast.makeText(getBaseContext(),
						getString(R.string.existem_plural) + " " + listAlarms.size() + " "
						+ getString(R.string.alarmes_plural), Toast.LENGTH_LONG).show();
			if (listAlarms.size() == 1)
				Toast.makeText(getBaseContext(), getString(R.string.existe_singular), Toast.LENGTH_LONG).show();
		}

		// carregar informação inicial da app entre as quais o main cal.
		lerBundle(savedInstanceState);
		listaFilmesDoDiaDaDB = DBUtils.getMoviesOfDay(thisActivity, mainCal, userPreferences.getDisplayChannel());
		numFilmesDia = listaFilmesDoDiaDaDB.size();
		lv = getListView(); // Carregar a listView para a variável
		lv.setCacheColorHint(0); // fundo transparante do listview
		registerForContextMenu(lv);

		// Verificação de disponibilidade de internet
		if (numFilmesDia > 4) { // list contains info
			lv.setVisibility(View.VISIBLE);
			wifiButton.setVisibility(View.GONE);
			lv.setAdapter(new CustomAdapter(thisActivity, listaFilmesDoDiaDaDB, estiloEmUso));
			marqueeCurrentMovie();
		} else { // empty list
			if (userPreferences.getConnectionType() == UserPreferences.NO_INTERNET_CONNECTION
					|| (userPreferences.getConnectionType() == UserPreferences.MOBILE_CONNECTION && userPreferences
							.getDataPlanOption() == com.numicago.guiahollywood.objects.UserPreferences.EnumDataPlan.DOWN_NOTHING)) {
				lv.setVisibility(View.GONE);
				wifiButton.setVisibility(View.VISIBLE);
				wifiButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
					}
				});
			} else {
				lv.setVisibility(View.VISIBLE);
				wifiButton.setVisibility(View.GONE);
				if (!reinicio)
					Toast.makeText(getBaseContext(), getString(R.string.mostrar_filmes_db_hoje), Toast.LENGTH_LONG)
							.show();
				buscarDadosNet(site);
			}
		}

		// Ver informação do filme quando se clica no item da lista
		lv.setOnItemClickListener(listviewClickListener()); // ver info do filme
															// clicado

		ImageView backgronud = (ImageView) findViewById(R.id.main_icon_backgroud_image);
		if (EnumSeason.returnSeasonId() == EnumSeason.NATAL.getId()) {
			backgronud.setBackgroundResource(R.drawable.logoprincipalxmas);			
		} else {
			backgronud.setBackgroundResource(R.drawable.logoprincipal);			
		}
		
		ImageButton escolherDiaButton = (ImageButton) findViewById(R.id.escolherDiaButton);
		escolherDiaButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(1); // show dialog vai chamar onCreateDialog
			}
		});
	}

	public void dataPlanSelected(EnumUseType dataPlanUsetype) {
		switch (dataPlanUsetype) {
		case NO_USE_DATA_PLAN_DB_MOVIES:
			listaFilmesDoDiaDaDB = DBUtils.getMoviesOfDay(thisActivity, mainCal, userPreferences.getDisplayChannel());
			lv.setAdapter(new CustomAdapter(thisActivity, listaFilmesDoDiaDaDB, estiloEmUso));
			marqueeCurrentMovie();
			break;
		case NO_USE_DATA_PLAN_DB_EMPTY:
			finish();
			break;
		case USE_DATA_PLAN_DB_MOVIES:
			listaFilmesDoDiaDaDB = DBUtils.getMoviesOfDay(thisActivity, mainCal, userPreferences.getDisplayChannel());
			lv.setAdapter(new CustomAdapter(thisActivity, listaFilmesDoDiaDaDB, estiloEmUso));
			marqueeCurrentMovie();
			break;
		case USE_DATA_PLAN_DB_EMPTY:
			buscarDadosNet(site);
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_THEME_LAYOUT) {
			refreshActivity();
		}
	}

	// Insuflar o menu com os botões necessários
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	// Escolha de opção de menu
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_limpar_cache:
			Intent clearCacheIntent = new Intent(thisActivity, ClearCacheActivity.class);
			thisActivity.startActivity(clearCacheIntent);
			return true;
		case R.id.menu_sobre:
			Intent sobreIntent = new Intent(thisActivity, SobreActivity.class);
			thisActivity.startActivity(sobreIntent);
			return true;
		case R.id.menu_alarmes:
			viewAlarmList();
			return true;
		case R.id.menu_calendar:
			Intent getMonthList = new Intent(getBaseContext(), MonthMoviesActivity.class);
			thisActivity.startActivity(getMonthList);
			return true;
		case R.id.menu_estatistica:
			Intent estatisticaInt = new Intent(getBaseContext(), EstatisticaActivity.class);
			thisActivity.startActivityForResult(estatisticaInt, REQUEST_THEME_LAYOUT);
			return true;
		case R.id.menu_actors:
			Intent addActorI = new Intent(getBaseContext(), ActorsListActivity.class);
			thisActivity.startActivity(addActorI);
			return true;
		case R.id.menu_settings:
			Intent UIIntent = new Intent(getBaseContext(), PreferencesActivity.class);
			thisActivity.startActivityForResult(UIIntent, REQUEST_THEME_LAYOUT);
			return true;
		case R.id.menu_destaques:
			Intent destaquesIntent = new Intent(getBaseContext(), DestaquesActivity.class);
			thisActivity.startActivityForResult(destaquesIntent, REQUEST_THEME_LAYOUT);
			return true;
		case R.id.menu_all:
			Intent todosIntent = new Intent(getBaseContext(), AllMoviesActivity.class);
			thisActivity.startActivity(todosIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void refreshActivity() {
		Bundle bundle = new Bundle();
		// String dateFormat = (canalEmExibicao == EnumLanguage.PORTUGAL.getId()
		// ? Utils.SITE_DATE_FORMAT_PT : Utils.SITE_DATE_FORMAT_ES);
		String dateFormat = (EnumCountryCanal.getEnumFromId(userPreferences.getDisplayChannel()).getDateFormat());
		bundle.putString(Utils.SELECTED_DATE, (String) DateFormat.format(dateFormat, mainCal));
		bundle.putBoolean(Utils.REINICIO_STR, true);
		onCreate(bundle);
	}

	@Override
	public void onBackPressed() {
		AlertUtils.onBackPressed(thisActivity);
	}

	// define the listener which is called once a user selected the date.
	private DateSlider.OnDateSetListener mDateSetListener = new DateSlider.OnDateSetListener() {
		public void onDateSet(DateSlider view, Calendar selectedDate) {
			mainCal = new NumiCal(selectedDate);
			refreshActivity();
		}
	};

	/**
	 * Abrir diálogo com date slider
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// this method is called after invoking 'showDialog' for the first time
		// here we initiate the corresponding DateSlideSelector and return the
		// dialog to its caller
		final NumiCal c = new NumiCal();
		final NumiCal cInitTime = new NumiCal();
		// Definir um limite no passado para a escolha de um dia (um ano e dois
		// meses antes
		cInitTime.addMonths(-2);
		cInitTime.addYears(-1);
		return new DateSlider(this, R.layout.altdateslider, mDateSetListener, c, cInitTime, null);

	}

	/**
	 * ListView click listener
	 */
	private OnItemClickListener listviewClickListener() {
		
		OnItemClickListener clickListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				// Criar intent para abrir nova actividade para mostrar detalhes
				// do filme
				Intent filmeInfoIntent = new Intent(thisActivity, FilmeInfoActivity.class);
				DayMovieHelper.dayMovieAsBundle = listaFilmesDoDiaDaDB.get(position);
				thisActivity.startActivity(filmeInfoIntent);
			}
		};
		return clickListener;
	}

	/**
	 * Carregar informação utilizavel pela actividade
	 * 
	 * @param bundle
	 */
	private void lerBundle(Bundle bundle) {
		// Se tem informação vamos ver o que é
		if (bundle != null) {
			String dateString;
			if (bundle.containsKey(Utils.REINICIO_STR))
				reinicio = true;
			dateString = bundle.getString(Utils.SELECTED_DATE);
			if (dateString == null) {
				int displayChannel = userPreferences.getDisplayChannel();
				String dateFormat = EnumCountryCanal.getEnumFromId(displayChannel).getDateFormat();
				System.out.println("date format: " + dateFormat);
				if (mainCal == null) {
					mainCal = new NumiCal();
					if (mainCal.getHourOfDay() <= 6) { // Se hora entre as 0 e as 6
						mainCal.addDays(-1);
					}
				}
				dateString = (String) DateFormat.format(dateFormat, mainCal);
				// Se data for hoje depois das 6 ou amanhã até às 6 da manhã
			}
			site = EnumCountryCanal.getEnumFromId(userPreferences.getDisplayChannel()).getSiteGuiaTv() + dateString;
		} else {
			// if bundle vazio significa primeira vez a abrir, e vamos meter o
			// dia de hoje.
			mainCal = new NumiCal();
			if (mainCal.getHourOfDay() <= 6) { // Se hora entre as 0 e as 6
				mainCal.addDays(-1);
			}

			SimpleDateFormat sdf = new SimpleDateFormat(EnumCountryCanal.getEnumFromId(
					userPreferences.getDisplayChannel()).getDateFormat());
			String dateString = sdf.format(mainCal.getDate());
			site = EnumCountryCanal.getEnumFromId(userPreferences.getDisplayChannel()).getSiteGuiaTv() + dateString;
		}
		if (mainCal.isTodayAfterSixAndTomorrowBeforeSix()) {
			dataListaTV.setText(getString(R.string.hoje));
		} else {
			dataListaTV.setText(Utils.UppercaseFirstLetters(
					((String) DateFormat.format("dd/MMMM/yyyy\n EEEE", mainCal))).replace("/", " de "));
		}
	}

	/**
	 * Inicializa o ProgressDialog e vai fazer o parse do site
	 * 
	 * @param site
	 */
	private void buscarDadosNet(final String site) {
		pd = ProgressDialog.show(GuiaHollywoodActivity.this, getString(R.string.hold), getString(R.string.loading),
				true, true);
		new ParseSite().execute(site);
	}

	/**
	 * Ver lista de alarmes sob forma de diálogo
	 * 
	 * @author Para
	 * 
	 */
	private void viewAlarmList() {
		final List<DayMovie> filmesWithAlarms = DBUtils.getAllAlarms(this);

		String[] filmesList = new String[filmesWithAlarms.size()];
		for (int i = 0; i < filmesWithAlarms.size(); i++) {
			filmesList[i] = filmesWithAlarms.get(i).getMovie().getLocalName() + "\n"
					+ filmesWithAlarms.get(i).getDay().getStart().toString("yyyy-MM-dd HH:mm");
		}

		if (filmesWithAlarms.size() != 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.lista_alarmes));
			builder.setItems(filmesList, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, final int item) {
					final Dialog alarmDialog = new Dialog(thisActivity);

					alarmDialog.setContentView(R.layout.alarme_opcoes);
					alarmDialog.setTitle(getString(R.string.escolha_opcao));
					alarmDialog.show();

					Button eliminarButton = (Button) alarmDialog.findViewById(R.id.eliminarAlarmeButton);
					eliminarButton.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Toast.makeText(getBaseContext(), "Operação ainda não implementada", Toast.LENGTH_LONG)
									.show();
						}
					});
					Button verInfoButton = (Button) alarmDialog.findViewById(R.id.verInfoButton);
					verInfoButton.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							DayMovieHelper.dayMovieAsBundle = filmesWithAlarms.get(item);
							Intent intent = new Intent(thisActivity, FilmeInfoActivity.class);
							alarmDialog.hide();
							thisActivity.startActivity(intent);
						}
					});
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	// Parse do site
	private class ParseSite extends AsyncTask<String, Void, List<String>> {
		protected List<String> doInBackground(String... arg) {
			ProgramacaoCanal listaConteudos; // objecto com toda a informação da
												// programação do dia (nomes,
												// horas, etc)
			// objecto que vai buscar informação ao site ao ser inicializado
			listaConteudos = new ProgramacaoCanal(arg[0], userPreferences.getDisplayChannel()); 
			listaFilmesDoDiaDaDB = listaConteudos.getProgramacaoDoDia();
			return new ArrayList<String>();
		}

		protected void onPostExecute(List<String> output) {
			pd.dismiss();
			if (!DBUtils.moviesOfDayExist(thisActivity, mainCal, userPreferences.getDisplayChannel()))
				DBUtils.addMoviesOfDay(thisActivity, listaFilmesDoDiaDaDB, mainCal);
			listaFilmesDoDiaDaDB = DBUtils.getMoviesOfDay(thisActivity, mainCal, userPreferences.getDisplayChannel());
			if (internetState == Utils.WIFI_CONNECTION || internetState == Utils.MOBILE_CONNECTION) {
				lv.setAdapter(new CustomAdapter(thisActivity, listaFilmesDoDiaDaDB, estiloEmUso));// listaConteudos
				marqueeCurrentMovie();
			} else if (internetState == Utils.NO_INTERNET_CONNECTION && numFilmesDia > 0) {
				lv.setAdapter(new CustomAdapter(thisActivity, listaFilmesDoDiaDaDB, estiloEmUso));
				marqueeCurrentMovie();
			}
		}
	}

	public void onResume() {
		super.onResume();
		userPreferences.setLocale();
	}

	/**
	 * Long click menu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		
		if (v.getId() == android.R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			Movie movie = listaFilmesDoDiaDaDB.get(info.position).getMovie();
			menu.setHeaderTitle("Escolha uma opção");

			menu.add(0, 0, 0, movie.isFavourite() ? REM_FAVS : ADD_FAVS);
			menu.add(0, 1, 0, movie.isWatchList() ? REM_WATCH : ADD_WATCH);
			menu.add(0, 2, 0, movie.isViewed() ? REM_SEEN : ADD_SEEN);
			menu.add(0, 3, 0, ADD_ALARM);
			menu.add(0, 4, 0, movie.getRating() != null && movie.getRating() != 0.0 ? UPDATE_RATING : ADD_RATING);
		}
	}

	private String REM_FAVS = "Remover dos favoritos";
	private String ADD_FAVS = "Adicionar aos favoritos";
	private String REM_WATCH = "Remover da lista de interesse";
	private String ADD_WATCH = "Adicionar à lista de interesse";
	private String REM_SEEN = "Remover dos vistos";
	private String ADD_SEEN = "Adicionar aos vistos";
	private String ADD_ALARM = "Adicionar alarme";
	private String ADD_RATING = "Adicionar votação";
	private String UPDATE_RATING = "Editar votação";

	private static final int favsLongclick = 0;
	private static final int watchlistLongclick = 1;
	private static final int viewedLongclick = 2;
	private static final int alarmLongClick = 3;
	private static final int ratingLongClick = 4;

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		Movie movieSelected = listaFilmesDoDiaDaDB.get(info.position).getMovie();

		if (menuItemIndex == favsLongclick) {
			boolean addFavs = item.getTitle().toString().equals(ADD_FAVS);
			DBUtils.setFavourite(thisActivity, movieSelected.getLocalName(), addFavs ? true : false);
			movieSelected.setFavourite(addFavs ? true : false);
			((CustomAdapter) lv.getAdapter()).notifyDataSetChanged();
		} else if (menuItemIndex == watchlistLongclick) {
			boolean addWatch = item.getTitle().toString().equals(ADD_WATCH);
			DBUtils.setWatchList(thisActivity, movieSelected.getLocalName(), addWatch ? true : false);
			movieSelected.setWatchList(addWatch ? true : false);
			((CustomAdapter) lv.getAdapter()).notifyDataSetChanged();
		} else if (menuItemIndex == viewedLongclick) {
			boolean addSeen = item.getTitle().toString().equals(ADD_SEEN);
			DBUtils.setViewed(thisActivity, movieSelected.getLocalName(), addSeen ? true : false);
			movieSelected.setViewed(addSeen ? true : false);
			((CustomAdapter) lv.getAdapter()).notifyDataSetChanged();
		} else if (menuItemIndex == ratingLongClick) {
			Movie movie = listaFilmesDoDiaDaDB.get(info.position).getMovie();
			AlertUtils.createRatingAlert(thisActivity, movie.getRating(), movie);
		} else if (menuItemIndex == alarmLongClick) {
			Bundle bundle0 = new Bundle();
			bundle0.putString("pt", listaFilmesDoDiaDaDB.get(info.position).getMovie().getLocalName());
			bundle0.putString("data", listaFilmesDoDiaDaDB.get(info.position).getDay().getStart()
					.toString("yyyy-MM-dd"));
			bundle0.putString("imagem", listaFilmesDoDiaDaDB.get(info.position).getMovie().getImageBigUrl());
			bundle0.putString("original", listaFilmesDoDiaDaDB.get(info.position).getMovie().getOriginalName());
			bundle0.putString("descricao", listaFilmesDoDiaDaDB.get(info.position).getMovie().getDescription());
			bundle0.putString("realizador", listaFilmesDoDiaDaDB.get(info.position).getMovie().getDirector());
			bundle0.putInt("year", listaFilmesDoDiaDaDB.get(info.position).getMovie().getYear());
			bundle0.putString("horario",
					listaFilmesDoDiaDaDB.get(info.position).getDay().getStart()
							.toString(getString(R.string.dateFormat)));
			bundle0.putInt("duracao", (int) listaFilmesDoDiaDaDB.get(info.position).getDurationInMillis());
			bundle0.putLong("init", listaFilmesDoDiaDaDB.get(info.position).getDay().getStart().getTimeInMillis());
			bundle0.putLong("fim", listaFilmesDoDiaDaDB.get(info.position).getDay().getEnd().getTimeInMillis());
			NumiCal calendar = new NumiCal(listaFilmesDoDiaDaDB.get(info.position).getDay().getStart()
					.getTimeInMillis());
			calendar.addMinutes(-timeBeforeInMinutes);
			Toast.makeText(this, "Adding alarm to " + calendar, Toast.LENGTH_LONG).show();
			new MovieAlarm(thisActivity, bundle0, calendar);
			DBUtils.addAlarmToDB(thisActivity, listaFilmesDoDiaDaDB.get(info.position).getDay().getId());
			listaFilmesDoDiaDaDB.get(info.position).getDay().setHasAlarm(true);
			((CustomAdapter) lv.getAdapter()).notifyDataSetChanged();
		}
		return true;
	}

	public void resetMovieRating(Movie movie, float rating) {
		DBUtils.updateMovieRating(thisActivity, movie.getLocalName(), rating);
		movie.setRating(rating);
		((CustomAdapter) lv.getAdapter()).notifyDataSetChanged();
	}
	
	public void marqueeCurrentMovie() {
		onairTV.setText(DBUtils.getOnAirMovieString(thisActivity, userPreferences.getDisplayChannel()));
		onairTV.setVisibility(View.VISIBLE);
		onairTV.setSelected(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unbindDrawables(findViewById(R.id.mainRootView));
		System.gc();
	}

	private void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}
}
