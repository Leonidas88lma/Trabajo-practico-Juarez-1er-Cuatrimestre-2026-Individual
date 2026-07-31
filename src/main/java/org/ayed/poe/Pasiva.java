package org.ayed.poe;

import org.ayed.poe.inventario.Estadistica;
import org.ayed.tda.diccionario.Diccionario;
import org.ayed.tda.lista.Lista;

public class Pasiva {

    private final int id;
    private final String nombre;
    private final int costo;
    private final Diccionario<Estadistica, Integer> bonificaciones;
    private final Lista<Pasiva> conexiones;
    private boolean asignada;

    public Pasiva(int id, String nombre, int costo, Diccionario<Estadistica, Integer> bonificaciones) {
        if (id < 0) {
            throw new IllegalArgumentException("El id no puede ser negativo");
        }
        if (costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }

        this.id = id;
        this.nombre = nombre;
        this.costo = costo;
        this.bonificaciones = bonificaciones;
        this.conexiones = new Lista<>();
        this.asignada = false;
    }

    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getCosto() {
        return this.costo;
    }

    public Diccionario<Estadistica, Integer> getBonificaciones() {
        return this.bonificaciones;
    }

    public String getTextoBonificaciones() {
        String bonificaciones = "";
        Estadistica[] estadisticasDisponibles = Estadistica.values();
        boolean primeraAgregada = false;
        Integer valor = null;

        for (int i = 0; i < estadisticasDisponibles.length; i++) {
            valor = this.bonificaciones.obtenerValor(estadisticasDisponibles[i]);

            if (valor != null) {

                if (primeraAgregada) {
                    bonificaciones += ", ";
                }

                bonificaciones += estadisticasDisponibles[i].name() + " +" + valor;
                primeraAgregada = true;
            }

        }

        return bonificaciones;

    }

    public Lista<Pasiva> getConexiones() {
        return this.conexiones;
    }

    public boolean estaAsignada() {
        return this.asignada;
    }

    public void setAsignada(boolean asignada) {
        this.asignada = asignada;
    }

    public void conectarCon(Pasiva otra) {
        if (otra != null) {
            this.conexiones.agregar(otra);
            otra.conexiones.agregar(this);
        }
    }
}