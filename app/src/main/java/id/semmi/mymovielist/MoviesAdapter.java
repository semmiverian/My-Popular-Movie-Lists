package id.semmi.mymovielist;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import id.semmi.mymovielist.models.Movies;

/**
 * Created by Semmiverian on 5/27/16.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private Context context;
    private List<Movies> movies;
    private OnMoviesClick listener;

    public MoviesAdapter(Context context, List<Movies> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View skuView = inflater.inflate(R.layout.recycler_movies_list,parent,false);
        return new ViewHolder(skuView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movies movie = movies.get(position);
        holder.movie_title.setText(movie.getOriginal_title());
        Glide.with(context).load("http://image.tmdb.org/t/p/w500/"+movie.getPoster_path())
                            .fitCenter()
                            .into(holder.movie_poster);
    }

    @Override
    public int getItemCount() {
        return (movies == null)? 0 : movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private AppCompatImageView movie_poster;
        private AppCompatTextView movie_title;

        public ViewHolder(final View itemView) {
            super(itemView);
            movie_poster = (AppCompatImageView) itemView.findViewById(R.id.movie_poster);
            movie_title = (AppCompatTextView) itemView.findViewById(R.id.movie_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    if(listener != null){
                        listener.onMoviesClick(itemView,position);
                    }

                }
            });
        }
    }

    public interface OnMoviesClick{
        void onMoviesClick(View view, int position);
    }

    public void setOnMoviesClick(OnMoviesClick listener){
        this.listener = listener;
    }
}
