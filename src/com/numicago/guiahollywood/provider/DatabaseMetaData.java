package com.numicago.guiahollywood.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseMetaData 
{
	public static String AUTHORITY = "com.numicago.guiahollywood.provider.DatabaseMetaData";

	public static String DATABASE_NAME = "guia_hollywood.db";
	public static int DATABASE_VERSION = 1; //24/10/2011
//	public static int DATABASE_LATEST_VERSION = 2; //18/12/2011
	//Filmes records e filmes datas tabela.
//	public static int DATABASE_LATEST_VERSION = 3; //26/05/2012 
//	public static int DATABASE_LATEST_VERSION = 5; //02/06/2012 
//	public static int DATABASE_LATEST_VERSION = 6; //14/07/2012 rating 
	public static int DATABASE_LATEST_VERSION = 7; //23/09/2012 actor/director 
	//Se alguma vez publicar a aplicação no market, ter em atenção estas situações
	//Subir sempre as duas.

	private DatabaseMetaData(){}

	public static final class MovieTMD implements BaseColumns {
		public static final String TABLE_NAME = "movie";

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/movie");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.numicago.movie";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.numicago.movie";

		public static final String DEFAULT_SORT_ORDER = "_ID ASC";

		public static final String LOCAL_NAME = "localName";
		public static final String ORIGINAL_NAME = "originalName";
		public static final String YEAR = "year";
		public static final String DESCRIPTION = "description";
		public static final String GENRE = "genre";
		public static final String DIRECTOR = "director";
		public static final String DURATION = "length";
		public static final String VIEWED = "viewed";
		public static final String FAVOURITE = "favourite";
		public static final String WHATCH_LIST = "watchList";
		public static final String RATING = "rating";
		
		public static final String SMALL_IMAGE_BLOB = "imagemSmallBlob";
		public static final String SMALL_IMAGE_URL = "imagemSmallUrl";
		public static final String BIG_IMAGE_BLOB = "imagemBigBlob";
		public static final String BIG_IMAGE_URL = "imagemBigUrl";
		
		public static final String HOLLYWOOD_URL = "hollywoodUrl";
		public static final String CHANNEL = "channel";
		public static final String COUNT = "count";

	}
	
	public static final class DayTMD implements BaseColumns {
		public static final String TABLE_NAME = "day";
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/day");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.numicago.day";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.numicago.day";
		
		public static final String MOVIE_ID = "movieId";
		public static final String DATE_START = "dateStart";
		public static final String DATE_END = "dateEnd";
		public static final String ALARM = "alarm";
		
		public static final String DEFAULT_SORT_ORDER = DATE_START + " ASC";
	}
	
	public static final class RefMovActTMD implements BaseColumns {
		public static final String TABLE_NAME = "actorMovieRef";
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/actorMovieRef");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.numicago.actorMovieRef";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.numicago.actorMovieRef";
		
		public static final String DEFAULT_SORT_ORDER = "_ID ASC";
		
		public static final String REF_MA_MOVIE_ID = "movieId";
		public static final String REF_MA_ACTOR_ID = "actorId";
	}
	
	public static final class RefMovieDirectorTMD implements BaseColumns {
		public static final String TABLE_NAME = "directorMovieRef";
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/directorMovieRef");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.numicago.directorMovieRef";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.numicago.directorMovieRef";
		
		public static final String DEFAULT_SORT_ORDER = "_ID ASC";
		
		public static final String REF_MD_MOVIE_ID = "movieId";
		public static final String REF_MD_DIRECTOR_ID = "directorId";
	}
	
	public static final class ActorTMD implements BaseColumns {
		public static final String TABLE_NAME = "actor";

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/actor");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.numicago.actor";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.numicago.actor";

		public static final String DEFAULT_SORT_ORDER = "_ID ASC";

		public static final String ACTOR_NAME = "name";
		public static final String ACTOR_BDAY = "bday";
		public static final String ACTOR_SMALL_IMAGE_BLOB = "smallImageBlob";
		public static final String ACTOR_BIG_IMAGE_BLOB = "bigImageBlob";
		public static final String ACTOR_SMALL_IMAGE_URL = "smallImageUrl";
		public static final String ACTOR_BIG_IMAGE_URL = "bigImageUrl";
		public static final String ACTOR_DESCRIPTION = "description";
		public static final String ACTOR_IMDB_URL = "imdbUrl";
		public static final String ACTOR_FAVOURITE = "favourite";
	}
	
	public static final class DirectorTMD implements BaseColumns {
		public static final String TABLE_NAME = "director";
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/director");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.numicago.director";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.numicago.director";
		
		public static final String DEFAULT_SORT_ORDER = "_ID ASC";
		
		public static final String DIRECTOR_NAME = "name";
		public static final String DIRECTOR_BDAY = "bday";
		public static final String DIRECTOR_SMALL_IMAGE = "smallImage";
		public static final String DIRECTOR_BIG_IMAGE_URL = "bigImageUrl";
		public static final String DIRECTOR_BIG_IMAGE_BLOB = "bigImageBlob";
		public static final String DIRECTOR_SMALL_IMAGE_URL = "smallImageUrl";
		public static final String DIRECTOR_SMALL_IMAGE_BLOB = "smallImageblob";
		public static final String DIRECTOR_DESCRIPTION = "description";
		public static final String DIRECTOR_IMDB_URL = "imdbUrl";
		public static final String DIRECTOR_FAVOURITE = "favourite";
		
	}
}
