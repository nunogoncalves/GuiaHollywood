package com.numicago.guiahollywood.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.widgets.TitleProvider;

public class ViewFlowAdapter extends BaseAdapter implements TitleProvider {

	private static final int all = 0;
	private static final int favs = 1;
	private static final int vistos = 2;
	private static final int watchlist = 3;
	private static final int ratings = 4;
	private static final int yearsProgress = 5;
	public static final int VIEW_MAX_COUNT = 6;
	private static String allString = "Todos";
	private static String favString = "Favoritos";
	private static String seenString = "Vistos";
	private static String watchListString = "Interesses";
	private static String ratingsString = "Votações";
	private static String yearsProgressString = "Filmes por Ano";

	private int allNum = 0;
	private int favsNum = 0;
	private int seenNum = 0;
	private int watchListNum = 0;
	private int ratingNum = 0;

	private final String[] names = {allString, favString, seenString, watchListString, ratingsString, yearsProgressString};

	private final int[] nums = {allNum, favsNum, seenNum, watchListNum, ratingNum, 0};

	private LayoutInflater mInflater;

	private class ViewHolder {
//		ProgressBar mProgressBar;
		View mContent;
		TextView mDate;
	}
	
	public ViewFlowAdapter(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_MAX_COUNT;
	}

	public int getCount() {
		return VIEW_MAX_COUNT;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public String getTitle(int position) {
		if (nums[position] == 0) {
			return names[position];
		}
		return names[position] + " (" + nums[position] + ")";
	}

	public void setAllNum(int favsNum) {
		this.allNum = favsNum;
		nums[0] = favsNum;
	}

	public void setFavsNum(int favsNum) {
		this.favsNum = favsNum;
		nums[1] = favsNum;
	}

	public void setSeenNum(int seenNum) {
		this.seenNum = seenNum;
		nums[2] = seenNum;
	}

	public void setWatchListNum(int watchListNum) {
		this.watchListNum = watchListNum;
		nums[3] = watchListNum;
	}

	public void setRatingNum(int ratingNum) {
		this.ratingNum = ratingNum;
		nums[4] = ratingNum;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		int view = getItemViewType(position);
		if (convertView == null) {
			switch (view) {
			case all:
				convertView = mInflater.inflate(R.layout.all_list_view, null);
				break;
			case favs:
				convertView = mInflater.inflate(R.layout.favs_list_view, null);
				break;
			case vistos:
				convertView = mInflater.inflate(R.layout.vistos_list_view, null);
				break;
			case watchlist:
				convertView = mInflater.inflate(R.layout.watch_list_view, null);
				break;
			case ratings:
				convertView = mInflater.inflate(R.layout.ratings_list_view, null);
				break;
			case yearsProgress:
				convertView = mInflater.inflate(R.layout.years_progress_scroller, null);
				break;
			}
		}
		return convertView;
	}
}
