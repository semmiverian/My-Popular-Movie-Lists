package id.semmi.mymovielist;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.semmi.mymovielist.models.Movies;
import id.semmi.mymovielist.models.NowPlaying;
import id.semmi.mymovielist.persist.MovieContract;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Semmiverian on 6/8/16.
 */
public class MainFragment extends Fragment {
    private RecyclerView rv;
    private List<Movies> movies;
    private MoviesAdapter mAdapter;
    private ContentLoadingProgressBar spinner;
    private static final String TAG = "asd";
    private static final int favorite_loader_id = 1;
    private OnMovieClickListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (OnMovieClickListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMovieClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.content_main,container,false);

        movies = new ArrayList<>();
        mAdapter = new MoviesAdapter(getActivity(),movies);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new SlideInUpAnimator());
        rv.setAdapter(mAdapter);
        spinner = (ContentLoadingProgressBar)view.findViewById(R.id.spinner);
        assert spinner != null;
        spinner.setVisibility(View.VISIBLE);
        setHasOptionsMenu(true);
        mAdapter.setOnMoviesClick(new MoviesAdapter.OnMoviesClick() {
            @Override
            public void onMoviesClick(View view, int position) {
                mListener.onMovieSelected(movies.get(position));
                Toast.makeText(getActivity(), movies.get(position).getOriginal_title(), Toast.LENGTH_SHORT).show();
//                Intent movieDetailsIntent = new Intent(getActivity(),MovieDetails.class);
//                movieDetailsIntent.putExtra(MovieDetails.parceableExtra,movies.get(position));
//                startActivity(movieDetailsIntent);
            }
        });
        firstApiCall();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    public interface OnMovieClickListener  {

        void onMovieSelected(Movies movies);
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
    private void favoriteLoaderCall() {
        clearRecyclerView();
        spinner.setVisibility(View.VISIBLE);

        getActivity().getLoaderManager().initLoader(favorite_loader_id, null, new LoaderManager.LoaderCallbacks<Cursor>()  {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                // execute the query from content provider in the background thread
                return new CursorLoader(getActivity(),MovieContract.MovieEntry.CONTENT_URI,null,null,null,null);
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
}
