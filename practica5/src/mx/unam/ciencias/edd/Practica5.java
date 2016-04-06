package mx.unam.ciencias.edd;

import java.util.Random;
import java.text.NumberFormat;

/**
 * Práctica 5: Árboles AVL.
 */
public class Practica5 {

    public static void main(String[] args) {
        int N = 1000000; /* Un millón */
        Random random = new Random();
        NumberFormat nf = NumberFormat.getIntegerInstance();
        long tiempoInicial, tiempoTotal;

        int[] arreglo = new int[N];
        for (int i = 0; i < N; i++)
            arreglo[i] = random.nextInt();

        Integer[] qs = new Integer[N];
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            qs[i] = arreglo[i];
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un arreglo con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        Arreglos.quickSort(qs);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en ordenar un arreglo con %s elementos " +
                          "usando QuickSort.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        int b = qs[random.nextInt(N)];

        tiempoInicial = System.nanoTime();
        int idx = Arreglos.busquedaBinaria(qs, b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en encontrar un elemento en un arreglo " +
                          "con %s elementos usando búsqueda binaria.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        Lista<Integer> ms = new Lista<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            ms.agregaFinal(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en crear una lista con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        ms = Lista.mergeSort(ms);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en ordenar una lista con %s elementos " +
                          "usando MergeSort.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        ArbolBinarioOrdenado<Integer> bo = new ArbolBinarioOrdenado<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            bo.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en crear un árbol binario ordenado con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        bo.contiene(b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en encontrar un elemento en un árbol " +
                          "binario completo con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        ArbolRojinegro<Integer> rn = new ArbolRojinegro<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            rn.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en crear un árbol rojinegro con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        rn.contiene(b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en encontrar un elemento en un árbol " +
                          "rojinegro con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        ArbolAVL<Integer> avl = new ArbolAVL<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            avl.agrega(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en crear un árbol AVL con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        avl.contiene(b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en encontrar un elemento en un árbol " +
                          "AVL con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
    }
}
