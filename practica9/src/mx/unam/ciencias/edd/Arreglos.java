package mx.unam.ciencias.edd;

/**
 * Clase para manipular arreglos genéricos de elementos comparables.
 */
public class Arreglos {

    /**
     * Auxiliar de QuickSort. Intercambia 2 elementos en un arreglo.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     * @param i una posición.
     * @param j una posición.
     */
    public static <T extends Comparable<T>> void intercambia(T[] a, int i, int j) {
        T n = a[j];
        a[j] = a[i];
        a[i] = n;
    }

    /**
     * Auxiliar de QuickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     * @param ini que indica donde iniciamos la iteración.
     * @param ini que indica donde terminamos la iteración.
     */
    private static <T extends Comparable<T>> void quickSort(T[] a, int ini, int fin){
        if (ini >= fin) {
            return;
        }
        int i = ini + 1;
        int j = fin;
        while (i < j) {
            if (a[i].compareTo(a[ini]) > 0 && a[j].compareTo(a[ini]) <= 0) {
                intercambia(a, i++, j--);
            } else if (a[i].compareTo(a[ini]) <= 0) {
                i++;
            } else {
                j--;
            }
        }
        if (a[i].compareTo(a[ini]) > 0) {
            i--;
        }
        intercambia(a, i, ini);
        quickSort(a, ini, i-1);
        quickSort(a, i+1, fin);
    }

    /**
     * Ordena el arreglo recibido usando QuickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void quickSort(T[] a) {
        if (a.length > 0) {
            quickSort(a, 0, a.length - 1);
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void selectionSort(T[] a) {
        int menor;
        for (int i = 0; i < a.length - 1; i++) {
            menor = i;
            for (int j = i+1; j < a.length; j++) {
                if (a[j].compareTo(a[menor]) < 0) {
                    menor = j;
                }
            }
            intercambia(a, i, menor);
        }
    }

    /**
     * Auxiliar de busquedaBinaria
     * @param <T> tipo del que puede ser el arreglo.
     * @param a el arreglo dónde buscar.
     * @param e el elemento a buscar.
     * @param i indice donde empieza a buscar.
     * @param j indice donde termina de buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    private static <T extends Comparable<T>> int busquedaBinaria (T[] a, T e, int ini, int fin) {
        if (ini == fin) {
            if (a[ini].compareTo(e) == 0) {
                return ini;
            }
            return -1;
        }
        int mitad = ((int)( (fin - ini) / 2 )) + ini;
        if (a[mitad].compareTo(e) < 0) {
            return busquedaBinaria(a, e, mitad + 1, fin);
        }
        return busquedaBinaria(a, e, ini, mitad);
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a el arreglo dónde buscar.
     * @param e el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int busquedaBinaria(T[] a, T e) {
        return busquedaBinaria(a, e, 0, a.length-1);
    }
}
