package movieData;

/**
 * Created by Min-Pc on 7/24/2018.
 */

public class MovieTrailer {

    private String name;
    private String site;
    private String type;
    private String key;

   public MovieTrailer(String key, String name, String site, String type){
        this.key = key;
        this.site = site;
        this.name = name;
        this.type = type;
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
