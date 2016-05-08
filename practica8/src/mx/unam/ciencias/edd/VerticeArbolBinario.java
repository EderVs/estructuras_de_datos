package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Interfaz para vértices de árboles binarios.
 */
public interface VerticeArbolBinario<T> {

    /**
     * Nos dice si el vértice tiene vértice padre.
     * @return <tt>true</tt> si el vértice tiene vértice padre, <tt>false</tt>
     *         en otro caso.
     */
    public boolean hayPadre();

    /**
     * Nos dice si el vértice tiene vértice izquierdo.
     * @return <tt>true</tt> si el vértice tiene vértice izquierdo,
     *         <tt>false</tt> en otro caso.
     */
    public boolean hayIzquierdo();

    /**
     * Nos dice si el vértice tiene vértice derecho.
     * @return <tt>true</tt> si el vértice tiene vértice derecho, <tt>false</tt>
     *         en otro caso.
     */
    public boolean hayDerecho();

    /**
     * Regresa el vértice padre del vértice.
     * @return el vértice padre del vértice.
     * @throws NoSuchElementException si el vértice no tiene padre.
     */
    public VerticeArbolBinario<T> getPadre();

    /**
     * Regresa el vértice izquierdo del vértice.
     * @return el vértice izquierdo del vértice.
     * @throws NoSuchElementException si el vértice no tiene izquierdo.
     */
    public VerticeArbolBinario<T> getIzquierdo();

    /**
     * Regresa el vértice derecho del vértice.
     * @return el vértice derecho del vértice.
     * @throws NoSuchElementException si el vértice no tiene derecho.
     */
    public VerticeArbolBinario<T> getDerecho();

    /**
     * Regresa el elemento que contiene el vértice.
     * @return el elemento que contiene el vértice.
     */
    public T get();
}
