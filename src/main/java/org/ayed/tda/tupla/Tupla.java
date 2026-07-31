package org.ayed.tda.tupla;

/**
 * Clase auxiliar para guardar una tupla de datos genéricos.
 * Esta clase puede ser de utilidad para estructuras
 * asociativas como diccionarios, y para algoritmos como A* y
 * Dijkstra.
 * En caso de necesitarlo, pueden crear clases similares
 * para devolver datos de diferentes tipos.
 */
public class Tupla<C, V> {
    private final C clave;
    private final V valor;

    /**
     * Constructor de Tupla.
     *
     * @param clave Dato de la clave.
     * @param valor Dato del valor.
     */
    public Tupla(C clave, V valor) {
        this.clave = clave;
        this.valor = valor;
    }

    /**
     * Obtiene la clave de la tupla.
     *
     * @return la clave.
     */
    public C clave() {
        return clave;
    }

    /**
     * Obtiene el valor de la tupla.
     *
     * @return el valor.
     */
    public V valor() {
        return valor;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", clave.toString(), valor.toString());
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object objeto) {
        if (objeto == this) {
            return true;
        }
        if (!(objeto instanceof Tupla<?, ?>)) {
            return false;
        }
        Tupla<C, V> otraTupla = (Tupla<C, V>) objeto;
        return clave.equals(otraTupla.clave) && valor.equals(otraTupla.valor);
    }

    @Override
    public int hashCode() {
        return clave.hashCode() + 31 * valor.hashCode();
    }
}
