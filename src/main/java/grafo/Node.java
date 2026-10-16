package grafo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id;
    private String nome;
    private Color cor;
    private List<Ligacao> adjacencia;

    public Node(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.adjacencia = new ArrayList<>();
    }

    public void addAdjacencia(Ligacao Adjacencia) {
        this.adjacencia.add(Adjacencia);
    }
    public void removeAdjacencia(Ligacao Adjacencia) {
        this.adjacencia.remove(Adjacencia);
    }

    public List<Ligacao> getAdjacencia() {
        return adjacencia;
    }

    public void setAdjacencia(List<Ligacao> adjacencia) {
        this.adjacencia = adjacencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public Color getCor() {
        return cor;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }
}
