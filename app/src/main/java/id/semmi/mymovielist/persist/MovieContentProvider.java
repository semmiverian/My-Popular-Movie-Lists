package id.semmi.mymovielist.persist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Movie;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Semmiverian on 6/1/16.
 */
public class MovieContentProvider extends ContentProvider {

    public static final int MOVIE = 1;
    public static final int MOVIE_DETAILS = 32;
    private MovieDbHelper movieDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sSqlQUERY_BUILDER;

    static{
        sSqlQUERY_BUILDER = new SQLiteQueryBuilder();
        sSqlQUERY_BUILDER.setTables(MovieContract.MovieEntry.Table_Name);
    }


    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,MovieContract.Path_Movie,MOVIE);
        matcher.addURI(authority,MovieContract.Path_Movie+"/*",MOVIE_DETAILS);
        return matcher;
    }
    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor retrieveCursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                retrieveCursor = getAllMovies();
                break;
            case MOVIE_DETAILS:
                retrieveCursor = getMovieDetail(uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri "+ uri);
        }
        retrieveCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retrieveCursor;
    }

    private Cursor getMovieDetail(Uri uri) {
        String movie_id = MovieContract.MovieEntry.getMovieId(uri);
        String[] id = new String[]{movie_id};
        Log.d("asd", "getMovieDetail: "+id);
        return sSqlQUERY_BUILDER.query(movieDbHelper.getReadableDatabase(),null, MovieContract.MovieEntry._ID+"= ?",id,null,null,null);
    }

    private Cursor getAllMovies() {
        return sSqlQUERY_BUILDER.query(movieDbHelper.getReadableDatabase(),null,null,null,null,null,null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int math = sUriMatcher.match(uri);
        switch (math){
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_DETAILS:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri"+ uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int match =  sUriMatcher.match(uri);
        Uri returnuri;
        switch(match){
            case MOVIE:
                long id = db.insert(MovieContract.MovieEntry.Table_Name,null,contentValues);
                if(id > 0){
                    returnuri = MovieContract.MovieEntry.buildFavoriteMovie(id);
                }else
                    throw new SQLException("Failed insert new row into "+ uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri"+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        db.close();
        return  returnuri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
