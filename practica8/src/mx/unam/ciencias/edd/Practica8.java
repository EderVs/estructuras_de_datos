package mx.unam.ciencias.edd;

/**
 * Práctica 8: Trayectoria mínima y algoritmo de Dijkstra.
 */
public class Practica8 {

    private static String cadena;

    public static void main(String[] args) {
        /*       3
         *    b─────d
         *  1╱│╲    │╲1
         *  ╱ │ ╲1  │ ╲
         * a 2│  ╲  │2 f
         *  ╲ │   ╲ │ ╱
         *  2╲│    ╲│╱1
         *    c─────e
         *       1           */
        Grafica<String> g = new Grafica<String>();
        g.agrega("a");
        g.agrega("b");
        g.agrega("c");
        g.agrega("d");
        g.agrega("e");
        g.agrega("f");

        g.conecta("a", "b", 1);
        g.conecta("a", "c", 2);
        g.conecta("b", "c", 2);
        g.conecta("b", "d", 3);
        g.conecta("b", "e", 1);
        g.conecta("c", "e", 1);
        g.conecta("d", "e", 2);
        g.conecta("d", "f", 1);
        g.conecta("e", "f", 1);

        /* BFS */
        g.paraCadaVertice((v) -> v.setColor(Color.ROJO));
        Cola<VerticeGrafica<String>> c = new Cola<VerticeGrafica<String>>();
        VerticeGrafica<String> vertice = g.vertice("a");
        vertice.setColor(Color.NEGRO);
        c.mete(vertice);
        cadena = "BFS 1: ";
        while (!c.esVacia()) {
            vertice = c.saca();
            cadena += vertice.getElemento() + ", ";
            for (VerticeGrafica<String> vecino : vertice.vecinos()) {
                if (vecino.getColor() == Color.ROJO) {
                    vecino.setColor(Color.NEGRO);
                    c.mete(vecino);
                }
            }
        }
        System.out.println(cadena);

        /* BFS de la clase */
        cadena = "BFS 2: ";
        g.bfs("a", (v) -> cadena += v.getElemento() + ", ");
        System.out.println(cadena);

        /* DFS */
        g.paraCadaVertice((v) -> v.setColor(Color.ROJO));
        Pila<VerticeGrafica<String>> p = new Pila<VerticeGrafica<String>>();
        vertice = g.vertice("a");
        vertice.setColor(Color.NEGRO);
        p.mete(vertice);
        cadena = "DFS 1: ";
        while (!p.esVacia()) {
            vertice = p.saca();
            cadena += vertice.getElemento() + ", ";
            for (VerticeGrafica<String> vecino : vertice.vecinos()) {
                if (vecino.getColor() == Color.ROJO) {
                    vecino.setColor(Color.NEGRO);
                    p.mete(vecino);
                }
            }
        }
        System.out.println(cadena);

        /* DFS de la clase */
        cadena = "DFS 2: ";
        g.dfs("a", (v) -> cadena += v.getElemento() + ", ");
        System.out.println(cadena);

        /* Trayectoria mínima */
        Lista<VerticeGrafica<String>> trayectoria = g.trayectoriaMinima("a", "f");
        String s = "Trayectoría mínima: ";
        for (VerticeGrafica<String> v : trayectoria)
            s += v.getElemento() + ", ";
        System.out.println(s);

        /* Dijkstra */
        Lista<VerticeGrafica<String>> dijkstra = g.dijkstra("a", "f");
        s = "Dijkstra: ";
        for (VerticeGrafica<String> v : dijkstra)
            s += v.getElemento() + ", ";
        System.out.println(s);
    }
}
