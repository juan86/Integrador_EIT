package org.educacionIt.model.domain;

public class MovieGenre {
    private int id;
    private String name;
    private String nameEs;



    public MovieGenre(int id, String name, String nameEs) {
        this.id = id;
        this.name = name;
        this.nameEs = nameEs;
    }

    public MovieGenre(String name, String nameEs) {
        this.name = name;
        this.nameEs = nameEs;
    }

    public int getId() {return id;}

    public String getName() {return name;}

    public String getNameEs() {return nameEs;}

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameEs(String nameEs) {
        this.nameEs = nameEs;
    }

    @Override
    public String toString() {
        return "id : "+id+'\n'+
                "Name : "+name+'\n'+
                "Espanish name: "+nameEs+"\n";
    }
}

