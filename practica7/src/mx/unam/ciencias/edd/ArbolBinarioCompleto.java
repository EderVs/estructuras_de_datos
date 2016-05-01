package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios completos. */
    private class Iterador implements Iterator<T> {

        private Cola<ArbolBinario<T>.Vertice> cola;

        /* Constructor que recibe la raíz del árbol. */
        public Iterador() {
            cola = new Cola<ArbolBinario<T>.Vertice>();
            if (raiz != null) cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia();
        }

        /* Regresa el elemento siguiente. */
        @Override public T next() {
            if (cola.mira().derecho != null) {
                cola.mete(cola.mira().izquierdo);
                cola.mete(cola.mira().derecho);
            } else if (cola.mira().izquierdo != null) {
                cola.mete(cola.mira().izquierdo);
            }
            return cola.saca().elemento;
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
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
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
     * Agrega el vértice desde vi hasta que llegue al tope a la izquierda
     * @param vi donde se empieza.
     * @param n vértice que se agrega.
     */
    private void agregaIzquierda(Vertice vi, Vertice n) {
        while (vi.izquierdo != null){
            vi = vi.izquierdo;
        }
        vi.izquierdo = this.ultimoAgregado = n;
        n.padre = vi;
        this.elementos++;
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Vertice n = new Vertice(elemento);
        Vertice vi = this.ultimoAgregado;
        if (this.esVacio()) {
            this.raiz = this.ultimoAgregado = n;
            this.elementos++;
            return;
        }
        if (this.esHijoIzquierdo(vi)) {
            vi.padre.derecho = this.ultimoAgregado = n;
            n.padre = vi.padre;
            this.elementos++;
            return;
        }
        while (this.esHijoDerecho(vi)) {
            vi = vi.padre;
        }
        if (vi != this.raiz) {
            vi = vi.padre.derecho;
        }
        this.agregaIzquierda(vi, n);
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Cola<Vertice> vertices = new Cola<Vertice>();
        Vertice vi = this.raiz;
        Boolean eliminado = false;
        Vertice aux = vi;
        while(vi != ultimoAgregado) {
            aux = vi;
            if (vi.elemento.equals(elemento) && !eliminado) {
                vi.elemento = this.ultimoAgregado.elemento;
                eliminado = true;
            }
            if (vi.hayIzquierdo()) vertices.mete(vi.izquierdo);
            if (vi.hayDerecho()) vertices.mete(vi.derecho);
            vi = vertices.saca();
        }
        if (eliminado || (!eliminado && vi.elemento.equals(elemento))) {
            this.elementos--;
            if (vi == this.raiz) {
                this.raiz = this.ultimoAgregado = null;
            } else if (this.esHijoDerecho(vi)) {
                vi.padre.derecho = null;
                this.ultimoAgregado = aux;
            } else {
                vi.padre.izquierdo = null;
                this.ultimoAgregado = aux;
            }
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
