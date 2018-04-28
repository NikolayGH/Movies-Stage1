package ru.prog_edu.movies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/*Здесь имплементировал Parcelable так как по скорости он значительно
  превосходит Serializable при передаче объектов между Activity*/

public class Movie implements Parcelable{
    private int voteCount;
    private int id;
    private boolean video;
    private double voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String originalLanguage;
    private List<Integer> genreIDs;
    private String backdropPath;
    private boolean adult;
    private String overview;
    private String releaseDate;

    public Movie(Parcel source) {
        this.voteCount = source.readInt();
        this.id = source.readInt();
        this.video = source.readByte()!= 0;
        this.voteAverage = source.readDouble();
        this.title = source.readString();
        this.popularity = source.readDouble();
        this.posterPath = source.readString();
        this.originalLanguage = source.readString();
        this.genreIDs = new ArrayList<>();
        source.readList(genreIDs, Integer.class.getClassLoader());
        this.backdropPath = source.readString();
        this.adult = source.readByte()!= 0;
        this.overview = source.readString();
        this.releaseDate = source.readString();

    }

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(voteCount);
        dest.writeInt(id);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeDouble(voteAverage);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeString(posterPath);
        dest.writeString(originalLanguage);
        dest.writeList(genreIDs);
        dest.writeString(backdropPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }

    public final static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
