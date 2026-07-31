package org.ayed.tda.diccionario;

import org.ayed.tda.iterador.Iterador;
import org.ayed.tda.lista.Lista;
import org.ayed.tda.tupla.Tupla;
import org.ayed.tda.vector.VectorEstatico;

/*
 * Implementación de un diccionario usando hashing.
 *
 * Internamente utiliza:
 * - Un vector estático como tabla hash.
 * - Listas enlazadas para manejar colisiones.
 *
 * Cada posición del vector contiene una lista de tuplas (clave, valor).
 */
public class Diccionario<C, V> {

    // Tabla hash donde cada posición contiene una lista de elementos
    private VectorEstatico<Lista<Tupla<C, V>>> datos;

    // Factor de carga por defecto
    private static final double FACTOR_DEFAULT = 0.75;

    // Porcentaje máximo recomendado de ocupación
    private double factorDeCarga;

    // Tamaño total de la tabla hash
    private int tamanioTabla;

    // Cantidad de elementos almacenados
    private int cantidadDatos;

    /*
     * Genera un hash numérico para una clave.
     *
     * Se mezcla el hash original con un desplazamiento
     * para distribuir mejor los valores.
     */
    private int hashear(C clave) {

        // Si la clave es null, se asigna hash 0
        if (clave == null) {
            return 0;
        }

        int hash = clave.hashCode();

        // Mezcla de bits para reducir colisiones
        return hash ^ hash >>> 16;
    }

    /*
     * Obtiene el índice de la tabla hash
     * correspondiente a una clave.
     */
    private int obtenerIndice(C clave) {
        return Math.abs(hashear(clave)) % tamanioTabla;
    }

    /*
     * Verifica si un número es primo.
     *
     * Se utiliza para elegir tamaños de tabla
     * que reduzcan colisiones.
     */
    private boolean esPrimo(int numero) {

        if (numero <= 1) {
            return false;
        }

        if (numero == 2) {
            return true;
        }

        if (numero % 2 == 0) {
            return false;
        }

        for (int i = 3; i * i <= numero; i += 2) {

            if (numero % i == 0) {
                return false;
            }
        }

        return true;
    }

    /*
     * Calcula el tamaño ideal de la tabla hash
     * según el factor de carga.
     *
     * Busca el siguiente número primo disponible.
     */
    private int calcularTamanioTabla(int tamanio) {

        int capacidad = (int) Math.ceil(tamanio / factorDeCarga);

        while (!esPrimo(capacidad)) {
            capacidad++;
        }

        return capacidad;
    }

    /*
     * Constructor principal.
     *
     * Inicializa la tabla hash y crea una lista vacía
     * en cada posición.
     */
    public Diccionario(int tamanio, double factorDeCarga) {

        if (tamanio <= 0) {
            throw new ExcepcionDiccionario("Tamaño inválido.");
        }

        if (factorDeCarga <= 0 || factorDeCarga >= 1) {
            throw new ExcepcionDiccionario("Factor de carga inválido.");
        }

        this.factorDeCarga = factorDeCarga;

        tamanioTabla = calcularTamanioTabla(tamanio);

        datos = new VectorEstatico<>(tamanioTabla);

        // Inicializa cada bucket con una lista vacía
        for (int i = 0; i < tamanioTabla; i++) {
            datos.asignar(i, new Lista<>());
        }

        cantidadDatos = 0;
    }

    /*
     * Constructor usando factor de carga por defecto.
     */
    public Diccionario(int tamanio) {
        this(tamanio, FACTOR_DEFAULT);
    }

    /*
     * Constructor copia.
     *
     * Realiza una copia del diccionario recibido.
     */
    public Diccionario(Diccionario<C, V> diccionario) {

        if (diccionario == null) {
            throw new ExcepcionDiccionario("El diccionario no puede ser null");
        }

        this.factorDeCarga = diccionario.factorDeCarga;
        this.tamanioTabla = diccionario.tamanioTabla;

        datos = new VectorEstatico<>(tamanioTabla);

        for (int i = 0; i < tamanioTabla; i++) {
            datos.asignar(i, new Lista<>(diccionario.datos.obtener(i)));
        }

        cantidadDatos = diccionario.cantidadDatos;
    }

    /*
     * Agrega un elemento al diccionario.
     *
     * Si la clave ya existe:
     * - reemplaza el valor anterior
     * - devuelve el valor viejo
     *
     * Si no existe:
     * - agrega una nueva tupla
     * - devuelve null
     */
    public V agregar(C clave, V valor) {

        int indice = obtenerIndice(clave);

        Lista<Tupla<C, V>> lista = datos.obtener(indice);

        // Busca si la clave ya existe
        for (int i = 0; i < lista.tamanio(); i++) {

            Tupla<C, V> actual = lista.dato(i);

            if ((clave == null && actual.clave() == null)
                    || (clave != null && clave.equals(actual.clave()))) {

                V viejo = actual.valor();

                // Reemplaza el valor existente
                lista.modificarDato(
                        new Tupla<>(clave, valor),
                        i);

                return viejo;
            }
        }

        // Inserta nueva clave
        lista.agregar(new Tupla<>(clave, valor));

        cantidadDatos++;

        return null;
    }

    /*
     * Elimina una clave del diccionario.
     *
     * Devuelve el valor eliminado o null
     * si la clave no existe.
     */
    public V eliminar(C clave) {

        int indice = obtenerIndice(clave);

        Lista<Tupla<C, V>> lista = datos.obtener(indice);

        for (int i = 0; i < lista.tamanio(); i++) {

            Tupla<C, V> actual = lista.dato(i);

            if ((clave == null && actual.clave() == null)
                    || (clave != null && clave.equals(actual.clave()))) {

                V valor = actual.valor();

                lista.eliminar(i);

                cantidadDatos--;

                return valor;
            }
        }

        return null;
    }

    /*
     * Busca el valor asociado a una clave.
     *
     * Devuelve:
     * - el valor encontrado
     * - null si no existe
     */
    public V obtenerValor(C clave) {

        int indice = obtenerIndice(clave);

        Lista<Tupla<C, V>> lista = datos.obtener(indice);

        for (int i = 0; i < lista.tamanio(); i++) {

            Tupla<C, V> actual = lista.dato(i);

            if ((clave == null && actual.clave() == null)
                    || (clave != null && clave.equals(actual.clave()))) {

                return actual.valor();
            }
        }

        return null;
    }

    /*
     * Devuelve la cantidad de elementos almacenados.
     */
    public int tamanio() {
        return cantidadDatos;
    }

    /*
     * Indica si el diccionario está vacío.
     */
    public boolean vacio() {
        return cantidadDatos == 0;
    }

    /*
     * Devuelve una lista con todos los valores
     * almacenados en el diccionario.
     */
    public Lista<V> valores() {

        Lista<V> valores = new Lista<>();

        Iterador<Tupla<C, V>> iterador;

        // Recorre toda la tabla hash
        for (int i = 0; i < tamanioTabla; i++) {

            iterador = this.datos.obtener(i).iterador();

            // Recorre cada lista enlazada
            while (iterador.haySiguiente()) {

                valores.agregar(iterador.dato().valor());

                iterador.siguiente();
            }
        }

        return valores;
    }
}