package org.educacionIt.model.utils;

import java.util.List;

public class PrintMenu {
    public static void print(String titulo, List<String> cuerpo) {
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

    private static String centerText(String texto, int ancho) {
        int espacios = (ancho - texto.length()) / 2;
        StringBuilder resultado = new StringBuilder();

        resultado.append(" ".repeat(Math.max(0, espacios)));

        resultado.append(texto);

        while (resultado.length() < ancho) {
            resultado.append(" ");
        }

        return resultado.toString();
    }

    private static String agregarEspacios(int cantidad) {
        return " ".repeat(Math.max(0, cantidad));
    }

    private static int obtenerTextoMasLargo(String titulo, List<String> cuerpo) {
        int anchoMaximo = titulo.length();

        for (String linea : cuerpo) {
            if (linea.length() > anchoMaximo) {
                anchoMaximo = linea.length();
            }
        }
        return anchoMaximo;
    }
}
