package org.ayed.poe.inventario;

import java.util.Random;

import org.ayed.tda.diccionario.Diccionario;

/**
 * Clase encargada de la generacion procedural de items del juego.
 * Controla la creacion de nombres, estadisticas, tamaños y rarezas.
 */
public class GeneradorItems {

    private static final int NUMERO_ID_BASE = 100;
    private static final int DANO_ATAQUE_BASE = 5;
    private static final int ARMADURA_BASE = 5;
    private static final int MANA_BASE = 10;
    private static final int DANO_HECHIZO_BASE = 5;
    private static final int VIDA_BASE = 25;

    private Random generadorRandom;
    private int contadorIds;
    private Diccionario<TipoItem, String[]> diccionarioSufijos;

    public void setContadorIds(int id) {
        this.contadorIds = id;
    }

    /**
     * Constructor de GeneradorItems.
     * Inicializa el generador de numeros aleatorios, el contador de identificadores
     * y precarga el diccionario de sufijos para los nombres.
     */
    public GeneradorItems() {
        this.generadorRandom = new Random();
        this.contadorIds = NUMERO_ID_BASE;
        this.cargarDiccionarioSufijos();
    }

    public int getUltimoId() {
        return this.contadorIds;
    }

    /**
     * Genera un item completamente aleatorio decidiendo su tipo y rareza.
     * 
     * @return Un nuevo Item con caracteristicas generadas aleatoriamente.
     */
    public Item generarItemAleatorio() {
        TipoItem[] tiposDeObjetoDisponibles = TipoItem.values();
        int elegirTipo = this.generadorRandom.nextInt(0, tiposDeObjetoDisponibles.length);
        TipoItem tipoObjetoElegido = tiposDeObjetoDisponibles[elegirTipo];

        int rarezaItem = this.generadorRandom.nextInt(0, 100);
        Item nuevoItem = null;

        if (rarezaItem < 2) {
            nuevoItem = this.crearItemUnico(tipoObjetoElegido, RarezaItem.UNICO);

        } else if (rarezaItem < 15) {
            nuevoItem = this.crearItem(tipoObjetoElegido, RarezaItem.RARO);

        } else if (rarezaItem < 40) {
            nuevoItem = this.crearItem(tipoObjetoElegido, RarezaItem.MAGICO);

        } else {
            nuevoItem = this.crearItem(tipoObjetoElegido, RarezaItem.NORMAL);

        }

        this.contadorIds += 1;

        return nuevoItem;
    }

    /**
     * Crea un item de rareza Normal, Magico o Raro, asignando sus estadisticas base
     * y atributos aleatorios adicionales segun corresponda.
     * 
     * @param tipo   El tipo de item a crear.
     * 
     * @param rareza La rareza del item a crear.
     * 
     * @return El Item instanciado con sus propiedades correspondientes.
     */
    private Item crearItem(TipoItem tipo, RarezaItem rareza) {
        Integer[] tamanio = this.generarTamanio(tipo);
        String nombre = this.generarNombre(tipo);
        int cantidadAtributos = this.obtenerCantidadEstadisticas(rareza);
        Estadistica[] estadisticasDisponibles = Estadistica.values();
        int cantidadEstadisticasTotales = estadisticasDisponibles.length;
        boolean[] estadisticasAModificar = new boolean[cantidadEstadisticasTotales]; // cada indice corresponde a una
                                                                                     // estadistica distinta del enum
        Diccionario<Estadistica, Integer> estadisticas = new Diccionario<Estadistica, Integer>(
                cantidadEstadisticasTotales);
        this.generarEstadisticasBase(estadisticas, tipo);
        int indiceEstadistica = 0;
        int contador = 0;

        while (contador < cantidadAtributos) {
            indiceEstadistica = this.generadorRandom.nextInt(0, cantidadEstadisticasTotales);

            if (!this.fueUsadaLaEstadistica(estadisticasAModificar, indiceEstadistica)) {
                int valorEstadistica = this.generarValorEstadistica(rareza);
                estadisticas.agregar(estadisticasDisponibles[indiceEstadistica], valorEstadistica);
                estadisticasAModificar[indiceEstadistica] = true;
                contador += 1;
            }
        }
        Item nuevoItem = new Item(this.contadorIds, tamanio[0], tamanio[1], tipo, rareza, nombre, estadisticas);
        return nuevoItem;
    }

    /**
     * Crea un item de rareza Unico con nombre y estadisticas fijas predefinidas.
     * 
     * @param tipo   El tipo de item unico a crear.
     * 
     * @param rareza La rareza del item (siempre sera UNICO).
     * 
     * @return El Item instanciado con sus valores fijos.
     * 
     * @throws IllegalArgumentException Si el tipo no tiene un objeto unico
     *                                  configurado.
     */
    private Item crearItemUnico(TipoItem tipo, RarezaItem rareza) {
        Integer[] tamanio = this.generarTamanio(tipo);
        String nombre = "";
        Estadistica[] estadisticasDisponibles = Estadistica.values();
        int cantidadEstadisticasTotales = estadisticasDisponibles.length;
        Diccionario<Estadistica, Integer> estadisticas = new Diccionario<Estadistica, Integer>(
                cantidadEstadisticasTotales);

        if (tipo == TipoItem.ANILLO) {
            nombre = "Anillo UNICO";
            estadisticas.agregar(Estadistica.VIDA, 200);
            estadisticas.agregar(Estadistica.MANA, 200);

        } else if (tipo == TipoItem.ARMADURA) {
            nombre = "Armadura UNICO";
            estadisticas.agregar(Estadistica.ARMADURA, 250);
            estadisticas.agregar(Estadistica.VIDA, 75);

        } else if (tipo == TipoItem.ARMA_ARCO) {
            nombre = "Arco UNICO";
            estadisticas.agregar(Estadistica.DANO_ATAQUE, 80);

        } else if (tipo == TipoItem.ARMA_ESPADA) {
            nombre = "Espada infernal";
            estadisticas.agregar(Estadistica.DANO_ATAQUE, 40);
            estadisticas.agregar(Estadistica.MANA, 75);

        } else if (tipo == TipoItem.ARMA_MAZA) {
            nombre = "Maza UNICO";
            estadisticas.agregar(Estadistica.DANO_ATAQUE, 150);

        } else if (tipo == TipoItem.ARMA_VARITA) {
            nombre = "Varita UNICO";
            estadisticas.agregar(Estadistica.DANO_HECHIZO, 100);

        } else if (tipo == TipoItem.BOTAS) {
            nombre = "Botas UNICO";
            estadisticas.agregar(Estadistica.ARMADURA, 100);
            estadisticas.agregar(Estadistica.VIDA, 100);

        } else if (tipo == TipoItem.CASCO) {
            nombre = "Casco UNICO";
            estadisticas.agregar(Estadistica.ARMADURA, 100);
            estadisticas.agregar(Estadistica.VIDA, 100);

        } else if (tipo == TipoItem.CINTURON) {
            nombre = "Cinturon UNICO";
            estadisticas.agregar(Estadistica.MANA, 200);

        } else if (tipo == TipoItem.GUANTES) {
            nombre = "Guantes UNICO";
            estadisticas.agregar(Estadistica.ARMADURA, 100);
            estadisticas.agregar(Estadistica.VIDA, 100);

        } else if (tipo == TipoItem.PENDIENTE) {
            nombre = "Pendiente UNICO";
            estadisticas.agregar(Estadistica.DANO_HECHIZO, 100);
            estadisticas.agregar(Estadistica.DANO_ATAQUE, 100);

        } else {
            throw new IllegalArgumentException("El tipo de item " + tipo + " no esta configurado para ser Unico.");
        }

        Item nuevoItem = new Item(this.contadorIds, tamanio[0], tamanio[1], tipo, rareza, nombre,
                estadisticas);
        return nuevoItem;
    }

    /**
     * Determina la cantidad de atributos extra que recibira un item en base a su
     * rareza.
     * 
     * @param rareza La rareza del item generado.
     * 
     * @return Un entero con la cantidad de estadisticas adicionales a modificar.
     */
    private int obtenerCantidadEstadisticas(RarezaItem rareza) {
        if (rareza == RarezaItem.RARO) {
            return this.generadorRandom.nextInt(5, 6); // TENEMOS SOLO 5 ESTADISTICAS

        } else if (rareza == RarezaItem.MAGICO) {
            return this.generadorRandom.nextInt(1, 3);

        } else {
            return 0;
        }
    }

    /**
     * Verifica si una estadistica ya fue seleccionada previamente durante la
     * generacion.
     * 
     * @param estadisticasAModificar Arreglo booleano con el estado de cada
     *                               estadistica.
     * 
     * @param indice                 El indice de la estadistica a verificar.
     * 
     * @return true si la estadistica ya fue usada, false en caso contrario.
     */
    private boolean fueUsadaLaEstadistica(boolean[] estadisticasAModificar, int indice) {
        return estadisticasAModificar[indice];
    }

    /**
     * Calcula el valor numerico que tomara un atributo adicional de forma
     * aleatoria.
     * 
     * @param rareza La rareza del item que define los rangos de valores.
     * @return El valor entero calculado para la estadistica.
     */
    private int generarValorEstadistica(RarezaItem rareza) {
        int valor;

        if (rareza == RarezaItem.MAGICO) {
            valor = this.generadorRandom.nextInt(5, 11);

        } else if (rareza == RarezaItem.RARO) {
            valor = this.generadorRandom.nextInt(11, 16);

        } else {
            valor = 0;
        }

        return valor;

    }

    /**
     * Construye el nombre del item combinando su tipo con un sufijo aleatorio.
     * 
     * @param tipo El tipo de item del cual se basara el nombre.
     * 
     * @return Una cadena de texto con el nombre final del item.
     * 
     * @throws IllegalArgumentException Si el tipo no tiene un nombre configurado.
     */
    private String generarNombre(TipoItem tipo) {
        String nombre = "";
        String[] arregloDeSufijos = this.diccionarioSufijos.obtenerValor(tipo);
        int indice = this.generadorRandom.nextInt(0, arregloDeSufijos.length);
        String sufijo = arregloDeSufijos[indice];

        if (tipo == TipoItem.ANILLO) {
            nombre = "Anillo " + sufijo;

        } else if (tipo == TipoItem.ARMADURA) {
            nombre = "Armadura " + sufijo;

        } else if (tipo == TipoItem.ARMA_ARCO) {
            nombre = "Arco " + sufijo;

        } else if (tipo == TipoItem.ARMA_ESPADA) {
            nombre = "Espada " + sufijo;

        } else if (tipo == TipoItem.ARMA_MAZA) {
            nombre = "Maza " + sufijo;

        } else if (tipo == TipoItem.ARMA_VARITA) {
            nombre = "Varita " + sufijo;

        } else if (tipo == TipoItem.BOTAS) {
            nombre = "Botas " + sufijo;

        } else if (tipo == TipoItem.CASCO) {
            nombre = "Casco " + sufijo;

        } else if (tipo == TipoItem.CINTURON) {
            nombre = "Cinturon " + sufijo;

        } else if (tipo == TipoItem.GUANTES) {
            nombre = "Guantes " + sufijo;

        } else if (tipo == TipoItem.PENDIENTE) {
            nombre = "Pendiente " + sufijo;

        } else {
            throw new IllegalArgumentException("El tipo de item " + tipo + " no tiene un nombre base configurado.");
        }
        return nombre;
    }

    /**
     * Define las dimensiones fisicas que ocupara el item dentro del inventario.
     * 
     * @param tipo El tipo de item a dimensionar.
     * 
     * @return Un arreglo de dos enteros representando [ancho, alto].
     */
    private Integer[] generarTamanio(TipoItem tipo) {
        Integer[] tamanio = new Integer[2];

        if (tipo == TipoItem.ANILLO) {
            tamanio[0] = 1;
            tamanio[1] = 1;

        } else if (tipo == TipoItem.ARMADURA) {
            tamanio[0] = 2;
            tamanio[1] = 3;

        } else if (tipo == TipoItem.ARMA_ARCO) {
            tamanio[0] = 2;
            tamanio[1] = 3;

        } else if (tipo == TipoItem.ARMA_ESPADA) {
            tamanio[0] = 2;
            tamanio[1] = 4;

        } else if (tipo == TipoItem.ARMA_MAZA) {
            tamanio[0] = 2;
            tamanio[1] = 3;

        } else if (tipo == TipoItem.ARMA_VARITA) {
            tamanio[0] = 1;
            tamanio[1] = 3;

        } else if (tipo == TipoItem.BOTAS) {
            tamanio[0] = 2;
            tamanio[1] = 2;

        } else if (tipo == TipoItem.CASCO) {
            tamanio[0] = 2;
            tamanio[1] = 2;

        } else if (tipo == TipoItem.CINTURON) {
            tamanio[0] = 3;
            tamanio[1] = 1;

        } else if (tipo == TipoItem.GUANTES) {
            tamanio[0] = 2;
            tamanio[1] = 2;

        } else if (tipo == TipoItem.PENDIENTE) {
            tamanio[0] = 1;
            tamanio[1] = 1;

        } else {
            tamanio[0] = 0;
            tamanio[1] = 0;
        }

        return tamanio;
    }

    /**
     * Agrega el atributo principal al item dependiendo de su tipo.
     * 
     * @param estadisticas El diccionario de estadisticas del item siendo
     *                     generado.
     * 
     * @param tipo         El tipo de item para determinar su atributo
     *                     caracteristico.
     */
    private void generarEstadisticasBase(Diccionario<Estadistica, Integer> estadisticas, TipoItem tipo) {

        if (tipo == TipoItem.ANILLO) {
            estadisticas.agregar(Estadistica.VIDA, VIDA_BASE);

        } else if (tipo == TipoItem.ARMADURA) {
            estadisticas.agregar(Estadistica.ARMADURA, ARMADURA_BASE);

        } else if (tipo == TipoItem.ARMA_ARCO) {
            estadisticas.agregar(Estadistica.DANO_ATAQUE, DANO_ATAQUE_BASE);

        } else if (tipo == TipoItem.ARMA_ESPADA) {
            estadisticas.agregar(Estadistica.DANO_ATAQUE, DANO_ATAQUE_BASE);

        } else if (tipo == TipoItem.ARMA_MAZA) {
            estadisticas.agregar(Estadistica.DANO_ATAQUE, DANO_ATAQUE_BASE);

        } else if (tipo == TipoItem.ARMA_VARITA) {
            estadisticas.agregar(Estadistica.DANO_HECHIZO, DANO_HECHIZO_BASE);

        } else if (tipo == TipoItem.BOTAS) {
            estadisticas.agregar(Estadistica.ARMADURA, ARMADURA_BASE);

        } else if (tipo == TipoItem.CASCO) {
            estadisticas.agregar(Estadistica.ARMADURA, ARMADURA_BASE);

        } else if (tipo == TipoItem.CINTURON) {
            estadisticas.agregar(Estadistica.ARMADURA, ARMADURA_BASE);

        } else if (tipo == TipoItem.GUANTES) {
            estadisticas.agregar(Estadistica.ARMADURA, ARMADURA_BASE);

        } else if (tipo == TipoItem.PENDIENTE) {
            estadisticas.agregar(Estadistica.MANA, MANA_BASE);

        } else {
            return;
        }

    }

    /**
     * Inicializa el diccionario interno con los conjuntos de sufijos
     * posibles para generar los nombres de cada tipo de item.
     */
    private void cargarDiccionarioSufijos() {
        TipoItem[] tiposDeObjeto = TipoItem.values();
        int cantidadEstadisticasTotales = tiposDeObjeto.length;
        Diccionario<TipoItem, String[]> dic = new Diccionario<>(cantidadEstadisticasTotales);
        this.diccionarioSufijos = dic;

        this.diccionarioSufijos.agregar(TipoItem.ARMA_ESPADA,
                new String[] { "de Hierro", "Oxidada", "Dentada" });

        this.diccionarioSufijos.agregar(TipoItem.ARMA_ARCO,
                new String[] { "de Caza", "Largo", "Compuesto" });

        this.diccionarioSufijos.agregar(TipoItem.ARMA_MAZA,
                new String[] { "de Guerra", "Pesada", "con Pinchos" });

        this.diccionarioSufijos.agregar(TipoItem.ARMA_VARITA,
                new String[] { "de Aprendiz", "de Cristal", "Arcana" });

        this.diccionarioSufijos.agregar(TipoItem.ARMADURA,
                new String[] { "de Cuero", "de Placas", "Pesada" });

        this.diccionarioSufijos.agregar(TipoItem.CASCO,
                new String[] { "de Hierro", "Templario", "Abollado" });

        this.diccionarioSufijos.agregar(TipoItem.BOTAS,
                new String[] { "de Cuero", "Ligeras", "Reforzadas" });

        this.diccionarioSufijos.agregar(TipoItem.GUANTES,
                new String[] { "de Seda", "de Batalla", "Desgastados" });

        this.diccionarioSufijos.agregar(TipoItem.ANILLO,
                new String[] { "de Rubi", "del Ocultista", "de Oro" });

        this.diccionarioSufijos.agregar(TipoItem.PENDIENTE,
                new String[] { "de Zafiro", "Maldito", "de Plata" });

        this.diccionarioSufijos.agregar(TipoItem.CINTURON,
                new String[] { "de Cuero", "de Cadenas" });

    }

}