package org.ayed.poe.sistemas;

import org.ayed.poe.mazmorra.Celda;
import org.ayed.poe.mazmorra.HeuristicaCelda;
import org.ayed.poe.mazmorra.Mazmorra;
import org.ayed.poe.entidades.Entidad;
import org.ayed.poe.entidades.Jugador;
import org.ayed.poe.entidades.Zombie;
import org.ayed.tda.lista.Lista;

public class ComportamientoZombie {

  SistemaMovimiento sistemaMovimiento = new SistemaMovimiento();
  SistemaTurnos sistemaTurnos = new SistemaTurnos();

  /**
   * Ejecuta el comportamiento correspondiente al turno de un zombie.
   *
   * Si el zombie no está alertado, va a moverse aleatoriamente. Si está alertado, intentará atacar
   * al jugador y, en caso contrario, lo perseguirá.
   *
   * @param zombie Zombie que ejecuta su turno.
   * @param mazmorra Mazmorra donde se hace el turno.
   */
  public void ejecutarTurno(Zombie zombie, Mazmorra mazmorra) {

    Jugador jugador = mazmorra.getJugador();
    sistemaTurnos.ejecutarDanioAmbientalEnTurno(zombie, mazmorra);

    if (!zombie.estaAlertado()) {

      if (zombie.estaAXCasillas(jugador, 5)) {
        zombie.alertar();
      } else {
        moverAleatorio(zombie, mazmorra);
      }

      return;
    }

    int direccionAtaque = zombie.puedeAtacar(jugador);

    if (direccionAtaque != Entidad.NO_ATACA) {
      zombie.atacarJugador(jugador);
      return;
    }

    perseguirJugador(zombie, jugador, mazmorra);
  }

  /**
   * Hace que el zombie persiga al jugador usando el algoritmo A*.
   *
   * También ejecuta los efectos de daño ambiental activos sobre el zombie antes de desplazarse.
   *
   * @param zombie Zombie que realizará la persecución.
   * @param jugador Jugador objetivo.
   * @param mazmorra Mazmorra donde se encuentran las entidades.
   */
  private void perseguirJugador(Zombie zombie, Jugador jugador, Mazmorra mazmorra) {

    Celda origen = mazmorra.obtenerCelda(zombie.getX(), zombie.getY());
    Celda destino = mazmorra.obtenerCelda(jugador.getX(), jugador.getY());

    Lista<Celda> camino = mazmorra.getGrafo().aEstrella(origen, destino, new HeuristicaCelda());

    if (camino.tamanio() <= 1) {
      return;
    }

    Celda siguiente = camino.dato(1);

    if (siguiente.getX() > zombie.getX()) {
      sistemaMovimiento.moverEntidad(mazmorra, zombie, Entidad.DERECHA);
    } else if (siguiente.getX() < zombie.getX()) {
      sistemaMovimiento.moverEntidad(mazmorra, zombie, Entidad.IZQUIERDA);
    } else if (siguiente.getY() > zombie.getY()) {
      sistemaMovimiento.moverEntidad(mazmorra, zombie, Entidad.ABAJO);
    } else if (siguiente.getY() < zombie.getY()) {
      sistemaMovimiento.moverEntidad(mazmorra, zombie, Entidad.ARRIBA);
    }
  }


  /**
   * Mueve al zombie en una dirección aleatoria válida.
   *
   * @param zombie Zombie que se moverá.
   * @param mazmorra Mazmorra donde se encuentra el zombie.
   */
  private void moverAleatorio(Zombie zombie, Mazmorra mazmorra) {

    int direccion = (int) (Math.random() * 4) + 1;

    while (!zombie.puedeMoverse(mazmorra, direccion)) {
      direccion = (int) (Math.random() * 4) + 1;
    }

    zombie.moverEntidad(direccion);
  }
}
