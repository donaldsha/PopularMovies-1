package db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Min-Pc on 7/24/2018.
 */

@Dao
public interface FavDao {

    @Query("SELECT * FROM favourites")
    LiveData<List<FavouriteData>> loadAllFavourites();

    @Query("SELECT * FROM favourites where id = :movieId")
    FavouriteData getFavouriteByMovieId(int movieId);

    @Insert
    void insertFavourite(FavouriteData favouriteData);

    @Delete
    void deleteFavourites(FavouriteData favouriteData);
}
