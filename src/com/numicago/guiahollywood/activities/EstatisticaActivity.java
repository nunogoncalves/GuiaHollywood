package com.numicago.guiahollywood.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.adapters.ExpadableAdapter;
import com.numicago.guiahollywood.adapters.ViewFlowAdapter;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.provider.DBUtils;
import com.numicago.guiahollywood.widgets.TitleFlowIndicator;
import com.numicago.guiahollywood.widgets.ViewFlow;
import com.numicago.guiahollywood.widgets.YearMoviesProgress;


public class EstatisticaActivity extends Activity {

	private ViewFlow viewFlow;
	private ExpandableListView listAll;
	private ExpandableListView listFavs;
	private ExpandableListView listSeen;
	private ExpandableListView listWatchList;
	private ExpandableListView listRating;
	private ScrollView scrollView;
	
	private int channel = GuiaHollywoodActivity.userPreferences.getDisplayChannel();
	
//	private ViewPager mPager;
//	private SwipeyTabsView mSwipeyTabs;
//	
//	private PagerAdapter mPagerAdapter;
//	private TabsAdapter mSwipeyTabsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title_layout);

		List<Movie> allMovies = DBUtils.getAllMovies(this, "", true, channel);
		List<Movie> watchlistMovies = DBUtils.getWatchlistMovies(this, channel);
		List<Movie> favMovies = DBUtils.getFavouriteMovies(this, channel);
		List<Movie> seenMovies = DBUtils.getViewedMovies(this, channel);
		List<Movie> ratingMovies = DBUtils.getRatingMovies(this, channel);
		
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		ViewFlowAdapter adapter = new ViewFlowAdapter(this);
		adapter.setAllNum(allMovies.size());
		adapter.setFavsNum(favMovies.size());
		adapter.setSeenNum(seenMovies.size());
		adapter.setWatchListNum(watchlistMovies.size());
		adapter.setRatingNum(ratingMovies.size());
		viewFlow.setAdapter(adapter);
		
		listAll = (ExpandableListView) findViewById(R.id.all_list);
		listAll.setGroupIndicator(null);
		ExpadableAdapter allListAdapter = new ExpadableAdapter(this, allMovies);
		listAll.setAdapter(allListAdapter);
		
		listWatchList = (ExpandableListView) findViewById(R.id.watch_list);
        listWatchList.setGroupIndicator(null);
        ExpadableAdapter watchListAdapter = new ExpadableAdapter(this, watchlistMovies);
        listWatchList.setAdapter(watchListAdapter);
        
        listFavs = (ExpandableListView) findViewById(R.id.favs_list);
        listFavs.setGroupIndicator(null);
        ExpadableAdapter favsAdapter = new ExpadableAdapter(this, favMovies);
        listFavs.setAdapter(favsAdapter);
        
        listSeen = (ExpandableListView) findViewById(R.id.vistos_list);
        listSeen.setGroupIndicator(null);
        ExpadableAdapter seenAdapter = new ExpadableAdapter(this, seenMovies);
        listSeen.setAdapter(seenAdapter);
        
        listRating = (ExpandableListView) findViewById(R.id.ratings_list);
        listRating.setGroupIndicator(null);
        ExpadableAdapter ratingAdapter = new ExpadableAdapter(this, ratingMovies);
        listRating.setAdapter(ratingAdapter);
		
        scrollView = (ScrollView) findViewById(R.id.years_progress_scroller_scrollview);
        yearsLinear = (LinearLayout) scrollView.getChildAt(0);
        buildYearsProgress();
        
        //provider for the title on the top.
        TitleFlowIndicator indicator = (TitleFlowIndicator) findViewById(R.id.viewflowindic);
		indicator.setTitleProvider(adapter);
		viewFlow.setFlowIndicator(indicator);
	}

	LinearLayout yearsLinear;
	
	private void buildYearsProgress() {
		//Set the years
		int minYear = DBUtils.getMinYear((Activity) this, 
				GuiaHollywoodActivity.userPreferences.getDisplayChannel());
		
		NumiCal cal = new NumiCal();
		int currentYear = cal.getYear();
		int max = 0;
		SparseIntArray yearCountMap = new SparseIntArray();
		for (int i = 0; i <  currentYear - minYear; i++) {
			int count = DBUtils.getYearMoviesCount((Activity) this, (i + minYear), 
					GuiaHollywoodActivity.userPreferences.getDisplayChannel());
			yearCountMap.put(i + minYear, count);
			if (count > max) {
				max = count;
			}
		}
		for (int i = 0; i <  currentYear - minYear; i++) {
			YearMoviesProgress ymp = new YearMoviesProgress(this, 
					i + minYear, yearCountMap.get(i + minYear), max);
			yearsLinear.addView(ymp);
		}
	}
}
