package com.numicago.guiahollywood.provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.numicago.guiahollywood.NumiCal;
import com.numicago.guiahollywood.activities.GuiaHollywoodActivity;
import com.numicago.guiahollywood.objects.Actor;
import com.numicago.guiahollywood.objects.Day;
import com.numicago.guiahollywood.objects.DayMovie;
import com.numicago.guiahollywood.objects.Movie;
import com.numicago.guiahollywood.provider.DatabaseMetaData.ActorTMD;
import com.numicago.guiahollywood.provider.DatabaseMetaData.DayTMD;
import com.numicago.guiahollywood.provider.DatabaseMetaData.DirectorTMD;
import com.numicago.guiahollywood.provider.DatabaseMetaData.MovieTMD;
import com.numicago.guiahollywood.provider.DatabaseMetaData.RefMovActTMD;
import com.numicago.guiahollywood.provider.DatabaseMetaData.RefMovieDirectorTMD;
import com.numicago.guiahollywood.provider.DatabaseProvider.DatabaseHelper;

public abstract class DBUtils {

	private static final Uri MOVIE_URI = MovieTMD.CONTENT_URI;

	private static final Uri DAY_URI = DayTMD.CONTENT_URI;

	private static final Uri ACTOR_URI = ActorTMD.CONTENT_URI;
	
	private static final Uri REF_ACTOR_MOVIE_URI = RefMovActTMD.CONTENT_URI;
	
	private static final Uri DIRECTOR_URI = DirectorTMD.CONTENT_URI;
	
	private static final Uri REF_DIRECTOR_MOVIE_URI = RefMovieDirectorTMD.CONTENT_URI;

	private static String MOVIE_TABLE = MovieTMD.TABLE_NAME;

	private static String DAY_TABLE = DayTMD.TABLE_NAME;

	/**
	 * Update the structure of the database, in case the version changed.
	 */
	private static void updateDBStructure() {
		if (GuiaHollywoodActivity.databaseVersion != null) {
			//		if (DatabaseMetaData.DATABASE_LATEST_VERSION > 0) {
			if (DatabaseMetaData.DATABASE_LATEST_VERSION > GuiaHollywoodActivity.databaseVersion) {
				SQLiteDatabase db = DatabaseProvider.mOpenHelper.getWritableDatabase();
				DatabaseProvider.mOpenHelper.onUpgrade(db, GuiaHollywoodActivity.databaseVersion,
						DatabaseMetaData.DATABASE_LATEST_VERSION);
				db.close();
				GuiaHollywoodActivity.userPreferences.setdBVersion(DatabaseMetaData.DATABASE_LATEST_VERSION);
				GuiaHollywoodActivity.databaseVersion = DatabaseMetaData.DATABASE_LATEST_VERSION;
			}
		}
	}

	public static void addMoviesOfDay(Activity context, List<DayMovie> movieDayList, 
			NumiCal calendar) {

		updateDBStructure();

		for (DayMovie dayMovie : movieDayList) {
			Movie movie = dayMovie.getMovie();
			Day day = dayMovie.getDay();

			//where localName = movie.getLocalName();
			String whereMovieName = MovieTMD.LOCAL_NAME + " = " + "\"" + movie.getLocalName() + "\"";
			//Get the movie with the given name
			Cursor c = context.managedQuery(MOVIE_URI, null, whereMovieName, null, null);
			//if movie exists
			boolean movieExists = c.getCount() > 0 ? true : false;
			
			c.close();
			if (!movieExists) { //no movie, add it.
				//add new day to the day table.
				ContentResolver cr = context.getContentResolver();
				ContentValues cv = fillContentValuesWithMovie(movie);
				cv.put(MovieTMD.COUNT, 1); //at the beginning it has one
				cr.insert(MOVIE_URI, cv);

			} else {
				Cursor c1 = context.managedQuery(MOVIE_URI, new String[] {MovieTMD.COUNT}, whereMovieName, null, null);
				if(c1.moveToFirst()) {
					ContentResolver cr = context.getContentResolver();
					ContentValues cv = new ContentValues();
					int count = c1.getInt(c1.getColumnIndex(MovieTMD.COUNT));
					cv.put(MovieTMD.COUNT, (count + 1));
					cr.update(MOVIE_URI, cv, whereMovieName, null);
				}
			}

			Cursor cId = context.managedQuery(MOVIE_URI, null, whereMovieName, null, null);
			int movieId = cId.moveToFirst() ? cId.getInt(cId.getColumnIndex(MovieTMD._ID)) : 0;
			cId.close();
			//add new day to the day table.
			ContentResolver cr = context.getContentResolver();
			ContentValues cv = fillContentValuesWithDay(day);
			cv.put(DayTMD.MOVIE_ID, movieId);
			cr.insert(DAY_URI, cv);

		}
	}
	
	public static void updateMovieSmallImageUrl(Activity context, Movie movie) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(MovieTMD.SMALL_IMAGE_URL, movie.getImageSmallUrl());
		cr.update(MOVIE_URI, cv, MovieTMD.ORIGINAL_NAME + " = \"" + movie.getOriginalName() + "\"", null);		
	}
	
	public static void updateMovie(Activity context, Movie movie) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(MovieTMD.BIG_IMAGE_URL, movie.getImageBigUrl());
		cv.put(MovieTMD.DESCRIPTION, movie.getDescription());
		cv.put(MovieTMD.DIRECTOR, movie.getDirector());
		cv.put(MovieTMD.YEAR, movie.getYear());
		cr.update(MOVIE_URI, cv, MovieTMD.ORIGINAL_NAME + " = \"" + movie.getOriginalName() + "\"", null);
	}
	
	/**
	 * Lê a db à procura dos filmes do dia. Este método também verifica se os filmes 
	 * presentes na DB são do próprio dia. Caso não o sejam, limpa-os. 
	 * @return 0 -> a DB não contém filmes<br>
	 * != 0 -> a DB contém filmes do dia (e foi preenchido a ListaFilmesDaDb <br>
	 * 
	 */
	public static final List<DayMovie> getMoviesOfDay(Context context, NumiCal calendar, int channel) {

		NumiCal today = new NumiCal(calendar);
		today.resetTime(6, 0, 0, 0);
		long today6AM = today.getTimeInMillis();
		today.addDays(1);
		today.resetTime(5, 59, 0, 0);
		long tomorrow559 = today.getTimeInMillis();

		String query = " Select * from " + MOVIE_TABLE + 
				" INNER JOIN " + DAY_TABLE + 
				" ON " + DAY_TABLE + "." + DayTMD.MOVIE_ID + " = " + MOVIE_TABLE + "." + MovieTMD._ID + 
				" WHERE " + MovieTMD.CHANNEL + " = " + channel +  
				" AND " + DAY_TABLE + "." + DayTMD.DATE_START + " > " + today6AM + 
				" AND " + DAY_TABLE + "." + DayTMD.DATE_START + " < " + tomorrow559;
		
		List<DayMovie> movieDayList = new ArrayList<DayMovie>();

		updateDBStructure();
		Cursor c;
		if (DatabaseProvider.mOpenHelper == null) {
			DatabaseHelper mOpenHelper = new DatabaseHelper(context);
			SQLiteDatabase db = mOpenHelper.getReadableDatabase();
			c = db.rawQuery(query, null);
		} else {
			c = DatabaseProvider.mOpenHelper.getWritableDatabase().rawQuery(query, null);			
		}
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			movieDayList.add(readDayMovieFromCursor(c));
		}		
		c.close();

		return movieDayList;
	}

	public static List<Movie> getAllMovies(Activity context, String sortOrder, boolean asc, int channelId) {
		String ascDesc = asc ? "ASC" : "DESC";
		String [] sortItems = new String[7];
		sortItems[0] = MovieTMD._ID + " " + ascDesc;
		sortItems[1] = MovieTMD.COUNT + " " + ascDesc + ", " + MovieTMD.LOCAL_NAME + " ASC";
		sortItems[2] = MovieTMD.LOCAL_NAME + " " + ascDesc;
		sortItems[3] = MovieTMD.ORIGINAL_NAME + " " + ascDesc;
		sortItems[4] = MovieTMD.YEAR + " " + ascDesc;
		sortItems[5] = MovieTMD.GENRE + " " + ascDesc;
		sortItems[6] = sortItems[0];

		String [] sortStrings = context.getResources().getStringArray(com.numicago.guiahollywood.R.array.ordenacao);

		int index = 0;
		for (int i = 0; i < sortStrings.length; i++) {
			if (sortStrings[i].equals(sortOrder)) {
				index = i;
				break;
			}
		}
		Cursor c; 
		//sort by "last first" added day on the db. (most recent movies)
		if (sortOrder.equals(context.getString(com.numicago.guiahollywood.R.string.lastDayDB))) {

			/*SELECT *, X.funct FROM movie INNER JOIN 
 			(SELECT movieId, MIN(dateStart) as funct FROM day GROUP BY movieId) as X
 			ON movie.movieId = _id ORDER BY funct DESC
			 */

			String query = "SELECT *, X.funct FROM " + MOVIE_TABLE +
					" INNER JOIN " + 
					" (SELECT " + DayTMD.MOVIE_ID + ", MIN(" + DayTMD.DATE_START + ") as funct FROM " + DAY_TABLE + 
					" GROUP BY " + DayTMD.MOVIE_ID + ") as X" +
					" ON " + "X." + DayTMD.MOVIE_ID + " = " + MovieTMD._ID + 
					" AND " + MovieTMD.CHANNEL + " = " + channelId +
					" ORDER BY funct " + ascDesc;
			c = DatabaseProvider.mOpenHelper.getReadableDatabase().rawQuery(query, null);
		} else {
			c = context.managedQuery(MOVIE_URI, null, MovieTMD.CHANNEL + " = " + channelId, null, sortItems[index]);
		}
		
		List<Movie> movielist = new ArrayList<Movie>();
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			movielist.add(readMovieFromCursor(c));
		}
		c.close();
		return movielist;
	}
	
	public static void addNewActor(Activity context, Actor actor) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = fillContentValuesWithActor(actor);
		cr.insert(ACTOR_URI, cv);
	}
	
	public static void updateActor(Activity context, Actor actor) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = fillContentValuesWithActor(actor);
		cr.update(ACTOR_URI, cv, ActorTMD._ID + " = " + actor.getId(), null);
	}
	
	public static List<Actor> getAllActors(Activity context, 
			int actorsortId) {
		String ascDesc = true ? "ASC" : "DESC";
		String [] sortItems = new String[7];
		sortItems[0] = ActorTMD.ACTOR_NAME + " " + ascDesc;
		sortItems[1] = ActorTMD.ACTOR_BDAY + " " + ascDesc;

		Cursor c = context.managedQuery(ACTOR_URI, null, null, null, sortItems[actorsortId]);
		List<Actor> actorList = new ArrayList<Actor>();
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			actorList.add(readActorFromCursor(c));
		}
		return actorList;
	}
	
	public static void clearRefActorMovieTable(Activity context) {
		context.getContentResolver().delete(REF_ACTOR_MOVIE_URI, null, null);
	}
	
	public static void addActorToMovie(Activity context, int actorId, int movieId) {
		//add actor/movie ids to reference table
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(RefMovActTMD.REF_MA_ACTOR_ID, actorId);
		cv.put(RefMovActTMD.REF_MA_MOVIE_ID, movieId);
		cr.insert(REF_ACTOR_MOVIE_URI, cv);
	}
	
	public static void updateMovieActors(Activity context, List<Integer> actorsIds , int movieId) {
		context.getContentResolver().delete(REF_ACTOR_MOVIE_URI, 
				RefMovActTMD.REF_MA_MOVIE_ID + " = " + movieId, null);
		for (Integer actorId : actorsIds) {
			addActorToMovie(context, actorId, movieId);
		}
	}
	
	public static void getAllActorRef(Activity context) {
		Cursor c = context.managedQuery(REF_ACTOR_MOVIE_URI, null, null, null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			
		}
		c.close();
		return;
	}
	
	public static Actor getActorFullInfo(Activity context, int actorId) {
		Cursor c = context.managedQuery(ACTOR_URI, null, ActorTMD.TABLE_NAME + "." + ActorTMD._ID 
				+ " = " + actorId, null, null);
		c.moveToFirst();
		return readActorFromCursor(c);
	}
	
	public static List<Movie> getActorMovies(Activity context, int actorId) {
		String query = "SELECT * from " + MovieTMD.TABLE_NAME 
				+ " WHERE " + MovieTMD._ID + " IN " + "(SELECT " + RefMovActTMD.TABLE_NAME 
				+ "." + RefMovActTMD.REF_MA_MOVIE_ID + " FROM " + RefMovActTMD.TABLE_NAME 
				+ " WHERE " + RefMovActTMD.TABLE_NAME + "." + 
				RefMovActTMD.REF_MA_ACTOR_ID + " = " + actorId + ");";
		Cursor c = DatabaseProvider.mOpenHelper.getReadableDatabase().rawQuery(query, null);
		List<Movie> movies = new ArrayList<Movie>();
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			movies.add(readMovieFromCursor(c));
		}
		return movies;
	}
	
	
	public static List<Actor> getMovieActors(Activity context, int movieId) {
		String query = "Select * from " + ActorTMD.TABLE_NAME + 
				" WHERE " + ActorTMD._ID + 
				" in (SELECT " + RefMovActTMD.REF_MA_ACTOR_ID + " FROM " + RefMovActTMD.TABLE_NAME 
				+ " WHERE " + RefMovActTMD.REF_MA_MOVIE_ID + " = " + movieId + ")";
		Cursor c = DatabaseProvider.mOpenHelper.getReadableDatabase().rawQuery(query, null);
		
		List<Actor> movieActors = new ArrayList<Actor>();
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			movieActors.add(readActorFromCursor(c));
		}
		c.close();		
		return movieActors;
	}
	
	public static void updateMovieRating(Activity context, String movieNomePT, float rating) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(MovieTMD.RATING, rating);
		cr.update(MOVIE_URI, cv, MovieTMD.LOCAL_NAME + " = \"" + movieNomePT + "\"", null);
	}
	
	/**
	 * Gets the month premieres.
	 *
	 * @param context the context
	 * @param calendar the calendar
	 * @param channelId the channel id
	 * @param ascDesc the asc desc
	 * @return the month premieres
	 */
	public static List<Movie> getMonthPremieres(Activity context, 
			NumiCal calendar, int channelId, boolean asc) {
		String ascDesc = asc ? "ASC" : "DESC";
		NumiCal calStart = new NumiCal(calendar);
		calStart.setDayOfMonth(1);
		calStart.resetTime(0, 0, 0, 0);
		NumiCal calEnd = new NumiCal(calStart);
		calEnd.addMonths(1);
		
		String query = "SELECT *, X.funct FROM " + MOVIE_TABLE +
				" INNER JOIN " + 
				" (SELECT " + DayTMD.MOVIE_ID + ", MIN(" + DayTMD.DATE_START + ") as funct FROM " + DAY_TABLE + 
				" GROUP BY " + DayTMD.MOVIE_ID + ") as X" +
				" ON " + "X." + DayTMD.MOVIE_ID + " = " + MovieTMD._ID + 
				" AND funct < " + calEnd.getTimeInMillis() + " AND funct >" + calStart.getTimeInMillis() +  
				" AND " + MovieTMD.CHANNEL + " = " + channelId +
				" ORDER BY funct " + ascDesc;
		
		List<Movie> listPremieres = new ArrayList<Movie>();
		Cursor c = DatabaseProvider.mOpenHelper.getReadableDatabase().rawQuery(query, null);
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			listPremieres.add(readMovieFromCursor(c));
		}
		c.close();		
		return listPremieres;
	}
	
	public static List<Movie> getFavouriteMovies(Activity context, int channel) {
		String query = MovieTMD.FAVOURITE + " = " + 1;
		Cursor c = context.managedQuery(MOVIE_URI, null, query, null, MovieTMD.LOCAL_NAME);
		List<Movie> listFavs = new ArrayList<Movie>();
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			listFavs.add(readMovieFromCursor(c));
		}
		c.close();		
		return listFavs;
	}
	
	public static List<Movie> getViewedMovies(Activity context, int channel) {
		String query = MovieTMD.VIEWED + " = " + 1;
		Cursor c = context.managedQuery(MOVIE_URI, null, query, null, MovieTMD.LOCAL_NAME);
		List<Movie> listViewed = new ArrayList<Movie>();
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			listViewed.add(readMovieFromCursor(c));
		}
		c.close();		
		return listViewed;
	}
	
	public static List<Movie> getWatchlistMovies(Activity context, int channel) {
		String query = MovieTMD.WHATCH_LIST + " = " + 1;
		Cursor c = context.managedQuery(MOVIE_URI, null, query, null, MovieTMD.LOCAL_NAME);
		List<Movie> watchlistMovies = new ArrayList<Movie>();
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			watchlistMovies.add(readMovieFromCursor(c));
		}
		c.close();		
		return watchlistMovies;
	}
	
	
	public static List<Movie> getRatingMovies(Activity context, int channel) {
		String query = MovieTMD.RATING + " > " + 0;
		Cursor c = context.managedQuery(MOVIE_URI, null, query, null, MovieTMD.LOCAL_NAME);
		List<Movie> ratingListMovies = new ArrayList<Movie>();
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ratingListMovies.add(readMovieFromCursor(c));
		}
		c.close();		
		return ratingListMovies;
	}
	
	
	
	public static boolean moviesOfDayExist(Activity context, NumiCal calendar, int channelId) {
		NumiCal tempCal = new NumiCal(calendar);
		if (tempCal.getHourOfDay() > 0 && tempCal.getHourOfDay() <= 6) {
			tempCal.addDays(-1);
		}
		tempCal.resetTime(6, 0, 0, 0);
		long today6AM = tempCal.getTimeInMillis();
		tempCal.addDays(1);
		tempCal.resetTime(5, 59, 0, 0);
		long tomorrow559 = tempCal.getTimeInMillis();

		String query = "SELECT * FROM " + DAY_TABLE + " JOIN " + MOVIE_TABLE +
				" ON " + MOVIE_TABLE + "." + MovieTMD._ID + " = " + DAY_TABLE + "." + DayTMD.MOVIE_ID +
				" AND " + MOVIE_TABLE + "." + MovieTMD.CHANNEL + " = " + channelId + 
				" AND " + DayTMD.DATE_START + " > " + today6AM + " AND "+  DayTMD.DATE_END + " < " + tomorrow559;
		Cursor c = DatabaseProvider.mOpenHelper.getReadableDatabase().rawQuery(query, null);

		//		String whereDay = DayTMD.DATE_START + " > " + today6AM + " AND "+  DayTMD.DATE_END + " < " + tomorrow559;
		//		Cursor c = context.managedQuery(DAY_URI, null, whereDay, null, null);
		boolean exist = c.getCount() > 2;
		c.close();
		return exist;
	}

	public static void clearRepeatedDays(Activity context, Movie movie) {
		List<Day> movieDays = getMovieDays(context, movie.getId());
		List<Day> listToRemove = new ArrayList<Day>();
		for (int i = 0; i < movieDays.size(); i++) {
			for (int j = i+1; j < movieDays.size(); j++) {
				if (movieDays.get(i).getStart().getTimeInMillis() == movieDays.get(j).getStart().getTimeInMillis()) {
					listToRemove.add(movieDays.get(j));
				}
			}
		}
		
		for (Day day : listToRemove) {
			removeDay(context, day.getId());
		}
	}
	
	private static void removeDay(Activity context, int dayID) {
		context.getContentResolver().delete(DAY_URI, DayTMD._ID + " = " + dayID, null);
	}
	
	/**
	 * Remover alarmes da base de dados que já tenham passado. Não faz sentido
	 * apresentar alarmes que já não existem.
	 */
	public static void removePastAlarms(Activity context) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(DayTMD.ALARM, 0);

		String where = DayTMD.DATE_START + " < " + new NumiCal().getTimeInMillis();
		cr.update(DAY_URI, cv, where , null);
	}

	public static void clearDB(Activity context) {
		ContentResolver cr = context.getContentResolver();
		cr.delete(MOVIE_URI, null, null);
		cr.delete(DAY_URI, null, null);
	}

	public static final List<Day> getMovieDays(Activity context, int movieId) {
		List<Day> dayList = new ArrayList<Day>();
		String whereMovieId = DayTMD.MOVIE_ID + " = " + movieId;
		Cursor c = context.managedQuery(DAY_URI, null, whereMovieId, null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			dayList.add(readDayFromCursor(c));
		}
		c.close();
		return dayList;
	}
	
	public static int getMinYear(Activity context, final int channelId) {
		String query = "SELECT MIN(" + MovieTMD.YEAR + ") FROM " + MOVIE_TABLE + 
				" WHERE " + MovieTMD.YEAR + " > " + 0 + " AND "
				+ MovieTMD.CHANNEL + " = " + channelId; 
		Cursor c = DatabaseProvider.mOpenHelper.getReadableDatabase().rawQuery(query, null);
		c.moveToFirst();
		return c.getInt(0);
	}

	public static int getYearMoviesCount(Activity context, final int year, final int channelId) {
		String where = MovieTMD.YEAR + " = " + year + " AND " + 
					MovieTMD.CHANNEL + " = " + channelId;
		Cursor c = context.managedQuery(MOVIE_URI, new String[]{MovieTMD.YEAR}, where, null, null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
	public static void addAlarmToDB(Activity context, int dayId) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(DayTMD.ALARM, 1);
		cr.update(DAY_URI, cv, DayTMD._ID + " = " + dayId, null);
	}

	public static void setFavourite(Activity context, String movieNomePT, boolean add) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(MovieTMD.FAVOURITE, add ? 1 : 0);
		cr.update(MOVIE_URI, cv, MovieTMD.LOCAL_NAME + " = \"" + movieNomePT + "\"", null);		
	}

	public static void setWatchList(Activity context, String movieNomePT, boolean add) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(MovieTMD.WHATCH_LIST, add ? 1 : 0);
		cr.update(MOVIE_URI, cv, MovieTMD.LOCAL_NAME + " = \"" + movieNomePT + "\"", null);		
	}

	/**
	 * Sets the viewed.
	 *
	 * @param context the context
	 * @param movieNomePT the movie nome pt
	 * @param add true if to set viewd, false if set to not viewed.
	 */
	public static void setViewed(Activity context, String movieNomePT, boolean add) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(MovieTMD.VIEWED, add ? 1 : 0);
		cr.update(MOVIE_URI, cv, MovieTMD.LOCAL_NAME + " = \"" + movieNomePT + "\"", null);		
	}
	
	public static int countViewed(Activity context) {
		String where = MovieTMD.VIEWED + " = " + 1;
		Cursor c = context.managedQuery(MOVIE_URI, new String[]{MovieTMD.VIEWED}, where, null, null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
	public static int countFavorites(Activity context) {
		String where = MovieTMD.FAVOURITE + " = " + 1;
		Cursor c = context.managedQuery(MOVIE_URI, new String[]{MovieTMD.FAVOURITE}, where, null, null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
	public static int countWatchList(Activity context) {
		String where = MovieTMD.WHATCH_LIST + " = " + 1;
		Cursor c = context.managedQuery(MOVIE_URI, new String[]{MovieTMD.WHATCH_LIST}, where, null, null);
		int count = c.getCount();
		c.close();
		return count;
	}

	public static int countRatingList(Activity context) {
		String where = MovieTMD.RATING + " > " + 0;
		Cursor c = context.managedQuery(MOVIE_URI, new String[]{MovieTMD.RATING}, where, null, null);
		int count = c.getCount();
		c.close();
		return count;		
	}
	
	public static List<DayMovie> getAllAlarms(Activity context) {

		updateDBStructure();
		removePastAlarms(context);

		List<DayMovie> alarmlist = new ArrayList<DayMovie>();
		String query = " Select * from " + MOVIE_TABLE + 
				" INNER JOIN " + DAY_TABLE + 
				" ON " + DAY_TABLE + "." + DayTMD.MOVIE_ID + " = " + MOVIE_TABLE + "." + MovieTMD._ID + 
				" WHERE " + DAY_TABLE + "." + DayTMD.ALARM + " > " + 0;

		Cursor c = DatabaseProvider.mOpenHelper.getReadableDatabase().rawQuery(query, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			alarmlist.add(readDayMovieFromCursor(c));
		}
		c.close();
		return alarmlist;
	}

	public static String getOnAirMovieString(Activity context, int channelId) {
		long now = new NumiCal().getTimeInMillis();
		String queryDay = DayTMD.DATE_START + " < " + now + " AND " + DayTMD.DATE_END + " > " + now;

		Cursor c = context.managedQuery(DAY_URI, new String[] {DayTMD.MOVIE_ID, DayTMD.DATE_START, DayTMD.DATE_END}, queryDay, null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			String queryMovie = MovieTMD._ID + " = " + c.getInt(c.getColumnIndex(DayTMD.MOVIE_ID)) 
					+ " AND " + MovieTMD.CHANNEL + " = " + channelId;
			Cursor cMovie = context.managedQuery(MOVIE_URI, null, queryMovie, null, null);
			cMovie.moveToFirst();
			if (cMovie.getCount() != 0) {
				String name = cMovie.getString(cMovie.getColumnIndex(MovieTMD.LOCAL_NAME));
				String start = new NumiCal(c.getLong(c.getColumnIndex(DayTMD.DATE_START))).toString("'das' " + "HH:mm");
				String end = new NumiCal(c.getLong(c.getColumnIndex(DayTMD.DATE_END))).toString(" 'às' " + "HH:mm");
				c.close();
				cMovie.close();
				return "\"" + name + " " + start + end;
			}
		} 
		c.close();
		return "no movie now";
	}

	private static ContentValues fillContentValuesWithMovie(Movie movie) {

		ContentValues cv = new ContentValues();

		cv.put(MovieTMD.LOCAL_NAME, movie.getLocalName());
		cv.put(MovieTMD.ORIGINAL_NAME, movie.getOriginalName());
		cv.put(MovieTMD.YEAR, movie.getYear());
		cv.put(MovieTMD.GENRE, movie.getGenre());
		cv.put(MovieTMD.DIRECTOR, movie.getDirector());
		cv.put(MovieTMD.DURATION, movie.getLength());
		cv.put(MovieTMD.DESCRIPTION, movie.getDescription());
		cv.put(MovieTMD.FAVOURITE, movie.isFavourite());
		cv.put(MovieTMD.WHATCH_LIST, movie.isWatchList());
		cv.put(MovieTMD.VIEWED, movie.isViewed());
		cv.put(MovieTMD.RATING, movie.getRating());
		cv.put(MovieTMD.BIG_IMAGE_URL, movie.getImageBigUrl());
		cv.put(MovieTMD.SMALL_IMAGE_URL, movie.getImageSmallUrl());
		cv.put(MovieTMD.HOLLYWOOD_URL, movie.getHollywoodUrl());
		cv.put(MovieTMD.CHANNEL, movie.getChannelId());

		return cv; 
	}
	
	private static ContentValues fillContentValuesWithActor(Actor actor) {
		
		ContentValues cv = new ContentValues();
		
		cv.put(ActorTMD.ACTOR_NAME, actor.getName());
		cv.put(ActorTMD.ACTOR_BDAY, actor.getBday().getTime());
		cv.put(ActorTMD.ACTOR_SMALL_IMAGE_BLOB, actor.getSmallImageBlob());
		cv.put(ActorTMD.ACTOR_BIG_IMAGE_BLOB, actor.getBigImageBlob());
		cv.put(ActorTMD.ACTOR_SMALL_IMAGE_URL, actor.getSmalImageUrl());
		cv.put(ActorTMD.ACTOR_BIG_IMAGE_URL, actor.getBigImageUrl());
		cv.put(ActorTMD.ACTOR_DESCRIPTION, actor.getDescription());
		cv.put(ActorTMD.ACTOR_FAVOURITE, actor.isFavourite());
		
		return cv; 
	}

	private static ContentValues fillContentValuesWithDay(Day day) {

		ContentValues cv = new ContentValues();

		cv.put(DayTMD.MOVIE_ID, day.getMovieId());
		cv.put(DayTMD.DATE_START, day.getStart().getTimeInMillis());
		cv.put(DayTMD.DATE_END, day.getEnd().getTimeInMillis());
		cv.put(DayTMD.ALARM, day.hasAlarm());
		return cv; 
	}

	public static DayMovie readDayMovieFromCursor(Cursor c) {
		Day day = readDayFromCursor(c);
		Movie movie = readMovieFromCursor(c);

		DayMovie dayMovie = new DayMovie();
		dayMovie.setMovie(movie);
		dayMovie.setDay(day);
		return dayMovie;
	}

	public static Movie readMovieFromCursor(Cursor c) {
		Movie movie = new Movie();
		//For some reason, if here I set MovieTMD._ID the query gets odd ids.
		try {
			movie.setId(c.getInt(c.getColumnIndex(DayTMD.MOVIE_ID)));
		} catch (Exception e) {
			movie.setId(c.getInt(c.getColumnIndex(MovieTMD._ID)));			
		}
		movie.setOriginalName(c.getString(c.getColumnIndex(MovieTMD.ORIGINAL_NAME)));
		movie.setLocalName(c.getString(c.getColumnIndex(MovieTMD.LOCAL_NAME)));
		movie.setImagemSmallUrl(c.getString(c.getColumnIndex(MovieTMD.SMALL_IMAGE_URL)));
		movie.setDescription(c.getString(c.getColumnIndex(MovieTMD.DESCRIPTION)));
		movie.setHollywoodUrl(c.getString(c.getColumnIndex(MovieTMD.HOLLYWOOD_URL)));
		movie.setChannelId(c.getInt(c.getColumnIndex(MovieTMD.CHANNEL)));
		movie.setYear(c.getInt(c.getColumnIndex(MovieTMD.YEAR)));
		movie.setGenre(c.getString(c.getColumnIndex(MovieTMD.GENRE)));
		movie.setDirector(c.getString(c.getColumnIndex(MovieTMD.DIRECTOR)));
		movie.setFavourite(c.getInt(c.getColumnIndex(MovieTMD.FAVOURITE)) == 1 ? true : false);
		movie.setWatchList(c.getInt(c.getColumnIndex(MovieTMD.WHATCH_LIST)) == 1 ? true : false);
		movie.setViewed(c.getInt(c.getColumnIndex(MovieTMD.VIEWED)) == 1 ? true : false);
		movie.setRating(c.getFloat(c.getColumnIndex(MovieTMD.RATING)));
		movie.setImageBigUrl(c.getString(c.getColumnIndex(MovieTMD.BIG_IMAGE_URL)));
		movie.setChannelId(c.getInt(c.getColumnIndex(MovieTMD.CHANNEL)));
		movie.setCount(c.getInt(c.getColumnIndex(MovieTMD.COUNT)));
		
		return movie;
	}

	public static Actor readActorFromCursor(Cursor c) {
		Actor actor = new Actor();
		actor.setId(c.getInt(c.getColumnIndex(ActorTMD._ID)));
		actor.setName(c.getString(c.getColumnIndex(ActorTMD.ACTOR_NAME)));
		actor.setBday(new Date(c.getLong(c.getColumnIndex(ActorTMD.ACTOR_BDAY))));
		actor.setFavourite(c.getInt(c.getColumnIndex(ActorTMD.ACTOR_FAVOURITE)) > 0 ? true : false);
		actor.setSmallImageBlob(c.getBlob(c.getColumnIndex(ActorTMD.ACTOR_SMALL_IMAGE_BLOB)));
		actor.setBigImageUrl(c.getString(c.getColumnIndex(ActorTMD.ACTOR_BIG_IMAGE_URL)));
		return actor;
	}
	
	public static Day readDayFromCursor(Cursor c) {
		Day day = new Day();

		day.setId(c.getInt(c.getColumnIndex(DayTMD._ID)));
		day.setMovieId(c.getInt(c.getColumnIndex(DayTMD.MOVIE_ID)));
		day.setStart(new NumiCal(c.getLong(c.getColumnIndex(DayTMD.DATE_START))));
		day.setEnd(new NumiCal(c.getLong(c.getColumnIndex(DayTMD.DATE_END))));
		day.setHasAlarm(c.getInt(c.getColumnIndex(DayTMD.ALARM)) == 1 ? true : false);

		return day;
	}
}
