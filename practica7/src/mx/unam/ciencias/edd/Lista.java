package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas implementan la interfaz {@link Iterable}, y por lo tanto se
 * pueden recorrer usando la estructura de control <em>for-each</em>. Las listas
 * no aceptan a <code>null</code> como elemento.</p>
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo {
        public T elemento;
        public Nodo anterior;
        public Nodo siguiente;

        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        public Lista<T>.Nodo anterior;
        public Lista<T>.Nodo siguiente;

        public Iterador() {
            this.start();   
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return this.siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.anterior = this.siguiente;
            this.siguiente = this.siguiente.siguiente;
            return this.anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return this.anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.siguiente = this.anterior;
            this.anterior = this.anterior.anterior;
            return this.siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            this.anterior = null;
            this.siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            this.siguiente = null;
            this.anterior = rabo;
        }

        /* No implementamos este método. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return this.longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    public int getElementos() {
        return this.longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    public boolean esVacio() {
        return this.cabeza == null;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. Después de llamar este
     * método, el iterador apunta a la cabeza de la lista. El método es idéntico
     * a {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agrega(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Nodo nuevo = new Nodo(elemento);
        if (this.esVacio()) {
            this.cabeza = nuevo;
            this.rabo = nuevo;
        } else {
            this.rabo.siguiente = nuevo;
            nuevo.anterior = this.rabo;
            this.rabo = nuevo;
        }
        this.longitud += 1;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último. Después de llamar este
     * método, el iterador apunta a la cabeza de la lista.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        this.agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último. Después de llamar este
     * método, el iterador apunta a la cabeza de la lista.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Nodo nuevo = new Nodo(elemento);
        if (this.esVacio()) {
            this.cabeza = nuevo;
            this.rabo = nuevo;
        } else {
            this.cabeza.anterior = nuevo;
            nuevo.siguiente = this.cabeza;
            this.cabeza = nuevo;
        }
        this.longitud += 1;
    }

    /**
     * Busca un elemento en la lista. Si el elemento está en la lista,
     * regresa el nodo que contiene es elemento, si no, regresa null
     * @param elemento que se quiere buscar.
     */
    private Nodo buscarElemento(T elemento) {
        Nodo i = cabeza;
        while (i != null) {
            if (i.elemento.equals(elemento)) {
                break;
            }
            i = i.siguiente;
        }
        return i;
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica. Si un elemento de la lista es
     * modificado, el iterador se mueve al primer elemento.
     * @param elemento el elemento a eliminar.
     */
    public void elimina(T elemento) {
        Nodo eliminado = this.buscarElemento(elemento);
        if (eliminado == null)
            return;
        if (this.cabeza == this.rabo) {
            this.cabeza = null;
            this.rabo = null;
        } else if (this.cabeza == eliminado) {
            this.cabeza = this.cabeza.siguiente;
            this.cabeza.anterior = null;
        } else if (this.rabo == eliminado) {
            this.rabo = this.rabo.anterior;
            this.rabo.siguiente = null;
        } else {
            eliminado.anterior.siguiente = eliminado.siguiente;
            eliminado.siguiente.anterior = eliminado.anterior;
        }
        this.longitud -= 1;
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if (this.esVacio()) {
            throw new NoSuchElementException();
        }
        T eliminado = this.cabeza.elemento;
        this.cabeza = this.cabeza.siguiente;
        if (this.longitud == 1) {
            this.rabo = null;
        } else {
            this.cabeza.anterior = null;
        }
        this.longitud -= 1;
        return eliminado;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if (this.esVacio()) {
            throw new NoSuchElementException();
        }
        T eliminado = this.rabo.elemento;
        this.rabo = this.rabo.anterior;
        if (this.longitud == 1) {
            this.cabeza = null;
        } else {
            this.rabo.siguiente = null;
        }
        this.longitud -= 1;
        return eliminado;
    }

    /**
     * Nos dice si un elemento está en la lista. El iterador no se mueve.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(T elemento) {
        return this.buscarElemento(elemento) != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> lista_r = new Lista<T>();
        Nodo i = this.cabeza;
        while (i != null) {
            lista_r.agregaInicio(i.elemento);
            i = i.siguiente;
        }
        return lista_r;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        return copia(new Lista<T>(), this.cabeza);
    }

    /* Método auxiliar recursivo para copia. */
    private Lista<T> copia(Lista<T> c, Nodo nodo) {
        if (nodo == null) {
            return c;
        }
        c.agregaFinal(nodo.elemento);
        return copia(c, nodo.siguiente);
    }

    /**
     * Limpia la lista de elementos. El llamar este método es equivalente a
     * eliminar todos los elementos de la lista.
     */
    public void limpia() {
        while (this.longitud != 0) {
            this.eliminaUltimo();
        }
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if (this.esVacio()) {
            throw new NoSuchElementException();
        }
        return this.cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if (this.esVacio()) {
            throw new NoSuchElementException();
        }
        return this.rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if (i < 0 || i >= this.getLongitud()) {
            throw new ExcepcionIndiceInvalido();
        }
        Nodo it = this.cabeza;
        int n = 0;
        while (n != i) {
            it = it.siguiente;
            n += 1;
        }
        return it.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si
     *         el elemento no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        Nodo it = this.cabeza;
        int n = 0;
        while (it != null) {
            if (it.elemento.equals(elemento))
                return n;
            n += 1;
            it = it.siguiente;
        }
        return -1;
    }

    private static <T extends Comparable<T>> Lista<T> mezcla(Lista<T> l1, Lista<T> l2) {
        Lista<T>.Nodo n1 = l1.cabeza, n2 = l2.cabeza;
        Lista<T> l = new Lista<T>();
        while (n1 != null && n2 != null) {
            if (n1.elemento.compareTo(n2.elemento) < 0) {
                l.agrega(n1.elemento);
                n1 = n1.siguiente;
            } else {
                l.agrega(n2.elemento);
                n2 = n2.siguiente;
            }
        }
        n1 = (n1 == null)? n2:n1;
        while(n1 != null) {
            l.agrega(n1.elemento);
            n1 = n1.siguiente;
        }
        return l;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param l la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> l) {
        if (l.cabeza == l.rabo) {
            return l.copia();
        }
        Lista<T> l1 = new Lista<T>(), l2 = new Lista<T>();
        Lista<T>.Nodo n1 = l.cabeza, n2 = l.rabo;
        while (n1 != n2 && n1 != n2.siguiente) {
            l1.agrega(n1.elemento);
            l2.agregaInicio(n2.elemento);
            n1 = n1.siguiente;
            n2 = n2.anterior;
        }
        if (n1 == n2) {
            l1.agrega(n1.elemento);
        }
        return mezcla(mergeSort(l1), mergeSort(l2));
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param l la lista donde se buscará.
     * @param e el elemento a buscar.
     * @return <tt>true</tt> si e está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> l, T e) {
        Lista<T>.Nodo n = l.cabeza;
        while (n != null) {
            if (n.elemento.equals(e)) {
                return true; 
            }
            n = n.siguiente;
        }
        return false;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        Nodo it = this.cabeza;
        String string_lista = "[";
        while (it != null) {
            string_lista += it.elemento;
            it = it.siguiente;
            if (it != null) {
                string_lista += ", ";
            }
        }
        string_lista += "]";
        return string_lista;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof Lista))
            return false;
        else {
            @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;
            Nodo i = this.cabeza;
            Nodo i_o = lista.cabeza;
            while (i != null && i_o != null) {
                if (!i.elemento.equals(i_o.elemento)) {
                    return false;
                }
                i = i.siguiente;
                i_o = i_o.siguiente;
            }
            return (i != null) == (i_o != null);
        }
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }
}