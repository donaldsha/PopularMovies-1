package jSonUtilities;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import movieData.Movie;
import movieData.MovieReview;
import movieData.MovieTrailer;

/**
 * Created by Min-Pc on 7/20/2018.
 */

public class JSONMovieUtilities {

    private static String JSON_MOVIE_ID = "id";
    private static String JSON_TITLE = "title";
    private static String JSON_POSTER = "poster_path";
    private static String JSON_RATING = "vote_average";
    private static String JSON_OVERVIEW = "overview";
    private static String JSON_RELEASE_DATE = "release_date";

    private static String JSON_REVIEW_AUTHOR = "author";
    private static String JSON_REVIEW_URL = "url";
    private static String JSON_REVIEW_CONTENT = "content";

    private static String JSON_TRAILER_NAME = "name";
    private static String JSON_TRAILER_KEY = "key";
    private static String JSON_TRAILER_TYPE = "type";
    private static String JSON_TRAILER_SITE = "site";

    private static String JSON_RESULTS = "results";

    public static List<Movie> ParseOverview(String jsonString){
        List movieData = new ArrayList();

        if (!TextUtils.isEmpty(jsonString)){
            try {
                JSONObject data = new JSONObject(jsonString);
                JSONArray results = data.getJSONArray(JSON_RESULTS);

                for (int i = 0;i < results.length(); i++){
                    JSONObject movie = results.getJSONObject(i);

                    Movie newMovie = new Movie(
                            movie.getInt(JSON_MOVIE_ID),
                            movie.getString(JSON_TITLE),
                            movie.getString(JSON_RELEASE_DATE),
                            movie.getString(JSON_POSTER),
                            movie.getDouble(JSON_RATING),
                            movie.getString(JSON_OVERVIEW));

                    movieData.add(newMovie);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return movieData;
    }

    public static List<MovieReview> ParseReviews(String jsonString){
        List movieReviewData = new ArrayList();

        if (!TextUtils.isEmpty(jsonString)) {
            try {
                JSONObject data = new JSONObject(jsonString);
                JSONArray results = data.getJSONArray(JSON_RESULTS);

                for (int i = 0; i < results.length(); i++) {
                    JSONObject review = results.getJSONObject(i);

                    MovieReview newReview = new MovieReview(
                            review.getString(JSON_REVIEW_AUTHOR),
                            review.getString(JSON_REVIEW_CONTENT),
                            review.getString(JSON_REVIEW_URL));

                    movieReviewData.add(newReview);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return movieReviewData;
    }

    public static List<MovieTrailer> ParseTrailers(String jsonString){
        List movieTrailerData = new ArrayList();

        if (!TextUtils.isEmpty(jsonString)) {
            try {
                JSONObject data = new JSONObject(jsonString);
                JSONArray results = data.getJSONArray(JSON_RESULTS);

                for (int i = 0; i < results.length(); i++) {
                    JSONObject trailer = results.getJSONObject(i);

                    MovieTrailer newTrailer = new MovieTrailer(
                            trailer.getString(JSON_TRAILER_KEY),
                            trailer.getString(JSON_TRAILER_NAME),
                            trailer.getString(JSON_TRAILER_SITE),
                            trailer.getString(JSON_TRAILER_TYPE)
                    );

                    movieTrailerData.add(newTrailer);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movieTrailerData;
    }


/*
    public static Movie[] getMovies(String movieJsonString) throws JSONException{
        JSONObject movieString = new JSONObject(movieJsonString);
        JSONArray arrayRes = movieString.getJSONArray("results");

        Movie[] movies = new Movie[arrayRes.length()];

        for (int i = 0; i<arrayRes.length(); i++){
            JSONObject movie = arrayRes.getJSONObject(i);
            int id = movie.getInt("id");
            String title = movie.getString("title");
            double rating = movie.getDouble("vote_average");
            String imageUrl = movie.getString("poster_path");
            String synopsis = movie.getString("overview");
            String release = movie.getString("release_date");

            movies[i] = new Movie(id, title, imageUrl, synopsis, rating,release);
        }

        return movies;
    }
    */
}
