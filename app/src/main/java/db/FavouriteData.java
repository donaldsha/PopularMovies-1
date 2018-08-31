package db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Min-Pc on 7/23/2018.
 */

@Entity(tableName = "favourites")
public class FavouriteData implements Parcelable{


    public static final String EXTRA_NAME_MOVIEDATA = "favourite_data";
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String title;
    private String poster_path;
    private String releaseDate;
    private String overview;
    private double rating;


    public FavouriteData(int id, String title, String poster_path, double rating, String releaseDate){
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.rating = rating;
        this.releaseDate = releaseDate;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    private FavouriteData(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<FavouriteData> CREATOR = new Creator<FavouriteData>() {
        @Override
        public FavouriteData createFromParcel(Parcel in) {
            return new FavouriteData(in);
        }

        @Override
        public FavouriteData[] newArray(int size) {
            return new FavouriteData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel out){
        id = out.readInt();
        title = out.readString();
        overview = out.readString();
        rating = out.readDouble();
        poster_path = out.readString();
        releaseDate = out.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeDouble(rating);
        parcel.writeString(poster_path);
        parcel.writeString(releaseDate);
    }
}
