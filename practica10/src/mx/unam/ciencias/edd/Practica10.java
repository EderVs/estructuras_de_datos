package mx.unam.ciencias.edd;

import java.text.NumberFormat;
import java.util.Random;

/**
 * Práctica 10: Diccionarios.
 */
public class Practica10 {

    /* Imprime el uso del programa y lo termina. */
    private static void uso() {
        System.err.println("Uso: java -jar practica10.jar N");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1)
            uso();

        int N = -1;
        try {
            N = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            uso();
        }

        Random random = new Random();
        long tiempoInicial, tiempoTotal;
        NumberFormat nf = NumberFormat.getIntegerInstance();

        int[] arreglo = new int[N];
        for (int i = 0; i < N; i++)
            arreglo[i] = random.nextInt(N);

        ArbolBinarioOrdenado<Integer> abo = new ArbolBinarioOrdenado<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            abo.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un árbol " +
                          "binario ordenado con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        ArbolRojinegro<Integer> arn = new ArbolRojinegro<Integer>();
        for (int i = 0; i < N; i++)
            arn.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un árbol " +
                          "rojinegro con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        ArbolAVL<Integer> avl = new ArbolAVL<Integer>();
        for (int i = 0; i < N; i++)
            avl.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un árbol " +
                          "AVL con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        Diccionario<Integer, Integer> dicc = new Diccionario<Integer, Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            dicc.agrega(arreglo[i], arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un diccionario " +
                          "con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        Conjunto<Integer> conjunto = new Conjunto<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            conjunto.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un conjunto " +
                          "con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
    }
}
