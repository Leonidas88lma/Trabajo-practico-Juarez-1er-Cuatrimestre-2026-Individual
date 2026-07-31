package org.ayed.poe.mazmorra;

import org.ayed.poe.entidades.Jugador;
import org.ayed.poe.excepciones.InventarioLlenoException;
import org.ayed.poe.excepciones.ItemNoEncontradoException;
import org.ayed.poe.inventario.Inventario;
import org.ayed.poe.inventario.Item;
import org.ayed.sistemaGuardado.DatosEscondite;
import org.ayed.tda.vector.VectorDinamico;
import org.ayed.tda.vector.VectorEstatico;

public class Escondite {
    private final int ALTO_PAGINA = 12;
    private final int ANCHO_PAGINA = 12;
    private final int MAX_PAGINAS = 3;
    private final int PRECIO_PAGINA = 250;

    private VectorEstatico<Inventario> paginasAlijo;
    private int cantidadPaginasActivas;
    private int paginaActual;

    public Escondite() {
        this.paginasAlijo = new VectorEstatico<>(MAX_PAGINAS);

        for (int i = 0; i < MAX_PAGINAS; i++) {
            this.paginasAlijo.asignar(i, new Inventario(ANCHO_PAGINA, ALTO_PAGINA));
        }

        this.cantidadPaginasActivas = 1;
        this.paginaActual = 0;
    }
    
    public Escondite(DatosEscondite datos) {
      this();

      this.cantidadPaginasActivas = datos.getCantidadPaginasActivas();
      this.paginaActual = 0;

      int i = 0;
      while (i < this.cantidadPaginasActivas) {
          this.paginasAlijo.asignar(i, new Inventario(datos.getPagina(i)));
          i++;
      }
  }

    public boolean paginaSiguiente() {
        if (this.paginaActual < this.cantidadPaginasActivas - 1) {
            this.paginaActual += 1;
            return true;
        }
        return false;
    }

    public boolean paginaAnterior() {
        if (this.paginaActual > 0) {
            this.paginaActual -= 1;
            return true;
        }
        return false;
    }

    public int getNumeroPaginaActual() {
        return this.paginaActual + 1;
    }

    public int getCantidadPaginasActivas() {
        return this.cantidadPaginasActivas;
    }

    public Inventario getAlijoActual() {
        return this.paginasAlijo.obtener(this.paginaActual);
    }
    
    public Inventario getPagina(int numeroPagina) {
      return this.paginasAlijo.obtener(numeroPagina);
  }

    /**
     * Intenta comprar una pagina nueva para el alijo si el jugador tiene
     * suficientes monedas
     * 
     * @param jugador el jugador que va a pagar
     * 
     * @return true si se compro con exito o false si no tiene monedas o ya tiene el
     *         maximo
     */
    public boolean comprarPagina(Jugador jugador) {
        if (this.cantidadPaginasActivas < MAX_PAGINAS) {
            if (jugador.restarMonedas(PRECIO_PAGINA)) {
                this.cantidadPaginasActivas += 1;
                return true;
            } else {
                System.out.println("No tenes suficientes monedas");
                return false;
            }
        } else {
            System.out.println("Ya compraste todas las paginas");
        }

        return false;
    }

    /**
     * Saca un item del inventario del jugador y lo intenta guardar en el alijo.
     * Si no hay espacio en el alijo se lo devuelve al inventario del jugador
     * *
     * 
     * @param jugador el jugador dueño del item
     * 
     * @param idItem  el id del item que se quiere guardar
     * @return true si se guardo en el alijo o false si no se encontro el item o el
     *         alijo esta lleno
     */
    public boolean guardarItemEnAlijo(Jugador jugador, int idItem) {
        Item item = null;
        try {
            item = jugador.getInventario().eliminarItem(idItem);

        } catch (ItemNoEncontradoException e) {
            System.out.println("No existe item con ese ID");
            return false;
        }

        boolean guardado = false;
        int i = 0;

        while (!guardado && i < this.cantidadPaginasActivas) {
            try {
                this.paginasAlijo.obtener(i).colocarItem(item);
                guardado = true;
            } catch (InventarioLlenoException e) {
                i += 1;
            }
        }
        if (!guardado) {
            System.out.println("El alijo esta lleno");
            jugador.guardarItemInventario(item);
        }
        return guardado;

    }

    /**
     * Saca un item del alijo y lo guarda en el inventario del jugador.
     * Si el jugador no tiene espacio el item vuelve a quedar en el alijo
     * 
     * @param jugador el jugador que va a recibir el item
     * 
     * @param idItem  el id del item a retirar
     * @return true si se paso al inventario del jugador o false si no habia espacio
     *         o no existe el id
     */
    public boolean retirarItemDeAlijo(Jugador jugador, int idItem) {
        Item itemEncontrado = null;
        int i = 0;
        int paginaDondeEstaba = -1;

        while (itemEncontrado == null && i < this.cantidadPaginasActivas) {
            try {
                itemEncontrado = this.paginasAlijo.obtener(i).eliminarItem(idItem);
                paginaDondeEstaba = i;
            } catch (ItemNoEncontradoException e) {
                i += 1;
            }
        }

        if (itemEncontrado == null) {
            return false;
        }

        if (jugador.guardarItemInventario(itemEncontrado)) {
            return true;
        } else {
            this.paginasAlijo.obtener(paginaDondeEstaba).colocarItem(itemEncontrado);
            return false;
        }
    }

    /**
     * Agarra todos los items de la pagina actual del alijo y los manda a la mochila
     * del jugador
     * Si algun item no entra en la mochila se cancela todo y los items vuelven a la
     * pagina del alijo
     * 
     * @param jugador el jugador que va a recibir los items
     * 
     * @return true si se pasaron todos los items o false si se cancelo por falta de
     *         espacio o pagina vacia
     */
    public boolean retirarPaginaCompleta(Jugador jugador) {
        Inventario alijoActual = this.getAlijoActual();
        VectorDinamico<Item> itemsATransferir = new VectorDinamico<Item>();
        VectorDinamico<Integer> idsEncontrados = new VectorDinamico<Integer>();
        Item item = null;
        boolean yaRegistrado = false;

        for (int y = 0; y < ALTO_PAGINA; y++) {
            for (int x = 0; x < ANCHO_PAGINA; x++) {
                item = alijoActual.obtenerItem(x, y);
                if (item != null) {
                    yaRegistrado = false;
                    for (int i = 0; i < idsEncontrados.tamanio(); i++) {
                        if (idsEncontrados.obtener(i) == item.getId()) {
                            yaRegistrado = true;
                        }
                    }
                    if (!yaRegistrado) {
                        idsEncontrados.agregar(item.getId());
                    }
                }
            }
        }

        if (idsEncontrados.tamanio() == 0) {
            return false;
        }

        for (int i = 0; i < idsEncontrados.tamanio(); i++) {
            itemsATransferir.agregar(alijoActual.eliminarItem(idsEncontrados.obtener(i)));
        }

        try {
            jugador.getInventario().transferirItems(itemsATransferir);
            return true;

        } catch (RuntimeException e) {
            for (int i = 0; i < itemsATransferir.tamanio(); i++) {
                try {
                    alijoActual.colocarItem(itemsATransferir.obtener(i));
                } catch (InventarioLlenoException er) {
                    System.out.println("Ocurrio un error interno");
                }
            }
            return false;
        }
    }

    public void mostrarPaginaActual() {
        System.out.println("Pagina " + this.getNumeroPaginaActual() + "/" + this.cantidadPaginasActivas);
        this.getAlijoActual().mostrarInventario();
    }

}