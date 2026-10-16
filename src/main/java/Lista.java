import java.util.List;

public class Lista { //Colocar arqui funções de procura, mostrar e codigos q se aplicam no grafo
    private List<Node> ListaDeAdjacencia;
    private int vertices =0, linhas=0;
    private int pesoTotal;
    private boolean direcionada;

    public Lista(List<Node> listaDeAdjacencia, boolean direcionada) {
        ListaDeAdjacencia = listaDeAdjacencia;
        this.direcionada = direcionada;
    }
    public void addVertice(Node vertice) {
        this.ListaDeAdjacencia.add(vertice);
        this.vertices++;
    }
    public void addAresta(Node origem, Node destino, int peso) {//Nao sei se isso precisa estar dentro de lista ou fora??
        Linha adjacencia = new Linha(origem, destino, peso);
        origem.addAdjacencia(adjacencia);
        if(!direcionada) {
            destino.addAdjacencia(adjacencia);
        }
        this.linhas++;
    }
    public void removeAresta(Linha linha) {//como caralhos faço isso???
        linha.getOrigem().removeAdjacencia(linha);
        if(!direcionada) {
            linha.getDestino().removeAdjacencia(linha);
        }
        this.linhas--;
    }
    public void removeVertice(Node vertice) {//vou remover o vertice e todas as arestas ligadas a ele, mas COMO??? so deus sabe, e eu n sou deus. Pq eu existokkkkkkkkkkk
        this.ListaDeAdjacencia.remove(vertice);
        for(Linha linha : vertice.getAdjacencia()) {
            if(!direcionada){
                vertice.removeAdjacencia(linha);
            }else{
                linha.getDestino().removeAdjacencia(linha);
            }
        }
        this.vertices--;
        this.linhas--;
    }
    public void mostrarLista() {
        String R;
        for(Node vertice : ListaDeAdjacencia) {
            String V = String.valueOf(vertice.getId());
         for(Linha linha : vertice.getAdjacencia()) { //usa getOutraPonta(vertice) em vez de getDestino(), para funcionar corretamente tanto direcionado quanto nao-direcionado
                String W = String.valueOf(linha.getDestino().getId());
                if(direcionada) {
                    R = "--" + linha.getPeso() +  "->";
                }else{
                    R = "--" + linha.getPeso() + "--";
                }
                System.out.println(V + " " + R + " " + W);
            }
        }
    }
    public boolean loop() {
        for(Node vertice : ListaDeAdjacencia) {
            for(Linha linha : vertice.getAdjacencia()) { //quando origem E destino sao o MESMO no (linha.getOrigem() == linha.getDestino()), nao quando o no atual apenas "e" o destino guardado na Linha
                if(linha.getOrigem() == linha.getDestino()) {
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
