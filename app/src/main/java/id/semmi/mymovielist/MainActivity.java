package id.semmi.mymovielist;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import id.semmi.mymovielist.models.Movies;
import id.semmi.mymovielist.models.NowPlaying;
import id.semmi.mymovielist.persist.MovieContract;
import id.semmi.mymovielist.persist.MovieDbHelper;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "asd";
    private RecyclerView rv;
    private List<Movies> movies;
    private MoviesAdapter mAdapter;
    private ContentLoadingProgressBar spinner;
    private static final int favorite_loader_id = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        movies = new ArrayList<>();
        mAdapter = new MoviesAdapter(this,movies);
        rv = (RecyclerView) findViewById(R.id.rv);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new SlideInUpAnimator());
        rv.setAdapter(mAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        spinner = (ContentLoadingProgressBar) findViewById(R.id.spinner);
        assert spinner != null;
        spinner.setVisibility(View.VISIBLE);
        firstApiCall();
        mAdapter.setOnMoviesClick(new MoviesAdapter.OnMoviesClick() {
            @Override
            public void onMoviesClick(View view, int position) {
//                Toast.makeText(MainActivity.this, movies.get(position).getOriginal_title(), Toast.LENGTH_SHORT).show();
                Intent movieDetailsIntent = new Intent(MainActivity.this,MovieDetails.class);
                movieDetailsIntent.putExtra(MovieDetails.parceableExtra,movies.get(position));
                startActivity(movieDetailsIntent);
            }
        });

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

    private void cursorTest(Cursor cursor) {
        int nameIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME);
        int ratingIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
        int movie_id_index = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        Log.d(TAG, "cursorTest: "+cursor.moveToFirst());
        Log.d(TAG, "cursorTest: "+cursor.getCount());
        Log.d(TAG, "cursorTest: "+nameIndex);
        Log.d(TAG, "cursorTest: "+cursor.getString(nameIndex));

        for (int i = 0; i < cursor.getCount(); i++) {
            Log.d(TAG, "cursorTestLooping: "+cursor.getString(nameIndex));
            Log.d(TAG, "cursorTestLooping: "+cursor.getString(movie_id_index));
        }
    }


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            popularApiCall();
        }
        if (id == R.id.action_highest_rated) {
            highestRatedMovieCall();
        }
        if(id == R.id.action_favorite){
            favoriteLoaderCall();
        }
        return true;
    }

    private void favoriteLoaderCall() {
        clearRecyclerView();
        spinner.setVisibility(View.VISIBLE);

        getLoaderManager().initLoader(favorite_loader_id, null, new LoaderManager.LoaderCallbacks<Cursor>()  {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                // execute the query from content provider in the background thread
                return new CursorLoader(MainActivity.this,MovieContract.MovieEntry.CONTENT_URI,null,null,null,null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                // call when the loader finished load the data
                loadFavoriteData(cursor);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                // call when the loader is being destroyed
            }
        });

    }

    private void loadFavoriteData(Cursor cursor) {
        int nameIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME);
        int ratingIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
        int imageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE);
        int descriptionIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DESCRIPTION);
        int dateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE);
        int movie_id_index = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        Log.d(TAG, "cursorTest: "+cursor.moveToFirst());
        Log.d(TAG, "cursorTest: "+cursor.getCount());
        Log.d(TAG, "cursorTest: "+nameIndex);
        Log.d(TAG, "cursorTest: "+cursor.getString(nameIndex));
        int i = 0;
        do{
            String addId = cursor.getString(movie_id_index);
            String addImage = cursor.getString(imageIndex);
            String addDesc = cursor.getString(descriptionIndex);
            String addTitle = cursor.getString(nameIndex);
            String addDate = cursor.getString(dateIndex);
            String addRating = cursor.getString(ratingIndex);
            Log.d(TAG, "sdsadas: "+addId);
            movies.add(new Movies(addId,addImage,addDesc,addTitle,addDate,addRating));
            mAdapter.notifyItemInserted(i);
            i++;
        }while (cursor.moveToNext());

        cursor.close();

    }

    private void highestRatedMovieCall() {
        clearRecyclerView();
        spinner.setVisibility(View.VISIBLE);
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ApiConstant.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit2.create(ApiInterface.class);
        Call<NowPlaying> call = apiInterface.fetchTopRated(ApiConstant.API_KEY);
        call.enqueue(new Callback<NowPlaying>() {
            @Override
            public void onResponse(Call<NowPlaying> call, Response<NowPlaying> response) {
                if(response.isSuccessful()){
                    spinner.setVisibility(View.GONE);
                    int insert = 0;
                    NowPlaying np = response.body();
                    for(Movies playing : np.getNowPlayingResults()){
                        movies.add(playing);
                        mAdapter.notifyItemInserted(insert);
                        insert++;
                    }
                }
            }

            @Override
            public void onFailure(Call<NowPlaying> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }



    private void popularApiCall() {
        spinner.setVisibility(View.VISIBLE);
        clearRecyclerView();
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ApiConstant.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit2.create(ApiInterface.class);
        Call<NowPlaying> call = apiInterface.fetchPopularMovie(ApiConstant.API_KEY);
        call.enqueue(new Callback<NowPlaying>() {
            @Override
            public void onResponse(Call<NowPlaying> call, Response<NowPlaying> response) {
                if(response.isSuccessful()){
                    spinner.setVisibility(View.GONE);
                    int insert = 0;
                    NowPlaying np = response.body();;
                    for(Movies playing : np.getNowPlayingResults()){
                        movies.add(playing);
                        mAdapter.notifyItemInserted(insert);
                        insert++;
                    }
                }
            }

            @Override
            public void onFailure(Call<NowPlaying> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }

    private void firstApiCall(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ApiConstant.base_url)
                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
                .build();
        ApiInterface apiInterface = retrofit2.create(ApiInterface.class);
        Call<NowPlaying> call = apiInterface.fetchNowPlaying(ApiConstant.API_KEY);
       call.enqueue(new Callback<NowPlaying>() {
           @Override
           public void onResponse(Call<NowPlaying> call, Response<NowPlaying> response) {
                if(response.isSuccessful()){
                    spinner.setVisibility(View.GONE);
                    int insert = 0;
                    NowPlaying np = response.body();
                    for(Movies playing : np.getNowPlayingResults()){
                        movies.add(playing);
                        mAdapter.notifyItemInserted(insert);
                        insert++;
                    }
                }
           }

           @Override
           public void onFailure(Call<NowPlaying> call, Throwable t) {
               Log.e(TAG, "onFailure: "+t.getMessage() );
           }
       });
    }

    private void clearRecyclerView() {
        int size = movies.size();
        mAdapter.notifyItemRangeRemoved(0,size);
        movies.clear();
    }


}
