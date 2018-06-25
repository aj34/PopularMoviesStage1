package com.sparkchicks.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

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

    private final String title;

    @SerializedName("vote_average")
    private final String userRating;

    @SerializedName("release_date")
    private final String releaseDate;

    private final String overview;

    @SerializedName("poster_path")
    private final String posterPath;

    public String getTitle() {
        return title;
    }
    public String getUserRating() {
        return userRating;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public String getOverview() {
        return overview;
    }
    public String getPosterPath() { return posterPath; }

    private Movie(Parcel in) {
        title = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        posterPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(userRating);
        parcel.writeString(releaseDate);
        parcel.writeString(overview);
        parcel.writeString(posterPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
