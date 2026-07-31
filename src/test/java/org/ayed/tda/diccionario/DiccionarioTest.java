package org.ayed.tda.diccionario;

import org.ayed.tda.lista.Lista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DiccionarioTest {
    private Diccionario<Integer, Integer> diccionario;
    private Integer valor;

    private void comparar(Lista<Integer> datos1, Lista<Integer> datos2) {
        assertEquals(datos1.tamanio(), datos2.tamanio());
        for (int i = 0; i < datos1.tamanio(); i++) {
            assertEquals(datos1.dato(i), datos2.dato(i));
        }
    }

    @BeforeEach
    void setUp() {
        diccionario = new Diccionario<>(20, 0.8);
    }

    @Test
    void Diccionario_no_puede_construirse_con_tamanio_no_valido() {
        assertThrows(ExcepcionDiccionario.class, () -> new Diccionario<>(-1, 0.8));
    }

    @Test
    void Diccionario_no_puede_construirse_con_factor_de_carga_no_valido() {
        assertThrows(ExcepcionDiccionario.class, () -> new Diccionario<>(20, -1.));
        assertThrows(ExcepcionDiccionario.class, () -> new Diccionario<>(20, 2.));
    }

    @Test
    void Diccionario_constructor_de_copia() {
        Random generadorAleatorio = new Random();
        ArrayList<Integer> datos = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            datos.add(generadorAleatorio.nextInt(1000));
        }
        Collections.shuffle(datos);

        for (Integer i : datos) {
            diccionario.agregar(i, i);
        }

        Diccionario<Integer, Integer> diccionarioCopia = new Diccionario<>(diccionario);

        comparar(diccionario.valores(), diccionarioCopia.valores());
    }

    @Test
    void Diccionario_vacio_constructor_de_copia() {
        Diccionario<Integer, Integer> diccionarioCopia = new Diccionario<>(diccionario);

        comparar(diccionario.valores(), diccionarioCopia.valores());
    }

    @Test
    void Diccionario_nulo_no_puede_copiarse() {
        diccionario = null;
        assertThrows(ExcepcionDiccionario.class, () -> new Diccionario<>(diccionario));
    }

    @Test
    void Diccionario_agregar() {
        assertDoesNotThrow(() -> diccionario.agregar(1, 1));
    }

    @Test
    void Diccionario_agregar_clave_duplicada_devuelve_valor_anterior() {
        diccionario.agregar(1, 1);
        assertDoesNotThrow(() -> valor = diccionario.agregar(1, 2));
        assertEquals(1, valor);
    }

    @Test
    void Diccionario_eliminar() {
        diccionario.agregar(1, 1);
        assertDoesNotThrow(() -> valor = diccionario.eliminar(1));
        assertEquals(1, valor);
    }

    @Test
    void Diccionario_puede_eliminar_si_esta_vacio() {
        diccionario.agregar(1, 1);
        assertDoesNotThrow(() -> diccionario.eliminar(1));
        assertDoesNotThrow(() -> valor = diccionario.eliminar(1));
        assertNull(valor);
    }

    @Test
    void Diccionario_puede_eliminar_si_la_clave_no_existe() {
        diccionario.agregar(1, 1);
        assertDoesNotThrow(() -> valor = diccionario.eliminar(2));
        assertNull(valor);
    }

    @Test
    void Diccionario_obtener_valor() {
        diccionario.agregar(1, 1);
        assertDoesNotThrow(() -> valor = diccionario.obtenerValor(1));
        assertEquals(1, valor);
    }

    @Test
    void Diccionario_puede_obtener_valor_si_esta_vacio() {
        assertDoesNotThrow(() -> valor = diccionario.obtenerValor(1));
        assertNull(valor);
    }

    @Test
    void Diccionario_puede_obtener_valor_si_la_clave_no_existe() {
        diccionario.agregar(1, 1);
        assertDoesNotThrow(() -> valor = diccionario.obtenerValor(2));
        assertNull(valor);
    }

    @Test
    void Diccionario_vacio() {
        assertTrue(diccionario.vacio());
    }

    @Test
    void Diccionario_no_vacio() {
        diccionario.agregar(1, 1);
        assertFalse(diccionario.vacio());
    }

    @Test
    void Diccionario_tamanio_vacio() {
        assertEquals(0, diccionario.tamanio());
    }

    @Test
    void Diccionario_tamanio_no_vacio() {
        diccionario.agregar(1, 1);
        assertEquals(1, diccionario.tamanio());
    }
}
