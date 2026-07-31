package org.ayed.tda.lista;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ColaTest {
    private Cola<Integer> cola;
    private Integer dato;

    @BeforeEach
    void setUp() {
        cola = new Cola<>();
    }

    @Test
    void Cola_constructor_de_copia() {
        for (int i = 1; i <= 10; i++) {
            cola.agregar(i);
        }

        Cola<Integer> colaCopia = new Cola<>(cola);

        assertEquals(cola.tamanio(), colaCopia.tamanio());
        while (!cola.vacio()) {
            assertEquals(cola.eliminar(), colaCopia.eliminar());
        }
    }

    @Test
    void Cola_vacia_constructor_de_copia() {
        Cola<Integer> colaCopia = new Cola<>(cola);

        assertEquals(cola.tamanio(), colaCopia.tamanio());
        while (!cola.vacio()) {
            assertEquals(cola.eliminar(), colaCopia.eliminar());
        }
    }

    @Test
    void Cola_nula_no_puede_copiarse() {
        cola = null;
        assertThrows(ExcepcionLista.class, () -> new Cola<>(cola));
    }

    @Test
    void Cola_agregar() {
        assertDoesNotThrow(() -> cola.agregar(1));
    }

    @Test
    void Cola_eliminar() {
        cola.agregar(1);
        cola.agregar(2);
        assertDoesNotThrow(() -> dato = cola.eliminar());
        assertEquals(1, dato);
    }

    @Test
    void Cola_no_puede_eliminar_si_esta_vacia() {
        assertThrows(ExcepcionLista.class, () -> cola.eliminar());
    }

    @Test
    void Cola_siguiente() {
        cola.agregar(1);
        cola.agregar(2);
        assertEquals(1, cola.siguiente());
    }

    @Test
    void Cola_no_puede_obtener_siguiente_si_esta_vacia() {
        assertThrows(ExcepcionLista.class, () -> cola.siguiente());
    }

    @Test
    void Cola_vacia() {
        assertTrue(cola.vacio());
    }

    @Test
    void Cola_no_vacia() {
        cola.agregar(1);
        assertFalse(cola.vacio());
    }

    @Test
    void Cola_tamanio_vacia() {
        assertEquals(0, cola.tamanio());
    }

    @Test
    void Cola_tamanio_no_vacia() {
        cola.agregar(1);
        assertEquals(1, cola.tamanio());
    }
}
