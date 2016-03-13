package mx.unam.ciencias.edd;

import java.util.Random;

/**
 * Práctica 1: Listas doblemente ligadas.
 */
public class Practica1 {

    public static void main(String[] args) {
        Random random = new Random();
        int total = 10 + random.nextInt(90);
        Lista<Integer> lista = new Lista<Integer>();
        int i;

        for (i = 0; i < total/2; i++) {
            if (lista.getLongitud() != i) {
                System.out.println("La longitud de la lista es incorrecta.");
                System.exit(1);
            }
            int r = random.nextInt(total);
            lista.agregaFinal(r);
            if (lista.getUltimo() != r) {
                System.out.println("Error al agregar al final.");
                System.exit(1);
            }
        }

        for (i = total/2; i < total; i++) {
            if (lista.getLongitud() != i) {
                System.out.println("La longitud de la lista es incorrecta.");
                System.exit(1);
            }
            int r = random.nextInt(total);
            lista.agregaInicio(r);
            if (lista.getPrimero() != r) {
                System.out.println("Error al agregar al inicio.");
                System.exit(1);
            }
        }

        i = 0;
        int[] a = new int[total];
        for (Integer n : lista) {
            System.out.printf("Elemento %d: %d\n", i, n);
            a[i++] = n;
        }

        System.out.println("Lista: " + lista);

        for (i = 0; i < total; i++) {
            if (lista.get(i) != a[i]) {
                System.out.printf("Error al obtener el %d-ésimo elemento.\n", i);
                System.exit(1);
            }
        }

        int p = lista.eliminaPrimero();
        System.out.printf("Primer elemento %d eliminado...\n", p);
        int u = lista.eliminaUltimo();
        System.out.printf("Último elemento %d eliminado...\n", u);

        while (lista.getLongitud() > 0) {
            int n = random.nextInt(lista.getLongitud());
            int e = lista.get(n);
            System.out.printf("Eliminando %d...\n", e);
            lista.elimina(e);
            System.out.println("Lista resultante: " + lista);
        }
    }
}
