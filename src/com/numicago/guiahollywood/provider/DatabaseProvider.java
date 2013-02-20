package com.numicago.guiahollywood.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.numicago.guiahollywood.activities.GuiaHollywoodActivity;
import com.numicago.guiahollywood.provider.DatabaseMetaData.ActorTMD;
import com.numicago.guiahollywood.provider.DatabaseMetaData.DayTMD;
import com.numicago.guiahollywood.provider.DatabaseMetaData.DirectorTMD;
import com.numicago.guiahollywood.provider.DatabaseMetaData.MovieTMD;
import com.numicago.guiahollywood.provider.DatabaseMetaData.RefMovActTMD;
import com.numicago.guiahollywood.provider.DatabaseMetaData.RefMovieDirectorTMD;

public class DatabaseProvider extends ContentProvider {
	private static final String TAG = "DATABASE PROVIDER";

	public DatabaseProvider() {
		super();
	}
	
	
	public static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DatabaseMetaData.DATABASE_NAME, null,
					DatabaseMetaData.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "inner oncreate called");

			createMovieTable(db);
			createDayTable(db);
			createActorAndRefTable(db);
			createDirectorAndRefTable(db);
		}
		
		private void createMovieTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + MovieTMD.TABLE_NAME + " ("
					+ MovieTMD._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
					+ MovieTMD.LOCAL_NAME + " TEXT," 
					+ MovieTMD.ORIGINAL_NAME + " TEXT," 
					+ MovieTMD.YEAR + " INTEGER," 
					+ MovieTMD.GENRE + " TEXT," 
					+ MovieTMD.DIRECTOR + " TEXT,"
					+ MovieTMD.DESCRIPTION + " TEXT,"
					+ MovieTMD.DURATION + " TEXT," 
					+ MovieTMD.FAVOURITE + " BOOLEAN," 
					+ MovieTMD.VIEWED + " BOOLEAN," 
					+ MovieTMD.WHATCH_LIST + " BOOLEAN,"
					+ MovieTMD.BIG_IMAGE_URL + " TEXT,"
					+ MovieTMD.SMALL_IMAGE_URL + " TEXT,"
					+ MovieTMD.BIG_IMAGE_BLOB + " blob,"
					+ MovieTMD.SMALL_IMAGE_BLOB + " blob,"
					+ MovieTMD.RATING + " real,"
					+ MovieTMD.HOLLYWOOD_URL + " TEXT," 
					+ MovieTMD.CHANNEL + " BOOLEAN," 
					+ MovieTMD.COUNT + " INTEGER" + ");");
		}
		
		private void createDayTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DayTMD.TABLE_NAME + " (" 
					+ DayTMD._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," 
					+ DayTMD.MOVIE_ID + " INTEGER, "
					+ DayTMD.DATE_START + " INTEGER, " 
					+ DayTMD.DATE_END + " INTEGER, " 
					+ DayTMD.ALARM + " BOOLEAN" + ");");
		}
		
		private void createActorAndRefTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + ActorTMD.TABLE_NAME + " (" 
					+ ActorTMD._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," 
					+ ActorTMD.ACTOR_NAME + " TEXT, "
					+ ActorTMD.ACTOR_BDAY + " INTEGER, " 
					+ ActorTMD.ACTOR_SMALL_IMAGE_URL + " TEXT, "
					+ ActorTMD.ACTOR_SMALL_IMAGE_BLOB + " blob, "
					+ ActorTMD.ACTOR_BIG_IMAGE_URL + " TEXT, "
					+ ActorTMD.ACTOR_BIG_IMAGE_BLOB + " blob, "
					+ ActorTMD.ACTOR_IMDB_URL + " TEXT, "
					+ ActorTMD.ACTOR_DESCRIPTION + " TEXT, " 
					+ ActorTMD.ACTOR_FAVOURITE + " BOOLEAN" + ");");
			
			db.execSQL("CREATE TABLE " + RefMovActTMD.TABLE_NAME + " (" 
					+ RefMovActTMD._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," 
					+ RefMovActTMD.REF_MA_MOVIE_ID  + " INTEGER, "
					+ RefMovActTMD.REF_MA_ACTOR_ID + " INTEGER" + ");");
		}
		
		private void createDirectorAndRefTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DirectorTMD.TABLE_NAME + " (" 
					+ DirectorTMD._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," 
					+ DirectorTMD.DIRECTOR_NAME + " TEXT, "
					+ DirectorTMD.DIRECTOR_BDAY + " INTEGER, " 
					+ DirectorTMD.DIRECTOR_SMALL_IMAGE_URL + " TEXT, "
					+ DirectorTMD.DIRECTOR_SMALL_IMAGE_BLOB + " blob, "
					+ DirectorTMD.DIRECTOR_BIG_IMAGE_BLOB + " blob, "
					+ DirectorTMD.DIRECTOR_BIG_IMAGE_URL + " TEXT, "
					+ DirectorTMD.DIRECTOR_DESCRIPTION + " TEXT, " 
					+ DirectorTMD.DIRECTOR_FAVOURITE + " BOOLEAN" + ");");

			db.execSQL("CREATE TABLE " + RefMovieDirectorTMD.TABLE_NAME + " (" 
					+ RefMovieDirectorTMD._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," 
					+ RefMovieDirectorTMD.REF_MD_MOVIE_ID  + " INTEGER, "
					+ RefMovieDirectorTMD.REF_MD_DIRECTOR_ID + " INTEGER" + ");");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "inner onupgrade called");
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");

			//from 5 to sixth version a new collumn was added.
			if (newVersion == 7) {
				if (oldVersion == 5) {
					db.execSQL("ALTER TABLE " + MovieTMD.TABLE_NAME + " ADD " 
							+ MovieTMD.RATING + " REAL");
					createActorAndRefTable(db);
					createDirectorAndRefTable(db);
				} else if (oldVersion == 6) {
					db.execSQL("DROP TABLE IF EXISTS " + ActorTMD.TABLE_NAME);
					db.execSQL("DROP TABLE IF EXISTS " + DirectorTMD.TABLE_NAME);
					db.execSQL("DROP TABLE IF EXISTS " + RefMovActTMD.TABLE_NAME);
					db.execSQL("DROP TABLE IF EXISTS " + RefMovieDirectorTMD.TABLE_NAME);
					createActorAndRefTable(db);
					createDirectorAndRefTable(db);
				}
			} else {
				db.execSQL("DROP TABLE IF EXISTS " + MovieTMD.TABLE_NAME);
				db.execSQL("DROP TABLE IF EXISTS " + DayTMD.TABLE_NAME);
				db.execSQL("DROP TABLE IF EXISTS " + ActorTMD.TABLE_NAME);
				db.execSQL("DROP TABLE IF EXISTS " + RefMovActTMD.TABLE_NAME);
				db.execSQL("DROP TABLE IF EXISTS " + DirectorTMD.TABLE_NAME);
				db.execSQL("DROP TABLE IF EXISTS " + RefMovieDirectorTMD.TABLE_NAME);
				onCreate(db);
			}
		}
	}

	private static HashMap<String, String> sMovieProjectionMap;
	static {
		sMovieProjectionMap = new HashMap<String, String>();
		sMovieProjectionMap.put(MovieTMD._ID, MovieTMD._ID);
		sMovieProjectionMap.put(MovieTMD.LOCAL_NAME, MovieTMD.LOCAL_NAME);
		sMovieProjectionMap.put(MovieTMD.ORIGINAL_NAME, MovieTMD.ORIGINAL_NAME);
		sMovieProjectionMap.put(MovieTMD.YEAR, MovieTMD.YEAR);
		sMovieProjectionMap.put(MovieTMD.GENRE, MovieTMD.GENRE);
		sMovieProjectionMap.put(MovieTMD.DIRECTOR, MovieTMD.DIRECTOR);
		sMovieProjectionMap.put(MovieTMD.DURATION, MovieTMD.DURATION);
		sMovieProjectionMap.put(MovieTMD.DESCRIPTION, MovieTMD.DESCRIPTION);
		sMovieProjectionMap.put(MovieTMD.RATING, MovieTMD.RATING);
		sMovieProjectionMap.put(MovieTMD.COUNT, MovieTMD.COUNT);
		sMovieProjectionMap.put(MovieTMD.FAVOURITE, MovieTMD.FAVOURITE);
		sMovieProjectionMap.put(MovieTMD.VIEWED, MovieTMD.VIEWED);
		sMovieProjectionMap.put(MovieTMD.WHATCH_LIST, MovieTMD.WHATCH_LIST);
		sMovieProjectionMap.put(MovieTMD.BIG_IMAGE_URL, MovieTMD.BIG_IMAGE_URL);
		sMovieProjectionMap.put(MovieTMD.BIG_IMAGE_BLOB, MovieTMD.BIG_IMAGE_BLOB);
		sMovieProjectionMap.put(MovieTMD.SMALL_IMAGE_URL, MovieTMD.SMALL_IMAGE_URL);
		sMovieProjectionMap.put(MovieTMD.SMALL_IMAGE_BLOB, MovieTMD.SMALL_IMAGE_BLOB);
		sMovieProjectionMap.put(MovieTMD.HOLLYWOOD_URL, MovieTMD.HOLLYWOOD_URL);
		sMovieProjectionMap.put(MovieTMD.CHANNEL, MovieTMD.CHANNEL);
	}
	
	private static HashMap<String, String> sDayProjectionMap;
	static {
		sDayProjectionMap = new HashMap<String, String>();
		sDayProjectionMap.put(DayTMD._ID, DayTMD._ID);
		sDayProjectionMap.put(DayTMD.MOVIE_ID, DayTMD.MOVIE_ID);
		sDayProjectionMap.put(DayTMD.DATE_START, DayTMD.DATE_START);
		sDayProjectionMap.put(DayTMD.DATE_END, DayTMD.DATE_END);
		sDayProjectionMap.put(DayTMD.ALARM, DayTMD.ALARM);
	}
	
	private static HashMap<String, String> sActorProjectionMap;
	static {
		sActorProjectionMap = new HashMap<String, String>();
		sActorProjectionMap.put(ActorTMD._ID, ActorTMD._ID);
		sActorProjectionMap.put(ActorTMD.ACTOR_NAME, ActorTMD.ACTOR_NAME);
		sActorProjectionMap.put(ActorTMD.ACTOR_BDAY, ActorTMD.ACTOR_BDAY);
		sActorProjectionMap.put(ActorTMD.ACTOR_SMALL_IMAGE_URL, ActorTMD.ACTOR_SMALL_IMAGE_URL);
		sActorProjectionMap.put(ActorTMD.ACTOR_SMALL_IMAGE_URL, ActorTMD.ACTOR_SMALL_IMAGE_URL);
		sActorProjectionMap.put(ActorTMD.ACTOR_BIG_IMAGE_BLOB, ActorTMD.ACTOR_BIG_IMAGE_BLOB);
		sActorProjectionMap.put(ActorTMD.ACTOR_BIG_IMAGE_URL, ActorTMD.ACTOR_BIG_IMAGE_URL);
		sActorProjectionMap.put(ActorTMD.ACTOR_DESCRIPTION, ActorTMD.ACTOR_DESCRIPTION);
		sActorProjectionMap.put(ActorTMD.ACTOR_IMDB_URL, ActorTMD.ACTOR_IMDB_URL);
		sActorProjectionMap.put(ActorTMD.ACTOR_FAVOURITE, ActorTMD.ACTOR_FAVOURITE);
	}
	
	private static HashMap<String, String> sRefActorProjectionMap;
	static {
		sRefActorProjectionMap = new HashMap<String, String>();
		sRefActorProjectionMap.put(RefMovActTMD._ID, RefMovActTMD._ID);
		sRefActorProjectionMap.put(RefMovActTMD.REF_MA_MOVIE_ID, RefMovActTMD.REF_MA_MOVIE_ID);
		sRefActorProjectionMap.put(RefMovActTMD.REF_MA_ACTOR_ID, RefMovActTMD.REF_MA_ACTOR_ID);
	}
	
	private static HashMap<String, String> sRefDirectorProjectionMap;
	static {
		sRefDirectorProjectionMap = new HashMap<String, String>();
		sRefDirectorProjectionMap.put(RefMovieDirectorTMD._ID, RefMovieDirectorTMD._ID);
		sRefDirectorProjectionMap.put(RefMovieDirectorTMD.REF_MD_MOVIE_ID, RefMovieDirectorTMD.REF_MD_MOVIE_ID);
		sRefDirectorProjectionMap.put(RefMovieDirectorTMD.REF_MD_DIRECTOR_ID, RefMovieDirectorTMD.REF_MD_DIRECTOR_ID);
	}
	
	private static HashMap<String, String> sDirectorProjectionMap;
	static {
		sDirectorProjectionMap = new HashMap<String, String>();
		sDirectorProjectionMap.put(DirectorTMD._ID, DirectorTMD._ID);
		sDirectorProjectionMap.put(DirectorTMD.DIRECTOR_NAME, DirectorTMD.DIRECTOR_NAME);
		sDirectorProjectionMap.put(DirectorTMD.DIRECTOR_BDAY, DirectorTMD.DIRECTOR_BDAY);
		sActorProjectionMap.put(DirectorTMD.DIRECTOR_SMALL_IMAGE_URL, DirectorTMD.DIRECTOR_SMALL_IMAGE_URL);
		sActorProjectionMap.put(DirectorTMD.DIRECTOR_SMALL_IMAGE_BLOB, DirectorTMD.DIRECTOR_SMALL_IMAGE_BLOB);
		sActorProjectionMap.put(DirectorTMD.DIRECTOR_BIG_IMAGE_BLOB, DirectorTMD.DIRECTOR_BIG_IMAGE_BLOB);
		sActorProjectionMap.put(DirectorTMD.DIRECTOR_BIG_IMAGE_BLOB, DirectorTMD.DIRECTOR_BIG_IMAGE_BLOB);
		sDirectorProjectionMap.put(DirectorTMD.DIRECTOR_DESCRIPTION, DirectorTMD.DIRECTOR_DESCRIPTION);
		sDirectorProjectionMap.put(DirectorTMD.DIRECTOR_IMDB_URL, DirectorTMD.DIRECTOR_IMDB_URL);
		sDirectorProjectionMap.put(DirectorTMD.DIRECTOR_FAVOURITE, DirectorTMD.DIRECTOR_FAVOURITE);
	}

	// Provide a mechanism to identify
	// all the incoming uri patterns.
	private static final UriMatcher sUriMatcher;
	private static final int INCOMING_MOVIE_COLLECTION_URI_INDICATOR = 1;
	private static final int INCOMING_MOVIE_SINGLE_URI_INDICATOR = 2;
	private static final int INCOMING_DAY_COLLECTION_URI_INDICATOR = 3;
	private static final int INCOMING_DAY_SINGLE_URI_INDICATOR = 4;
	private static final int INCOMING_ACTOR_COLLECTION_URI_INDICATOR = 5;
	private static final int INCOMING_ACTOR_SINGLE_URI_INDICATOR = 6;
	private static final int INCOMING_REF_ACTOR_SINGLE_URI_INDICATOR = 7;
	private static final int INCOMING_REF_ACTOR_COLLECTION_URI_INDICATOR = 8;
	private static final int INCOMING_DIRECTOR_COLLECTION_URI_INDICATOR = 9;
	private static final int INCOMING_DIRECTOR_SINGLE_URI_INDICATOR = 10;
	private static final int INCOMING_REF_DIRECTOR_SINGLE_URI_INDICATOR = 11;
	private static final int INCOMING_REF_DIRECTOR_COLLECTION_URI_INDICATOR = 12;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(DatabaseMetaData.AUTHORITY, "movie",
				INCOMING_MOVIE_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(DatabaseMetaData.AUTHORITY, "movie/#",
				INCOMING_MOVIE_SINGLE_URI_INDICATOR);

		sUriMatcher.addURI(DatabaseMetaData.AUTHORITY, "day",
				INCOMING_DAY_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(DatabaseMetaData.AUTHORITY, "day/#",
				INCOMING_DAY_SINGLE_URI_INDICATOR);
		
		sUriMatcher.addURI(DatabaseMetaData.AUTHORITY, "actor",
				INCOMING_ACTOR_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(DatabaseMetaData.AUTHORITY, "actor/#",
				INCOMING_ACTOR_SINGLE_URI_INDICATOR);
		
		sUriMatcher.addURI(DatabaseMetaData.AUTHORITY, "actorMovieRef",
				INCOMING_REF_ACTOR_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(DatabaseMetaData.AUTHORITY, "actorMovieRef/#",
				INCOMING_REF_ACTOR_SINGLE_URI_INDICATOR);

		sUriMatcher.addURI(DatabaseMetaData.AUTHORITY, "director",
				INCOMING_DIRECTOR_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(DatabaseMetaData.AUTHORITY, "director/#",
				INCOMING_DIRECTOR_SINGLE_URI_INDICATOR);
		
		sUriMatcher.addURI(DatabaseMetaData.AUTHORITY, "directorMovieRef",
				INCOMING_REF_DIRECTOR_SINGLE_URI_INDICATOR);
	}

	public static DatabaseHelper mOpenHelper;

	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case INCOMING_MOVIE_COLLECTION_URI_INDICATOR:
			count = db.delete(MovieTMD.TABLE_NAME, where, whereArgs);
			break;

		case INCOMING_MOVIE_SINGLE_URI_INDICATOR:
			String filmeRowId = uri.getPathSegments().get(1);
			count = db.delete(
					MovieTMD.TABLE_NAME,
					MovieTMD._ID
							+ "="
							+ filmeRowId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case INCOMING_DAY_COLLECTION_URI_INDICATOR:
			count = db.delete(DayTMD.TABLE_NAME, where, whereArgs);
			break;

		case INCOMING_DAY_SINGLE_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);
			count = db.delete(DayTMD.TABLE_NAME,
					DayTMD._ID + " = " + rowId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
			
		case INCOMING_ACTOR_COLLECTION_URI_INDICATOR:
			count = db.delete(ActorTMD.TABLE_NAME, where, whereArgs);
			break;
			
		case INCOMING_ACTOR_SINGLE_URI_INDICATOR:
			String actorRowId = uri.getPathSegments().get(1);
			count = db.delete(ActorTMD.TABLE_NAME,
					ActorTMD._ID + " = " + actorRowId
					+ (!TextUtils.isEmpty(where) ? " AND (" + where
							+ ')' : ""), whereArgs);
			break;
			
		case INCOMING_DIRECTOR_COLLECTION_URI_INDICATOR:
			count = db.delete(DirectorTMD.TABLE_NAME, where, whereArgs);
			break;
			
		case INCOMING_DIRECTOR_SINGLE_URI_INDICATOR:
			String directorRowId = uri.getPathSegments().get(1);
			count = db.delete(DirectorTMD.TABLE_NAME,
					DirectorTMD._ID + " = " + directorRowId
					+ (!TextUtils.isEmpty(where) ? " AND (" + where
							+ ')' : ""), whereArgs);
			break;

		case INCOMING_REF_ACTOR_COLLECTION_URI_INDICATOR:
			count = db.delete(RefMovActTMD.TABLE_NAME, where, whereArgs);
			break;
			
		case INCOMING_REF_ACTOR_SINGLE_URI_INDICATOR:
			String refRowId = uri.getPathSegments().get(1);
			count = db.delete(RefMovActTMD.TABLE_NAME,
					RefMovActTMD._ID + " = " + refRowId
					+ (!TextUtils.isEmpty(where) ? " AND (" + where
							+ ')' : ""), whereArgs);
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case INCOMING_MOVIE_COLLECTION_URI_INDICATOR:
			return MovieTMD.CONTENT_TYPE;

		case INCOMING_MOVIE_SINGLE_URI_INDICATOR:
			return MovieTMD.CONTENT_ITEM_TYPE;

		case INCOMING_DAY_COLLECTION_URI_INDICATOR:
			return DayTMD.CONTENT_TYPE;

		case INCOMING_DAY_SINGLE_URI_INDICATOR:
			return DayTMD.CONTENT_ITEM_TYPE;
			
		case INCOMING_ACTOR_COLLECTION_URI_INDICATOR:
			return ActorTMD.CONTENT_TYPE;
			
		case INCOMING_ACTOR_SINGLE_URI_INDICATOR:
			return ActorTMD.CONTENT_ITEM_TYPE;
			
		case INCOMING_DIRECTOR_COLLECTION_URI_INDICATOR:
			return DirectorTMD.CONTENT_TYPE;
			
		case INCOMING_DIRECTOR_SINGLE_URI_INDICATOR:
			return DirectorTMD.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	public Uri insert(Uri uri, ContentValues initialValues) {
		// Validate the requested uri
		if (sUriMatcher.match(uri) != INCOMING_MOVIE_COLLECTION_URI_INDICATOR
				&& sUriMatcher.match(uri) != INCOMING_DAY_COLLECTION_URI_INDICATOR
				&& sUriMatcher.match(uri) != INCOMING_ACTOR_COLLECTION_URI_INDICATOR 
				&& sUriMatcher.match(uri) != INCOMING_REF_ACTOR_SINGLE_URI_INDICATOR 
				&& sUriMatcher.match(uri) != INCOMING_REF_ACTOR_COLLECTION_URI_INDICATOR 
				&& sUriMatcher.match(uri) != INCOMING_DIRECTOR_COLLECTION_URI_INDICATOR) {
			throw new IllegalArgumentException("Nuno Database provider exception: Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		// Long now = Long.valueOf(System.currentTimeMillis());

		if (sUriMatcher.match(uri) == INCOMING_MOVIE_COLLECTION_URI_INDICATOR) {
			if (!values.containsKey(MovieTMD.LOCAL_NAME)) {
				throw new SQLException(
						"Falha ao introduzir linha porque o nome local do filme é necessário"
								+ uri);
			}
			if (!values.containsKey(MovieTMD.ORIGINAL_NAME)) {
				throw new SQLException(
						"Falha ao introduzir linha porque o nome original do filme é necessário"
								+ uri);
			}

			if (!values.containsKey(MovieTMD.CHANNEL)) {
				throw new SQLException(
						"Falha ao introduzir linha porque o canal do filme é necessária"
								+ uri);
			}
			
			if (!values.containsKey(MovieTMD.HOLLYWOOD_URL)) {
				throw new SQLException("Falha ao introduzir linha porque o "
						+ "link do canal do filme é necessária" + uri);
			}
		}

		if (sUriMatcher.match(uri) == INCOMING_DAY_COLLECTION_URI_INDICATOR) {
			if (!values.containsKey(DayTMD.DATE_START)) {
				throw new SQLException(
						"Falha ao introduzir linha porque a data do alarme é necessária"
								+ uri);
			}
			if (!values.containsKey(DayTMD.DATE_END)) {
				throw new SQLException(
						"Falha ao introduzir linha porque a ID do filme é necessária"
								+ uri);
			}
			if (!values.containsKey(DayTMD.MOVIE_ID)) {
				throw new SQLException(
						"Falha ao introduzir linha porque a id do filme é necessário"
								+ uri);
			}
		}
		
		if (sUriMatcher.match(uri) == INCOMING_ACTOR_COLLECTION_URI_INDICATOR) {
			if (!values.containsKey(ActorTMD.ACTOR_NAME)) {
				throw new SQLException(
						"Falha ao introduzir linha porque a nome do actor é necessário"
								+ uri);
			}
		}
		
		if (sUriMatcher.match(uri) == INCOMING_REF_ACTOR_SINGLE_URI_INDICATOR) {
			if (!values.containsKey(RefMovActTMD.REF_MA_ACTOR_ID)) {
				throw new SQLException(
						"Falha ao introduzir linha porque a id do actor é necessário"
								+ uri);
			}
			if (!values.containsKey(RefMovActTMD.REF_MA_MOVIE_ID)) {
				throw new SQLException(
						"Falha ao introduzir linha porque a id do filme é necessário"
								+ uri);
			}
		}
		
		if (sUriMatcher.match(uri) == INCOMING_DIRECTOR_COLLECTION_URI_INDICATOR) {
			if (!values.containsKey(DirectorTMD.DIRECTOR_NAME)) {
				throw new SQLException(
						"Falha ao introduzir linha porque a nome do realizador é necessário"
								+ uri);
			}
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId;
		Uri insertedUri = null;

		if (values.containsKey(RefMovActTMD.REF_MA_ACTOR_ID)
				&& values.containsKey(RefMovActTMD.REF_MA_MOVIE_ID)) {
			rowId = db.insert(RefMovActTMD.TABLE_NAME, null, values);
			if (rowId > 0) {
				insertedUri = ContentUris.withAppendedId(RefMovActTMD.CONTENT_URI,
						rowId);
				getContext().getContentResolver().notifyChange(insertedUri,
						null);
			}
			return insertedUri;
		}
		
		if (values.containsKey(DayTMD.MOVIE_ID)) {
			rowId = db.insert(DayTMD.TABLE_NAME, null, values);
			if (rowId > 0) {
				insertedUri = ContentUris.withAppendedId(DayTMD.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(insertedUri,
						null);
			}
			return insertedUri;
		}

		if (values.containsKey(MovieTMD.LOCAL_NAME) == true) {
			rowId = db.insert(MovieTMD.TABLE_NAME, null, values);
			if (rowId > 0) {
				insertedUri = ContentUris.withAppendedId(MovieTMD.CONTENT_URI,
						rowId);
				getContext().getContentResolver().notifyChange(insertedUri,
						null);
			}
			return insertedUri;
		}

		if (values.containsKey(ActorTMD.ACTOR_NAME) == true) {
			rowId = db.insert(ActorTMD.TABLE_NAME, null, values);
			if (rowId > 0) {
				insertedUri = ContentUris.withAppendedId(ActorTMD.CONTENT_URI,
						rowId);
				getContext().getContentResolver().notifyChange(insertedUri,
						null);
			}
			return insertedUri;
		}
		
		throw new SQLException("Failed to insert row into " + uri);
	}

	public boolean onCreate() {
		Log.d(TAG, "main onCreate called");
		mOpenHelper = new DatabaseHelper(getContext());
		Log.d(TAG, "main oncreate = " + GuiaHollywoodActivity.databaseVersion);
		return true;
	}

	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		//GuiaHollywoodActivity.databaseVersion
		if (GuiaHollywoodActivity.databaseVersion != null) {
			if (DatabaseMetaData.DATABASE_LATEST_VERSION != GuiaHollywoodActivity.databaseVersion) {
				System.out.println("Updating");
				SQLiteDatabase db = mOpenHelper.getWritableDatabase();
				mOpenHelper.onUpgrade(db, GuiaHollywoodActivity.databaseVersion,
						DatabaseMetaData.DATABASE_LATEST_VERSION);
				db.close();
				GuiaHollywoodActivity.userPreferences
				.setdBVersion(DatabaseMetaData.DATABASE_LATEST_VERSION);
			}
		}
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		Cursor c;
		SQLiteDatabase db;
		String orderBy;

		
		
		switch (sUriMatcher.match(uri)) {
		case INCOMING_MOVIE_COLLECTION_URI_INDICATOR:
			qb.setTables(MovieTMD.TABLE_NAME);
			qb.setProjectionMap(sMovieProjectionMap);
			// If no sort order is specified use the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = MovieTMD.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sortOrder;
			}

			// Get the database and run the query
			db = mOpenHelper.getReadableDatabase();
			c = qb.query(db, projection, selection, selectionArgs, null, null,
					orderBy);
			// example of getting a count
			// int i = c.getCount();

			// Tell the cursor what uri to watch,
			// so it knows when its source data changes
			c.setNotificationUri(getContext().getContentResolver(), uri);
			break;

		case INCOMING_MOVIE_SINGLE_URI_INDICATOR:
			qb.setTables(MovieTMD.TABLE_NAME);
			qb.setProjectionMap(sMovieProjectionMap);
			qb.appendWhere(MovieTMD._ID + "=" + uri.getPathSegments().get(1));

			// If no sort order is specified use the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = MovieTMD.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sortOrder;
			}

			// Get the database and run the query
			db = mOpenHelper.getReadableDatabase();
			c = qb.query(db, projection, selection, selectionArgs, null, null,
					orderBy);

			// Tell the cursor what uri to watch,
			// so it knows when its source data changes
			c.setNotificationUri(getContext().getContentResolver(), uri);
			break;

		case INCOMING_DAY_COLLECTION_URI_INDICATOR:
			qb.setTables(DayTMD.TABLE_NAME);
			qb.setProjectionMap(sDayProjectionMap);

			// If no sort order is specified use the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = DayTMD.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sortOrder;
			}

			// Get the database and run the query
			db = mOpenHelper.getReadableDatabase();
			c = qb.query(db, projection, selection, selectionArgs, null, null,
					orderBy);
			
			c.setNotificationUri(getContext().getContentResolver(), uri);
			break;
			
		case INCOMING_ACTOR_COLLECTION_URI_INDICATOR:
			qb.setTables(ActorTMD.TABLE_NAME);
			qb.setProjectionMap(sActorProjectionMap);
			
			// If no sort order is specified use the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = ActorTMD.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sortOrder;
			}
			
			// Get the database and run the query
			db = mOpenHelper.getReadableDatabase();
			c = qb.query(db, projection, selection, selectionArgs, null, null,
					orderBy);
			
			c.setNotificationUri(getContext().getContentResolver(), uri);
			break;
			
		case INCOMING_REF_ACTOR_COLLECTION_URI_INDICATOR:
			qb.setTables(RefMovActTMD.TABLE_NAME);
			qb.setProjectionMap(sRefActorProjectionMap);
			
			// If no sort order is specified use the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = RefMovActTMD.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sortOrder;
			}
			
			// Get the database and run the query
			db = mOpenHelper.getReadableDatabase();
			c = qb.query(db, projection, selection, selectionArgs, null, null,
					orderBy);
			
			c.setNotificationUri(getContext().getContentResolver(), uri);
			break;

		case INCOMING_REF_ACTOR_SINGLE_URI_INDICATOR:
			qb.setTables(RefMovActTMD.TABLE_NAME);
			qb.setProjectionMap(sRefActorProjectionMap);
			qb.appendWhere(RefMovActTMD._ID + "=" + uri.getPathSegments().get(1));

			// If no sort order is specified use the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = RefMovActTMD.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sortOrder;
			}

			// Get the database and run the query
			db = mOpenHelper.getReadableDatabase();
			c = qb.query(db, projection, selection, selectionArgs, null, null,
					orderBy);

			// Tell the cursor what uri to watch,
			// so it knows when its source data changes
			c.setNotificationUri(getContext().getContentResolver(), uri);
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		return c;
	}

	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		
		case INCOMING_MOVIE_COLLECTION_URI_INDICATOR:
			count = db.update(MovieTMD.TABLE_NAME, values, where, whereArgs);
			break;

		case INCOMING_MOVIE_SINGLE_URI_INDICATOR:
			String rowFilmeId = uri.getPathSegments().get(1);
			count = db.update(
					MovieTMD.TABLE_NAME,
					values,
					MovieTMD._ID
							+ "="
							+ rowFilmeId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case INCOMING_DAY_COLLECTION_URI_INDICATOR:
			count = db.update(DayTMD.TABLE_NAME, values, where, whereArgs);
			break;

		case INCOMING_DAY_SINGLE_URI_INDICATOR:
			String rowAlarmId = uri.getPathSegments().get(1);
			count = db.update(DayTMD.TABLE_NAME, values, MovieTMD._ID + "="
							+ rowAlarmId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		case INCOMING_ACTOR_SINGLE_URI_INDICATOR:
			String rowActorId = uri.getPathSegments().get(1);
			count = db.update(ActorTMD.TABLE_NAME, values, ActorTMD._ID + "="
					+ rowActorId
					+ (!TextUtils.isEmpty(where) ? " AND (" + where
							+ ')' : ""), whereArgs);
			break;
			
		case INCOMING_ACTOR_COLLECTION_URI_INDICATOR:
			count = db.update(ActorTMD.TABLE_NAME, values, where, whereArgs);
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}
