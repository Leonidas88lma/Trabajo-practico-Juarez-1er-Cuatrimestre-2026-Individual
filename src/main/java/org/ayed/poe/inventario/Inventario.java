package org.ayed.poe.inventario;

import org.ayed.poe.excepciones.*;
import org.ayed.tda.matriz.Matriz;
import org.ayed.tda.vector.VectorDinamico;

public class Inventario {

  private Matriz<Item> grilla;
  private VectorDinamico<Item> items;
  private VectorDinamico<Integer> posX;
  private VectorDinamico<Integer> posY;

  public Inventario(int ancho, int alto) {
    if (ancho < 1 || alto < 1) {
      throw new IllegalArgumentException();
    }

    grilla = new Matriz<>(alto, ancho);
    items = new VectorDinamico<>();
    posX = new VectorDinamico<>();
    posY = new VectorDinamico<>();
  }

  public Inventario(Inventario otro) {
    this.grilla = new Matriz<>(otro.grilla);
    this.items = new VectorDinamico<>(otro.items);
    this.posX = new VectorDinamico<>(otro.posX);
    this.posY = new VectorDinamico<>(otro.posY);
  }

  public int getAlto() {
    return this.grilla.filas();
  }

  public int getAncho() {
    return this.grilla.columnas();
  }

  public Item obtenerItem(int x, int y) {
    validarCoord(x, y);
    return grilla.elemento(y, x);
  }

  public void mostrarInventario() {
    Item itemActual = null;
    for (int y = 0; y < grilla.filas(); y++) {
      for (int x = 0; x < grilla.columnas(); x++) {
        itemActual = obtenerItem(x, y);

        if (itemActual == null) {
          System.out.print("[  .  ] ");

        } else {
          System.out.printf("[%3d  ] ", itemActual.getId());
        }
      }
      System.out.println();
    }
    System.out.println();
    this.mostrarItems();
  }

  public void colocarItem(int x, int y, Item item) {
    validarDuplicado(item.getId());
    validarFueraDeLimites(x, y, item);
    validarColision(x, y, item);

    for (int i = 0; i < item.getAlto(); i++) {
      for (int j = 0; j < item.getAncho(); j++) {
        grilla.asignar(y + i, x + j, item);
      }
    }

    items.agregar(item);
    posX.agregar(x);
    posY.agregar(y);
  }

  public void colocarItem(Item item) {
    validarDuplicado(item.getId());

    int x = 0;
    boolean colocado = false;

    while (x < grilla.columnas() && !colocado) {

      int y = 0;

      while (y < grilla.filas() && !colocado) {

        if (entra(x, y, item)) {
          colocarItem(x, y, item);
          colocado = true;
        }

        y++;
      }

      x++;
    }

    if (!colocado) {
      throw new InventarioLlenoException();
    }
  }

  public Item eliminarItem(int id) {
    int idx = buscarIndice(id);
    if (idx == -1) {
      throw new ItemNoEncontradoException();
    }

    Item item = items.obtener(idx);
    int x = posX.obtener(idx);
    int y = posY.obtener(idx);

    limpiar(x, y, item);

    items.eliminar(idx);
    posX.eliminar(idx);
    posY.eliminar(idx);

    return item;
  }

  public Item eliminarItem(int x, int y) {
    validarCoord(x, y);

    Item item = grilla.elemento(y, x);
    if (item == null) {
      throw new ItemNoEncontradoException();
    }

    return eliminarItem(item.getId());
  }

  public void transferirItems(VectorDinamico<Item> nuevos) {
    Matriz<Item> backup = new Matriz<>(grilla);
    VectorDinamico<Item> itemsBackup = new VectorDinamico<>(items);
    VectorDinamico<Integer> xBackup = new VectorDinamico<>(posX);
    VectorDinamico<Integer> yBackup = new VectorDinamico<>(posY);

    try {
      for (int i = 0; i < nuevos.tamanio(); i++) {
        colocarItem(nuevos.obtener(i));
      }
    } catch (RuntimeException e) {
      grilla = backup;
      items = itemsBackup;
      posX = xBackup;
      posY = yBackup;
      throw e;
    }
  }

  public Item obtenerItemPorId(int id) {
    int indice = this.buscarIndice(id);
    Item item = null;
    if (indice != -1) {
      item = this.items.obtener(indice);
    }

    return item;
  }

  public void guardar(String ruta) {
    try (java.io.PrintWriter pw = new java.io.PrintWriter(ruta)) {

      pw.println(grilla.columnas() + " " + grilla.filas());

      for (int i = 0; i < items.tamanio(); i++) {
        Item item = items.obtener(i);

        pw.println(item.getId() + " " + item.getAncho() + " " + item.getAlto() + " "
            + posX.obtener(i) + " " + posY.obtener(i));
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void cargar(String ruta) {
    java.io.File f = new java.io.File(ruta);
    if (!f.exists()) {
      throw new ArchivoNoExistenteException();
    }

    try (java.util.Scanner sc = new java.util.Scanner(f)) {

      int ancho = sc.nextInt();
      int alto = sc.nextInt();

      grilla = new Matriz<>(alto, ancho);
      items = new VectorDinamico<>();
      posX = new VectorDinamico<>();
      posY = new VectorDinamico<>();

      while (sc.hasNext()) {
        int id = sc.nextInt();
        int w = sc.nextInt();
        int h = sc.nextInt();
        int x = sc.nextInt();
        int y = sc.nextInt();

        colocarItem(x, y, new Item(id, w, h));
      }

    } catch (Exception e) {
      grilla = new Matriz<>(1, 1);
      items = new VectorDinamico<>();
      posX = new VectorDinamico<>();
      posY = new VectorDinamico<>();
    }
  }

  // funciones auxiliares estas 4

  private void validarCoord(int x, int y) {
    if (x < 0 || x >= grilla.columnas() || y < 0 || y >= grilla.filas()) {
      throw new InventarioFueraDeLimitesException();
    }
  }

  private void validarDuplicado(int id) {
    if (buscarIndice(id) != -1) {
      throw new ItemDuplicadoException();
    }
  }

  private int buscarIndice(int id) {
    int i = 0;

    while (i < items.tamanio() && items.obtener(i).getId() != id) {
      i++;
    }

    if (i < items.tamanio()) {
      return i;
    }

    return -1;
  }

  private void mostrarItems() {
    Item itemActual = null;
    for (int i = 0; i < items.tamanio(); i++) {
      itemActual = items.obtener(i);
      System.out.println("ID [" + itemActual.getId() + "]: " + itemActual);
    }
  }

  // SEPARADO EN DOS VALIDACIONES

  private void validarFueraDeLimites(int x, int y, Item item) {
    if (x < 0 || y < 0 || x + item.getAncho() > grilla.columnas()
        || y + item.getAlto() > grilla.filas()) {
      throw new InventarioFueraDeLimitesException();
    }
  }

  private void validarColision(int x, int y, Item item) {

    int i = 0;
    boolean colision = false;

    while (i < item.getAlto() && !colision) {

      int j = 0;

      while (j < item.getAncho() && !colision) {

        if (grilla.elemento(y + i, x + j) != null) {
          colision = true;
        }

        j++;
      }

      i++;
    }

    if (colision) {
      throw new EspacioOcupadoException();
    }
  }

  private boolean entra(int x, int y, Item item) {

    if (x + item.getAncho() > grilla.columnas()
        || y + item.getAlto() > grilla.filas()) {
      return false;
    }

    int i = 0;
    boolean libre = true;

    while (i < item.getAlto() && libre) {

      int j = 0;

      while (j < item.getAncho() && libre) {

        if (grilla.elemento(y + i, x + j) != null) {
          libre = false;
        }

        j++;
      }

      i++;
    }

    return libre;
  }

  private void limpiar(int x, int y, Item item) {
    for (int i = 0; i < item.getAlto(); i++) {
      for (int j = 0; j < item.getAncho(); j++) {
        grilla.asignar(y + i, x + j, null);
      }
    }
  }
}
