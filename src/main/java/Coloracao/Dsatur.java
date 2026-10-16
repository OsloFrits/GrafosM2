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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Dsatur {
    private Node inicio;
    private Grafo grafo;
    private List<Color> cores = new ArrayList<>();
    private List<Node> verticesVisitados = new ArrayList<>();
    private int maiorSaturacao=0,saturacao=0,maiorGrau=0;

    // Vizinhos de cada vertice nos DOIS sentidos (no direcionado a Ligacao A->B so fica na lista do A,
    // entao o B nao "enxergava" o A). Usar Set tambem ignora aresta repetida no calculo do grau.
    private Map<Node, Set<Node>> vizinhos = new HashMap<>();

    private Color[] corAleatoria = {//Do you remenber, the rainnnnnnnnnnnnnnnn *lagrimas masculinas*
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.ORANGE,
            Color.MAGENTA,
            Color.CYAN
    };

    // Monta o mapa de vizinhos percorrendo todas as arestas uma vez
    private void montarVizinhos(Grafo grafo) {//Thats girl is poisonnnnnnnnnnn
        vizinhos.clear();
        for (Node vertice : grafo.getListaDeAdjacencia()) {
            vizinhos.put(vertice, new LinkedHashSet<>());
        }
        for (Node vertice : grafo.getListaDeAdjacencia()) {
            for (Ligacao linha : vertice.getAdjacencia()) {
                Node a = linha.getOrigem();
                Node b = linha.getDestino();
                if (a == b) continue; // laço nao conta como vizinho
                if (vizinhos.containsKey(a) && vizinhos.containsKey(b)) {
                    vizinhos.get(a).add(b);
                    vizinhos.get(b).add(a);
                }
            }
        }
    }

    public Node inicio(Grafo grafo){//Mds quem me dera ter a inteligencia, porem nasci burro demais pra fazer isso
        List<Node> listaDeAdjacencia = grafo.getListaDeAdjacencia();
        inicio = null;          // zera pra nao devolver um vertice ja colorido de antes
        maiorSaturacao = -1;    // -1 pra que ate saturacao 0 / grau 0 (vertice isolado) seja escolhido
        maiorGrau = -1;
        cores.clear();

        boolean semCores = true;//Serve so pra um coisa

        for (Node vertice : listaDeAdjacencia) {
            if (vertice.getCor() != null) {
                semCores = false;
                break;
            }
        }
        // Primeiro passo do DSATUR: ninguem tem cor ainda, entao pega o vertice de maior grau
        if (semCores) {
            for (Node vertice : listaDeAdjacencia) {
                int grau = vizinhos.get(vertice).size();
                if (grau > maiorGrau) {
                    maiorGrau = grau;
                    inicio = vertice;
                }
            }
            return inicio;
        }
        for(Node vertice : listaDeAdjacencia){
            if (vertice.getCor() != null) continue;

            for(Node vizinho : vizinhos.get(vertice)){
                if (vizinho.getCor() != null && !cores.contains(vizinho.getCor())) {//Verifica se cor lgbt e se ela ja ta lista de cores vista
                    saturacao += 1;
                    cores.add(vizinho.getCor());
                }
            }
            int grau = vizinhos.get(vertice).size();
            if (saturacao > maiorSaturacao || (saturacao == maiorSaturacao && grau > maiorGrau)) {
                maiorSaturacao = saturacao;
                maiorGrau = grau;
                inicio = vertice;
            }
            saturacao = 0;
            cores.clear();
        }
        return inicio;
    }

    // Cor numero "indice": as 7 primeiras sao as da lista; depois disso gera cores diferentes
    // (angulo aureo no matiz), em vez de cor aleatoria que podia repetir uma ja usada.
    private Color corDoIndice(int indice) {
        if (indice < corAleatoria.length) return corAleatoria[indice];
        float matiz = (indice * 0.618034f) % 1f;
        return Color.getHSBColor(matiz, 0.75f, 0.9f);
    }

    private Color escolherCor(Node node) {//Irei admitir, isso foi feito pela IA, estava considerando comer concreto. tava mt confuso e n tava conseguindo fazer
        for (int i = 0; ; i++) {
            Color cor = corDoIndice(i);
            boolean disponivel = true;
            for (Node vizinho : vizinhos.get(node)) {
                if (cor.equals(vizinho.getCor())) {
                    disponivel = false;
                    break;
                }
            }
            if (disponivel) return cor;
        }
    }

    public void dsatur(Node node, Grafo grafo, Ligacao ligacao){//Nome feio da desgrama
        this.grafo = grafo;
        verticesVisitados.clear();
        cores.clear();
        maiorSaturacao=0;
        maiorGrau=0;

        if (grafo.getListaDeAdjacencia().isEmpty()) return; // grafo vazio, nada pra colorir

        for (Node vertice : grafo.getListaDeAdjacencia()) vertice.setCor(null);
        montarVizinhos(grafo);

        Node inicio = inicio(grafo);//Bizarro aaaaa freeeveeerr yeaaaahh *guitarrinhas tocandinho* tometometometometomeotometometomeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
        inicio.setCor(corDoIndice(0));//esqueça tudoooo papai, free bird é bom dms
        verticesVisitados.add(inicio);

        while (verticesVisitados.size() < grafo.getListaDeAdjacencia().size()) {
            Node atual = inicio(grafo);
            if (atual == null) break;

            Color cor = escolherCor(atual);
            atual.setCor(cor);

            verticesVisitados.add(atual);//Quando vc se foi, choreiiiii, choreiiiii!! agr q voltou so ri, so ri, sou reiiiiiiiiii!!!!!!!
        }
    }

    // Quantidade de cores diferentes usadas (bom pra mostrar na tela junto com o grafo colorido)
    public int getQuantidadeCores() {
        Set<Color> usadas = new LinkedHashSet<>();
        if (grafo == null) return 0;
        for (Node vertice : grafo.getListaDeAdjacencia()) {
            if (vertice.getCor() != null) usadas.add(vertice.getCor());
        }
        return usadas.size();
    }
}