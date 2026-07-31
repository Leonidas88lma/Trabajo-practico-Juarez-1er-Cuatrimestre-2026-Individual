package org.ayed.sistemaGuardado;

import org.ayed.poe.inventario.Inventario;
import org.ayed.poe.mazmorra.Escondite;
import org.ayed.tda.vector.VectorEstatico;

public class DatosEscondite {

  private VectorEstatico<Inventario> paginasAlijo;
  private int cantidadPaginasActivas;
  private int paginaActual=0;
  
  public DatosEscondite(Escondite escondite) {
    this.paginasAlijo = new VectorEstatico<>(3);
    this.cantidadPaginasActivas = escondite.getCantidadPaginasActivas();
    while (paginaActual < cantidadPaginasActivas) {
        this.paginasAlijo.asignar(
            paginaActual,
            new Inventario(escondite.getPagina(paginaActual))
        );
        paginaActual++;
    }
  }

  public DatosEscondite(int cantidadPaginasActivas) {
    this.paginasAlijo = new VectorEstatico<>(3);
    this.cantidadPaginasActivas = cantidadPaginasActivas;
  }
  
  public Inventario getPagina(int num) {
    return this.paginasAlijo.obtener(num);
  }
  
  public int getCantidadPaginasActivas() {
    return this.cantidadPaginasActivas;
  }

  public void setPagina(int numeroPagina, Inventario inventario) {
    this.paginasAlijo.asignar(numeroPagina, inventario);
  }
  
  
}
