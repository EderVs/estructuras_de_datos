package mx.unam.ciencias.edd;

/**
 * Práctica 7: Montículos mínimos.
 */
public class Practica7 {

    public static void main(String[] args) {
        String[] abc = { "a", "b", "c", "d", "e", "f", "g",
                         "h", "i", "j", "k", "l", "m", "n",
                         "o", "p", "q", "r", "s", "t", "u",
                         "v", "w", "x", "y", "z" };
        Lista<Indexable<String>> lista = new Lista<Indexable<String>>();
        for (int i = 0; i < abc.length; i++) {
            Indexable<String> indexable = new Indexable<String>(abc[i], i);
            lista.agregaFinal(indexable);
        }
        MonticuloMinimo<Indexable<String>> monticulo;
        monticulo = new MonticuloMinimo<Indexable<String>>(lista);
        while (!monticulo.esVacio()) {
            Indexable<String> indexable = monticulo.elimina();
            System.out.print(indexable.getElemento() + " ");
        }
        System.out.println();
    }
}
