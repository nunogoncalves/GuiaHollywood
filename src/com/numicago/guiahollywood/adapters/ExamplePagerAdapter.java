package com.numicago.guiahollywood.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.numicago.guiahollywood.MyLog;
import com.numicago.guiahollywood.activities.GuiaHollywoodActivity;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.provider.DBUtils;


public class ExamplePagerAdapter extends PagerAdapter {
	
	protected transient Activity mContext;
	
//	private int mLength = 0;
//	private int mBackgroundColor = 0xFFFFFFFF;
//	private int mTextColor = 0xFF000000;
	private ExpandableListView lv;
	
	private int channel = GuiaHollywoodActivity.userPreferences.getDisplayChannel();
	private List<List<Movie>> listListMovies;
	
	public ExamplePagerAdapter(Activity context, int length, int backgroundColor, int textColor) {
		mContext = context;
//		mLength = length;
//		mBackgroundColor = backgroundColor;
//		mTextColor = textColor;
		listListMovies = new ArrayList<List<Movie>>();
		
		List<Movie> favs = DBUtils.getFavouriteMovies(mContext, channel);
		listListMovies.add(favs);
		List<Movie> viewed = DBUtils.getViewedMovies(mContext, channel);
		listListMovies.add(viewed);
		List<Movie> watchList = DBUtils.getWatchlistMovies(mContext, channel);
		listListMovies.add(watchList);
		List<Movie> ratings = DBUtils.getRatingMovies(mContext, channel);
		listListMovies.add(ratings);
	}
	
	@Override
	public int getCount() {
		return 4;
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		MyLog.p("Instantiate expandable");
		RelativeLayout v = new RelativeLayout(mContext);
		
		lv = new ExpandableListView(mContext);
		lv.setGroupIndicator(null);
		lv.setAdapter(new ExpadableAdapter(mContext, listListMovies.get(position)));
		
//		TextView t = new TextView(mContext);
//		TextView t1 = new TextView(mContext);
//		t1.setText("Test");
//		t.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		t.setText(mData[position]);
//		t.setTextSize(120);
//		t.setGravity(Gravity.CENTER);
//		t.setTextColor(mTextColor);
//		t.setBackgroundColor(mBackgroundColor);
		
//		v.addView(t);
		v.addView(lv);
		
		((ViewPager) container).addView(v, 0);
		((ViewPager) container).setBackgroundColor(new Random().nextInt(0xFFFFFF));
		return v;
	}
	
	
	@Override
	public void destroyItem(View container, int position, Object view) {
		((ViewPager) container).removeView((View) view);
	}
	
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}
	
	
	@Override
	public void finishUpdate(View container) {}
	
	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {}
	
	@Override
	public Parcelable saveState() {
		return null;
	}
	
	@Override
	public void startUpdate(View container) {}
	
}
