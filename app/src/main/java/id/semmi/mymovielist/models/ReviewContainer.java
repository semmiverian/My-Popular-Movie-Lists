package id.semmi.mymovielist.models;

import java.util.List;

/**
 * Created by Semmiverian on 6/2/16.
 */
public class ReviewContainer {
    private String id;
    private List<Review> results;

    public ReviewContainer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
