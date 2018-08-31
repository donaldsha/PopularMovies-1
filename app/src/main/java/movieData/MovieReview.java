package movieData;

/**
 * Created by Min-Pc on 7/24/2018.
 */

public class MovieReview {

    private String author;
    private String url;
    private String content;

    public MovieReview(String author, String content, String url){
        this.author = author;
        this.content = content;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
