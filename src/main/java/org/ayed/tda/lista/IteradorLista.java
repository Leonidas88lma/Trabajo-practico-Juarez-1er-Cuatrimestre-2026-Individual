package org.ayed.tda.lista;

import org.ayed.tda.iterador.Iterador;
import org.ayed.tda.iterador.ExcepcionNoHayDato;
/*
 * Implementación de un iterador para la estructura Lista.
 *
 * Permite:
 * - recorrer hacia adelante y atrás
 * - modificar elementos
 * - insertar elementos
 * - eliminar elementos
 *
 * Mantiene un cursor apuntando al nodo actual.
 */
class IteradorLista<T> implements Iterador<T> {

    // Lista asociada al iterador
    private Lista<T> lista;

    // Nodo actual donde está parado el cursor
    private Nodo<T> cursor;

    // Índice actual del cursor
    private int indice;

    /*
     * Constructor.
     *
     * Inicializa el iterador al comienzo de la lista.
     */
    IteradorLista(Lista<T> lista) {

        this.lista = lista;

        this.cursor = lista.primero;

        this.indice = 0;
    }

    /*
     * Constructor con posición inicial.
     *
     * Permite crear el iterador en un índice específico.
     */
    IteradorLista(Lista<T> lista, int indice) {

        this.lista = lista;

        this.indice = indice;

        // Si apunta al final de la lista
        if (indice == lista.tamanio()) {

            cursor = null;

        } else {

            cursor = lista.obtenerNodo(indice);
        }
    }

    /*
     * Devuelve el dato actual del cursor.
     */
    @Override
    public T dato() {

        if (cursor == null) {
            throw new ExcepcionNoHayDato("No hay dato.");
        }

        return cursor.dato;
    }

    /*
     * Indica si existe un siguiente elemento.
     */
    @Override
    public boolean haySiguiente() {
        return cursor != null;
    }

    /*
     * Avanza el cursor al siguiente nodo.
     */
    @Override
    public void siguiente() {

        if (!haySiguiente()) {
            throw new ExcepcionNoHayDato("No hay siguiente.");
        }

        cursor = cursor.siguiente;

        indice++;
    }

    /*
     * Indica si existe un elemento anterior.
     */
    @Override
    public boolean hayAnterior() {

        // Caso cursor fuera de la lista
        if (cursor == null) {

            return lista.ultimo != null;
        }

        return cursor.anterior != null;
    }

    /*
     * Retrocede el cursor al nodo anterior.
     */
    @Override
    public void anterior() {

        if (!hayAnterior()) {
            throw new ExcepcionNoHayDato("No hay anterior.");
        }

        // Si está después del último elemento
        if (cursor == null) {

            cursor = lista.ultimo;

            indice = lista.tamanio() - 1;

        } else {

            cursor = cursor.anterior;

            indice--;
        }
    }

    /*
     * Inserta un elemento en la posición actual.
     *
     * Luego el cursor avanza.
     */
    @Override
    public void agregar(T dato) {

        lista.agregar(dato, indice);

        indice++;

        // Si quedó al final
        if (indice == lista.tamanio()) {

            cursor = null;

        } else {

            cursor = lista.obtenerNodo(indice);
        }
    }

    /*
     * Modifica el dato del nodo actual.
     */
    @Override
    public void modificarDato(T dato) {

        if (cursor == null) {
            throw new ExcepcionNoHayDato("No hay dato.");
        }

        cursor.dato = dato;
    }

    /*
     * Elimina el nodo actual.
     *
     * Luego el cursor pasa al siguiente nodo.
     */
    @Override
    public T eliminar() {

        if (cursor == null) {
            throw new ExcepcionNoHayDato("No hay dato.");
        }

        T eliminado = cursor.dato;

        Nodo<T> siguiente = cursor.siguiente;

        lista.eliminar(indice);

        cursor = siguiente;

        return eliminado;
    }
}