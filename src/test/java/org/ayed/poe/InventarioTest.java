package org.ayed.poe;

import org.ayed.poe.excepciones.*;
import org.ayed.poe.inventario.Inventario;
import org.ayed.poe.inventario.Item;
import org.ayed.tda.vector.VectorDinamico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class InventarioTest {
    private Inventario inventario;
    private final int ANCHO = 12;
    private final int ALTO = 5;

    @BeforeEach
    void setUp() {
        inventario = new Inventario(ANCHO, ALTO);
    }

    @Test
    void Constructor_con_dimensiones_validas_debe_crear_inventario() {
        assertDoesNotThrow(() -> new Inventario(1, 1));
        assertDoesNotThrow(() -> new Inventario(100, 100));
    }

    @Test
    void Constructor_con_dimensiones_invalidas_debe_fallar() {
        assertThrows(IllegalArgumentException.class, () -> new Inventario(0, 5), "El ancho no puede ser 0");
        assertThrows(IllegalArgumentException.class, () -> new Inventario(-1, 5), "El ancho no puede ser negativo");
        assertThrows(IllegalArgumentException.class, () -> new Inventario(5, 0), "El alto no puede ser 0");
        assertThrows(IllegalArgumentException.class, () -> new Inventario(5, -1), "El alto no puede ser negativo");
    }

    @Test
    void Colocar_item_exito() {
        Item item = new Item(1, 2, 2);
        inventario.colocarItem(0, 0, item);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(item, inventario.obtenerItem(i, j),
                        "El item en la posición (" + i + "," + j + ") debería ser el mismo que se colocó.");
            }
        }
    }

    @Test
    void Colocar_item_fallo_fuera_de_limites() {
        Item item = new Item(1, 2, 2);
        assertThrows(InventarioFueraDeLimitesException.class,
                () -> inventario.colocarItem(ANCHO - 1, ALTO - 1, item),
                "Se esperaba InventarioFueraDeLimitesException al intentar colocar un item fuera de los límites de la grilla.");

        assertNull(inventario.obtenerItem(ANCHO - 1, ALTO - 1),
                "La celda debería permanecer vacía tras un intento fallido de colocación fuera de límites.");
    }

    @Test
    void Colocar_item_fallo_colision() {
        Item item1 = new Item(1, 2, 2);
        inventario.colocarItem(0, 0, item1);

        Item item2 = new Item(2, 2, 2);
        assertThrows(EspacioOcupadoException.class,
                () -> inventario.colocarItem(1, 1, item2),
                "Se esperaba EspacioOcupadoException al intentar colocar un item sobre otro existente.");

        assertEquals(item1, inventario.obtenerItem(0, 0), "El item original no debería verse afectado por la colisión.");
        assertEquals(item1, inventario.obtenerItem(1, 1), "El espacio ocupado no debería haber cambiado.");
        assertNull(inventario.obtenerItem(2, 2), "El item colisionante no debería haberse colocado, ni siquiera parcialmente.");
    }

    @Test
    void Colocar_item_fallo_item_duplicado() {
        Item item1 = new Item(1, 2, 2);
        inventario.colocarItem(0, 0, item1);

        Item item2 = new Item(1, 1, 1);

        assertThrows(ItemDuplicadoException.class,
                () -> inventario.colocarItem(2, 2, item2),
                "Se esperaba ItemDuplicadoException al intentar colocar un item con un ID ya existente.");

        assertNull(inventario.obtenerItem(2, 2), "No se debería haber colocado el item duplicado.");
    }

    @Test
    void Colocar_item_auto_exito() {
        Item item1 = new Item(1, 2, 2);
        inventario.colocarItem(0, 0, item1);

        Item item2 = new Item(2, 1, 1);
        inventario.colocarItem(item2);

        assertEquals(item2, inventario.obtenerItem(0, 2),
                "El colocar automático debería haber colocado el item en la primera posición disponible (0,2).");
    }

    @Test
    void Colocar_item_auto_fallo_espacio_insuficiente() {
        for (int y = 0; y < ALTO; y++) {
            for (int x = 0; x < ANCHO; x++) {
                inventario.colocarItem(x, y, new Item(x * 100 + y + 1000, 1, 1));
            }
        }

        Item item = new Item(9999, 1, 1);
        assertThrows(InventarioLlenoException.class,
                () -> inventario.colocarItem(item),
                "Se esperaba InventarioLlenoException cuando no hay espacio contiguo suficiente para el item.");
    }

    @Test
    void Colocar_item_auto_fallo_item_duplicado() {
        Item item1 = new Item(1, 2, 2);
        inventario.colocarItem(0, 0, item1);

        Item item2 = new Item(1, 1, 1);
        assertThrows(ItemDuplicadoException.class,
                () -> inventario.colocarItem(item2),
                "Se esperaba ItemDuplicadoException al intentar auto-slottear un item con ID existente.");
    }

    @Test
    void Eliminar_item_por_ID_exito() {
        Item item = new Item(1, 2, 2);
        inventario.colocarItem(0, 0, item);

        Item removed = inventario.eliminarItem(1);
        assertEquals(item, removed, "El item retornado por removeItem debería ser el mismo que se eliminó.");
        assertNull(inventario.obtenerItem(0, 0), "La posición (0,0) debería quedar vacía tras eliminar el item.");
        assertNull(inventario.obtenerItem(1, 1), "La posición (1,1) debería quedar vacía tras eliminar el item.");
    }

    @Test
    void Eliminar_item_por_ID_fallo_item_no_encontrado() {
        assertThrows(ItemNoEncontradoException.class,
                () -> inventario.eliminarItem(999),
                "Se esperaba ItemNoEncontradoException al intentar eliminar un ID inexistente.");
    }

    @Test
    void Eliminar_item_por_coordenada_exito() {
        Item item = new Item(1, 2, 2);
        inventario.colocarItem(0, 0, item);

        Item removed = inventario.eliminarItem(0, 0);
        assertEquals(item, removed, "El item eliminado por coordenada debería coincidir con el original.");
        assertNull(inventario.obtenerItem(0, 0), "La celda debería quedar vacía tras removeItemAt.");
    }

    @Test
    void Eliminar_item_por_coordenada_fallo_coordenada_vacia() {
        assertThrows(ItemNoEncontradoException.class,
                () -> inventario.eliminarItem(0, 0),
                "Se esperaba ItemNoEncontradoException al intentar eliminar en una celda vacía.");
    }

    @Test
    void Eliminar_item_por_coordenada_fallo_fuera_de_limites() {
        assertThrows(InventarioFueraDeLimitesException.class,
                () -> inventario.eliminarItem(ANCHO, ALTO),
                "Se esperaba InventarioFueraDeLimitesException al intentar eliminar fuera de la grilla.");
    }

    @Test
    void Transferir_items_exito() {
        VectorDinamico<Item> items = new VectorDinamico<>();
        items.agregar(new Item(1, 1, 1));
        items.agregar(new Item(2, 1, 1));

        inventario.transferirItems(items);

        assertEquals(items.obtener(0), inventario.obtenerItem(0, 0), "El primer item de la transferencia masiva debería estar en (0,0).");
        assertEquals(items.obtener(1), inventario.obtenerItem(0, 1), "El segundo item de la transferencia masiva debería estar en (0,1).");
    }

    @Test
    void Transferir_items_fallo_lleno() {
        for (int y = 0; y < ALTO; y++) {
            for (int x = 0; x < ANCHO; x++) {
                if (x == ANCHO - 1 && y == ALTO - 1) continue;
                inventario.colocarItem(x, y, new Item(x * 100 + y + 1000, 1, 1));
            }
        }

        VectorDinamico<Item> items = new VectorDinamico<>();
        items.agregar(new Item(9001, 1, 1));
        items.agregar(new Item(9002, 1, 1));

        assertThrows(InventarioLlenoException.class,
                () -> inventario.transferirItems(items),
                "Se esperaba InventarioLlenoException durante la transferencia masiva si no entran todos los items.");

        assertNull(inventario.obtenerItem(ANCHO - 1, ALTO - 1),
                "El inventario debería haber hecho rollback, eliminando el item 9001 que sí entraba.");
    }

    @Test
    void Transferir_items_fallo_item_duplicado() {
        Item existing = new Item(1, 1, 1);
        inventario.colocarItem(0, 0, existing);

        VectorDinamico<Item> items = new VectorDinamico<>();
        items.agregar(new Item(2, 1, 1));
        items.agregar(new Item(1, 1, 1));

        assertThrows(ItemDuplicadoException.class,
                () -> inventario.transferirItems(items),
                "Se esperaba ItemDuplicadoException durante la transferencia masiva si hay un item repetido.");

        assertNull(inventario.obtenerItem(1, 0),
                "El inventario debería haber hecho rollback, eliminando el item 2 que era válido.");
    }

    @Test
    void Guardar_cargar_exito() {
        String filepath = "inventario_test.save";

        Item item1 = new Item(1, 2, 2);
        inventario.colocarItem(0, 0, item1);

        inventario.guardar(filepath);

        Inventario newInventory = new Inventario(ANCHO, ALTO);
        newInventory.cargar(filepath);

        assertEquals(item1, newInventory.obtenerItem(0, 0),
                "El item cargado desde el archivo debería ser idéntico al guardado.");

        File file = new File(filepath);
        if (!file.delete()) {
            System.err.println("WARNING: No se pudo eliminar el archivo " + filepath + ".");
        }
    }

    @Test
    void Cargar_fallo_archivo_no_existente() {
        assertThrows(ArchivoNoExistenteException.class,
                () -> inventario.cargar("archivo_inexistente.save"),
                "Se esperaba ArchivoNoExistenteException al intentar cargar un archivo que no existe.");
    }
}
