package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros son autobalanceados, y por lo tanto las operaciones de
 * inserción, eliminación y búsqueda pueden realizarse en <i>O</i>(log
 * <i>n</i>).
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles rojinegros. La única
     * diferencia con los vértices de árbol binario, es que tienen un campo para
     * el color del vértice.
     */
    protected class VerticeRojinegro extends ArbolBinario<T>.Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            this.color = color.ROJO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            return ((this.color == Color.NEGRO) ? "N":"R") + "{" + this.elemento.toString() + "}";
            // Aquí va su código.
        }

        /**
         * Auxiliar de equals. Compara vertice por vertice.
         * @param v1 Vertice de arbol 1
         * @param v2 Vertice de arbol 2
         * @return <code>true</code> si arbol 1 y arbol 2
         *         son iguales; <code>false</code> en otro caso.
         */
        private boolean equals(VerticeRojinegro v1, VerticeRojinegro v2) {
            if (v1 == null && v2 == null) {
                return true;
            }
            if ((v1 == null && v2 != null) || (v1 != null && v2 == null) || !v1.elemento.equals(v2.elemento) || v1.color != v2.color) {
                return false;
            }
            return equals(verticeRojinegro(v1.izquierdo), verticeRojinegro(v2.izquierdo)) && equals(verticeRojinegro(v1.derecho), verticeRojinegro(v2.derecho));
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeRojinegro vertice = (VerticeRojinegro)o;
            return this.equals(this, vertice);
        }
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del
     *         mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * VerticeRojinegro}). Método auxililar para hacer esta audición en un único
     * lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice
     *                rojinegro.
     * @return el vértice recibido visto como vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro v = (VerticeRojinegro)vertice;
        return v;
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro verticeRN = this.verticeRojinegro(vertice);
        return verticeRN.color;
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

    private void revalanceoAgrega (VerticeRojinegro vertice) {
        VerticeRojinegro padre, tio, abuelo, aux;
        // Caso 1
        if (!vertice.hayPadre()) {
            vertice.color = Color.NEGRO;
            return;
        }
        // Caso 2
        padre = this.verticeRojinegro(vertice.padre);
        if (padre.color == Color.NEGRO) {
            return;
        }
        // Caso 3
        abuelo = this.verticeRojinegro(padre.padre);
        if (this.esHijoIzquierdo(padre)) {
            tio = this.verticeRojinegro(abuelo.derecho);
        } else {
            tio = this.verticeRojinegro(abuelo.izquierdo);
        }
        if (tio != null && tio.color == Color.ROJO) {
            tio.color = Color.NEGRO;
            padre.color = Color.NEGRO;
            abuelo.color = Color.ROJO;
            this.revalanceoAgrega(abuelo);
            return;
        }
        // Caso 4
        if (this.esHijoIzquierdo(vertice) != this.esHijoIzquierdo(padre)) {
            if (this.esHijoIzquierdo(padre)) {
                super.giraIzquierda(padre);
            } else {
                super.giraDerecha(padre);
            }
            aux = padre;
            padre = vertice;
            vertice = aux;
        }
        // Caso 5
        padre.color = Color.NEGRO;
        abuelo.color = Color.ROJO;
        if (this.esHijoIzquierdo(vertice)) {
            this.giraDerecha(abuelo);
        } else {
            this.giraIzquierda(abuelo);
        }
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        VerticeRojinegro ultimoAgregadoRN;
        super.agrega(elemento);
        ultimoAgregadoRN = this.verticeRojinegro(this.ultimoAgregado);
        this.revalanceoAgrega(ultimoAgregadoRN);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
    }
}
