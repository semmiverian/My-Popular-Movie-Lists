package id.semmi.mymovielist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.semmi.mymovielist.models.NowPlaying;
import id.semmi.mymovielist.models.Movies;
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
            return true;
        }
        if (id == R.id.action_highest_rated) {
            highestRatedMovieCall();
            return true;
        }
        return true;
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
