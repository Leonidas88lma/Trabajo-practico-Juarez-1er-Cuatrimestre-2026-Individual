package org.ayed.sistemaGuardado;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;

import org.ayed.poe.Pasiva;
import org.ayed.tda.lista.Lista;
import org.ayed.tda.iterador.Iterador;

import org.ayed.poe.ArbolPasivas;
import org.ayed.poe.inventario.TipoItem;
import org.ayed.poe.mazmorra.Escondite;
import org.ayed.tda.diccionario.Diccionario;
import org.ayed.tda.vector.VectorDinamico;
import org.ayed.poe.entidades.Jugador;
import org.ayed.poe.inventario.Inventario;
import org.ayed.poe.inventario.Item;
import org.ayed.poe.inventario.Estadistica;
import org.ayed.poe.inventario.RarezaItem;

public class GestorGuardado {

  private static final int ANCHO_INVENTARIO = 10;
  private static final int ALTO_INVENTARIO = 10;
  private String lineaPendiente;

  public GestorGuardado() {
  }

  public boolean guardarPartida(DatosPartida datos, String rutaArchivo) {
    FileWriter archivo = null;

    try {
      archivo = new FileWriter(rutaArchivo);
      DatosJugador datosJugador = datos.getDatosJugador();

      archivo.write("[JUGADOR]\n");
      archivo.write("nivel;" + datosJugador.getNivel() + "\n");
      archivo.write("experienciaAcumulada;" + datosJugador.getExperienciaAcumulada() + "\n");
      archivo.write("puntosHabilidad;" + datosJugador.getPuntosHabilidad() + "\n");
      archivo.write("experienciaRestante;" + datosJugador.getExperienciaRestanteProxNivel() + "\n");
      archivo.write("monedas;" + datosJugador.getMonedas() + "\n");
      archivo.write("ultimoIdAgregado;" + datos.getUltimoIdItems() + "\n");

      this.guardarInventario(datosJugador.getInventario(), archivo, "\n[INVENTARIO]\n");

      archivo.write("\n[ESCONDITE]\n");
      archivo
          .write("Paginas activas;" + datos.getDatosEscondite().getCantidadPaginasActivas() + "\n");

      for (int i = 0; i < datos.getDatosEscondite().getCantidadPaginasActivas(); i++) {
        this.guardarInventario(datos.getDatosEscondite().getPagina(i), archivo,
            "[PAGINA_" + i + "]\n");
      }

      archivo.write("\n[EQUIPO]\n");
      this.guardarEquipo(datos, archivo);

      archivo.write("\n[ARBOL]\n");
      this.guardarArbol(datos, archivo);

      return true;

    } catch (IOException e) {
      System.out.println("--- Error al escribir el archivo ---");
      return false;

    } finally {
      if (archivo != null) {
        try {
          archivo.close();

        } catch (IOException e) {
          System.out.println("--- Error al cerrar el archivo ---");

        }
      }
    }
  }

  public DatosPartida cargarPartida(String rutaArchivo) {
    Diccionario<TipoItem, Item> equipo = null;
    DatosJugador datosJugador = null;
    Inventario inventario = null;
    DatosEscondite datosEscondite = null;
    ArbolPasivas arbol = null;

    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {

      String linea = br.readLine();

      while (linea != null) {

        linea = linea.trim();

        if (linea.equals("[JUGADOR]")) {

          datosJugador = cargarJugador(br);

        } else if (linea.equals("[INVENTARIO]")) {

          Lista<String> lineasInventario = leerLineasInventario(br);

          inventario = cargarInventario(lineasInventario);
        } else if (linea.equals("[EQUIPO]")) {

          Lista<String> lineasEquipo = leerLineasInventario(br);

          equipo = cargarEquipo(lineasEquipo);
        } else if (linea.equals("[ESCONDITE]")) {

          datosEscondite = cargarEscondite(br);

        } else if (linea.equals("[ARBOL]")) {

          Jugador jugadorTemporal = new Jugador(0, 0);

          arbol = cargarArbolDesdeArchivo("arbol.txt", jugadorTemporal);

          Lista<String> lineasArbol = leerLineasInventario(br);

          cargarArbol(lineasArbol, arbol);
        } else if (equipo != null) {
          System.out.println("Equipo cargado:");

          Lista<Item> objetos = equipo.valores();

          Iterador<Item> iterador = objetos.iterador();

          while (iterador.haySiguiente()) {

            System.out.println(iterador.dato());

            iterador.siguiente();
          }
        }

        if (lineaPendiente != null) {
          linea = lineaPendiente;
          lineaPendiente = null;
        } else {
          linea = br.readLine();
        }
      }

      if (datosJugador != null) {
        System.out.println("Nivel: " + datosJugador.getNivel());
        System.out.println("Experiencia: " + datosJugador.getExperienciaAcumulada());
        System.out.println("Puntos habilidad: " + datosJugador.getPuntosHabilidad());
        System.out
            .println("Experiencia restante: " + datosJugador.getExperienciaRestanteProxNivel());
        System.out.println("Monedas: " + datosJugador.getMonedas());
      }

      if (inventario != null) {
        System.out.println("Inventario cargado:");
        inventario.mostrarInventario();
      }

    } catch (IOException e) {
      System.out.println("--- Error al leer el archivo ---");
    }

    if (datosJugador != null) {

      datosJugador.setInventario(inventario);
      datosJugador.setEquipo(equipo);

      return new DatosPartida(datosJugador, datosEscondite, arbol, datosJugador.getNivel(), 100);
    }

    return null;
  }

  private boolean guardarInventario(Inventario inventario, FileWriter archivo, String mensaje) {

    VectorDinamico<Integer> idsEncontrados = new VectorDinamico<Integer>();
    Item item = null;
    boolean yaRegistrado = false;

    try {
      archivo.write(mensaje);

    } catch (IOException e) {
      System.out.println("--- Error al escribir el archivo ---");
      return false;
    }

    for (int y = 0; y < inventario.getAlto(); y++) {
      for (int x = 0; x < inventario.getAncho(); x++) {
        item = inventario.obtenerItem(x, y);
        if (item != null) {
          yaRegistrado = false;
          for (int i = 0; i < idsEncontrados.tamanio(); i++) {
            if (idsEncontrados.obtener(i) == item.getId()) {
              yaRegistrado = true;
            }
          }
          if (!yaRegistrado) {
            this.escribirItemArchivo(archivo, item, x, y);
            idsEncontrados.agregar(item.getId());

          }
        }
      }
    }

    try {
      archivo.write("\n");

    } catch (IOException e) {
      System.out.println("--- Error al escribir el archivo ---");
      return false;
    }

    return true;
  }

  private boolean guardarEquipo(DatosPartida datos, FileWriter archivo) {

    Diccionario<TipoItem, Item> equipo = datos.getDatosJugador().getEquipo();
    Lista<Item> objetos = equipo.valores();
    Iterador<Item> iterador = objetos.iterador();
    Item objetoActual = null;

    while (iterador.haySiguiente()) {
      objetoActual = iterador.dato();
      if (objetoActual != null) {
        this.escribirItemArchivo(archivo, objetoActual, null, null);
      }
      iterador.siguiente();

    }

    Item[] anillos = datos.getDatosJugador().getAnillos();

    for (int i = 0; i < anillos.length; i++) {
      objetoActual = anillos[i];
      if (objetoActual != null) {
        this.escribirItemArchivo(archivo, objetoActual, null, null);

      }
    }

    return true;
  }

  private boolean guardarArbol(DatosPartida datos, FileWriter archivo) {
    ArbolPasivas arbol = datos.getArbolPasivas();
    if (arbol == null) {
      return true;
    }

    try {
      Lista<org.ayed.poe.Pasiva> pasivas = arbol.obtenerPasivas();
      Iterador<org.ayed.poe.Pasiva> iterador = pasivas.iterador();

      while (iterador.haySiguiente()) {
        org.ayed.poe.Pasiva pasiva = iterador.dato();

        if (pasiva != null && pasiva.estaAsignada()) {
          archivo.write(pasiva.getId() + "\n");
        }

        iterador.siguiente();
      }

    } catch (IOException e) {
      System.out.println("--- Error al escribir el arbol en el archivo ---");
      return false;
    }

    return true;
  }

  private boolean escribirItemArchivo(FileWriter archivo, Item item, Integer x, Integer y) {

    String linea = "";
    String lineaEstadisticas = "";
    int cantidadEstadisticasItem = 0;
    Diccionario<Estadistica, Integer> estadisticasItem = item.getEstadisticas();
    Estadistica estadisticaActual = null;
    Estadistica[] estadisticasDisponibles = Estadistica.values();

    for (int i = 0; i < estadisticasDisponibles.length; i++) {
      if (estadisticasItem.obtenerValor(estadisticasDisponibles[i]) != null) {
        estadisticaActual = estadisticasDisponibles[i];
        lineaEstadisticas += estadisticaActual + ";"
            + estadisticasItem.obtenerValor(estadisticasDisponibles[i]) + ";";
        cantidadEstadisticasItem += 1;
      }
    }

    if (x != null) {
      linea = "item;" + item.getId() + ";" + item.getNombre() + ";" + item.getAlto() + ";"
          + item.getAncho() + ";" + item.getTipo() + ";" + item.getRareza() + ";" + x + ";" + y
          + ";" + cantidadEstadisticasItem + ";";
    } else {
      linea = "item;" + item.getId() + ";" + item.getNombre() + ";" + item.getAlto() + ";"
          + item.getAncho() + ";" + item.getTipo() + ";" + item.getRareza() + ";"
          + cantidadEstadisticasItem + ";";
    }

    linea += lineaEstadisticas;
    try {
      archivo.write(linea + "\n");

    } catch (IOException e) {
      System.out.println("--- Error al escribir el archivo ---");
      return false;
    }

    return true;
  }

  private DatosJugador cargarJugador(BufferedReader br) throws IOException {

    int nivel = 0;
    int experienciaAcumulada = 0;
    int puntosHabilidad = 0;
    int experienciaRestanteProxNivel = 0;
    int monedas = 0;
    int ultimoIdAgregado = 0;

    String linea = br.readLine();

    while (linea != null && !linea.startsWith("[")) {

      String[] datos = linea.split(";");

      switch (datos[0].trim()) {
        case "nivel":
          nivel = Integer.parseInt(datos[1].trim());
          break;

        case "experienciaAcumulada":
          experienciaAcumulada = Integer.parseInt(datos[1].trim());
          break;

        case "puntosHabilidad":
          puntosHabilidad = Integer.parseInt(datos[1].trim());
          break;

        case "experienciaRestante":
          experienciaRestanteProxNivel = Integer.parseInt(datos[1].trim());
          break;

        case "monedas":
          monedas = Integer.parseInt(datos[1].trim());
          break;

        case "ultimoIdAgregado":
          ultimoIdAgregado = Integer.parseInt(datos[1].trim());
          break;

      }

      linea = br.readLine();
    }

    this.lineaPendiente = linea;

    return new DatosJugador(nivel, experienciaAcumulada, puntosHabilidad,
        experienciaRestanteProxNivel, monedas, ultimoIdAgregado);
  }

  private Inventario cargarInventario(Lista<String> lineas) {

    Inventario inventario = new Inventario(ANCHO_INVENTARIO, ALTO_INVENTARIO);

    Iterador<String> iterador = lineas.iterador();

    while (iterador.haySiguiente()) {

      String lineaActual = iterador.dato();

      if (lineaActual.startsWith("item")) {

        String[] datosItem = lineaActual.split(";");

        int id = Integer.parseInt(datosItem[1]);
        String nombre = datosItem[2];
        int alto = Integer.parseInt(datosItem[3]);
        int ancho = Integer.parseInt(datosItem[4]);

        TipoItem tipo = TipoItem.valueOf(datosItem[5]);
        RarezaItem rareza = RarezaItem.valueOf(datosItem[6]);

        int posicionX = Integer.parseInt(datosItem[7]);
        int posicionY = Integer.parseInt(datosItem[8]);

        int cantidadEstadisticas = Integer.parseInt(datosItem[9]);

        Diccionario<Estadistica, Integer> estadisticas = new Diccionario<>(Estadistica.values().length);

        int posicionActual = 10;

        for (int j = 0; j < cantidadEstadisticas; j++) {

          Estadistica estadistica = Estadistica.valueOf(datosItem[posicionActual]);

          int valor = Integer.parseInt(datosItem[posicionActual + 1]);

          estadisticas.agregar(estadistica, valor);

          posicionActual += 2;
        }

        Item item = new Item(id, ancho, alto, tipo, rareza, nombre, estadisticas);

        inventario.colocarItem(posicionX, posicionY, item);
      }

      iterador.siguiente();
    }

    return inventario;
  }

  private Lista<String> leerLineasInventario(BufferedReader br) throws IOException {

    Lista<String> lineas = new Lista<>();

    String linea = br.readLine();

    while (linea != null && !linea.startsWith("[")) {

      if (!linea.isEmpty()) {
        lineas.agregar(linea);
      }

      linea = br.readLine();
    }

    this.lineaPendiente = linea;

    return lineas;
  }

  private Diccionario<TipoItem, Item> cargarEquipo(Lista<String> lineas) {

    Diccionario<TipoItem, Item> equipo = new Diccionario<>(TipoItem.values().length);
    Iterador<String> iterador = lineas.iterador();

    while (iterador.haySiguiente()) {

      String lineaActual = iterador.dato();

      if (lineaActual.startsWith("item")) {

        Item item = cargarItemEquipo(lineaActual);

        equipo.agregar(item.getTipo(), item);
      }

      iterador.siguiente();
    }

    return equipo;
  }

  private Item cargarItemEquipo(String linea) {

    String[] datos = linea.split(";");

    int id = Integer.parseInt(datos[1]);
    String nombre = datos[2];
    int alto = Integer.parseInt(datos[3]);
    int ancho = Integer.parseInt(datos[4]);

    TipoItem tipo = TipoItem.valueOf(datos[5]);
    RarezaItem rareza = RarezaItem.valueOf(datos[6]);

    int cantidadEstadisticas = Integer.parseInt(datos[7]);

    Diccionario<Estadistica, Integer> estadisticas = new Diccionario<>(Estadistica.values().length);

    int posicionActual = 8;

    int contador = 0;

    while (contador < cantidadEstadisticas) {

      Estadistica estadistica = Estadistica.valueOf(datos[posicionActual]);

      int valor = Integer.parseInt(datos[posicionActual + 1]);

      estadisticas.agregar(estadistica, valor);

      posicionActual += 2;
      contador++;
    }

    return new Item(id, ancho, alto, tipo, rareza, nombre, estadisticas);
  }

  private void cargarArbol(Lista<String> lineas, ArbolPasivas arbol) {

    Iterador<String> iterador = lineas.iterador();

    while (iterador.haySiguiente()) {

      int idPasiva = Integer.parseInt(iterador.dato());

      arbol.obtenerPasiva(idPasiva).setAsignada(true);

      iterador.siguiente();
    }
  }

  private ArbolPasivas cargarArbolDesdeArchivo(String ruta, Jugador jugador) {
    ArbolPasivas arbol = null;
    try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
      String linea;
      boolean leyendoConexiones = false;

      while ((linea = br.readLine()) != null) {
        linea = linea.trim();
        if (linea.isEmpty() || linea.startsWith("#")) {
          continue;
        }

        if (linea.equals("[CONEXIONES]")) {
          leyendoConexiones = true;
          continue;
        }

        if (!leyendoConexiones) {
          String[] partes = linea.split(";");
          int id = Integer.parseInt(partes[0]);
          String nombre = partes[1];
          int costo = Integer.parseInt(partes[2]);

          Diccionario<Estadistica, Integer> bonifs = new Diccionario<>(Estadistica.values().length);

          if (partes.length > 3) {

            String[] bonificaciones = partes[3].split(",");

            for (String bonificacion : bonificaciones) {

              String[] datos = bonificacion.split(":");

              Estadistica estadistica = Estadistica.valueOf(datos[0]);
              int valor = Integer.parseInt(datos[1]);

              bonifs.agregar(estadistica, valor);
            }
          }

          Pasiva nueva = new Pasiva(id, nombre, costo, bonifs);
          if (arbol == null) {
            arbol = new ArbolPasivas(nueva, jugador);
          } else {
            arbol.registrarPasiva(nueva);
          }
        } else {
          String[] partes = linea.split(";");
          int idA = Integer.parseInt(partes[0]);
          int idB = Integer.parseInt(partes[1]);
          arbol.conectarPasivas(idA, idB);
        }
      }
    } catch (IOException e) {
      System.out.println("Error al cargar el arbol: " + e.getMessage());
    }
    return arbol;
  }

  private DatosEscondite cargarEscondite(BufferedReader br) throws IOException {

    String linea = br.readLine();

    int cantidadPaginas = 1;

    while (linea != null && linea.trim().isEmpty()) {
      linea = br.readLine();
    }

    if (linea.startsWith("Paginas activas")) {
      cantidadPaginas = Integer.parseInt(linea.split(";")[1]);
    }

    DatosEscondite datosEscondite = new DatosEscondite(cantidadPaginas);

    linea = br.readLine();

    while (linea != null && linea.startsWith("[PAGINA_")) {

      int numeroPagina = Integer.parseInt(linea.replace("[PAGINA_", "").replace("]", ""));

      Lista<String> lineasPagina = leerLineasInventario(br);

      Inventario pagina = cargarInventario(lineasPagina);

      datosEscondite.setPagina(numeroPagina, pagina);

      if (lineaPendiente != null) {
        linea = lineaPendiente;
        lineaPendiente = null;
      } else {
        linea = br.readLine();
      }
    }

    lineaPendiente = linea;

    return datosEscondite;
  }

}
