package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles AVL. La única diferencia
     * con los vértices de árbol binario, es que tienen una variable de clase
     * para la altura del vértice.
     */
    protected class VerticeAVL extends ArbolBinario<T>.Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
            this.altura = 0;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        public String toString() {
            return this.elemento + " " + this.altura + "/" + calcularBalance(this);
        }

        /**
         * Auxiliar de equals. Compara vertice por vertice.
         * @param v1 Vertice de arbol 1
         * @param v2 Vertice de arbol 2
         * @return <code>true</code> si arbol 1 y arbol 2
         *         son iguales; <code>false</code> en otro caso.
         */
        private boolean equals(VerticeAVL v1, VerticeAVL v2) {
            if (v1 == null && v2 == null) {
                return true;
            }
            if ((v1 == null && v2 != null) || (v1 != null && v2 == null) || !v1.elemento.equals(v2.elemento) || v1.altura != v2.altura) {
                return false;
            }
            return equals(verticeAVL(v1.izquierdo), verticeAVL(v2.izquierdo)) && equals(verticeAVL(v1.derecho), verticeAVL(v2.derecho));
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)o;
            return this.equals(this, vertice);
        }
    }

    /**
     * Auxiliar. Calcula la altura de cualquier vertice y lo asigna a su atributo.
     * @param vertice VerticeAVL de quien se quiere calcular la altura.
     **/
    private void calcularAltura(VerticeAVL vertice) {
        if (vertice == null) {
            return;
        }
        vertice.altura =  Math.max(this.getAltura(vertice.izquierdo), this.getAltura(vertice.derecho)) + 1;
    }

    /**
     * Auxiliar. Calcula el balance de cualquier vertice.
     * @param vertice VerticeAVL de quier se quiere calcular el balance.
     * @return el balance del vertice. 
     **/
    private int calcularBalance(VerticeAVL vertice) {
        return (vertice == null) ? 0 : this.getAltura(verticeAVL(vertice.izquierdo)) - this.getAltura(verticeAVL(vertice.derecho));
    }

    /**
     * Auxiliar. Rebalancea el arbol.
     * @param vertice VerticeAVL desde donde se va a rebalancear.
     **/
    private void rebalanceo(VerticeAVL vertice) {
        VerticeAVL d, i;
        if (vertice == null) {
            return;
        }
        this.calcularAltura(vertice);
        if (this.calcularBalance(vertice) == -2) {
            if (this.calcularBalance(verticeAVL(vertice.derecho)) == 1) {
                d = verticeAVL(vertice.derecho);
                super.giraDerecha(d);
                this.calcularAltura(d);
                this.calcularAltura(verticeAVL(d.padre));
            }
            super.giraIzquierda(vertice);
            this.calcularAltura(vertice);
        } else if (this.calcularBalance(vertice) == 2) {
            if (this.calcularBalance(verticeAVL(vertice.izquierdo)) == -1) {
                i = verticeAVL(vertice.izquierdo);
                super.giraIzquierda(i);
                this.calcularAltura(i);
                this.calcularAltura(verticeAVL(i.padre));
            }
            super.giraDerecha(vertice);
            this.calcularAltura(vertice);
        }
        this.rebalanceo(verticeAVL(vertice.padre));
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario. La complejidad en tiempo del método es <i>O</i>(log
     * <i>n</i>) garantizado.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        VerticeAVL vertice;
        super.agrega(elemento);
        vertice = verticeAVL(super.ultimoAgregado);
        this.rebalanceo(vertice);
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
     * Auxiliar de Elimina. Sube el unico vertice que puede tener el vertice padre.
     * @param padre Vertice que sera remplazado por su unico hijo.
     **/
    private void subirUnicoHijo(Vertice padre) {
        if (!padre.hayIzquierdo()) {
            this.eliminaSinHijoIzquierdo(padre);
        } else {
            this.eliminaSinHijoDerecho(padre);
        }
    }


    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo. La
     * complejidad en tiempo del método es <i>O</i>(log <i>n</i>) garantizado.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeAVL eliminar = verticeAVL(super.busca(elemento)), aux;
        if (eliminar == null) {
            return;
        }
        if (eliminar.hayIzquierdo()) {
            aux = this.verticeAVL(super.maximoEnSubarbol(eliminar.izquierdo));
            eliminar.elemento = aux.elemento;
            eliminar = aux;
        }
        if (!eliminar.hayIzquierdo() && !eliminar.hayDerecho()) {
            this.eliminaHoja(eliminar);
        } else {
            this.subirUnicoHijo(eliminar);
        }
        this.rebalanceo(verticeAVL(eliminar.padre));
    }

    /**
     * Regresa la altura del vértice AVL.
     * @param vertice el vértice del que queremos la altura.
     * @return la altura del vértice AVL.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeAVL}.
     */
    public int getAltura(VerticeArbolBinario<T> vertice) {
        return (vertice == null) ? -1 : verticeAVL(vertice).altura;
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * VerticeAVL}). Método auxililar para hacer esta audición en un único
     * lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice AVL.
     * @return el vértice recibido visto como vértice AVL.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeAVL}.
     */
    protected VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice) {
        return (VerticeAVL)vertice;
    }
}
