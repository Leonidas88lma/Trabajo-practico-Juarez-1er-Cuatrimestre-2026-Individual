package org.ayed.poe.mazmorra;

import org.ayed.tda.grafo.Heuristica;

public class HeuristicaCelda implements Heuristica<Celda> {

  @Override
  public int calcularPuntaje(Celda origen, Celda destino) {

    return Math.abs(origen.getX() - destino.getX()) + Math.abs(origen.getY() - destino.getY());
  }
}
