package activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import adapters.MainViewModels;
import com.example.popularmoviesstage1.R;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import adapters.AppExecuters;
import adapters.FavouriteAdapter;
import adapters.RecyclerViewAdapter;
import db.FavouriteData;
import db.FavouriteDatabase;
import jSonUtilities.JSONMovieUtilities;
import jSonUtilities.NetworkUtilities;
import movieData.Movie;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener, LoaderManager.LoaderCallbacks<String>,
        FavouriteAdapter.ItemClickListener{

    private RecyclerView movierecyclerView;
    private RecyclerViewAdapter movieDataAdapter;
    private TextView movieErrorMessages;
    private TextView movieNoFavourites;
    private ProgressBar loadingIndicator;
    private FavouriteAdapter movieFavouriteAdapter;
    private FavouriteDatabase movieFavouriteDatabase;

    private String selectMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingIndicator = (ProgressBar) findViewById(R.id.progressbar_loadingindicator);
        movierecyclerView = (RecyclerView)findViewById(R.id.Recyclerview_posters);
        movieErrorMessages = (TextView)findViewById(R.id.textView_error_message_display);
        movieNoFavourites = (TextView)findViewById(R.id.textview_no_favourites);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String api_key = sharedPreferences.getString(getString(R.string.settings_api_key_key), "");

        selectMode = sharedPreferences.getString(getString(R.string.settings_search_key),
                getString(R.string.settings_most_popular_value));

        String title;
        if (selectMode.equals(getString(R.string.settings_most_popular_value))){
            title = getString(R.string.settings_most_popular);
        }else if (selectMode.equals(getString(R.string.settings_highest_rated_value))){
            title = getString(R.string.app_title_highest);
        }else {
            title = getString(R.string.app_title_favourites);
        }
        setTitle(title);

        if (selectMode.equals(getString(R.string.settings_favourites_value))){
            movieFavouriteDatabase = FavouriteDatabase.getInstance(getApplicationContext());
            movierecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            movieFavouriteAdapter = new FavouriteAdapter(this);
            movieFavouriteAdapter.setClickListener(MainActivity.this);
            movierecyclerView.setAdapter(movieFavouriteAdapter);

            loadingIndicator.setVisibility(View.VISIBLE);
            movierecyclerView.setVisibility(View.VISIBLE);
            setupViewModel();

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                    AppExecuters.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadingIndicator.setVisibility(View.VISIBLE);
                                    movierecyclerView.setVisibility(View.INVISIBLE);
                                }
                            });

                            int position = viewHolder.getAdapterPosition();
                            FavouriteData favouriteData = movieFavouriteAdapter.getFavourites().get(position);
                            movieFavouriteDatabase.favDao().deleteFavourites(favouriteData);
                        }
                    });
                }
            }).attachToRecyclerView(movierecyclerView);

        }else{
            movierecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.BUNDLE_API_KEY), api_key);
            bundle.putString(getString(R.string.BUNDLE_ORDER_BY), selectMode);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Object> dataLoader = loaderManager.getLoader(1);
            if (dataLoader==null){
                loaderManager.initLoader(1, bundle, MainActivity.this);
            }else{
                loaderManager.restartLoader(1, bundle, MainActivity.this);
            }
        }
    }

    private void setupViewModel() {
        MainViewModels mainModel = ViewModelProviders.of(this).get(MainViewModels.class);
        mainModel.getFavourites().observe(this, new Observer<List<FavouriteData>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteData> favouriteData) {
                movieFavouriteAdapter.setFavourites(favouriteData);
                final int favCount = favouriteData.size();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingIndicator.setVisibility(View.INVISIBLE);

                        movierecyclerView.setVisibility(favCount > 0 ? View.VISIBLE : View.INVISIBLE);
                        movieNoFavourites.setVisibility(favCount > 0 ? View.INVISIBLE : View.VISIBLE);
                    }
                });
            }
        });
    }



    @Override
    public void onItemClickListener(View view, int index) {
        FavouriteData clkMovie = movieFavouriteAdapter.getFavData(index);
        Intent intent = new Intent(this, FavouriteDetailsActivity.class);
        intent.putExtra(FavouriteData.EXTRA_NAME_MOVIEDATA, clkMovie);
        startActivity(intent);
    }

    public void onItemClick(View view, int index){
        Movie clickedmovie = movieDataAdapter.getItem(index);
        launchDetailActivity(clickedmovie);
    }

    private void launchDetailActivity(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Movie.EXTRA_NAME_MOVIEDATA, movie);
        startActivity(intent);
    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null){
                    return;
                }
                loadingIndicator.setVisibility(View.VISIBLE);
                movierecyclerView.setVisibility(View.INVISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String api_key = args.getString(getString(R.string.BUNDLE_API_KEY));
                String order_by = args.getString(getString(R.string.BUNDLE_ORDER_BY));

                URL url = NetworkUtilities.buildUrl(api_key, order_by);
                String json = "";
                try {
                    json = NetworkUtilities.getResponseFromHTTPUrl(url);
                }catch (IOException e){
                    e.printStackTrace();
                    return null;
                }
                return json;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        loadingIndicator.setVisibility(View.INVISIBLE);

        if (!TextUtils.isEmpty(data)){
            movierecyclerView.setVisibility(View.VISIBLE);
            List<Movie> movieList = JSONMovieUtilities.ParseOverview(data);
            movieDataAdapter = new RecyclerViewAdapter(MainActivity.this, movieList);
            movieDataAdapter.setClickListener(MainActivity.this);
            movierecyclerView.setAdapter(movieDataAdapter);
        }else {
            movieErrorMessages.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
