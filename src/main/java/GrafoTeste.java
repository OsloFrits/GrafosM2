import java.util.ArrayList;

public class GrafoTeste {

    public static Lista criar(boolean direcionado) {

        Lista lista = new Lista(new ArrayList<>(), direcionado);

        Node a = new Node(1, "A");
        Node b = new Node(2, "B");
        Node c = new Node(3, "C");
        Node d = new Node(4, "D");
        Node e = new Node(5, "E");
        Node f = new Node(6, "F");
        Node g = new Node(7, "G");

        lista.addVertice(a);
        lista.addVertice(b);
        lista.addVertice(c);
        lista.addVertice(d);
        lista.addVertice(e);
        lista.addVertice(f);
        lista.addVertice(g);

        lista.addAresta(a, b, 7);
        lista.addAresta(a, c, 4);
        lista.addAresta(a, d, 9);

        lista.addAresta(b, c, 2);
        lista.addAresta(b, e, 6);

        lista.addAresta(c, d, 3);
        lista.addAresta(c, f, 5);

        lista.addAresta(d, e, 1);
        lista.addAresta(e, g, 4);

        return lista;
    }
}