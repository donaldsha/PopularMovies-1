package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmoviesstage1.R;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import db.FavouriteData;
import jSonUtilities.NetworkUtilities;
import movieData.Movie;

/**
 * Created by Min-Pc on 7/24/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Movie> movie;
    private ItemClickListener itemClickListener;
    private Context context;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Movie> movie){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.movie = movie;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;


        ViewHolder(View view){
            super(view);
            image = (ImageView)view.findViewById(R.id.imageView_item_image);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener{
        void onItemClick(View view, int index);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_recycleview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        URL imageUrl = NetworkUtilities.buildPosterUrl(movie.get(position).getMovieImageUrl());
        Picasso.get().load(imageUrl.toString())
                .placeholder(R.mipmap.icon)
                .error(R.mipmap.error)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        if (movie == null){
            return 0;
        }
        return movie.size();
    }

    public Movie getItem(int id){
        return movie.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
