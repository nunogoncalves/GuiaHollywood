package com.numicago.guiahollywood.adapters;

import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.pager.SwipeyTabButton;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

public class SwipeyTabsAdapter implements TabsAdapter {
	
	private Activity mContext;
	
	private String[] mTitles = {"Favoritos", "Vistos", "Interesses", "Votações"};
	
	public SwipeyTabsAdapter(Activity ctx) {
		this.mContext = ctx;
	}
	
	public View getView(int position) {
		SwipeyTabButton tab;
		
		LayoutInflater inflater = mContext.getLayoutInflater();
		tab = (SwipeyTabButton) inflater.inflate(R.layout.tab_swipey, null);
		
		if (position < mTitles.length) {
			tab.setText(mTitles[position]);
		}
		
		return tab;
	}
	
}
