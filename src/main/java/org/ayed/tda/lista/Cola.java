package org.ayed.tda.lista;

public class Cola<T> {
    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int cantidadDatos;

    /**
     * Constructor de Cola.
     */
    public Cola() {
        primero = null;
        ultimo = null;
        cantidadDatos = 0;
    }

    /**
     * Constructor de copia de Cola.
     *
     * @param cola Cola a copiar.
     *             No puede ser nula.
     * @throws ExcepcionLista si la cola es nula.
     */
    public Cola(Cola<T> cola) {
        if (cola == null) {
            throw new ExcepcionLista("La cola no puede ser nula.");
        }

        primero = null;
        ultimo = null;
        cantidadDatos = 0;

        Nodo<T> actual = cola.primero;

        while (actual != null) {
            agregar(actual.dato);
            actual = actual.siguiente;
        }
    }

    /**
     * Agrega el dato al final de la cola.
     *
     * @param dato Dato a agregar.
     */
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (vacio()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            ultimo = nuevo;
        }

        cantidadDatos++;
    }

    /**
     * Elimina el siguiente dato de la cola (FIFO).
     *
     * @return el siguiente dato de la cola.
     * @throws ExcepcionLista si la cola está vacía.
     */
    public T eliminar() {
        if (vacio()) {
            throw new ExcepcionLista("La cola está vacía.");
        }

        T dato = primero.dato;

        primero = primero.siguiente;

        if (primero != null) {
            primero.anterior = null;
        } else {
            ultimo = null;
        }

        cantidadDatos--;

        return dato;
    }

    /**
     * Obtiene el siguiente dato de la cola (FIFO).
     *
     * @return el siguiente dato de la cola.
     * @throws ExcepcionLista si la cola está vacía.
     */
    public T siguiente() {
        if (vacio()) {
            throw new ExcepcionLista("La cola está vacía.");
        }

        return primero.dato;
    }

    /**
     * Obtiene el tamaño de la cola.
     *
     * @return el tamaño de la cola.
     */
    public int tamanio() {
        return cantidadDatos;
    }

    /**
     * Evalúa si la cola está vacía.
     *
     * @return true si la cola está vacía.
     */
    public boolean vacio() {
        return cantidadDatos == 0;
    }
}