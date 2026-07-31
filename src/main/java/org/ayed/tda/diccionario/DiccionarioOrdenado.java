package org.ayed.tda.diccionario;

import org.ayed.tda.comparador.Comparador;
import org.ayed.tda.lista.Cola;
import org.ayed.tda.lista.Lista;
import org.ayed.tda.tupla.Tupla;

public class DiccionarioOrdenado<C, V> {

    private Nodo<C, V> raiz;
    private Comparador<C> comparador;
    private int cantidadDatos;

    public DiccionarioOrdenado(Comparador<C> comparador) {

        if (comparador == null) {
            throw new ExcepcionDiccionario("El comparador no puede ser nulo.");
        }

        this.comparador = comparador;
        raiz = null;
        cantidadDatos = 0;
    }

    public DiccionarioOrdenado(DiccionarioOrdenado<C, V> diccionarioOrdenado) {

        if (diccionarioOrdenado == null) {
            throw new ExcepcionDiccionario("El diccionario no puede ser nulo.");
        }

        this.comparador = diccionarioOrdenado.comparador;
        this.raiz = clonarSubarbol(diccionarioOrdenado.raiz, null);
        this.cantidadDatos = diccionarioOrdenado.cantidadDatos;
    }

    private Nodo<C, V> clonarSubarbol(Nodo<C, V> nodo, Nodo<C, V> padre) {

        if (nodo == null) {
            return null;
        }

        Nodo<C, V> nuevo = new Nodo<>(nodo.clave, nodo.valor, padre);

        nuevo.hijoIzquierdo = clonarSubarbol(nodo.hijoIzquierdo, nuevo);
        nuevo.hijoDerecho = clonarSubarbol(nodo.hijoDerecho, nuevo);

        return nuevo;
    }

    private Nodo<C, V> buscarNodo(C clave) {

        Nodo<C, V> actual = raiz;

        while (actual != null) {

            int cmp = comparador.comparar(clave, actual.clave);

            if (cmp == 0) {
                return actual;
            }

            if (cmp < 0) {
                actual = actual.hijoIzquierdo;
            } else {
                actual = actual.hijoDerecho;
            }
        }

        return null;
    }

    private Nodo<C, V> obtenerSucesorInmediato(Nodo<C, V> nodo) {

        Nodo<C, V> actual = nodo.hijoDerecho;

        while (actual.hijoIzquierdo != null) {
            actual = actual.hijoIzquierdo;
        }

        return actual;
    }

    public V agregar(C clave, V valor) {

        if (raiz == null) {
            raiz = new Nodo<>(clave, valor);
            cantidadDatos++;
            return null;
        }

        Nodo<C, V> actual = raiz;
        Nodo<C, V> padre = null;

        while (actual != null) {

            padre = actual;

            int cmp = comparador.comparar(clave, actual.clave);

            if (cmp == 0) {

                V viejo = actual.valor;
                actual.valor = valor;

                return viejo;
            }

            if (cmp < 0) {
                actual = actual.hijoIzquierdo;
            } else {
                actual = actual.hijoDerecho;
            }
        }

        Nodo<C, V> nuevo = new Nodo<>(clave, valor, padre);

        if (comparador.comparar(clave, padre.clave) < 0) {
            padre.hijoIzquierdo = nuevo;
        } else {
            padre.hijoDerecho = nuevo;
        }

        cantidadDatos++;

        return null;
    }

    public V eliminar(C clave) {

        Nodo<C, V> nodo = buscarNodo(clave);

        if (nodo == null) {
            return null;
        }

        V valorEliminado = nodo.valor;

        eliminarNodo(nodo);

        cantidadDatos--;

        return valorEliminado;
    }

    private void reemplazarNodo(Nodo<C, V> nodo, Nodo<C, V> reemplazo) {

        if (nodo.padre == null) {

            raiz = reemplazo;

        } else if (nodo == nodo.padre.hijoIzquierdo) {

            nodo.padre.hijoIzquierdo = reemplazo;

        } else {

            nodo.padre.hijoDerecho = reemplazo;
        }

        if (reemplazo != null) {
            reemplazo.padre = nodo.padre;
        }
    }

    private void eliminarNodo(Nodo<C, V> nodo) {

        // Sin hijos
        if (nodo.hijoIzquierdo == null && nodo.hijoDerecho == null) {

            reemplazarNodo(nodo, null);
            return;
        }

        // Un hijo derecho
        if (nodo.hijoIzquierdo == null) {

            reemplazarNodo(nodo, nodo.hijoDerecho);
            return;
        }

        // Un hijo izquierdo
        if (nodo.hijoDerecho == null) {

            reemplazarNodo(nodo, nodo.hijoIzquierdo);
            return;
        }

        // Dos hijos
        Nodo<C, V> sucesor = obtenerSucesorInmediato(nodo);

        nodo.clave = sucesor.clave;
        nodo.valor = sucesor.valor;

        eliminarNodo(sucesor);
    }

    public V obtenerValor(C clave) {

        Nodo<C, V> nodo = buscarNodo(clave);

        return nodo == null ? null : nodo.valor;
    }

    public Lista<Tupla<C, V>> inorder() {

        Lista<Tupla<C, V>> lista = new Lista<>();

        inorderRec(raiz, lista);

        return lista;
    }

    private void inorderRec(
            Nodo<C, V> nodo,
            Lista<Tupla<C, V>> lista) {

        if (nodo == null) {
            return;
        }

        inorderRec(nodo.hijoIzquierdo, lista);

        lista.agregar(new Tupla<>(nodo.clave, nodo.valor));

        inorderRec(nodo.hijoDerecho, lista);
    }

    public Lista<Tupla<C, V>> preorder() {

        Lista<Tupla<C, V>> lista = new Lista<>();

        preorderRec(raiz, lista);

        return lista;
    }

    private void preorderRec(
            Nodo<C, V> nodo,
            Lista<Tupla<C, V>> lista) {

        if (nodo == null) {
            return;
        }

        lista.agregar(new Tupla<>(nodo.clave, nodo.valor));

        preorderRec(nodo.hijoIzquierdo, lista);
        preorderRec(nodo.hijoDerecho, lista);
    }

    public Lista<Tupla<C, V>> postorder() {

        Lista<Tupla<C, V>> lista = new Lista<>();

        postorderRec(raiz, lista);

        return lista;
    }

    private void postorderRec(
            Nodo<C, V> nodo,
            Lista<Tupla<C, V>> lista) {

        if (nodo == null) {
            return;
        }

        postorderRec(nodo.hijoIzquierdo, lista);
        postorderRec(nodo.hijoDerecho, lista);

        lista.agregar(new Tupla<>(nodo.clave, nodo.valor));
    }

    public Lista<Tupla<C, V>> ancho() {

        Lista<Tupla<C, V>> recorrido = new Lista<>();

        if (raiz == null) {
            return recorrido;
        }

        Cola<Nodo<C, V>> cola = new Cola<>();

        cola.agregar(raiz);

        while (!cola.vacio()) {

            Nodo<C, V> actual = cola.eliminar();

            recorrido.agregar(
                    new Tupla<>(actual.clave, actual.valor));

            if (actual.hijoIzquierdo != null) {
                cola.agregar(actual.hijoIzquierdo);
            }

            if (actual.hijoDerecho != null) {
                cola.agregar(actual.hijoDerecho);
            }
        }

        return recorrido;
    }

    public int tamanio() {
        return cantidadDatos;
    }

    public boolean vacio() {
        return cantidadDatos == 0;
    }
}