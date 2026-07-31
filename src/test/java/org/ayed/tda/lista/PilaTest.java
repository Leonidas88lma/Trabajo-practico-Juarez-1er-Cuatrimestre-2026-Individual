package org.ayed.tda.lista;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PilaTest {
    private Pila<Integer> pila;
    private Integer dato;

    @BeforeEach
    void setUp() {
        pila = new Pila<>();
    }

    @Test
    void Pila_constructor_de_copia() {
        for (int i = 1; i <= 10; i++) {
            pila.agregar(i);
        }

        Pila<Integer> pilaCopia = new Pila<>(pila);

        assertEquals(pila.tamanio(), pilaCopia.tamanio());
        while (!pila.vacio()) {
            assertEquals(pila.eliminar(), pilaCopia.eliminar());
        }
    }

    @Test
    void Pila_vacia_constructor_de_copia() {
        Pila<Integer> pilaCopia = new Pila<>(pila);

        assertEquals(pila.tamanio(), pilaCopia.tamanio());
        while (!pila.vacio()) {
            assertEquals(pila.eliminar(), pilaCopia.eliminar());
        }
    }

    @Test
    void Pila_nula_no_puede_copiarse() {
        pila = null;
        assertThrows(ExcepcionLista.class, () -> new Pila<>(pila));
    }

    @Test
    void Pila_agregar() {
        assertDoesNotThrow(() -> pila.agregar(1));
    }

    @Test
    void Pila_eliminar() {
        pila.agregar(1);
        pila.agregar(2);
        assertDoesNotThrow(() -> dato = pila.eliminar());
        assertEquals(2, dato);
    }

    @Test
    void Pila_no_puede_eliminar_si_esta_vacia() {
        assertThrows(ExcepcionLista.class, () -> pila.eliminar());
    }

    @Test
    void Pila_siguiente() {
        pila.agregar(1);
        pila.agregar(2);
        assertEquals(2, pila.siguiente());
    }

    @Test
    void Pila_no_puede_obtener_siguiente_si_esta_vacia() {
        assertThrows(ExcepcionLista.class, () -> pila.siguiente());
    }

    @Test
    void Pila_vacia() {
        assertTrue(pila.vacio());
    }

    @Test
    void Pila_no_vacia() {
        pila.agregar(1);
        assertFalse(pila.vacio());
    }

    @Test
    void Pila_tamanio_vacia() {
        assertEquals(0, pila.tamanio());
    }

    @Test
    void Pila_tamanio_no_vacia() {
        pila.agregar(1);
        assertEquals(1, pila.tamanio());
    }
}
