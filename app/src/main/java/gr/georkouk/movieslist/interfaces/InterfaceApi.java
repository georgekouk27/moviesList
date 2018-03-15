package gr.georkouk.movieslist.interfaces;

/*
 * Created by geoDev on 4/3/2018.
 */

import gr.georkouk.movieslist.entity.ResponseMovies;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface InterfaceApi {

    @GET("movie/top_rated")
    Call<ResponseMovies> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<ResponseMovies> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Call<ResponseMovies> getNowPlayingMovies(@Query("api_key") String apiKey);

}
