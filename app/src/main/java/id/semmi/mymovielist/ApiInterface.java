package id.semmi.mymovielist;

import id.semmi.mymovielist.models.NowPlaying;
import retrofit2.Call;
import retrofit2.http.GET;
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
}
