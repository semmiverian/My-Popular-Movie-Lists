package id.semmi.mymovielist;

import java.util.List;

import id.semmi.mymovielist.models.NowPlaying;
import id.semmi.mymovielist.models.ReviewContainer;
import id.semmi.mymovielist.models.Trailer;
import id.semmi.mymovielist.models.TrailerContainer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Semmiverian on 5/26/16.
 */
public interface ApiInterface {

    @GET("movie/now_playing/")
    Call<NowPlaying> fetchNowPlaying(@Query("api_key") String API_KEY);

    @GET("movie/top_rated/")
    Call<NowPlaying> fetchTopRated(@Query("api_key") String API_KEY);

    @GET("movie/popular/")
    Call<NowPlaying> fetchPopularMovie(@Query("api_key") String API_KEY);

    @GET("movie/{id}/videos")
    Call<TrailerContainer> fetchMovieTrailer(@Path("id") String id, @Query("api_key") String API_KEY );

    @GET("movie/{id}/reviews")
    Call<ReviewContainer> fetchMovieReview(@Path("id") String id, @Query("api_key") String API_KEY );
}
