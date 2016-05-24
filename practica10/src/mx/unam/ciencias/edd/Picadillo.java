package mx.unam.ciencias.edd;

/**
 * Interfaz genérica para picadillos.
 */
@FunctionalInterface
public interface Picadillo<T> {

    /**
     * Calcula el picadillo del objeto recibido.
     * @param objeto el objeto del que queremos el picadillo.
     * @return el picadillo del objeto recibido.
     */
    public int picadillo(T objeto);
}
