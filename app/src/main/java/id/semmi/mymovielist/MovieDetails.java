package id.semmi.mymovielist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import id.semmi.mymovielist.models.Movies;
import id.semmi.mymovielist.models.Review;
import id.semmi.mymovielist.models.ReviewContainer;
import id.semmi.mymovielist.models.Trailer;
import id.semmi.mymovielist.models.TrailerContainer;
import id.semmi.mymovielist.persist.MovieContract;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetails extends AppCompatActivity {
    private AppCompatImageView movieImage;
    public final static String parceableExtra = "Extra";
    private AppCompatTextView title;
    private AppCompatTextView rating;
    private AppCompatTextView releaseDate;
    private AppCompatTextView synopsis;
    private RecyclerView trailerRv,reviewRv;
    private TrailerAdapter trailerAdapter;
    private List<Trailer> trailerList;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        trailerList = new ArrayList<>();
        reviewList = new ArrayList<>();


        movieImage = (AppCompatImageView) findViewById(R.id.movieImage);
        title = (AppCompatTextView) findViewById(R.id.title);
        rating = (AppCompatTextView) findViewById(R.id.rating);
        releaseDate = (AppCompatTextView) findViewById(R.id.releaseDate);
        synopsis = (AppCompatTextView) findViewById(R.id.synopsis);
        trailerRv = (RecyclerView) findViewById(R.id.rvTrailer);
        reviewRv = (RecyclerView) findViewById(R.id.rvReview);
        fab = (FloatingActionButton) findViewById(R.id.fab);



        trailerAdapter = new TrailerAdapter(this,trailerList);
        reviewAdapter = new ReviewAdapter(this,reviewList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        trailerRv.setLayoutManager(mLayoutManager);
        trailerRv.setItemAnimator(new SlideInUpAnimator());
        trailerRv.setAdapter(trailerAdapter);

        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        reviewRv.setLayoutManager(mLayoutManager2);
        reviewRv.setItemAnimator(new SlideInUpAnimator());
        reviewRv.setAdapter(reviewAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getExtras().get(parceableExtra) != null){
            Movies movie = getIntent().getParcelableExtra(parceableExtra);
            Log.d("asd", "onCreate: "+movie.getId());

            Glide.with(this).load("http://image.tmdb.org/t/p/w500/"+movie.getPoster_path())
                    .centerCrop()
                    .into(movieImage);
            title.setText(movie.getOriginal_title());
            rating.setText("Rating: "+movie.getVote_average());
            releaseDate.setText(movie.getRelease_date());
            synopsis.setText(movie.getOverview());
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl(ApiConstant.base_url)
//                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface apiInterface = retrofit2.create(ApiInterface.class);
            Call<TrailerContainer> call = apiInterface.fetchMovieTrailer(movie.getId(),ApiConstant.API_KEY);
            call.enqueue(new Callback<TrailerContainer>() {
                @Override
                public void onResponse(Call<TrailerContainer> call, Response<TrailerContainer> response) {
                    if(response.isSuccessful()){
                        int count = 0;
                        TrailerContainer container = response.body();
                        for(Trailer trailer : container.getResults()){
                            trailerList.add(trailer);
                            trailerAdapter.notifyItemInserted(count);
                            count++;
                        }
                    }
                }

                @Override
                public void onFailure(Call<TrailerContainer> call, Throwable t) {
                    Log.e("Error", "onFailure: "+t.getMessage());
                }
            });

            Call<ReviewContainer> callReview = apiInterface.fetchMovieReview(movie.getId(),ApiConstant.API_KEY);
            callReview.enqueue(new Callback<ReviewContainer>() {
                @Override
                public void onResponse(Call<ReviewContainer> call, Response<ReviewContainer> response) {
                    if(response.isSuccessful()){
                        ReviewContainer container = response.body();
                        if(container.getResults().size() == 0){
                            reviewList.add(new Review("There is no review yet for this movie"));
                            reviewAdapter.notifyItemInserted(0);
                            return;
                        }
                        for(Review review : container.getResults()){
                            int count = 0;
                            reviewList.add(review);
                            reviewAdapter.notifyItemInserted(count);
                            count++;
                        }
                    }
                }

                @Override
                public void onFailure(Call<ReviewContainer> call, Throwable t) {
                    Log.e("Error", "onFailure: "+t.getMessage());
                }
            });
        }

        trailerAdapter.setOnTrailerClick(new TrailerAdapter.OnTrailerClick() {
            @Override
            public void onTrailerClick(View view, int position) {
                Toast.makeText(MovieDetails.this, trailerList.get(position).getKey(), Toast.LENGTH_SHORT).show();
                Intent youtubeAppIntent = new Intent(Intent.ACTION_VIEW);
//                youtubeAppIntent.setData(Uri.parse("https://www.youtube.com/watch?v"+trailerList.get(position).getKey()));
                youtubeAppIntent.setData(Uri.parse("vnd.youtube:"+trailerList.get(position).getKey()));
                startActivity(youtubeAppIntent);
            }
        });
    }

    public void fabClick(View v){
        Movies movie = getIntent().getParcelableExtra(parceableExtra);
        Cursor cursor = this.getContentResolver().query(MovieContract.MovieEntry.buildFavoriteMovieWithId(movie.getId()),null,null,null,null);
        assert cursor != null;
        Log.d("sd", "onCreate pake provider: "+ cursor.getCount());
        if(cursor.getCount() == 0){
            this.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,movieData());
            Toast.makeText(MovieDetails.this, "Added to Your Favorite Movies List", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MovieDetails.this, "You've Already put this movie into your favorite lists", Toast.LENGTH_SHORT).show();
        }
        cursor.close();

    }

    private ContentValues movieData() {
        Movies movie = getIntent().getParcelableExtra(parceableExtra);
        ContentValues cv =new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_NAME,movie.getOriginal_title());
        cv.put(MovieContract.MovieEntry.COLUMN_DATE,movie.getRelease_date());
        cv.put(MovieContract.MovieEntry.COLUMN_RATING,movie.getVote_average());
        cv.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION,movie.getOverview());
        cv.put(MovieContract.MovieEntry.COLUMN_IMAGE,movie.getPoster_path());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,movie.getId());
        Log.d("asd", "movieData: "+movie.getOriginal_title());
        return cv;
    }

}
