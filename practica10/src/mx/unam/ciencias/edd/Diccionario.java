package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, permitiendo (en general, dependiendo de qué tan bueno
 * sea su método para generar picadillos) agregar, eliminar, y buscar valores en
 * tiempo <i>O</i>(1) (amortizado) en cada uno de estos casos.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Clase para las entradas del diccionario. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
            this.llave = llave;
			this.valor = valor;
        }
    }

    /* Clase privada para iteradores de diccionarios. */
    private class Iterador implements Iterator<V> {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Diccionario<K,V>.Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
			Lista<K> llaves = llaves();
			Lista<Diccionario<K,V>.Entrada> iterador = new Lista<Diccionario<K,V>.Entrada>();
			for (K llave: llaves) {
				iterador.agrega(getEntrada(llave));
			}
			this.iterador = iterador.iterator();
		}
        /* Nos dice si hay un siguiente elemento. */
        public boolean hasNext() {
			return this.iterador.hasNext();
		}

        /* Regresa el siguiente elemento. */
        public V next() {
            return this.iterador.next().valor;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Tamaño mínimo; decidido arbitrariamente a 2^6. */
    private static final int MIN_N = 64;

    /* Máscara para no usar módulo. */
    private int mascara;
    /* Picadillo. */
    private Picadillo<K> picadillo;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores*/
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private Lista<Entrada>[] nuevoArreglo(int n) {
        Lista[] arreglo = new Lista[n];
        return (Lista<Entrada>[])arreglo;
    }

    /**
     * Construye un diccionario con un tamaño inicial y picadillo
     * predeterminados.
     */
    public Diccionario() {
        this.mascara = MIN_N-1;
		this.picadillo = ((K o) -> o.hashCode());
		this.entradas = nuevoArreglo(mascara+1);
	}

	private int calcularMascara(int tam) {
		int m = 1;
		while (m <= tam) {
			m = (m << 1) | 1;
		}
		m = (m << 1) | 1;
		return m;
	}

    /**
     * Construye un diccionario con un tamaño inicial definido por el usuario, y
     * un picadillo predeterminado.
     * @param tam el tamaño a utilizar.
     */
    public Diccionario(int tam) {
        this.mascara = this.calcularMascara(tam);
		this.picadillo = ((K o) -> o.hashCode());
		this.entradas = nuevoArreglo(mascara+1);
    }

    /**
     * Construye un diccionario con un tamaño inicial predeterminado, y un
     * picadillo definido por el usuario.
     * @param picadillo el picadillo a utilizar.
     */
    public Diccionario(Picadillo<K> picadillo) {
        this.mascara = MIN_N-1;
		this.picadillo = picadillo;
		this.entradas = nuevoArreglo(mascara+1);
    }

    /**
     * Construye un diccionario con un tamaño inicial, y un método de picadillo
     * definidos por el usuario.
     * @param tam el tamaño del diccionario.
     * @param picadillo el picadillo a utilizar.
     */
    public Diccionario(int tam, Picadillo<K> picadillo) {
        this.mascara = this.calcularMascara(tam);
		this.picadillo = picadillo;
		this.entradas = nuevoArreglo(mascara+1);
    }

	private int aplicarHash(K llave) {
		return (this.picadillo.picadillo(llave) & this.mascara);
	}

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        Entrada entrada = new Entrada(llave, valor);
		Lista<Entrada>[] nuevo;
		int i;

		if (llave == null || valor == null) {
			throw new IllegalArgumentException();
		}

		i = this.aplicarHash(llave);
		if (entradas[i] == null) {
			entradas[i] = new Lista<Entrada>();
		} else {
			for (Entrada e: entradas[i]) {
				if (e.llave.equals(llave)) {
					e.valor = valor;
					return;
				}
			}	
		}
		entradas[i].agrega(entrada);
		this.elementos++;

		if (this.carga() >= this.MAXIMA_CARGA) {
			mascara *= 2;
			nuevo = this.nuevoArreglo(mascara+1);
			for (Lista<Entrada> l: entradas) {
				if (l != null) {
					for (Entrada e: l) {
						i = this.aplicarHash(e.llave);
						if (nuevo[i] == null) {
							nuevo[i] = new Lista<Entrada>();
						}
						nuevo[i].agrega(e);
					}
				}
			}
			this.entradas = nuevo;
		}
		
    }

	private Entrada getEntrada(K llave) {
		int i = this.aplicarHash(llave);
		if(this.entradas[i] != null) {
			for (Entrada e:entradas[i]) {
				if (e.llave.equals(llave)) {
					return e;
				}
			}
		}
		throw new NoSuchElementException();
	}

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
    	return this.getEntrada(llave).valor;
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <tt>true</tt> si la llave está en el diccionario,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(K llave) {
		if (llave != null) {
			int i = this.aplicarHash(llave);
			if (this.entradas[i] != null) {
				for (Entrada e:entradas[i]) {
					if (e.llave.equals(llave)) {
						return true;
					}
				}
			}
		}
		return false;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        int i = this.aplicarHash(llave);
		if (this.entradas[i] != null) {
			for (Entrada e:entradas[i]) {
				if (e.llave.equals(llave)) {
					entradas[i].elimina(e);
					this.elementos--;
					return;
				}
			}
		}
		throw new NoSuchElementException();
    }

    /**
     * Regresa una lista con todas las llaves con valores asociados en el
     * diccionario. La lista no tiene ningún tipo de orden.
     * @return una lista con todas las llaves.
     */
    public Lista<K> llaves() {
		Lista<K> llaves = new Lista<K>();
    	for (Lista<Entrada> l : this.entradas) {
			if (l != null) {
				for (Entrada e : l) {
					llaves.agrega(e.llave);
				}
			}
		}
		return llaves;
    }

    /**
     * Regresa una lista con todos los valores en el diccionario. La lista no
     * tiene ningún tipo de orden.
     * @return una lista con todos los valores.
     */
    public Lista<V> valores() {
        Lista<V> valores = new Lista<V>();
    	for (Lista<Entrada> l : this.entradas) {
			if (l != null) {
				for (Entrada e : l) {
					valores.agrega(e.valor);
				}
			}
		}
		return valores;
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
    	int colisiones = 0;
		for (Lista<Entrada> l : this.entradas) {
			if (l != null) {
				colisiones += l.getLongitud()-1;	
			}
		}
		return colisiones;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        int colision, colisionMaxima = 0;
		for (Lista<Entrada> l : this.entradas) {
			if (l != null) {
				colision = l.getLongitud()-1;
				if (colisionMaxima < colision) {
					colisionMaxima = colision;
				}	
			}
		}
		return colisionMaxima;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
		return (this.elementos + 0.0)/this.entradas.length;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {    
		return this.elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacio() {
        return this.elementos == 0;
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof Diccionario))
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d = (Diccionario<K, V>)o;
        Lista<K> llaves = this.llaves(), llaves_d = d.llaves();
		if (llaves.getLongitud() != llaves_d.getLongitud()) {
			return false;
		}
		for (K llave :llaves) {
			if (!(d.contiene(llave) && d.get(llave).equals(this.get(llave)))) {
				return false;
			}
		}
		return true;
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar el diccionario.
     */
    @Override public Iterator<V> iterator() {
    	return new Iterador();   
	}
}
