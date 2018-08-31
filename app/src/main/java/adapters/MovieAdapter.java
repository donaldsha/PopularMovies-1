package adapters;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmoviesstage1.R;
import com.squareup.picasso.Picasso;

import movieData.Movie;

/**
 * Created by Min-Pc on 7/20/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Movie[] movies;
    private final MovieAdapterOnClickHadler movieAdapterOnClickHadler;
    private final Context context;
    public static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/w500";

    public MovieAdapter(MovieAdapterOnClickHadler clkHandler, Context context){
        movieAdapterOnClickHadler = clkHandler;
        this.context = context;

    }

    public interface MovieAdapterOnClickHadler{
        void onItemClickListener(int id, String title, String imageUrl, String synopsis, double rating, String releaseDate);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView image;

        public MovieViewHolder(View item){
            super(item);
            image = item.findViewById(R.id.movie_image_view);
            item.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int adapterPos = getAdapterPosition();
            int id = movies[adapterPos].getMovieId();
            String title = movies[adapterPos].getMovieTitle();
            String imageUrl = movies[adapterPos].getMovieImageUrl();
            String synopsis = movies[adapterPos].getSynopsis();
            double rating = movies[adapterPos].getUserRating();
            String release = movies[adapterPos].getReleaseDate();

            movieAdapterOnClickHadler.onItemClickListener(id, title, imageUrl, synopsis, rating, release);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context cont = parent.getContext();
        int itemLayout = R.layout.activity_movie_item;
        LayoutInflater inflater = LayoutInflater.from(cont);
        View view = inflater.inflate(itemLayout, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        String path = BASE_URL_IMAGE + movies[position].getMovieImageUrl();
        Picasso.get()
                .load(path)
                .placeholder(R.mipmap.icon)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        if (null == movies){
            return 0;
        }else{
            return movies.length;
        }
    }

    public void setMovieData(Movie[] data){
        movies = data;
        notifyDataSetChanged();
    }
}
