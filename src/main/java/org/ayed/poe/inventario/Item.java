package org.ayed.poe.inventario;

import java.util.Objects;

import org.ayed.tda.diccionario.Diccionario;

public class Item {
    private final int id;
    private final int ancho;
    private final int alto;
    private TipoItem tipo;
    private RarezaItem rareza;
    private String nombre;
    private Diccionario<Estadistica, Integer> estadisticas;

    public Item(int id, int ancho, int alto, TipoItem tipo, RarezaItem rareza, String nombre,
            Diccionario<Estadistica, Integer> estadisticas) {
        this.id = id;
        this.ancho = ancho;
        this.alto = alto;
        this.tipo = tipo;
        this.nombre = nombre;
        this.rareza = rareza;
        this.estadisticas = estadisticas;
    }

    public Item(int id, int ancho, int alto) {
        this.id = id;
        this.ancho = ancho;
        this.alto = alto;
    }

    public Item() {
        this.id = 0;
        this.ancho = 0;
        this.alto = 0;
    }

    public int getId() {
        return id;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public String getNombre() {
        return this.nombre;
    }

    public TipoItem getTipo() {
        return this.tipo;
    }
    
    public RarezaItem getRareza() {
      return this.rareza;
    }

    public Diccionario<Estadistica, Integer> getEstadisticas() {
        return this.estadisticas;
    }

    @Override
    public String toString() {
        return "Nombre: " + this.nombre + " | Tipo: " + this.tipo + " | Rareza: " + this.rareza;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Item))
            return false;
        Item item = (Item) o;
        return id == item.id &&
                ancho == item.ancho &&
                alto == item.alto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ancho, alto);
    }
}