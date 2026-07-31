package org.ayed.poe.entidades;

import org.ayed.poe.excepciones.InventarioLlenoException;
import org.ayed.poe.inventario.Inventario;
import org.ayed.poe.inventario.Item;
import org.ayed.poe.inventario.TipoItem;
import org.ayed.tda.diccionario.Diccionario;
import org.ayed.tda.lista.Lista;

public class Equipamiento {

    private static final int TAMANIO_DICCIONARIO_EQUIPO = 7;

    private Diccionario<TipoItem, Item> equipo;
    private Item[] anillosEquipados;

    public Equipamiento(Diccionario<TipoItem, Item> equipo, Item[] anillos) {
        this.equipo = equipo;
        this.anillosEquipados = anillos;
    }

    public Equipamiento() {
        this.equipo = new Diccionario<TipoItem, Item>(TAMANIO_DICCIONARIO_EQUIPO);
        this.anillosEquipados = new Item[2];
    }

    public Diccionario<TipoItem, Item> getEquipo() {
        return this.equipo;
    }

    public Item[] getAnillos() {
        return this.anillosEquipados;
    }

    public Lista<Item> obtenerItemsEquipados() {
        return this.equipo.valores();
    }

    public void agregarEquipo(TipoItem tipo, Item item) {
        this.equipo.agregar(tipo, item);
    }

    public Item eliminarEquipo(TipoItem tipo) {
        return this.equipo.eliminar(tipo);
    }

    /**
     Determina si un item corresponde a un arma.

     Pre:
     - objeto != null.

     Post:
     - No modifica el estado del jugador.
     */

    public boolean esArma(Item objeto) {
        return this.esTipoArma(objeto.getTipo());
    }

    /**
     Equipa un anillo en un espacio disponible.
     Si ambos espacios están ocupados, intenta reemplazar uno de ellos.

     Pre:
     - anillo != null.

     Post:
     - Si fue posible equiparlo, el anillo queda equipado.
     - Si no había espacio en el inventario para reemplazar un anillo,
     no se realizan cambios.
     */

    private boolean equiparAnillo(Item anillo, Inventario inventario) {
        boolean guardado = false;
        boolean exito = false;
        int i = 0;

        while (i < this.anillosEquipados.length && !guardado) {
            if (this.anillosEquipados[i] == null) {
                this.anillosEquipados[i] = anillo;
                guardado = true;
                exito = true;
            }

            i++;
        }

        if (!guardado) {
            try {
                inventario.colocarItem(this.anillosEquipados[0]);
                this.anillosEquipados[0] = anillo;
                exito = true;

            } catch (InventarioLlenoException e) {
                System.out.println("No tenes espacio para desequipar este anillo");
            }
        }

        return exito;
    }

    public boolean equipar(Item objeto, Inventario inventario) {

        TipoItem tipo = objeto.getTipo();
        boolean teniaArma = false;

        Lista<Item> items = this.obtenerItemsEquipados();

        if (tipo == TipoItem.ANILLO) {

            return this.equiparAnillo(objeto, inventario);

        } else if (this.esArma(objeto)) {

            for (int i = 0; i < items.tamanio() && !teniaArma; i++) {
                if (this.esArma(items.dato(i))) {
                    teniaArma = true;
                }
            }

            if (teniaArma && !this.desequiparArma(inventario)) {
                return false;
            }

            this.agregarEquipo(tipo, objeto);

        } else {

            Item viejo = this.eliminarEquipo(tipo);

            if (viejo != null) {

                try {
                    inventario.colocarItem(viejo);

                } catch (InventarioLlenoException e) {
                    this.agregarEquipo(tipo, viejo);
                    return false;
                }
            }

            this.agregarEquipo(tipo, objeto);
        }

        return true;
    }

    /**
     Desequipa el arma actualmente equipada y la guarda en el inventario.

     Pre:
     - El jugador puede tener o no un arma equipada.

     Post:
     - Si existía un arma equipada y había espacio en el inventario,
     el arma fue removida del equipo y almacenada en el inventario.
     - Si no había espacio, el arma permanece equipada.
     */

    public boolean desequiparArma(Inventario inventario) {

        TipoItem[] armas = {
                TipoItem.ARMA_ARCO,
                TipoItem.ARMA_ESPADA,
                TipoItem.ARMA_MAZA,
                TipoItem.ARMA_VARITA
        };

        boolean exito = false;
        boolean armaEncontrada = false;

        int i = 0;

        while (i < armas.length && !armaEncontrada) {

            Item arma = this.eliminarEquipo(armas[i]);

            if (arma != null) {

                armaEncontrada = true;

                try {
                    inventario.colocarItem(arma);
                    exito = true;

                } catch (InventarioLlenoException e) {
                    this.agregarEquipo(arma.getTipo(), arma);
                }
            }

            i++;
        }

        if (!armaEncontrada) {
            System.out.println("No tenes ningun arma equipada");
        }

        return exito;
    }

    public boolean esTipoArma(TipoItem tipo) {

        return tipo == TipoItem.ARMA_ARCO
                || tipo == TipoItem.ARMA_ESPADA
                || tipo == TipoItem.ARMA_MAZA
                || tipo == TipoItem.ARMA_VARITA;
    }

    public boolean desequipar(TipoItem tipo, Inventario inventario) {

        Item itemDesequipado = this.eliminarEquipo(tipo);

        if (itemDesequipado != null) {

            try {
                inventario.colocarItem(itemDesequipado);
                return true;

            } catch (InventarioLlenoException e) {

                this.agregarEquipo(tipo, itemDesequipado);
            }
        }

        return false;
    }

    public Item obtenerAnillo(int posicion) {
        return this.anillosEquipados[posicion];
    }

    public void quitarAnillo(int posicion) {
        this.anillosEquipados[posicion] = null;
    }

    public boolean desequiparAnillo(int posicion, Inventario inventario) {

        Item anillo = this.obtenerAnillo(posicion);

        if (anillo != null) {

            try {
                inventario.colocarItem(anillo);
                this.quitarAnillo(posicion);
                return true;

            } catch (InventarioLlenoException e) {
                return false;
            }
        }

        return false;
    }

    public Lista<Item> obtenerTodosLosItemsEquipados() {
        Lista<Item> items = this.obtenerItemsEquipados();

        for (int i = 0; i < this.anillosEquipados.length; i++) {
            if (this.anillosEquipados[i] != null) {
                items.agregar(this.anillosEquipados[i]);
            }
        }

        return items;
    }

    /**
     Muestra por consola todos los objetos equipados.

     Pre:
     - Ninguna.

     Post:
     - Se imprime el equipamiento actual del jugador.
     */

    public void mostrarEquipo() {
        Lista<Item> itemsEquipados = this.obtenerItemsEquipados();
        Item itemActual = null;

        for (int i = 0; i < itemsEquipados.tamanio(); i++) {
            itemActual = itemsEquipados.dato(i);

            if (itemActual != null) {
                System.out.println("ID [" + itemActual.getId() + "]: " + itemActual);
            }
        }

        for (int i = 0; i < this.anillosEquipados.length; i++) {
            if (this.anillosEquipados[i] != null) {
                System.out.println((i + 1) + "-" + this.anillosEquipados[i]);
            }
        }
    }

}