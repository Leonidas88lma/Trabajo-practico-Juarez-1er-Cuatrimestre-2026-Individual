package org.ayed.tda.colaPrioridad;

import org.ayed.tda.comparador.Comparador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ColaPrioridadTest {
    private ColaPrioridad<Integer> colaPrioridad;
    private Comparador<Integer> comparador;
    private Integer dato;

    private static class Comp implements Comparador<Integer> {
        @Override
        public int comparar(Integer dato1, Integer dato2) {
            return dato1.compareTo(dato2);
        }
    }

    @BeforeEach
    void setUp() {
        comparador = new Comp();
        colaPrioridad = new ColaPrioridad<>(comparador);
    }

    @Test
    void Cola_prioridad_no_puede_construirse_con_comparador_nulo() {
        comparador = null;
        assertThrows(ExcepcionColaPrioridad.class, () -> new ColaPrioridad<>(comparador));
    }

    @Test
    void Cola_prioridad_constructor_de_copia() {
        for (int i = 1; i <= 10; i++) {
            colaPrioridad.agregar(i);
        }

        ColaPrioridad<Integer> colaPrioridadCopia = new ColaPrioridad<>(colaPrioridad);

        assertEquals(colaPrioridad.tamanio(), colaPrioridadCopia.tamanio());
        while (!colaPrioridad.vacio()) {
            assertEquals(colaPrioridad.eliminar(), colaPrioridadCopia.eliminar());
        }
    }

    @Test
    void Cola_prioridad_vacia_constructor_de_copia() {
        ColaPrioridad<Integer> colaPrioridadCopia = new ColaPrioridad<>(colaPrioridad);

        assertEquals(colaPrioridad.tamanio(), colaPrioridadCopia.tamanio());
        while (!colaPrioridad.vacio()) {
            assertEquals(colaPrioridad.eliminar(), colaPrioridadCopia.eliminar());
        }
    }

    @Test
    void Cola_prioridad_nula_no_puede_copiarse() {
        colaPrioridad = null;
        assertThrows(ExcepcionColaPrioridad.class, () -> new ColaPrioridad<>(colaPrioridad));
    }

    @Test
    void Cola_prioridad_agregar() {
        assertDoesNotThrow(() -> colaPrioridad.agregar(1));
    }

    @Test
    void Cola_prioridad_eliminar() {
        colaPrioridad.agregar(1);
        colaPrioridad.agregar(2);
        assertDoesNotThrow(() -> dato = colaPrioridad.eliminar());
        assertEquals(2, dato);
    }

    @Test
    void Cola_prioridad_no_puede_eliminar_si_esta_vacia() {
        assertThrows(ExcepcionColaPrioridad.class, () -> colaPrioridad.eliminar());
    }

    @Test
    void Cola_prioridad_siguiente() {
        colaPrioridad.agregar(1);
        colaPrioridad.agregar(2);
        assertEquals(2, colaPrioridad.siguiente());
    }

    @Test
    void Cola_prioridad_no_puede_obtener_siguiente_si_esta_vacia() {
        assertThrows(ExcepcionColaPrioridad.class, () -> colaPrioridad.siguiente());
    }

    @Test
    void Cola_prioridad_vacia() {
        assertTrue(colaPrioridad.vacio());
    }

    @Test
    void Cola_prioridad_no_vacia() {
        colaPrioridad.agregar(1);
        assertFalse(colaPrioridad.vacio());
    }

    @Test
    void Cola_prioridad_tamanio_vacia() {
        assertEquals(0, colaPrioridad.tamanio());
    }

    @Test
    void Cola_prioridad_tamanio_no_vacia() {
        colaPrioridad.agregar(1);
        assertEquals(1, colaPrioridad.tamanio());
    }

    @Test
    void Cola_prioridad_caso_de_uso() {
        Random generadorAleatorio = new Random();
        for (int i = 0; i < 100; i++) {
            colaPrioridad.agregar(generadorAleatorio.nextInt(1000));
        }
        Integer anterior = colaPrioridad.eliminar();
        Integer actual;
        while (!colaPrioridad.vacio()) {
            actual = colaPrioridad.eliminar();
            assertTrue(anterior >= actual);
            anterior = actual;
        }
    }
}
