import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PainelGrafo extends JPanel {

    private Lista lista;
    private Map<Node, Point> posicoes = new HashMap<>();
    private List<List<Node>> componentes;
    private List<Node> ordemDFS;
    private String titulo;
    private final int RAIO_VERTICE = 25;

    public PainelGrafo(Lista lista) {
        this.lista = lista;
        this.titulo = "Grafo";
        setBackground(Color.WHITE);
        calcularPosicoes();
    }

    public PainelGrafo(Lista lista, String titulo) {
        this.lista = lista;
        this.titulo = titulo;
        setBackground(Color.WHITE);
        calcularPosicoes();
    }

    public PainelGrafo(Lista lista, List<Node> ordemDFS, boolean ehDFS) {
        this.lista = lista;
        this.ordemDFS = ordemDFS;
        this.titulo = "DFS";
        setBackground(Color.WHITE);
        calcularPosicoes();
    }

    public PainelGrafo(Lista lista, List<List<Node>> componentes) {
        this.lista = lista;
        this.componentes = componentes;
        this.titulo = "Roy";
        setBackground(Color.WHITE);
        calcularPosicoes();
    }

    private void calcularPosicoes() {
        posicoes.clear();

        List<Node> vertices = lista.getListaDeAdjacencia();

        if (vertices.isEmpty()) {
            return;
        }

        int largura = 800;
        int altura = 480;
        int centroX = largura / 2;
        int centroY = altura / 2;
        int centroRaio = Math.min(largura, altura) / 2 - 80;
        int quantidade = vertices.size();

        for (int i = 0; i < quantidade; i++) {
            double angulo = 2 * Math.PI * i / quantidade;

            int x = centroX
                    + (int) (centroRaio * Math.cos(angulo));

            int y = centroY
                    + (int) (centroRaio * Math.sin(angulo));

            posicoes.put(
                    vertices.get(i),
                    new Point(x, y)
            );
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        desenharTitulo(g2);
        desenharArestas(g2);
        desenharVertices(g2);

        if (ordemDFS != null) {
            desenharDFS(g2);
        }

        if (componentes != null) {
            desenharComponentes(g2);
        }
    }

    private void desenharTitulo(Graphics2D g2) {
        g2.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        g2.setColor(Color.BLACK);

        g2.drawString(
                titulo,
                20,
                30
        );

        if ("AGM".equals(titulo)) {
            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            16
                    )
            );

            g2.drawString(
                    "Peso total: " + lista.getPesoTotal(),
                    20,
                    55
            );
        }
    }

    private void desenharArestas(Graphics2D g2) {
        Set<Linha> arestasDesenhadas = new HashSet<>();

        for (Node origem : lista.getListaDeAdjacencia()) {
            Point pontoOrigem = posicoes.get(origem);

            if (pontoOrigem == null) {
                continue;
            }

            for (Linha linha : origem.getAdjacencia()) {
                if (arestasDesenhadas.contains(linha)) {
                    continue;
                }

                Node destino;

                if (linha.getOrigem() == origem) {
                    destino = linha.getDestino();
                } else {
                    destino = linha.getOrigem();
                }

                Point pontoDestino = posicoes.get(destino);

                if (pontoDestino == null) {
                    continue;
                }

                if (origem == destino) {
                    desenharLoop(
                            g2,
                            pontoOrigem,
                            linha
                    );
                } else {
                    desenharLinha(
                            g2,
                            pontoOrigem,
                            pontoDestino,
                            linha
                    );
                }

                arestasDesenhadas.add(linha);
            }
        }
    }

    private void desenharLinha(
            Graphics2D g2,
            Point origem,
            Point destino,
            Linha linha) {

        int x1 = origem.x;
        int y1 = origem.y;
        int x2 = destino.x;
        int y2 = destino.y;

        g2.setColor(Color.BLACK);

        g2.drawLine(
                x1,
                y1,
                x2,
                y2
        );

        int meioX = (x1 + x2) / 2;
        int meioY = (y1 + y2) / 2;

        g2.setColor(Color.RED);

        g2.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        g2.drawString(
                String.valueOf(linha.getPeso()),
                meioX,
                meioY
        );

        if (lista.isDirecionada()) {
            desenharSeta(
                    g2,
                    x1,
                    y1,
                    x2,
                    y2
            );
        }
    }

    private void desenharSeta(
            Graphics2D g2,
            int x1,
            int y1,
            int x2,
            int y2) {

        double angulo = Math.atan2(
                y2 - y1,
                x2 - x1
        );

        int tamanho = 10;

        int xPonta = x2
                - (int) (
                RAIO_VERTICE
                        * Math.cos(angulo)
        );

        int yPonta = y2
                - (int) (
                RAIO_VERTICE
                        * Math.sin(angulo)
        );

        int xA = xPonta
                - (int) (
                tamanho
                        * Math.cos(
                        angulo - Math.PI / 6
                )
        );

        int yA = yPonta
                - (int) (
                tamanho
                        * Math.sin(
                        angulo - Math.PI / 6
                )
        );

        int xB = xPonta
                - (int) (
                tamanho
                        * Math.cos(
                        angulo + Math.PI / 6
                )
        );

        int yB = yPonta
                - (int) (
                tamanho
                        * Math.sin(
                        angulo + Math.PI / 6
                )
        );

        g2.drawLine(
                xPonta,
                yPonta,
                xA,
                yA
        );

        g2.drawLine(
                xPonta,
                yPonta,
                xB,
                yB
        );
    }

    private void desenharLoop(
            Graphics2D g2,
            Point ponto,
            Linha linha) {

        int tamanho = 30;

        g2.setColor(Color.BLACK);

        g2.drawOval(
                ponto.x - tamanho / 2,
                ponto.y - RAIO_VERTICE - tamanho,
                tamanho,
                tamanho
        );

        g2.setColor(Color.RED);

        g2.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        g2.drawString(
                String.valueOf(linha.getPeso()),
                ponto.x + 15,
                ponto.y - 40
        );
    }

    private void desenharVertices(Graphics2D g2) {
        for (Node vertice : lista.getListaDeAdjacencia()) {
            Point ponto = posicoes.get(vertice);

            if (ponto == null) {
                continue;
            }

            g2.setColor(
                    obterCorVertice(vertice)
            );

            g2.fillOval(
                    ponto.x - RAIO_VERTICE,
                    ponto.y - RAIO_VERTICE,
                    RAIO_VERTICE * 2,
                    RAIO_VERTICE * 2
            );

            g2.setColor(Color.BLACK);

            g2.drawOval(
                    ponto.x - RAIO_VERTICE,
                    ponto.y - RAIO_VERTICE,
                    RAIO_VERTICE * 2,
                    RAIO_VERTICE * 2
            );

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            11
                    )
            );

            String id =
                    String.valueOf(vertice.getId());

            String nome =
                    vertice.getNome();

            int larguraId =
                    g2.getFontMetrics()
                            .stringWidth(id);

            int larguraNome =
                    g2.getFontMetrics()
                            .stringWidth(nome);

            int alturaTexto =
                    g2.getFontMetrics()
                            .getAscent();

            g2.drawString(
                    id,
                    ponto.x - larguraId / 2,
                    ponto.y - 2
            );

            g2.drawString(
                    nome,
                    ponto.x - larguraNome / 2,
                    ponto.y + alturaTexto
            );

            if (ordemDFS != null) {
                int indice =
                        ordemDFS.indexOf(vertice);

                if (indice >= 0) {
                    g2.setColor(Color.BLUE);

                    g2.setFont(
                            new Font(
                                    "Arial",
                                    Font.BOLD,
                                    13
                            )
                    );

                    g2.drawString(
                            String.valueOf(indice + 1),
                            ponto.x - 5,
                            ponto.y - RAIO_VERTICE - 8
                    );
                }
            }

            if (componentes != null) {
                for (int i = 0;
                     i < componentes.size();
                     i++) {

                    if (componentes.get(i).contains(vertice)) {
                        g2.setColor(Color.BLACK);

                        g2.setFont(
                                new Font(
                                        "Arial",
                                        Font.BOLD,
                                        12
                                )
                        );

                        g2.drawString(
                                "S" + (i + 1),
                                ponto.x - 8,
                                ponto.y + RAIO_VERTICE + 15
                        );

                        break;
                    }
                }
            }
        }
    }

    private Color obterCorVertice(Node vertice) {
        if (ordemDFS != null
                && ordemDFS.contains(vertice)) {

            return Color.GREEN;
        }

        if (componentes != null) {
            for (int i = 0;
                 i < componentes.size();
                 i++) {

                if (componentes.get(i).contains(vertice)) {
                    if (i % 3 == 0) {
                        return Color.CYAN;
                    }

                    if (i % 3 == 1) {
                        return Color.ORANGE;
                    }

                    return Color.PINK;
                }
            }
        }

        return Color.LIGHT_GRAY;
    }

    private void desenharDFS(Graphics2D g2) {
        if (ordemDFS == null
                || ordemDFS.isEmpty()) {

            return;
        }

        g2.setColor(Color.BLACK);

        g2.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        StringBuilder texto =
                new StringBuilder("Ordem: ");

        for (int i = 0;
             i < ordemDFS.size();
             i++) {

            texto.append(
                    ordemDFS
                            .get(i)
                            .getNome()
            );

            if (i < ordemDFS.size() - 1) {
                texto.append(" -> ");
            }
        }

        g2.drawString(
                texto.toString(),
                20,
                getHeight() - 20
        );
    }

    private void desenharComponentes(Graphics2D g2) {
        if (componentes == null) {
            return;
        }

        g2.setColor(Color.BLACK);

        g2.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        int y = 55;

        for (int i = 0;
             i < componentes.size();
             i++) {

            StringBuilder texto =
                    new StringBuilder(
                            "S" + (i + 1) + ": "
                    );

            for (int j = 0;
                 j < componentes.get(i).size();
                 j++) {

                texto.append(
                        componentes
                                .get(i)
                                .get(j)
                                .getNome()
                );

                if (j <
                        componentes.get(i).size() - 1) {

                    texto.append(", ");
                }
            }

            g2.drawString(
                    texto.toString(),
                    600,
                    y
            );

            y += 20;
        }
    }
}