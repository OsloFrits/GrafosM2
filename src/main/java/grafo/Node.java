package grafo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id;
    private String nome;
    private Color cor;
    private List<Ligacao> adjacencia;
    private double latitude;
    private double longitude;

    public Node(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.adjacencia = new ArrayList<>();
    }

    public Node(int id, String nome, double latitude, double longitude){
        this(id, nome);
        this.latitude = latitude;
        this.longitude = longitude;
    }

     public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
