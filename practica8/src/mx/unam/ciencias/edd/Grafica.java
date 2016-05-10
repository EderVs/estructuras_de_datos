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
            throw new UnsupportedOperationException("Eliminar con el iterador " +
                                                    "no está soportado");
        }
    }

    /* Vecinos para gráficas; un vecino es un vértice y el peso de la arista que
     * los une. Implementan VerticeGrafica. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vecino del vértice. */
        public Grafica<T>.Vertice vecino;
        /* El peso de vecino conectando al vértice con el vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Grafica<T>.Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T getElemento() {
            return this.vecino.getElemento();
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return this.vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return this.vecino.getColor();
        }

        /* Define el color del vecino. */
        @Override public void setColor(Color color) {
            this.vecino.setColor(color);
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return this.vecino.vecinos;
        }
    }

    /* Vertices para gráficas; implementan la interfaz ComparableIndexable y
     * VerticeGrafica */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de vecinos del vértice. */
        public Lista<Grafica<T>.Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            this.color = Color.NINGUNO;
            this.vecinos = new Lista<Grafica<T>.Vecino>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T getElemento() {
            return this.elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return this.vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return this.color;
        }

        /* Define el color del vértice. */
        @Override public void setColor(Color color) {
            this.color = color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return this.vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return this.indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            return compareToInfinitos(this.distancia, vertice.distancia);
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue de la vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
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
    @Override public int getElementos() {
        return vertices.getLongitud();
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
     * gráfica. El peso de la vecino que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        Vertice av, bv;
        Vecino vca, vcb;
        if (a == b) {
            throw new IllegalArgumentException();
        }
        if (this.sonVecinos(a, b)) {
            throw new IllegalArgumentException();
        }
        av = this.busca(a);
        bv = this.busca(b);
        bv.vecinos.agrega(new Vecino(av, 1));
        av.vecinos.agrega(new Vecino(bv, 1));
        this.aristas += 1;
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b, double peso) {
        Vertice av, bv;
        Vecino vca, vcb;
        if (a == b) {
            throw new IllegalArgumentException();
        }
        if (this.sonVecinos(a, b)) {
            throw new IllegalArgumentException();
        }
        av = this.busca(a);
        bv = this.busca(b);
        bv.vecinos.agrega(new Vecino(av, peso));
        av.vecinos.agrega(new Vecino(bv, peso));
        this.aristas += 1;
    }

    private Vecino obtenerVecino(Vertice a, Vertice b) {
        for (Vecino v:a.vecinos) {
            if (v.vecino.equals(b)) {
                return v;
            }
        }
        return null;
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
        Vecino vca, vcb;
        if (!this.sonVecinos(a,b)) {
            throw new IllegalArgumentException();
        }
        vca = this.obtenerVecino(bv, av);
        vcb = this.obtenerVecino(av, bv);
        av.vecinos.elimina(vcb);
        bv.vecinos.elimina(vca);
        this.aristas -= 1;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for (Vertice v:vertices) {
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
        for (Vecino vci: v.vecinos) {
            this.desconecta(v.elemento, vci.vecino.elemento);
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
        for (Vecino v:av.vecinos) {
            if (v.vecino.equals(bv)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Regresa el peso de la vecino que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la vecino que comparten los vértices que contienen a
     *         los elementos recibidos, o -1 si los elementos no están
     *         conectados.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public double getPeso(T a, T b) {
        Vertice av = this.busca(a), bv = this.busca(b);
        if (!this.sonVecinos(a,b)) {
            return -1;
        }
        return this.obtenerVecino(av, bv).peso;
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
            for (Vecino vcj: vi.vecinos) {
                if (vcj.vecino.color == Color.NINGUNO) {
                    ms.mete(vcj.vecino);
                    vcj.vecino.color = Color.NEGRO;
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
        return vertices.esVacio();
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Hace un compareTo entre dos numeros y regresa lo indicado
     * tomando en cuenta que puede trabajar con infinitos.
     * @param a double que se quiere comparar
     * @param b double que se quiere comparar
     * @return -1 si el primer argumento es menor, 1 si es mayor
     * y 0 si son iguales.
     */
    private int compareToInfinitos(double a, double b) {
        if (a != -1 && (b == -1 || a < b)) {
            return -1;
        }
        if (b != -1 && (a == -1 || a > b)) {
            return 1;
        }
        return 0;
    }

    /**
     * Hace una suma entre dos numeros y regresa lo indicado
     * tomando en cuenta que puede trabajar con infinitos.
     * @param a double que se quiere sumar
     * @param b double que se quiere sumar
     * @return suma entre los dos valores.
     */
    private double sumaInfinitos(double a, double b) {
        if (a == -1 || b == -1) {
            return -1;
        }
        return a + b;
    }

    /**
     * Auxiliar de Trayectoria. desde el minimo del monticulo hasta
     * que este es vacio.
     * @param flag boolean que sirve como bandera para contar el peso,
     * de cada aristar o predeterminarlo con 1.
     */
    private void fijarDistancias(boolean flag) {
        MonticuloMinimo<Vertice> mm= new MonticuloMinimo<Vertice>(this.vertices);
        Vertice v;
        double pesoActual;
        while (!mm.esVacio()) {
            v = mm.elimina();
            for (Vecino vci: v.vecinos) {
                if (flag) {
                    pesoActual = vci.peso;
                } else {
                    pesoActual = 1;
                }
                if (this.compareToInfinitos(sumaInfinitos(v.distancia, pesoActual), vci.vecino.distancia) < 0) {
                    vci.vecino.distancia = sumaInfinitos(v.distancia, pesoActual);
                    mm.reordena(vci.vecino);
                }
            }
        }
    }

    /**
     * Auxiliar de trayectoria. Obtiene la trayectoria despues de haber
     * fijado las distancias.  
     * @param origen Vertice de origen.
     * @param destino Vertice de destino.
     * @param flag boolean que sirve como bandera para contar el peso,
     * de cada aristar o predeterminarlo con 1.
     * @return Lista que incluye la trayectoria a seguir.
     */
    private Lista<VerticeGrafica<T>> construirTrayectoria (Vertice origen, Vertice destino, boolean flag) {
        Lista<VerticeGrafica<T>> trayectoria = new Lista<VerticeGrafica<T>>();
        Vertice v;
        double pesoActual;
        if (destino.distancia != -1) {
            trayectoria.agrega(destino);
            v = destino;
            while (v != origen) {
                for (Vecino vci:v.vecinos) {
                    if (flag) {
                        pesoActual = vci.peso;
                    } else {
                        pesoActual = 1;
                    }
                    if (v.distancia - pesoActual == vci.vecino.distancia) {
                        trayectoria.agregaInicio(vci.vecino);
                        v = vci.vecino;
                        break;  
                    }
                }
            }
        }
        return trayectoria;
    }

    /**
     * Auxiliar de trayectoria minima y de Dijkstra. Obtiene la trayectoria deseada
     * a partir de la bandera.
     * @param origen Generico de origen.
     * @param destino Generico de destino.
     * @param flag boolean que sirve como bandera para contar el peso,
     * de cada aristar o predeterminarlo con 1.
     * @return Lista que incluye la trayectoria a seguir.
     */
    private Lista<VerticeGrafica<T>> trayectoria(T origen, T destino, boolean flag) {
        Vertice origenV = this.busca(origen), destinoV = this.busca(destino);

        for (Vertice v: vertices) {
            v.distancia = -1;
        }
        origenV.distancia = 0;
        this.fijarDistancias(flag);

        return this.construirTrayectoria(origenV, destinoV, flag);
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        return trayectoria(origen, destino, false);
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y
     *         el vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        return trayectoria(origen, destino, true);
    }
}
