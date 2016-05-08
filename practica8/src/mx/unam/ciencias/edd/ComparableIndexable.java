package mx.unam.ciencias.edd;

/**
 * Interface para objetos comparables e indexables. Los objetos instancias de
 * ComparableIndexable, además de tener el método {@link Comparable#compareTo}
 * (heredado de {@link Comparable}), tienen los métodos {@link
 * ComparableIndexable#getIndice} y {@link ComparableIndexable#setIndice}, que
 * permiten obtener y actualizar el índice del elemento.
 */
public interface ComparableIndexable<T> extends Comparable<T> {

    /**
     * Regresa el índice del objeto.
     * @return el índice del objeto.
     */
    public int getIndice();

    /**
     * Actualiza el índice del objeto.
     * @param indice el nuevo índice.
     */
    public void setIndice(int indice);
}
