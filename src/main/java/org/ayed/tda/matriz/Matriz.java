package org.ayed.tda.matriz;

/**
 * Implementación de una matriz genérica de tamaño fijo.
 *
 * @param <T> Tipo de dato a almacenar en la matriz.
 */
public class Matriz<T> {

    private T[][] datos;
    private int filas;
    private int columnas;

    /**
     * Constructor para matriz cuadrada inicializada con un valor.
     */
    @SuppressWarnings("unchecked")
    public Matriz(int tamanio, T valor) {
        if (tamanio < 1) {
            throw new IllegalArgumentException("Tamaño inválido");
        }

        this.filas = tamanio;
        this.columnas = tamanio;
        datos = (T[][]) new Object[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                datos[i][j] = valor;
            }
        }
    }

    /**
     * Constructor para matriz cuadrada vacía.
     */
    @SuppressWarnings("unchecked")
    public Matriz(int tamanio) {
        if (tamanio < 1) {
            throw new IllegalArgumentException("Tamaño inválido");
        }

        this.filas = tamanio;
        this.columnas = tamanio;
        datos = (T[][]) new Object[filas][columnas];
    }

    /**
     * Constructor para matriz rectangular inicializada con un valor.
     */
    @SuppressWarnings("unchecked")
    public Matriz(int filas, int columnas, T valor) {
        if (filas < 1 || columnas < 1) {
            throw new IllegalArgumentException("Dimensiones inválidas");
        }

        this.filas = filas;
        this.columnas = columnas;
        datos = (T[][]) new Object[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                datos[i][j] = valor;
            }
        }
    }

    /**
     * Constructor para matriz rectangular vacía.
     */
    @SuppressWarnings("unchecked")
    public Matriz(int filas, int columnas) {
        if (filas < 1 || columnas < 1) {
            throw new IllegalArgumentException("Dimensiones inválidas");
        }

        this.filas = filas;
        this.columnas = columnas;
        datos = (T[][]) new Object[filas][columnas];
    }

    /**
     * Constructor por copia.
     */
    @SuppressWarnings("unchecked")
    public Matriz(Matriz<T> otra) {
        if (otra == null) {
            throw new IllegalArgumentException("Matriz nula");
        }

        this.filas = otra.filas;
        this.columnas = otra.columnas;
        datos = (T[][]) new Object[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                datos[i][j] = otra.datos[i][j];
            }
        }
    }

    /**
     * Obtiene el elemento en (i, j).
     */
    public T elemento(int i, int j) {
        validarIndices(i, j);
        return datos[i][j];
    }

    /**
     * Asigna un valor en (i, j).
     */
    public void asignar(int i, int j, T valor) {
        validarIndices(i, j);
        datos[i][j] = valor;
    }

    /**
     * Devuelve la cantidad de filas.
     */
    public int filas() {
        return filas;
    }

    /**
     * Devuelve la cantidad de columnas.
     */
    public int columnas() {
        return columnas;
    }

    /**
     * Valida índices.
     */
    private void validarIndices(int i, int j) {
        if (i < 0 || i >= filas || j < 0 || j >= columnas) {
            throw new IndiceNoValidoException("Índices fuera de rango");
        }
    }
}