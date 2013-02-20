package com.numicago.guiahollywood.homewidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.R;
import com.numicago.guiahollywood.activities.GuiaHollywoodActivity;
import com.numicago.guiahollywood.objects.Day;
import com.numicago.guiahollywood.objects.DayMovie;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.objects.UserPreferences;
import com.numicago.guiahollywood.provider.DBUtils;
import com.numicago.guiahollywood.provider.DatabaseMetaData.DayTMD;
import com.numicago.guiahollywood.provider.DatabaseMetaData.MovieTMD;

public class HomeWidgetView extends AppWidgetProvider implements Comparator<DayMovie> {

	RemoteViews remoteViews;

	AppWidgetManager appWidgetManager;

	List<Day> dayList;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		remoteViews = new RemoteViews(context.getPackageName(), R.layout.home_widget_layout);
		this.appWidgetManager = appWidgetManager;
		
		for (int appWidgetId : appWidgetIds) {

			NumiCal today = new NumiCal();
			long now = System.currentTimeMillis();

			new UserPreferences(context);
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
			String key = settings.getString(UserPreferences.CHANNEL_PREF_KEY,
					context.getString(R.string.canalPortugal));

			int channelId = UserPreferences.DISPLAY_CHANNEL.get(key);
			today.addHours(8);
			long end8Hours = today.getTimeInMillis();

			String queryDay = DayTMD.DATE_END + " > " + now + " AND " + DayTMD.DATE_END + " < " + end8Hours;

			Cursor c = context.getContentResolver().query(DayTMD.CONTENT_URI, null, 
					queryDay, null, DayTMD.DATE_END);

			dayList = new ArrayList<Day>();
			String movieIdVals = "";
			int count = 0;
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				dayList.add(DBUtils.readDayFromCursor(c));
				movieIdVals = movieIdVals + dayList.get(count++).getMovieId() + ", ";
			}
			c.close();

			if (movieIdVals != null && movieIdVals.length() != 0) {

				movieIdVals = movieIdVals.substring(0, movieIdVals.length() - 2);

				String queryMovie = MovieTMD._ID + " IN " + "(" + movieIdVals + ")" 
						+ " AND " + MovieTMD.CHANNEL + " = " + channelId;

				Cursor cMovie = context.getContentResolver().query(MovieTMD.CONTENT_URI, null, queryMovie, null, null);
				int [] timeTvs = new int[] {R.id.home_time_tv1, R.id.home_time_tv2, 
						R.id.home_time_tv3, R.id.home_time_tv4};
				int [] tvs = new int[] {R.id.home_widget_tv1, R.id.home_widget_tv2, R.id.home_widget_tv3,
						R.id.home_widget_tv4};

				List<DayMovie> listMovies = new ArrayList<DayMovie>();

				int i = 0;
				if (cMovie.moveToFirst()) {
					
					for (i = 0; i < tvs.length && !cMovie.isAfterLast(); i++) {
						String name = cMovie.getString(cMovie.getColumnIndex(MovieTMD.LOCAL_NAME));
						NumiCal start = getCorrectDay(cMovie.getInt(cMovie.getColumnIndex(MovieTMD._ID))).getStart();

						Movie movie = new Movie();
						movie.setLocalName(name);
						Day day = new Day();
						day.setStart(start);

						DayMovie dayMovie = new DayMovie();
						dayMovie.setDay(day);
						dayMovie.setMovie(movie);

						listMovies.add(dayMovie);

						cMovie.moveToNext();
					}
					cMovie.close();
				}

				Collections.sort(listMovies, this);

				for (int j = 0; j < i && j < listMovies.size(); j++) {
					//Caused by: java.lang.IndexOutOfBoundsException: Invalid index 3, size is 3
					remoteViews.setTextViewText(timeTvs[j], listMovies.get(j).getDay().getStart().toString("HH:mm"));
					remoteViews.setTextViewText(tvs[j], " - " + listMovies.get(j).getMovie().getLocalName());
				}

//				ComponentName thisWidget = new ComponentName(context, HomeWidgetView.class);
				this.appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
//				this.appWidgetManager.updateAppWidget(thisWidget, remoteViews);
				// Create an Intent to launch ExampleActivity
	            Intent intent = new Intent(context, GuiaHollywoodActivity.class);
	            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
	            remoteViews.setOnClickPendingIntent(R.id.home_time_tv1, pendingIntent);
			}
		}
	}

	private Day getCorrectDay(int movieId) {
		for (Day day : dayList) {
			if (day.getMovieId() == movieId) {
				return day;
			}
		}
		return null;
	}

	public int compare(DayMovie day1, DayMovie day2) {
		return day1.getDay().getStart().getTime().compareTo(day2.getDay().getStart().getDate());
	}
}
