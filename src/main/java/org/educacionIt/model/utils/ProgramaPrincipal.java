package org.educacionIt.model.utils;

import java.util.List;
import java.util.Scanner;
import java.io.File;

public class ProgramaPrincipal {
    private static Scanner input = new Scanner(System.in);
    private static OpcionMenuPeliculas opcionPeliculas = new OpcionMenuPeliculas(input);
    private static OpcionMenuGenero opcionGenero = new OpcionMenuGenero(input);
    private static OpcionMenuBusqueda opcionBusqueda= new OpcionMenuBusqueda(input);

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
            PrintMenu.print(titulo, opciones);
            System.out.print("Ingrese una opcion: ");
            int inputOption = input.nextInt();
            input.nextLine(); // Clear buffer
            switch (inputOption){
                case 1:
                    opcionPeliculas.menu();
                    break;
                case 2:
                    opcionGenero.menu();
                    break;
                case 3:
                    opcionBusqueda.searchByTitle();
                    break;
                case 4:
                    opcionBusqueda.searchByGenre();
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




    private static boolean isValidImage(String path){

        return true;
    }

    public static String getRutaAbsolutaImagenes(){
        File carpeta = new File(System.getProperty("user.dir"), "imagenes");
        return carpeta.getAbsolutePath();
    }
}
