package org.ayed.tda.lista;

public class Pila<T> {
    private Nodo<T> ultimo;
    private int cantidadDatos;

    /**
     * Constructor de Pila.
     */
    public Pila() {
        ultimo = null;
        cantidadDatos = 0;
    }

    /**
     * Constructor de copia de Pila.
     *
     * @param pila Pila a copiar.
     *             No puede ser nula.
     * @throws ExcepcionLista si la pila es nula.
     */
    public Pila(Pila<T> pila) {
        if (pila == null) {
            throw new ExcepcionLista("La pila no puede ser nula.");
        }

        ultimo = null;
        cantidadDatos = 0;

        Lista<T> aux = new Lista<>();

        Nodo<T> actual = pila.ultimo;

        while (actual != null) {
            aux.agregar(actual.dato);
            actual = actual.anterior;
        }

        for (int i = aux.tamanio() - 1; i >= 0; i--) {
            agregar(aux.dato(i));
        }
    }

    /**
     * Agrega el dato al final de la pila.
     *
     * @param dato Dato a agregar.
     */
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        nuevo.anterior = ultimo;
        ultimo = nuevo;

        cantidadDatos++;
    }

    /**
     * Elimina el siguiente dato de la pila (LIFO).
     *
     * @return el siguiente dato de la pila.
     * @throws ExcepcionLista si la pila está vacía.
     */
    public T eliminar() {
        if (vacio()) {
            throw new ExcepcionLista("La pila está vacía.");
        }

        T dato = ultimo.dato;

        ultimo = ultimo.anterior;

        cantidadDatos--;

        return dato;
    }

    /**
     * Obtiene el siguiente dato de la pila (LIFO).
     *
     * @return el siguiente dato de la pila.
     * @throws ExcepcionLista si la pila está vacía.
     */
    public T siguiente() {
        if (vacio()) {
            throw new ExcepcionLista("La pila está vacía.");
        }

        return ultimo.dato;
    }

    /**
     * Obtiene el tamaño de la pila.
     *
     * @return el tamaño de la pila.
     */
    public int tamanio() {
        return cantidadDatos;
    }

    /**
     * Evalúa si la pila está vacía.
     *
     * @return true si la pila está vacía.
     */
    public boolean vacio() {
        return cantidadDatos == 0;
    }
}