package org.educacionIt.model.domain;

import java.util.ArrayList;
import java.util.List;


public class Movie {
    private int code;
    private String title;
    private String url;
    private String image;
    private List<MovieGenre> genres;

    public Movie(String title, String url, String image) {
        this.title = title;
        this.url = url;
        this.image = image;
        this.genres = new ArrayList<>();
    }

    public Movie(int code, String title, String url, String image) {
        this.code = code;
        this.title = title;
        this.url = url;
        this.image = image;
        this.genres = new ArrayList<>();
    }

    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public void setCode(int code){this.code = code;}

    public void setTitle(String title){this.title = title;}

    public void setUrl(String url) {this.url = url;}

    public void setImage(String image) {this.image = image;}

    public void addGenres(MovieGenre genres) {this.genres.add(genres);}

    public List<MovieGenre> getGenres() {
        return genres;
    }

    @Override
    public String toString() {
        return "Code:    "+ code  +"\n"+
                "Title:  "+ title + '\n' +
                "Url:    "+ url   + '\n' +
                "Genres: "+genres +'\n';
    }
}
