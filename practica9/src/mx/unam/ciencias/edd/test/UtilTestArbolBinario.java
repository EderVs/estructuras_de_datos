package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase de métodos utilitarios para las pruebas unitarias de las clases que
 * extiendan {@link ArbolBinario}.
 */
public class UtilTestArbolBinario {

    /* Valida un vértice, y recursivamente valida sus hijos. */
    private static void arbolBinarioValido(VerticeArbolBinario<?> v) {
        try {
            if (v.hayIzquierdo()) {
                VerticeArbolBinario<?> i = v.getIzquierdo();
                Assert.assertTrue(i.hayPadre());
                Assert.assertTrue(i.getPadre() == v);
                arbolBinarioValido(i);
            }
            if (v.hayDerecho()) {
                VerticeArbolBinario<?> d = v.getDerecho();
                Assert.assertTrue(d.hayPadre());
                Assert.assertTrue(d.getPadre() == v);
                arbolBinarioValido(d);
            }
        } catch (NoSuchElementException sdee) {
            Assert.fail();
        }
    }

    /**
     * Valida un árbol binario. Para todos sus vértices comprueba que si un
     * vértice A tiene como hijo al vértice B, entonces el vértice B tiene al
     * vértice A como padre.
     * @param arbol el árbol a validar.
     */
    public static void arbolBinarioValido(ArbolBinario<?> arbol) {
        if (arbol.esVacio())
            return;
        VerticeArbolBinario<?> v = arbol.raiz();
        Assert.assertFalse(v.hayPadre());
        arbolBinarioValido(v);
    }
}
