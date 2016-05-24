package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     */
    @Override public void mete(T elemento) {
        if (elemento == null) throw new IllegalArgumentException();
        Nodo n = new Nodo(elemento);
        if (this.esVacia()) {
        	this.rabo = n;
        } else {
        	n.siguiente = this.cabeza;
        }
        this.cabeza = n;
    }
}
