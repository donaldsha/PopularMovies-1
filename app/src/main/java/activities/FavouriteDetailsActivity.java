package activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.AsyncTaskLoader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesstage1.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import adapters.AppExecuters;
import db.FavouriteData;
import db.FavouriteDatabase;
import jSonUtilities.JSONMovieUtilities;
import jSonUtilities.NetworkUtilities;
import movieData.Movie;
import movieData.MovieReview;
import movieData.MovieTrailer;

/**
 * Created by Min-Pc on 7/24/2018.
 */

public class FavouriteDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private ImageView imageViewFavourite;
    private LinearLayout linearLayoutReviews;
    private LayoutInflater inflater;
    private LinearLayout linearLayoutTrailers;
    private ProgressBar progressBarReviews;
    private ProgressBar progressBarTrailers;

    private static Movie movie;
    private static FavouriteData favouriteData;
    private FavouriteDatabase favouriteDatabase;
    private static boolean isFavourite;

    private static final int TRAILER_LOADER_ID = 2;
    private static final int REVIEW_LOADER_ID = 3;


    private void closeOnError(){
        finish();
        Toast.makeText(this, R.string.error_movie_details, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_details);

        linearLayoutReviews = (LinearLayout)findViewById(R.id.list_favourite_reviews);
        linearLayoutTrailers = (LinearLayout)findViewById(R.id.list_favourite_trailers);
        progressBarReviews = (ProgressBar)findViewById(R.id.progressbar_reviews);
        progressBarTrailers = (ProgressBar)findViewById(R.id.progressbar_trailer);
        inflater = (LayoutInflater)LayoutInflater.from(FavouriteDetailsActivity.this);
        imageViewFavourite = (ImageView)findViewById(R.id.imageView_favourite);
        Intent intent = getIntent();

        if (intent == null){
            closeOnError();
        }

        favouriteData = intent.getParcelableExtra(FavouriteData.EXTRA_NAME_MOVIEDATA);
        final int favMovieId = favouriteData.getId();
        InitializeDetails(favouriteData);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String api_key = sharedPreferences.getString(getString(R.string.settings_api_key_key),"");

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.EXTRA_API_KEY), api_key);
        bundle.putInt(getString(R.string.EXTRA_MOVIE_ID), favMovieId);

        LoaderManager loaderManager = getSupportLoaderManager();

        Loader<Object> trailerLoader = loaderManager.getLoader(TRAILER_LOADER_ID);
        if (trailerLoader == null){
            loaderManager.initLoader(TRAILER_LOADER_ID, bundle,FavouriteDetailsActivity.this);
        }else{
            loaderManager.restartLoader(TRAILER_LOADER_ID, bundle, FavouriteDetailsActivity.this);
        }

        Loader<Object> reviewLoader = loaderManager.getLoader(REVIEW_LOADER_ID);
        if (reviewLoader == null){
            loaderManager.initLoader(REVIEW_LOADER_ID, bundle,FavouriteDetailsActivity.this);
        }else{
            loaderManager.restartLoader(REVIEW_LOADER_ID, bundle, FavouriteDetailsActivity.this);
        }

        favouriteDatabase = FavouriteDatabase.getInstance(getApplicationContext());
        AppExecuters.getInstance().diskIO().execute(new Runnable(){
            @Override
            public void run() {
                favouriteData = favouriteDatabase.favDao().getFavouriteByMovieId(favouriteData.getId());
                isFavourite = favouriteData != null;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SetFavouriteIcon();
                    }
                });
            }
        });

        isFavourite = false;
        imageViewFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFavourite = !isFavourite;
                AppExecuters.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFavourite){
                            favouriteDatabase.favDao().deleteFavourites(favouriteData);
                            favouriteData = null;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isFavourite){
                                    Toast.makeText(FavouriteDetailsActivity.this, getApplicationContext().getString(R.string.remove_from_favourites), Toast.LENGTH_SHORT).show();
                                }
                                SetFavouriteIcon();
                            }
                        });
                    }
                });
            }
        });
    }

    private void InitializeTrailer(String jsonString){
        progressBarTrailers.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(jsonString)){
            List<MovieTrailer> trailers = JSONMovieUtilities.ParseTrailers(jsonString);
            int dispTrailers = 0;

            if (trailers.size() > 0){
                for (int i = 0; i<trailers.size();i++){
                    String site = trailers.get(i).getSite();
                    String type = trailers.get(i).getType();

                    if (site.equalsIgnoreCase("youtube") && type.equalsIgnoreCase("trailer")){
                        View view = inflater.inflate(R.layout.activity_trailer_item, linearLayoutTrailers, false);
                        TextView name = (TextView)view.findViewById(R.id.textView_trailer_name);
                        name.setText(trailers.get(i).getName());

                        TextView tVtype = (TextView)view.findViewById(R.id.textView_trailer_type);
                        String hostInfo = trailers.get(i).getType() + " (" + trailers.get(i).getSite() + ")";
                        tVtype.setText(hostInfo);

                        view.setTag(trailers.get(i));
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View vv) {
                                MovieTrailer movieTrailer = (MovieTrailer)vv.getTag();
                                PlayTrailer(movieTrailer);
                            }
                        });

                        linearLayoutTrailers.addView(view);
                        dispTrailers++;
                    }
                }
            }
            if (dispTrailers == 0){
                View v = inflater.inflate(R.layout.activity_trailer_nodata, linearLayoutReviews, false);
                linearLayoutTrailers.addView(v);
            }
        }else {
            View v = inflater.inflate(R.layout.activity_trailer_load_error, linearLayoutReviews, false);
            linearLayoutTrailers.addView(v);
        }
    }

    private void PlayTrailer(MovieTrailer movieTrailer) {
        String traKey = movieTrailer.getKey();
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + traKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + traKey));
        try {
            startActivity(appIntent);
        }catch (ActivityNotFoundException e){
            startActivity(webIntent);
        }
    }

    private void SetFavouriteIcon() {
        if (isFavourite){
            imageViewFavourite.setImageDrawable(getDrawable(R.mipmap.not_favourite));
        }else {
            imageViewFavourite.setVisibility(View.INVISIBLE);
        }
    }

    private void InitializeDetails(FavouriteData favouriteData){
        String title = favouriteData.getTitle();
        String release = favouriteData.getReleaseDate();
        String poster = favouriteData.getPoster_path();
        String overview = favouriteData.getOverview();
        double rating = favouriteData.getRating();

        ImageView imageView = (ImageView)findViewById(R.id.imageView_favourite_poster);
        URL urlPoster = NetworkUtilities.buildPosterUrl(poster);

        Picasso.get().load(urlPoster.toString()).placeholder(R.mipmap.icon)
                .into(imageView);

        TextView movTitle = (TextView)findViewById(R.id.textView_favourite_title);
        movTitle.setText(title);
        TextView movRelease = (TextView)findViewById(R.id.textView_favourite_release);
        movRelease.setText(release);
        TextView movOverview = (TextView)findViewById(R.id.textView_favourite_overview);
        movOverview.setText(overview);
        TextView movRating = (TextView)findViewById(R.id.textView_favourite_average);
        movRating.setText(Double.toString(rating));
    }

    private void InitializeReviews(String jsonString) {
        progressBarReviews.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(jsonString)) {
            List<MovieReview> reviews = JSONMovieUtilities.ParseReviews(jsonString);


            if (reviews.size() > 0) {
                for (int i = 0; i < reviews.size(); i++) {

                    View view = inflater.inflate(R.layout.activity_review_item, linearLayoutReviews, false);
                    TextView name = (TextView) view.findViewById(R.id.textView_author);
                    name.setText(reviews.get(i).getAuthor());

                    TextView content = (TextView) view.findViewById(R.id.textView_content);
                    content.setText(reviews.get(i).getContent());


                    view.setTag(reviews.get(i));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View vv) {
                            MovieReview movieReview = (MovieReview) vv.getTag();
                            ShowReview(movieReview);
                        }
                    });

                    linearLayoutReviews.addView(view);

                }
            } else {
                View v = inflater.inflate(R.layout.activity_review_nodata, linearLayoutReviews, false);
                linearLayoutReviews.addView(v);
            }
        } else {
            View v = inflater.inflate(R.layout.activity_review_load_error, linearLayoutReviews, false);
            linearLayoutReviews.addView(v);
        }
    }

    private void ShowReview(MovieReview movieReview) {
        String urlReview = movieReview.getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlReview));
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(final int i,final Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (bundle == null){
                    return;
                }if (i == TRAILER_LOADER_ID){
                    progressBarTrailers.setVisibility(View.VISIBLE);
                }else{
                    progressBarReviews.setVisibility(View.VISIBLE);
                }
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                int movId = bundle.getInt(getString(R.string.EXTRA_MOVIE_ID));
                String api_key = bundle.getString(getString(R.string.EXTRA_API_KEY));

                URL url;
                if (i == TRAILER_LOADER_ID){
                    url = NetworkUtilities.buildTrailerUrl(api_key, movId);
                }else {
                    url = NetworkUtilities.buildReviewUrl(api_key, movId);
                }
                String jsonString = "";
                try {
                    jsonString = NetworkUtilities.getResponseFromHTTPUrl(url);
                }catch (IOException e){
                    e.printStackTrace();
                    return null;
                }
                return jsonString;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        int loaderId = loader.getId();

        if (loaderId == TRAILER_LOADER_ID){
            InitializeTrailer(s);
        }else {
            InitializeReviews(s);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
