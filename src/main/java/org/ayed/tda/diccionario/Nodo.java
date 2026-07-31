package org.ayed.tda.diccionario;

/**
 * Nodo para el uso exclusivo del paquete diccionario (Package-private).
 */
class Nodo<C, V> {
    C clave;
    V valor;
    Nodo<C, V> padre;
    Nodo<C, V> hijoIzquierdo;
    Nodo<C, V> hijoDerecho;

    /**
     * Constructor de Nodo.
     *
     * @param clave         Clave del nodo.
     * @param valor         Valor del nodo.
     * @param padre         Nodo padre.
     * @param hijoIzquierdo Nodo hijo izquierdo.
     * @param hijoDerecho   Nodo hijo derecho.
     */
    public Nodo(C clave, V valor, Nodo<C, V> padre, Nodo<C, V> hijoIzquierdo, Nodo<C, V> hijoDerecho) {
        this.clave = clave;
        this.valor = valor;
        this.padre = padre;
        this.hijoIzquierdo = hijoIzquierdo;
        this.hijoDerecho = hijoDerecho;
    }

    /**
     * Constructor de Nodo.
     *
     * @param clave Clave del nodo.
     * @param valor Valor del nodo.
     * @param padre Nodo padre.
     */
    public Nodo(C clave, V valor, Nodo<C, V> padre) {
        this(clave, valor, padre, null, null);
    }

    /**
     * Constructor de Nodo.
     *
     * @param clave Clave del nodo.
     * @param valor Valor del nodo.
     */
    public Nodo(C clave, V valor) {
        this(clave, valor, null);
    }
}
