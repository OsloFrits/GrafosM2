package grafo;

import java.util.List;

public class Grafo { //Colocar arqui funções de procura, mostrar e codigos q se aplicam no grafo
    private List<Node> ListaDeAdjacencia;
    private int vertices =0, linhas=0;
    private int pesoTotal;
    private boolean direcionada;

    public Grafo(List<Node> listaDeAdjacencia, boolean direcionada) {
        ListaDeAdjacencia = listaDeAdjacencia;
        this.direcionada = direcionada;
    }
    public void addVertice(Node vertice) {
        this.ListaDeAdjacencia.add(vertice);
        this.vertices++;
    }
    public void addAresta(Node origem, Node destino, int peso) {//Nao sei se isso precisa estar dentro de lista ou fora??
        Ligacao adjacencia = new Ligacao(origem, destino, peso);
        origem.addAdjacencia(adjacencia);
        if(!direcionada) {
            destino.addAdjacencia(adjacencia);
        }
        this.linhas++;
    }
    public void removeAresta(Ligacao ligacao) {//como caralhos faço isso???
        ligacao.getOrigem().removeAdjacencia(ligacao);
        if(!direcionada) {
            ligacao.getDestino().removeAdjacencia(ligacao);
        }
        this.linhas--;
    }
    public void removeVertice(Node vertice) {//vou remover o vertice e todas as arestas ligadas a ele, mas COMO??? so deus sabe, e eu n sou deus. Pq eu existokkkkkkkkkkk
        this.ListaDeAdjacencia.remove(vertice);
        for(Ligacao ligacao : vertice.getAdjacencia()) {
            if(!direcionada){
                vertice.removeAdjacencia(ligacao);
            }else{
                ligacao.getDestino().removeAdjacencia(ligacao);
            }
        }
        this.vertices--;
        this.linhas--;
    }
    public void mostrarLista() {
        String R;
        for(Node vertice : ListaDeAdjacencia) {
            String V = String.valueOf(vertice.getId());
         for(Ligacao ligacao : vertice.getAdjacencia()) { //usa getOutraPonta(vertice) em vez de getDestino(), para funcionar corretamente tanto direcionado quanto nao-direcionado
                String W = String.valueOf(ligacao.getDestino().getId());
                if(direcionada) {
                    R = "--" + ligacao.getPeso() +  "->";
                }else{
                    R = "--" + ligacao.getPeso() + "--";
                }
                System.out.println(V + " " + R + " " + W);
            }
        }
    }
    public boolean loop() {
        for(Node vertice : ListaDeAdjacencia) {
            for(Ligacao ligacao : vertice.getAdjacencia()) { //quando origem E destino sao o MESMO no (linha.getOrigem() == linha.getDestino()), nao quando o no atual apenas "e" o destino guardado na grafo.Linha
                if(ligacao.getOrigem() == ligacao.getDestino()) {
                    return true;
                }
            }
        }
        return false;
    }



    public List<Node> getListaDeAdjacencia() {
        return ListaDeAdjacencia;
    }

    public boolean isDirecionada() {
        return direcionada;
    }

    public int getVertices() {
        return vertices;
    }

    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    public int getLinhas() {
        return linhas;
    }

    public void setLinhas(int linhas) {
        this.linhas = linhas;
    }

    public int getPesoTotal() {
        return pesoTotal;
    }

    public void setPesoTotal(int pesoTotal) {
        this.pesoTotal = pesoTotal;
    }
}
