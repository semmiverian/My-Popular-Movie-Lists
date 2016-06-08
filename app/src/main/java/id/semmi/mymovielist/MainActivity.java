package id.semmi.mymovielist;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import id.semmi.mymovielist.models.Movies;
import id.semmi.mymovielist.persist.MovieContract;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieClickListener {

    private boolean isOnTablet;
    private static final String detail_fragment_tag = "Fragment Detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        firstApiCall();

        if(findViewById(R.id.movieDetails) != null){
            isOnTablet = true;
            Log.d("asd", "onCreate: on tablet");
//            if(savedInstanceState != null){
//                getSupportFragmentManager().beginTransaction().replace(R.id.movieDetails,new DetailFragment(),detail_fragment_tag).commit();
//            }
        }else{
            isOnTablet = false;
        }



//
//        MovieDbHelper helper = new MovieDbHelper(this);
//        SQLiteDatabase database = helper.getWritableDatabase();
//        ContentValues test = insertDummyData();
//        long row_id;
//        row_id = database.insert(MovieContract.MovieEntry.Table_Name,null,test);
//        Log.d(TAG, "onCreate: "+row_id);
//
//        Cursor cursor = database.query(MovieContract.MovieEntry.Table_Name,null,null,null,null,null,null);
//        Log.d(TAG, "onCreate: "+cursor.moveToFirst());
//        Log.d(TAG, "onCreate1212: "+cursor.getString(1));
//        cursor.close();
//        database.close();


//
//        this.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,insertDummyData());
//
//        Cursor cursor = this.getContentResolver().query(MovieContract.MovieEntry.buildFavoriteMovieWithId("1"),null,null,null,null);
//        assert cursor != null;
//        Log.d(TAG, "onCreate pake provider: "+ cursor.getCount());
//        cursor.close();

        // With Loader
//
//        getLoaderManager().initLoader(favorite_loader_id, null, new LoaderManager.LoaderCallbacks<Cursor>()  {
//            @Override
//            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//                // execute the query from content provider in the background thread
//                return new CursorLoader(MainActivity.this,MovieContract.MovieEntry.CONTENT_URI,null,null,null,null);
//            }
//
//            @Override
//            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//                // call when the loader finished load the data
//                cursorTest(cursor);
//            }
//
//            @Override
//            public void onLoaderReset(Loader<Cursor> loader) {
//                // call when the loader is being destroyed
//            }
//        });


    }

//    private void cursorTest(Cursor cursor) {
//        int nameIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME);
//        int ratingIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
//        int movie_id_index = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
//        Log.d(TAG, "cursorTest: "+cursor.moveToFirst());
//        Log.d(TAG, "cursorTest: "+cursor.getCount());
//        Log.d(TAG, "cursorTest: "+nameIndex);
//        Log.d(TAG, "cursorTest: "+cursor.getString(nameIndex));
//
//        for (int i = 0; i < cursor.getCount(); i++) {
//            Log.d(TAG, "cursorTestLooping: "+cursor.getString(nameIndex));
//            Log.d(TAG, "cursorTestLooping: "+cursor.getString(movie_id_index));
//        }
//    }


    public ContentValues insertDummyData(){
        ContentValues test = new ContentValues();
        test.put(MovieContract.MovieEntry.COLUMN_NAME,"Jono Man");
        test.put(MovieContract.MovieEntry.COLUMN_DATE,"12/12/12");
        test.put(MovieContract.MovieEntry.COLUMN_RATING,4.5);
        test.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION,"Lorem Ipsum");
        test.put(MovieContract.MovieEntry.COLUMN_IMAGE,"/dummy");
        return test;
    }


    @Override
    public void onMovieSelected(Movies movies) {
        if(isOnTablet){
            Bundle movieBundle = new Bundle();
            movieBundle.putParcelable("Movie Parceable",movies);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(movieBundle);
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.movieDetails,fragment,detail_fragment_tag)
                                       .commit();

        }else{
            Intent movieDetailsIntent = new Intent(this,MovieDetails.class);
            movieDetailsIntent.putExtra(MovieDetails.parceableExtra,movies);
            startActivity(movieDetailsIntent);
        }
    }
}
