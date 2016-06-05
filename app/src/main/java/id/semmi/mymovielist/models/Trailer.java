package id.semmi.mymovielist.models;

/**
 * Created by Semmiverian on 6/2/16.
 */
public class Trailer {
    private String key;
    private String name;

    public Trailer() {
    }

    public String getKey() {
        return key;

    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Trailer(String key, String name) {

        this.key = key;
        this.name = name;
    }
}
