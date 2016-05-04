package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>). Podemos crear un montículo
 * mínimo con <em>n</em> elementos en tiempo <em>O</em>(<em>n</em>), y podemos
 * agregar y actualizar elementos en tiempo <em>O</em>(log <em>n</em>). Eliminar
 * el elemento mínimo también nos toma tiempo <em>O</em>(log <em>n</em>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T> {

    /* Clase privada para iteradores de montículos. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return (indice < arbol.length) && arbol[indice] != null;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (indice >= arbol.length) {
                throw new NoSuchElementException();
            }
            return arbol[indice++];
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* El siguiente índice dónde agregar un elemento. */
    private int siguiente;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] creaArregloGenerico(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Lista)}, pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        this.arbol = creaArregloGenerico(1);
    }

    /**
     * Constructor para montículo mínimo que recibe una lista. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param lista la lista a partir de la cuál queremos construir el
     *              montículo.
     */
    public MonticuloMinimo(Lista<T> lista) {
        int i = 0;
        this.arbol = this.creaArregloGenerico(lista.getLongitud());
        for (T e: lista) {
            this.arbol[i] = e;
            e.setIndice(i++);
        }
        for (i = (arbol.length - 1) / 2; i >= 0; i--) {
            this.minHeapify(arbol[i]);
        }
        siguiente = arbol.length;
    }

    private boolean indiceValido(int i) {
        return !(i < 0 || i >= this.arbol.length || arbol[i] == null);
    }

    private void reordenaArriba(T elemento) {
        int padre = (elemento.getIndice() - 1);
        if (padre != -1) {
            padre /= 2;
        }
        if (!this.indiceValido(padre) || arbol[padre].compareTo(elemento) < 0) {
            return;
        }
        this.intercambia(arbol[padre], elemento);
        this.reordenaArriba(elemento);
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        T[] aux;
        int i = 0;
        if (siguiente >= arbol.length) {
            aux = this.arbol;
            this.arbol = this.creaArregloGenerico(siguiente * 2);
            for (T e: aux) {
                arbol[i++] = e;
            }
        }
        arbol[siguiente] = elemento;
        elemento.setIndice(siguiente++);
        reordenaArriba(elemento);
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    public T elimina() {
        T min;
        if (this.esVacio()) {
            throw new IllegalStateException();
        }
        min = arbol[0];
        this.intercambia(arbol[0], arbol[--siguiente]);
        arbol[siguiente].setIndice(-1);
        arbol[siguiente] = null;
        minHeapify(arbol[0]);
        return min;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        int aux = elemento.getIndice();
        this.intercambia(elemento, arbol[--siguiente]);
        arbol[siguiente] = null;
        elemento.setIndice(-1);
        this.reordena(arbol[aux]);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for (T e: arbol) {
            if (e == null) {
                return false;
            }
            if (e.equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean esVacio() {
        return this.siguiente == 0;
    }

    private void intercambia(T i, T j) {
        int aux = j.getIndice();
        arbol[i.getIndice()] = j;
        arbol[j.getIndice()] = i;
        j.setIndice(i.getIndice());
        i.setIndice(aux);
    }

    private void minHeapify(T elemento) {
        int izq, der;
        int min;
        if (elemento == null) {
            return;
        }
        izq = elemento.getIndice() * 2 + 1;
        der = elemento.getIndice() * 2 + 2;
        if (!this.indiceValido(izq) && !this.indiceValido(der)) {
            return;
        }
        
        min = der;
        if (this.indiceValido(izq)) {
            if (this.indiceValido(der)) {
                if (arbol[izq].compareTo(arbol[der]) < 0) {
                    min = izq;
                }   
            } else {
                min = izq;
            }
        }

        if (arbol[min].compareTo(elemento) < 0) {
            this.intercambia(elemento, arbol[min]);
            this.minHeapify(elemento);
        }
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    public void reordena(T elemento) {
        int  padre;
        if (elemento == null) {
            return;
        }
        padre = (elemento.getIndice()-1);
        if (padre != -1) {
            padre /= 2;
        }
        if (!this.indiceValido(padre) || arbol[padre].compareTo(elemento)<=0) {
            this.minHeapify(elemento);
        } else {
            this.reordenaArriba(elemento);
        }
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return this.siguiente;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    public T get(int i) {
        if (!this.indiceValido(i)) {
            throw new NoSuchElementException();
        }
        return arbol[i];
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
