package org.educacionIt.model.utils;

import org.educacionIt.dao.imp.MovieDao;
import org.educacionIt.model.domain.Movie;

import java.util.List;
import java.util.Scanner;

public class OpcionMenuBusqueda {
    private Scanner input;

    public OpcionMenuBusqueda( Scanner input){
        this.input = input;
    }

    public void searchByTitle(){
        System.out.print("Ingrese el Nombre del titulo de la Pelicula: ");
        String title = input.nextLine();
        MovieDao movieDao = new MovieDao();
        List<Movie> movies = movieDao.getByProperty("title", new Movie(title, "", null));
        List<String> listMovieName = movies.stream().map(Movie::getTitle).toList();
        PrintMenu.print("Peliculas con Titulo: "+title, listMovieName);
        System.out.print("Ingrese el numero de la pelicula a ver: ");
        int movieSelected = input.nextInt();
        if(!listMovieName.isEmpty()){
            if(movieSelected > 0 && movieSelected <= listMovieName.size()){
                System.out.println(movies.get(movieSelected - 1));
            }
        }else{
            System.out.println("No existe peliculas con el nombre: "+title);
        }
    }

    public void searchByGenre(){
        System.out.print("Ingrese el genero de la pelicula a buscar: ");
        String genreName = input.nextLine();
        MovieDao movieDao = new MovieDao();
        List<Movie> movies = movieDao.getMovieForGenre(genreName);
        List<String> listMovieName = movies.stream().map(Movie::getTitle).toList();
        PrintMenu.print("Peliculas con Genero: "+genreName, listMovieName);
        System.out.print("Ingrese el numero de la pelicula a ver: ");
        int movieSelected = input.nextInt();
        if(!listMovieName.isEmpty()){
            if(movieSelected > 0 && movieSelected <= listMovieName.size()){
                System.out.println(movies.get(movieSelected - 1));
            }
        }else{
            System.out.println("No hay peliculas con el genero: "+genreName);
        }
    }

}
