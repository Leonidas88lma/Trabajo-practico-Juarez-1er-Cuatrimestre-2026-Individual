package org.ayed.poe.sistemas;

import org.ayed.poe.TipoEfecto;
import org.ayed.poe.entidades.Entidad;
import org.ayed.poe.entidades.Jugador;
import org.ayed.poe.entidades.Zombie;
import org.ayed.poe.inventario.Item;
import org.ayed.poe.inventario.RarezaItem;
import org.ayed.poe.inventario.TipoItem;
import org.ayed.poe.mazmorra.Mazmorra;

public class SistemaAtaqueJugador {

  private final int[][] patronAtaquePunio = {{0, -1}, {0, -2}};

  private final int[][] patronAtaqueEspada =
      {{0, -1}, {-1, -2}, {0, -2}, {1, -2}, {-2, -3}, {-1, -3}, {0, -3}, {1, -3}, {2, -3}};

  private final int[][] patronAtaqueEspadaUnica =
      {{0, -1}, {-1, -2}, {0, -2}, {1, -2}, {-1, -3}, {0, -3}, {1, -3}};

  private final int[][] patronAtaqueEspadaUnica2 =
      {{-2, -3}, {2, -3}, {3, -4}, {2, -4}, {1, -4}, {0, -4}, {-1, -4}, {-2, -4}, {-3, -4}};

  private final int[][] patronAtaqueMaza = {{-1, -1}, {0, -1}, {1, -1}, {-1, -2}, {0, -2}, {1, -2}};

  private final int[][] patronAtaqueVarita = {{-1, -4}, {1, -4}, {-1, -3}, {0, -3}, {1, -3},
      {-2, -2}, {-1, -2}, {0, -2}, {1, -2}, {2, -2}, {0, -1}};

  private final int[][] patronAtaqueArco = {{0, -1}, {0, -2}, {0, -3}, {0, -4}, {0, -5}, {0, -6}};

  /**
   * Obtiene el arma actualmente equipada por el jugador.
   *
   * Recorre los posibles tipos de arma equipables y devuelve la primera que encuentre.
   *
   * @param jugador El jugador cuyo equipo se quiere consultar
   * @return El arma equipada o null si no tiene ninguna equipada
   */
  public Item obtenerArmaEquipada(Jugador jugador) {
    TipoItem[] armas =
        {TipoItem.ARMA_ARCO, TipoItem.ARMA_ESPADA, TipoItem.ARMA_MAZA, TipoItem.ARMA_VARITA};
    Item arma = null;
    int i = 0;

    while (i < armas.length && arma == null) {

      arma = jugador.getEquipo().obtenerValor(armas[i]);

      i++;
    }

    return arma;
  }

  /**
   * Ejecuta un ataque del jugador sobre la mazmorra.
   *
   * Determina el patrón de ataque según el arma equipada y aplica daño a las entidades alcanzadas.
   * Algunas armas especiales pueden utilizar patrones secundarios o aplicar efectos adicionales.
   *
   * @param jugador El jugador que realiza el ataque
   * @param mazmorra La mazmorra donde se encuentran las entidades
   * @param direccion La dirección hacia la que se realiza el ataque
   */
  public void atacar(Jugador jugador, Mazmorra mazmorra, int direccion) {

    Item arma = obtenerArmaEquipada(jugador);

    int[][] patronPrincipal = patronAtaquePunio;
    int[][] patronSecundario = null;

    boolean incendia = false;

    if (arma != null) {

      if (arma.getTipo() == TipoItem.ARMA_MAZA) {

        patronPrincipal = patronAtaqueMaza;

      } else if (arma.getTipo() == TipoItem.ARMA_ESPADA && arma.getRareza() == RarezaItem.UNICO) {

        patronPrincipal = patronAtaqueEspadaUnica;
        patronSecundario = patronAtaqueEspadaUnica2;
        incendia = true;

      } else if (arma.getTipo() == TipoItem.ARMA_ESPADA) {

        patronPrincipal = patronAtaqueEspada;

      } else if (arma.getTipo() == TipoItem.ARMA_ARCO) {

        patronPrincipal = patronAtaqueArco;

      } else if (arma.getTipo() == TipoItem.ARMA_VARITA) {

        patronPrincipal = patronAtaqueVarita;
      }
    }

    aplicarPatron(jugador, mazmorra, direccion, patronPrincipal, 1.0, incendia);

    if (patronSecundario != null) {

      aplicarPatron(jugador, mazmorra, direccion, patronSecundario, 0.5, incendia);
    }
  }

  /**
   * Aplica un patrón de ataque sobre la mazmorra.
   *
   * Rota el patrón según la dirección indicada, busca entidades afectadas por cada casilla del
   * patrón y les aplica daño. Opcionalmente puede aplicar el efecto de incendiado.
   *
   * @param jugador El jugador que origina el ataque
   * @param mazmorra La mazmorra donde se ejecuta el ataque
   * @param direccion La dirección del ataque
   * @param patron El patrón de ataque a utilizar
   * @param multiplicadorDanio Multiplicador aplicado al daño base
   * @param incendia Indica si el ataque debe aplicar incendiado
   */
  private void aplicarPatron(Jugador jugador, Mazmorra mazmorra, int direccion, int[][] patron,
      double multiplicadorDanio, boolean incendia) {

    int[][] patronRotado = jugador.rotarAtaque(patron, direccion);

    for (int i = 0; i < patronRotado.length; i++) {

      int objetivoX = jugador.getX() + patronRotado[i][0];

      int objetivoY = jugador.getY() + patronRotado[i][1];

      Entidad entidad = mazmorra.obtenerEntidadEn(objetivoX, objetivoY);

      if (entidad instanceof Zombie) {

        int danio = (int) (jugador.getDanoAtaque() * multiplicadorDanio);

        if (jugador.getEstadoCongelado() > 0) {
          danio = (int) (danio - (danio * 0.35));
        }

        entidad.recibirDano(danio);

        if (incendia) {
          entidad.recibirDanoAmbiental(TipoEfecto.INCENDIADO);
          System.out.println("Zombie incendiado");
        }
      }
    }
  }
}
