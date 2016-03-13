package mx.unam.ciencias.edd.test;

import java.util.Random;
import mx.unam.ciencias.edd.Arreglos;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link Arreglos}.
 */
public class TestArreglos {

    private Random random;
    private int total;
    private Integer[] arreglo;

    /* Nos dice si el arreglo recibido está ordenado. */
    private static <T extends Comparable<T>>
                      boolean estaOrdenado(T[] a) {
        for (int i = 1; i < a.length; i++)
            if (a[i-1].compareTo(a[i]) > 0)
                return false;
        return true;
    }

    /**
     * Crea un generador de números aleatorios para cada prueba, un
     * número total de elementos para nuestro arreglo, y un arreglo.
     */
    public TestArreglos() {
        random = new Random();
        total = 10 + random.nextInt(90);
        arreglo = new Integer[total];
    }

    /**
     * Prueba unitaria para {@link Arreglos#quickSort}.
     */
    @Test public void testQuickSort() {
        arreglo[0] = 1;
        arreglo[1] = 0;
        for (int i = 2; i < total; i++)
            arreglo[i] = random.nextInt(total);
        Assert.assertFalse(estaOrdenado(arreglo));
        Arreglos.quickSort(arreglo);
        Assert.assertTrue(estaOrdenado(arreglo));
    }

    /**
     * Prueba unitaria para {@link Arreglos#selectionSort}.
     */
    @Test public void testSelectionSort() {
        arreglo[0] = 1;
        arreglo[1] = 0;
        for (int i = 2; i < total; i++)
            arreglo[i] = random.nextInt(total);
        Assert.assertFalse(estaOrdenado(arreglo));
        Arreglos.selectionSort(arreglo);
        Assert.assertTrue(estaOrdenado(arreglo));
    }

    /**
     * Prueba unitaria para {@link Arreglos#busquedaBinaria}.
     */
    @Test public void testBusquedaBinaria() {
        int ini = random.nextInt(total);
        for (int i = 0; i < total; i++)
            arreglo[i] = ini + i;
        for (int i = 0; i < total; i++)
            Assert.assertTrue(Arreglos.busquedaBinaria(arreglo, arreglo[i]) == i);
        Assert.assertTrue(Arreglos.busquedaBinaria(arreglo, ini - 1) == -1);
        Assert.assertTrue(Arreglos.busquedaBinaria(arreglo, ini + total) == -1);
    }
}
