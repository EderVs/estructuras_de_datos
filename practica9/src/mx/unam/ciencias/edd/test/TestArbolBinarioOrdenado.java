package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.ArbolBinarioOrdenado;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link
 * ArbolBinarioOrdenado}.
 */
public class TestArbolBinarioOrdenado {

    private int total;
    private Random random;
    private ArbolBinarioOrdenado<Integer> arbol;

    /* Método auxiliar recursivo para llenar una cola con los elementos del
     * árbol recorrido en in-order. */
    private static <T extends Comparable<T>> void
    llenaColaEnOrden(VerticeArbolBinario<T> v, Cola<T> cola) {
        if (v.hayIzquierdo())
            llenaColaEnOrden(v.getIzquierdo(), cola);
        cola.mete(v.get());
        if (v.hayDerecho())
            llenaColaEnOrden(v.getDerecho(), cola);
    }

    /**
     * Valida un árbol ordenado. Comprueba que para todo nodo A se
     * cumpla que si A tiene como hijo izquierdo a B, entonces B ≤
     * A, y si A tiene como hijo derecho a C, entonces A ≤ C.
     * @param <T> tipo del que puede ser el árbol binario ordenado.
     * @param arbol el árbol a revisar.
     */
    public static <T extends Comparable<T>> void
    arbolBinarioOrdenadoValido(ArbolBinarioOrdenado<T> arbol) {
        if (arbol.esVacio())
            return;
        UtilTestArbolBinario.arbolBinarioValido(arbol);
        Cola<T> cola = new Cola<T>();
        try {
            llenaColaEnOrden(arbol.raiz(), cola);
        } catch (NoSuchElementException sdee) {
            Assert.fail();
        }
        T a = cola.saca();
        while (!cola.esVacia()) {
            T b = cola.saca();
            Assert.assertTrue(a.compareTo(b) <= 0);
            a = b;
        }
    }

    /**
     * Crea un árbol binario para cada prueba.
     */
    public TestArbolBinarioOrdenado() {
        random = new Random();
        arbol = new ArbolBinarioOrdenado<Integer>();
        total = 3 + random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#ArbolBinarioOrdenado()}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(arbol.esVacio());
        Assert.assertTrue(arbol.getElementos() == 0);
    }

    /**
     * Prueba unitaria para {@link
     * ArbolBinarioOrdenado#ArbolBinarioOrdenado(Coleccion)}.
     */
    @Test public void testConstructorColeccion() {
        Lista<Integer> lista = new Lista<Integer>();
        for (int i = 0; i < total; i++)
            lista.agrega(random.nextInt(total));
        arbol = new ArbolBinarioOrdenado<Integer>(lista);
        Assert.assertTrue(lista.getLongitud() == arbol.getElementos());
        for (Integer n : lista)
            Assert.assertTrue(arbol.contiene(n));
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#agrega}.
     */
    @Test public void testAgrega() {
        try {
            arbol.agrega(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arbol.agrega(n);
            VerticeArbolBinario<Integer> v;
            v = arbol.getUltimoVerticeAgregado();
            Assert.assertTrue(arbol.getElementos() == i+1);
            VerticeArbolBinario<Integer> it = arbol.busca(n);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == n);
            arbolBinarioOrdenadoValido(arbol);
            Assert.assertTrue(v.get() == n);
        }
    }

    /* Llena el árbol con elementos no repetidos. */
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
     * Prueba unitaria para {@link ArbolBinarioOrdenado#elimina}.
     */
    @Test public void testElimina() {
        int[] a = arregloSinRepetidos();
        for (int n : a)
            arbol.agrega(n);
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
            arbolBinarioOrdenadoValido(arbol);
            a[i] = -1;
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#profundidad}.
     */
    @Test public void testProfundidad() {
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolBinarioOrdenadoValido(arbol);
            int min = (int)Math.floor(Math.log(i+1)/Math.log(2));
            int max = i;
            Assert.assertTrue(arbol.profundidad() >= min &&
                              arbol.profundidad() <= max);
        }
        arbol = new ArbolBinarioOrdenado<Integer>();
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            arbolBinarioOrdenadoValido(arbol);
            Assert.assertTrue(arbol.profundidad() == i);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#getElementos}.
     */
    @Test public void testGetElementos() {
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolBinarioOrdenadoValido(arbol);
            Assert.assertTrue(arbol.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#getUltimoVerticeAgregado}.
     */
    @Test public void testGetUltimoVerticeAgregado() {
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(total);
            arbol.agrega(n);
            VerticeArbolBinario<Integer> v = arbol.getUltimoVerticeAgregado();
            Assert.assertTrue(v.get().equals(n));
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#contiene}.
     */
    @Test public void testContiene() {
        int[] a = arregloSinRepetidos();
        for (int n : a) {
            Assert.assertFalse(arbol.contiene(n));
            arbol.agrega(n);
            Assert.assertTrue(arbol.contiene(n));
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#busca}.
     */
    @Test public void testBusca() {
        int[] a = arregloSinRepetidos();
        for (int n : a) {
            Assert.assertTrue(arbol.busca(n) == null);
            arbol.agrega(n);
            Assert.assertFalse(arbol.busca(n) == null);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#raiz}.
     */
    @Test public void testRaiz() {
        try {
            arbol.raiz();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int p = Integer.MAX_VALUE;
        for (int i = 0; i < total; i++) {
            int v = random.nextInt(total);
            if (p == Integer.MAX_VALUE)
                p = v;
            arbol.agrega(v);
        }
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == p);
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#esVacio}.
     */
    @Test public void testEsVacio() {
        Assert.assertTrue(arbol.esVacio());
        arbol.agrega(1);
        Assert.assertFalse(arbol.esVacio());
        arbol.elimina(1);
        Assert.assertTrue(arbol.esVacio());
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            Assert.assertFalse(arbol.esVacio());
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#equals}.
     */
    @Test public void testEquals() {
        arbol = new ArbolBinarioOrdenado<Integer>();
        ArbolBinarioOrdenado<Integer> arbol2 = new ArbolBinarioOrdenado<Integer>();
        Assert.assertTrue(arbol.equals(arbol2));
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertTrue(arbol.equals(arbol2));
        arbol = new ArbolBinarioOrdenado<Integer>();
        arbol2 = new ArbolBinarioOrdenado<Integer>();
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            if (i != total - 1)
                arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertFalse(arbol.equals(arbol2));
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#toString}.
     */
    @Test public void testToString() {
        /* Estoy dispuesto a aceptar una mejor prueba. */
        Assert.assertTrue(arbol.toString() != null &&
                          arbol.toString().equals(""));
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolBinarioOrdenadoValido(arbol);
            Assert.assertTrue(arbol.toString() != null &&
                              !arbol.toString().equals(""));
        }
        String cadena =
            "-1\n" +
            "├─›-3\n" +
            "│  └─›-5\n" +
            "└─»2\n" +
            "   └─»4";
        arbol = new ArbolBinarioOrdenado<Integer>();
        for (int i = 1; i <= 5; i++)
            arbol.agrega(i * (int)Math.pow(-1, i));
        Assert.assertTrue(arbol.toString().equals(cadena));
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioOrdenado#iterator}.
     */
    @Test public void testIterator() {
        Lista<Integer> lista = new Lista<Integer>();
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arbol.agrega(n);
            lista.agrega(n);
        }
        lista = Lista.mergeSort(lista);
        int c = 0;
        Iterator<Integer> i1 = arbol.iterator();
        Iterator<Integer> i2 = lista.iterator();
        while (i1.hasNext() && i2.hasNext())
            Assert.assertTrue(i1.next() == i2.next());
        Assert.assertTrue(!i1.hasNext() && !i2.hasNext());
    }

    /**
     * Prueba unitaria para el método toString de {@link VerticeArbolBinario}.
     */
    @Test public void testVerticeToString() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.toString().equals(String.valueOf(r)));
    }

    /**
     * Prueba unitaria para {@link VerticeArbolBinario#hayPadre}.
     */
    @Test public void testVerticeHayPadre() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == r);
        Assert.assertFalse(v.hayPadre());
        int i = random.nextInt(total);
        arbol.agrega(i);
        v = arbol.getUltimoVerticeAgregado();
        Assert.assertTrue(v.hayPadre());
    }

    /**
     * Prueba unitaria para {@link VerticeArbolBinario#hayIzquierdo}.
     */
    @Test public void testVerticeHayIzquierdo() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == r);
        Assert.assertFalse(v.hayIzquierdo());
        int i = r-1;
        arbol.agrega(i);
        Assert.assertTrue(v.hayIzquierdo());
    }

    /**
     * Prueba unitaria para {@link VerticeArbolBinario#hayDerecho}.
     */
    @Test public void testVerticeHayDerecho() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == r);
        Assert.assertFalse(v.hayDerecho());
        arbol.agrega(r-1);
        arbol.agrega(r+1);
        Assert.assertTrue(v.hayDerecho());
    }

    /**
     * Prueba unitaria para {@link VerticeArbolBinario#getPadre}.
     */
    @Test public void testVerticeGetPadre() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == r);
        Assert.assertFalse(v.hayPadre());
        try {
            v.getPadre();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int i = random.nextInt(total);
        arbol.agrega(i);
        v = arbol.getUltimoVerticeAgregado();
        Assert.assertTrue(v.get() == i);
        Assert.assertTrue(v.hayPadre());
        v = v.getPadre();
        Assert.assertTrue(v.get() == r);
    }

    /**
     * Prueba unitaria para {@link VerticeArbolBinario#getIzquierdo}.
     */
    @Test public void testVerticeGetIzquierdo() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == r);
        Assert.assertFalse(v.hayIzquierdo());
        try {
            v.getIzquierdo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int i = r-1;
        arbol.agrega(i);
        Assert.assertTrue(v.hayIzquierdo());
        v = v.getIzquierdo();
        Assert.assertTrue(v.get() == i);
    }

    /**
     * Prueba unitaria para {@link VerticeArbolBinario#getDerecho}.
     */
    @Test public void testVerticeGetDerecho() {
        int r = random.nextInt(total);
        arbol.agrega(r);
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == r);
        Assert.assertFalse(v.hayDerecho());
        try {
            v.getDerecho();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int i = r-1;
        int d = r+1;
        arbol.agrega(i);
        arbol.agrega(d);
        Assert.assertTrue(v.hayDerecho());
        v = v.getDerecho();
        Assert.assertTrue(v.get() == d);
    }

    /**
     * Prueba unitaria para {@link VerticeArbolBinario#get}.
     */
    @Test public void testVerticeGet() {
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(total);
            arbol.agrega(n);
            VerticeArbolBinario<Integer> v = arbol.getUltimoVerticeAgregado();
            Assert.assertTrue(v.get().equals(n));
        }
    }

    /**
     * Prueba unitaria para el método equals de {@link VerticeArbolBinario}.
     */
    @Test public void testVerticeEquals() {
        int r = random.nextInt(total);
        int i = r-1;
        int d = r+1;
        arbol.agrega(r);
        arbol.agrega(i);
        arbol.agrega(d);
        ArbolBinarioOrdenado<Integer> otro = new ArbolBinarioOrdenado<Integer>();
        otro.agrega(r);
        Assert.assertFalse(arbol.raiz().equals(otro.raiz()));
        otro.agrega(i);
        Assert.assertFalse(arbol.raiz().equals(otro.raiz()));
        otro.agrega(d);
        Assert.assertTrue(arbol.raiz().equals(otro.raiz()));
    }

    /**
     * Prueba unitaria para la implementación {@link Iterator#hasNext} a través
     * del método {@link ArbolBinarioOrdenado#iterator}.
     */
    @Test public void testIteradorHasNext() {
        Iterator<Integer> iterador = arbol.iterator();
        Assert.assertFalse(iterador.hasNext());
        arbol.agrega(-1);
        iterador = arbol.iterator();
        Assert.assertTrue(iterador.hasNext());
        for (int i = 0; i < total; i++)
            arbol.agrega(i);
        iterador = arbol.iterator();
        for (int i = 0; i < total; i++)
            iterador.next();
        Assert.assertTrue(iterador.hasNext());
        iterador.next();
        Assert.assertFalse(iterador.hasNext());
    }

    /**
     * Prueba unitaria para la implementación {@link Iterator#next} a través del
     * método {@link ArbolBinarioOrdenado#iterator}.
     */
    @Test public void testIteradorNext() {
        Iterator<Integer> iterador = arbol.iterator();
        try {
            iterador.next();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++)
            arbol.agrega(i);
        iterador = arbol.iterator();
        for (int i = 0; i < total; i++)
            Assert.assertTrue(iterador.next().equals(i));
        try {
            iterador.next();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para la implementación {@link Iterator#remove} a través
     * del método {@link ArbolBinarioOrdenado#iterator}.
     */
    @Test public void testIteradorRemove() {
        for (int i = 0; i < total; i++)
            arbol.agrega(i);
        Iterator<Integer> iterador = arbol.iterator();
        while (iterador.hasNext()) {
            try {
                iterador.next();
                iterador.remove();
                Assert.fail();
            } catch (UnsupportedOperationException uoe) {}
        }
    }

    /**
     * Prueba unitaria para {@link
     * ArbolBinarioOrdenado#giraDerecha}.
     */
    @Test public void testGiraDerecha() {
        if (total == 1)
            total++;
        int[] a = arregloSinRepetidos();
        a[total-2] = 2001;
        a[total-1] = 2000;
        for (int n : a)
            arbol.agrega(n);
        VerticeArbolBinario<Integer> vertice = null;
        int Q = -1;
        do {
            Q = a[random.nextInt(total)];
            vertice = arbol.busca(Q);
            Assert.assertTrue(vertice != null);
            Assert.assertTrue(vertice.get() == Q);
        } while (!vertice.hayIzquierdo());
        vertice = vertice.getIzquierdo();
        int P = vertice.get();
        int A = -1, B = -1, C = -1;
        if (vertice.hayIzquierdo()) {
            vertice = vertice.getIzquierdo();
            A = vertice.get();
            vertice = vertice.getPadre();
        }
        if (vertice.hayDerecho()) {
            vertice = vertice.getDerecho();
            B = vertice.get();
            vertice = vertice.getPadre();
        }
        vertice = vertice.getPadre();
        if (vertice.hayDerecho()) {
            vertice = vertice.getDerecho();
            C = vertice.get();
            vertice = vertice.getPadre();
        }
        arbol.giraDerecha(vertice);
        Assert.assertTrue(arbol.getElementos() == total);
        arbolBinarioOrdenadoValido(arbol);
        for (int n : a)
            Assert.assertTrue(arbol.busca(n) != null);
        Assert.assertTrue(vertice.get() == Q);
        Assert.assertTrue(vertice.hayPadre());
        vertice = vertice.getPadre();
        Assert.assertTrue(vertice.get() == P);
        if (A != -1) {
            Assert.assertTrue(vertice.hayIzquierdo());
            vertice = vertice.getIzquierdo();
            Assert.assertTrue(vertice.get() == A);
            vertice = vertice.getPadre();
        }
        Assert.assertTrue(vertice.hayDerecho());
        vertice = vertice.getDerecho();
        Assert.assertTrue(vertice.get() == Q);
        if (B != -1) {
            Assert.assertTrue(vertice.hayIzquierdo());
            vertice = vertice.getIzquierdo();
            Assert.assertTrue(vertice.get() == B);
            vertice = vertice.getPadre();
        }
        if (C != -1) {
            Assert.assertTrue(vertice.hayDerecho());
            vertice = vertice.getDerecho();
            Assert.assertTrue(vertice.get() == C);
            vertice = vertice.getPadre();
        }
    }

    /**
     * Prueba unitaria para {@link
     * ArbolBinarioOrdenado#giraIzquierda}.
     */
    @Test public void testGiraIzquierda() {
        if (total == 1)
            total++;
        int[] a = arregloSinRepetidos();
        a[total-2] = 2000;
        a[total-1] = 2001;
        for (int n : a)
            arbol.agrega(n);
        VerticeArbolBinario<Integer> vertice = null;
        int P = -1;
        do {
            P = a[random.nextInt(total)];
            vertice = arbol.busca(P);
            Assert.assertTrue(vertice != null);
            Assert.assertTrue(vertice.get() == P);
        } while (!vertice.hayDerecho());
        vertice = vertice.getDerecho();
        int Q = vertice.get();
        int A = -1, B = -1, C = -1;
        if (vertice.hayIzquierdo()) {
            vertice = vertice.getIzquierdo();
            B = vertice.get();
            vertice = vertice.getPadre();
        }
        if (vertice.hayDerecho()) {
            vertice = vertice.getDerecho();
            C = vertice.get();
            vertice = vertice.getPadre();
        }
        vertice = vertice.getPadre();
        if (vertice.hayIzquierdo()) {
            vertice = vertice.getIzquierdo();
            A = vertice.get();
            vertice = vertice.getPadre();
        }
        arbol.giraIzquierda(vertice);
        Assert.assertTrue(arbol.getElementos() == total);
        arbolBinarioOrdenadoValido(arbol);
        for (int n : a)
            Assert.assertTrue(arbol.busca(n) != null);
        Assert.assertTrue(vertice.get() == P);
        Assert.assertTrue(vertice.hayPadre());
        vertice = vertice.getPadre();
        Assert.assertTrue(vertice.get() == Q);
        if (C != -1) {
            Assert.assertTrue(vertice.hayDerecho());
            vertice = vertice.getDerecho();
            Assert.assertTrue(vertice.get() == C);
            vertice = vertice.getPadre();
        }
        Assert.assertTrue(vertice.hayIzquierdo());
        vertice = vertice.getIzquierdo();
        Assert.assertTrue(vertice.get() == P);
        if (A != -1) {
            Assert.assertTrue(vertice.hayIzquierdo());
            vertice = vertice.getIzquierdo();
            Assert.assertTrue(vertice.get() == A);
            vertice = vertice.getPadre();
        }
        if (B != -1) {
            Assert.assertTrue(vertice.hayDerecho());
            vertice = vertice.getDerecho();
            Assert.assertTrue(vertice.get() == B);
            vertice = vertice.getPadre();
        }
    }
}
