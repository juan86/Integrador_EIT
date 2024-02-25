package org.educacionIt.model.utils;

import org.educacionIt.dao.imp.GenreDao;
import org.educacionIt.dao.imp.MovieDao;
import org.educacionIt.model.domain.Movie;
import org.educacionIt.model.domain.MovieGenre;

import java.util.List;
import java.util.Scanner;
import java.io.File;

public class ProgramaPrincipal {
    private static Scanner input = new Scanner(System.in);
    public static void initial(){
        boolean continueRunning = true;
        String titulo = "Administracion de Peliculas";
        List<String> opciones = List.of(
                "Administrar Peliculas.",
                "Administrar Genero de Peliculas.",
                "Buscar Pelicula por titulo.",
                "Buscar Pelicula por genero.",
                "Salir."
        );
        while (continueRunning){
            printMenu(titulo, opciones);
            System.out.print("Ingrese una opcion: ");
            int inputOption = input.nextInt();
            input.nextLine(); // Clear buffer
            switch (inputOption){
                case 1:
                    option1();
                    break;
                case 2:
                    option2();
                    break;
                case 3:
                    option3();
                    break;
                case 4:
                    option4();
                    break;
                case 5:
                    continueRunning = false;
                    System.out.println("Hasta pronto...");
                    break;
                default:
                    System.out.println("Valor invalido...");
            }
        }

    }

    public static void option1(){
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
            printMenu(titulo, opciones);
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
    public static void addMovieOption(){
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
    public static void deleteMovieOption(){
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
    public static void searchMovieByIdOption(){
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
    public static void modifyMovieByIdOption(){
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
    private static void modifyGenre(Movie movie){
        boolean continueRunning = true;
        String titulo = "Modificacion de Genero de Pelicula";
        List<String> opciones = List.of(
                "Agregar Genero.",
                "Eliminar Genero.",
                "Salir."
        );
        do{
            printMenu(titulo, opciones);
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
    private static void deleteMovieGenre(Movie movie){
        List<MovieGenre> listMovieGenres = movie.getGenres();
        List<String> genreNames = new java.util.ArrayList<>(listMovieGenres.stream().map(MovieGenre::getName).toList());
        genreNames.add("Ninguno");

        if(genreNames.size() > 1){
            printMenu("Generos de la pelicula: "+movie.getTitle(), genreNames);
            System.out.print("Seleccione el genero a eliminar: ");
            int selectedGenre = input.nextInt();
            if(selectedGenre != genreNames.size()){
                if(selectedGenre > 0 && selectedGenre <= genreNames.size()){
                    listMovieGenres.remove(selectedGenre - 1);
                }
            }
        }
    }
    public static void listMovies(){
        MovieDao movieDao = new MovieDao();
        List<Movie> movies = movieDao.getAll();
        List<String> listIdAndNameMovies = movies.stream().map(movie -> "( "+movie.getCode()+" ) - ( "+movie.getTitle()+" )").toList();
        printMenu("( ID ) - ( Nombre de pelicula )", listIdAndNameMovies);
    }
    public static MovieGenre getGenreForAdd(){
        MovieGenre movieGenre = null;
        GenreDao genreDao = new GenreDao();
        List<MovieGenre> movieGenres = genreDao.getAll();
        List<String> genreNames = new java.util.ArrayList<>(movieGenres.stream().map(MovieGenre::getName).toList());
        genreNames.add("Ninguno");

        if(genreNames.size() > 0){
            printMenu("Generos Cargados", genreNames);
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

    public static void option2(){
        boolean option2Running = true;
        String titulo = "ABM Genero Peliculas";
        List<String> opciones = List.of(
                "Alta Genero.",
                "Baja Genero por ID.",
                "Buscar Genero por ID.",
                "Modificar Genero por ID.",
                "<- Menu Principal."
        );
        while (option2Running){
            printMenu(titulo, opciones);
            System.out.println("Ingrese una opcion:");
            int inputOption = input.nextInt();
            switch (inputOption){
                case 1:
                    addGenreOption();
                    break;
                case 2:
                    deleteGenreOption();
                    break;
                case 3:
                    searchGenreByIdOption();
                    break;
                case 4:
                    modifyGenreByIdOption();
                    break;
                case 5:
                    option2Running = false;
                    break;
                default:
                    System.out.println("Invalid input...");
            }
        }
    }
    public static void addGenreOption(){
        System.out.println("Alta Genero de Peliculas");
        System.out.print("Ingrese el nombre del genero en idioma ingles: ");
        input.nextLine(); // Clear buffer
        String genreNameEn = input.nextLine();
        System.out.print("Ingrese el nombre del genero en idioma español: ");
        String genreNameEs = input.nextLine();
        GenreDao genreDao =  new GenreDao();
        genreDao.insert(new MovieGenre(genreNameEn, genreNameEs));
        System.out.println("Se agrego un nuevo genero.");
    }
    public static void deleteGenreOption(){
        System.out.println("Eliminar genero.");
        System.out.print("Ingrese el Id del genero que quiere eliminar: ");
        int genreId = input.nextInt();
        GenreDao genreDao = new GenreDao();
        MovieGenre movieGenre = genreDao.searchById(genreId);
        if(movieGenre != null) {
            genreDao.delete(movieGenre);
        }else{
            System.out.println("No se encontro el genro con el ID: "+genreId);
        }
    }
    public static void searchGenreByIdOption(){
        System.out.println("Buscar Genero por id");
        System.out.println("ingrese el id del genero a buscar: ");
        int genreId = input.nextInt();
        GenreDao genreDao = new GenreDao();
        MovieGenre movieGenre = genreDao.searchById(genreId);
        if(movieGenre != null){
            System.out.println("Genero encontrado: ");
            System.out.println(movieGenre);
        }else{
            System.out.println("No se encontro ningun genero con el id: "+genreId);
        }
    }
    public static void modifyGenreByIdOption(){
        System.out.println("Modificar Género por ID");
        System.out.println("Ingrese el ID del género a modificar: ");
        int genreId = input.nextInt();
        input.nextLine(); // Clear buffer

        GenreDao genreDao = new GenreDao();
        MovieGenre movieGenre = genreDao.searchById(genreId);

        if(movieGenre != null){
            System.out.println("Nombre del género en idioma Inglés (no ingrese nada si no quiere modificar): ");
            String nameEn = input.nextLine();
            System.out.println("Nombre del género en idioma Español (no ingrese nada si no quiere modificar): ");
            String nameEs = input.nextLine();

            if (!nameEn.isEmpty()) {
                movieGenre.setName(nameEn.toUpperCase());
            }
            if (!nameEs.isEmpty()) {
                movieGenre.setNameEs(nameEs.toUpperCase());
            }

            genreDao.update(movieGenre);
            System.out.println("El género se modificó correctamente...");
        }else {
            System.out.println("No se encontro un genero con el id: "+ genreId);
        }
    }

    public static void option3(){
        System.out.print("Ingrese el Nombre del titulo de la Pelicula: ");
        String title = input.nextLine();
        MovieDao movieDao = new MovieDao();
        List<Movie> movies = movieDao.getByProperty("title", new Movie(title, "", null));
        List<String> listMovieName = movies.stream().map(Movie::getTitle).toList();
        printMenu("Peliculas con Titulo: "+title, listMovieName);
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
    public static void option4(){
        System.out.print("Ingrese el genero de la pelicula a buscar: ");
        String genreName = input.nextLine();
        MovieDao movieDao = new MovieDao();
        List<Movie> movies = movieDao.getMovieForGenre(genreName);
        List<String> listMovieName = movies.stream().map(Movie::getTitle).toList();
        printMenu("Peliculas con Genero: "+genreName, listMovieName);
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
    public static void printMenu(String titulo, List<String> cuerpo) {
        // Caracteres utilizados para el cuadro
        char esquinaSuperiorIzquierda = '╔';
        char esquinaSuperiorDerecha = '╗';
        char esquinaInferiorIzquierda = '╚';
        char esquinaInferiorDerecha = '╝';
        char bordeHorizontal = '═';
        char bordeVertical = '║';
        char bordeVerticalIzquierdo = '╠';
        char bordeVerticalderecho = '╣';

        int anchoMaximo =  obtenerTextoMasLargo(titulo, cuerpo);

        int altura = cuerpo.size() + 4;
        int ancho = anchoMaximo + 7;

        if (altura < 2 || ancho < 2) {
            System.out.println("El cuadro es demasiado pequeño.");
            return;
        }

        // Imprimir la esquina superior izquierda
        System.out.print(esquinaSuperiorIzquierda);

        // Imprimir la parte superior del cuadro
        for (int i = 0; i < ancho - 2; i++) {
            System.out.print(bordeHorizontal);
        }

        // Imprimir la esquina superior derecha
        System.out.println(esquinaSuperiorDerecha);

        // Imprimir el título centrado
        System.out.println(bordeVertical + centerText(titulo, ancho - 2) + bordeVertical);

        // Imprimir la línea horizontal de igual
        System.out.print(bordeVerticalIzquierdo);
        for (int i = 0; i < ancho - 2; i++) {
            System.out.print(bordeHorizontal);
        }
        System.out.println(bordeVerticalderecho);

        // Imprimir líneas del cuerpo numeradas
        for (int i = 0; i < cuerpo.size(); i++) {
            String lineaNumerada = (i + 1) + ". " + cuerpo.get(i);
            System.out.println(bordeVertical + " " + lineaNumerada + agregarEspacios(ancho - 3 - lineaNumerada.length()) + bordeVertical);
        }

        // Imprimir la esquina inferior izquierda
        System.out.print(esquinaInferiorIzquierda);

        // Imprimir la parte inferior del cuadro
        for (int i = 0; i < ancho - 2; i++) {
            System.out.print(bordeHorizontal);
        }

        // Imprimir la esquina inferior derecha
        System.out.println(esquinaInferiorDerecha);
    }

    public static String centerText(String texto, int ancho) {
        int espacios = (ancho - texto.length()) / 2;
        StringBuilder resultado = new StringBuilder();

        resultado.append(" ".repeat(Math.max(0, espacios)));

        resultado.append(texto);

        while (resultado.length() < ancho) {
            resultado.append(" ");
        }

        return resultado.toString();
    }

    public static String agregarEspacios(int cantidad) {
        return " ".repeat(Math.max(0, cantidad));
    }

    public static int obtenerTextoMasLargo(String titulo, List<String> cuerpo) {
        int anchoMaximo = titulo.length();

        for (String linea : cuerpo) {
            if (linea.length() > anchoMaximo) {
                anchoMaximo = linea.length();
            }
        }
        return anchoMaximo;
    }

    private static boolean isValidImage(String path){

        return true;
    }

    public static String getRutaAbsolutaImagenes(){
        File carpeta = new File(System.getProperty("user.dir"), "imagenes");
        return carpeta.getAbsolutePath();
    }
}
