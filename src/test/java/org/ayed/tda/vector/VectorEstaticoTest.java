package org.ayed.tda.vector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorEstaticoTest {
    private VectorEstatico<Integer> vectorEstatico;

    @Test
    void Constructor_tamanio_negativo_debe_lanzar_excepcion() {
        assertThrows(TamanioInvalidoException.class,
                () -> new VectorEstatico<>(-1),
                "Se esperaba TamanioInvalidoException al crear un vector con tamaño negativo.");
    }

    @Test
    void Constructor_tamanio_cero_debe_crear_vector() {
        assertDoesNotThrow(() -> new VectorEstatico<>(0), "Se debe poder crear un vector con tamaño 0.");
    }

    @Test
    void Constructor_copia_null_debe_lanzar_excepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new VectorEstatico<>(null),
                "Se esperaba IllegalArgumentException al intentar copiar un vector nulo.");
    }

    @Test
    void Constructor_copia_debe_copiar_contenido() {
        vectorEstatico = new VectorEstatico<>(5);
        vectorEstatico.asignar(0, 10);
        vectorEstatico.asignar(2, 20);
        vectorEstatico.asignar(4, 30);

        VectorEstatico<Integer> copia = new VectorEstatico<>(vectorEstatico);

        assertEquals(vectorEstatico.tamanio(), copia.tamanio(),
                "El tamaño del vector copiado debe ser igual al original.");
        assertEquals(10, copia.obtener(0), "El elemento en índice 0 debe ser el mismo.");
        assertNull(copia.obtener(1), "El elemento en índice 1 debe ser null.");
        assertEquals(20, copia.obtener(2), "El elemento en índice 2 debe ser el mismo.");
        assertNull(copia.obtener(3), "El elemento en índice 3 debe ser null.");
        assertEquals(30, copia.obtener(4), "El elemento en índice 4 debe ser el mismo.");
    }

    @Test
    void Asignar_y_obtener_debe_funcionar_correctamente() {
        vectorEstatico = new VectorEstatico<>(3);

        vectorEstatico.asignar(0, 100);
        assertEquals(100, vectorEstatico.obtener(0), "El valor obtenido debe ser el que se asignó.");

        vectorEstatico.asignar(1, 200);
        assertEquals(200, vectorEstatico.obtener(1), "El valor obtenido debe ser el que se asignó.");

        vectorEstatico.asignar(0, 300);
        assertEquals(300, vectorEstatico.obtener(0), "El valor debería haberse sobrescrito correctamente.");
    }

    @Test
    void Asignar_fuera_de_rango_debe_lanzar_excepcion() {
        vectorEstatico = new VectorEstatico<>(5);

        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorEstatico.asignar(-1, 10),
                "Se esperaba IndiceFueraDeRangoException al asignar en un índice negativo.");
        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorEstatico.asignar(5, 10),
                "Se esperaba IndiceFueraDeRangoException al asignar en un índice fuera de rango.");
    }

    @Test
    void Obtener_fuera_de_rango_debe_lanzar_excepcion() {
        vectorEstatico = new VectorEstatico<>(5);

        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorEstatico.obtener(-1),
                "Se esperaba IndiceFueraDeRangoException al obtener de un índice negativo.");
        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorEstatico.obtener(5),
                "Se esperaba IndiceFueraDeRangoException al obtener de un índice fuera de rango.");
    }

    @Test
    void Limpiar_debe_asignar_null() {
        vectorEstatico = new VectorEstatico<>(5);
        vectorEstatico.asignar(2, 500);

        assertEquals(500, vectorEstatico.obtener(2), "El valor debe estar presente antes de limpiar.");

        vectorEstatico.limpiar(2);
        assertNull(vectorEstatico.obtener(2), "La posición debería ser null después de limpiar.");
    }

    @Test
    void Limpiar_fuera_de_rango_debe_lanzar_excepcion() {
        vectorEstatico = new VectorEstatico<>(5);

        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorEstatico.limpiar(-1),
                "Se esperaba IndiceFueraDeRangoException al limpiar un índice negativo.");
        assertThrows(IndiceFueraDeRangoException.class,
                () -> vectorEstatico.limpiar(5),
                "Se esperaba IndiceFueraDeRangoException al limpiar un índice fuera de rango.");
    }

    @Test
    void Tamanio_debe_retornar_capacidad_maxima() {
        vectorEstatico = new VectorEstatico<>(10);
        assertEquals(10, vectorEstatico.tamanio(),
                "El tamaño del vector estático debe ser igual a la capacidad con la que fue creado.");
    }
}
