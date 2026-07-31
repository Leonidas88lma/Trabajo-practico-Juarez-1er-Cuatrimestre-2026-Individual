package org.ayed.tda.vector;

/**
 * Implementación de un Vector Dinámico.
 * <p>
 * Esta estructura de datos crece automáticamente cuando se llena y libera
 * memoria
 * cuando se vacía significativamente. Permite acceso aleatorio, inserción y
 * eliminación de elementos.
 * <p>
 * Los métodos que modifican la estructura del vector (como
 * {@link #agregar(Object, int)} y
 * {@link #eliminar(int)}) desplazan los elementos según sea necesario.
 *
 * @param <T> Tipo de dato a almacenar en el vector.
 */
public class VectorDinamico<T> {
    // La capacidad inicial NO se puede cambiar.
    private static final int CAPACIDAD_INICIAL = 0;
    private T[] datos;
    private int cantidadElementos;

    /**
     * Constructor de Vector.
     * Inicializa un vector con capacidad inicial 0.
     */
    @SuppressWarnings("unchecked")
    public VectorDinamico() {
        datos = (T[]) new Object[0];
        cantidadElementos = 0;
    }

    /**
     * Constructor de copia de Vector.
     *
     * @param otro Vector a copiar.
     * @throws IllegalArgumentException si el vector a copiar es nulo.
     */
    @SuppressWarnings("unchecked")
    public VectorDinamico(VectorDinamico<T> otro) {
        if (otro == null) {
            throw new IllegalArgumentException("Vector nulo");
        }
        datos = (T[]) new Object[otro.capacidad()];
        cantidadElementos = otro.cantidadElementos;
        for (int i = 0; i < cantidadElementos; i++) {
            datos[i] = otro.datos[i];
        }
    }

    @SuppressWarnings("unchecked")
    private void redimensionar(int nuevaCapacidad) {
        T[] nuevo = (T[]) new Object[nuevaCapacidad];
        for (int i = 0; i < cantidadElementos; i++) {
            nuevo[i] = datos[i];
        }
        datos = nuevo;
    }

    /**
     * Agrega un dato al final del vector.
     * <p>
     * Si el vector está lleno, aumenta su capacidad.
     *
     * @param dato Dato a agregar.
     */
    public void agregar(T dato) {
        if (cantidadElementos == datos.length) {
            int nuevaCapacidad = (datos.length == 0) ? 1 : datos.length * 2;
            redimensionar(nuevaCapacidad);
        }
        datos[cantidadElementos++] = dato;
    }

    /**
     * Agrega un dato al vector en el índice indicado, desplazando los elementos
     * posteriores.
     * <p>
     * Ejemplo:
     * 
     * <pre>
     * {@code
     * Vector<Integer> v = new Vector<>();
     * v.agregar(1);
     * v.agregar(3); // [1, 3]
     * v.agregar(2, 1); // [1, 2, 3]
     * }
     * </pre>
     *
     * @param dato   Dato a agregar.
     * @param indice Índice en el que se inserta el dato.
     *               Debe estar entre 0 y el tamaño lógico del vector (inclusive).
     * @throws IndiceFueraDeRangoException si el índice es menor a 0 o mayor que el
     *                                     tamaño lógico.
     */
    public void agregar(T dato, int indice) {
        if (indice < 0 || indice > cantidadElementos) {
            throw new IndiceFueraDeRangoException();
        }

        if (cantidadElementos == datos.length) {
            int nuevaCapacidad = (datos.length == 0) ? 1 : datos.length * 2;
            redimensionar(nuevaCapacidad);
        }

        for (int i = cantidadElementos; i > indice; i--) {
            datos[i] = datos[i - 1];
        }

        datos[indice] = dato;
        cantidadElementos++;
    }

    /**
     * Elimina el último dato del vector.
     * <p>
     * Reduce la capacidad del vector si es necesario, dependiendo de la estrategia.
     *
     * @return el dato eliminado.
     * @throws VectorVacioException si el vector está vacío.
     */
    public T eliminar() {
        if (vacio()) {
            throw new VectorVacioException();
        }

        T eliminado = datos[cantidadElementos - 1];
        datos[cantidadElementos - 1] = null;
        cantidadElementos--;

        achicarSiHaceFalta();

        return eliminado;
    }

    /**
     * Elimina el dato del vector en el índice indicado, desplazando los elementos
     * posteriores.
     * <p>
     * Ejemplo:
     * 
     * <pre>
     * {@code
     * Vector<Integer> v = ...; // [1, 2, 3]
     * v.eliminar(1);           // [1, 3]
     * }
     * </pre>
     *
     * @param indice Índice del dato a eliminar.
     *               Debe estar entre 0 y tamaño lógico - 1.
     * @return el dato eliminado.
     * @throws VectorVacioException        si el vector está vacío.
     * @throws IndiceFueraDeRangoException si el índice es menor a 0 o mayor o igual
     *                                     al tamaño lógico.
     */
    public T eliminar(int indice) {
        if (vacio()) {
            throw new VectorVacioException();
        }
        if (indice < 0 || indice >= cantidadElementos) {
            throw new IndiceFueraDeRangoException();
        }

        T eliminado = datos[indice];

        for (int i = indice; i < cantidadElementos - 1; i++) {
            datos[i] = datos[i + 1];
        }

        datos[cantidadElementos - 1] = null;
        cantidadElementos--;

        achicarSiHaceFalta();

        return eliminado;
    }

    @SuppressWarnings("unchecked")
    private void achicarSiHaceFalta() {
        if (cantidadElementos == 0) {
            datos = (T[]) new Object[0];
            return;
        }

        if (datos.length > cantidadElementos * 2) {
            redimensionar(cantidadElementos * 2);
        }
    }

    /**
     * Obtiene el dato del vector en el índice indicado.
     *
     * @param indice Índice del dato a obtener.
     *               Debe estar entre 0 y tamaño lógico - 1.
     * @return el dato en el índice indicado.
     * @throws IndiceFueraDeRangoException si el índice es menor a 0 o mayor o igual
     *                                     al tamaño lógico.
     */
    public T obtener(int indice) {
        if (indice < 0 || indice >= cantidadElementos) {
            throw new IndiceFueraDeRangoException();
        }
        return datos[indice];
    }

    /**
     * Modifica el dato del vector en el índice indicado.
     *
     * @param indice Índice del dato a modificar.
     *               Debe estar entre 0 y tamaño lógico - 1.
     * @param dato   Nuevo dato.
     * @throws IndiceFueraDeRangoException si el índice es menor a 0 o mayor o igual
     *                                     al tamaño lógico.
     */
    public void cambiar(int indice, T dato) {
        if (indice < 0 || indice >= cantidadElementos) {
            throw new IndiceFueraDeRangoException();
        }
        datos[indice] = dato;
    }

    /**
     * Obtiene el tamaño lógico del vector (cantidad de elementos almacenados).
     *
     * @return el tamaño del vector.
     */
    public int tamanio() {
        return cantidadElementos;
    }

    /**
     * Obtiene el tamaño físico actual del vector (capacidad).
     * <p>
     * NOTA: Este método es únicamente para probar el comportamiento dinámico.
     *
     * @return la capacidad actual del vector.
     */
    public int capacidad() {
        return datos.length;
    }

    /**
     * Evalúa si el vector está vacío.
     *
     * @return true si el vector está vacío.
     */
    public boolean vacio() {
        return cantidadElementos == 0;
    }
}
