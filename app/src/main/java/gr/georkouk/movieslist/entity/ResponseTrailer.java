package gr.georkouk.movieslist.entity;


import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class ResponseTrailer {

    @SerializedName("results")
    private List<Trailer> trailers;

    public ResponseTrailer() {
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

}
