package id.semmi.mymovielist.models;

/**
 * Created by Semmiverian on 6/2/16.
 */
public class Review {
    private String id;
    private String author;
    private String content;

    public Review() {
    }

    public Review(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
