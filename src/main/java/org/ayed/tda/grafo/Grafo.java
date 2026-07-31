package org.ayed.tda.grafo;

import java.util.*;

import org.ayed.tda.colaPrioridad.ColaPrioridad;
import org.ayed.tda.conjunto.Conjunto;
import org.ayed.tda.diccionario.Diccionario;
import org.ayed.tda.lista.Cola;
import org.ayed.tda.lista.Lista;

/**
 * Implementación de un grafo no dirigido.
 *
 * Cada vértice puede conectarse con otros vértices mediante
 * aristas con peso.
 *
 * Internamente se representa usando:
 *
 * Map<Vertice, Map<Vertice, Peso>>
 *
 * Esto permite:
 * - acceder rápido a vértices
 * - obtener adyacentes eficientemente
 * - almacenar pesos de aristas
 *
 * Además incluye algoritmos de búsqueda:
 * - BFS
 * - Dijkstra
 * - A*
 */

public class Grafo<T> {
    private long operacionesBfs;
    private long operacionesDijkstra;
    private long operacionesAEstrella;
    public long getOperacionesBfs() {
        return operacionesBfs;
    }

    public long getOperacionesDijkstra() {
        return operacionesDijkstra;
    }

    public long getOperacionesAEstrella() {
        return operacionesAEstrella;
    }
    /*
     * Estructura principal del grafo.
     *
     * Cada vértice apunta a otro mapa:
     * vértice vecino -> peso de arista
     */
    protected final Map<T, Map<T, Integer>> adyacencias;

    // Valor usado como distancia "infinita"
    protected static final int INFINITO = 99999;

    // Peso por defecto si no se usa peso explícito
    protected static final int SIN_PESO = 1;

    /**
     * Constructor.
     *
     * Inicializa el mapa de adyacencias vacío.
     */
    public Grafo() {
        adyacencias = new HashMap<>();
    }

    /**
     * Constructor copia.
     *
     * Realiza una copia del grafo recibido.
     */
    public Grafo(Grafo<T> grafo) {

        if (grafo == null) {
            throw new ExcepcionGrafo("El grafo no puede ser nulo.");
        }

        adyacencias = new HashMap<>();

        // Copia cada vértice y sus adyacencias
        for (T vertice : grafo.adyacencias.keySet()) {

            adyacencias.put(
                    vertice,
                    new HashMap<>(grafo.obtenerAdyacentes(vertice)));
        }
    }

    /**
     * Agrega un vértice nuevo al grafo.
     */
    public void agregarVertice(T vertice) {

        if (vertice == null) {
            throw new ExcepcionGrafo("El vértice no puede ser nulo.");
        }

        if (adyacencias.containsKey(vertice)) {
            throw new ExcepcionGrafo("El vértice ya existe.");
        }

        // Se crea su lista de adyacencias vacía
        adyacencias.put(vertice, new HashMap<>());
    }

    /**
     * Elimina un vértice y todas sus conexiones.
     */
    public void eliminarVertice(T vertice) {

        if (!adyacencias.containsKey(vertice)) {
            throw new ExcepcionGrafo("El vértice no existe.");
        }

        // Elimina el vértice principal
        adyacencias.remove(vertice);

        // Elimina referencias desde otros vértices
        for (Map<T, Integer> adyacente : adyacencias.values()) {
            adyacente.remove(vertice);
        }
    }

    /**
     * Agrega una arista entre dos vértices.
     *
     * Como el grafo es no dirigido:
     * origen -> destino
     * destino -> origen
     */
    public void agregarArista(T origen, T destino, int peso) {

        if (!adyacencias.containsKey(origen)
                || !adyacencias.containsKey(destino)) {

            throw new ExcepcionGrafo("La arista no es válida.");
        }

        obtenerAdyacentes(origen).put(destino, peso);

        obtenerAdyacentes(destino).put(origen, peso);
    }

    /**
     * Elimina una arista entre dos vértices.
     */
    public void eliminarArista(T origen, T destino) {

        if (!adyacencias.containsKey(origen)
                || !adyacencias.containsKey(destino)) {

            throw new ExcepcionGrafo("La arista no es válida.");
        }

        obtenerAdyacentes(origen).remove(destino);

        obtenerAdyacentes(destino).remove(origen);
    }

    /**
     * Obtiene el peso de una arista.
     */
    public int obtenerArista(T origen, T destino) {

        if (!adyacencias.containsKey(origen)
                || !adyacencias.containsKey(destino)) {

            throw new ExcepcionGrafo("La arista no es válida.");
        }

        if (!obtenerAdyacentes(origen).containsKey(destino)) {
            throw new ExcepcionGrafo("La arista no existe.");
        }

        return obtenerAdyacentes(origen).get(destino);
    }

    /**
     * Devuelve todos los vértices adyacentes
     * a un vértice dado.
     */
    public Map<T, Integer> obtenerAdyacentes(T vertice) {

        if (!adyacencias.containsKey(vertice)) {
            throw new ExcepcionGrafo("El vértice no existe.");
        }

        return adyacencias.get(vertice);
    }

    /**
     * Reconstruye el camino desde origen hasta destino.
     *
     * Usa un diccionario de "anteriores":
     *
     * actual -> nodo previo
     *
     * y reconstruye el camino hacia atrás.
     */
    private Lista<T> reconstruirCamino(
            Diccionario<T, T> anteriores,
            T origen,
            T destino) {

        Lista<T> camino = new Lista<>();

        // Caso trivial
        if (origen.equals(destino)) {

            camino.agregar(origen);

            return camino;
        }

        T actual = destino;

        // Reconstrucción inversa del camino
        while (actual != null) {

            // Inserta al inicio
            camino.agregar(actual, 0);

            if (actual.equals(origen)) {
                return camino;
            }

            actual = anteriores.obtenerValor(actual);
        }

        // Si no se encontró camino
        return new Lista<>();
    }

    /**
     * BFS (Breadth First Search).
     *
     * Busca el camino con menor cantidad de pasos,
     * ignorando pesos.
     *
     * Ideal para grafos sin pesos.
     */
    public Lista<T> bfs(T origen, T destino) {
        operacionesBfs = 0;

        if (!adyacencias.containsKey(origen)
                || !adyacencias.containsKey(destino)) {

            throw new ExcepcionGrafo("El vértice no existe.");
        }

        Cola<T> cola = new Cola<>();

        Conjunto<T> visitados = new Conjunto<>(adyacencias.size());

        Diccionario<T, T> anteriores = new Diccionario<>(adyacencias.size());

        cola.agregar(origen);

        visitados.agregar(origen);

        while (!cola.vacio()) {
            operacionesBfs++;

            T actual = cola.eliminar();

            // Si llegó al destino
            if (actual.equals(destino)) {

                return reconstruirCamino(
                        anteriores,
                        origen,
                        destino);
            }

            // Explora vecinos
            for (T vecino : obtenerAdyacentes(actual).keySet()) {
                operacionesBfs++;

                if (!visitados.contiene(vecino)) {

                    visitados.agregar(vecino);

                    anteriores.agregar(vecino, actual);

                    cola.agregar(vecino);
                }
            }
        }

        // No existe camino
        return new Lista<>();
    }

    /**
     * Algoritmo de Dijkstra.
     *
     * Busca el camino de costo mínimo
     * considerando pesos.
     */
    public Lista<T> dijkstra(T origen, T destino) {
        operacionesDijkstra = 0;

        if (!adyacencias.containsKey(origen)
                || !adyacencias.containsKey(destino)) {

            throw new ExcepcionGrafo("El vértice no existe.");
        }

        // Distancia mínima conocida a cada nodo
        Diccionario<T, Integer> distancias = new Diccionario<>(adyacencias.size());

        // Nodo previo en el camino óptimo
        Diccionario<T, T> anteriores = new Diccionario<>(adyacencias.size());

        // Conjunto de nodos procesados
        Conjunto<T> visitados = new Conjunto<>(adyacencias.size());

        // Cola de prioridad
        ColaPrioridad<NodoPrioridad<T>> cola = new ColaPrioridad<>(
                new ComparadorNodoPrioridad<>());

        // Inicializa distancias infinitas
        for (T vertice : adyacencias.keySet()) {
            distancias.agregar(vertice, INFINITO);
        }

        distancias.agregar(origen, 0);

        cola.agregar(new NodoPrioridad<>(origen, 0));

        while (!cola.vacio()) {
            operacionesDijkstra++;

            NodoPrioridad<T> nodo = cola.eliminar();

            T actual = nodo.vertice;

            // Evita reprocesar nodos
            if (visitados.contiene(actual)) {
                continue;
            }

            visitados.agregar(actual);

            if (actual.equals(destino)) {
                break;
            }

            // Relajación de aristas
            for (Map.Entry<T, Integer> entry : obtenerAdyacentes(actual).entrySet()) {
                operacionesDijkstra++;

                T vecino = entry.getKey();

                int peso = entry.getValue();

                int nuevaDistancia = distancias.obtenerValor(actual) + peso;

                // Si encontró un camino mejor
                if (nuevaDistancia < distancias.obtenerValor(vecino)) {

                    distancias.agregar(
                            vecino,
                            nuevaDistancia);

                    anteriores.agregar(
                            vecino,
                            actual);

                    cola.agregar(
                            new NodoPrioridad<>(
                                    vecino,
                                    -nuevaDistancia));
                }
            }
        }

        return reconstruirCamino(
                anteriores,
                origen,
                destino);
    }

    /**
     * Algoritmo A*.
     *
     * Similar a Dijkstra pero utilizando
     * una heurística para guiar la búsqueda.
     *
     * Generalmente más eficiente.
     */
    public Lista<T> aEstrella(
            T origen,
            T destino,
            Heuristica<T> heuristica) {
        operacionesAEstrella = 0;

        if (!adyacencias.containsKey(origen)
                || !adyacencias.containsKey(destino)) {

            throw new ExcepcionGrafo("El vértice no existe.");
        }

        // Costo real desde origen
        Diccionario<T, Integer> gScore = new Diccionario<>(adyacencias.size());

        // Camino óptimo
        Diccionario<T, T> anteriores = new Diccionario<>(adyacencias.size());

        // Nodos ya evaluados
        Conjunto<T> cerrados = new Conjunto<>(adyacencias.size());

        // Cola de nodos abiertos
        ColaPrioridad<NodoPrioridad<T>> abiertos = new ColaPrioridad<>(
                new ComparadorNodoPrioridad<>());

        // Inicializa costos infinitos
        for (T vertice : adyacencias.keySet()) {
            gScore.agregar(vertice, INFINITO);
        }

        gScore.agregar(origen, 0);

        abiertos.agregar(
                new NodoPrioridad<>(
                        origen,
                        0));

        while (!abiertos.vacio()) {
            operacionesAEstrella++;

            NodoPrioridad<T> nodo = abiertos.eliminar();

            T actual = nodo.vertice;

            // Llegó al destino
            if (actual.equals(destino)) {

                return reconstruirCamino(
                        anteriores,
                        origen,
                        destino);
            }

            // Evita reprocesar nodos
            if (cerrados.contiene(actual)) {
                continue;
            }

            cerrados.agregar(actual);

            // Explora vecinos
            for (Map.Entry<T, Integer> entry : obtenerAdyacentes(actual).entrySet()) {

                operacionesAEstrella++;

                T vecino = entry.getKey();

                int peso = entry.getValue();

                int tentative = gScore.obtenerValor(actual) + peso;

                // Mejor camino encontrado
                if (tentative < gScore.obtenerValor(vecino)) {

                    anteriores.agregar(vecino, actual);

                    gScore.agregar(vecino, tentative);

                    // fScore = costo real + heurística
                    int fScore = tentative +
                            heuristica.calcularPuntaje(
                                    vecino,
                                    destino);

                    abiertos.agregar(
                            new NodoPrioridad<>(
                                    vecino,
                                    -fScore));
                }
            }
        }

        // No existe camino
        return new Lista<>();
    }
}