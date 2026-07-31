package org.ayed.sistemaGuardado;

import java.io.IOException;
import java.io.PrintWriter;
import org.ayed.poe.ArbolPasivas;
import org.ayed.poe.entidades.Jugador;
import org.ayed.poe.mazmorra.Escondite;

public class DatosPartida {

  private DatosJugador datosJugador;
  private DatosEscondite datosEscondite;
  private int nivelActual;
  private int ultimoIdItems;
  private ArbolPasivas arbolHabilidades;

  public DatosPartida(Jugador jugador, Escondite escondite, ArbolPasivas arbol, int nivelActual,
      int ultimoIdItems) {
    this.datosJugador = new DatosJugador(jugador);
    this.datosEscondite = new DatosEscondite(escondite);
    this.nivelActual = nivelActual;
    this.ultimoIdItems = ultimoIdItems;
    this.arbolHabilidades = arbol;
  }

  public DatosPartida(DatosJugador datosJugador, DatosEscondite datosEscondite, ArbolPasivas arbol,
      int nivelActual, int ultimoIdItems) {

    this.datosJugador = datosJugador;
    this.datosEscondite = datosEscondite;
    this.arbolHabilidades = arbol;
    this.nivelActual = nivelActual;
    this.ultimoIdItems = ultimoIdItems;
  }

  /*
   * public void guardarPartidaArchivo(int numeroSlot) {
   * try (PrintWriter partida = new PrintWriter("partidasGuardadas/partida" +
   * numeroSlot + ".txt")) {
   * 
   * partida.println("[Jugador]");
   * partida.println("#DATO;VALOR");
   * partida.println("Nivel;" + this.datosJugador.getNivel());
   * partida.println("ExperienciaAcumulada;" +
   * this.datosJugador.getExperienciaAcumulada());
   * partida.println("PuntosHabilidad;" + this.datosJugador.getPuntosHabilidad());
   * partida.println(
   * "ExperienciaRestanteProximoNivel;" +
   * this.datosJugador.getExperienciaRestanteProxNivel());
   * 
   * } catch (IOException e) {
   * e.printStackTrace();
   * }
   * }
   */

  public DatosJugador getDatosJugador() {
    return this.datosJugador;
  }

  public DatosEscondite getDatosEscondite() {
    return this.datosEscondite;
  }

  public int getNivelActual() {
    return this.nivelActual;
  }

  public ArbolPasivas getArbolPasivas() {
    return this.arbolHabilidades;
  }

  public void setArbolPasivas(ArbolPasivas arbol) {
    this.arbolHabilidades = arbol;
  }

  public int getUltimoIdItems() {
    return ultimoIdItems;
  }

}
