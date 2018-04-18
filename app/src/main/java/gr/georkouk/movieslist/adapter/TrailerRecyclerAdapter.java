package gr.georkouk.movieslist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import gr.georkouk.movieslist.R;
import gr.georkouk.movieslist.entity.Trailer;


@SuppressWarnings("UnusedReturnValue")
public class TrailerRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerAdapter.ViewHolder> {

    private static final String YOUTUBE = "https://img.youtube.com/vi/%s/mqdefault.jpg";
    private Context context;
    private List<Trailer> trailers;
    private OnItemclickListener onItemclickListener;

    public TrailerRecyclerAdapter(Context context){
        this.context = context;
        this.trailers = new ArrayList<>();
    }

    @NonNull
    @Override
    public TrailerRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);

        return new TrailerRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerRecyclerAdapter.ViewHolder holder, int position) {
        final Trailer trailer = trailers.get(position);

        Picasso.with(this.context)
                .load(String.format(YOUTUBE, trailer.getKey()))
                .into(holder.thumbnail);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemclickListener.onTrailerClick(trailer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != this.trailers ? this.trailers.size() : 0);
    }

    public boolean swapTrailers(List<Trailer> trailers){
        this.trailers = trailers;

        notifyDataSetChanged();

        return true;
    }

    public void setOnItemClickListener(OnItemclickListener onItemclickListener){
        this.onItemclickListener = onItemclickListener;
    }

    public Trailer getTrailer(int pos){
        if(this.trailers.size() > pos){
            return this.trailers.get(pos);
        }
        else{
            return new Trailer();
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private ImageView thumbnail;

        ViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;
            this.thumbnail = view.findViewById(R.id.thumbnail);
        }

    }

    public interface OnItemclickListener {

        void onTrailerClick(Trailer trailer);

    }

}
