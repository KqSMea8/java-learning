package melon.web.model;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "movie")
public class Movie {
    @Id
    private String id;
    private String title;
    private String url;
    private String directors;
    private String casts;
    private String cover;
    private Integer rate;
    private Integer star;

}
