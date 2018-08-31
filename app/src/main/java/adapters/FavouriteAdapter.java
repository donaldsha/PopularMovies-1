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

/**
 * Created by Min-Pc on 7/24/2018.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>{

    private List<FavouriteData> favouriteData;
    private FavouriteAdapter.ItemClickListener itemClickListener;
    private Context context;
    private LayoutInflater inflater;

    public FavouriteAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;
        TextView text;

        FavouriteViewHolder(View view){
            super(view);
            text = (TextView)view.findViewById(R.id.textView_favourite_title);
            image = (ImageView)view.findViewById(R.id.imageView_favourite_poster);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (itemClickListener != null){
                itemClickListener.onItemClickListener(view, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener{
        void onItemClickListener(View view, int index);
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_favourite_item, parent, false);

        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, int position) {
        FavouriteData favData = favouriteData.get(position);
        String title = favData.getTitle();
        String poster_path = favData.getPoster_path();
        holder.text.setText(title);

        URL imageUrl = NetworkUtilities.buildPosterUrl(poster_path);
        Picasso.get().load(imageUrl.toString())
                .placeholder(R.mipmap.icon)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        if (favouriteData == null){
            return 0;
        }
        return favouriteData.size();
    }

    public void setFavourites(List<FavouriteData> favouriteData){
        this.favouriteData = favouriteData;
        notifyDataSetChanged();
    }

    public List<FavouriteData> getFavourites(){
        return this.favouriteData;
    }

    public FavouriteData getFavData(int id){
        return favouriteData.get(id);
    }

    public void setClickListener(FavouriteAdapter.ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
