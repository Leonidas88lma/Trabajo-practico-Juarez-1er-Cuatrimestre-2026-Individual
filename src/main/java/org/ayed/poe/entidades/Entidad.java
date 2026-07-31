package org.ayed.poe.entidades;

import org.ayed.poe.TipoEfecto;
import org.ayed.poe.mazmorra.Mazmorra;

public abstract class Entidad {

  public static final int NO_ATACA = 0;
  public static final int ARRIBA = 1;
  public static final int DERECHA = 2;
  public static final int ABAJO = 3;
  public static final int IZQUIERDA = 4;
  protected int x, y;
  protected String simbolo;
  protected int vidaActual;
  protected int estadoIncendiado = 0;
  protected int estadoCongelado = 0;

  /**
   * Crea una nueva entidad en la posición indicada.
   *
   * @param x Coordenada horizontal inicial.
   * @param y Coordenada vertical inicial.
   * @param simbolo Símbolo utilizado para representar la entidad.
   * @param hp Vida inicial de la entidad.
   */
  public Entidad(int x, int y, String simbolo, int hp) {
    this.x = x;
    this.y = y;
    this.simbolo = simbolo;
    this.vidaActual = hp;
  }

  /**
   * Actualiza la posición de la entidad.
   *
   * @param x Nueva coordenada horizontal.
   * @param y Nueva coordenada vertical.
   */
  public void setPosicion(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Rota un patrón de ataque según la dirección indicada.
   *
   * @param patron Patrón de ataque base.
   * @param direccion Dirección hacia la cual rotar el patrón.
   * @return Una copia del patrón con las coordenadas rotadas.
   */
  public int[][] rotarAtaque(int[][] patron, int direccion) {
    int[][] patronRotado = new int[patron.length][2];
    for (int i = 0; i < patron.length; i++) {
      int x = patron[i][0];
      int y = patron[i][1];

      int nuevoX = x;
      int nuevoY = y;

      if (direccion == DERECHA) {
        nuevoX = -y;
        nuevoY = x;
      } else if (direccion == ABAJO) {
        nuevoX = -x;
        nuevoY = -y;
      } else if (direccion == IZQUIERDA) {
        nuevoX = y;
        nuevoY = -x;
      }
      patronRotado[i][0] = nuevoX;
      patronRotado[i][1] = nuevoY;
    }
    return patronRotado;
  }

  /**
   * Desplaza la entidad una casilla en la dirección indicada.
   *
   * @param posicion Dirección del movimiento.
   */
  public void moverEntidad(int posicion) {
    if (posicion == ARRIBA) {
      this.y -= 1;
    } else if (posicion == DERECHA) {
      this.x += 1;
    } else if (posicion == ABAJO) {
      this.y += 1;
    } else if (posicion == IZQUIERDA) {
      this.x -= 1;
    }
  }

  public int getVidaActual() {
    return this.vidaActual;
  }

  /**
   * Reduce la vida actual del personaje en base al dano recibido
   * 
   * @param danoRecibido La cantidad de dano a restar
   * @return La vida restante del personaje tras el ataque
   */
  public int recibirDano(int danoRecibido) {
    this.vidaActual -= danoRecibido;
    return this.vidaActual;
  }

  /**
   * Aplica el efecto ambiental indicado sobre la entidad, daño para casillas electrificadas o
   * congeladas, daño constante para las incendiadas
   *
   * Los efectos pueden causar daño directo o aplicar estados alterados.
   *
   * @param efecto Efecto ambiental a aplicar.
   */
  public void recibirDanoAmbiental(TipoEfecto efecto) {
    if (efecto == TipoEfecto.ELECTRICO) {
      recibirDano(10);
    } else if (efecto == TipoEfecto.INCENDIADO) {
      estadoIncendiado = 4;
    } else if (efecto == TipoEfecto.CONGELADO) {
      estadoCongelado = 4;
      recibirDano(3);
    }
  }

  /**
   * Indica si la entidad sigue con vida.
   *
   * @return true si su vida es mayor a cero, false en caso contrario.
   */
  public boolean estaVivo() {
    return this.vidaActual > 0;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public String getSimbolo() {
    return simbolo;
  }

  public int getEstadoIncendiado() {
    return estadoIncendiado;
  }

  public void bajarEstadoIncendiado() {
    estadoIncendiado--;
  }
  
  public int getEstadoCongelado() {
    return estadoCongelado;
  }
  
  public void bajarEstadoCongelado() {
    estadoCongelado--;
  }

  public abstract void ejecutarTurno(Mazmorra m);

}
