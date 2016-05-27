package id.semmi.mymovielist.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Semmiverian on 5/26/16.
 */
public class Movies implements Parcelable{
    private String poster_path;
    private String overview;
    private String original_title;
    private String release_date;
    private String vote_average;

    public Movies() {
    }

    protected Movies(Parcel in) {
        poster_path = in.readString();
        overview = in.readString();
        original_title = in.readString();
        release_date = in.readString();
        vote_average = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

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

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(original_title);
        parcel.writeString(release_date);
        parcel.writeString(vote_average);
    }
}
