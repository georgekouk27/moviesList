package gr.georkouk.movieslist;

import android.content.Intent;
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
import gr.georkouk.movieslist.adapter.MoviesRecyclerAdapter;
import gr.georkouk.movieslist.entity.Movie;
import gr.georkouk.movieslist.entity.ResponseMovies;
import gr.georkouk.movieslist.interfaces.InterfaceApi;
import gr.georkouk.movieslist.network.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings("UnusedReturnValue")
public class ActivityMain extends AppCompatActivity {

    private final static String API_KEY = "";

    private MoviesRecyclerAdapter moviesAdapter;
    private InterfaceApi interfaceApi;


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

    private boolean initializeView(){

        Spinner spinner = findViewById(R.id.spMoviesOptions);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, R.layout.layout_drop_title, getResources().getStringArray(R.array.movies_options));

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        Call<ResponseMovies> call = getApiCallType(pos);

        call.enqueue(new Callback<ResponseMovies>() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(@NonNull Call<ResponseMovies> call, @NonNull Response<ResponseMovies> response) {
                moviesAdapter.swapMovies(response.body().getResults());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMovies> call, @NonNull Throwable t) {
                Log.e("my==>", t.toString());
            }

        });

        return true;
    }

    private Call<ResponseMovies> getApiCallType(int pos){
        Call<ResponseMovies> call;

        if(pos == 0){
            call = this.interfaceApi.getNowPlayingMovies(API_KEY);
        }
        else if (pos == 1){
            call = this.interfaceApi.getTopRatedMovies(API_KEY);
        }
        else{
            call = this.interfaceApi.getPopularMovies(API_KEY);
        }

        return call;
    }

}
