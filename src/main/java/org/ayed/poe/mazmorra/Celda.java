package org.ayed.poe.mazmorra;

import org.ayed.poe.TipoEfecto;

public class Celda {

    public boolean esPared;
    public boolean tieneItem;
    public TipoEfecto tipoEfecto;
    private int x;
    private int y;

    public Celda(TipoEfecto tipo, int x, int y) {
      this.tipoEfecto = tipo;
      this.x = x;
      this.y = y;
    }
    
    public TipoEfecto getTipoEfecto() {
      return tipoEfecto;
    }
    
    public int getX() {
      return x;
    }
    public int getY() {
      return y;
    }
}