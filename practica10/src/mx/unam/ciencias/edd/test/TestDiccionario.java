package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.AlgoritmoPicadillo;
import mx.unam.ciencias.edd.Arreglos;
import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Picadillo;
import mx.unam.ciencias.edd.FabricaPicadillos;
import mx.unam.ciencias.edd.Lista;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link Diccionario}.
 */
public class TestDiccionario {

    private int total;
    private Random random;
    private Diccionario<String, String> diccionario;

    private static final int N = 64;

    /**
     * Crea un diccionario para cada prueba.
     */
    public TestDiccionario() {
        random = new Random();
        total = N + random.nextInt(N);
        diccionario = new Diccionario<String, String>(total);
    }

    /**
     * Prueba unitaria para {@link Diccionario#Diccionario}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(diccionario.esVacio());
        Assert.assertTrue(diccionario.getElementos() == 0);
        Lista<String> llaves = diccionario.llaves();
        Lista<String> valores = diccionario.valores();
        Assert.assertTrue(llaves.getLongitud() == 0);
        Assert.assertTrue(valores.getLongitud() == 0);
        Assert.assertTrue(diccionario.carga() == 0.0);
        Assert.assertTrue(diccionario.colisiones() == 0);
    }

    /**
     * Prueba unitaria para {@link Diccionario#agrega}.
     */
    @Test public void testAgrega() {
        int ini = random.nextInt(10000);
        for (int i = 0; i < total * 2; i++) {
            String s = String.format("%x", ini + i * 1000);
            Assert.assertFalse(diccionario.contiene(s));
            diccionario.agrega(s, s);
            Assert.assertTrue(diccionario.getElementos() == i+1);
            Assert.assertTrue(diccionario.contiene(s));
            Assert.assertTrue(diccionario.get(s).equals(s));
            Assert.assertTrue(diccionario.carga() < Diccionario.MAXIMA_CARGA);
        }
        String k = String.format("%x", ini);
        String v = String.format("%x", ini+1);
        diccionario.agrega(k, v);
        Assert.assertTrue(diccionario.getElementos() == total*2);
        Assert.assertTrue(diccionario.contiene(k));
        Assert.assertTrue(diccionario.get(k).equals(v));
        Assert.assertTrue(diccionario.carga() < Diccionario.MAXIMA_CARGA);
        try {
            diccionario.agrega(null, "X");
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            diccionario.agrega("X", null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Diccionario#get}.
     */
    @Test public void testGet() {
        int ini = 1 + random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            Assert.assertTrue(diccionario.get(s).equals(s));
        }
        try {
            diccionario.get("00000");
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Diccionario#contiene}.
     */
    @Test public void testContiene() {
        int ini = random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            Assert.assertFalse(diccionario.contiene(s));
            diccionario.agrega(s, s);
            Assert.assertTrue(diccionario.contiene(s));
        }
        Assert.assertFalse(diccionario.contiene("00000"));
    }

    /**
     * Prueba unitaria para {@link Diccionario#elimina}.
     */
    @Test public void testElimina() {
        String[] arreglo = new String[total];
        int ini = random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            arreglo[i] = String.format("%x", ini + i * 1000);
            diccionario.agrega(arreglo[i], arreglo[i]);
        }
        for (int i = 0; i < total; i++) {
            Assert.assertTrue(diccionario.contiene(arreglo[i]));
            diccionario.elimina(arreglo[i]);
            Assert.assertFalse(diccionario.contiene(arreglo[i]));
            Assert.assertTrue(diccionario.getElementos() == total - (i+1));
            try {
                diccionario.get(arreglo[i]);
                Assert.fail();
            } catch (NoSuchElementException nsee) {}
        }
    }

    /**
     * Prueba unitaria para {@link Diccionario#llaves}.
     */
    @Test public void testLlaves() {
        String[] arreglo = new String[total];
        int ini = random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            arreglo[i] = String.format("%x", ini + i * 1000);
            diccionario.agrega(arreglo[i], arreglo[i]);
        }
        Lista<String> llaves = diccionario.llaves();
        Assert.assertTrue(llaves.getLongitud() == total);
        llaves = Lista.mergeSort(llaves);
        Arreglos.quickSort(arreglo);
        int i = 0;
        for (String k : llaves)
            Assert.assertTrue(k.equals(arreglo[i++]));
    }

    /**
     * Prueba unitaria para {@link Diccionario#valores}.
     */
    @Test public void testValores() {
        String[] arreglo = new String[total];
        int ini = random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            arreglo[i] = String.format("%x", ini + i * 1000);
            diccionario.agrega(arreglo[i], arreglo[i]);
        }
        Lista<String> valores = diccionario.valores();
        Assert.assertTrue(valores.getLongitud() == total);
        valores = Lista.mergeSort(valores);
        Arreglos.quickSort(arreglo);
        int i = 0;
        for (String k : valores)
            Assert.assertTrue(k.equals(arreglo[i++]));
    }

    /**
     * Prueba unitaria para {@link Diccionario#colisiones}.
     */
    @Test public void testColisiones() {
        Picadillo<String> hd;
        hd = FabricaPicadillos.getInstancia(AlgoritmoPicadillo.XOR_STRING);
        diccionario = new Diccionario<String, String>(total, hd);
        byte[] bs1 = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        byte[] bs2 = { 0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 0x03, 0x04 };
        String val1 = String.format("%x", random.nextInt(1000));
        String val2 = String.format("%x", random.nextInt(1000));
        diccionario.agrega(new String(bs1), val1);
        diccionario.agrega(new String(bs2), val2);
        Assert.assertTrue(diccionario.colisiones() == 1);
    }

    /**
     * Prueba unitaria para {@link Diccionario#colisionMaxima}.
     */
    @Test public void testColisionMaxima() {
        int r = random.nextInt(10000);
        String s = String.format("%x", r);
        diccionario.agrega(s, s);
        Assert.assertTrue(diccionario.colisionMaxima() == 0);
        Picadillo<String> hd;
        hd = FabricaPicadillos.getInstancia(AlgoritmoPicadillo.XOR_STRING);
        diccionario = new Diccionario<String, String>(total, hd);
        byte[] bs1 = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        byte[] bs2 = { 0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 0x03, 0x04 };
        String val1 = String.format("%x", random.nextInt(1000));
        String val2 = String.format("%x", random.nextInt(1000));
        diccionario.agrega(new String(bs1), val1);
        diccionario.agrega(new String(bs2), val2);
        Assert.assertTrue(diccionario.colisionMaxima() == 1);
    }

    /**
     * Prueba unitaria para {@link Diccionario#carga}.
     */
    @Test public void testCarga() {
        int ini = random.nextInt(10000);
        double c = 0.0;
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            Assert.assertTrue(diccionario.carga() > c);
            c = diccionario.carga();
            Assert.assertTrue(diccionario.carga() < Diccionario.MAXIMA_CARGA);
        }
        for (int i = total; i < total*4; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            Assert.assertTrue(diccionario.carga() < Diccionario.MAXIMA_CARGA);
        }
    }

    /**
     * Prueba unitaria para {@link Diccionario#getElementos}.
     */
    @Test public void testGetElementos() {
        int ini = random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            Assert.assertTrue(diccionario.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link Diccionario#esVacio}.
     */
    @Test public void testEsVacio() {
        Assert.assertTrue(diccionario.esVacio());
        int ini = random.nextInt(10000);
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            Assert.assertFalse(diccionario.esVacio());
        }
        Lista<String> llaves = diccionario.llaves();
        for (String llave : llaves) {
            Assert.assertFalse(diccionario.esVacio());
            diccionario.elimina(llave);
        }
        Assert.assertTrue(diccionario.esVacio());
    }

    /**
     * Prueba unitaria para {@link Diccionario#equals}.
     */
    @Test public void testEquals() {
        Diccionario<String, String> d2 = new Diccionario<String, String>();
        Assert.assertTrue(diccionario.equals(d2));
        int ini = random.nextInt(10000);
        String[] a = new String[total];
        for (int i = 0; i < total; i++)
            a[i] = String.format("%x", ini + i * 1000);
        for (int i = 0; i < total; i++) {
            diccionario.agrega(a[i], a[i]);
            d2.agrega(a[total - i - 1], a[total - i - 1]);
        }
        Assert.assertFalse(diccionario == d2);
        Assert.assertTrue(diccionario.equals(d2));
        for (int i = 0; i < total; i++) {
            diccionario.elimina(a[i]);
            Assert.assertFalse(diccionario.equals(d2));
            d2.elimina(a[i]);
            Assert.assertTrue(diccionario.equals(d2));
        }
        Assert.assertTrue(diccionario.esVacio());
        Assert.assertTrue(d2.esVacio());
        Assert.assertTrue(diccionario.equals(d2));
    }

    /**
     * Prueba unitaria para {@link Diccionario#iterator}.
     */
    @Test public void testIterator() {
        int ini = random.nextInt(10000);
        Lista<String> lista = new Lista<String>();
        for (int i = 0; i < total; i++) {
            String s = String.format("%x", ini + i * 1000);
            diccionario.agrega(s, s);
            lista.agregaFinal(s);
        }
        int c = 0;
        for (String s : diccionario) {
            Assert.assertTrue(lista.contiene(s));
            lista.elimina(s);
            c++;
        }
        Assert.assertTrue(c == total);
        Assert.assertTrue(lista.getLongitud() == 0);
        Lista<String> llaves = diccionario.llaves();
        for (String llave : llaves)
            diccionario.elimina(llave);
        for (String s : diccionario);
    }
}
