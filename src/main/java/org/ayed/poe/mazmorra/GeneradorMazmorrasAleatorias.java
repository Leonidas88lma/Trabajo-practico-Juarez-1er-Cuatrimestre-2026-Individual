package org.ayed.poe.mazmorra;

import org.ayed.poe.TipoEfecto;
import org.ayed.tda.lista.Lista;
import org.ayed.tda.matriz.Matriz;

public class GeneradorMazmorrasAleatorias {

  private static final int[][] PLANTILLA_1 =
      {{2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1}, {7, 3}, {8, 3}, {7, 4}, {8, 4}, {10, 6}, {11, 6},
          {12, 6}, {10, 7}, {11, 7}, {12, 7}, {4, 9}, {1, 12}};

  private static final int[][] PLANTILLA_2 =
      {{4, 1}, {4, 2}, {4, 3}, {4, 4}, {4, 5}, {8, 7}, {9, 7}, {10, 7}, {11, 7}, {12, 7}, {2, 10},
          {3, 10}, {4, 10}, {2, 11}, {3, 11}, {4, 11}, {12, 12}, {1, 8}};

  private static final int[][] PLANTILLA_3 =
      {{2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1}, {6, 4}, {7, 4}, {8, 4}, {6, 5}, {7, 5}, {8, 5},
          {10, 7}, {11, 7}, {10, 8}, {11, 8}, {1, 10}, {13, 12}};

  private static final int[][] PLANTILLA_4 =
      {{4, 1}, {5, 1}, {6, 1}, {7, 1}, {8, 1}, {11, 4}, {12, 4}, {13, 4}, {11, 5}, {12, 5}, {13, 5},
          {2, 7}, {3, 7}, {2, 8}, {3, 8}, {7, 10}, {5, 12}};

  private static final int[][] PLANTILLA_5 =
      {{2, 1}, {3, 1}, {4, 1}, {2, 2}, {3, 2}, {4, 2}, {7, 4}, {8, 4}, {9, 4}, {10, 4}, {11, 4},
          {10, 8}, {11, 8}, {10, 9}, {11, 9}, {4, 11}, {12, 12}, {1, 9}};

  private static final int[][][] PLANTILLAS_PAREDES =
      {PLANTILLA_1, PLANTILLA_2, PLANTILLA_3, PLANTILLA_4, PLANTILLA_5};

  public static final int SUELO = 0;
  public static final int PARED = 1;
  public static final int ELECTRICO = 2;
  public static final int INCENDIADO = 3;
  public static final int CONGELADO = 4;

  private Matriz<Integer> mapa;
  private Lista<int[]> posicionesZombies;
  private Mazmorra mazmorraJugable;

  public Mazmorra getMazmorraJugable() {
    return mazmorraJugable;
  }

  public Lista<int[]> getPosicionesZombies() {
    return posicionesZombies;
  }

  public void generarNivel(int nivel) {

    mapa = new Matriz<>(15, 15, SUELO);
    generarParedes(mapa);
    generarEspeciales(mapa);
    generarMapaJugable(mapa);
    generarZombies(nivel);

  }

  private void generarMapaJugable(Matriz<Integer> mapa) {
    Mazmorra mazmorra = new Mazmorra(mapa.filas(), mapa.columnas());
    for (int i = 0; i < mapa.filas(); i++) {
      for (int j = 0; j < mapa.columnas(); j++) {
        if (mapa.elemento(i, j) == PARED) {
          mazmorra.celdas[i][j].esPared = true;
        } else if (mapa.elemento(i, j) == ELECTRICO) {
          mazmorra.celdas[i][j].tipoEfecto = TipoEfecto.ELECTRICO;
        } else if (mapa.elemento(i, j) == INCENDIADO) {
          mazmorra.celdas[i][j].tipoEfecto = TipoEfecto.INCENDIADO;
        } else if (mapa.elemento(i, j) == CONGELADO) {
          mazmorra.celdas[i][j].tipoEfecto = TipoEfecto.CONGELADO;
        }
      }
    }

    mazmorra.construirGrafo();
    mazmorraJugable = mazmorra;
  }

  private void generarParedes(Matriz<Integer> mapa) {

    int numeroPlantilla = (int) (Math.random() * PLANTILLAS_PAREDES.length);
    int[][] paredes = PLANTILLAS_PAREDES[numeroPlantilla];

    for (int i = 0; i < paredes.length; i++) {

      int x = paredes[i][0];
      int y = paredes[i][1];

      mapa.asignar(y, x, PARED);
    }
  }

  private void generarEspeciales(Matriz<Integer> mapa) {
    int charcosAgregados = 0;
    while (charcosAgregados < 4) {
      int tipoAAgregar = ((int) (Math.random() * 3)) + 2;
      boolean colocado = false;
      while (!colocado) {
        int x = (int) (Math.random() * 15);
        int y = (int) (Math.random() * 15);
        if (puedeAgregarse(mapa, x, y)) {
          int forma = (int) (Math.random() * 10);

          if (forma == 0) {
            mapa.asignar(y + 1, x + 1, tipoAAgregar);
            mapa.asignar(y + 2, x + 1, tipoAAgregar);

          } else if (forma == 1) {
            mapa.asignar(y + 1, x + 1, tipoAAgregar);
            mapa.asignar(y + 1, x + 2, tipoAAgregar);

          } else if (forma == 2) {
            mapa.asignar(y + 1, x + 1, tipoAAgregar);
            mapa.asignar(y + 1, x + 2, tipoAAgregar);
            mapa.asignar(y + 2, x + 1, tipoAAgregar);

          } else if (forma == 3) {
            mapa.asignar(y + 1, x + 1, tipoAAgregar);
            mapa.asignar(y + 1, x + 2, tipoAAgregar);
            mapa.asignar(y + 2, x + 2, tipoAAgregar);
          } else {
            mapa.asignar(y + 1, x + 1, tipoAAgregar);
            mapa.asignar(y + 1, x + 2, tipoAAgregar);
            mapa.asignar(y + 2, x + 1, tipoAAgregar);
            mapa.asignar(y + 2, x + 2, tipoAAgregar);
          }
          colocado = true;
          charcosAgregados++;
        }
      }
    }
  }

  public boolean puedeAgregarse(Matriz<Integer> mapa, int x, int y) {
    if ((x + 3 >= mapa.columnas()) || (y + 3 >= mapa.filas())) {
      return false;
    }
    for (int i = x; i <= x + 3; i++) {
      for (int j = y; j <= y + 3; j++) {
        if (mapa.elemento(j, i) != SUELO) {
          return false;
        }
      }
    }
    return true;
  }

  private void generarZombies(int nivel) {
    int cantidad;
    if (nivel <= 2) {
      cantidad = 2;
    } else if (nivel <= 4) {
      cantidad = 3;
    } else {
      cantidad = 4;
    }

    posicionesZombies = new Lista<>();

    while (posicionesZombies.tamanio() < cantidad) {
      int x = (int) (Math.random() * mapa.filas());
      int y = (int) (Math.random() * mapa.columnas());
      boolean posicionValida = !(x == 7 && y == 14) && mapa.elemento(y, x) == SUELO;
      if (posicionValida) {
        boolean repetida = false;
        int i = 0;

        while (i < posicionesZombies.tamanio() && !repetida) {

          int[] pos = posicionesZombies.dato(i);

          if (pos[0] == x && pos[1] == y) {
            repetida = true;
          }

          i++;
        }
        if (!repetida) {
          posicionesZombies.agregar(new int[] {x, y});
        }
      }
    }
  }

}
