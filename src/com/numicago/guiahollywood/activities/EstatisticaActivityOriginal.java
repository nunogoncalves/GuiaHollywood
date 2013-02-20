package com.numicago.guiahollywood.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.numicago.guiahollywood.AnimationUtils;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.Utils;
import com.numicago.guiahollywood.adapters.ExpadableAdapter;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.provider.DBUtils;

public class EstatisticaActivityOriginal extends Activity {

	private ViewFlipper vf;  

	View leftView;
	View rightView;
	View currentView;

	View favViewBlue;
	View watchListViewBlue;
	View vistosViewBlue;
	View ratingsViewBlue;

	ExpandableListView favsList;
	ExpandableListView watchList;
	ExpandableListView vistosList;
	ExpandableListView ratingsList;
	
	ImageView favsImage;
	ImageView watchImage;
	ImageView viewedImage;
	ImageView ratingImage;

	View[] viewLiss = new View[]{favsList, watchList, vistosList, ratingsList};

	private float oldTouchValue;

	int orientation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.estatistica);

		orientation = getResources().getConfiguration().orientation;
		
		LinearLayout llFavs = (LinearLayout) findViewById(R.id.estatistica_favs_value_ll);
		llFavs.addView(Utils.createImageNumber(this, DBUtils.countFavorites(this)));
		LinearLayout llViewd = (LinearLayout) findViewById(R.id.estatistica_vistos_value_ll);
		llViewd.addView(Utils.createImageNumber(this, DBUtils.countViewed(this)));
		LinearLayout llWatchList = (LinearLayout) findViewById(R.id.estatistica_watch_list_value_ll);
		llWatchList.addView(Utils.createImageNumber(this, DBUtils.countWatchList(this)));
		LinearLayout llRatingList = (LinearLayout) findViewById(R.id.estatistica_rating__ll);
		llRatingList.addView(Utils.createImageNumber(this, DBUtils.countRatingList(this)));

		vf = (ViewFlipper) findViewById(R.id.viewFlipper);

		favViewBlue = findViewById(R.id.favoritos_blue_view);
		watchListViewBlue = findViewById(R.id.watchlist_blue_view);
		vistosViewBlue = findViewById(R.id.vistos_blue_view);
		ratingsViewBlue = findViewById(R.id.ratings_blue_view);

		favsImage = (ImageView) findViewById(R.id.favsIV);
		watchImage = (ImageView) findViewById(R.id.watchIV);
		viewedImage = (ImageView) findViewById(R.id.viewIV);
		ratingImage = (ImageView) findViewById(R.id.ratingIV);
		
		favsList = (ExpandableListView) findViewById(R.id.favs_list);
		favsList.setGroupIndicator(null);
		watchList = (ExpandableListView) findViewById(R.id.whatch_view);
		watchList.setGroupIndicator(null);
		vistosList = (ExpandableListView) findViewById(R.id.vistos_view);
		vistosList.setGroupIndicator(null);
		ratingsList = (ExpandableListView) findViewById(R.id.ratings_view);
		ratingsList.setGroupIndicator(null);

		currentView = favsList;

		List<Movie> favMovies = DBUtils.getFavouriteMovies(this,  
				GuiaHollywoodActivity.userPreferences.getDisplayChannel());
		favsList.setAdapter(new ExpadableAdapter(this, favMovies));

		List<Movie> watchMovies = DBUtils.getWatchlistMovies(this,  
				GuiaHollywoodActivity.userPreferences.getDisplayChannel());
		watchList.setAdapter(new ExpadableAdapter(this, watchMovies));

		List<Movie> vistosMovies = DBUtils.getViewedMovies(this,  
				GuiaHollywoodActivity.userPreferences.getDisplayChannel());
		vistosList.setAdapter(new ExpadableAdapter(this, vistosMovies));

		List<Movie> ratingsMovies = DBUtils.getRatingMovies(this,  
				GuiaHollywoodActivity.userPreferences.getDisplayChannel());
		ratingsList.setAdapter(new ExpadableAdapter(this, ratingsMovies));
	}

	@Override
	public boolean onTouchEvent(MotionEvent touchevent) {
		switch (touchevent.getAction())
		{
		case MotionEvent.ACTION_DOWN:
		{
			oldTouchValue = touchevent.getX();
			if (vf.getCurrentView().getId() == watchList.getId()) {
				currentView = watchList;
				leftView = favsList;
				rightView = vistosList;
			} else if (vf.getCurrentView().getId() == favsList.getId()) {
				currentView = favsList;
				leftView = ratingsList;
				rightView = watchList;
			} else if (vf.getCurrentView().getId() == vistosList.getId()) {
				currentView = vistosList;
				leftView = watchList;
				rightView = ratingsList;
			} else if (vf.getCurrentView().getId() == ratingsList.getId()) {
				currentView = ratingsList;
				leftView = vistosList;
				rightView = favsList;
			}
			break;
		}
		case MotionEvent.ACTION_UP:
		{
			//				if(this.searchOk==false) return false;
			float currentX = touchevent.getX();
			if (oldTouchValue - currentX < -50)
			{
				if (orientation == Configuration.ORIENTATION_PORTRAIT) {
					vf.setInAnimation(AnimationUtils.inFromLeftAnimation());
					vf.setOutAnimation(AnimationUtils.outToRightAnimation());
				} else {
					vf.setInAnimation(AnimationUtils.inFromTopAnimation());
					vf.setOutAnimation(AnimationUtils.outFromBottom());					
				}
				vf.showNext();
				watchListViewBlue.setVisibility(View.GONE);
				favsImage.setImageResource(R.drawable.heart_big_baw);
				viewedImage.setImageResource(R.drawable.seen_baw);
				ratingImage.setImageResource(R.drawable.star_baw);
				watchImage.setImageResource(R.drawable.warning_baw);
				favViewBlue.setVisibility(View.GONE);
				vistosViewBlue.setVisibility(View.GONE);
				ratingsViewBlue.setVisibility(View.GONE);
				
				if (vf.getCurrentView().getId() == watchList.getId()) {
					watchListViewBlue.setVisibility(View.VISIBLE);
					watchImage.setImageResource(R.drawable.warning);
				} else if (vf.getCurrentView().getId() == favsList.getId()) {
					favsImage.setImageResource(R.drawable.heart_big_color);
					favViewBlue.setVisibility(View.VISIBLE);
				} else if (vf.getCurrentView().getId() == vistosList.getId()) {
					viewedImage.setImageResource(R.drawable.seen_color);
					vistosViewBlue.setVisibility(View.VISIBLE);
				} else if (vf.getCurrentView().getId() == ratingsList.getId()) {
					ratingsViewBlue.setVisibility(View.VISIBLE);
					ratingImage.setImageResource(R.drawable.star_color);
				}
			}
			if (oldTouchValue - currentX > 50)
			{
				vf.setInAnimation(AnimationUtils.inFromRightAnimation());
				vf.setOutAnimation(AnimationUtils.outToLeftAnimation());
				vf.showPrevious();
				
				watchListViewBlue.setVisibility(View.GONE);
				favsImage.setImageResource(R.drawable.heart_big_baw);
				viewedImage.setImageResource(R.drawable.seen_baw);
				ratingImage.setImageResource(R.drawable.star_baw);
				watchImage.setImageResource(R.drawable.warning_baw);
				favViewBlue.setVisibility(View.GONE);
				vistosViewBlue.setVisibility(View.GONE);
				ratingsViewBlue.setVisibility(View.GONE);
				
				if (vf.getCurrentView().getId() == vistosList.getId()) {
					vistosViewBlue.setVisibility(View.VISIBLE);
					viewedImage.setImageResource(R.drawable.seen_color);
				} else if (vf.getCurrentView().getId() == favsList.getId()) {
					favsImage.setImageResource(R.drawable.heart_big_color);
					favViewBlue.setVisibility(View.VISIBLE);
				} else if (vf.getCurrentView().getId() == watchList.getId()) {
					watchListViewBlue.setVisibility(View.VISIBLE);
					watchImage.setImageResource(R.drawable.warning);
				} else if (vf.getCurrentView().getId() == ratingsList.getId()) {
					ratingImage.setImageResource(R.drawable.star_color);
					ratingsViewBlue.setVisibility(View.VISIBLE);
				}
			}
			break;
		}
		case MotionEvent.ACTION_MOVE:
			break;
		}
		return false;
	}	

	//Insuflar o menu com os botões necessários
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.estatistica_movies_menu, menu);
		return true;
	}

	//Escolha de opção de menu
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.menu_graph_stats:
			Intent graphIntent = new Intent(this, GraphStatisticsActivity.class);
			startActivity(graphIntent);
			return true;
		default: 
			return super.onOptionsItemSelected(item);
		}
	}
}
