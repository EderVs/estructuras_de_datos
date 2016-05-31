package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * Clase para conjuntos de elementos. Todos sus métodos tienen complejidad en
 * tiempo <em>O</em>(1) (amortizado), excepto por {@link union} y {@link
 * interseccion}, que son <em>O</em>(<em>n</em>).
 */
public class Conjunto<T> implements Coleccion<T> {

    /* El conjunto de elementos. */
    private Diccionario<T, T> conjunto;

    /**
     * Crea un nuevo conjunto.
     */
    public Conjunto() {
        this.conjunto = new Diccionario<T, T>();
    }

    /**
     * Crea un nuevo conjunto para un número determinado de elementos.
     * @param n el número tentativo de elementos.
     */
    public Conjunto(int n) {
        this.conjunto = new Diccionario<T, T>(n);
    }

    /**
     * Agrega un elemento al conjunto.
     * @param elemento el elemento que queremos agregar al conjunto.
     * @throws IllegalArgumentException si el elemento es <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        this.conjunto.agrega(elemento, elemento);
    }

    /**
     * Nos dice si el elemento está en el conjunto.
     * @param elemento el elemento que queremos saber si está en el conjunto.
     * @return <code>true</code> si el elemento está en el conjunto,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return this.conjunto.contiene(elemento);
    }

    /**
     * Elimina el elemento del conjunto es vacío, si está.
     * @param elemento el elemento que queremos eliminar del conjunto.
     */
    @Override public void elimina(T elemento) {
        this.conjunto.elimina(elemento);
    }

    /**
     * Nos dice si el conjunto es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al conjunto.
     * @return <code>true</code> si el objeto recibido es instancia de Conjunto,
     *         y tiene los mismos elementos.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof Conjunto))
            return false;
        @SuppressWarnings("unchecked") Conjunto<T> c = (Conjunto<T>)o;
        return c.conjunto.equals(this.conjunto);
    }

    /**
     * Nos dice si el conjunto es vacío.
     * @return <code>true</code> si el conjunto es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacio() {
        return this.conjunto.getElementos() == 0;
    }

    /**
     * Regresa el número de elementos en el conjunto.
     * @return el número de elementos en el conjunto.
     */
    @Override public int getElementos() {
        return this.conjunto.getElementos();
    }

    /**
     * Regresa la intersección del conjunto y el conjunto recibido.
     * @param conjunto el conjunto que queremos intersectar con éste.
     * @return la intersección del conjunto y el conjunto recibido.
     */
    public Conjunto<T> interseccion(Conjunto<T> conjunto) {
        Conjunto<T> interseccion = new Conjunto<T>();
        for (T elemento: this.conjunto) {
            if (conjunto.contiene(elemento)) {
                interseccion.agrega(elemento);
            }
        }
        return interseccion;
    }

    /**
     * Regresa la unión del conjunto y el conjunto recibido.
     * @param conjunto el conjunto que queremos unir con éste.
     * @return la unión del conjunto y el conjunto recibido.
     */
    public Conjunto<T> union(Conjunto<T> conjunto) {
        Conjunto<T> union = new Conjunto<T>();
        for (T elemento: this.conjunto) {
            union.agrega(elemento);
        }
        for (T elemento: conjunto) {
            union.agrega(elemento);
        }
        return union;
    }

    /**
     * Regresa un iterador para iterar el conjunto.
     * @return un iterador para iterar el conjunto.
     */
    @Override public Iterator<T> iterator() {
        return this.conjunto.iterator();
    }
}
