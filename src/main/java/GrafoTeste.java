import grafo.Grafo;
import grafo.Node;

import java.util.ArrayList;

public class GrafoTeste {

    public static Grafo criar(boolean direcionado) {

        Grafo grafo = new Grafo(new ArrayList<>(), direcionado);

        Node a = new Node(1, "A");
        Node b = new Node(2, "B");
        Node c = new Node(3, "C");
        Node d = new Node(4, "D");
        Node e = new Node(5, "E");
        Node f = new Node(6, "F");
        Node g = new Node(7, "G");

        grafo.addVertice(a);
        grafo.addVertice(b);
        grafo.addVertice(c);
        grafo.addVertice(d);
        grafo.addVertice(e);
        grafo.addVertice(f);
        grafo.addVertice(g);

        grafo.addAresta(a, b, 7);
        grafo.addAresta(a, c, 4);
        grafo.addAresta(a, d, 9);

        grafo.addAresta(b, c, 2);
        grafo.addAresta(b, e, 6);

        grafo.addAresta(c, d, 3);
        grafo.addAresta(c, f, 5);

        grafo.addAresta(d, e, 1);
        grafo.addAresta(e, g, 4);

        return grafo;
    }
}