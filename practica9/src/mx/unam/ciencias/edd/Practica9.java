package mx.unam.ciencias.edd;

import java.util.Random;

/**
 * Práctica 9: Picadillos.
 */
public class Practica9 {

    public static void main(String[] args) {

        Random random = new Random();

        String[] letras = {
            "a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p",
            "q", "r", "s", "t", "u", "v", "w", "x",
            "y", "z"
        };

        int n = 10 + random.nextInt(40);

        String mensaje = "";
        for (int i = 0; i < n; i++)
            mensaje += letras[random.nextInt(letras.length)];

        System.out.println("Mensaje: " + mensaje);
        Picadillo<String> bj =
            FabricaPicadillos.getInstancia(AlgoritmoPicadillo.BJ_STRING);
        System.out.printf("BJ  : 0x%08x\n", bj.picadillo(mensaje));
        Picadillo<String> glib =
            FabricaPicadillos.getInstancia(AlgoritmoPicadillo.GLIB_STRING);
        System.out.printf("GLib: 0x%08x\n", glib.picadillo(mensaje));
        Picadillo<String> xor =
            FabricaPicadillos.getInstancia(AlgoritmoPicadillo.XOR_STRING);
        System.out.printf("XOR : 0x%08x\n", xor.picadillo(mensaje));
    }
}
