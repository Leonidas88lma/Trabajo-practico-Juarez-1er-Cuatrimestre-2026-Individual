package org.ayed.poe.entidades;

import org.ayed.poe.*;
import org.ayed.poe.mazmorra.Celda;
import org.ayed.poe.mazmorra.Mazmorra;
import org.ayed.poe.sistemas.ComportamientoZombie;

public class Zombie extends Entidad {

  private final int[][] patronAtaqueBase = {{0, -1}, {-1, -1}, {0, -2}, {1, -1}};
  private int danoAtaque = 20;
  private boolean alertado = false;
  private final ComportamientoZombie comportamiento;

  public Zombie(int x, int y, int nivelMazmorra) {
    super(x, y, "Z", (int) (50 * (1 + 0.01 * (nivelMazmorra - 1))));

    this.danoAtaque = (int) (10 * (1 + 0.005 * (nivelMazmorra - 1)));

    this.comportamiento = new ComportamientoZombie();
  }

  public boolean estaAlertado() {
    return alertado;
  }

  /**
   * Cambia el estado del zombie a alertado.
   */
  public void alertar() {
    alertado = true;
  }

  /**
   * Verifica si una entidad se encuentra a una determinada cantidad de casillas utilizando
   * distancia Manhattan.
   *
   * @param entidad Entidad cuya distancia se desea comprobar.
   * @param celdas Distancia máxima permitida.
   * @return true si la entidad se encuentra dentro del rango indicado.
   */
  public boolean estaAXCasillas(Entidad entidad, int celdas) {
    int distanciaX = Math.abs(this.x - entidad.getX());
    int distanciaY = Math.abs(this.y - entidad.getY());
    int distancia = distanciaX + distanciaY;
    return distancia <= celdas;
  }

  /**
   * Verifica si el zombie puede moverse en la dirección indicada, solo se utiliza cuando el zombie
   * no esta alertado.
   *
   * @param m Mazmorra donde se encuentra el zombie.
   * @param direccion Dirección hacia la que desea desplazarse.
   * @return true si el movimiento es válido, false en caso contrario.
   */
  public boolean puedeMoverse(Mazmorra m, int direccion) {

    int nuevox = this.x;
    int nuevoy = this.y;
    if (direccion == ARRIBA) {
      nuevoy--;
    } else if (direccion == DERECHA) {
      nuevox++;
    } else if (direccion == ABAJO) {
      nuevoy++;
    } else if (direccion == IZQUIERDA) {
      nuevox--;
    }
    if (!m.esCaminable(nuevox, nuevoy)) {
      return false;
    }
    if (!alertado) {
      Celda destino = m.obtenerCelda(nuevox, nuevoy);
      if (destino.tipoEfecto != TipoEfecto.NINGUNO) {
        return false;
      }
    }
    return true;
  }

  /**
   * Determina si el jugador se encuentra dentro de alguno de los patrones de ataque del zombie.
   *
   * @param p Jugador objetivo.
   * @return La dirección de ataque correspondiente o NO_ATACA si no puede atacar.
   */
  public int puedeAtacar(Jugador p) {
    if (!estaAXCasillas(p, 6)) {
      return NO_ATACA;
    }
    int j_x = p.getX();
    int j_y = p.getY();
    int direccion = ARRIBA;
    while (direccion <= IZQUIERDA) {
      int[][] patron = rotarAtaque(patronAtaqueBase, direccion);
      for (int[] ints : patron) {
        int dx = ints[0];
        int dy = ints[1];
        int objetivoX = this.x + dx;
        int objetivoY = this.y + dy;
        if (objetivoX == j_x && objetivoY == j_y) {
          return direccion;
        }
      }
      direccion++;
    }
    return NO_ATACA;
  }

  /**
   * Realiza un ataque contra el jugador si tiene vida, esto ultimo para evitar que zombies muertos
   * hagan daño.
   *
   * @param jugador Jugador que recibirá el daño.
   */
  public void atacarJugador(Jugador jugador) {
    if (this.vidaActual > 0) {
      if (this.estadoCongelado != 0) {
        jugador.recibirDano((int) (danoAtaque - (danoAtaque * 0.35)));
      } else {
        jugador.recibirDano(danoAtaque);
      }
    }
  }

  @Override
  public void ejecutarTurno(Mazmorra m) {
    comportamiento.ejecutarTurno(this, m);
  }
}
