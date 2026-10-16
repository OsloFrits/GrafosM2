/*
package Coloracao;

import grafo.Grafo;
import grafo.Ligacao;
import grafo.Node;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Dsatur {
    private Node inicio;
    private Grafo grafo;
    private List<Color> cores;
    private List<Node> verticesVisitados = new java.util.ArrayList<>();
    private int maiorSaturacao=0,saturacao=0,maiorGrau=0;

    private Color[] corAleatoria = {//Do you remenber, the rainnnnnnnnnnnnnnnn *lagrimas masculinas*
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.ORANGE,
            Color.MAGENTA,
            Color.CYAN
    };

    public Node inicio(Grafo grafo){//Mds quem me dera ter a inteligencia, porem nasci burro demais pra fazer isso
        List<Node> listaDeAdjacencia = grafo.getListaDeAdjacencia();

        boolean semCores = true;//Serve so pra um coisa

        for (Node vertice : listaDeAdjacencia) {
            if (vertice.getCor() != null) {
                semCores = false;
                break;
            }
        }

        if (semCores) {
            for (Node vertice : listaDeAdjacencia) {
                if (vertice.getAdjacencia().size() > maiorGrau) {
                    maiorGrau = vertice.getAdjacencia().size();
                    inicio = vertice;
                }
            }
        }

        for(Node vertice : listaDeAdjacencia){
            for(Ligacao linha : vertice.getAdjacencia()){
                if (linha.getOrigem().getCor() != null && !cores.contains(linha.getOrigem().getCor())) {//Verifica se cor lgbt e se ela ja ta lista de cores vista
                    saturacao += 1;
                    cores.add(linha.getOrigem().getCor());
                }
            }
            if (saturacao > maiorSaturacao) {
                maiorSaturacao = saturacao;
                inicio = vertice;
            }
            saturacao = 0;
            cores.clear();
        }
        return inicio;
    }

    public void dsatur(Node node, Grafo grafo, Ligacao ligacao){//Nome feio da desgrama
        Node inicio = inicio(grafo);//Bizarro aaaaa freeeveeerr yeaaaahh *guitarrinhas tocandinho* tometometometometomeotometometomeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
        inicio.setCor(Color.RED);//esqueça tudoooo papai, free bird é bom dms

        verticesVisitados.add(inicio);

        while (verticesVisitados.size() < grafo.getVertices().size()) {
            for (Node verticeAtual : grafo.getListaDeAdjacencia()) {
                if (verticesVisitados.contains(verticeAtual)) {
                    continue;
                }
                for (Ligacao linha : verticeAtual.getAdjacencia()) {//Dà de fazer trabalho escutando musica de n, me distraio mt, slk
                    Node destino = linha.getDestino();
                    if (destino.getCor() == null) {//aiaiaiaiaiaiaiaiaaiaiiaiaiaiaiiaiaaiiaiaiaiaiaiaiaiai tometometometometometometmtometoetmtoetmotetomteomteotmtoteoteotmetometoetmeot
                        Color cor = corAleatoria[new Random().nextInt(corAleatoria.length)];//Oie como va? que ritmo! bueno para goza! oie como va? que ritmo! bueno para goza! *Sons de pesado de bateria*
                        destino.setCor(cor);
                    }
                    if (!verticesVisitados.contains(destino)) {
                        verticesVisitados.add(destino);//bateu uma fome
                    }
                }
                verticesVisitados.add(verticeAtual);//Quando vc se foi, choreiiiii, choreiiiii!! agr q voltou so ri, so ri, sou reiiiiiiiiii!!!!!!!
                break;
            }
        }
    }
}
*/
package Coloracao;

import grafo.Grafo;
import grafo.Ligacao;
import grafo.Node;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Dsatur {
    private Node inicio;
    private Grafo grafo;
    private List<Color> cores = new ArrayList<>();
    private List<Node> verticesVisitados = new ArrayList<>();
    private int maiorSaturacao=0,saturacao=0,maiorGrau=0;

    private Color[] corAleatoria = {//Do you remenber, the rainnnnnnnnnnnnnnnn *lagrimas masculinas*
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.ORANGE,
            Color.MAGENTA,
            Color.CYAN
    };

    public Node inicio(Grafo grafo){//Mds quem me dera ter a inteligencia, porem nasci burro demais pra fazer isso
        List<Node> listaDeAdjacencia = grafo.getListaDeAdjacencia();
        maiorSaturacao=0;
        maiorGrau=0;
        cores.clear();

        boolean semCores = true;//Serve so pra um coisa

        for (Node vertice : listaDeAdjacencia) {
            if (vertice.getCor() != null) {
                semCores = false;
                break;
            }
        }

        if (semCores) {
            for (Node vertice : listaDeAdjacencia) {
                if (vertice.getAdjacencia().size() > maiorGrau) {
                    maiorGrau = vertice.getAdjacencia().size();
                    inicio = vertice;
                }
            }
            return inicio;
        }

        for(Node vertice : listaDeAdjacencia){
            if (vertice.getCor() != null) continue;

            for(Ligacao linha : vertice.getAdjacencia()){
                Node vizinho = linha.getOrigem() == vertice ? linha.getDestino() : linha.getOrigem();
                if (vizinho.getCor() != null && !cores.contains(vizinho.getCor())) {//Verifica se cor lgbt e se ela ja ta lista de cores vista
                    saturacao += 1;
                    cores.add(vizinho.getCor());
                }
            }
            if (saturacao > maiorSaturacao || (saturacao == maiorSaturacao && vertice.getAdjacencia().size() > maiorGrau)) {
                maiorSaturacao = saturacao;
                maiorGrau = vertice.getAdjacencia().size();
                inicio = vertice;
            }
            saturacao = 0;
            cores.clear();
        }
        return inicio;
    }

    private Color escolherCor(Node node) {//Irei admitir, isso foi feito pela IA, estava considerando comer concreto. tava mt confuso e n tava conseguindo fazer
        for (Color cor : corAleatoria) {
            boolean disponivel = true;
            for (Ligacao linha : node.getAdjacencia()) {
                Node vizinho = linha.getOrigem() == node ? linha.getDestino() : linha.getOrigem();
                if (cor.equals(vizinho.getCor())) {
                    disponivel = false;
                    break;
                }
            }
            if (disponivel) return cor;
        }
        return null;
    }

    public void dsatur(Node node, Grafo grafo, Ligacao ligacao){//Nome feio da desgrama
        this.grafo = grafo;
        verticesVisitados.clear();
        cores.clear();
        maiorSaturacao=0;
        maiorGrau=0;

        for (Node vertice : grafo.getListaDeAdjacencia()) vertice.setCor(null);

        Node inicio = inicio(grafo);//Bizarro aaaaa freeeveeerr yeaaaahh *guitarrinhas tocandinho* tometometometometomeotometometomeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
        inicio.setCor(Color.RED);//esqueça tudoooo papai, free bird é bom dms
        verticesVisitados.add(inicio);

        while (verticesVisitados.size() < grafo.getListaDeAdjacencia().size()) {
            Node atual = inicio(grafo);
            if (atual == null) break;

            Color cor = escolherCor(atual);
            if (cor == null) {
                cor = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
            }
            atual.setCor(cor);

            if (!verticesVisitados.contains(atual)) verticesVisitados.add(atual);//Quando vc se foi, choreiiiii, choreiiiii!! agr q voltou so ri, so ri, sou reiiiiiiiiii!!!!!!!
        }
    }
}