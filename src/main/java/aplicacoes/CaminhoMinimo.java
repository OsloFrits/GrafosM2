package aplicacoes;

import grafo.Grafo;
import grafo.Ligacao;
import grafo.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class CaminhoMinimo {

    private static final double RAIO_TERRA_KM = 6371.0;

    //Lista aberta: guarda o f assim que entra na fila
    private record Entrada(Node no, double f){}

    //Resultados
    private Map<Node, Double> h = new LinkedHashMap<>();
    private List<Node> caminho = new ArrayList<>();
    private List<String> passos = new ArrayList<>();
    private double custoTotal = -1;

    //Distancia em km entre dois vertices pela formula de Haversine, calculo do diabo
    public static double haversine (Node a, Node b){
        double lat1 = Math.toRadians(a.getLatitude());
        double lat2 = Math.toRadians(b.getLatitude());
        double dLat = lat2 - lat1;
        double dLon = Math.toRadians(b.getLongitude() - a.getLongitude());

        double x = Math.sin(dLat / 2) * Math.sin(dLat / 2) 
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon /2);
        return 2 * RAIO_TERRA_KM * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));
    }

    //Monta tabela h(n) de todos os vertices em relacao ao destino
    public Map<Node, Double> calcularHeuristica(Grafo grafo, Node destino) {
        h.clear();
        for (Node v : grafo.getListaDeAdjacencia()){
            h.put(v, haversine(v, destino));
        }
        return h;
    }

    //Executa o A* da origem ate o destino: TRue se encontrado
    public boolean executar(Grafo grafo, Node origem, Node destino){
        caminho.clear();
        passos.clear();
        custoTotal = -1;

        calcularHeuristica(grafo, destino);

        Map<Node, Double> g = new HashMap<>();
        Map<Node, Node> pai = new HashMap<>();
        Set<Node> fechados = new HashSet<>();
        PriorityQueue<Entrada> abertos = new PriorityQueue<>((e1, e2) -> Double.compare(e1.f(), e2.f()));

        g.put(origem, 0.0);
        abertos.add(new Entrada(origem, h.get(origem)));

        while(!abertos.isEmpty()){
            Node atual = abertos.poll().no(); //melhor nodo(Menor f)

            if (fechados.contains(atual)) continue;

            //A PriorityQueue do Java não deixa mudar o f de um elemento que já está dentro dela.
            //Quando o algoritmo acha um caminho melhor para uma cidade que já está na lista aberta, coloca a cidade de novo na lista com o novo f.
            //A entrada antiga continua lá dentro.
            passos.add(String.format("Fecha %-18s g=%7.1f  h=%7.1f  f=%7.1f",
                    atual.getNome(), g.get(atual), h.get(atual), g.get(atual) + h.get(atual)));

            if(atual == destino){
                custoTotal = g.get(destino);
                montarCaminho(pai, destino);
                return true;
            }

            fechados.add(atual);

            for (Ligacao ligacao : atual.getAdjacencia()){
                Node vizinho = grafo.isDirecionada() ? ligacao.getDestino() : ligacao.getOutraPonta(atual);

                if(vizinho == atual || fechados.contains(vizinho)) continue;
                
                double novoG = g.get(atual) + ligacao.getPeso();

                //So atualiza se achou caminho melhor até o vizinho
                if (novoG < g.getOrDefault(vizinho, Double.MAX_VALUE)){
                    g.put(vizinho, novoG);
                    pai.put(vizinho, atual);
                    abertos.add(new Entrada(vizinho, novoG + h.get(vizinho)));
                }
            }
        }
        return false; //Não tem caminho ou vazia
    }

    //Volta pelos pais do destino ate a origem e invrte
    private void montarCaminho(Map<Node, Node> pai, Node destino){
        Node atual = destino;
        while (atual != null){
            caminho.add(atual);
            atual = pai.get(atual);
        }
        Collections.reverse(caminho);
    }

    public Map<Node, Double> getH() {
        return h;
    }

    public List<Node> getCaminho(){
        return caminho;
    }

    public List<String> getPassos() {
        return passos;
    }

    public double getCustoTotal() {
        return custoTotal;
    }
}