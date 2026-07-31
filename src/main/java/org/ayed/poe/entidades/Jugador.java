package org.ayed.poe.entidades;

import org.ayed.poe.mazmorra.Mazmorra;
import org.ayed.sistemaGuardado.DatosJugador;
import org.ayed.poe.ArbolPasivas;
import org.ayed.poe.Pasiva;
import org.ayed.poe.excepciones.InventarioLlenoException;
import org.ayed.poe.excepciones.ItemNoEncontradoException;
import org.ayed.poe.inventario.Estadistica;
import org.ayed.poe.inventario.Inventario;
import org.ayed.poe.inventario.Item;
import org.ayed.poe.inventario.TipoItem;
import org.ayed.tda.diccionario.Diccionario;
import org.ayed.tda.iterador.Iterador;
import org.ayed.tda.lista.Lista;

public class Jugador extends Entidad {

  private final int VIDA_BASE = 100;
  private final int MANA_BASE = 100;
  private final int DANO_ATAQUE_BASE = 30; // testeando
  private final int DANO_HECHIZO_BASE = 20;
  private final int ARMADURA_BASE = 0;
  private final int NIVEL_BASE = 1;
  private final int PUNTOS_HABILIDAD_BASE = 0;
  private final int EXPERIENCIA_ACUMULADA_BASE = 0;
  private final int EXPERIENCIA_RESTANTE_PROX_NIVEL = 100;
  private final int CANTIDAD_MONEDAS_BASE = 0;

  private int manaActual;
  private int danoAtaque;
  private int danoHechizo;
  private int armadura;
  private int nivel;
  private int experienciaAcumulada;
  private int puntosHabilidad;
  private int experienciaRestanteProxNivel;
  private Inventario inventario;
  private Equipamiento equipamiento;
  private int vidaMax;
  private int manaMax;
  private ArbolPasivas arbolPasivas;
  private int monedas;

  /**
   * Crea un jugador con estadísticas base y estructuras inicializadas.
   * 
   * Pre:
   * - x e y representan una posición válida dentro del mapa.
   * 
   * Post:
   * - El jugador queda creado con estadísticas base.
   * - Se inicializa un inventario vacío.
   * - No posee equipamiento ni monedas.
   */

  public Jugador(int x, int y) {
    super(x, y, "[P]", 200);
    this.manaActual = MANA_BASE;
    this.vidaActual = VIDA_BASE;
    this.danoAtaque = DANO_ATAQUE_BASE;
    this.danoHechizo = DANO_HECHIZO_BASE;
    this.armadura = ARMADURA_BASE;
    this.nivel = NIVEL_BASE;
    this.experienciaAcumulada = EXPERIENCIA_ACUMULADA_BASE;
    this.puntosHabilidad = PUNTOS_HABILIDAD_BASE;
    this.experienciaRestanteProxNivel = EXPERIENCIA_RESTANTE_PROX_NIVEL;
    this.equipamiento = new Equipamiento();
    this.monedas = CANTIDAD_MONEDAS_BASE;
    this.inventario = new Inventario(10, 10);
  }

  public Jugador(int x, int y, DatosJugador datos) {
    this(x, y);

    this.nivel = datos.getNivel();
    this.experienciaAcumulada = datos.getExperienciaAcumulada();
    this.puntosHabilidad = datos.getPuntosHabilidad();
    this.experienciaRestanteProxNivel = datos.getExperienciaRestanteProxNivel();

    this.inventario = new Inventario(datos.getInventario());
    this.equipamiento = new Equipamiento(datos.getEquipo(), datos.getAnillos());
    this.monedas = datos.getMonedas();

    this.calcularEstadisticas();
  }

  public int getVidaMax() {
    return this.vidaMax;
  }

  public int getManaMax() {
    return this.manaMax;
  }

  public int getArmadura() {
    return this.armadura;
  }

  public int getDanoAtaque() {
    return this.danoAtaque;
  }

  public int getDanoHechizo() {
    return this.danoHechizo;
  }

  public int getNivel() {
    return this.nivel;
  }

  public int getPuntosHabilidad() {
    return this.puntosHabilidad;
  }

  public int getExperienciaAcumulada() {
    return this.experienciaAcumulada;
  }

  public int getExperienciaRestanteProxNivel() {
    return this.experienciaRestanteProxNivel;
  }

  public int getMonedas() {
    return this.monedas;
  }

  public Inventario getInventario() {
    return this.inventario;
  }

  public Diccionario<TipoItem, Item> getEquipo() {
    return this.equipamiento.getEquipo();
  }

  public Item[] getAnillos() {
    return this.equipamiento.getAnillos();
  }

  public void sumarPuntosHabilidad() {
    this.puntosHabilidad += 1;
  }

  public void sumarCantidadPuntosHabilidad(int cantidad) {
    this.puntosHabilidad += cantidad;
  }

  public void recargarVida() {
    this.vidaActual = this.vidaMax;
  }

  /**
   * Intenta descontar monedas del jugador.
   * 
   * Pre:
   * - cantidad >= 0.
   * 
   * Post:
   * - Si el jugador posee suficientes monedas, se descuentan.
   * - En caso contrario, el estado del jugador no cambia.
   */

  public boolean restarMonedas(int cantidad) {
    int resultado = this.monedas - cantidad;

    if (resultado >= 0) {
      this.monedas = resultado;
      return true;
    }

    return false;
  }

  /**
   * Agrega monedas al jugador.
   * 
   * Pre:
   * - cantidad > 0.
   * 
   * Post:
   * - Si la cantidad es positiva, las monedas aumentan.
   * - En caso contrario, el estado del jugador no cambia.
   */

  public boolean sumarMonedas(int cantidad) {
    if (cantidad > 0) {
      this.monedas += cantidad;
      return true;
    }

    return false;
  }

  public void verInventario() {
    this.inventario.mostrarInventario();
  }

  public void mostrarEquipo() {
    this.equipamiento.mostrarEquipo();
  }

  /**
   * Equipa un objeto en el espacio correspondiente.
   * 
   * Pre:
   * - objeto != null.
   * 
   * Post:
   * - Si el objeto puede equiparse, pasa a formar parte del equipo.
   * - Las estadísticas del jugador son recalculadas.
   */

  public boolean equiparObjeto(Item objeto) {

    boolean equipado = this.equipamiento.equipar(objeto, this.inventario);

    if (equipado) {
      this.calcularEstadisticas();
    }

    return equipado;
  }

  /**
   * Intenta almacenar un item en el inventario.
   * 
   * Pre:
   * - item != null.
   * 
   * Post:
   * - Si existe espacio disponible, el item queda almacenado.
   */

  public boolean guardarItemInventario(Item item) {
    try {
      this.inventario.colocarItem(item);
      return true;

    } catch (InventarioLlenoException e) {
      return false;
    }

  }

  /**
   * Busca un item por ID dentro del inventario y lo equipa.
   * 
   * Pre:
   * - Debe existir un item con dicho ID en el inventario.
   * 
   * Post:
   * - Si el item pudo equiparse, deja de estar en el inventario.
   * - Si no pudo equiparse, permanece almacenado.
   */

  public boolean equiparObjetoId(int id) {
    Item itemAEquipar = null;
    boolean equipado = false;

    try {
      itemAEquipar = this.inventario.eliminarItem(id);

    } catch (ItemNoEncontradoException e) {
      System.out.println("No existe un item con ese ID");
      return false;
    }

    if (itemAEquipar != null) {
      equipado = this.equiparObjeto(itemAEquipar);
      if (!equipado) {
        this.guardarItemInventario(itemAEquipar);
      }

    }

    return equipado;
  }

  /**
   * Desequipa un anillo según la posición indicada.
   * 
   * Pre:
   * - pos debe ser 1 o 2.
   * 
   * Post:
   * - Si existe un anillo en dicha posición y hay espacio,
   * el anillo pasa al inventario.
   * - Se recalculan las estadísticas.
   */

  public boolean desequiparAnilloPorPosicion(int pos) {

    if (pos <= 0 || pos > 2) {
      System.out.println("Posicion invalida");
      return false;
    }

    boolean exito = this.equipamiento.desequiparAnillo(pos - 1, this.inventario);

    if (exito) {
      this.calcularEstadisticas();
    }

    return exito;
  }

  /**
   * Desequipa un objeto del tipo indicado.
   * 
   * Pre:
   * - tipo != null.
   * 
   * Post:
   * - Si existe un objeto equipado de ese tipo y hay espacio,
   * pasa al inventario.
   * - Las estadísticas son recalculadas.
   */

  public boolean desequiparObjetoPorTipo(TipoItem tipo) {

    boolean exito;

    if (this.equipamiento.esTipoArma(tipo)) {

      exito = this.equipamiento.desequiparArma(this.inventario);

    } else {

      exito = this.equipamiento.desequipar(tipo, this.inventario);
    }

    if (exito) {
      this.calcularEstadisticas();
    }

    return exito;
  }

  /**
   * Recalcula todas las estadísticas del jugador.
   * 
   * Pre:
   * - El jugador debe encontrarse correctamente inicializado.
   * 
   * Post:
   * - vidaMax, manaMax, armadura, daño de ataque y daño de hechizo
   * reflejan las bonificaciones del equipamiento y las pasivas.
   * - vidaActual y manaActual nunca superan sus máximos.
   */

  public void calcularEstadisticas() {

    int viejaVidaMax = this.vidaMax;
    int viejoManaMax = this.manaMax;

    this.vidaMax = VIDA_BASE;
    this.manaMax = MANA_BASE;
    this.armadura = ARMADURA_BASE;
    this.danoAtaque = DANO_ATAQUE_BASE;
    this.danoHechizo = DANO_HECHIZO_BASE;

    Lista<Item> itemsEquipados = this.equipamiento.obtenerTodosLosItemsEquipados();
    Iterador<Item> iterador = itemsEquipados.iterador();
    Diccionario<Estadistica, Integer> estadisticasItemActual = null;
    Item itemActual = null;

    while (iterador.haySiguiente()) {
      itemActual = iterador.dato();
      estadisticasItemActual = itemActual.getEstadisticas();
      this.actualizarEstadisticas(estadisticasItemActual);
      iterador.siguiente();
    }

    if (this.arbolPasivas != null) {
      Lista<Pasiva> pasivas = arbolPasivas.obtenerPasivas();
      Iterador<Pasiva> it = pasivas.iterador();

      while (it.haySiguiente()) {
        Pasiva pasiva = it.dato();

        if (pasiva.estaAsignada()) {
          actualizarEstadisticas(pasiva.getBonificaciones());
        }

        it.siguiente();
      }
    }

    this.vidaActual += (this.vidaMax - viejaVidaMax);
    this.manaActual += (this.manaMax - viejoManaMax);

    if (this.vidaActual > this.vidaMax) {
      this.vidaActual = this.vidaMax;
    }

    if (this.manaActual > this.manaMax) {
      this.manaActual = this.manaMax;
    }

    if (this.vidaActual < 1) {
      this.vidaActual = 1;
    }

    if (this.manaActual < 0) {
      this.manaActual = 0;
    }

  }

  /**
   * Recorre las estadísticas recibidas y las aplica al personaje.
   * 
   * Pre:
   * - estadisticasItemActual != null.
   * 
   * Post:
   * - Las estadísticas del personaje fueron actualizadas según los
   * valores contenidos en el diccionario recibido.
   */

  public void actualizarEstadisticas(Diccionario<Estadistica, Integer> estadisticasItemActual) {
    Estadistica[] estadisticas = Estadistica.values();
    Integer valorActual = null;

    for (int i = 0; i < estadisticas.length; i++) {
      valorActual = estadisticasItemActual.obtenerValor(estadisticas[i]);
      if (valorActual != null) {
        this.actualizarLaEstadistica(estadisticas[i], valorActual);
      }
    }
  }

  /**
   * Actualiza una estadística específica del personaje.
   * 
   * Pre:
   * - estadistica != null.
   * 
   * Post:
   * - La estadística indicada fue incrementada en el valor recibido.
   */

  private void actualizarLaEstadistica(Estadistica estadistica, int valor) {

    if (estadistica == Estadistica.ARMADURA) {
      this.armadura += valor;

    } else if (estadistica == Estadistica.DANO_ATAQUE) {
      this.danoAtaque += valor;

    } else if (estadistica == Estadistica.DANO_HECHIZO) {
      this.danoHechizo += valor;

    } else if (estadistica == Estadistica.MANA) {
      this.manaMax += valor;

    } else if (estadistica == Estadistica.VIDA) {
      this.vidaMax += valor;
    }
  }

  public void subirNivel() {
    this.nivel += 1;
    this.puntosHabilidad += 1;
  }

  public void reducirPuntosDeHabilidad(int puntos) {
    this.puntosHabilidad -= puntos;
  }

  public void setArbolPasivas(ArbolPasivas arbolPasivas) {
    this.arbolPasivas = arbolPasivas;
    this.calcularEstadisticas();
  }

  public ArbolPasivas getArbolPasivas() {
    return this.arbolPasivas;
  }

  public void ejecutarTurno(Mazmorra m) {
  }
}
