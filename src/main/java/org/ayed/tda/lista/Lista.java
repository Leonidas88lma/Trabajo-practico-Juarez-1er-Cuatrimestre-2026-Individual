package org.ayed.tda.lista;

import org.ayed.tda.iterador.Iterador;

public class Lista<T> {

    Nodo<T> primero;
    Nodo<T> ultimo;
    int cantidadDatos;

    /**
     * Constructor de Lista.
     */
    public Lista() {
        primero = null;
        ultimo = null;
        cantidadDatos = 0;
    }

    /**
     * Constructor de copia de Lista.
     */
    public Lista(Lista<T> lista) {
        if (lista == null) {
            throw new ExcepcionLista("La lista no puede ser nula.");
        }

        primero = null;
        ultimo = null;
        cantidadDatos = 0;

        Nodo<T> actual = lista.primero;

        while (actual != null) {
            agregar(actual.dato);
            actual = actual.siguiente;
        }
    }

    /**
     * Agrega un dato al final de la lista.
     */
    public void agregar(T dato) {

        Nodo<T> nuevo = new Nodo<>(dato, ultimo, null);

        if (vacio()) {
            primero = nuevo;
        } else {
            ultimo.siguiente = nuevo;
        }

        ultimo = nuevo;
        cantidadDatos++;
    }

    /**
     * Agrega un dato en el índice indicado.
     */
    public void agregar(T dato, int indice) {

        if (indice < 0 || indice > cantidadDatos) {
            throw new ExcepcionLista("Índice inválido.");
        }

        if (indice == cantidadDatos) {
            agregar(dato);
            return;
        }

        Nodo<T> actual = obtenerNodo(indice);

        Nodo<T> nuevo = new Nodo<>(
                dato,
                actual.anterior,
                actual);

        if (actual.anterior != null) {
            actual.anterior.siguiente = nuevo;
        } else {
            primero = nuevo;
        }

        actual.anterior = nuevo;

        cantidadDatos++;
    }

    /**
     * Elimina el último dato.
     */
    public T eliminar() {

        if (vacio()) {
            throw new ExcepcionLista("La lista está vacía.");
        }

        T eliminado = ultimo.dato;

        if (cantidadDatos == 1) {
            primero = null;
            ultimo = null;
        } else {
            ultimo = ultimo.anterior;
            ultimo.siguiente = null;
        }

        cantidadDatos--;

        return eliminado;
    }

    /**
     * Elimina el dato del índice indicado.
     */
    public T eliminar(int indice) {

        if (indice < 0 || indice >= cantidadDatos) {
            throw new ExcepcionLista("Índice inválido.");
        }

        if (indice == cantidadDatos - 1) {
            return eliminar();
        }

        Nodo<T> actual = obtenerNodo(indice);

        T eliminado = actual.dato;

        if (actual.anterior != null) {
            actual.anterior.siguiente = actual.siguiente;
        } else {
            primero = actual.siguiente;
        }

        if (actual.siguiente != null) {
            actual.siguiente.anterior = actual.anterior;
        }

        cantidadDatos--;

        return eliminado;
    }

    /**
     * Obtiene el dato en el índice indicado.
     */
    public T dato(int indice) {

        if (indice < 0 || indice >= cantidadDatos) {
            throw new ExcepcionLista("Índice inválido.");
        }

        return obtenerNodo(indice).dato;
    }

    /**
     * Modifica el dato en el índice indicado.
     */
    public void modificarDato(T dato, int indice) {

        if (indice < 0 || indice >= cantidadDatos) {
            throw new ExcepcionLista("Índice inválido.");
        }

        obtenerNodo(indice).dato = dato;
    }

    /**
     * Obtiene el tamaño de la lista.
     */
    public int tamanio() {
        return cantidadDatos;
    }

    /**
     * Evalúa si la lista está vacía.
     */
    public boolean vacio() {
        return cantidadDatos == 0;
    }

    /**
     * Obtiene un iterador al inicio.
     */
    public Iterador<T> iterador() {
        return new IteradorLista<>(this);
    }

    /**
     * Obtiene un iterador en el índice indicado.
     */
    public Iterador<T> iterador(int indice) {

        if (indice < 0 || indice > cantidadDatos) {
            throw new ExcepcionLista("Índice inválido.");
        }

        return new IteradorLista<>(this, indice);
    }

    /**
     * Obtiene el nodo en el índice indicado.
     */
    Nodo<T> obtenerNodo(int indice) {

        Nodo<T> actual;

        if (indice < cantidadDatos / 2) {

            actual = primero;

            for (int i = 0; i < indice; i++) {
                actual = actual.siguiente;
            }

        } else {

            actual = ultimo;

            for (int i = cantidadDatos - 1; i > indice; i--) {
                actual = actual.anterior;
            }
        }

        return actual;
    }
}