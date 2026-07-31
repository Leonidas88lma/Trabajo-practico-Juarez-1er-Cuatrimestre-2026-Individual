package org.ayed.sistemaGuardado;

import org.ayed.poe.ArbolPasivas;
import org.ayed.poe.entidades.Jugador;
import org.ayed.poe.inventario.Inventario;
import org.ayed.poe.inventario.Item;
import org.ayed.poe.inventario.TipoItem;
import org.ayed.tda.diccionario.Diccionario;

public class DatosJugador {

  private int nivel;
  private int experienciaAcumulada;
  private int puntosHabilidad;
  private int experienciaRestanteProxNivel;
  private Inventario inventario;
  private ArbolPasivas arbolPasivas;
  private int ultimoIdAgregado;

  private Diccionario<TipoItem, Item> equipo;
  private Item[] anillosEquipados;
  private int monedas;

  public DatosJugador(Jugador jugador) {

    this.nivel = jugador.getNivel();
    this.experienciaAcumulada = jugador.getExperienciaAcumulada();
    this.puntosHabilidad = jugador.getPuntosHabilidad();
    this.experienciaRestanteProxNivel = jugador.getExperienciaRestanteProxNivel();
    this.inventario = new Inventario(jugador.getInventario());
    this.equipo = new Diccionario<>(jugador.getEquipo());
    this.arbolPasivas = new ArbolPasivas(jugador.getArbolPasivas(), jugador);
    this.anillosEquipados = new Item[2];
    Item[] anillos = jugador.getAnillos();
    for (int i = 0; i < anillos.length; i++) {
      this.anillosEquipados[i] = anillos[i];
    }

    this.monedas = jugador.getMonedas();
    this.ultimoIdAgregado = 0;

  }

  public DatosJugador(
      int nivel,
      int experienciaAcumulada,
      int puntosHabilidad,
      int experienciaRestanteProxNivel,
      int monedas,
      int ultimoIdAgregado) {

    this.nivel = nivel;
    this.experienciaAcumulada = experienciaAcumulada;
    this.puntosHabilidad = puntosHabilidad;
    this.experienciaRestanteProxNivel = experienciaRestanteProxNivel;
    this.monedas = monedas;
    this.ultimoIdAgregado = ultimoIdAgregado;

    this.inventario = null;
    this.arbolPasivas = null;
    this.equipo = new Diccionario<>(TipoItem.values().length);
    this.anillosEquipados = new Item[2];
  }

  public int getNivel() {
    return this.nivel;
  }

  public int getUltimoId() {
    return this.ultimoIdAgregado;
  }

  public int getExperienciaAcumulada() {
    return this.experienciaAcumulada;
  }

  public int getPuntosHabilidad() {
    return this.puntosHabilidad;
  }

  public int getExperienciaRestanteProxNivel() {
    return this.experienciaRestanteProxNivel;
  }

  public Inventario getInventario() {
    return this.inventario;
  }

  public Diccionario<TipoItem, Item> getEquipo() {
    return this.equipo;
  }

  public Item[] getAnillos() {
    return this.anillosEquipados;
  }

  public int getMonedas() {
    return this.monedas;
  }

  public void setInventario(Inventario inventario) {
    this.inventario = inventario;
  }

  public void setEquipo(Diccionario<TipoItem, Item> equipo) {
    this.equipo = equipo;
  }

  public ArbolPasivas getArbolPasivas() {
    return this.arbolPasivas;
  }

}
