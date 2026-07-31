package org.ayed.poe;

import org.ayed.poe.entidades.Entidad;
import org.ayed.poe.entidades.Jugador;
import org.ayed.poe.entidades.Zombie;
import org.ayed.poe.inventario.TipoItem;
import org.ayed.poe.mazmorra.Escondite;
import org.ayed.poe.mazmorra.GeneradorMazmorrasAleatorias;
import org.ayed.poe.mazmorra.Mazmorra;
import org.ayed.poe.mazmorra.RenderizadorMazmorra;
import org.ayed.poe.sistemas.LectorTeclado;
import org.ayed.poe.sistemas.SistemaMovimiento;
import org.ayed.poe.sistemas.SistemaTurnos;
import org.ayed.sistemaGuardado.DatosJugador;
import org.ayed.sistemaGuardado.DatosPartida;
import org.ayed.sistemaGuardado.GestorGuardado;
import org.ayed.tda.lista.Lista;
import org.ayed.poe.sistemas.EstadosJuego;
import org.ayed.poe.sistemas.Menu;
import org.ayed.poe.sistemas.SistemaAtaqueJugador;

import org.ayed.poe.inventario.Item;
import org.ayed.poe.inventario.RarezaItem;
import org.ayed.poe.inventario.GeneradorItems;
import org.ayed.poe.inventario.Estadistica;
import org.ayed.tda.diccionario.Diccionario;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MotorJuego {

  private Mazmorra mazmorra;
  private Jugador jugador;
  private final RenderizadorMazmorra render;
  private final Menu menu;
  private Escondite escondite;
  private final GeneradorItems generadorItems;
  private EstadosJuego estadoActual;
  private EstadosJuego estadoAnterior;
  private final SistemaMovimiento sistemaMovimiento;
  private final SistemaTurnos sistemaTurnos;
  private final SistemaAtaqueJugador sistemaAtaqueJugador;
  private boolean juegoActivo;
  private LectorTeclado lector;
  private int nivelActual;
  private boolean mazmorraLimpia;
  private ArbolPasivas arbolHabilidades;
  private DatosPartida datosPartida;
  private GestorGuardado gestorPartida;
  private int ultimoIdItems = 100;

  public MotorJuego() {
    this.nivelActual = 1;
    GeneradorMazmorrasAleatorias generador = new GeneradorMazmorrasAleatorias();
    generador.generarNivel(1);

    this.mazmorra = generador.getMazmorraJugable();
    this.jugador = new Jugador(7, 14);
    this.arbolHabilidades = cargarArbolDesdeArchivo("arbol.txt", this.jugador);
    this.jugador.setArbolPasivas(this.arbolHabilidades);
    this.escondite = new Escondite();
    this.generadorItems = new GeneradorItems();
    this.sistemaMovimiento = new SistemaMovimiento();
    this.sistemaTurnos = new SistemaTurnos();
    this.sistemaAtaqueJugador = new SistemaAtaqueJugador();

    mazmorra.registrarEntidad(jugador);
    Lista<int[]> posiciones = generador.getPosicionesZombies();

    for (int i = 0; i < posiciones.tamanio(); i++) {
      int[] pos = posiciones.dato(i);
      Zombie zombie = new Zombie(pos[0], pos[1], nivelActual);
      mazmorra.registrarEntidad(zombie);
    }

    this.lector = new LectorTeclado();
    this.render = new RenderizadorMazmorra();
    this.menu = new Menu();
    this.estadoActual = EstadosJuego.MENU_PRINCIPAL;
    this.estadoAnterior = EstadosJuego.MENU_PRINCIPAL;
    this.gestorPartida = new GestorGuardado();

    this.juegoActivo = true;

  }

  public void run() {
    while (juegoActivo) {
      if (this.estadoActual == EstadosJuego.MENU_PRINCIPAL) {
        manejarMenuPrincipal();

      } else if (this.estadoActual == EstadosJuego.EN_JUEGO) {
        manejarJuego();

      } else if (this.estadoActual == EstadosJuego.EN_INVENTARIO) {
        manejarInventario();

      } else if (this.estadoActual == EstadosJuego.EN_ARBOL_HABILIDADES) {
        manejarArbolHabilidades();

      } else {
        manejarEscondite();
      }
    }
  }

  /**
   * Procesa una acción ingresada por el jugador.
   *
   * Dependiendo de la tecla presionada puede mover al jugador, abrir menús,
   * realizar ataques o
   * finalizar la partida.
   *
   * @param entrada La tecla ingresada por el usuario
   */
  private void procesarInput(char entrada) {
    if (entrada == 'X') {
      this.juegoActivo = false;
      return;
    }

    if (entrada == 'W') {
      sistemaMovimiento.moverEntidad(mazmorra, jugador, Entidad.ARRIBA);
      sistemaTurnos.ejecutarDanioAmbientalEnTurno(jugador, mazmorra);
    }
    if (entrada == 'D') {
      sistemaMovimiento.moverEntidad(mazmorra, jugador, Entidad.DERECHA);
      sistemaTurnos.ejecutarDanioAmbientalEnTurno(jugador, mazmorra);
    }
    if (entrada == 'S') {
      sistemaMovimiento.moverEntidad(mazmorra, jugador, Entidad.ABAJO);
      sistemaTurnos.ejecutarDanioAmbientalEnTurno(jugador, mazmorra);
    }
    if (entrada == 'A') {
      sistemaMovimiento.moverEntidad(mazmorra, jugador, Entidad.IZQUIERDA);
      sistemaTurnos.ejecutarDanioAmbientalEnTurno(jugador, mazmorra);
    }

    if (entrada == 'I') {
      this.estadoAnterior = EstadosJuego.EN_JUEGO;
      this.estadoActual = EstadosJuego.EN_INVENTARIO;
    }

    // PARA TESTEAR
    if (entrada == 'E') {
      this.estadoAnterior = EstadosJuego.EN_JUEGO;
      this.estadoActual = EstadosJuego.EN_ESCONDITE;
    }

    if (entrada == 'J') {
      int direccion = lector.leerDireccion("Ingrese la direccion del ataque: ");
      sistemaAtaqueJugador.atacar(jugador, mazmorra, direccion);
      sistemaTurnos.ejecutarDanioAmbientalEnTurno(jugador, mazmorra);
    }
  }

  /**
   * Muestra y gestiona el menú principal del juego.
   *
   * Permite comenzar una nueva partida, cargar una existente o salir del juego.
   */
  private void manejarMenuPrincipal() {
    char accion = this.menu.mostrarMenuPrincipal();

    if (accion == '1') {
      this.estadoAnterior = EstadosJuego.MENU_PRINCIPAL;
      this.estadoActual = EstadosJuego.EN_JUEGO;
    } else if (accion == '2') {

      String nombreArchivo = this.lector.leerTexto("Ingrese el nombre del archivo: ");
      this.datosPartida = this.gestorPartida.cargarPartida(nombreArchivo + ".txt");

      if (this.datosPartida != null) {

        Jugador jugadorNuevo = new Jugador(7, 14, this.datosPartida.getDatosJugador());

        if (mazmorra.getEntidades().obtener(0) instanceof Jugador) {
          mazmorra.getEntidades().eliminar(0);
          mazmorra.getEntidades().agregar(jugadorNuevo, 0);
          this.jugador = jugadorNuevo;
          mazmorra.setJugador(jugadorNuevo);
        } else {
          mazmorra.getEntidades().agregar(jugadorNuevo, 0);
          this.jugador = jugadorNuevo;
          mazmorra.setJugador(jugadorNuevo);
        }

        this.arbolHabilidades = new ArbolPasivas(this.datosPartida.getArbolPasivas(), this.jugador);

        this.nivelActual = this.datosPartida.getDatosJugador().getNivel();
        this.escondite = new Escondite(this.datosPartida.getDatosEscondite());
        this.ultimoIdItems = this.datosPartida.getDatosJugador().getUltimoId() + 1;

        this.generadorItems.setContadorIds(this.ultimoIdItems);

        this.jugador.setArbolPasivas(this.arbolHabilidades);

        this.estadoAnterior = EstadosJuego.MENU_PRINCIPAL;
        this.estadoActual = EstadosJuego.EN_ESCONDITE;

      } else {
        System.out.println("No se pudo cargar la partida.");
      }
    } else if (accion == '3') {
      this.juegoActivo = false;
    } else {
      System.out.println("Ingresa una opcion valida");
    }
  }

  private String obtenerEstadoEntidad(String nombre, Entidad entidad) {

    String mensaje = nombre + ": " + entidad.getVidaActual();

    if (entidad.getEstadoIncendiado() > 0) {
      mensaje += " | Incendiado por " + entidad.getEstadoIncendiado() + " turnos";
    }
    if (entidad.getEstadoCongelado() > 0) {
      mensaje += " | Congelado por " + entidad.getEstadoCongelado() + " turnos";
    }

    return mensaje;
  }

  /**
   * Ejecuta el ciclo principal de juego mientras el jugador se encuentra dentro
   * de una mazmorra.
   *
   * Renderiza el estado actual, procesa acciones del jugador, ejecuta los turnos
   * de las entidades y
   * verifica condiciones de muerte o finalización del nivel.
   */
  private void manejarJuego() {

    System.out.println();
    System.out.println(obtenerEstadoEntidad("HP", jugador) + " | Monedas: " + jugador.getMonedas());
    render.mostrarEstadoMazmorra(mazmorra);

    for (int i = 1; i < mazmorra.getEntidades().tamanio(); i++) {

      Entidad zombie = mazmorra.getEntidades().obtener(i);

      System.out.println(obtenerEstadoEntidad("HP Zombie " + i, zombie));
    }

    System.out.println();

    char accion = this.menu.mostrarMenuJuego();

    procesarInput(accion);

    if (this.estadoActual == EstadosJuego.EN_JUEGO) {
      this.sistemaTurnos.ejecutarTurno(mazmorra);
      this.lootear();
      this.mazmorra.eliminarMuertos();
      this.verificarFinDeNivel();
    }

    if (!jugador.estaVivo()) {
      System.out.println("Te moriste");
      this.juegoActivo = false;
    }
  }

  /**
   * Gestiona las acciones disponibles dentro del inventario.
   *
   * Permite consultar objetos, ver estadísticas, equipar, desequipar objetos y
   * acceder al árbol de
   * habilidades.
   */
  private void manejarInventario() {
    System.out.println();
    char accion = this.menu.mostrarMenuInventario();
    boolean exito = false;

    if (accion == '1') {
      this.jugador.verInventario();

    }

    if (accion == '2') {
      System.out.println("\n--- STATS DEL JUGADOR ---");
      System.out.println("Vida Maxima: " + this.jugador.getVidaMax());
      System.out.println("Mana Maximo: " + this.jugador.getManaMax());
      System.out.println("Dano de Ataque: " + this.jugador.getDanoAtaque());
      System.out.println("Dano de Hechizo: " + this.jugador.getDanoHechizo());
      System.out.println("Armadura: " + this.jugador.getArmadura());
      System.out.println("-------------------------\n");
    }

    if (accion == '3') {
      this.jugador.mostrarEquipo();
    }

    if (accion == '4') {
      int id = this.lector.leerEntero("Ingresa el ID del item que deseas equipar: ");
      exito = this.jugador.equiparObjetoId(id);
      if (exito) {
        System.out.println("\n*** Objeto equipado con exito ***\n");

      }

    }

    if (accion == '5') {
      System.out.println(
          "1-Armadura | 2-Botas | 3-Guantes | 4-Casco | 5-Cinturon | 6-Anillo | 7-Pendiente | 8-Arma");
      int tipo = this.lector.leerEntero("Ingresa el numero del item que deseas desequipar: ");
      this.manejarObjetosDesequipados(tipo);

    }

    if (accion == '6') {
      this.estadoActual = this.estadoAnterior;
    }
  }

  /**
   * Gestiona las acciones disponibles dentro del escondite.
   *
   * Permite almacenar objetos, retirarlos, comprar páginas de alijo y avanzar al
   * siguiente nivel
   * cuando corresponda.
   */
  private void manejarEscondite() {
    System.out.println("\n === ESCONDITE ===");
    System.out.println("Monedas: " + this.jugador.getMonedas());
    this.escondite.mostrarPaginaActual();
    System.out.println();

    this.datosPartida = new DatosPartida(jugador, escondite, arbolHabilidades, nivelActual, ultimoIdItems);
    char accion = this.menu.mostrarMenuEscondite();
    boolean exito = false;
    String nombreArchivo = "";

    if (accion == '1') {
      this.estadoAnterior = EstadosJuego.EN_ESCONDITE;
      this.estadoActual = EstadosJuego.EN_INVENTARIO;

    } else if (accion == '2') {
      this.jugador.verInventario();
      int id = this.lector.leerEntero("Ingresa el ID del item a depositar: ");
      exito = this.escondite.guardarItemEnAlijo(this.jugador, id);
      if (exito) {
        System.out.println("\n*** Item depositado con exito ***\n");
      }

    } else if (accion == '3') {
      int id = this.lector.leerEntero("Ingresa el ID del item a retirar: ");
      exito = this.escondite.retirarItemDeAlijo(this.jugador, id);
      if (exito) {
        System.out.println("\n*** Item retirado con exito ***\n");
      } else {
        System.out.println("\n*** No hay espacio o ID incorrecto ***\n");
      }

    } else if (accion == '4') {
      exito = this.escondite.retirarPaginaCompleta(this.jugador);
      if (exito) {
        System.out.println("\n*** Se completo la transferencia ***\n");
      } else {
        System.out.println("\n*** Tu mochila esta llena o la pagina esta vacia ***\n");
      }

    } else if (accion == '5') {
      this.escondite.paginaAnterior();

    } else if (accion == '6') {
      this.escondite.paginaSiguiente();

    } else if (accion == '7') {
      exito = this.escondite.comprarPagina(this.jugador);
      if (exito) {
        System.out.println("\n*** Pagina desbloqueada con exito ***\n");
      }

    } else if (accion == '8') {
      nombreArchivo = this.lector.leerTexto("Ingresa el nombre del archivo: ") + ".txt";
      exito = this.gestorPartida.guardarPartida(datosPartida, nombreArchivo);

    } else if (accion == '9') {

      if (this.mazmorraLimpia) {
        this.siguienteNivel();
      }

      this.estadoAnterior = EstadosJuego.EN_ESCONDITE;
      this.estadoActual = EstadosJuego.EN_JUEGO;
    } else if (accion == '0') {

      this.estadoActual = EstadosJuego.MENU_PRINCIPAL;

    } else if (accion == 'H') {
      this.estadoActual = EstadosJuego.EN_ARBOL_HABILIDADES;
    }
  }

  /**
   * Gestiona la interacción con el árbol de habilidades.
   *
   * Permite asignar y desasignar pasivas utilizando los puntos de habilidad
   * disponibles.
   */
  private void manejarArbolHabilidades() {
    System.out.println("\n === ARBOL DE HABILIDADES ===");
    System.out.println("Puntos disponibles: " + jugador.getPuntosHabilidad());
    char opcion = this.menu.mostrarMenuArbol();

    if (opcion == '1') {
      this.arbolHabilidades.mostrarArbolDisponible();

      int id = this.lector.leerEntero("Ingresa el ID de la pasiva (1 a 6): ");

      if (this.arbolHabilidades.asignarPasivaDirecta(id)) {
        System.out.println("\n*** Pasiva asignada con exito ***\n");
        this.jugador.calcularEstadisticas();

      } else {
        System.out.println("\n*** No se pudo asignar (requisitos o puntos insuficientes) ***\n");
      }
    } else if (opcion == '2') {
      this.arbolHabilidades.mostrarArbolDisponible();

      int id = this.lector.leerEntero("Ingresa el ID de destino: ");

      if (this.arbolHabilidades.asignarPasivaCaminoOptimo(id)) {
        System.out.println("\n*** Camino optimo adquirido con exito ***\n");
        this.jugador.calcularEstadisticas();

      } else {
        System.out.println("\n*** Puntos insuficientes o pasiva inalcanzable ***\n");
      }
    } else if (opcion == '3') {
      this.arbolHabilidades.mostrarArbolAdquirido();
      int id = this.lector.leerEntero("Ingresa el ID de la pasiva a retirar: ");

      if (this.arbolHabilidades.desasignarPasiva(id)) {
        System.out.println("\n*** Pasiva desasignada con exito ***\n");
        this.jugador.calcularEstadisticas();
        this.jugador.sumarCantidadPuntosHabilidad(this.arbolHabilidades.getCostoPasivaPorId(id));

      } else {
        System.out.println("\n*** No podes retirar esta pasiva (dejaria ramas flotando) ***\n");
      }
    } else if (opcion == '4') {
      this.estadoActual = EstadosJuego.EN_ESCONDITE;
    }
  }

  /**
   * Desequipa un objeto equipado según el tipo indicado.
   *
   * También permite seleccionar cuál de los anillos equipados desea retirarse.
   *
   * @param tipo Código numérico del tipo de objeto a desequipar
   */
  private void manejarObjetosDesequipados(int tipo) {
    TipoItem tipoObjeto = null;
    boolean exito = false;
    boolean armaDesequipada = false;

    if (tipo == 1) {
      tipoObjeto = TipoItem.ARMADURA;
    } else if (tipo == 2) {
      tipoObjeto = TipoItem.BOTAS;
    } else if (tipo == 3) {
      tipoObjeto = TipoItem.GUANTES;
    } else if (tipo == 4) {
      tipoObjeto = TipoItem.CASCO;
    } else if (tipo == 5) {
      tipoObjeto = TipoItem.CINTURON;
    } else if (tipo == 7) {
      tipoObjeto = TipoItem.PENDIENTE;

    } else if (tipo == 6) {
      int posAnillo = this.lector.leerEntero("elegi el anillo que queres desequipar (1 o 2): ");
      exito = this.jugador.desequiparAnilloPorPosicion(posAnillo);
      if (exito) {
        System.out.println("\n*** Anillo desequipado con exito ***\n");

      }
      return;

    } else if (tipo == 8) {
      armaDesequipada = this.jugador.desequiparObjetoPorTipo(TipoItem.ARMA_ARCO);

      if (armaDesequipada) {
        System.out.println("\n*** Arma desequipada con exito ***\n");

      }
      return;

    } else {
      System.out.println("Opcion invalida");
      return;
    }

    if (tipoObjeto != null) {
      exito = this.jugador.desequiparObjetoPorTipo(tipoObjeto);
      if (exito) {
        System.out.println("\n*** Objeto desequipado con exito ***\n");

      }
    }
  }

  /**
   * Recorre las entidades derrotadas de la mazmorra y genera las recompensas
   * correspondientes.
   *
   * Otorga monedas al jugador e intenta añadir al inventario el objeto generado
   * como botín.
   */
  private void lootear() {
    int monedasDrop = 25;
    for (int i = 1; i < mazmorra.getEntidades().tamanio(); i++) {
      Entidad e = mazmorra.getEntidades().obtener(i);

      if (e instanceof Zombie && !e.estaVivo()) {
        this.jugador.sumarMonedas(monedasDrop);
        System.out.println("\n[!] Mataste a un Zombie");

        this.ultimoIdItems = this.generadorItems.getUltimoId();
        Item loot = this.generadorItems.generarItemAleatorio();

        if (this.jugador.guardarItemInventario(loot)) {
          System.out.println("[!] El Zombie solto: " + loot.getNombre());

        } else {
          System.out.println("[!] El Zombie solto un item, pero tu inventario esta lleno");
        }
      }
    }
  }

  /**
   * Verifica si todos los enemigos de la mazmorra han sido eliminados.
   *
   * En caso afirmativo, marca el nivel como completado y traslada al jugador al
   * escondite.
   */
  private void verificarFinDeNivel() {
    if (this.mazmorra.getEntidades().tamanio() == 1) {
      System.out.println("\n*** Mazmorra limpia ***\n");
      System.out.println("\n*** Viajando al escondite ***\n");

      this.jugador.subirNivel();
      this.nivelActual += 1;
      this.jugador.recargarVida();

      this.mazmorraLimpia = true;
      this.estadoAnterior = EstadosJuego.EN_JUEGO;
      this.estadoActual = EstadosJuego.EN_ESCONDITE;
    }
  }

  /**
   * Genera y prepara el siguiente nivel.
   *
   * Crea una nueva mazmorra, reposiciona al jugador y genera los enemigos
   * correspondientes al nuevo
   * nivel.
   */
  private void siguienteNivel() {
    System.out.println("----> Nivel " + this.nivelActual + " <----");

    GeneradorMazmorrasAleatorias generador = new GeneradorMazmorrasAleatorias();
    generador.generarNivel(this.nivelActual);

    this.mazmorra = generador.getMazmorraJugable();

    this.jugador.setPosicion(7, 14);

    this.mazmorra.registrarEntidad(this.jugador);

    Lista<int[]> posiciones = generador.getPosicionesZombies();

    int[] pos = null;
    Zombie zombie = null;

    for (int i = 0; i < posiciones.tamanio(); i++) {
      pos = posiciones.dato(i);
      zombie = new Zombie(pos[0], pos[1], nivelActual);
      this.mazmorra.registrarEntidad(zombie);
    }

    this.mazmorraLimpia = false;

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
}
