package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.ArbolAVL;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link ArbolAVL}.
 */
public class TestArbolAVL {

    private int total;
    private Random random;
    private ArbolAVL<Integer> arbol;

    /* Método auxiliar para validar la altura y balance de cada vértice. */
    private static <T extends Comparable<T>> int
    validaAlturasYBalances(ArbolAVL<T> arbol, VerticeArbolBinario<T> vertice) {
        int aIzq = vertice.hayIzquierdo() ?
            validaAlturasYBalances(arbol, vertice.getIzquierdo()) : -1;
        int aDer = vertice.hayDerecho() ?
            validaAlturasYBalances(arbol, vertice.getDerecho()) : -1;
        int altura = Math.max(aIzq, aDer) + 1;
        Assert.assertTrue(arbol.getAltura(vertice) == altura);
        int balance = aIzq - aDer;
        Assert.assertTrue(balance >= -1 && balance <= 1);
        return altura;
    }

    /**
     * Valida un árbol AVL. Comprueba que para todo nodo A se cumpla que su
     * altura sea correcta, y que su balance esté en el rango [-1, 1].
     * @param <T> tipo del que puede ser el árbol AVL.
     * @param arbol el árbol a revisar.
     */
    public static <T extends Comparable<T>> void
    arbolAVLValido(ArbolAVL<T> arbol) {
        if (arbol.esVacio())
            return;
        TestArbolBinarioOrdenado.arbolBinarioOrdenadoValido(arbol);
        VerticeArbolBinario<T> raiz = arbol.raiz();
        validaAlturasYBalances(arbol, raiz);
    }

    /**
     * Crea un árbol AVL para cada prueba.
     */
    public TestArbolAVL() {
        random = new Random();
        arbol = new ArbolAVL<Integer>();
        total = 1 + random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link ArbolAVL#ArbolAVL}.
     */
    @Test public void testConstructor() {
        arbol = new ArbolAVL<Integer>();
        Assert.assertTrue(arbol.esVacio());
        Assert.assertTrue(arbol.getElementos() == 0);
    }

    /* Construye un arreglo con elementos no repetidos. */
    private int[] arregloSinRepetidos() {
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            int r;
            boolean repetido = false;
            do {
                r = random.nextInt(1000);
                repetido = false;
                for (int j = 0; j < i; j++)
                    if (r == a[j])
                        repetido = true;
            } while (repetido);
            a[i] = r;
        }
        return a;
    }

    /**
     * Prueba unitaria para {@link ArbolAVL#agrega}.
     */
    @Test public void testAgrega() {
        Assert.assertTrue(arbol.esVacio());
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arbol.agrega(n);
            Assert.assertTrue(arbol.getElementos() == i+1);
            VerticeArbolBinario<Integer> it = arbol.busca(n);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == n);
            arbolAVLValido(arbol);
        }
        Assert.assertTrue(arbol.getElementos() == total);
    }

    /**
     * Prueba unitaria para {@link ArbolAVL#elimina}.
     */
    @Test public void testElimina() {
        arbol.elimina(0);
        int[] a = arregloSinRepetidos();
        for (int i = 0; i < total; i++)
            arbol.agrega(a[i]);
        int n = total;
        int m = total - 1;
        while (arbol.getElementos() > 0) {
            Assert.assertTrue(arbol.getElementos() == n);
            int i = random.nextInt(total);
            if (a[i] == -1)
                continue;
            int e = a[i];
            VerticeArbolBinario<Integer> it = arbol.busca(e);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == e);
            arbol.elimina(e);
            it = arbol.busca(e);
            Assert.assertTrue(it == null);
            Assert.assertTrue(arbol.getElementos() == --n);
            arbolAVLValido(arbol);
            a[i] = -1;
        }
        arbol.elimina(a[a.length/2]);
    }

    /**
     * Prueba unitaria para {@link ArbolAVL#getAltura}.
     */
    @Test public void testGetAltura() {
        arbol.agrega(1);
        arbol.agrega(0);
        arbol.agrega(2);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == 1);
        Assert.assertTrue(arbol.getAltura(v) == 1);
        Assert.assertTrue(v.hayIzquierdo());
        v = v.getIzquierdo();
        Assert.assertTrue(v.get() == 0);
        Assert.assertTrue(arbol.getAltura(v) == 0);
        Assert.assertTrue(v.hayPadre());
        v = v.getPadre();
        Assert.assertTrue(v.hayDerecho());
        v = v.getDerecho();
        Assert.assertTrue(v.get() == 2);
        Assert.assertTrue(arbol.getAltura(v) == 0);
    }

    /**
     * Prueba unitaria para {@link ArbolAVL#giraIzquierda}.
     */
    @Test public void testGiraIzquierda() {
        try {
            arbol.giraIzquierda(null);
            Assert.fail();
        } catch (UnsupportedOperationException uoe) {}
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            VerticeArbolBinario<Integer> v = arbol.getUltimoVerticeAgregado();
            try {
                arbol.giraIzquierda(v);
                Assert.fail();
            } catch (UnsupportedOperationException uoe) {}
        }
    }

    /**
     * Prueba unitaria para {@link ArbolAVL#giraDerecha}.
     */
    @Test public void testGiraDerecha() {
        try {
            arbol.giraDerecha(null);
            Assert.fail();
        } catch (UnsupportedOperationException uoe) {}
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            VerticeArbolBinario<Integer> v = arbol.getUltimoVerticeAgregado();
            try {
                arbol.giraDerecha(v);
                Assert.fail();
            } catch (UnsupportedOperationException uoe) {}
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#equals}.
     */
    @Test public void testEquals() {
        arbol = new ArbolAVL<Integer>();
        ArbolAVL<Integer> arbol2 = new ArbolAVL<Integer>();
        Assert.assertTrue(arbol.equals(arbol2));
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertTrue(arbol.equals(arbol2));
        arbol = new ArbolAVL<Integer>();
        arbol2 = new ArbolAVL<Integer>();
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            if (i != total - 1)
                arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertFalse(arbol.equals(arbol2));
        Assert.assertFalse(arbol.equals(""));
        Assert.assertFalse(arbol.equals(null));
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#toString}.
     */
    @Test public void testToString() {
        /* Estoy dispuesto a aceptar una mejor prueba. */
        Assert.assertTrue(arbol.toString() != null &&
                          arbol.toString().equals(""));
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolAVLValido(arbol);
            Assert.assertTrue(arbol.toString() != null &&
                              !arbol.toString().equals(""));
        }
        String cadena =
            "4 2/0\n" +
            "├─›2 1/0\n" +
            "│  ├─›1 0/0\n" +
            "│  └─»3 0/0\n" +
            "└─»6 1/0\n" +
            "   ├─›5 0/0\n" +
            "   └─»7 0/0";
        arbol = new ArbolAVL<Integer>();
        for (int i = 1; i <= 7; i++)
            arbol.agrega(i);
        Assert.assertTrue(arbol.toString().equals(cadena));
    }
}
