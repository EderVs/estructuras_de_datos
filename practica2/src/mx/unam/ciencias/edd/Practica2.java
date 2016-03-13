package mx.unam.ciencias.edd;

import java.util.Random;
import java.text.NumberFormat;

/**
 * Práctica 2: Pilas, colas, ordenamientos y búsquedas.
 */
public class Practica2 {

    public static void main(String[] args) {

        /* Pilas y colas. */

        String[] a = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

        Pila<String> pila = new Pila<String>();
        Cola<String> cola = new Cola<String>();

        System.out.println("Arreglo original:");
        for (String s : a) {
            System.out.print(s + " ");
            pila.mete(s);
            cola.mete(s);
        }
        System.out.println();

        System.out.println("Pila:");
        while (!pila.esVacia())
            System.out.print(pila.saca() + " ");
        System.out.println();

        System.out.println("Cola:");
        while (!cola.esVacia())
            System.out.print(cola.saca() + " ");
        System.out.println("\n");

        /* Ordenamientos y búsquedas. */

        int N = 100000; /* Cien mil */
        Random random = new Random();
        NumberFormat nf = NumberFormat.getIntegerInstance();
        long tiempoInicial, tiempoTotal;

        int[] arreglo = new int[N];
        for (int i = 0; i < N; i++)
            arreglo[i] = random.nextInt();

        Integer[] is = new Integer[N];
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            is[i] = arreglo[i];
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un arreglo con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        Arreglos.selectionSort(is);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en ordenar un arreglo con %s elementos " +
                          "usando SelectionSort.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        Integer[] qs = new Integer[N];
        for (int i = 0; i < N; i++)
            qs[i] = arreglo[i];

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
    }
}
