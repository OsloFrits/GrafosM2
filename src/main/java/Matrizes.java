import java.util.ArrayList;
import java.util.List;

public class Matrizes {

    public static int[][] matrizAdjacencia(Lista lista) {//Resolver mega problema, como n direcionada ambos tem referencia da linha. lgo vai dar bomba
        int [][] matriz = new int[lista.getVertices()][lista.getVertices()];
        List<Node> listaDeAdjacencia = lista.getListaDeAdjacencia();

        for(Node vertice : listaDeAdjacencia){
            List<Linha> ligacao = vertice.getAdjacencia();
            int i = listaDeAdjacencia.indexOf(vertice);
            for(Linha linha : ligacao){
                 Node destino = linha.getOutraPonta(vertice); //Substituição do getDestino() por getOutraPonta()
                 int j = listaDeAdjacencia.indexOf(destino);
                 matriz[i][j] = 1;
                 if(!lista.isDirecionada()){
                    matriz[j][i] = 1;
                 }
            }
        }
        mostraMatriz(matriz, listaDeAdjacencia, "Matriz de adjacencia");
        return matriz;
    }
    public static int[][] matrizIncidencia(Lista lista) {
        int[][] matriz = new int[lista.getVertices()][lista.getLinhas()];
        List<Node> listaDeAdjacencia = lista.getListaDeAdjacencia();
        List<Linha> listaDeLigacoes = new ArrayList<>();

        for (Node vertice : listaDeAdjacencia) {
            for (Linha linha : vertice.getAdjacencia()) {
                if (!listaDeLigacoes.contains(linha)) {
                    listaDeLigacoes.add(linha);
                }
            }
        }
        for(Linha linha : listaDeLigacoes){
            int j = listaDeLigacoes.indexOf(linha);
            int origem = listaDeAdjacencia.indexOf(linha.getOrigem());
            int destino = listaDeAdjacencia.indexOf(linha.getDestino());

            matriz[origem][j] = 1;
            if(lista.isDirecionada()){
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
