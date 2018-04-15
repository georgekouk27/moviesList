package gr.georkouk.movieslist.interfaces;

/*
 * Created by geoDev on 4/3/2018.
 */

import gr.georkouk.movieslist.entity.ResponseMovies;
import gr.georkouk.movieslist.entity.ResponseReviews;
import gr.georkouk.movieslist.entity.ResponseTrailer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface InterfaceApi {

    @GET("movie/top_rated")
    Call<ResponseMovies> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<ResponseMovies> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Call<ResponseMovies> getNowPlayingMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<ResponseTrailer> getMovieTrailers(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ResponseReviews> getMovieReviews(@Path("id") int movieId, @Query("api_key") String apiKey);

}
