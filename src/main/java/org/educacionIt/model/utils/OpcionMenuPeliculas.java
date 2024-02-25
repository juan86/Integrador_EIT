package org.educacionIt.model.utils;

import org.educacionIt.dao.imp.GenreDao;
import org.educacionIt.dao.imp.MovieDao;
import org.educacionIt.model.domain.Movie;
import org.educacionIt.model.domain.MovieGenre;

import java.util.List;
import java.util.Scanner;

public class OpcionMenuPeliculas {
    private Scanner input;

    public OpcionMenuPeliculas(Scanner input){
        this.input = input;
    }

    public void menu(){

        boolean option1Running = true;
        String titulo = "ABM Peliculas";
        List<String> opciones = List.of(
                "Alta Pelicula.",
                "Baja Pelicula por ID.",
                "Buscar pelicula por ID.",
                "Modificar Pelicula por ID.",
                "Listar Peliculas.",
                "<- Menu Principal."
        );

        while (option1Running){
            PrintMenu.print(titulo, opciones);
            System.out.print("Ingrese una opcion: ");
            int inputOption = input.nextInt();
            input.nextLine(); // Clear buffer
            switch (inputOption){
                case 1:
                    addMovieOption();
                    break;
                case 2:
                    deleteMovieOption();
                    break;
                case 3:
                    searchMovieByIdOption();
                    break;
                case 4:
                    modifyMovieByIdOption();
                    break;
                case 5:
                    listMovies();
                    break;
                case 6:
                    option1Running = false;
                    break;
                default:
                    System.out.println("Invalid input...");
            }
        }
    }

    private void addMovieOption(){
        System.out.println("Alta pelicula.\n");
        System.out.print("Ingrese el nombre de la pelicula: ");
        String nameMovie = input.next();
        System.out.print("URL: ");
        String urlMovie = input.next();
        Movie newMovie = new Movie(nameMovie, urlMovie, null);
        boolean addNewGenre = true;
        do{
            System.out.println("Deseas asignarle generos a la pelicula? Y/N: ");
            String inputQuestion = input.next();
            if(inputQuestion.equalsIgnoreCase("y")){
                MovieGenre selectedGenre = getGenreForAdd();
                if(selectedGenre != null) newMovie.addGenres(selectedGenre);
            }else {
                addNewGenre = false;
            }
        }while(addNewGenre);
        if(newMovie != null){
            MovieDao movieDao = new MovieDao();
            movieDao.insert(newMovie);
            System.out.println("Se guardo correctamente la pelicula: "+newMovie);
        }
    }

    private void deleteMovieOption(){
        System.out.println("Eliminar Pelicula por ID");
        System.out.print("Ingrese el ID de la pelicula que desea Eliminar: ");
        int movieId = input.nextInt();
        MovieDao movieDao = new MovieDao();
        Movie movieFound = movieDao.searchById(movieId);
        if(movieFound != null){
            movieDao.delete(movieFound);
            System.out.println("Se elimino correctamente la pelicula:");
            System.out.println(movieFound);
        }else {
            System.out.println("No existe ninguna pelicula con el id: "+movieId);
        }
    }

    private void searchMovieByIdOption(){
        System.out.println("Buscar pelicula por id ");
        System.out.print("Ingrese el id de la pelicula que desea buscar: ");
        int movieId = input.nextInt();
        MovieDao movieDao = new MovieDao();
        Movie movieFound = movieDao.searchById(movieId);
        if(movieFound != null){
            System.out.println("Pelicula encontrada:");
            System.out.println(movieFound);
        }else{
            System.out.println("No se encuentra ninguna Pelicula con el id: "+movieId);
        }
    }

    private void modifyMovieByIdOption(){
        System.out.println("Modificar pelicula por id");
        int movieId = input.nextInt();
        input.nextLine(); // Clear buffer
        MovieDao movieDao = new MovieDao();
        Movie movieFound = movieDao.searchById(movieId);
        if(movieFound != null){
            System.out.print("Ingrese el nombre de la Pelicula (no ingrese nada si no quiere modificar): ");
            String movieName = input.nextLine();
            System.out.println("Ingrese la url de la Pelicula (no ingrese nada si no quiere modificar): ");
            String movieUrl = input.nextLine();
            System.out.print("Desea modificar el genero de l apelicula? (S/N)");
            Movie movieModify = new Movie(movieName, movieUrl, null);
            String condition = input.next();
            movieFound.setTitle(movieName);
            movieFound.setUrl(movieUrl);
            if(condition.equalsIgnoreCase("S")){
                modifyGenre(movieFound);
            }
            movieDao.update(movieFound);
        }else{
            System.out.println("No existe la Pelicula con el id: "+movieId);
        }
    }

    private void modifyGenre(Movie movie){
        boolean continueRunning = true;
        String titulo = "Modificacion de Genero de Pelicula";
        List<String> opciones = List.of(
                "Agregar Genero.",
                "Eliminar Genero.",
                "Salir."
        );
        do{
            PrintMenu.print(titulo, opciones);
            System.out.println("Ingrese una opcion:");
            int inputOption = input.nextInt();
            switch (inputOption){
                case 1:
                    MovieGenre newMovieGenre = getGenreForAdd();
                    if(newMovieGenre != null) movie.addGenres(newMovieGenre);
                    break;
                case 2:
                    deleteMovieGenre(movie);
                    break;
                case 3:
                    continueRunning = false;
                    break;
                default:
                    System.out.println("Valor invalido...");
            }

        }while (continueRunning);
    }

    private void deleteMovieGenre(Movie movie){
        List<MovieGenre> listMovieGenres = movie.getGenres();
        List<String> genreNames = new java.util.ArrayList<>(listMovieGenres.stream().map(MovieGenre::getName).toList());
        genreNames.add("Ninguno");

        if(genreNames.size() > 1){
            PrintMenu.print("Generos de la pelicula: "+movie.getTitle(), genreNames);
            System.out.print("Seleccione el genero a eliminar: ");
            int selectedGenre = input.nextInt();
            if(selectedGenre != genreNames.size()){
                if(selectedGenre > 0 && selectedGenre <= genreNames.size()){
                    listMovieGenres.remove(selectedGenre - 1);
                }
            }
        }
    }

    private void listMovies(){
        MovieDao movieDao = new MovieDao();
        List<Movie> movies = movieDao.getAll();
        List<String> listIdAndNameMovies = movies.stream().map(movie -> "( "+movie.getCode()+" ) - ( "+movie.getTitle()+" )").toList();
        PrintMenu.print("( ID ) - ( Nombre de pelicula )", listIdAndNameMovies);
    }

    private MovieGenre getGenreForAdd(){
        MovieGenre movieGenre = null;
        GenreDao genreDao = new GenreDao();
        List<MovieGenre> movieGenres = genreDao.getAll();
        List<String> genreNames = new java.util.ArrayList<>(movieGenres.stream().map(MovieGenre::getName).toList());
        genreNames.add("Ninguno");

        if(genreNames.size() > 0){
            PrintMenu.print("Generos Cargados", genreNames);
            System.out.println("Seleccione el genero a agregar: ");
            int selectedGenre = input.nextInt();
            if(selectedGenre != genreNames.size()){
                if(selectedGenre > 0 && selectedGenre <= genreNames.size()){
                    movieGenre = movieGenres.get(selectedGenre - 1);
                }
            }
        }
        return movieGenre;
    }
}
