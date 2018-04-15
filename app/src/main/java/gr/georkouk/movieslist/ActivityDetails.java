package gr.georkouk.movieslist;

/*
 * Created by georkouk on 7/3/18.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import gr.georkouk.movieslist.adapter.ReviewRecyclerAdapter;
import gr.georkouk.movieslist.adapter.TrailerRecyclerAdapter;
import gr.georkouk.movieslist.entity.Movie;
import gr.georkouk.movieslist.entity.ResponseReviews;
import gr.georkouk.movieslist.entity.ResponseTrailer;
import gr.georkouk.movieslist.entity.Trailer;
import gr.georkouk.movieslist.interfaces.InterfaceApi;
import gr.georkouk.movieslist.network.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings({"UnusedReturnValue", "ResultOfMethodCallIgnored"})
public class ActivityDetails extends AppCompatActivity {

    private Movie movie;
    private ImageView ivBackdrop;
    private ImageView ivPoster;
    private TextView tvMovieTitle;
    private TextView tvMovieReleaseDate;
    private TextView tvMovieOverview;
    private TextView tvMovieRating;
    private RatingBar ratingBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private InterfaceApi interfaceApi;
    private TrailerRecyclerAdapter trailersAdapter;
    private ReviewRecyclerAdapter reviewsAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initializeViews();

        Intent intent = getIntent();

        if (intent != null){
            this.movie = (Movie) intent.getSerializableExtra("movie");

            this.interfaceApi = RestClient.getClient().create(InterfaceApi.class);

            fillView();
            fillTrailers();
            fillReviews();
        }
        else {
            Toast.makeText(this, "Problem occurred", Toast.LENGTH_SHORT).show();

            this.finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean initializeViews(){
        this.ivBackdrop = findViewById(R.id.ivBackdrop);
        this.ivPoster = findViewById(R.id.ivPoster);
        this.tvMovieTitle = findViewById(R.id.tvMovieTitle);
        this.tvMovieReleaseDate = findViewById(R.id.tvMovieReleaseDate);
        this.tvMovieOverview = findViewById(R.id.tvMovieOverview);
        this.tvMovieRating = findViewById(R.id.tvMovieRating);
        this.ratingBar = findViewById(R.id.ratingBar);

        this.collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        this.collapsingToolbarLayout.setExpandedTitleColor(
                getResources().getColor(R.color.transparent)
        );

        RecyclerView rvTrailers = findViewById(R.id.rvTrailers);

        rvTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        this.trailersAdapter = new TrailerRecyclerAdapter(this);
        this.trailersAdapter.setOnItemClickListener(new TrailerRecyclerAdapter.OnItemclickListener() {
            @Override
            public void onTrailerClick(Trailer trailer) {
                playTrailer(trailer);
            }
        });

        rvTrailers.setAdapter(this.trailersAdapter);

        RecyclerView rvReviews = findViewById(R.id.rvReviews);

        rvReviews.setLayoutManager(new LinearLayoutManager(this));

        this.reviewsAdapter = new ReviewRecyclerAdapter(this);

        rvReviews.setAdapter(this.reviewsAdapter);

        return true;
    }

    private boolean fillView(){
        Picasso.with(this)
                .load(movie.getBackdropPath())
                .into(this.ivBackdrop);

        Picasso.with(this)
                .load(movie.getPosterPath())
                .into(this.ivPoster);

        this.collapsingToolbarLayout.setTitle(movie.getOriginalTitle());

        this.tvMovieTitle.setText(movie.getTitle());

        String[] date = movie.getReleaseDate().split("-");

        String tmp = date[1] + "/" + date[0];
        this.tvMovieReleaseDate.setText(tmp);

        this.tvMovieOverview.setText(movie.getOverview());

        tmp = movie.getVoteAverage() + "/10";
        this.tvMovieRating.setText(tmp);

        this.ratingBar.setRating(
                (float) movie.getVoteAverage() / 2
        );

        return true;
    }

    private boolean fillTrailers(){
        Call<ResponseTrailer> call = this.interfaceApi.getMovieTrailers(this.movie.getId(), BuildConfig.API_KEY);

        call.enqueue(new Callback<ResponseTrailer>() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(@NonNull Call<ResponseTrailer> call, @NonNull Response<ResponseTrailer> response) {
                trailersAdapter.swapTrailers(response.body().getTrailers());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseTrailer> call, @NonNull Throwable t) {
                Log.e("my==>", t.toString());
            }

        });

        return true;
    }

    private boolean fillReviews(){
        Call<ResponseReviews> call = this.interfaceApi.getMovieReviews(this.movie.getId(), BuildConfig.API_KEY);

        call.enqueue(new Callback<ResponseReviews>() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(@NonNull Call<ResponseReviews> call, @NonNull Response<ResponseReviews> response) {
                reviewsAdapter.swapReviews(response.body().getReviews());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseReviews> call, @NonNull Throwable t) {
                Log.e("my==>", t.toString());
            }

        });

        return true;
    }

    private boolean playTrailer(Trailer trailer){
        if(trailer!= null && trailer.getKey() != null && !trailer.getKey().equals("")) {

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));

            startActivity(intent);
        }

        return true;
    }

}
