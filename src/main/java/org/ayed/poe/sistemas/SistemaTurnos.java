package org.ayed.poe.sistemas;

import org.ayed.poe.mazmorra.Mazmorra;
import org.ayed.poe.entidades.Entidad;
import org.ayed.poe.entidades.Jugador;
import org.ayed.poe.entidades.Zombie;
import org.ayed.tda.vector.VectorDinamico;

public class SistemaTurnos {

  public void ejecutarDanioAmbientalEnTurno(Entidad entidad, Mazmorra mazmorra) {
    if (entidad.getEstadoIncendiado() > 0) {
      entidad.recibirDano(3);
      entidad.bajarEstadoIncendiado();
    }
    if (entidad.getEstadoCongelado() > 0) {
      entidad.bajarEstadoCongelado();
    }
  }

  public void ejecutarTurno(Mazmorra mazmorra) {

    VectorDinamico<Entidad> entidades = mazmorra.getEntidades();

    int i = 0;

    while (i < entidades.tamanio()) {

      Entidad entidad = entidades.obtener(i);

      if (!(entidad instanceof Jugador)) {
        entidad.ejecutarTurno(mazmorra);
      }

      i++;
    }
  }
}