package org.educacionIt.model.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Movie {
    private int code;
    private String title;
    private String url;
    private File image;
    private List<MovieGenre> genres;

    public Movie(int code, String title, String url, File image) {
        this.code = code;
        this.title = title;
        this.url = url;
        this.image = image;
        this.genres = new ArrayList<>();
    }

    public Movie(String title, String url, File image) {
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

    public File getImage() {
        return image;
    }

    public void setCode(int code){this.code = code;}

    public void setTitle(String title){this.title = title;}

    public void setUrl(String url) {this.url = url;}

    public void setImage(File image) {this.image = image;}

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
