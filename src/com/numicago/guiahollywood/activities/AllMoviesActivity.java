package com.numicago.guiahollywood.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.Utils;
import com.numicago.guiahollywood.adapters.ExpadableAdapter;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.provider.DBUtils;

public class AllMoviesActivity extends Activity {

	private ExpadableAdapter mAdapter;

	private ExpandableListView expLV;

	private FrameLayout searchLayout;

	private AutoCompleteTextView searchBox;
	
	private List<Movie> listMovies;

	private ImageButton hideButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		listMoviesToPass = listMovies;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.estatisticas);

		listMovies = DBUtils.getAllMovies(this, 
				GuiaHollywoodActivity.userPreferences.getSortBy(),
				GuiaHollywoodActivity.userPreferences.getAscDesc(), 
				GuiaHollywoodActivity.userPreferences.getDisplayChannel());

		expLV = (ExpandableListView) findViewById(R.id.tracklist);
		mAdapter = new ExpadableAdapter(this, listMovies);
		expLV.setAdapter(mAdapter);
		expLV.setCacheColorHint(0); //fundo transparante do listview
		expLV.setGroupIndicator(null);
		//		registerForContextMenu(expLV);

		LinearLayout infoLayout = (LinearLayout) findViewById(R.id.infoLayout);
		infoLayout.addView(Utils.createImageNumber(this, listMovies.size()));
		
		TextView nFilmes = (TextView) findViewById(R.id.estatistica_n_filmes);
		nFilmes.setVisibility(View.GONE);
//		nFilmes.setText("Total de filmes: " + listMovies.size());
	}

	//Insuflar o menu com os botões necessários
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.all_movies_menu, menu);
		return true;
	}

	//Escolha de opção de menu
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.estatistica_menu_search:
			search();			
			return true;
		case R.id.estatistica_menu_confs:
			Intent intent = new Intent(this, EstatisticaPreferencesActivity.class);
			//			Intent intent = new Intent(this, SortActivity.class);
			startActivity(intent);
			return true;
		case R.id.estatistica_menu_premieres:
			Intent premiersIntent = new Intent(this, MonthPremieresActivity.class);
			startActivity(premiersIntent);
			return true;
		case R.id.estatistica_get_all_info:
			listMoviesToPass = listMovies;
			Intent downAllI = new Intent(this, DownAllInfoActivity.class);
			startActivity(downAllI);
		default:
			return true;
		}
	}

	public static List<Movie> listMoviesToPass;

	private void search() {
		if (searchLayout == null) {
			searchLayout = (FrameLayout) findViewById(R.id.estatistica_search_layout);
			searchBox = (AutoCompleteTextView) findViewById(R.id.estatistica_search_box);
			hideButton = (ImageButton) findViewById(R.id.close_button);
		}
		searchLayout.setVisibility(View.VISIBLE);
		searchBox.setHint(GuiaHollywoodActivity.userPreferences.getSearchBy());

		String[] names = null;
		if (GuiaHollywoodActivity.userPreferences.getSearchBy().equals(
				getString(R.string.localName))) {
			names = new String[listMovies.size()];
			for (int i = 0; i < listMovies.size(); i++) {
				names[i] = listMovies.get(i).getLocalName();
			}
		} else {
			names = new String[listMovies.size()];
			for (int i = 0; i < listMovies.size(); i++) {
				names[i] = listMovies.get(i).getOriginalName();
			}
		}

		ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, names);
		searchBox.setAdapter(namesAdapter);

		searchBox.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
				String name = searchBox.getText().toString();
				Integer selected = null;
				if (GuiaHollywoodActivity.userPreferences.getSearchBy().equals(
						getString(R.string.localName))) {
					for (int i = 0; i < listMovies.size(); i++) {
						if (name.equals(listMovies.get(i).getLocalName())) {
							selected = i;
							break;
						}
					}
				} else {
					for (int i = 0; i < listMovies.size(); i++) {
						if (name.equals(listMovies.get(i).getOriginalName())) {
							selected = i;
							break;
						}
					}

				}
				if (selected != null) {
					expLV.setSelectedGroup(selected.intValue());	
				}
			}
		});

		hideButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				searchLayout.setVisibility(View.GONE);
				searchBox.setText("");
			}
		});
	}

	@Override
	protected void onRestart() {
		onCreate(null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unbindDrawables(findViewById(R.id.RootView));
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

	@Override
	protected void onResume() {
		super.onResume();
		if (expLV.getAdapter() != null && expLV.getAdapter() instanceof ExpadableAdapter) {
			((ExpadableAdapter) expLV.getAdapter()).notifyDataSetChanged();
		}
	}
}
