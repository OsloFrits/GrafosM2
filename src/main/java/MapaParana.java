import grafo.Grafo;
import grafo.Node;

import java.util.ArrayList;

/**
 * Mapa do enunciado do T2 (cidades do Parana) para testar o A*.
 * Coordenadas geograficas (latitude, longitude) do centro de cada cidade, em graus decimais.
 * Pesos das arestas = distancias rodoviarias (km) do mapa do enunciado.
 */
public class MapaParana {

    public static Grafo criar() {

        // Estradas valem nos dois sentidos -> grafo nao direcionado
        Grafo grafo = new Grafo(new ArrayList<>(), false);

        Node foz        = new Node(1,  "Foz do Iguacu",     -25.5163, -54.5854);
        Node cascavel   = new Node(2,  "Cascavel",          -24.9555, -53.4552);
        Node toledo     = new Node(3,  "Toledo",            -24.7246, -53.7412);
        Node umuarama   = new Node(4,  "Umuarama",          -23.7656, -53.3201);
        Node maringa    = new Node(5,  "Maringa",           -23.4205, -51.9333);
        Node londrina   = new Node(6,  "Londrina",          -23.3045, -51.1696);
        Node beltrao    = new Node(7,  "Francisco Beltrao", -26.0817, -53.0535);
        Node guarapuava = new Node(8,  "Guarapuava",        -25.3907, -51.4628);
        Node pontaGrossa= new Node(9,  "Ponta Grossa",      -25.0916, -50.1668);
        Node saoMateus  = new Node(10, "Sao Mateus do Sul", -25.8677, -50.3842);
        Node curitiba   = new Node(11, "Curitiba",          -25.4284, -49.2733);
        Node paranagua  = new Node(12, "Paranagua",         -25.5205, -48.5095);

        grafo.addVertice(foz);
        grafo.addVertice(cascavel);
        grafo.addVertice(toledo);
        grafo.addVertice(umuarama);
        grafo.addVertice(maringa);
        grafo.addVertice(londrina);
        grafo.addVertice(beltrao);
        grafo.addVertice(guarapuava);
        grafo.addVertice(pontaGrossa);
        grafo.addVertice(saoMateus);
        grafo.addVertice(curitiba);
        grafo.addVertice(paranagua);

        grafo.addAresta(maringa, londrina, 114);
        grafo.addAresta(maringa, umuarama, 190);
        grafo.addAresta(maringa, pontaGrossa, 314);
        grafo.addAresta(londrina, pontaGrossa, 273);
        grafo.addAresta(umuarama, toledo, 126);
        grafo.addAresta(toledo, cascavel, 50);
        grafo.addAresta(cascavel, foz, 143);
        grafo.addAresta(cascavel, beltrao, 186);
        grafo.addAresta(cascavel, guarapuava, 250);
        grafo.addAresta(guarapuava, pontaGrossa, 165);
        grafo.addAresta(pontaGrossa, curitiba, 114);
        grafo.addAresta(curitiba, paranagua, 90);
        grafo.addAresta(curitiba, saoMateus, 157);
        grafo.addAresta(saoMateus, beltrao, 354);

        return grafo;
    }
}
