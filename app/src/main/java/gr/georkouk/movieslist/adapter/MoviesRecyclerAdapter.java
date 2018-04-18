package gr.georkouk.movieslist.adapter;

/*
 * Created by georkouk on 7/3/18.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import gr.georkouk.movieslist.R;
import gr.georkouk.movieslist.entity.Movie;


@SuppressWarnings("UnusedReturnValue")
public class MoviesRecyclerAdapter
        extends RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movies;
    private OnItemclickListener onItemClickListener;


    public MoviesRecyclerAdapter(Context context){
        this.context = context;
        this.movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movie = this.movies.get(position);

        Picasso.with(this.context)
                .load(movie.getFullPosterPath())
                .into(holder.poster);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onPosterClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != this.movies ? this.movies.size() : 0);
    }

    public void setOnItemClickListener(OnItemclickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public boolean swapMovies(List<Movie> movies){
        this.movies = movies;

        notifyDataSetChanged();

        return true;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private ImageView poster;

        ViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;
            this.poster = view.findViewById(R.id.iv_moviePoster);
        }

    }

    public interface OnItemclickListener {

        void onPosterClick(Movie movie);

    }

}
