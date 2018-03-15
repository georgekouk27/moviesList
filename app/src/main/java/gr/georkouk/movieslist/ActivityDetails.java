package gr.georkouk.movieslist;

/*
 * Created by georkouk on 7/3/18.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import gr.georkouk.movieslist.entity.Movie;


@SuppressWarnings("UnusedReturnValue")
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

            fillView();
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

}
