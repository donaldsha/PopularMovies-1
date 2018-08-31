package db;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

/**
 * Created by Min-Pc on 7/23/2018.
 */

@Database(entities = {FavouriteData.class}, version = 1, exportSchema = false)
public abstract class FavouriteDatabase extends RoomDatabase{

    private static final Object LOCK = new Object();
    private static final String LOG_TAG = FavouriteDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "favouritedb";

    private static FavouriteDatabase instance;

    public static FavouriteDatabase getInstance(Context context){
        if (instance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        FavouriteDatabase.class,
                        FavouriteDatabase.DATABASE_NAME).build();
            }
        }

        Log.d(LOG_TAG, "Getting database instance");
        return instance;
    }

    public abstract FavDao favDao();

}
