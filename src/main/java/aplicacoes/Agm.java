package aplicacoes;

import grafo.Ligacao;
import grafo.Grafo;
import grafo.Node;

import java.util.ArrayList;
import java.util.List;

public class Agm {

    public static Grafo calcularAgm(Grafo grafo) {

        Grafo agm =
                new Grafo(
                        new ArrayList<>(),
                        false
                );

        List<Node> listaDeAdjacencia =
                grafo.getListaDeAdjacencia();

        List<Node> verticesVisitados =
                new ArrayList<>();

        Ligacao menorLigacao = null;
        Node proximoVertice = null;
        Node origem = null;
        Node destino = null;

        int pesoMenor =
                Integer.MAX_VALUE;

        int pesoTotal = 0;

        Node verticeInicial =
                listaDeAdjacencia.get(0);

        verticesVisitados.add(
                verticeInicial
        );

        for (Node vertice :
                listaDeAdjacencia) {

            agm.addVertice(
                    new Node(
                            vertice.getId(),
                            vertice.getNome()
                    )
            );
        }

        while (
                verticesVisitados.size()
                        < listaDeAdjacencia.size()
        ) {

            pesoMenor =
                    Integer.MAX_VALUE;

            menorLigacao = null;
            proximoVertice = null;

            for (Node vertice :
                    verticesVisitados) {

                for (Ligacao ligacao :
                        vertice.getAdjacencia()) {

                    Node outroVertice;

                    if (ligacao.getOrigem() == vertice) {
                        outroVertice =
                                ligacao.getDestino();
                    } else {
                        outroVertice =
                                ligacao.getOrigem();
                    }

                    if (verticesVisitados.contains(
                            outroVertice)) {

                        continue;
                    }

                    if (ligacao.getPeso() < pesoMenor) {

                        pesoMenor =
                                ligacao.getPeso();

                        menorLigacao =
                                ligacao;

                        proximoVertice =
                                outroVertice;
                    }
                }
            }

            if (menorLigacao == null) {
                break;
            }

            for (Node vertice :
                    agm.getListaDeAdjacencia()) {

                if (
                        vertice.getId()
                                == menorLigacao
                                .getOrigem()
                                .getId()
                ) {

                    origem = vertice;
                }
            }

            for (Node vertice :
                    agm.getListaDeAdjacencia()) {

                if (
                        vertice.getId()
                                == menorLigacao
                                .getDestino()
                                .getId()
                ) {

                    destino = vertice;
                }
            }

            agm.addAresta(
                    origem,
                    destino,
                    menorLigacao.getPeso()
            );

            pesoTotal +=
                    menorLigacao.getPeso();

            verticesVisitados.add(
                    proximoVertice
            );
        }

        agm.setPesoTotal(
                pesoTotal
        );

        return agm;
    }
}