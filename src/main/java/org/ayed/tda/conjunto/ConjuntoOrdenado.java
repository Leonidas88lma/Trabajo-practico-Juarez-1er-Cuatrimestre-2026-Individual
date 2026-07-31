package org.ayed.tda.conjunto;

import org.ayed.tda.comparador.Comparador;
import org.ayed.tda.diccionario.DiccionarioOrdenado;

public class ConjuntoOrdenado<T> {
    private final DiccionarioOrdenado<T, Object> datos;

    /**
     * Constructor.
     *
     * @param comparador Comparador a utilizar.
     */
    public ConjuntoOrdenado(Comparador<T> comparador) {
        datos = new DiccionarioOrdenado<>(comparador);
    }

    /**
     * Constructor de copia de ConjuntoOrdenado.
     *
     * @param conjuntoOrdenado Conjunto a copiar.
     *                         No puede ser nulo.
     */
    public ConjuntoOrdenado(ConjuntoOrdenado<T> conjuntoOrdenado) {
        if (conjuntoOrdenado == null) {
            throw new ExcepcionConjunto("El conjunto no puede ser nulo.");
        }
        datos = new DiccionarioOrdenado<>(conjuntoOrdenado.datos);
    }

    /**
     * Agrega un dato al conjunto.
     * Si el dato ya existía, el conjunto
     * queda en el mismo estado.
     *
     * @param dato Dato a agregar.
     * @return true si se agregó el dato.
     */
    public boolean agregar(T dato) {
        return datos.agregar(dato, new Object()) == null;
    }

    /**
     * Elimina un dato del conjunto.
     * Si el dato no existía, el conjunto
     * queda en el mismo estado.
     *
     * @param dato Dato a eliminar.
     * @return true si se eliminó el dato.
     */
    public boolean eliminar(T dato) {
        return datos.eliminar(dato) != null;
    }

    /**
     * Evalúa si el dato está en el conjunto.
     *
     * @param dato Dato a evaluar.
     * @return true si el dato está en el conjunto.
     */
    public boolean contiene(T dato) {
        return datos.obtenerValor(dato) != null;
    }

    /**
     * Obtiene el tamaño del conjunto.
     *
     * @return el tamaño del conjunto.
     */
    public int tamanio() {
        return datos.tamanio();
    }

    /**
     * Evalúa si el conjunto está vacío.
     *
     * @return true si el conjunto está vacío.
     */
    public boolean vacio() {
        return datos.vacio();
    }
}
