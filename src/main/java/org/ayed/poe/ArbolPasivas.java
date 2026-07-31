package org.ayed.poe;

import org.ayed.poe.entidades.Jugador;
import org.ayed.tda.diccionario.Diccionario;
import org.ayed.tda.lista.Lista;
import org.ayed.tda.lista.Cola;
import org.ayed.tda.iterador.Iterador;

public class ArbolPasivas {
  private static final int CAPACIDAD_DICCIONARIO_PASIVAS = 200;
  private Pasiva raiz;
  private Diccionario<Integer, Pasiva> pasivaPorId;
  private Jugador jugador;

  public Lista<Pasiva> obtenerPasivas() {
    return this.pasivaPorId.valores();
  }

  public ArbolPasivas(Pasiva raiz, Jugador jugador) {

    if (raiz == null) {
      throw new IllegalArgumentException("La raiz no puede ser nula");
    }

    this.jugador = jugador;
    this.raiz = raiz;
    this.pasivaPorId = new Diccionario<>(CAPACIDAD_DICCIONARIO_PASIVAS);

    this.raiz.setAsignada(true);

    this.pasivaPorId.agregar(raiz.getId(), raiz);
  }

  public ArbolPasivas(ArbolPasivas otro, Jugador jugador) {
    this.jugador = jugador;

    this.pasivaPorId = new Diccionario<>(CAPACIDAD_DICCIONARIO_PASIVAS);

    Lista<Pasiva> pasivas = otro.obtenerPasivas();
    Iterador<Pasiva> it = pasivas.iterador();

    while (it.haySiguiente()) {
      Pasiva p = it.dato();

      Pasiva copia = new Pasiva(
          p.getId(),
          p.getNombre(),
          p.getCosto(),
          p.getBonificaciones());

      copia.setAsignada(p.estaAsignada());

      this.pasivaPorId.agregar(copia.getId(), copia);

      if (p == otro.getRaiz()) {
        this.raiz = copia;
      }

      it.siguiente();
    }

    it = pasivas.iterador();

    while (it.haySiguiente()) {
      Pasiva original = it.dato();
      Pasiva copia = this.obtenerPasiva(original.getId());

      Iterador<Pasiva> itCon = original.getConexiones().iterador();

      while (itCon.haySiguiente()) {
        Pasiva vecinoOriginal = itCon.dato();

        if (vecinoOriginal.getId() > original.getId()) {
          copia.conectarCon(this.obtenerPasiva(vecinoOriginal.getId()));
        }

        itCon.siguiente();
      }

      it.siguiente();
    }
  }

  public Pasiva getRaiz() {
    return this.raiz;
  }

  public Integer getCostoPasivaPorId(int id) {
    Pasiva p = this.pasivaPorId.obtenerValor(id);
    return p.getCosto();
  }

  public void mostrarArbolDisponible() {
    System.out.println("--- PASIVAS PARA DESBLOQUEAR ---");
    Lista<Pasiva> todasLasPasivas = this.pasivaPorId.valores();
    Iterador<Pasiva> iterador = todasLasPasivas.iterador();

    while (iterador.haySiguiente()) {
      Pasiva p = iterador.dato();

      if (!p.estaAsignada()) {
        System.out.printf("ID: %-2d | %-18s | Costo: %d pts | Da: %s%n",
            p.getId(), p.getNombre(), p.getCosto(), p.getTextoBonificaciones());
      }

      iterador.siguiente();
    }
    System.out.println("--------------------------------");
  }

  public void mostrarArbolAdquirido() {
    System.out.println("--- PASIVAS DESBLOQUEADAS ---");
    Lista<Pasiva> todasLasPasivas = this.pasivaPorId.valores();
    Iterador<Pasiva> iterador = todasLasPasivas.iterador();

    while (iterador.haySiguiente()) {
      Pasiva p = iterador.dato();

      if (p.estaAsignada() && p != this.raiz) {
        System.out.printf("ID: %-2d | %-18s | Costo: %d pts | Da: %s%n",
            p.getId(), p.getNombre(), p.getCosto(), p.getTextoBonificaciones());
      }

      iterador.siguiente();
    }
    System.out.println("--------------------------------");
  }

  public void registrarPasiva(Pasiva nuevaPasiva) {
    if (nuevaPasiva != null && this.pasivaPorId.obtenerValor(nuevaPasiva.getId()) == null) {
      this.pasivaPorId.agregar(nuevaPasiva.getId(), nuevaPasiva);
    }
  }

  public void conectarPasivas(int idA, int idB) {
    Pasiva a = this.obtenerPasiva(idA);
    Pasiva b = this.obtenerPasiva(idB);
    if (a != null && b != null) {
      a.conectarCon(b);
    }
  }

  public Pasiva obtenerPasiva(int id) {
    return this.pasivaPorId.obtenerValor(id);
  }

  public boolean asignarPasivaDirecta(int id) {
    Pasiva pasiva = this.obtenerPasiva(id);
    if (pasiva == null || pasiva.estaAsignada()
        || this.jugador.getPuntosHabilidad() < pasiva.getCosto()) {
      return false;
    }

    boolean conectadoAAsignado = false;
    Iterador<Pasiva> it = pasiva.getConexiones().iterador();
    while (it.haySiguiente() && !conectadoAAsignado) {
      if (it.dato().estaAsignada()) {
        conectadoAAsignado = true;
      } else {
        it.siguiente();
      }
    }

    if (conectadoAAsignado) {
      pasiva.setAsignada(true);
      this.jugador.reducirPuntosDeHabilidad(pasiva.getCosto());
      this.jugador.calcularEstadisticas();
    }

    return conectadoAAsignado;
  }

  public boolean asignarPasivaCaminoOptimo(int idDestino) {
    Pasiva destino = this.obtenerPasiva(idDestino);
    if (destino == null || destino.estaAsignada()) {
      return false;
    }

    Diccionario<Integer, Pasiva> padreDe = new Diccionario<>(CAPACIDAD_DICCIONARIO_PASIVAS);
    Cola<Pasiva> cola = new Cola<>();
    Diccionario<Integer, Boolean> visitados = new Diccionario<>(CAPACIDAD_DICCIONARIO_PASIVAS);

    cola.agregar(this.raiz);
    visitados.agregar(this.raiz.getId(), true);
    boolean encontrado = false;

    while (!cola.vacio() && !encontrado) {
      Pasiva actual = cola.eliminar();

      if (actual.getId() == idDestino) {
        encontrado = true;
      } else {
        Iterador<Pasiva> it = actual.getConexiones().iterador();
        while (it.haySiguiente()) {
          Pasiva vecino = it.dato();
          if (visitados.obtenerValor(vecino.getId()) == null) {
            visitados.agregar(vecino.getId(), true);
            padreDe.agregar(vecino.getId(), actual);
            cola.agregar(vecino);
          }
          it.siguiente();
        }
      }
    }

    if (!encontrado) {
      return false;
    }

    Lista<Pasiva> camino = new Lista<>();
    Pasiva paso = destino;
    int costoTotal = 0;

    while (paso != null && !paso.estaAsignada()) {
      camino.agregar(paso, 0);
      costoTotal += paso.getCosto();
      paso = padreDe.obtenerValor(paso.getId());
    }

    boolean exito = this.jugador.getPuntosHabilidad() >= costoTotal;
    if (exito) {
      this.jugador.reducirPuntosDeHabilidad(costoTotal);

      Iterador<Pasiva> itCamino = camino.iterador();

      while (itCamino.haySiguiente()) {
        itCamino.dato().setAsignada(true);
        itCamino.siguiente();
      }

      this.jugador.calcularEstadisticas();
    }

    return exito;
  }

  public boolean desasignarPasiva(int id) {
    Pasiva pasiva = this.obtenerPasiva(id);
    if (pasiva == null || pasiva == this.raiz || !pasiva.estaAsignada()) {
      return false;
    }

    pasiva.setAsignada(false);

    Cola<Pasiva> cola = new Cola<>();
    Diccionario<Integer, Boolean> alcanzables = new Diccionario<>(CAPACIDAD_DICCIONARIO_PASIVAS);

    cola.agregar(this.raiz);
    alcanzables.agregar(this.raiz.getId(), true);

    while (!cola.vacio()) {
      Pasiva actual = cola.eliminar();
      Iterador<Pasiva> it = actual.getConexiones().iterador();
      while (it.haySiguiente()) {
        Pasiva vecino = it.dato();
        if (vecino.estaAsignada() && alcanzables.obtenerValor(vecino.getId()) == null) {
          alcanzables.agregar(vecino.getId(), true);
          cola.agregar(vecino);
        }
        it.siguiente();
      }
    }

    boolean estructuraValida = true;
    Lista<Pasiva> todasLasPasivas = this.pasivaPorId.valores();
    Iterador<Pasiva> itTodas = todasLasPasivas.iterador();

    while (itTodas.haySiguiente() && estructuraValida) {
      Pasiva p = itTodas.dato();
      if (p.estaAsignada() && alcanzables.obtenerValor(p.getId()) == null) {
        estructuraValida = false;
      } else {
        itTodas.siguiente();
      }
    }

    if (!estructuraValida) {
      pasiva.setAsignada(true);
    } else {
      this.jugador.calcularEstadisticas();
    }

    return estructuraValida;
  }
}
