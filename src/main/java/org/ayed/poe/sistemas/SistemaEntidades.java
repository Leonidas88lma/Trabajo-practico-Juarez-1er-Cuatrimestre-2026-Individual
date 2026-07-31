package org.ayed.poe.sistemas;

import org.ayed.poe.entidades.Entidad;
import org.ayed.poe.entidades.Jugador;
import org.ayed.tda.vector.VectorDinamico;

public class SistemaEntidades {

  private final VectorDinamico<Entidad> entidades;
  private Jugador jugadorPrincipal;

  public VectorDinamico<Entidad> getEntidades() {
    return entidades;
  }

  public SistemaEntidades() {
    this.entidades = new VectorDinamico<>();
  }

  public void registrar(Entidad e) {
    entidades.agregar(e);

    if (e instanceof Jugador) {
      jugadorPrincipal = (Jugador) e;
    }
  }

  public Jugador getJugador() {
    return jugadorPrincipal;
  }
  
  public void setJugador(Jugador jugador) {
    jugadorPrincipal = jugador;
  }

  public Entidad obtenerEn(int x, int y) {
    int i = 0;

    while (i < entidades.tamanio()) {
      Entidad e = entidades.obtener(i);

      if (e.getX() == x && e.getY() == y) {
        return e;
      }

      i++;
    }
    return null;
  }

  public void eliminarMuertos() {
    int i = 1;

    while (i < entidades.tamanio()) {
      Entidad entidad = entidades.obtener(i);

      if (entidad.getVidaActual() <= 0) {
        entidades.eliminar(i);
      } else {
        i++;
      }
    }
  }
}
