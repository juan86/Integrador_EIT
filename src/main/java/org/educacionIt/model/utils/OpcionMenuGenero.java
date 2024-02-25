package org.educacionIt.model.utils;

import org.educacionIt.dao.imp.GenreDao;
import org.educacionIt.model.domain.MovieGenre;

import java.util.List;
import java.util.Scanner;

public class OpcionMenuGenero {
    private Scanner input;

    public OpcionMenuGenero(Scanner input){
        this.input = input;
    }

    public void menu(){
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
            PrintMenu.print(titulo, opciones);
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

    private void addGenreOption(){
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

    private void deleteGenreOption(){
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

    private void searchGenreByIdOption(){
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

    private void modifyGenreByIdOption(){
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
}
