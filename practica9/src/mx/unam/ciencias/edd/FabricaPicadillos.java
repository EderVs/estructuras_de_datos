package mx.unam.ciencias.edd;

/**
 * Clase para fabricar picadillos.
 */
public class FabricaPicadillos {

    private static int xorHash(String str) {
        byte[] k =  str.getBytes();
        int r = 0, i = 0, t = 0, l = k.length;

        while (l >= 4) {
            r ^= (k[i] << 24) | (k[i+1] << 16) | (k[i+2] << 8) | k[i+3];
            l -= 4; i += 4;
        }

        switch (l) {
            case 3:
                t |= k[i+2] << 8;
            case 2: 
                t |= k[i+1] << 16;
            case 1: 
                t |= k[i] << 24;
        }

        return r ^ t;
    }

    private static int[] mezcla_bj(int a, int b, int c) {
        int[] arr = new int[3];
        a -= b; a -= c; a ^= (c >>> 13);
        b -= c; b -= a; b ^= (a <<  8);
        c -= a; c -= b; c ^= (b >>> 13);
        a -= b; a -= c; a ^= (c >>> 12);
        b -= c; b -= a; b ^= (a <<  16);
        c -= a; c -= b; c ^= (b >>> 5);
        a -= b; a -= c; a ^= (c >>> 3);
        b -= c; b -= a; b ^= (a <<  10);
        c -= a; c -= b; c ^= (b >>> 15);
        arr[0] = a; arr[1] = b; arr[2] = c;
        return arr;
    }

    private static int bjHash(String str) {
        byte[] k = str.getBytes();
        int n = k.length;
        int a,b,c,l;
        int[] arr = null;
        l = n;
        a = b = 0x9e3779b9;
        c = 0xffffffff;
        int i = 0;
        while (l >= 12){
            a += (k[i]   + (k[i+1] << 8) + (k[i+2]  << 16) + (k[i+3]  << 24));
            b += (k[i+4] + (k[i+5] << 8) + (k[i+6]  << 16) + (k[i+7]  << 24));
            c += (k[i+8] + (k[i+9] << 8) + (k[i+10] << 16) + (k[i+11] << 24));

            arr = mezcla_bj(a,b,c);
            a = arr[0]; b = arr[1]; c = arr[2];

            i += 12;
            l -=12;
        }
        c += n;
        switch (l) {
            case 11: c += (k[i+10] << 24);
            case 10: c += (k[i+9]  << 16);
            case  9: c += (k[i+8]  << 8);

            case  8: b += (k[i+7]  << 24);
            case  7: b += (k[i+6]  << 16);
            case  6: b += (k[i+5]  << 8);
            case  5: b +=  k[i+4];

            case  4: a += (k[i+3]  << 24);
            case  3: a += (k[i+2]  << 16);
            case  2: a += (k[i+1]  << 8);
            case  1: a += k[i];
        }

        arr = mezcla_bj(a,b,c);
        a = arr[0]; b = arr[1]; c = arr[2];

        return c;
    }

    public static int glib(String str) {
        byte[] k = str.getBytes();
        int h = 5381;
        for (int i = 0; i < k.length; i++) {
            byte b = k [i];
            h = h * 33 + b;
        }
        return h;
    }

    /**
     * Regresa una instancia de {@link Picadillo} para cadenas.
     * @param algoritmo el algoritmo de picadillo que se desea.
     * @return una instancia de {@link Picadillo} para cadenas.
     * @throws IllegalArgumentException si recibe un identificador no
     *         reconocido.
     */
    public static Picadillo<String> getInstancia(AlgoritmoPicadillo algoritmo) {
        return (entrada) -> {
            switch (algoritmo) {
                case XOR_STRING:
                    return xorHash(entrada);
                case BJ_STRING:
                    return bjHash(entrada); 
                case GLIB_STRING:
                    return glib(entrada);
                default:
                    throw new IllegalArgumentException();
            }
        };
    }
}
