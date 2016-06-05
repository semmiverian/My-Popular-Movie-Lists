package id.semmi.mymovielist;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.semmi.mymovielist.models.Trailer;

/**
 * Created by Semmiverian on 6/2/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Context mContext;
    private List<Trailer> mTrailer;
    private OnTrailerClick listener;

    public TrailerAdapter(Context mContext, List<Trailer> mTrailer) {
        this.mContext = mContext;
        this.mTrailer = mTrailer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View trailerView = inflater.inflate(R.layout.recycler_movie_trailer,parent,false);
        return new ViewHolder(trailerView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trailer trailer = mTrailer.get(position);
        holder.trailerButton.setText(trailer.getName());

    }

    @Override
    public int getItemCount() {
        return (mTrailer == null)?0 : mTrailer.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private AppCompatButton trailerButton;
        public ViewHolder(final View itemView) {
            super(itemView);
            trailerButton = (AppCompatButton) itemView.findViewById(R.id.playYoutubeTrailer);
            trailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    if(listener != null){
                        listener.onTrailerClick(itemView,position);
                    }
                }
            });
        }
    }

    public interface OnTrailerClick{
        void onTrailerClick(View view, int position);
    }
    public void setOnTrailerClick(OnTrailerClick listener){
        this.listener = listener;
    }
}
