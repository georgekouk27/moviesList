package gr.georkouk.movieslist.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import gr.georkouk.movieslist.R;
import gr.georkouk.movieslist.entity.Review;


public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Review> reviews;


    public ReviewRecyclerAdapter(Context context){
        this.context = context;
        this.reviews = new ArrayList<>();
    }

    @NonNull
    @Override
    public ReviewRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return new ReviewRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRecyclerAdapter.ViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.tvReviewAuthor.setText(review.getAuthor());
        holder.tvReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return (null != this.reviews ? this.reviews.size() : 0);
    }

    public boolean swapReviews(List<Review> reviews){
        this.reviews = reviews;

        notifyDataSetChanged();

        return true;
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private TextView tvReviewAuthor;
        private TextView tvReviewContent;

        ViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;
            this.tvReviewAuthor = view.findViewById(R.id.tvReviewAuthor);
            this.tvReviewContent = view.findViewById(R.id.tvReviewContent);
        }

    }

}
