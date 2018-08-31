package adapters;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import db.FavouriteData;
import db.FavouriteDatabase;

/**
 * Created by Min-Pc on 7/25/2018.
 */

public class MainViewModels extends AndroidViewModel{

    private static final String LOG_TAG = MainViewModels.class.getSimpleName();
    private LiveData<List<FavouriteData>> favData;

    public MainViewModels(@NonNull Application application) {
        super(application);

        Log.d(LOG_TAG, "call loadAllFavourites");
        FavouriteDatabase favdatabase = FavouriteDatabase.getInstance(this.getApplication());
        favData = favdatabase.favDao().loadAllFavourites();
    }

    public LiveData<List<FavouriteData>> getFavourites(){
        return this.favData;
    }
}
