package org.ayed.tda.lista;

import org.ayed.tda.iterador.ExcepcionNoHayDato;
import org.ayed.tda.iterador.Iterador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListaTest {
    private Lista<Integer> lista;
    private Integer dato;
    private Iterador<Integer> iterador;

    @BeforeEach
    void setUp() {
        lista = new Lista<>();
    }

    @Test
    void Lista_constructor_de_copia() {
        for (int i = 1; i <= 10; i++) {
            lista.agregar(i);
        }

        Lista<Integer> listaCopia = new Lista<>(lista);

        assertEquals(lista.tamanio(), listaCopia.tamanio());
        for (int i = 0; i < lista.tamanio(); i++) {
            assertEquals(lista.dato(i), listaCopia.dato(i));
        }
    }

    @Test
    void Lista_vacia_constructor_de_copia() {
        Lista<Integer> listaCopia = new Lista<>(lista);

        assertEquals(lista.tamanio(), listaCopia.tamanio());
        for (int i = 0; i < lista.tamanio(); i++) {
            assertEquals(lista.dato(i), listaCopia.dato(i));
        }
    }

    @Test
    void Lista_nula_no_puede_copiarse() {
        lista = null;
        assertThrows(ExcepcionLista.class, () -> new Lista<>(lista));
    }

    @Test
    void Lista_agregar() {
        assertDoesNotThrow(() -> lista.agregar(1));
    }

    @Test
    void Lista_agregar_indice() {
        lista.agregar(1);
        assertDoesNotThrow(() -> lista.agregar(2, 0));
        assertDoesNotThrow(() -> lista.agregar(3, 2));
    }

    @Test
    void Lista_no_puede_agregar_indice_si_el_indice_es_negativo() {
        assertThrows(ExcepcionLista.class, () -> lista.agregar(1, -1));
    }

    @Test
    void Lista_no_puede_agregar_indice_si_el_indice_es_mayor_al_tamanio() {
        assertThrows(ExcepcionLista.class, () -> lista.agregar(1, 1));
    }

    @Test
    void Lista_eliminar() {
        lista.agregar(1);
        assertDoesNotThrow(() -> dato = lista.eliminar());
        assertEquals(1, dato);
    }

    @Test
    void Lista_no_puede_eliminar_si_esta_vacia() {
        lista.agregar(1);
        assertDoesNotThrow(() -> lista.eliminar());
        assertThrows(ExcepcionLista.class, () -> lista.eliminar());
    }

    @Test
    void Lista_eliminar_indice() {
        lista.agregar(1);
        lista.agregar(2);
        assertDoesNotThrow(() -> dato = lista.eliminar(0));
        assertEquals(1, dato);
    }

    @Test
    void Lista_no_puede_eliminar_indice_si_esta_vacia() {
        lista.agregar(1);
        assertDoesNotThrow(() -> lista.eliminar());
        assertThrows(ExcepcionLista.class, () -> lista.eliminar(0));
    }

    @Test
    void Lista_no_puede_eliminar_indice_si_el_indice_es_negativo() {
        lista.agregar(1);
        assertThrows(ExcepcionLista.class, () -> lista.eliminar(-1));
    }

    @Test
    void Lista_no_puede_eliminar_indice_si_el_indice_es_mayor_o_igual_al_tamanio() {
        lista.agregar(1);
        assertThrows(ExcepcionLista.class, () -> lista.eliminar(1));
    }

    @Test
    void Lista_vacia() {
        assertTrue(lista.vacio());
    }

    @Test
    void Lista_no_vacia() {
        lista.agregar(1);
        assertFalse(lista.vacio());
    }

    @Test
    void Lista_tamanio_vacia() {
        assertEquals(0, lista.tamanio());
    }

    @Test
    void Lista_tamanio_no_vacia() {
        lista.agregar(1);
        assertEquals(1, lista.tamanio());
    }

    @Test
    void Lista_dato() {
        lista.agregar(1);
        assertEquals(1, lista.dato(0));
    }

    @Test
    void Lista_no_puede_obtener_dato_si_esta_vacia() {
        assertThrows(ExcepcionLista.class, () -> lista.dato(0));
    }

    @Test
    void Lista_no_puede_obtener_dato_si_el_indice_es_negativo() {
        lista.agregar(1);
        assertThrows(ExcepcionLista.class, () -> lista.dato(-1));
    }

    @Test
    void Lista_no_puede_obtener_dato_si_el_indice_es_mayor_o_igual_al_tamanio() {
        lista.agregar(1);
        assertThrows(ExcepcionLista.class, () -> lista.dato(1));
    }

    @Test
    void Lista_modificar_dato() {
        lista.agregar(1);
        lista.modificarDato(2, 0);
        assertEquals(2, lista.dato(0));
    }

    @Test
    void Lista_no_puede_modificar_dato_si_el_tamanio_es_cero() {
        assertThrows(ExcepcionLista.class, () -> lista.modificarDato(1, 0));
    }

    @Test
    void Lista_no_puede_modificar_dato_si_esta_vacia() {
        assertThrows(ExcepcionLista.class, () -> lista.modificarDato(1, 0));
    }

    @Test
    void Lista_no_puede_modificar_dato_si_el_indice_es_negativo() {
        lista.agregar(1);
        assertThrows(ExcepcionLista.class, () -> lista.modificarDato(2, -1));
    }

    @Test
    void Lista_no_puede_modificar_dato_si_el_indice_es_mayor_o_igual_al_tamanio() {
        lista.agregar(1);
        assertThrows(ExcepcionLista.class, () -> lista.modificarDato(2, 1));
    }

    @Test
    void Lista_caso_de_uso() {
        // El lista es [1, 2, 9, 3, 5, 6, 7, 10].
        Integer[] listaEsperado = {1, 2, 9, 3, 5, 6, 7, 10};

        // [1, 2, 3, 4, 5, 6, 7, 8].
        for (int i = 1; i <= 8; i++) {
            lista.agregar(i);
        }
        // [1, 2, 9, 3, 4, 5, 6, 7, 8].
        lista.agregar(9, 2);
        // [1, 2, 9, 3, 5, 6, 7, 8].
        lista.eliminar(4);
        // [1, 2, 9, 3, 5, 6, 7].
        lista.eliminar();
        // [1, 2, 9, 3, 5, 6, 7, 10].
        lista.agregar(10);

        assertEquals(listaEsperado.length, lista.tamanio());
        for (int i = 0; i < listaEsperado.length; i++) {
            assertEquals(listaEsperado[i], lista.dato(i));
        }
    }

    @Test
    void Lista_iterador() {
        assertDoesNotThrow(() -> lista.iterador());
    }

    @Test
    void Lista_iterador_indice() {
        assertDoesNotThrow(() -> lista.iterador(0));
    }

    @Test
    void Lista_iterador_no_puede_construirse_con_indice_negativo() {
        assertThrows(ExcepcionLista.class, () -> lista.iterador(-1));
    }

    @Test
    void Lista_iterador_no_puede_construirse_con_indice_mayor_al_tamanio() {
        assertThrows(ExcepcionLista.class, () -> lista.iterador(1));
    }

    @Test
    void Lista_iterador_dato() {
        lista.agregar(1);
        iterador = lista.iterador();
        assertDoesNotThrow(() -> dato = iterador.dato());
        assertEquals(1, dato);
    }

    @Test
    void Lista_iterador_no_puede_obtener_dato_en_lista_vacia() {
        iterador = lista.iterador();
        assertThrows(ExcepcionNoHayDato.class, () -> iterador.dato());
    }

    @Test
    void Lista_iterador_no_puede_obtener_dato_al_final_de_la_lista() {
        lista.agregar(1);
        iterador = lista.iterador(1);
        assertThrows(ExcepcionNoHayDato.class, () -> iterador.dato());
    }

    @Test
    void Lista_iterador_hay_siguiente() {
        lista.agregar(1);
        iterador = lista.iterador();
        assertTrue(iterador.haySiguiente());
    }

    @Test
    void Lista_iterador_no_hay_siguiente() {
        iterador = lista.iterador();
        assertFalse(iterador.haySiguiente());
    }

    @Test
    void Lista_iterador_siguiente() {
        lista.agregar(1);
        iterador = lista.iterador();
        assertDoesNotThrow(() -> iterador.siguiente());
    }

    @Test
    void Lista_iterador_no_puede_avanzar_si_no_hay_siguiente() {
        iterador = lista.iterador();
        assertThrows(ExcepcionNoHayDato.class, () -> iterador.siguiente());
    }

    @Test
    void Lista_iterador_hay_anterior() {
        lista.agregar(1);
        iterador = lista.iterador(1);
        assertTrue(iterador.hayAnterior());
    }

    @Test
    void Lista_iterador_no_hay_anterior() {
        lista.agregar(1);
        iterador = lista.iterador();
        assertFalse(iterador.hayAnterior());
    }

    @Test
    void Lista_iterador_anterior() {
        lista.agregar(1);
        lista.agregar(2);
        iterador = lista.iterador(2);
        assertDoesNotThrow(() -> iterador.anterior());
        assertDoesNotThrow(() -> iterador.anterior());
    }

    @Test
    void Lista_iterador_no_puede_retroceder_si_no_hay_anterior() {
        lista.agregar(1);
        iterador = lista.iterador();
        assertThrows(ExcepcionNoHayDato.class, () -> iterador.anterior());
    }

    @Test
    void Lista_iterador_agregar() {
        iterador = lista.iterador();
        assertDoesNotThrow(() -> iterador.agregar(1));
    }

    @Test
    void Lista_iterador_modificar_dato() {
        lista.agregar(1);
        iterador = lista.iterador();
        assertDoesNotThrow(() -> iterador.modificarDato(2));
        assertEquals(2, lista.dato(0));
    }

    @Test
    void Lista_iterador_no_puede_modificar_dato_si_la_lista_esta_vacia() {
        iterador = lista.iterador();
        assertThrows(ExcepcionNoHayDato.class, () -> iterador.modificarDato(1));
    }

    @Test
    void Lista_iterador_no_puede_modificar_dato_si_esta_al_final() {
        lista.agregar(1);
        iterador = lista.iterador(1);
        assertThrows(ExcepcionNoHayDato.class, () -> iterador.modificarDato(2));
    }

    @Test
    void Lista_iterador_eliminar() {
        lista.agregar(1);
        iterador = lista.iterador();
        assertDoesNotThrow(() -> dato = iterador.eliminar());
        assertEquals(1, dato);
    }

    @Test
    void Lista_iterador_no_puede_eliminar_si_la_lista_esta_vacia() {
        iterador = lista.iterador();
        assertThrows(ExcepcionNoHayDato.class, () -> iterador.eliminar());
    }

    @Test
    void Lista_iterador_no_puede_eliminar_si_esta_al_final() {
        lista.agregar(1);
        iterador = lista.iterador(1);
        assertThrows(ExcepcionNoHayDato.class, () -> iterador.eliminar());
    }

    @Test
    void Lista_iterador_caso_de_uso() {
        // El lista es [1, 2, 9, 3, 5, 6, 7, 10].
        Integer[] listaEsperado = {1, 2, 9, 3, 5, 6, 7, 10};

        iterador = lista.iterador();
        // [1, 2, 3, 4, 5, 6, 7, 8].
        for (int i = 1; i <= 8; i++) {
            iterador.agregar(i);
        }
        // [1, 2, 9, 3, 4, 5, 6, 7, 8].
        iterador = lista.iterador(2);
        iterador.agregar(9);
        // [1, 2, 9, 3, 5, 6, 7, 8].
        iterador.siguiente();
        iterador.eliminar();
        // [1, 2, 9, 3, 5, 6, 7].
        iterador = lista.iterador(7);
        iterador.eliminar();
        // [1, 2, 9, 3, 5, 6, 7, 10].
        iterador.agregar(10);

        assertEquals(listaEsperado.length, lista.tamanio());
        iterador = lista.iterador();
        int i = 0;
        while (iterador.haySiguiente()) {
            assertEquals(listaEsperado[i], iterador.dato());
            iterador.siguiente();
            i++;
        }
    }
}
