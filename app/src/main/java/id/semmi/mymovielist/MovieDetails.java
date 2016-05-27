package id.semmi.mymovielist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.bumptech.glide.Glide;

import id.semmi.mymovielist.models.Movies;

public class MovieDetails extends AppCompatActivity {
    private AppCompatImageView movieImage;
    public final static String parceableExtra = "Extra";
    private AppCompatTextView title;
    private AppCompatTextView rating;
    private AppCompatTextView releaseDate;
    private AppCompatTextView synopsis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        movieImage = (AppCompatImageView) findViewById(R.id.movieImage);
        title = (AppCompatTextView) findViewById(R.id.title);
        rating = (AppCompatTextView) findViewById(R.id.rating);
        releaseDate = (AppCompatTextView) findViewById(R.id.releaseDate);
        synopsis = (AppCompatTextView) findViewById(R.id.synopsis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getExtras().get(parceableExtra) != null){
            Movies movie = getIntent().getParcelableExtra(parceableExtra);
            Glide.with(this).load("http://image.tmdb.org/t/p/w500/"+movie.getPoster_path())
                    .centerCrop()
                    .into(movieImage);
            title.setText(movie.getOriginal_title());
            rating.setText("Rating: "+movie.getVote_average());
            releaseDate.setText(movie.getRelease_date());
            synopsis.setText(movie.getOverview());
        }
    }

}
