public class Linha {
    private Node origem, destino;
    private int peso;

    public Linha(Node origem, Node destino, int peso) {
        this.origem = origem;
        this.destino = destino;
        this.peso = peso;
    }

    public void setOrigem(Node origem) {
        this.origem = origem;
    }
    public void setDestino(Node destino) {
        this.destino = destino;
    }
    public Node getOrigem() {
        return origem;
    }
    public Node getDestino() {
        return destino;
    }
    public int getPeso() {
        return peso;
    }
    public void setPeso(int peso) {
        this.peso = peso;
    }
      public Node getOutraPonta (Node atual){
        if (atual == origem) {
            return destino;
        } else {
            return origem;
        }
    }
}
