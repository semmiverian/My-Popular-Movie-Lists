package id.semmi.mymovielist.persist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Semmiverian on 6/1/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_MOVIE_DB = "CREATE TABLE "+ MovieContract.MovieEntry.Table_Name +"("+
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieEntry.COLUMN_NAME+" TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_RATING+" REAL NOT NULL," +
                MovieContract.MovieEntry.COLUMN_DATE+" TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_DESCRIPTION+" TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_IMAGE+" TEXT NOT NULL"+
                ")";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.Table_Name);
        onCreate(sqLiteDatabase);

    }
}
