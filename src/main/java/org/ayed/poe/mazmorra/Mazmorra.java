package org.ayed.poe.mazmorra;

import org.ayed.poe.TipoEfecto;
import org.ayed.poe.entidades.Entidad;
import org.ayed.poe.entidades.Jugador;
import org.ayed.poe.sistemas.SistemaEntidades;
import org.ayed.tda.grafo.Grafo;
import org.ayed.tda.vector.VectorDinamico;

public class Mazmorra {

  public Celda[][] celdas;
  private Grafo<Celda> grafo;
  public int cantidadFilas;
  public int cantidadColumnas;
  private final SistemaEntidades sistemaEntidades;


  public Celda obtenerCelda(int x, int y) {
    return celdas[y][x];
  }

  public VectorDinamico<Entidad> getEntidades() {
    return sistemaEntidades.getEntidades();
  }

  public Mazmorra(int filas, int columnas) {
    this.cantidadFilas = filas;
    this.cantidadColumnas = columnas;
    this.celdas = new Celda[filas][columnas];

    this.sistemaEntidades = new SistemaEntidades();

    inicializarCeldas();
  }

  private void inicializarCeldas() {
    int fila = 0;
    while (fila < cantidadFilas) {
      int columna = 0;
      while (columna < cantidadColumnas) {
        celdas[fila][columna] = new Celda(TipoEfecto.NINGUNO, columna, fila);
        columna++;
      }
      fila++;
    }
  }


  public boolean esCaminable(int x, int y) {
    if (x < 0 || x >= cantidadColumnas || y < 0 || y >= cantidadFilas) {
      return false;
    }
    if (celdas[y][x].esPared) {
      return false;
    }
    return obtenerEntidadEn(x, y) == null;
  }

  public void registrarEntidad(Entidad e) {
    sistemaEntidades.registrar(e);
  }

  public Jugador getJugador() {
    return sistemaEntidades.getJugador();
  }
  
  public void setJugador(Jugador jugadorNuevo) {
    sistemaEntidades.setJugador(jugadorNuevo);
  }

  public Entidad obtenerEntidadEn(int x, int y) {
    return sistemaEntidades.obtenerEn(x, y);
  }


  private int obtenerPeso(Celda celda) {
    if (celda.tipoEfecto != TipoEfecto.NINGUNO) {
      return 5;
    }
    return 4;
  }

  public void construirGrafo() {
    grafo = new Grafo<>();
    for (int i = 0; i < cantidadFilas; i++) {
      for (int j = 0; j < cantidadColumnas; j++) {
        if (!celdas[i][j].esPared) {
          grafo.agregarVertice(celdas[i][j]);
        }
      }
    }
    for (int i = 0; i < cantidadFilas; i++) {
      for (int j = 0; j < cantidadColumnas; j++) {
        if (celdas[i][j].esPared) {
          continue;
        }
        Celda actual = celdas[i][j];
        if (j + 1 < cantidadColumnas && !celdas[i][j + 1].esPared) {
          grafo.agregarArista(actual, celdas[i][j + 1], obtenerPeso(celdas[i][j + 1]));
        }
        if (i + 1 < cantidadFilas && !celdas[i + 1][j].esPared) {

          grafo.agregarArista(actual, celdas[i + 1][j], obtenerPeso(celdas[i + 1][j]));
        }
      }
    }
  }

  public Grafo<Celda> getGrafo() {
    return grafo;
  }
  
  public void eliminarMuertos() {
    sistemaEntidades.eliminarMuertos();
}

}
