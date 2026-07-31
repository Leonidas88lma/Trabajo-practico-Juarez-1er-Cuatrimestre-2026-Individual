package org.ayed.tda.matriz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrizTest {
    private Matriz<Integer> matriz;

    @BeforeEach
    void setUp() {
        matriz = new Matriz<>(2, 2);
    }

    @Test
    void Constructor_con_dimensiones_validas_debe_crear_matriz() {
        assertDoesNotThrow(() -> new Matriz<>(1));
        assertDoesNotThrow(() -> new Matriz<>(1, 1));
        assertDoesNotThrow(() -> new Matriz<>(1, 1, 0));
        assertDoesNotThrow(() -> new Matriz<>(1, Integer.valueOf(0)));
    }

    @Test
    void Constructor_con_dimensiones_invalidas_debe_fallar() {
        // Matriz(int tamanio, T valor)
        assertThrows(IllegalArgumentException.class, () -> new Matriz<>(0, 5), "El tamaño no puede ser 0");
        assertThrows(IllegalArgumentException.class, () -> new Matriz<>(-1, 5), "El tamaño no puede ser negativo");

        // Matriz(int tamanio)
        assertThrows(IllegalArgumentException.class, () -> new Matriz<>(0), "El tamaño no puede ser 0");
        assertThrows(IllegalArgumentException.class, () -> new Matriz<>(-1), "El tamaño no puede ser negativo");

        // Matriz(int filas, int columnas, T valor)
        assertThrows(IllegalArgumentException.class, () -> new Matriz<>(0, 5, 1), "Filas no pueden ser 0");
        assertThrows(IllegalArgumentException.class, () -> new Matriz<>(5, 0, 1), "Columnas no pueden ser 0");

        // Matriz(int filas, int columnas)
        assertThrows(IllegalArgumentException.class, () -> new Matriz<>(0, 5), "Filas no pueden ser 0");
        assertThrows(IllegalArgumentException.class, () -> new Matriz<>(5, 0), "Columnas no pueden ser 0");
    }

    @Test
    void Constructor_copia_matriz_nula_debe_fallar() {
        assertThrows(IllegalArgumentException.class, () -> new Matriz<>(null), "No se puede copiar una matriz nula");
    }

    @Test
    void Constructor_matriz_cuadrada_con_valor() {
        matriz = new Matriz<>(3, Integer.valueOf(5));

        assertEquals(3, matriz.filas(), "La matriz cuadrada debería tener 3 filas.");
        assertEquals(3, matriz.columnas(), "La matriz cuadrada debería tener 3 columnas.");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(5, matriz.elemento(i, j),
                        "Todos los elementos de la matriz cuadrada deberían ser 5.");
            }
        }
    }

    @Test
    void Constructor_matriz_rectangular_vacia() {
        matriz = new Matriz<>(2, 3);

        assertEquals(2, matriz.filas(), "La matriz rectangular debería tener 2 filas.");
        assertEquals(3, matriz.columnas(), "La matriz rectangular debería tener 3 columnas.");
        assertNull(matriz.elemento(0, 0), "El elemento inicial debería ser null en una matriz vacía.");
    }

    @Test
    void Constructor_matriz_cuadrada_vacia() {
        matriz = new Matriz<>(2);

        assertEquals(2, matriz.filas(), "La matriz cuadrada debería tener 2 filas.");
        assertEquals(2, matriz.columnas(), "La matriz cuadrada debería tener 2 columnas.");
        assertNull(matriz.elemento(0, 0), "El elemento inicial debería ser null en una matriz vacía.");
    }

    @Test
    void Constructor_matriz_rectangular_con_valor() {
        matriz = new Matriz<>(2, 4, 99);

        assertEquals(2, matriz.filas(), "La matriz debería tener 2 filas.");
        assertEquals(4, matriz.columnas(), "La matriz debería tener 4 columnas.");

        assertEquals(99, matriz.elemento(0, 0), "El elemento en (0,0) debería ser 99.");
        assertEquals(99, matriz.elemento(1, 3), "El elemento en (1,3) debería ser 99.");
    }

    @Test
    void Constructor_copia() {
        matriz = new Matriz<>(2, 2, 1);
        matriz.asignar(0, 0, 10);

        Matriz<Integer> copia = new Matriz<>(matriz);

        assertEquals(matriz.filas(), copia.filas(), "La copia debe tener las mismas filas.");
        assertEquals(matriz.columnas(), copia.columnas(), "La copia debe tener las mismas columnas.");
        assertEquals(10, copia.elemento(0, 0), "El elemento modificado en original debe reflejarse en la copia al inicio.");
        assertEquals(1, copia.elemento(0, 1), "El elemento no modificado debe ser igual al original.");

        matriz.asignar(0, 0, 999);
        assertEquals(10, copia.elemento(0, 0), "Modificar la matriz original no debería afectar a la copia.");
    }

    @Test
    void Asignar_elemento_exito() {
        matriz = new Matriz<>(3, 3);

        matriz.asignar(1, 1, 500);
        assertEquals(500, matriz.elemento(1, 1), "El valor asignado debe ser recuperable con elemento().");

        matriz.asignar(1, 1, 1000);
        assertEquals(1000, matriz.elemento(1, 1), "Asignar de nuevo debe sobrescribir el valor anterior.");
    }

    @Test
    void Elemento_fallo_indices_invalidos() {
        matriz = new Matriz<>(2, 2);

        assertThrows(IndiceNoValidoException.class,
                () -> matriz.elemento(-1, 0),
                "Se esperaba IndiceNoValidoException al acceder con índice de fila negativo.");
        assertThrows(IndiceNoValidoException.class,
                () -> matriz.elemento(0, -1),
                "Se esperaba IndiceNoValidoException al acceder con índice de columna negativo.");
        assertThrows(IndiceNoValidoException.class,
                () -> matriz.elemento(2, 0),
                "Se esperaba IndiceNoValidoException al acceder con índice de fila fuera de rango.");
        assertThrows(IndiceNoValidoException.class,
                () -> matriz.elemento(0, 2),
                "Se esperaba IndiceNoValidoException al acceder con índice de columna fuera de rango.");

        assertThrows(IndiceNoValidoException.class,
                () -> matriz.asignar(2, 2, 1),
                "Se esperaba IndiceNoValidoException al asignar fuera de rango.");
    }
}
