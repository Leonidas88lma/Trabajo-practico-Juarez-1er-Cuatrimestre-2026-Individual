package org.ayed.tda.vector;

/**
 * Implementación de un Vector Estático.
 * <p>
 * Esta clase representa un arreglo de tamaño fijo. No permite agregar ni
 * eliminar elementos
 * que modifiquen el tamaño del contenedor, aunque sí permite limpiar posiciones
 * (asignar null).
 * Los elementos se acceden y modifican directamente por índice.
 *
 * @param <T> Tipo de dato a almacenar en el vector.
 */
public class VectorEstatico<T> {
    private T[] datos;

    /**
     * Constructor de Vector Estático.
     * <p>
     * Crea un vector con una capacidad fija.
     *
     * @param tamanio Tamaño fijo del vector.
     * @throws TamanioInvalidoException si el tamaño es negativo.
     */
    @SuppressWarnings("unchecked")
    public VectorEstatico(int tamanio) {
        if (tamanio < 0) {
            throw new TamanioInvalidoException("Tamaño inválido");
        }
        datos = (T[]) new Object[tamanio];
    }

    /**
     * Constructor de copia de Vector Estático.
     * <p>
     * Crea un nuevo vector con el mismo tamaño y contenido que el original.
     *
     * @param otro Vector a copiar.
     * @throws IllegalArgumentException si el vector a copiar es nulo.
     */
    @SuppressWarnings("unchecked")
    public VectorEstatico(VectorEstatico<T> otro) {
        if (otro == null) {
            throw new IllegalArgumentException("Vector nulo");
        }
        datos = (T[]) new Object[otro.tamanio()];
        for (int i = 0; i < datos.length; i++) {
            datos[i] = otro.datos[i];
        }
    }

    /**
     * Obtiene el dato en la posición indicada.
     *
     * @param indice Índice del dato a obtener.
     *               Debe estar entre 0 y tamanio() - 1.
     * @return El dato en la posición indicada (puede ser null).
     * @throws IndiceFueraDeRangoException si el índice es menor a 0 o mayor o igual
     *                                     al tamaño.
     */
    public T obtener(int indice) {
        validarIndice(indice);
        return datos[indice];
    }

    /**
     * Asigna un dato en la posición indicada, reemplazando el valor anterior.
     *
     * @param indice Índice donde asignar el dato.
     *               Debe estar entre 0 y tamanio() - 1.
     * @param dato   Dato a asignar (puede ser null).
     * @throws IndiceFueraDeRangoException si el índice es menor a 0 o mayor o igual
     *                                     al tamaño.
     */
    public void asignar(int indice, T dato) {
        validarIndice(indice);
        datos[indice] = dato;
    }

    /**
     * Limpia la posición indicada (asigna null).
     * <p>
     * Esta operación no reduce el tamaño del vector, simplemente deja la posición
     * vacía.
     *
     * @param indice Índice a limpiar.
     *               Debe estar entre 0 y tamanio() - 1.
     * @throws IndiceFueraDeRangoException si el índice es menor a 0 o mayor o igual
     *                                     al tamaño.
     */
    public void limpiar(int indice) {
        validarIndice(indice);
        datos[indice] = null;
    }

    /**
     * Obtiene el tamaño fijo del vector.
     *
     * @return La capacidad total del vector.
     */
    public int tamanio() {
        return datos.length;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= datos.length) {
            throw new IndiceFueraDeRangoException("Índice inválido");
        }
    }
}
