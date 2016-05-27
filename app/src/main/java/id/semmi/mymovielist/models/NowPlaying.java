package id.semmi.mymovielist.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Semmiverian on 5/26/16.
 */
public class NowPlaying{
    private String page;
    @SerializedName("results")
    private List<Movies> nowPlayingResults;

    public NowPlaying() {
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<Movies> getNowPlayingResults() {
        return nowPlayingResults;
    }

    public void setNowPlayingResults(List<Movies> nowPlayingResults) {
        this.nowPlayingResults = nowPlayingResults;
    }


}
