package mx.unam.ciencias.edd.test;

import java.util.Random;
import mx.unam.ciencias.edd.AlgoritmoPicadillo;
import mx.unam.ciencias.edd.FabricaPicadillos;
import mx.unam.ciencias.edd.Picadillo;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link FabricaPicadillos}.
 */
public class TestFabricaPicadillos {

    private Picadillo<String> picadillo;
    private String[] mensajes = {
        "a",
        "ab",
        "abc",
        "abcd",
        "abcde",
        "abcdef",
        "abcdefg",
        "abcdefgh",
        "abcdefghi",
        "abcdefghij",
        "abcdefghijk",
        "abcdefghijkl",
        "abcdefghijklm",
        "abcdefghijklmn",
        "abcdefghijklmno",
        "abcdefghijklmnop",
        "abcdefghijklmnopq",
        "abcdefghijklmnopqr",
        "abcdefghijklmnopqrs",
        "abcdefghijklmnopqrst",
        "abcdefghijklmnopqrstu",
        "abcdefghijklmnopqrstuv",
        "abcdefghijklmnopqrstuvw",
        "abcdefghijklmnopqrstuvwx",
        "abcdefghijklmnopqrstuvwxy",
        "abcdefghijklmnopqrstuvwxyz"
    };

   private int[] picadillosBJ = {
        0x3c3b12c7, 0x87d283ec, 0xa4e034c3, 0x90cc7c5f, 0x88934524,
        0xe765b9c0, 0xebd77ecb, 0x76e1f9f5, 0x4943f137, 0xb4ac0622,
        0x1745062a, 0x76f6ebe7, 0x8a553099, 0x93618619, 0x811c8dc8,
        0xa086930d, 0xde6cebcf, 0x20326b1e, 0x8ba24f58, 0x17bb0c06,
        0xfeff3a61, 0x8f1b9516, 0x6444150c, 0x096970d1, 0x7c00ba45,
        0x3573a572,
    };

    private int[] picadillosGLib = {
        0x0002b606, 0x00597728, 0x0b885c8b, 0x7c93ee4f, 0x0f11b894,
        0xf148cb7a, 0x1a623b21, 0x66a99fa9, 0x3bdd9532, 0xb7903bdc,
        0xa997b7c7, 0xdc8eb113, 0x6e64d3e0, 0x3aff504e, 0x9ae95a7d,
        0xf814aa8d, 0xfaa9fc9e, 0x4fe990d0, 0x4d1bab43, 0xf0911417,
        0x02b3976c, 0x59268562, 0x7df73219, 0x3cdd75b1, 0xd88c2c4a,
        0xea11b604,
    };

    private int[] picadillosXOR = {
        0x61000000, 0x61620000, 0x61626300, 0x61626364, 0x04626364,
        0x04046364, 0x04040464, 0x0404040c, 0x6d04040c, 0x6d6e040c,
        0x6d6e6f0c, 0x6d6e6f60, 0x006e6f60, 0x00006f60, 0x00000060,
        0x00000010, 0x71000010, 0x71720010, 0x71727310, 0x71727364,
        0x04727364, 0x04047364, 0x04040464, 0x0404041c, 0x7d04041c,
        0x7d7e041c,
    };

    /**
     * Prueba unitaria para {@link FabricaPicadillos#getInstancia} con la
     * huella digital de Bob Jenkins.
     */
    @Test public void testGetInstanciaStringBJ() {
        Picadillo<String> bj =
            FabricaPicadillos.getInstancia(AlgoritmoPicadillo.BJ_STRING);
        int i = 0;
        for (String mensaje : mensajes)
            Assert.assertTrue(bj.picadillo(mensaje) == picadillosBJ[i++]);
    }

    /**
     * Prueba unitaria para {@link FabricaPicadillos#getInstancia} con la
     * huella digital de GLib.
     */
    @Test public void testGetInstanciaStringGLib() {
        Picadillo<String> bj =
            FabricaPicadillos.getInstancia(AlgoritmoPicadillo.GLIB_STRING);
        int i = 0;
        for (String mensaje : mensajes)
            Assert.assertTrue(bj.picadillo(mensaje) == picadillosGLib[i++]);
    }

    /**
     * Prueba unitaria para {@link FabricaPicadillos#getInstancia} con la
     * huella digital XOR.
     */
    @Test public void testGetInstanciaStringXOR() {
        Picadillo<String> bj =
            FabricaPicadillos.getInstancia(AlgoritmoPicadillo.XOR_STRING);
        int i = 0;
        for (String mensaje : mensajes)
            Assert.assertTrue(bj.picadillo(mensaje) == picadillosXOR[i++]);
    }
}
