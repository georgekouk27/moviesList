package gr.georkouk.movieslist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import gr.georkouk.movieslist.adapter.MoviesRecyclerAdapter;
import gr.georkouk.movieslist.data.MovieContract;
import gr.georkouk.movieslist.entity.Movie;
import gr.georkouk.movieslist.entity.ResponseMovies;
import gr.georkouk.movieslist.interfaces.InterfaceApi;
import gr.georkouk.movieslist.network.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings("UnusedReturnValue")
public class ActivityMain extends AppCompatActivity {

    private final static String API_KEY = BuildConfig.API_KEY;

    private MoviesRecyclerAdapter moviesAdapter;
    private InterfaceApi interfaceApi;
    private AsyncTask asyncTask;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        setTitle("");

        initializeView();

        this.interfaceApi = RestClient.getClient().create(InterfaceApi.class);
    }

    @Override
    protected void onResume() {
        if(this.spinner != null && this.spinner.getSelectedItemPosition() == 3){
            fillMoviesList(3);
        }

        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState.getBoolean("asyncRunning", false)){
            fillMoviesList(3);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(this.asyncTask != null
                && !this.asyncTask.isCancelled()
                && this.asyncTask.getStatus() != AsyncTask.Status.FINISHED){

            this.asyncTask.cancel(true);

            outState.putBoolean("asyncRunning" , true);
        }
        else {
            outState.putBoolean("asyncRunning", false);
        }

        super.onSaveInstanceState(outState);
    }

    private boolean initializeView(){

        this.spinner = findViewById(R.id.spMoviesOptions);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, R.layout.layout_drop_title, getResources().getStringArray(R.array.movies_options));

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinner.setAdapter(spinnerArrayAdapter);

        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fillMoviesList(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        this.moviesAdapter = new MoviesRecyclerAdapter(this);

        this.moviesAdapter.setOnItemClickListener(new MoviesRecyclerAdapter.OnItemclickListener() {

            @Override
            public void onPosterClick(Movie movie) {
                Intent intent = new Intent(ActivityMain.this, ActivityDetails.class);

                intent.putExtra("movie", movie);

                startActivity(intent);
            }

        });

        recyclerView.setAdapter(this.moviesAdapter);

        return true;
    }

    private boolean fillMoviesList(int pos){
        if(pos == 3){
            getMoviesFromDB();
        }
        else {
            Call<ResponseMovies> call = getApiCallType(pos);

            call.enqueue(new Callback<ResponseMovies>() {

                @SuppressWarnings("ConstantConditions")
                @Override
                public void onResponse(@NonNull Call<ResponseMovies> call,
                                       @NonNull Response<ResponseMovies> response) {

                    moviesAdapter.swapMovies(response.body().getResults());
                }

                @Override
                public void onFailure(@NonNull Call<ResponseMovies> call, @NonNull Throwable t) {
                    Log.e("my==>", t.toString());
                }

            });
        }

        return true;
    }

    private Call<ResponseMovies> getApiCallType(int pos){
        Call<ResponseMovies> call = null;

        if(pos == 0){
            call = this.interfaceApi.getNowPlayingMovies(API_KEY);
        }
        else if (pos == 1){
            call = this.interfaceApi.getTopRatedMovies(API_KEY);
        }
        else if (pos == 2){
            call = this.interfaceApi.getPopularMovies(API_KEY);
        }

        return call;
    }

    @SuppressLint("StaticFieldLeak")
    private void getMoviesFromDB() {
        this.asyncTask = new AsyncTask<Void, Void, Cursor>() {
            List<Movie> items = new ArrayList<>();

            @Override
            protected Cursor doInBackground(Void... params) {
                String[] projection = {
                        MovieContract.MovieItem.COLUMN_ID,
                        MovieContract.MovieItem.COLUMN_TITLE,
                        MovieContract.MovieItem.COLUMN_OVERVIEW,
                        MovieContract.MovieItem.COLUMN_VOTE_AVERAGE,
                        MovieContract.MovieItem.COLUMN_RELEASE_DATE,
                        MovieContract.MovieItem.COLUMN_ORIGINAL_TITLE,
                        MovieContract.MovieItem.COLUMN_POSTER_PATH,
                        MovieContract.MovieItem.COLUMN_BACKDROP_PATH,
                };
                return getApplicationContext().getContentResolver().query(
                        MovieContract.MovieItem.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                );
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                try {
                    if (cursor != null) {

                        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                            Movie movie = new Movie();

                            movie.setId(
                                    cursor.getInt(cursor.getColumnIndex(MovieContract.MovieItem.COLUMN_ID))
                            );

                            movie.setTitle(
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieItem.COLUMN_TITLE))
                            );

                            movie.setOverview(
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieItem.COLUMN_OVERVIEW))
                            );

                            movie.setVoteAverage(
                                    cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieItem.COLUMN_VOTE_AVERAGE))
                            );

                            movie.setReleaseDate(
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieItem.COLUMN_RELEASE_DATE))
                            );

                            movie.setOriginalTitle(
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieItem.COLUMN_ORIGINAL_TITLE))
                            );

                            movie.setPosterPath(
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieItem.COLUMN_POSTER_PATH))
                            );

                            movie.setBackdropPath(
                                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieItem.COLUMN_BACKDROP_PATH))
                            );

                            items.add(movie);
                        }

                    }

                    swapMovies(items);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    if(cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }
        }.execute();
    }

    private boolean swapMovies(final List<Movie> movies){
        moviesAdapter.swapMovies(movies);

        return movies.size() > 0;
    }

}
