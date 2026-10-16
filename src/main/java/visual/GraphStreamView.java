package visual;

import grafo.Grafo;
import grafo.Ligacao;
import grafo.Node;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 * Adaptador: pega o SEU Grafo (lista de adjacencia com Node/Ligacao)
 * e monta um grafo do GraphStream so para exibir.
 * Os algoritmos continuam rodando no seu Grafo; o GraphStream so desenha.
 */
public class GraphStreamView {

    // ===== AJUSTE AQUI =====
    /** Tamanho da janela em pixels. */
    public static int LARGURA_JANELA = 1200;
    public static int ALTURA_JANELA  = 900;
    /** Distancia entre os vertices: 1 = padrao do GraphStream; 2, 3... = mais afastados. */
    public static double ESPACAMENTO = 2.0;
    // =======================

    private static final String CSS =
            "graph { padding: 40px; fill-color: white; }" +
                    "node { size-mode: fit; padding: 9px; fill-color: #4a90d9; stroke-mode: plain; stroke-color: #1f4e79;" +
                    "       text-size: 16px; text-color: white; text-style: bold; text-alignment: center; }" +
                    "node.claro { text-color: black; }" +
                    "node.inicio { fill-color: #e53935; }" +
                    "node.caminho { fill-color: #e53935; stroke-color: #7f0000; }" +
                    "edge { size: 2px; fill-color: #555; text-size: 14px; text-background-mode: plain;" +
                    "       text-background-color: white; arrow-size: 12px, 6px; }" +
                    "edge.destaque { size: 5px; fill-color: #e53935; }" +
                    "edge.apagada  { fill-color: #ccc; text-color: #bbb; }";

    static {
        // Precisa vir antes de qualquer display()
        System.setProperty("org.graphstream.ui", "swing");
    }

    // ------------------------------------------------------------------
    // Conversao Grafo -> GraphStream
    // ------------------------------------------------------------------

    /**
     * Converte o seu Grafo para um Graph do GraphStream.
     * @param usarCores se true, pinta os vertices com Node.getCor() (resultado do DSATUR)
     */
    public static Graph converter(Grafo grafo, String titulo, boolean usarCores) {
        // MultiGraph aceita arestas paralelas e lacos (loop), que o seu Grafo tambem aceita
        Graph g = new MultiGraph(titulo);
        g.setStrict(false);
        g.setAttribute("ui.stylesheet", CSS);
        g.setAttribute("ui.quality");
        g.setAttribute("ui.antialias");
        g.setAttribute("ui.title", titulo);

        for (Node v : grafo.getListaDeAdjacencia()) {
            org.graphstream.graph.Node n = g.addNode(String.valueOf(v.getId()));
            n.setAttribute("ui.label", v.getNome());
            if (usarCores && v.getCor() != null) {
                pintar(n, v.getCor());
            }
        }

        // Em grafo nao direcionado a MESMA Ligacao esta na lista dos dois nos,
        // entao guardo as ja adicionadas para nao desenhar duas vezes.
        Set<Ligacao> vistas = Collections.newSetFromMap(new IdentityHashMap<>());
        int contador = 0;
        for (Node v : grafo.getListaDeAdjacencia()) {
            for (Ligacao l : v.getAdjacencia()) {
                if (!vistas.add(l)) continue;
                String origem = String.valueOf(l.getOrigem().getId());
                String destino = String.valueOf(l.getDestino().getId());
                if (g.getNode(origem) == null || g.getNode(destino) == null) continue;
                Edge e = g.addEdge("e" + (contador++), origem, destino, grafo.isDirecionada());
                e.setAttribute("ui.label", String.valueOf(l.getPeso()));
                e.setAttribute("layout.weight", ESPACAMENTO); // aresta "mais longa" no layout
            }
        }
        return g;
    }

    // ------------------------------------------------------------------
    // Exibicoes
    // ------------------------------------------------------------------

    /** Grafo "original": tudo azul, ignorando cores do DSATUR. */
    public static Viewer exibir(Grafo grafo) {
        return mostrar(converter(grafo, "Grafo", false));
    }

    /** Grafo colorido pelo DSATUR (usa a cor salva em cada Node). */
    public static Viewer exibirColoracao(Grafo grafo, int quantidadeCores) {
        return mostrar(converter(grafo, "DSATUR - " + quantidadeCores + " cores", true));
    }

    /**
     * Grafo original com um caminho em destaque (para o A*).
     * @param caminho vertices na ordem: origem, ..., destino
     */
    public static Viewer exibirCaminho(Grafo grafo, List<Node> caminho, int custoTotal) {
        Graph g = converter(grafo, "Caminho minimo - custo " + custoTotal, false);
        for (Node v : caminho) {
            g.getNode(String.valueOf(v.getId())).setAttribute("ui.class", "caminho");
        }
        for (int i = 0; i + 1 < caminho.size(); i++) {
            Edge e = arestaEntre(g, caminho.get(i), caminho.get(i + 1), grafo.isDirecionada());
            if (e != null) e.setAttribute("ui.class", "destaque");
        }
        return mostrar(g);
    }

    /** Grafo original com as arestas da AGM destacadas em vermelho. */
    public static Viewer exibirAgm(Grafo original, Grafo agm) {
        Graph g = converter(original, "AGM - peso total: " + agm.getPesoTotal(), false);
        for (int i = 0; i < g.getEdgeCount(); i++) {
            Edge e = g.getEdge(i);
            boolean naAgm = existeAresta(agm,
                    Integer.parseInt(e.getSourceNode().getId()),
                    Integer.parseInt(e.getTargetNode().getId()));
            e.setAttribute("ui.class", naAgm ? "destaque" : "apagada");
        }
        return mostrar(g);
    }

    /** Ordem de visita da DFS no rotulo de cada vertice (ex.: "A (1)"). */
    public static Viewer exibirDFS(Grafo grafo, List<Node> ordem) {
        Graph g = converter(grafo, "DFS", false);
        for (int i = 0; i < ordem.size(); i++) {
            Node v = ordem.get(i);
            org.graphstream.graph.Node n = g.getNode(String.valueOf(v.getId()));
            n.setAttribute("ui.label", v.getNome() + " (" + (i + 1) + ")");
            if (i == 0) n.setAttribute("ui.class", "inicio");
        }
        return mostrar(g);
    }

    /** Pinta cada componente fortemente conexo (Roy) com uma cor. */
    public static Viewer exibirRoy(Grafo grafo, List<List<Node>> componentes) {
        Graph g = converter(grafo, "Roy - " + componentes.size() + " componentes", false);
        for (int c = 0; c < componentes.size(); c++) {
            float matiz = (float) c / Math.max(1, componentes.size());
            Color cor = Color.getHSBColor(matiz, 0.65f, 0.85f);
            for (Node v : componentes.get(c)) {
                pintar(g.getNode(String.valueOf(v.getId())), cor);
            }
        }
        return mostrar(g);
    }

    // ------------------------------------------------------------------
    // Auxiliares
    // ------------------------------------------------------------------

    private static Viewer mostrar(Graph g) {
        SwingViewer viewer = new SwingViewer(g, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();                       // posiciona os vertices sozinho
        ViewPanel painel = (ViewPanel) viewer.addDefaultView(false); // false = eu crio a janela

        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame(String.valueOf(g.getAttribute("ui.title")));
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // fechar nao encerra o programa
            janela.add(painel);
            janela.setSize(LARGURA_JANELA, ALTURA_JANELA);
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
        });
        return viewer;
    }

    private static void pintar(org.graphstream.graph.Node n, Color cor) {
        n.setAttribute("ui.style", "fill-color: " + hex(cor) + ";");
        // texto preto em cor clara (amarelo, ciano...), branco nas escuras
        double luminancia = 0.299 * cor.getRed() + 0.587 * cor.getGreen() + 0.114 * cor.getBlue();
        if (luminancia > 160) n.setAttribute("ui.class", "claro");
    }

    private static Edge arestaEntre(Graph g, Node a, Node b, boolean direcionado) {
        String ia = String.valueOf(a.getId()), ib = String.valueOf(b.getId());
        Edge melhor = null;
        for (int i = 0; i < g.getEdgeCount(); i++) {
            Edge e = g.getEdge(i);
            String s = e.getSourceNode().getId(), t = e.getTargetNode().getId();
            boolean liga = (s.equals(ia) && t.equals(ib)) || (!direcionado && s.equals(ib) && t.equals(ia));
            if (!liga) continue;
            // se houver arestas paralelas, destaca a de menor peso
            if (melhor == null || peso(e) < peso(melhor)) melhor = e;
        }
        return melhor;
    }

    private static int peso(Edge e) {
        return Integer.parseInt(String.valueOf(e.getAttribute("ui.label")));
    }

    private static boolean existeAresta(Grafo grafo, int idA, int idB) {
        for (Node v : grafo.getListaDeAdjacencia()) {
            for (Ligacao l : v.getAdjacencia()) {
                int o = l.getOrigem().getId(), d = l.getDestino().getId();
                if ((o == idA && d == idB) || (o == idB && d == idA)) return true;
            }
        }
        return false;
    }

    private static String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}