package id.semmi.mymovielist;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.semmi.mymovielist.models.Review;

/**
 * Created by Semmiverian on 6/2/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context mContext;
    private List<Review> reviewList;

    public ReviewAdapter(Context mContext, List<Review> reviewList) {
        this.mContext = mContext;
        this.reviewList = reviewList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View reviewView = inflater.inflate(R.layout.recycler_movie_review,parent,false);
        return new ViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.author.setText(review.getAuthor());
        holder.review.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return (reviewList == null) ? 0 : reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private AppCompatTextView author;
        private AppCompatTextView review;

        public ViewHolder(View itemView) {
            super(itemView);
            author = (AppCompatTextView) itemView.findViewById(R.id.author);
            review = (AppCompatTextView) itemView.findViewById(R.id.review);
        }
    }
}
