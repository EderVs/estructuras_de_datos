package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.AccionVerticeGrafica;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeGrafica;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link Grafica}.
 */
public class TestGrafica {

    private int total;
    private Random random;
    private Grafica<Integer> grafica;

    /**
     * Crea una gráfica para cada prueba.
     */
    public TestGrafica() {
        random = new Random();
        total = 2 + random.nextInt(100);
        grafica = new Grafica<Integer>();
    }

    /**
     * Prueba unitaria para {@link Grafica#Grafica}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(grafica.esVacio());
        Assert.assertTrue(grafica.getElementos() == 0);
        Assert.assertTrue(grafica.getAristas() == 0);
    }

    /**
     * Prueba unitaria para {@link Grafica#getElementos}.
     */
    @Test public void testGetVertices() {
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);
            Assert.assertTrue(grafica.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link Grafica#getAristas}.
     */
    @Test public void testGetAristas() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        int cont = 0;
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                grafica.conecta(i, j);
                cont++;
                Assert.assertTrue(grafica.getAristas() == cont);
                Assert.assertTrue(grafica.sonVecinos(i, j));
            }
        }
    }

    /**
     * Prueba unitaria para {@link Grafica#agrega}.
     */
    @Test public void testAgrega() {
        try {
            grafica.agrega(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);
            Assert.assertTrue(grafica.contiene(i));
        }
        try {
            grafica.agrega(total/2);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#conecta}.
     */
    @Test public void testConecta() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j);
                Assert.assertTrue(grafica.sonVecinos(i, j));
            }
        }
        try {
            grafica.conecta(0, 0);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            grafica.conecta(-1, -2);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            grafica.conecta(0, 1);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#conecta(Object,Object,double)}.
     */
    @Test public void testConectaInt() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j, i+j);
                Assert.assertTrue(grafica.sonVecinos(i, j));
                Assert.assertTrue(grafica.getPeso(i, j) == i+j);
            }
        }
        try {
            grafica.conecta(0, 0, 1);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            grafica.conecta(-1, -2, 1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            grafica.conecta(0, 1, 1);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        grafica = new Grafica<Integer>();
        grafica.agrega(1);
        grafica.agrega(2);
        try {
            grafica.conecta(1, 2, 0);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            grafica.conecta(1, 2, -1);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#desconecta}.
     */
    @Test public void testDesconecta() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        int c = (total * (total-1)) / 2;
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertTrue(grafica.getAristas() == c--);
                Assert.assertTrue(grafica.sonVecinos(i, j));
                grafica.desconecta(i, j);
                Assert.assertFalse(grafica.sonVecinos(i, j));
            }
        }
        try {
            grafica.desconecta(0, 0);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            grafica.desconecta(-1, -2);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#contiene}.
     */
    @Test public void testContiene() {
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);
            Assert.assertTrue(grafica.contiene(i));
        }
        Assert.assertFalse(grafica.contiene(-1));
    }

    /**
     * Prueba unitaria para {@link Grafica#elimina}.
     */
    @Test public void testElimina() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        int vertices = total;
        int aristas = (total * (total - 1)) / 2;
        int[] grado = { vertices - 1 };
        for (int i = 0; i < total; i++) {
            grafica.paraCadaVertice((v) -> Assert.assertTrue(v.getGrado() ==
                                                             grado[0]));
            Assert.assertTrue(grafica.getElementos() == vertices);
            Assert.assertTrue(grafica.getAristas() == aristas);
            grafica.elimina(i);
            vertices--;
            aristas -= vertices;
            grado[0]--;
        }
        Assert.assertTrue(grafica.esVacio());
        Assert.assertTrue(grafica.getElementos() == 0);
        Assert.assertTrue(grafica.getAristas() == 0);
    }

    /**
     * Prueba unitaria para {@link Grafica#sonVecinos}.
     */
    @Test public void testSonVecinos() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j);
                Assert.assertTrue(grafica.sonVecinos(i, j));
            }
        }
        try {
            grafica.sonVecinos(-1, -2);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

   /**
     * Prueba unitaria para {@link Grafica#getPeso}.
     */
    @Test public void testGetPeso() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                grafica.conecta(i, j, i+j);
                Assert.assertTrue(grafica.getPeso(i, j) == i+j);
            }
        }
        try {
            grafica.getPeso(-1, -2);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            grafica.getPeso(1, 1);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

   /**
     * Prueba unitaria para {@link Grafica#vertice}.
     */
    @Test public void testVertice() {
        try {
            grafica.vertice(-1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        VerticeGrafica<Integer> vertice = grafica.vertice(0);
        Assert.assertTrue(vertice.getElemento() == 0);
        Assert.assertTrue(vertice.getGrado() == total - 1);
        Assert.assertTrue(vertice.getColor() == Color.NINGUNO);
        vertice.setColor(Color.ROJO);
        Assert.assertTrue(vertice.getColor() == Color.ROJO);
        int v = 1;
        Lista<Integer> vecinos = new Lista<Integer>();
        for (int i = 1; i < total; i++)
            vecinos.agrega(i);
        for (VerticeGrafica<Integer> vecino : vertice.vecinos()) {
            Assert.assertTrue(vecinos.contiene(vecino.getElemento()));
            vecinos.elimina(vecino.getElemento());
        }
        Assert.assertTrue(vecinos.esVacio());
        Assert.assertTrue(vecinos.getLongitud() == 0);
    }

   /**
     * Prueba unitaria para {@link Grafica#paraCadaVertice}.
     */
    @Test public void testParaCadaVertice() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        int[] cont = { 0 };
        grafica.paraCadaVertice((v) -> {
                v.setColor(Color.ROJO);
                cont[0]++;
            });
        Assert.assertTrue(cont[0] == total);
        grafica.paraCadaVertice((v) -> Assert.assertTrue(v.getColor() ==
                                                         Color.ROJO));
    }

   /**
     * Prueba unitaria para {@link Grafica#bfs}.
     */
    @Test public void testBfs() {
        for (int i = 0; i < 7; i++)
            grafica.agrega(i);
        grafica.conecta(0, 1);
        grafica.conecta(0, 2);
        grafica.conecta(1, 3);
        grafica.conecta(1, 4);
        grafica.conecta(3, 5);
        grafica.conecta(3, 6);
        int[] c = { 0 };
        int[] a = { 0, 1, 2, 3, 4, 5, 6 };
        grafica.bfs(0, (v) -> Assert.assertTrue(v.getElemento() == a[c[0]++]));
        grafica = new Grafica<Integer>();
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        Lista<Integer> lista = new Lista<Integer>();
        grafica.bfs(0, (v) -> lista.agrega(v.getElemento()));
        Lista<Integer> vertices = new Lista<Integer>();
        for (int i = 0; i < total; i++)
            vertices.agrega(i);
        Assert.assertTrue(lista.getLongitud() == vertices.getLongitud());
        for (Integer n : lista)
            Assert.assertTrue(vertices.contiene(lista.eliminaPrimero()));
        grafica.paraCadaVertice((v) -> Assert.assertTrue(v.getColor() ==
                                                         Color.NINGUNO));
    }

    /**
     * Prueba unitaria para {@link Grafica#dfs}.
     */
    @Test public void testDfs() {
        for (int i = 0; i < 7; i++)
            grafica.agrega(i);
        grafica.conecta(0, 1);
        grafica.conecta(0, 2);
        grafica.conecta(1, 3);
        grafica.conecta(1, 4);
        grafica.conecta(3, 5);
        grafica.conecta(3, 6);
        int[] c = { 0 };
        int[] a = { 0, 2, 1, 4, 3, 6, 5 };
        grafica.dfs(0, (v) -> Assert.assertTrue(v.getElemento() == a[c[0]++]));
        grafica = new Grafica<Integer>();
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        Lista<Integer> lista = new Lista<Integer>();
        grafica.dfs(0, (v) -> lista.agrega(v.getElemento()));
        Lista<Integer> vertices = new Lista<Integer>();
        for (int i = 0; i < total; i++)
            vertices.agrega(i);
        Assert.assertTrue(lista.getLongitud() == vertices.getLongitud());
        for (Integer n : lista)
            Assert.assertTrue(vertices.contiene(lista.eliminaPrimero()));
        grafica.paraCadaVertice((v) -> Assert.assertTrue(v.getColor() ==
                                                         Color.NINGUNO));
    }

    /**
     * Prueba unitaria para {@link Grafica#esVacio}.
     */
    @Test public void testEsVacio() {
        Assert.assertTrue(grafica.esVacio());
        grafica.agrega(0);
        Assert.assertFalse(grafica.esVacio());
    }

    /**
     * Prueba unitaria para {@link Grafica#iterator}.
     */
    @Test public void testIterator() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        int c = 0;
        for (Integer i : grafica)
            Assert.assertTrue(i == c++);
    }

    /**
     * Prueba unitaria para la implementación {@link VerticeGrafica#getElemento}
     * a través del método {@link Grafica#vertice}.
     */
    @Test public void testVerticeGetElemento() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            VerticeGrafica<Integer> vertice = grafica.vertice(i);
            Assert.assertTrue(i == vertice.getElemento());
        }
    }

    /**
     * Prueba unitaria para la implementación {@link VerticeGrafica#getGrado}
     * a través del método {@link Grafica#vertice}.
     */
    @Test public void testVerticeGetGrado() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        for (int i = 0; i < total; i++) {
            VerticeGrafica<Integer> vertice = grafica.vertice(i);
            Assert.assertTrue(vertice.getGrado() == total - 1);
        }
    }

    /**
     * Prueba unitaria para las implementaciones de {@link
     * VerticeGrafica#getColor} y {@link VerticeGrafica#setColor} a través del
     * método {@link Grafica#vertice}.
     */
    @Test public void testVerticeGetSetColor() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            VerticeGrafica<Integer> vertice = grafica.vertice(i);
            Assert.assertTrue(vertice.getColor() == Color.NINGUNO);
            vertice.setColor(Color.ROJO);
            Assert.assertTrue(vertice.getColor() == Color.ROJO);
        }
    }

    /**
     * Prueba unitaria para la implementación de {@link VerticeGrafica#vecinos}
     * a través del método {@link Grafica#vertice}.
     */
    @Test public void testVerticeVecinos() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        for (int i = 0; i < total; i++) {
            VerticeGrafica<Integer> vertice = grafica.vertice(i);
            int c = 0;
            for (VerticeGrafica<Integer> vecino : vertice.vecinos()) {
                int e = vecino.getElemento();
                Assert.assertTrue(e >= 0);
                Assert.assertTrue(e < total);
                Assert.assertFalse(e == i);
                c++;
            }
            Assert.assertTrue(vertice.getGrado() == c);
        }
    }

    /* Crea una gráfica específica para correr TrayectoriaMinima y
     * Dijsktra. */
    private void llenaGrafica() {
        grafica.agrega(1);
        grafica.agrega(2);
        grafica.agrega(3);
        grafica.agrega(4);
        grafica.agrega(5);
        grafica.agrega(6);
        grafica.conecta(1, 2, 1);
        grafica.conecta(1, 3, 2);
        grafica.conecta(2, 3, 2);
        grafica.conecta(2, 4, 3);
        grafica.conecta(2, 5, 1);
        grafica.conecta(3, 5, 1);
        grafica.conecta(4, 5, 2);
        grafica.conecta(4, 6, 1);
        grafica.conecta(5, 6, 1);
    }

    /**
     * Prueba unitaria para {@link Grafica#trayectoriaMinima}.
     */
    @Test public void testTreyactoriaMinima() {
        llenaGrafica();
        Lista<VerticeGrafica<Integer>> trayectoria = grafica.trayectoriaMinima(1, 6);
        Assert.assertTrue(trayectoria.getLongitud() == 4);
        int[] t = { 1, 2, 4, 6 };
        int c = 0;
        for (VerticeGrafica<Integer> v : trayectoria)
            Assert.assertTrue(v.getElemento() == t[c++]);
        grafica = new Grafica<Integer>();
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 1; i < total; i++)
            grafica.conecta(i - 1, i);
        trayectoria = grafica.trayectoriaMinima(0, total - 1);
        Assert.assertTrue(trayectoria.getLongitud() == total);
        c = 0;
        for (VerticeGrafica<Integer> v : trayectoria)
            Assert.assertTrue(v.getElemento() == c++);
    }

   /**
     * Prueba unitaria para {@link Grafica#dijkstra}.
     */
    @Test public void testDijkstra() {
        llenaGrafica();
        Lista<VerticeGrafica<Integer>> dijkstra = grafica.dijkstra(1, 6);
        Assert.assertTrue(dijkstra.getLongitud() == 4);
        int[] t = { 1, 2, 5, 6 };
        int c = 0;
        for (VerticeGrafica<Integer> v : dijkstra)
            Assert.assertTrue(v.getElemento() == t[c++]);
        grafica = new Grafica<Integer>();
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 1; i < total; i++)
            grafica.conecta(i - 1, i, 1.0);
        grafica.conecta(total - 1, 0, total*10);
        dijkstra = grafica.dijkstra(0, total - 1);
        Assert.assertTrue(dijkstra.getLongitud() == total);
        c = 0;
        for (VerticeGrafica<Integer> v : dijkstra)
            Assert.assertTrue(v.getElemento() == c++);
    }
}
