package org.ayed.poe.sistemas;

import org.ayed.poe.mazmorra.Mazmorra;
import org.ayed.poe.TipoEfecto;
import org.ayed.poe.entidades.Entidad;

public class SistemaMovimiento {

  public boolean moverEntidad(Mazmorra mazmorra, Entidad entidad, int direccion) {

    int nuevoX = entidad.getX();
    int nuevoY = entidad.getY();

    if (direccion == Entidad.ARRIBA) {
      nuevoY--;
    } else if (direccion == Entidad.DERECHA) {
      nuevoX++;
    } else if (direccion == Entidad.ABAJO) {
      nuevoY++;
    } else if (direccion == Entidad.IZQUIERDA) {
      nuevoX--;
    }

    TipoEfecto efectoProximaCasilla = mazmorra.obtenerCelda(nuevoX, nuevoY).getTipoEfecto();
    if (efectoProximaCasilla != TipoEfecto.NINGUNO) {
      entidad.recibirDanoAmbiental(efectoProximaCasilla);
    }

    if (!mazmorra.esCaminable(nuevoX, nuevoY)) {
      return false;
    }

    entidad.moverEntidad(direccion);

    return true;
  }
}
