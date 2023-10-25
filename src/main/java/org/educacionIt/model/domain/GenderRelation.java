package org.educacionIt.model.domain;

public class GenderRelation {
    private int id;
    private int idMovie;
    private int idGender;

    public GenderRelation(int idMovie, int id_Gender) {
        this.idMovie = idMovie;
        this.idGender = id_Gender;
    }

    public GenderRelation(int id, int idMovie, int id_Gender) {
        this.id = id;
        this.idMovie = idMovie;
        this.idGender = id_Gender;
    }

    public int getId() {
        return id;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public int getIdGender() {
        return idGender;
    }
}

