package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios ordenados. */
    private class Iterador implements Iterator<T> {

        /* Pila para emular la pila de ejecución. */
        private Pila<ArbolBinario<T>.Vertice> pila;

        /* Construye un iterador con el vértice recibido. */
        public Iterador() {
            pila = new Pila<ArbolBinario<T>.Vertice>();
            if (esVacio()) {
                return;
            }
            Vertice vi = raiz;
            while(vi != null){
                pila.mete(vi);
                vi = vi.izquierdo;
            }
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento del árbol en orden. */
        @Override public T next() {
            Vertice v = pila.saca(), vi;
            if (v.hayDerecho()) {
                vi = v.derecho;
                while(vi != null){
                    pila.mete(vi);
                    vi = vi.izquierdo;
                }
            }
            return v.elemento;
            
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Auxiliar de agrega.
     * @param v Vertice donde estamos
     * @param elemento Elemento que se quiere agregar
     */
    private void agrega(Vertice v, T elemento) {
        if (elemento.compareTo(v.elemento) <= 0) {
            if (!v.hayIzquierdo()) {
                v.izquierdo = nuevoVertice(elemento);
                v.izquierdo.padre = v;
                this.ultimoAgregado = v.izquierdo;
                this.elementos++;
                return;
            }
            this.agrega(v.izquierdo, elemento);
        } else {
            if (!v.hayDerecho()) {
                v.derecho = nuevoVertice(elemento);
                v.derecho.padre = v;
                this.ultimoAgregado = v.derecho;
                this.elementos++;
                return;
            }
            this.agrega(v.derecho, elemento);
        }
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) throw new IllegalArgumentException();
        if (this.esVacio()) {
            this.raiz = this.ultimoAgregado = nuevoVertice(elemento);
            this.elementos++;
        } else {
            this.agrega(this.raiz,  elemento);
        }
    }

    /**
     * Verifica si el vertice recibido es hijo izquierdo de su padre
     * @param v vertice que se verifica.
     * @throws <code> true </code> si lo es. <code> false </code> en otro caso.
     */    
    private boolean esHijoIzquierdo(Vertice v) {
        if (!v.hayPadre()) {
            return false;
        }
        return v.padre.izquierdo == v;
    }

    /**
     * Verifica si el vertice recibido es hijo derecho de su padre
     * @param v vertice que se verifica.
     * @throws <code> true </code> si lo es. <code> false </code> en otro caso.
     */    
    private boolean esHijoDerecho(Vertice v) {
        if (!v.hayPadre()) {
            return false;
        }
        return v.padre.derecho == v;
    }


    /**
     * Auxiliar de elimina. Elimina una hoja.
     * @param eliminar el elemento a eliminar que debe ser hoja.
     */
    private void eliminaHoja(Vertice eliminar) {
        if (this.raiz == eliminar) {
            this.raiz = null;
            this.ultimoAgregado = null;
        } else if (this.esHijoIzquierdo(eliminar)) {
            eliminar.padre.izquierdo = null;
        } else {
            eliminar.padre.derecho = null;
        }
        this.elementos--;
    }

    /**
     * Auxiliar de elimina. Elimina vertice que no tiene hijo izquierdo.
     * @param eliminar el elemento a eliminar que debe no tener hijo izquierdo.
     */
    private void eliminaSinHijoIzquierdo(Vertice eliminar) {
        if (this.raiz == eliminar) {
            this.raiz = this.raiz.derecho;
            eliminar.derecho.padre = null;
        } else {
            eliminar.derecho.padre = eliminar.padre;
            if (this.esHijoIzquierdo(eliminar)) {
                eliminar.padre.izquierdo = eliminar.derecho;
            } else {
                eliminar.padre.derecho = eliminar.derecho;
            }
        }
        this.elementos--;
    }

    /**
     * Auxiliar de elimina. Elimina vertice que no tiene hijo derecho.
     * @param eliminar el elemento a eliminar que debe no tener hijo derecho.
     */
    private void eliminaSinHijoDerecho(Vertice eliminar) {
        if (this.raiz == eliminar) {
            this.raiz = this.raiz.izquierdo;
            eliminar.izquierdo.padre = null;
        } else {
            eliminar.izquierdo.padre = eliminar.padre;
            if (this.esHijoIzquierdo(eliminar)) {
                eliminar.padre.izquierdo = eliminar.izquierdo;
            } else {
                eliminar.padre.derecho = eliminar.izquierdo;
            }
        }
        this.elementos--;
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice eliminar = this.busca(this.raiz, elemento), vi;
        if (eliminar == null) {
            return;
        }
        if (eliminar.hayIzquierdo()) {
            vi = eliminar;
            eliminar = maximoEnSubarbol(eliminar.izquierdo);
            vi.elemento = eliminar.elemento;            
        }

        if (!eliminar.hayIzquierdo() && !eliminar.hayDerecho()) {
            this.eliminaHoja(eliminar);
        } else if (!eliminar.hayIzquierdo()) {
            this.eliminaSinHijoIzquierdo(eliminar);
        } else {
            this.eliminaSinHijoDerecho(eliminar);
        }
    }

    /**
     * Busca recursivamente un elemento, a partir del vértice recibido.
     * @param vertice el vértice a partir del cuál comenzar la búsqueda. Puede
     *                ser <code>null</code>.
     * @param elemento el elemento a buscar a partir del vértice.
     * @return el vértice que contiene el elemento a buscar, si se encuentra en
     *         el árbol; <code>null</code> en otro caso.
     */
    @Override protected Vertice busca(Vertice vertice, T elemento) {
        if (vertice == null) {
            return null;
        }
        if (vertice.elemento.equals(elemento)) {
            return vertice;
        }
        if (elemento.compareTo(vertice.elemento) < 0) {
            return this.busca(vertice.izquierdo, elemento);
        }
        return this.busca(vertice.derecho, elemento);
    }

    /**
     * Regresa el vértice máximo en el subárbol cuya raíz es el vértice que
     * recibe.
     * @param vertice el vértice raíz del subárbol del que queremos encontrar el
     *                máximo.
     * @return el vértice máximo el subárbol cuya raíz es el vértice que recibe.
     */
    protected Vertice maximoEnSubarbol(Vertice vertice) {
        while(vertice.hayDerecho()) {
            vertice = vertice.derecho;
        }
        return vertice;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        if (vertice == null || !vertice.hayIzquierdo()) {
            return;
        }
        Vertice v = this.vertice(vertice);
        Vertice vi = v.izquierdo;
        vi.padre = v.padre;
        if (v != this.raiz) {
            if (this.esHijoIzquierdo(v)) {
                vi.padre.izquierdo = vi;
            } else {
                vi.padre.derecho = vi;
            }
        } else {
            this.raiz = vi;
        }
        v.izquierdo = vi.derecho;
        if (vi.hayDerecho()) {
            vi.derecho.padre = v;
        }
        v.padre = vi;
        vi.derecho = v;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        if (vertice == null || !vertice.hayDerecho()) {
            return;
        }
        Vertice v = this.vertice(vertice);
        Vertice vd = v.derecho;
        vd.padre = v.padre;
        if (v != this.raiz) {
            if (this.esHijoIzquierdo(v)) {
                vd.padre.izquierdo = vd;
            } else {
                vd.padre.derecho = vd;
            }
        } else {
            this.raiz = vd;
        }
        v.derecho = vd.izquierdo;
        if (vd.hayIzquierdo()) {
            vd.izquierdo.padre = v;
        }
        v.padre = vd;
        vd.izquierdo = v;
    }
}
