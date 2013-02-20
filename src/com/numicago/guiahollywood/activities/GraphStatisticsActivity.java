package com.numicago.guiahollywood.activities;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewStyle;
import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.provider.DBUtils;


public class GraphStatisticsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.graph_statistics);
		
		int minYear = DBUtils.getMinYear(this, GuiaHollywoodActivity.userPreferences.getDisplayChannel());
		
		NumiCal cal = new NumiCal();
		int currentYear = cal.getYear();
		String [] years = new String[currentYear - minYear];
		GraphViewData[] gvData = new GraphViewData[currentYear - minYear];
		for (int i = 0; i <  currentYear - minYear; i++) {
			int count = DBUtils.getYearMoviesCount(this, (i + minYear), 
					GuiaHollywoodActivity.userPreferences.getDisplayChannel());
			years[i] = "" + (i + minYear);
			gvData[i] = new GraphViewData((i + minYear), (float)count);
		}
		
		GraphViewStyle style = new GraphViewStyle(Color.RED, 1);
//	    GraphViewSeries exampleSeries = new GraphViewSeries(gvData);
	    GraphViewSeries exampleSeries = new GraphViewSeries("Filmes por ano", style, gvData);
	    
	    GraphView graphView = new BarGraphView(this,"Filmes por ano");
	    graphView.setHorizontalLabels(years);
	    graphView.addSeries(exampleSeries); // data  
	    graphView.setViewPort(2000, 2011);  
	    graphView.setScrollable(true);  
	    // optional - activate scaling / zooming  
	    graphView.setScalable(true);  
	      
	    LinearLayout layout = (LinearLayout) findViewById(R.id.layout);  
	    layout.addView(graphView);  
	}
}
