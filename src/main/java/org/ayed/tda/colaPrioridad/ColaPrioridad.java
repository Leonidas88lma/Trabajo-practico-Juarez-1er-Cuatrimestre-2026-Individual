package org.ayed.tda.colaPrioridad;

import org.ayed.tda.comparador.Comparador;
import org.ayed.tda.vector.VectorDinamico;

public class ColaPrioridad<T> {

    private VectorDinamico<T> datos;
    private Comparador<T> comparador;

    /**
     * Constructor de ColaPrioridad.
     *
     * @param comparador Comparador a utilizar.
     *                   No puede ser nulo.
     * @throws ExcepcionColaPrioridad si el comparador es nulo.
     */
    public ColaPrioridad(Comparador<T> comparador) {

        if (comparador == null) {
            throw new ExcepcionColaPrioridad("El comparador no puede ser nulo.");
        }

        this.comparador = comparador;
        this.datos = new VectorDinamico<>();
    }

    /**
     * Constructor de copia de ColaPrioridad.
     *
     * @param colaPrioridad Cola a copiar.
     *                      No puede ser nula.
     * @throws ExcepcionColaPrioridad si la cola es nula.
     */
    public ColaPrioridad(ColaPrioridad<T> colaPrioridad) {

        if (colaPrioridad == null) {
            throw new ExcepcionColaPrioridad("La cola no puede ser nula.");
        }

        this.comparador = colaPrioridad.comparador;
        this.datos = new VectorDinamico<>(colaPrioridad.datos);
    }

    /**
     * Reordena el Heap para mantener el invariante.
     * Desplaza datos hacia arriba.
     */
    private void heapificarHaciaArriba() {

        int indice = datos.tamanio() - 1;

        while (indice > 0) {

            int padre = (indice - 1) / 2;

            T actual = datos.obtener(indice);
            T datoPadre = datos.obtener(padre);

            if (comparador.comparar(actual, datoPadre) <= 0) {
                break;
            }

            datos.cambiar(indice, datoPadre);
            datos.cambiar(padre, actual);

            indice = padre;
        }
    }

    /**
     * Reordena el Heap para mantener el invariante.
     * Desplaza datos hacia abajo.
     */
    private void heapificarHaciaAbajo() {

        int indice = 0;

        while (true) {

            int izquierdo = indice * 2 + 1;
            int derecho = indice * 2 + 2;

            int mayor = indice;

            if (izquierdo < datos.tamanio()
                    && comparador.comparar(
                            datos.obtener(izquierdo),
                            datos.obtener(mayor)) > 0) {

                mayor = izquierdo;
            }

            if (derecho < datos.tamanio()
                    && comparador.comparar(
                            datos.obtener(derecho),
                            datos.obtener(mayor)) > 0) {

                mayor = derecho;
            }

            if (mayor == indice) {
                break;
            }

            T aux = datos.obtener(indice);

            datos.cambiar(indice, datos.obtener(mayor));
            datos.cambiar(mayor, aux);

            indice = mayor;
        }
    }

    /**
     * Agrega el dato a la cola.
     *
     * @param dato Dato a agregar.
     */
    public void agregar(T dato) {

        datos.agregar(dato);

        heapificarHaciaArriba();
    }

    /**
     * Elimina el dato con mayor prioridad.
     *
     * @return el dato eliminado.
     * @throws ExcepcionColaPrioridad si la cola está vacía.
     */
    public T eliminar() {

        if (vacio()) {
            throw new ExcepcionColaPrioridad("La cola está vacía.");
        }

        T maximo = datos.obtener(0);

        if (datos.tamanio() == 1) {
            datos.eliminar();
            return maximo;
        }

        T ultimo = datos.eliminar();

        datos.cambiar(0, ultimo);

        heapificarHaciaAbajo();

        return maximo;
    }

    /**
     * Obtiene el dato con mayor prioridad.
     *
     * @return el dato con mayor prioridad.
     * @throws ExcepcionColaPrioridad si la cola está vacía.
     */
    public T siguiente() {

        if (vacio()) {
            throw new ExcepcionColaPrioridad("La cola está vacía.");
        }

        return datos.obtener(0);
    }

    /**
     * Obtiene el tamaño de la cola.
     *
     * @return el tamaño de la cola.
     */
    public int tamanio() {
        return datos.tamanio();
    }

    /**
     * Evalúa si la cola está vacía.
     *
     * @return true si la cola está vacía.
     */
    public boolean vacio() {
        return datos.vacio();
    }
}