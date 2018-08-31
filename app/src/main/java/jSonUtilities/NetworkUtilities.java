package jSonUtilities;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by Min-Pc on 7/20/2018.
 */

final public class NetworkUtilities {

    //private static String STATIC_WEATHER_URL = null;
    private static final String PARAM_API_KEY = "api_key";
    private static final String PATH_REVIEW = "reviews";
    private static final String PATH_TRAILER = "videos";
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String LOG_TAG = NetworkUtilities.class.getSimpleName();


    public static URL buildUrl(String api_key, String sort){

        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendPath(sort)
                       .appendQueryParameter(PARAM_API_KEY, api_key).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildTrailerUrl(String api_key, int movieId){
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendPath(PATH_TRAILER)
                .appendQueryParameter(PARAM_API_KEY, api_key).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildReviewUrl(String api_key, int movieId){
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendPath(PATH_REVIEW)
                .appendQueryParameter(PARAM_API_KEY, api_key).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildPosterUrl(String poster_path){
        URL url = null;
        try {
            url = new URL(POSTER_BASE_URL + poster_path);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHTTPUrl(URL url) throws IOException{
        String jsonResponse = "";
        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 );
            urlConnection.setConnectTimeout(15000 );
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);

        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineReader = bufferedReader.readLine();
            while (lineReader != null){
                output.append(lineReader);
                lineReader = bufferedReader.readLine();
            }
        }
        return output.toString();
    }
}
