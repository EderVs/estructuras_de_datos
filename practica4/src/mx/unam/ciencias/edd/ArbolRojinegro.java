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
            if (elemento == null) {
                this.color = color.NEGRO;
            } else {
                this.color = color.ROJO;
            }
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            return ((esNegro(this)) ? "N":"R") + "{" +  ((this.elemento == null) ? "null":this.elemento.toString()) + "}";
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
     * Auxiliar que dice si 2 vertices tienen diferente color.
     * @param v1 VerticeRojinegro
     * @param v2 VerticeRojinegro
     * @return <code> true </code> si lo es. <code> false </code> en otro caso.
     */
    private boolean sonVerticesBicoloreados(VerticeRojinegro v1, VerticeRojinegro v2) {
        return this.esNegro(v1) != this.esNegro(v2);
    }

    private void subirUnicoHijo(Vertice padre) {
        if (!padre.hayIzquierdo()) {
            this.eliminaSinHijoIzquierdo(padre);
        } else {
            this.eliminaSinHijoDerecho(padre);
        }
    }

    /**
     * Auxiliar que regresa el hijo de un vertice que solo tiene un hijo.
     * @param padre vertice de quien queremos su hijo
     * @return unico hijo de padre
     */
    private VerticeRojinegro getUnicoHijo(VerticeRojinegro padre) {
        if (padre.hayIzquierdo()) {
            return verticeRojinegro(padre.izquierdo);
        }
        return verticeRojinegro(padre.derecho);
    }

    private VerticeRojinegro getHermano(VerticeRojinegro vertice) {
        if (this.esHijoIzquierdo(vertice)) {
            return verticeRojinegro(vertice.padre.derecho);
        }
        return verticeRojinegro(vertice.padre.izquierdo);
    }

    private boolean esNegro(VerticeRojinegro vertice) {
        return vertice == null || vertice.color == Color.NEGRO;
    }

    private void revalanceoElimina(VerticeRojinegro vertice) {
        VerticeRojinegro hermano, padre, sobrinoIzq, sobrinoDer;
        // Caso 1
        if (!vertice.hayPadre()) {
            this.raiz = vertice;
            return;
        }
        // Caso 2
        padre = verticeRojinegro(vertice.padre);
        hermano = this.getHermano(vertice);
        if (!this.esNegro(hermano)) {
            hermano.color = Color.NEGRO;
            padre.color = Color.ROJO;
            if (this.esHijoIzquierdo(vertice)) {
                super.giraIzquierda(padre);
            } else {
                super.giraDerecha(padre);
            }
            padre = verticeRojinegro(vertice.padre);
            hermano = this.getHermano(vertice);
            System.out.println("CASO 2");
        }
        // Caso 3
        sobrinoIzq = verticeRojinegro(hermano.izquierdo);
        sobrinoDer = verticeRojinegro(hermano.derecho);
        if (this.esNegro(hermano) && this.esNegro(sobrinoIzq) && this.esNegro(sobrinoDer)) {
            if (this.esNegro(padre)) {
                System.out.println("CASO 3");
                hermano.color = Color.ROJO;
                this.revalanceoElimina(padre);
                return;
            }
            System.out.println("CASO 4");
            // Caso 4
            padre.color = Color.NEGRO;
            hermano.color = Color.ROJO;
            return;
        }
        // Caso 5
        if (this.sonVerticesBicoloreados(sobrinoIzq, sobrinoDer) && (
            // Evaluando si un sobrino es cruzado
            (this.esNegro(sobrinoIzq) && this.esHijoDerecho(vertice)) || (this.esNegro(sobrinoDer) && this.esHijoIzquierdo(vertice)))) {
            System.out.println("CASO 5");
            // Coloreamos al sobrino Rojo de Negro
            if (!this.esNegro(sobrinoIzq)) {
                sobrinoIzq.color = Color.NEGRO;
            } else {
                sobrinoDer.color = Color.NEGRO;
            }
            // Coloreamos al hermano de Rojo
            hermano.color = Color.ROJO;
            //Giramos sobre el hermano en la direccion contraria al vertice
            if (this.esHijoIzquierdo(vertice)) {
                super.giraDerecha(hermano);
            } else {
                super.giraIzquierda(hermano);
            }
        }
        System.out.println("CASO 6");
        // Caso 6
        // Coloreamos al hermano del color del padre
        hermano.color = padre.color;
        // Coloreamos al padre de negro
        padre.color = Color.NEGRO;
        // Coloreamos al sobrino cruzado de Negro
        if (this.esHijoIzquierdo(vertice)) {
            sobrinoDer.color = Color.NEGRO;
        } else {
            sobrinoIzq.color = Color.NEGRO;
        }
        // Giramos sobre el padre en la direccion del vertice
        if (this.esHijoIzquierdo(vertice)) {
            super.giraIzquierda(padre);
        } else {
            super.giraDerecha(padre);
        }
    }

    private void eliminarFantasma() {
        Vertice eliminar = this.busca(this.raiz, null);
        if (eliminar != null) {
            eliminaHoja(eliminar);
        }
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro aux, hijo;
        VerticeRojinegro eliminar = this.verticeRojinegro(super.busca(elemento));
        if (eliminar == null) {
            return;
        }
        if (eliminar.hayIzquierdo()) {
            aux = verticeRojinegro(maximoEnSubarbol(eliminar.izquierdo));
            aux.elemento = eliminar.elemento;
            eliminar = aux;
        }
        if (!eliminar.hayIzquierdo() && !eliminar.hayDerecho()) {
            eliminar.izquierdo = this.nuevoVertice(null);
            eliminar.izquierdo.padre = eliminar;
        }
        hijo = getUnicoHijo(eliminar);
        this.subirUnicoHijo(eliminar);
        hijo.color = Color.NEGRO;
        // Si tenian diferentes colores, rebalanceamos.
        if (!this.sonVerticesBicoloreados(eliminar, hijo)) {
            this.revalanceoElimina(hijo);
        }
        // Eliminamos el vertice fantasma si lo hay
        this.eliminarFantasma();
    }
}
