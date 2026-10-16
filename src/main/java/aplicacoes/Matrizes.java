package aplicacoes;

import grafo.Ligacao;
import grafo.Grafo;
import grafo.Node;

import java.util.ArrayList;
import java.util.List;

public class Matrizes {

    public static int[][] matrizAdjacencia(Grafo grafo) {//Resolver mega problema, como n direcionada ambos tem referencia da linha. lgo vai dar bomba
        int [][] matriz = new int[grafo.getVertices()][grafo.getVertices()];
        List<Node> listaDeAdjacencia = grafo.getListaDeAdjacencia();

        for(Node vertice : listaDeAdjacencia){
            List<Ligacao> ligacao = vertice.getAdjacencia();
            int i = listaDeAdjacencia.indexOf(vertice);
            for(Ligacao linha : ligacao){
                 Node destino = linha.getOutraPonta(vertice); //Substituição do getDestino() por getOutraPonta()
                 int j = listaDeAdjacencia.indexOf(destino);
                 matriz[i][j] = 1;
                 if(!grafo.isDirecionada()){
                    matriz[j][i] = 1;
                 }
            }
        }
        mostraMatriz(matriz, listaDeAdjacencia, "Matriz de adjacencia");
        return matriz;
    }
    public static int[][] matrizIncidencia(Grafo grafo) {
        int[][] matriz = new int[grafo.getVertices()][grafo.getLinhas()];
        List<Node> listaDeAdjacencia = grafo.getListaDeAdjacencia();
        List<Ligacao> listaDeLigacoes = new ArrayList<>();

        for (Node vertice : listaDeAdjacencia) {
            for (Ligacao ligacao : vertice.getAdjacencia()) {
                if (!listaDeLigacoes.contains(ligacao)) {
                    listaDeLigacoes.add(ligacao);
                }
            }
        }
        for(Ligacao ligacao : listaDeLigacoes){
            int j = listaDeLigacoes.indexOf(ligacao);
            int origem = listaDeAdjacencia.indexOf(ligacao.getOrigem());
            int destino = listaDeAdjacencia.indexOf(ligacao.getDestino());

            matriz[origem][j] = 1;
            if(grafo.isDirecionada()){
                matriz[destino][j] = -1;
            }else{
                matriz[destino][j] = 1;
            }
        }
        mostraMatriz(matriz, listaDeAdjacencia, "Matriz de incidencia");
        return matriz;
    }

    public static void mostraMatriz(int[][] matriz, List<Node> listaDeAdjacencia, String tipo){//Ta natural quanto a luz do dia...
        System.out.println(tipo);
        for(int i=0; i<matriz.length; i++){
            for(int j=0; j<matriz[i].length; j++){
                System.out.print(matriz[i][j] + " ");
            }
            System.out.print("|\n");
        }
    }

}
