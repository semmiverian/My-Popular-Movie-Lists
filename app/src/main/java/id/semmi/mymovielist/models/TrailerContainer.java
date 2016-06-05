package id.semmi.mymovielist.models;

import java.util.List;

/**
 * Created by Semmiverian on 6/2/16.
 */
public class TrailerContainer {
    private String id;
    private List<Trailer> results;

    public TrailerContainer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
