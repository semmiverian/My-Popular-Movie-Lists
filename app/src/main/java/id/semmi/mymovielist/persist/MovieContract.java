package id.semmi.mymovielist.persist;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Semmiverian on 6/1/16.
 */
public  class MovieContract {

    public static final String CONTENT_AUTHORITY = "id.semmi.mymovielist";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String Path_Movie = "movie";


    public static class MovieEntry implements BaseColumns {
        public static  final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(Path_Movie).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + Path_Movie;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + Path_Movie;

        public static final String Table_Name = "movie";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_MOVIE_ID = "movie_id";



        public static  Uri buildFavoriteMovie(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static  Uri buildFavoriteMovieWithId(String movieId){
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }

        public static String getMovieId(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }




}
