package org.ayed.poe.mazmorra;

import org.ayed.poe.TipoEfecto;
import org.ayed.poe.entidades.Entidad;
import org.ayed.poe.entidades.Jugador;
import org.ayed.poe.entidades.Zombie;

public class RenderizadorMazmorra {

  public static final char SIMBOLO_JUGADOR = 'P';
  public static final char SIMBOLO_ZOMBIE = 'Z';
  public static final char SIMBOLO_PARED = '#';
  public static final char SIMBOLO_ITEM = '*';
  public static final char SIMBOLO_ELECTRICO = 'E';
  public static final char SIMBOLO_CONGELADO = 'C';
  public static final char SIMBOLO_INCENDIADO = 'I';
  public static final char SIMBOLO_VACIO = '.';


  public static final String COLOR_RESET = "\u001B[0m";
  public static final String COLOR_ROJO = "\u001B[31m";
  public static final String COLOR_VERDE = "\u001B[32m";
  public static final String COLOR_AMARILLO = "\u001B[33m";
  public static final String COLOR_AZUL = "\u001B[34m";

  /**
   * Muestra por consola el estado completo de la mazmorra.
   * 
   * @param mazmorra Mazmorra a representar.
   */
  public void mostrarEstadoMazmorra(Mazmorra mazmorra) {
    int fila = 0;

    while (fila < mazmorra.cantidadFilas) {
      imprimirFilaMazmorra(mazmorra, mazmorra.celdas[fila], mazmorra.cantidadColumnas);
      fila++;
    }
  }

  /**
   * Imprime una fila de la mazmorra.
   *
   * @param filaCeldas Array de celdas de la fila.
   * @param cantidadColumnas Cantidad de columnas.
   */
  private void imprimirFilaMazmorra(Mazmorra mazmorra, Celda[] filaCeldas, int cantidadColumnas) {
    int columna = 0;
    while (columna < cantidadColumnas) {
      Celda celda = filaCeldas[columna];
      char simboloCelda = obtenerSimboloCelda(celda, mazmorra);
      String color = obtenerColorCelda(celda);
      System.out.print(color + simboloCelda + COLOR_RESET + " ");
      columna++;
    }
    System.out.println();
  }

  /**
   * Determina el símbolo visual de una celda según su contenido.
   *
   * @param celda Celda a evaluar.
   * @return Símbolo representativo.
   */
  private char obtenerSimboloCelda(Celda celda, Mazmorra mazmorra) {
    Entidad e = mazmorra.obtenerEntidadEn(celda.getX(), celda.getY());
    if (e instanceof Jugador) {
      return SIMBOLO_JUGADOR;
    }
    if (e instanceof Zombie) {
      return SIMBOLO_ZOMBIE;
    }
    if (celda.esPared) {
      return SIMBOLO_PARED;
    }
    if (celda.tieneItem) {
      return SIMBOLO_ITEM;
    }
    if (celda.tipoEfecto == TipoEfecto.ELECTRICO) {
      return SIMBOLO_ELECTRICO;
    }
    if (celda.tipoEfecto == TipoEfecto.CONGELADO) {
      return SIMBOLO_CONGELADO;
    }
    if (celda.tipoEfecto == TipoEfecto.INCENDIADO) {
      return SIMBOLO_INCENDIADO;
    }

    return SIMBOLO_VACIO;
  }

  private String obtenerColorCelda(Celda celda) {


    if (celda.tipoEfecto == TipoEfecto.INCENDIADO) {
      return COLOR_ROJO;
    }

    if (celda.tipoEfecto == TipoEfecto.CONGELADO) {
      return COLOR_AZUL;
    }

    if (celda.tipoEfecto == TipoEfecto.ELECTRICO) {
      return COLOR_AMARILLO;
    }

    return COLOR_RESET;
  }
}
