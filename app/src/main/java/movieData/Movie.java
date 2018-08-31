package movieData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Min-Pc on 7/20/2018.
 */

public class Movie implements Parcelable {

    public static final String EXTRA_NAME_MOVIEDATA = "movie_data";
    private int id;
    private String title;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private double voteAverage;

    public Movie(int movieId, String movieTitle,String releaseDate,String movieImageUrl, double userRating, String synopsis){
        this.id = movieId;
        this.title = movieTitle;
        this.releaseDate = releaseDate;
        this.posterPath = movieImageUrl;
        this.voteAverage = userRating;
        this.overview = synopsis;
    }

    public int getMovieId() {
        return id;
    }

    public void setMovieId(int movieId) {
        this.id = movieId;
    }

    public String getMovieTitle() {
        return title;
    }

    public void setMovieTitle(String movieTitle) {
        this.title = movieTitle;
    }

    public String getMovieImageUrl() {
        return posterPath;
    }

    public void setMovieImageUrl(String movieImageUrl) {
        this.posterPath = movieImageUrl;
    }

    public String getSynopsis() {
        return overview;
    }

    public void setSynopsis(String synopsis) {
        this.overview = synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getUserRating() {
        return voteAverage;
    }

    public void setUserRating(double userRating) {
        this.voteAverage = userRating;
    }

    private Movie(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
    }

    private void readFromParcel(Parcel out){
        id = out.readInt();
        title = out.readString();
        voteAverage = out.readDouble();
        overview = out.readString();
        posterPath = out.readString();
        releaseDate = out.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
