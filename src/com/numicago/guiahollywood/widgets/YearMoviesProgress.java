package com.numicago.guiahollywood.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.numicago.guiahollywood.R;

public class YearMoviesProgress extends LinearLayout {

	 private LayoutInflater mInflater;
	 
	 private LinearLayout yearProgress;
	
	 private TextView yearTv;
	 
	 private ProgressBar pb;
	 
	 private TextView countTv;
	 
	 private int year;
	 private int count; 
	 private int max;
	 
	public YearMoviesProgress(Context context) {
		super(context);
		setOrientation(HORIZONTAL);
		
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		yearProgress = (LinearLayout) mInflater.inflate(R.layout.year_progress, null);
		pb = (ProgressBar) yearProgress.findViewById(R.id.year_progress_count);
		yearTv = (TextView) yearProgress.findViewById(R.id.year_progress_year_tv);
		countTv = (TextView) yearProgress.findViewById(R.id.year_progress_count_tv);
		
		addView(yearProgress);
	}
	
	public YearMoviesProgress(Context context, int year, int count, int max) {
		this(context);
		this.year = year;
		yearTv.setText("Ano: " + year);
		this.count = count;
		countTv.setText("Contagem: "+ count);
		this.max = max;
		pb.setMax(max);
		pb.setProgress(count);
	}
	
	public void setMaxProgress(int max) {
		this.max = max;
		pb.setMax(max);
		pb.setProgress(count);
	}
	
	public void setYear(int year) {
		yearTv.setText(year + "" + " max = " + max);
	}
	
	public void setCount(int count) {
		countTv.setText("Contagem: " + count);
	}
}
