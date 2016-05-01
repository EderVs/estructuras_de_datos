package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase privada para iteradores de gráficas. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Grafica<T>.Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            this.iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return this.iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return this.iterador.next().getElemento();
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Vertices para gráficas; implementan la interfaz VerticeGrafica */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* Lista de vecinos. */
        public Lista<Grafica<T>.Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            this.vecinos = new Lista<Grafica<T>.Vertice>();
            this.color = Color.NINGUNO;
        }

        /* Regresa el elemento del vértice. */
        @Override public T getElemento() {
            return this.elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return this.color;
        }

        /* Define el color del vértice. */
        @Override public void setColor(Color color) {
            this.color = color;
        }

        /* Regresa un iterador para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return this.vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        this.vertices = new Lista<Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    public int getElementos() {
        return this.vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return this.aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null || this.contiene(elemento)) {
            throw new IllegalArgumentException();
        }
        Vertice v = new Vertice(elemento);   
        this.vertices.agrega(v);
    }

    /**
     * Auxiliar que busca un elemento y regresa el Vertice que lo contiene
     * @param elemento que se quiere encontrar.
     * @return Vertice que contiene al elemento.
     * @throws NoSuchElementException si no encuentra el elemento.
     **/
    private Vertice busca(T elemento){
        for (Vertice v: this.vertices) {
            if (v.elemento.equals(elemento)) {
                return v;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        Vertice av, bv;
        if (a == b) {
            throw new IllegalArgumentException();
        }
        av = this.busca(a);
        bv = this.busca(b);
        if (av.vecinos.contiene(bv)) {
            throw new IllegalArgumentException();
        }
        av.vecinos.agrega(bv);
        bv.vecinos.agrega(av);
        this.aristas += 1;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice av = this.busca(a), bv = this.busca(b);
        if (!av.vecinos.contiene(bv)) {
            throw new IllegalArgumentException();
        }
        av.vecinos.elimina(bv);
        bv.vecinos.elimina(av);
        this.aristas -= 1;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for (Vertice v: vertices) {
            if (v.elemento.equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice v = this.busca(elemento);
        for (Vertice vi: v.vecinos) {
            this.desconecta(v.elemento, vi.elemento);
        }
        vertices.elimina(v);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        Vertice av = this.busca(a), bv = this.busca(b);
        for (Vertice v: av.vecinos) {
            if (v == bv) {
                return true;
            }
        }
        return false;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        return this.busca(elemento);
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for (Vertice v: vertices) {
            accion.actua(v);
        }
    }

    /**
     * Auxiliar que recorre ya sea de manera bfs o dfs segun se le pase un MeteSaca y ejecuta una accion.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @param ms MeteSaca que vamos a Utilizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica. 
     **/
    private void recorreYActua(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> ms) {
        Vertice v = this.busca(elemento), vi;
        this.paraCadaVertice((x) -> x.setColor(Color.NINGUNO));
        ms.mete(v);
        v.color = Color.NEGRO;
        while(!ms.esVacia()) {
            vi = ms.saca();
            accion.actua(vi);
            for (Vertice vj: vi.vecinos) {
                if (vj.color == Color.NINGUNO) {
                    ms.mete(vj);
                    vj.color = Color.NEGRO;
                }
            }
        }
        this.paraCadaVertice((x) -> x.setColor(Color.NINGUNO));
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Cola<Vertice> cola = new Cola<Vertice>();
        recorreYActua(elemento, accion, cola);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Pila<Vertice> pila = new Pila<Vertice>();
        recorreYActua(elemento, accion, pila);
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacio() {
        return this.vertices.esVacio();
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
