package org.ayed.tda.conjunto;

import org.ayed.tda.diccionario.Diccionario;

public class Conjunto<T> {
    private final Diccionario<T, Object> datos;

    /**
     * Constructor.
     *
     * @param tamanio Cantidad de datos a guardar.
     *                Debe ser positivo.
     * @throws ExcepcionConjunto si el tamaño no es válido.
     */
    public Conjunto(int tamanio) {
        if (tamanio < 0) {
            throw new ExcepcionConjunto("El tamaño no es válido.");
        }
        datos = new Diccionario<>(tamanio);
    }

    /**
     * Constructor de copia de Conjunto.
     *
     * @param conjunto Conjunto a copiar.
     *                 No puede ser nulo.
     */
    public Conjunto(Conjunto<T> conjunto) {
        if (conjunto == null) {
            throw new ExcepcionConjunto("El conjunto no puede ser nulo.");
        }
        datos = new Diccionario<>(conjunto.datos);
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
