package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link ArbolRojinegro}.
 */
public class TestArbolRojinegro {

    private int total;
    private Random random;
    private ArbolRojinegro<Integer> arbol;

    /* Valida el vértice de un árbol rojinegro, y recursivamente
     * revisa sus hijos. */
    private static <T extends Comparable<T>> void
    arbolRojinegroValido(ArbolRojinegro<T> arbol,
                         VerticeArbolBinario<T> v) {
        switch (arbol.getColor(v)) {
        case NEGRO:
            if (v.hayIzquierdo())
                arbolRojinegroValido(arbol, v.getIzquierdo());
            if (v.hayDerecho())
                arbolRojinegroValido(arbol, v.getDerecho());
            break;
        case ROJO:
            if (v.hayIzquierdo()) {
                VerticeArbolBinario<T> i = v.getIzquierdo();
                Assert.assertTrue(arbol.getColor(i) == Color.NEGRO);
                arbolRojinegroValido(arbol, i);
            }
            if (v.hayDerecho()) {
                VerticeArbolBinario<T> d = v.getDerecho();
                Assert.assertTrue(arbol.getColor(d) == Color.NEGRO);
                arbolRojinegroValido(arbol, d);
            }
            break;
        default:
            Assert.fail();
        }
    }

    /* Valida que los caminos del vértice a sus hojas tengan todos
       el mismo número de vértices negros. */
    private static <T extends Comparable<T>> int
    validaCaminos(ArbolRojinegro<T> arbol,
                  VerticeArbolBinario<T> v) {
        int ni = -1, nd = -1;
        if (v.hayIzquierdo()) {
            VerticeArbolBinario<T> i = v.getIzquierdo();
            ni = validaCaminos(arbol, i);
        } else {
            ni = 1;
        }
        if (v.hayDerecho()) {
            VerticeArbolBinario<T> d = v.getDerecho();
            nd = validaCaminos(arbol, d);
        } else {
            nd = 1;
        }
        Assert.assertTrue(ni == nd);
        switch (arbol.getColor(v)) {
        case NEGRO:
            return 1 + ni;
        case ROJO:
            return ni;
        default:
            Assert.fail();
        }
        // Inalcanzable.
        return -1;
    }

    /**
     * Valida un árbol rojinegro. Comprueba que la raíz sea negra, que las hojas
     * sean negras, que un vértice rojo tenga dos hijos negros, y que todo
     * camino de la raíz a sus hojas tiene el mismo número de vértices negros.
     * @param <T> tipo del que puede ser el árbol rojinegro.
     * @param arbol el árbol a revisar.
     */
    public static <T extends Comparable<T>> void
    arbolRojinegroValido(ArbolRojinegro<T> arbol) {
        if (arbol.esVacio())
            return;
        TestArbolBinarioOrdenado.arbolBinarioOrdenadoValido(arbol);
        VerticeArbolBinario<T> v = arbol.raiz();
        Assert.assertTrue(arbol.getColor(v) == Color.NEGRO);
        arbolRojinegroValido(arbol, v);
        validaCaminos(arbol, v);
    }

    /**
     * Crea un árbol rojo-negro para cada prueba.
     */
    public TestArbolRojinegro() {
        random = new Random();
        arbol = new ArbolRojinegro<Integer>();
        total = random.nextInt(100);
    }

    /* Valores para agregar en la parte determinista del método agrega. */
    private static final int E1 = 1;
    private static final int E2 = 2;
    private static final int E3 = 0;
    private static final int E4 = 4;
    private static final int E5 = 3;
    private static final int E6 = 5;

    /* Prueba determinísticamente el caso 1. */
    private void testAgregaCaso1() {
        arbol.agrega(E1);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        if (arbol.getColor(v) != Color.NEGRO ||
            v.get() != E1 || v.hayIzquierdo() || v.hayDerecho())
            Assert.fail("Fallo en caso 1");
    }

    /* Prueba determinísticamente el caso 2a (sin hermano). */
    private void testAgregaCaso2A() {
        arbol.agrega(E2);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        if (arbol.getColor(v) != Color.NEGRO ||
            v.get() != E1 || v.hayIzquierdo() || !v.hayDerecho())
            Assert.fail("Fallo en caso 2 sin hermano");
        VerticeArbolBinario<Integer> d = v.getDerecho();
        if (arbol.getColor(d) != Color.ROJO ||
            d.get() != E2 || d.hayIzquierdo() || d.hayDerecho() ||
            !d.hayPadre() || d.getPadre() != v)
            Assert.fail("Fallo en caso 2 sin hermano");
    }

    /* Prueba determinísticamente el caso 2b (con hermano). */
    private void testAgregaCaso2B() {
        arbol.agrega(E3);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        if (arbol.getColor(v) != Color.NEGRO ||
            v.get() != E1 || !v.hayIzquierdo() || !v.hayDerecho())
            Assert.fail("Fallo en caso 2 con hermano");
        VerticeArbolBinario<Integer> d = v.getDerecho();
        if (arbol.getColor(d) != Color.ROJO ||
            d.get() != E2 || d.hayIzquierdo() || d.hayDerecho() ||
            !d.hayPadre() || d.getPadre() != v)
            Assert.fail("Fallo en caso 2 con hermano");
        VerticeArbolBinario<Integer> i = v.getIzquierdo();
        if (arbol.getColor(i) != Color.ROJO ||
            i.get() != E3 || i.hayIzquierdo() || i.hayDerecho() ||
            !i.hayPadre() || i.getPadre() != v)
            Assert.fail("Fallo en caso 2 con hermano");
    }

    /* Prueba determinísticamente el caso 3. */
    private void testAgregaCaso3() {
        arbol.agrega(E4);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        if (arbol.getColor(v) != Color.NEGRO ||
            v.get() != E1 || !v.hayIzquierdo() || !v.hayDerecho())
            Assert.fail("Fallo en caso 3");
        VerticeArbolBinario<Integer> d = v.getDerecho();
        if (arbol.getColor(d) != Color.NEGRO ||
            d.get() != E2 || d.hayIzquierdo() || !d.hayDerecho() ||
            !d.hayPadre() || d.getPadre() != v)
            Assert.fail("Fallo en caso 3");
        VerticeArbolBinario<Integer> i = v.getIzquierdo();
        if (arbol.getColor(i) != Color.NEGRO ||
            i.get() != E3 || i.hayIzquierdo() || i.hayDerecho() ||
            !i.hayPadre() || i.getPadre() != v)
            Assert.fail("Fallo en caso 3");
        VerticeArbolBinario<Integer> dd = d.getDerecho();
        if (arbol.getColor(dd) != Color.ROJO ||
            dd.get() != E4 || dd.hayIzquierdo() || dd.hayDerecho() ||
            !dd.hayPadre() || dd.getPadre() != d)
            Assert.fail("Fallo en caso 3");
    }

    /* Prueba determinísticamente el caso 4. */
    private void testAgregaCaso4() {
        arbol.agrega(E5);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        if (arbol.getColor(v) != Color.NEGRO ||
            v.get() != E1 || !v.hayIzquierdo() || !v.hayDerecho())
            Assert.fail("Fallo en caso 4");
        VerticeArbolBinario<Integer> d = v.getDerecho();
        if (arbol.getColor(d) != Color.NEGRO ||
            d.get() != E5 || !d.hayIzquierdo() || !d.hayDerecho() ||
            !d.hayPadre() || d.getPadre() != v)
            Assert.fail("Fallo en caso 4");
        VerticeArbolBinario<Integer> i = v.getIzquierdo();
        if (arbol.getColor(i) != Color.NEGRO ||
            i.get() != E3 || i.hayIzquierdo() || i.hayDerecho() ||
            !i.hayPadre() || i.getPadre() != v)
            Assert.fail("Fallo en caso 4");
        VerticeArbolBinario<Integer> dd = d.getDerecho();
        if (arbol.getColor(dd) != Color.ROJO ||
            dd.get() != E4 || dd.hayIzquierdo() || dd.hayDerecho() ||
            !dd.hayPadre() || dd.getPadre() != d)
            Assert.fail("Fallo en caso 4");
        VerticeArbolBinario<Integer> di = d.getIzquierdo();
        if (arbol.getColor(di) != Color.ROJO ||
            di.get() != E2 || di.hayIzquierdo() || di.hayDerecho() ||
            !di.hayPadre() || di.getPadre() != d)
            Assert.fail("Fallo en caso 4");
    }

    /* Prueba determinísticamente el caso 5. */
    private void testAgregaCaso5() {
        arbol.agrega(E6);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        if (arbol.getColor(v) != Color.NEGRO ||
            v.get() != E1 || !v.hayIzquierdo() || !v.hayDerecho())
            Assert.fail("Fallo en caso 5");
        VerticeArbolBinario<Integer> d = v.getDerecho();
        if (arbol.getColor(d) != Color.ROJO ||
            d.get() != E5 || !d.hayIzquierdo() || !d.hayDerecho() ||
            !d.hayPadre() || d.getPadre() != v)
            Assert.fail("Fallo en caso 5");
        VerticeArbolBinario<Integer> i = v.getIzquierdo();
        if (arbol.getColor(i) != Color.NEGRO ||
            i.get() != E3 || i.hayIzquierdo() || i.hayDerecho() ||
            !i.hayPadre() || i.getPadre() != v)
            Assert.fail("Fallo en caso 5");
        VerticeArbolBinario<Integer> dd = d.getDerecho();
        if (arbol.getColor(dd) != Color.NEGRO ||
            dd.get() != E4 || dd.hayIzquierdo() || !dd.hayDerecho() ||
            !dd.hayPadre() || dd.getPadre() != d)
            Assert.fail("Fallo en caso 5");
        VerticeArbolBinario<Integer> di = d.getIzquierdo();
        if (arbol.getColor(di) != Color.NEGRO ||
            di.get() != E2 || di.hayIzquierdo() || di.hayDerecho() ||
            !di.hayPadre() || di.getPadre() != d)
            Assert.fail("Fallo en caso 5");
        VerticeArbolBinario<Integer> ddd = dd.getDerecho();
        if (arbol.getColor(ddd) != Color.ROJO ||
            ddd.get() != E6 || ddd.hayIzquierdo() || ddd.hayDerecho() ||
            !ddd.hayPadre() || ddd.getPadre() != dd)
            Assert.fail("Fallo en caso 5");
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#agrega}.
     */
    @Test public void testAgrega() {
        testAgregaCaso1();
        testAgregaCaso2A();
        testAgregaCaso2B();
        testAgregaCaso3();
        testAgregaCaso4();
        testAgregaCaso5();
        arbol = new ArbolRojinegro<Integer>();
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arbol.agrega(n);
            Assert.assertTrue(arbol.getElementos() == i+1);
            VerticeArbolBinario<Integer> it = arbol.busca(n);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == n);
            arbolRojinegroValido(arbol);
        }
    }

    /* Regresa una cola con los elementos del árbol al recorrerlo por BFS. */
    private Cola<Integer> bfs() {
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Cola<VerticeArbolBinario<Integer>> c = new Cola<>();
        Cola<Integer> q = new Cola<Integer>();
        c.mete(v);
        while (!c.esVacia()) {
            v = c.saca();
            q.mete(v.get());
            if (v.hayIzquierdo())
                c.mete(v.getIzquierdo());
            if (v.hayDerecho())
                c.mete(v.getDerecho());
        }
        return q;
    }

    /* Prueba la estructura del árbol respecto a un arreglo de enteros. */
    private void pruebaEstructura(int[] a, String mensaje) {
        Cola<Integer> c = bfs();
        int i = 0;
        while (!c.esVacia()) {
            int n = c.saca();
            if (i >= a.length || a[i++] != n)
                Assert.fail(mensaje);
        }
    }

    /* Prueba el método elimina determinísticamente. */
    private void testEliminaDeterministico() {
        for (int i = 0; i < 15; i++)
            arbol.agrega(i);
        pruebaEstructura(new int[] { 3, 1, 7, 0, 2, 5, 9, 4, 6, 8, 11, 10, 13, 12, 14 },
                         "Fallo al inicializar árbol.");
        arbol.elimina(8);
        pruebaEstructura(new int[] { 3, 1, 7, 0, 2, 5, 11, 4, 6, 9, 13, 10, 12, 14 },
                         "Fallo en los casos 2 o 4.");
        arbol.elimina(1);
        pruebaEstructura(new int[] { 7, 3, 11, 0, 5, 9, 13, 2, 4, 6, 10, 12, 14 },
                         "Fallo en los casos 3, 2 o 4.");
        arbol.elimina(6);
        pruebaEstructura(new int[] { 7, 3, 11, 0, 5, 9, 13, 2, 4, 10, 12, 14 },
                         "Fallo en el caso 4.");
        arbol.elimina(4);
        pruebaEstructura(new int[] { 7, 3, 11, 0, 5, 9, 13, 2, 10, 12, 14 },
                         "Fallo al eliminar hoja roja.");
        arbol.elimina(5);
        pruebaEstructura(new int[] { 7, 2, 11, 0, 3, 9, 13, 10, 12, 14 },
                         "Fallo en los casos 5 o 6.");
        arbol.elimina(10);
        pruebaEstructura(new int[] { 7, 2, 11, 0, 3, 9, 13, 12, 14 },
                         "Fallo al eliminar hoja roja.");
        arbol.elimina(13);
        pruebaEstructura(new int[] { 7, 2, 11, 0, 3, 9, 12, 14 },
                         "Fallo al eliminar hoja negra de padre rojo.");
        arbol.elimina(14);
        pruebaEstructura(new int[] { 7, 2, 11, 0, 3, 9, 12 },
                         "Fallo al eliminar hoja roja.");
        arbol.elimina(2);
        pruebaEstructura(new int[] { 7, 0, 11, 3, 9, 12 },
                         "Fallo en los casos 3 o 1.");
        arbol.elimina(3);
        pruebaEstructura(new int[] { 7, 0, 11, 9, 12 },
                         "Fallo al eliminar hoja roja.");
        arbol.elimina(12);
        pruebaEstructura(new int[] { 7, 0, 11, 9 },
                         "Fallo en el caso 4.");
        arbol.elimina(9);
        pruebaEstructura(new int[] { 7, 0, 11 },
                         "Fallo al eliminar hoja roja.");
        arbol.elimina(7);
        pruebaEstructura(new int[] { 0, 11 },
                         "Fallo en el caso 1.");
        arbol.elimina(11);
        pruebaEstructura(new int[] { 0 },
                         "Fallo al eliminar hoja roja.");
        arbol.elimina(0);
        Assert.assertTrue(arbol.esVacio());
        Assert.assertTrue(arbol.getElementos() == 0);
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#elimina}.
     */
    @Test public void testElimina() {
        testEliminaDeterministico();
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
            arbol.agrega(a[i]);
        }
        for (int i : a)
            Assert.assertTrue(arbol.busca(i) != null);
        int n = total;
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
            arbolRojinegroValido(arbol);
            a[i] = -1;
        }
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#getColor}.
     */
    @Test public void testGetColor() {
        arbol.agrega(1);
        arbol.agrega(0);
        arbol.agrega(2);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == 1);
        Assert.assertTrue(arbol.getColor(v) == Color.NEGRO);
        Assert.assertTrue(v.hayIzquierdo());
        v = v.getIzquierdo();
        Assert.assertTrue(v.get() == 0);
        Assert.assertTrue(arbol.getColor(v) == Color.ROJO);
        Assert.assertTrue(v.hayPadre());
        v = v.getPadre();
        Assert.assertTrue(v.hayDerecho());
        v = v.getDerecho();
        Assert.assertTrue(v.get() == 2);
        Assert.assertTrue(arbol.getColor(v) == Color.ROJO);
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#giraIzquierda}.
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
     * Prueba unitaria para {@link ArbolRojinegro#giraDerecha}.
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
        arbol = new ArbolRojinegro<Integer>();
        ArbolRojinegro<Integer> arbol2 = new ArbolRojinegro<Integer>();
        Assert.assertTrue(arbol.equals(arbol2));
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertTrue(arbol.equals(arbol2));
        arbol = new ArbolRojinegro<Integer>();
        arbol2 = new ArbolRojinegro<Integer>();
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            if (i != total - 1)
                arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        if (total > 0)
            Assert.assertFalse(arbol.equals(arbol2));
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
            arbolRojinegroValido(arbol);
            Assert.assertTrue(arbol.toString() != null &&
                              !arbol.toString().equals(""));
        }
        String cadena =
            "N{2}\n" +
            "├─›N{1}\n" +
            "└─»R{4}\n" +
            "   ├─›N{3}\n" +
            "   └─»N{6}\n" +
            "      ├─›R{5}\n" +
            "      └─»R{7}";
        arbol = new ArbolRojinegro<Integer>();
        for (int i = 1; i <= 7; i++)
            arbol.agrega(i);
        Assert.assertTrue(arbol.toString().equals(cadena));
    }
}
