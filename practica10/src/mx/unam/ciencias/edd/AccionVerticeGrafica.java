package mx.unam.ciencias.edd;

/**
 * Interfaz para realizar acciones sobre iteradores de gráficas.
 */
@FunctionalInterface
public interface AccionVerticeGrafica<T> {

    /**
     * Realiza una acción sobre un vértice de gráfica.
     * @param vertice el vértice sobre el que se realizará la acción.
     */
    public void actua(VerticeGrafica<T> vertice);
}

