package org.ayed.tda.vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorDinamicoTest {
    private VectorDinamico<Integer> vectorDinamico;
    private Integer dato;

    @BeforeEach
    void setUp() {
        vectorDinamico = new VectorDinamico<>();
    }

    @Test
    void Constructor_copia_debe_copiar_contenido() {
        for (int i = 1; i <= 10; i++) {
            vectorDinamico.agregar(i);
        }

        VectorDinamico<Integer> vectorDinamicoCopia = new VectorDinamico<>(vectorDinamico);

        assertEquals(vectorDinamico.tamanio(), vectorDinamicoCopia.tamanio(),
                "El tamaño del vector copiado debe ser igual al original.");
        for (int i = 0; i < vectorDinamico.tamanio(); i++) {
            assertEquals(vectorDinamico.obtener(i), vectorDinamicoCopia.obtener(i),
                    "El elemento en el índice " + i + " debe ser igual en la copia.");
        }
    }

    @Test
    void Constructor_copia_con_vector_vacio() {
        VectorDinamico<Integer> vectorDinamicoCopia = new VectorDinamico<>(vectorDinamico);
        assertEquals(0, vectorDinamicoCopia.tamanio(),
                "El vector copiado de uno vacío debe tener tamaño 0.");
    }

    @Test
    void Constructor_copia_null_debe_lanzar_excepcion() {
        vectorDinamico = null;
        assertThrows(IllegalArgumentException.class,
                () -> new VectorDinamico<>(vectorDinamico),
                "Se esperaba IllegalArgumentException al intentar copiar un vector nulo.");
    }

    @Test
    void Agregar_y_tamanio_debe_incrementar() {
        assertEquals(0, vectorDinamico.tamanio(), "El tamaño inicial del vector debe ser 0.");
        vectorDinamico.agregar(1);
        assertEquals(1, vectorDinamico.tamanio(), "El tamaño del vector debe ser 1 después de agregar un elemento.");
        vectorDinamico.agregar(2);
        assertEquals(2, vectorDinamico.tamanio(), "El tamaño del vector debe ser 2 después de agregar otro elemento.");
    }

    @Test
    void Agregar_indice_debe_desplazar_elementos() {
        vectorDinamico.agregar(10);
        vectorDinamico.agregar(30);
        vectorDinamico.agregar(20, 1);

        assertEquals(3, vectorDinamico.tamanio(), "El tamaño debe aumentar al insertar en un índice.");
        assertEquals(10, vectorDinamico.obtener(0), "El primer elemento debe mantenerse.");
        assertEquals(20, vectorDinamico.obtener(1), "El nuevo elemento debe estar en el índice 1.");
        assertEquals(30, vectorDinamico.obtener(2), "El elemento desplazado debe estar en el índice 2.");
    }

    @Test
    void Agregar_indice_invalido_debe_lanzar_excepcion() {
        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorDinamico.agregar(1, -1),
                "Se esperaba IndiceFueraDeRangoException al agregar en índice negativo.");
        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorDinamico.agregar(1, 1),
                "Se esperaba IndiceFueraDeRangoException al agregar en índice mayor al tamaño.");
    }

    @Test
    void Eliminar_debe_remover_el_ultimo_elemento() {
        vectorDinamico.agregar(1);
        vectorDinamico.agregar(2);
        dato = vectorDinamico.eliminar();
        assertEquals(2, dato, "El elemento eliminado debe ser el último (2).");
        assertEquals(1, vectorDinamico.tamanio(), "El tamaño debe reducirse tras eliminar.");
        assertEquals(1, vectorDinamico.obtener(0), "El primer elemento debe permanecer.");
    }

    @Test
    void Eliminar_en_vector_vacio_debe_lanzar_excepcion() {
        assertThrows(VectorVacioException.class,
                () -> vectorDinamico.eliminar(),
                "Se esperaba VectorVacioException al intentar eliminar de un vector vacío.");
    }

    @Test
    void Eliminar_indice_debe_desplazar_elementos() {
        vectorDinamico.agregar(10);
        vectorDinamico.agregar(20);
        vectorDinamico.agregar(30);
        dato = vectorDinamico.eliminar(1);

        assertEquals(20, dato, "El elemento eliminado en índice 1 debe ser 20.");
        assertEquals(2, vectorDinamico.tamanio(), "El tamaño debe reducirse a 2.");
        assertEquals(10, vectorDinamico.obtener(0), "El primer elemento debe seguir en índice 0.");
        assertEquals(30, vectorDinamico.obtener(1), "El elemento en índice 2 debe desplazarse al índice 1.");
    }

    @Test
    void Eliminar_indice_invalido_debe_lanzar_excepcion() {
        vectorDinamico.agregar(1);
        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorDinamico.eliminar(-1),
                "Se esperaba IndiceFueraDeRangoException al eliminar en índice negativo.");
        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorDinamico.eliminar(1),
                "Se esperaba IndiceFueraDeRangoException al eliminar en índice fuera de rango.");
    }

    @Test
    void Vacio_debe_funcionar_correctamente() {
        assertTrue(vectorDinamico.vacio(), "Un vector recién creado debería estar vacío.");
        vectorDinamico.agregar(1);
        assertFalse(vectorDinamico.vacio(), "Un vector con elementos no debería estar vacío.");
    }

    @Test
    void Obtener_debe_devolver_elemento_correcto() {
        vectorDinamico.agregar(100);
        assertEquals(100, vectorDinamico.obtener(0), "El elemento obtenido en el índice 0 debe ser el agregado.");
    }

    @Test
    void Obtener_indice_invalido_debe_lanzar_excepcion() {
        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorDinamico.obtener(0),
                "Se esperaba IndiceFueraDeRangoException al obtener de un vector vacío.");
        vectorDinamico.agregar(1);
        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorDinamico.obtener(1),
                "Se esperaba IndiceFueraDeRangoException al obtener en un índice fuera de rango.");
    }

    @Test
    void Cambiar_debe_modificar_elemento() {
        vectorDinamico.agregar(1);
        vectorDinamico.cambiar(0, 99);
        assertEquals(99, vectorDinamico.obtener(0), "El elemento en índice 0 debería haber cambiado a 99.");
    }

    @Test
    void Cambiar_indice_invalido_debe_lanzar_excepcion() {
        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorDinamico.cambiar(0, 1),
                "Se esperaba IndiceFueraDeRangoException al cambiar en un vector vacío.");
        vectorDinamico.agregar(1);
        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorDinamico.cambiar(1, 99),
                "Se esperaba IndiceFueraDeRangoException al cambiar en un índice fuera de rango.");
    }

    @Test
    void Redimension_eficiente() {
        int cantidad = 100;
        for (int i = 0; i < cantidad; i++) {
            vectorDinamico.agregar(i);
        }

        assertTrue(vectorDinamico.capacidad() <= vectorDinamico.tamanio() * 2,
                "La capacidad excedió el doble del tamaño lógico durante el crecimiento. Verifica tu estrategia de redimensionamiento.");

        while (vectorDinamico.tamanio() > 20) {
            vectorDinamico.eliminar();
        }

        assertTrue(vectorDinamico.capacidad() <= vectorDinamico.tamanio() * 2,
                "El vector no liberó memoria tras eliminar muchos elementos. Verifica tu estrategia de redimensionamiento.");
    }

    @Test
    void Caso_de_uso() {
        Integer[] vectorEsperado = {1, 2, 9, 3, 5, 6, 7, 10};

        for (int i = 1; i <= 8; i++) {
            vectorDinamico.agregar(i);
        }
        vectorDinamico.agregar(9, 2);   // [1, 2, 9, 3, 4, 5, 6, 7, 8]
        vectorDinamico.eliminar(4);     // [1, 2, 9, 3, 5, 6, 7, 8]
        vectorDinamico.eliminar();      // [1, 2, 9, 3, 5, 6, 7]
        vectorDinamico.agregar(10);     // [1, 2, 9, 3, 5, 6, 7, 10]

        assertEquals(vectorEsperado.length, vectorDinamico.tamanio(),
                "El tamaño final del vector no coincide con el esperado en el caso de uso general.");
        for (int i = 0; i < vectorEsperado.length; i++) {
            assertEquals(vectorEsperado[i], vectorDinamico.obtener(i),
                    "El elemento en la posición " + i + " no coincide con el esperado.");
        }
    }
}
