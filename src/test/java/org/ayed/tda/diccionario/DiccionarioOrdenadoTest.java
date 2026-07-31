package org.ayed.tda.diccionario;

import org.ayed.tda.comparador.Comparador;
import org.ayed.tda.lista.Lista;
import org.ayed.tda.tupla.Tupla;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class DiccionarioOrdenadoTest {
    private DiccionarioOrdenado<Integer, Integer> diccionarioOrdenado;
    private Comparador<Integer> comparador;
    private Integer valor;
    private Lista<Tupla<Integer, Integer>> recorrido;

    private static class Comp implements Comparador<Integer> {
        @Override
        public int comparar(Integer dato1, Integer dato2) {
            return dato1.compareTo(dato2);
        }
    }

    private void comparar(Lista<Tupla<Integer, Integer>> esperado, Lista<Tupla<Integer, Integer>> actual) {
        assertEquals(esperado.tamanio(), actual.tamanio());
        for (int i = 0; i < esperado.tamanio(); i++) {
            assertEquals(esperado.dato(i), actual.dato(i));
        }
    }

    private void compararRecorrido(Integer[] recorridoEsperado) {
        assertEquals(recorridoEsperado.length, recorrido.tamanio());
        for (int i = 0; i < recorridoEsperado.length; i++) {
            assertEquals(recorridoEsperado[i], recorrido.dato(i).valor());
        }
    }

    private void llenarDiccionario() {
        diccionarioOrdenado.agregar(10, 10);
        diccionarioOrdenado.agregar(7, 7);
        diccionarioOrdenado.agregar(13, 13);
        diccionarioOrdenado.agregar(5, 5);
        diccionarioOrdenado.agregar(9, 9);
        diccionarioOrdenado.agregar(11, 11);
        diccionarioOrdenado.agregar(14, 14);
        diccionarioOrdenado.agregar(3, 3);
        diccionarioOrdenado.agregar(8, 8);
        diccionarioOrdenado.agregar(12, 12);
        diccionarioOrdenado.agregar(16, 16);
        diccionarioOrdenado.agregar(4, 4);
        diccionarioOrdenado.agregar(15, 15);
    }

    @BeforeEach
    void setUp() {
        comparador = new Comp();
        diccionarioOrdenado = new DiccionarioOrdenado<>(comparador);
    }

    @Test
    void Diccionario_ordenado_no_puede_construirse_con_comparador_nulo() {
        comparador = null;
        assertThrows(ExcepcionDiccionario.class, () -> new DiccionarioOrdenado<>(comparador));
    }

    @Test
    void Diccionario_ordenado_constructor_de_copia() {
        ArrayList<Integer> datos = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            datos.add(i);
        }
        Collections.shuffle(datos);

        for (Integer i : datos) {
            diccionarioOrdenado.agregar(i, i);
        }

        DiccionarioOrdenado<Integer, Integer> diccionarioOrdenadoCopia = new DiccionarioOrdenado<>(diccionarioOrdenado);

        comparar(diccionarioOrdenado.inorder(), diccionarioOrdenadoCopia.inorder());
        comparar(diccionarioOrdenado.preorder(), diccionarioOrdenadoCopia.preorder());
        comparar(diccionarioOrdenado.postorder(), diccionarioOrdenadoCopia.postorder());
        comparar(diccionarioOrdenado.ancho(), diccionarioOrdenadoCopia.ancho());
    }

    @Test
    void Diccionario_ordenado_vacio_constructor_de_copia() {
        DiccionarioOrdenado<Integer, Integer> diccionarioOrdenadoCopia = new DiccionarioOrdenado<>(diccionarioOrdenado);

        comparar(diccionarioOrdenado.inorder(), diccionarioOrdenadoCopia.inorder());
        comparar(diccionarioOrdenado.preorder(), diccionarioOrdenadoCopia.preorder());
        comparar(diccionarioOrdenado.postorder(), diccionarioOrdenadoCopia.postorder());
        comparar(diccionarioOrdenado.ancho(), diccionarioOrdenadoCopia.ancho());
    }

    @Test
    void Diccionario_ordenado_nulo_no_puede_copiarse() {
        diccionarioOrdenado = null;
        assertThrows(ExcepcionDiccionario.class, () -> new DiccionarioOrdenado<>(diccionarioOrdenado));
    }

    @Test
    void Diccionario_ordenado_agregar() {
        assertDoesNotThrow(() -> diccionarioOrdenado.agregar(1, 1));
    }

    @Test
    void Diccionario_ordenado_agregar_subarbol_izquierdo() {
        diccionarioOrdenado.agregar(5, 5);
        diccionarioOrdenado.agregar(2, 2);
        diccionarioOrdenado.agregar(8, 8);
        assertDoesNotThrow(() -> diccionarioOrdenado.agregar(1, 1));
        assertDoesNotThrow(() -> diccionarioOrdenado.agregar(3, 3));
    }

    @Test
    void Diccionario_ordenado_agregar_subarbol_derecho() {
        diccionarioOrdenado.agregar(5, 5);
        diccionarioOrdenado.agregar(2, 2);
        diccionarioOrdenado.agregar(8, 8);
        assertDoesNotThrow(() -> diccionarioOrdenado.agregar(7, 7));
        assertDoesNotThrow(() -> diccionarioOrdenado.agregar(10, 10));
    }

    @Test
    void Diccionario_ordenado_agregar_clave_duplicada_devuelve_valor_anterior() {
        diccionarioOrdenado.agregar(1, 1);
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.agregar(1, 2));
        assertEquals(1, valor);
    }

    @Test
    void Diccionario_ordenado_eliminar() {
        diccionarioOrdenado.agregar(1, 1);
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(1));
        assertEquals(1, valor);
    }

    @Test
    void Diccionario_ordenado_eliminar_hoja_subarbol_izquierdo() {
        llenarDiccionario();
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(4));
        assertEquals(4, valor);
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(8));
        assertEquals(8, valor);
    }

    @Test
    void Diccionario_ordenado_eliminar_hoja_subarbol_derecho() {
        llenarDiccionario();
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(12));
        assertEquals(12, valor);
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(15));
        assertEquals(15, valor);
    }

    @Test
    void Diccionario_ordenado_eliminar_nodo_interior_con_un_hijo_subarbol_izquierdo() {
        llenarDiccionario();
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(5));
        assertEquals(5, valor);
    }

    @Test
    void Diccionario_ordenado_eliminar_nodo_interior_con_un_hijo_subarbol_derecho() {
        llenarDiccionario();
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(16));
        assertEquals(16, valor);
    }

    @Test
    void Diccionario_ordenado_eliminar_nodo_interior_con_dos_hijos_subarbol_izquierdo() {
        llenarDiccionario();
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(7));
        assertEquals(7, valor);
    }

    @Test
    void Diccionario_ordenado_eliminar_nodo_interior_con_dos_hijos_subarbol_derecho() {
        llenarDiccionario();
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(13));
        assertEquals(13, valor);
    }

    @Test
    void Diccionario_ordenado_eliminar_raiz() {
        llenarDiccionario();
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(10));
        assertEquals(10, valor);
    }

    @Test
    void Diccionario_ordenado_eliminar_raiz_con_solo_hijo_derecho() {
        diccionarioOrdenado.agregar(10, 10);
        diccionarioOrdenado.agregar(13, 13);
        diccionarioOrdenado.agregar(11, 11);
        diccionarioOrdenado.agregar(14, 14);
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(10));
        assertEquals(10, valor);
    }

    @Test
    void Diccionario_ordenado_eliminar_raiz_con_solo_hijo_izquierdo() {
        diccionarioOrdenado.agregar(10, 10);
        diccionarioOrdenado.agregar(7, 7);
        diccionarioOrdenado.agregar(5, 5);
        diccionarioOrdenado.agregar(9, 9);
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(10));
        assertEquals(10, valor);
    }

    @Test
    void Diccionario_ordenado_puede_eliminar_si_esta_vacio() {
        diccionarioOrdenado.agregar(1, 1);
        assertDoesNotThrow(() -> diccionarioOrdenado.eliminar(1));
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(1));
        assertNull(valor);
    }

    @Test
    void Diccionario_ordenado_puede_eliminar_si_la_clave_no_existe() {
        diccionarioOrdenado.agregar(1, 1);
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.eliminar(2));
        assertNull(valor);
    }

    @Test
    void Diccionario_ordenado_obtener_valor() {
        diccionarioOrdenado.agregar(1, 1);
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.obtenerValor(1));
        assertEquals(1, valor);
    }

    @Test
    void Diccionario_ordenado_puede_obtener_valor_si_esta_vacio() {
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.obtenerValor(1));
        assertNull(valor);
    }

    @Test
    void Diccionario_ordenado_puede_obtener_valor_si_la_clave_no_existe() {
        diccionarioOrdenado.agregar(1, 1);
        assertDoesNotThrow(() -> valor = diccionarioOrdenado.obtenerValor(2));
        assertNull(valor);
    }

    @Test
    void Diccionario_ordenado_inorder() {
        llenarDiccionario();
        Integer[] recorridoEsperado = {3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        recorrido = diccionarioOrdenado.inorder();
        compararRecorrido(recorridoEsperado);
    }

    @Test
    void Diccionario_ordenado_vacio_inorder() {
        Integer[] recorridoEsperado = {};
        recorrido = diccionarioOrdenado.inorder();
        compararRecorrido(recorridoEsperado);
    }

    @Test
    void Diccionario_ordenado_preorder() {
        llenarDiccionario();
        Integer[] recorridoEsperado = {10, 7, 5, 3, 4, 9, 8, 13, 11, 12, 14, 16, 15};
        recorrido = diccionarioOrdenado.preorder();
        compararRecorrido(recorridoEsperado);
    }

    @Test
    void Diccionario_ordenado_vacio_preorder() {
        Integer[] recorridoEsperado = {};
        recorrido = diccionarioOrdenado.preorder();
        compararRecorrido(recorridoEsperado);
    }

    @Test
    void Diccionario_ordenado_postorder() {
        llenarDiccionario();
        Integer[] recorridoEsperado = {4, 3, 5, 8, 9, 7, 12, 11, 15, 16, 14, 13, 10};
        recorrido = diccionarioOrdenado.postorder();
        compararRecorrido(recorridoEsperado);
    }

    @Test
    void Diccionario_ordenado_vacio_postorder() {
        Integer[] recorridoEsperado = {};
        recorrido = diccionarioOrdenado.postorder();
        compararRecorrido(recorridoEsperado);
    }

    @Test
    void Diccionario_ordenado_ancho() {
        llenarDiccionario();
        Integer[] recorridoEsperado = {10, 7, 13, 5, 9, 11, 14, 3, 8, 12, 16, 4, 15};
        recorrido = diccionarioOrdenado.ancho();
        compararRecorrido(recorridoEsperado);
    }

    @Test
    void Diccionario_ordenado_vacio_ancho() {
        Integer[] recorridoEsperado = {};
        recorrido = diccionarioOrdenado.ancho();
        compararRecorrido(recorridoEsperado);
    }

    @Test
    void Diccionario_ordenado_vacio() {
        assertTrue(diccionarioOrdenado.vacio());
    }

    @Test
    void Diccionario_ordenado_no_vacio() {
        diccionarioOrdenado.agregar(1, 1);
        assertFalse(diccionarioOrdenado.vacio());
    }

    @Test
    void Diccionario_ordenado_tamanio_vacio() {
        assertEquals(0, diccionarioOrdenado.tamanio());
    }

    @Test
    void Diccionario_ordenado_tamanio_no_vacio() {
        diccionarioOrdenado.agregar(1, 1);
        assertEquals(1, diccionarioOrdenado.tamanio());
    }

    @Test
    void Diccionario_ordenado_caso_de_uso() {
        Integer[] recorridoEsperado;

        llenarDiccionario();

        diccionarioOrdenado.eliminar(8);

        recorridoEsperado = new Integer[]{3, 4, 5, 7, 9, 10, 11, 12, 13, 14, 15, 16};
        recorrido = diccionarioOrdenado.inorder();
        compararRecorrido(recorridoEsperado);
        recorridoEsperado = new Integer[]{10, 7, 5, 3, 4, 9, 13, 11, 12, 14, 16, 15};
        recorrido = diccionarioOrdenado.preorder();
        compararRecorrido(recorridoEsperado);
        recorridoEsperado = new Integer[]{4, 3, 5, 9, 7, 12, 11, 15, 16, 14, 13, 10};
        recorrido = diccionarioOrdenado.postorder();
        compararRecorrido(recorridoEsperado);
        recorridoEsperado = new Integer[]{10, 7, 13, 5, 9, 11, 14, 3, 12, 16, 4, 15};
        recorrido = diccionarioOrdenado.ancho();
        compararRecorrido(recorridoEsperado);

        diccionarioOrdenado.eliminar(5);

        recorridoEsperado = new Integer[]{3, 4, 7, 9, 10, 11, 12, 13, 14, 15, 16};
        recorrido = diccionarioOrdenado.inorder();
        compararRecorrido(recorridoEsperado);
        recorridoEsperado = new Integer[]{10, 7, 3, 4, 9, 13, 11, 12, 14, 16, 15};
        recorrido = diccionarioOrdenado.preorder();
        compararRecorrido(recorridoEsperado);
        recorridoEsperado = new Integer[]{4, 3, 9, 7, 12, 11, 15, 16, 14, 13, 10};
        recorrido = diccionarioOrdenado.postorder();
        compararRecorrido(recorridoEsperado);
        recorridoEsperado = new Integer[]{10, 7, 13, 3, 9, 11, 14, 4, 12, 16, 15};
        recorrido = diccionarioOrdenado.ancho();
        compararRecorrido(recorridoEsperado);

        diccionarioOrdenado.eliminar(13);

        recorridoEsperado = new Integer[]{3, 4, 7, 9, 10, 11, 12, 14, 15, 16};
        recorrido = diccionarioOrdenado.inorder();
        compararRecorrido(recorridoEsperado);
        recorridoEsperado = new Integer[]{10, 7, 3, 4, 9, 14, 11, 12, 16, 15};
        recorrido = diccionarioOrdenado.preorder();
        compararRecorrido(recorridoEsperado);
        recorridoEsperado = new Integer[]{4, 3, 9, 7, 12, 11, 15, 16, 14, 10};
        recorrido = diccionarioOrdenado.postorder();
        compararRecorrido(recorridoEsperado);
        recorridoEsperado = new Integer[]{10, 7, 14, 3, 9, 11, 16, 4, 12, 15};
        recorrido = diccionarioOrdenado.ancho();
        compararRecorrido(recorridoEsperado);

        diccionarioOrdenado.eliminar(10);

        recorridoEsperado = new Integer[]{3, 4, 7, 9, 11, 12, 14, 15, 16};
        recorrido = diccionarioOrdenado.inorder();
        compararRecorrido(recorridoEsperado);
        recorridoEsperado = new Integer[]{11, 7, 3, 4, 9, 14, 12, 16, 15};
        recorrido = diccionarioOrdenado.preorder();
        compararRecorrido(recorridoEsperado);
        recorridoEsperado = new Integer[]{4, 3, 9, 7, 12, 15, 16, 14, 11};
        recorrido = diccionarioOrdenado.postorder();
        compararRecorrido(recorridoEsperado);
        recorridoEsperado = new Integer[]{11, 7, 14, 3, 9, 12, 16, 4, 15};
        recorrido = diccionarioOrdenado.ancho();
        compararRecorrido(recorridoEsperado);
    }
}
