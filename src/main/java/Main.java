import Coloracao.Dsatur;
import aplicacoes.Agm;
import aplicacoes.BuscaProfundidade;
import aplicacoes.Matrizes;
import aplicacoes.Roy;
import grafo.Ligacao;
import grafo.Grafo;
import grafo.Node;
import visual.GraphStreamView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        char Resp='p';
        boolean direcionado;
        while(Resp != 'n' && Resp != 's') {
            System.out.println("O grafo será direcionado? (s/n)");
            String linha = scanner.nextLine().trim().toLowerCase();
            if (!linha.isEmpty()) Resp = linha.charAt(0);
        }
        if(Resp == 's'){
            direcionado = true;
        }else{
            direcionado = false;
        }
        Grafo grafo = new Grafo(new ArrayList<>(), direcionado);

        int opcao;
        do {
            mostrarMenu(grafo);
            opcao = lerInteiro("Opcao: ");
            switch (opcao) {
                case 1:
                    grafo = GrafoTeste.criar(direcionado);
                    System.out.println("Grafo de teste carregado.");
                    break;
                case 2:
                    adicionarVertice(grafo);
                    break;
                case 3:
                    removerVertice(grafo);
                    break;
                case 4:
                    editarVertice(grafo);
                    break;
                case 5:
                    adicionarAresta(grafo);
                    break;
                case 6:
                    removerAresta(grafo);
                    break;
                case 7:
                    editarAresta(grafo);
                    break;
                case 8:
                    mostrarGrafo(grafo);
                    break;
                case 9:
                    mostrarMatrizes(grafo);
                    break;
                case 10:
                    executarAGM(grafo);
                    break;
                case 11:
                    executarDFS(grafo);
                    break;
                case 12:
                    executarRoy(grafo);
                    break;
                case 13:
                    executarDsatur(grafo);
                    break;
                case 0:
                    System.out.println("Programa encerrado.");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);

        scanner.close();
    }

    public static void mostrarMenu(Grafo grafo) {

        System.out.println("\n========== GRAFO ==========");
        System.out.println("Vertices: " + grafo.getVertices()
                + " | Arestas: " + grafo.getLinhas());
        System.out.println("----------------------------");
        System.out.println("1  - Carregar grafo de teste");
        System.out.println("2  - Adicionar vertice");
        System.out.println("3  - Remover vertice");
        System.out.println("4  - Editar vertice");
        System.out.println("5  - Adicionar aresta");
        System.out.println("6  - Remover aresta");
        System.out.println("7  - Editar aresta");
        System.out.println("8  - Mostrar grafo");
        System.out.println("9  - Mostrar matrizes");
        System.out.println("10 - Executar AGM");
        System.out.println("11 - Executar DFS");
        System.out.println("12 - Executar Roy");
        System.out.println("13 - Executar DSATUR (coloracao)");
        System.out.println("0  - Sair");
        System.out.println("============================");
    }

    public static void adicionarVertice(Grafo grafo) {

        System.out.println("\n--- ADICIONAR VERTICE ---");

        int id = lerInteiro("ID: ");
        if (buscarVerticePorId(grafo, id) != null) {
            System.out.println("Ja existe um vertice com esse ID.");
            return;
        }

        String nome = lerTexto("Nome: ");
        grafo.addVertice(new Node(id, nome));
        System.out.println("Vertice adicionado.");
    }

    public static void removerVertice(Grafo grafo) {

        System.out.println("\n--- REMOVER VERTICE ---");

        if (grafo.getVertices() == 0) {
            System.out.println("O grafo esta vazio.");
            return;
        }

        int id = lerInteiro("ID: ");
        Node vertice = buscarVerticePorId(grafo, id);

        if (vertice == null) {
            System.out.println("Vertice nao encontrado.");
            return;
        }

        removerVerticeSeguro(grafo, vertice);
        System.out.println("Vertice removido.");
    }

    public static void editarVertice(Grafo grafo) {

        System.out.println("\n--- EDITAR VERTICE ---");

        if (grafo.getVertices() == 0) {
            System.out.println("O grafo esta vazio.");
            return;
        }

        int id = lerInteiro("ID: ");
        Node vertice = buscarVerticePorId(grafo, id);

        if (vertice == null) {
            System.out.println("Vertice nao encontrado.");
            return;
        }

        System.out.println("Nome atual: " + vertice.getNome());
        String novoNome = lerTexto("Novo nome: ");

        vertice.setNome(novoNome);
        System.out.println("Vertice alterado.");
    }

    public static void adicionarAresta(Grafo grafo) {

        System.out.println("\n--- ADICIONAR ARESTA ---");

        if (grafo.getVertices() < 2) {
            System.out.println("E necessario ter pelo menos 2 vertices.");
            return;
        }

        int idOrigem = lerInteiro("ID origem: ");
        Node origem = buscarVerticePorId(grafo, idOrigem);

        if (origem == null) {
            System.out.println("Origem nao encontrada.");
            return;
        }

        int idDestino = lerInteiro("ID destino: ");
        Node destino = buscarVerticePorId(grafo, idDestino);

        if (destino == null) {
            System.out.println("Destino nao encontrado.");
            return;
        }

        int peso = lerInteiro("Peso: ");
        grafo.addAresta(origem, destino, peso);
        System.out.println("Aresta adicionada.");
    }

    public static void removerAresta(Grafo grafo) {

        System.out.println("\n--- REMOVER ARESTA ---");

        if (grafo.getLinhas() == 0) {
            System.out.println("Nao existem arestas.");
            return;
        }

        int idOrigem = lerInteiro("ID origem: ");
        int idDestino = lerInteiro("ID destino: ");

        Node origem = buscarVerticePorId(grafo, idOrigem);
        Node destino = buscarVerticePorId(grafo, idDestino);

        if (origem == null || destino == null) {
            System.out.println("Vertice nao encontrado.");
            return;
        }

        Ligacao ligacao = buscarAresta(grafo, origem, destino);

        if (ligacao == null) {
            System.out.println("Aresta nao encontrada.");
            return;
        }

        grafo.removeAresta(ligacao);

        System.out.println("Aresta removida.");
    }

    public static void editarAresta(Grafo grafo) {

        System.out.println("\n--- EDITAR ARESTA ---");

        if (grafo.getLinhas() == 0) {
            System.out.println("Nao existem arestas.");
            return;
        }

        int idOrigem = lerInteiro("ID origem: ");
        int idDestino = lerInteiro("ID destino: ");

        Node origem = buscarVerticePorId(grafo, idOrigem);
        Node destino = buscarVerticePorId(grafo, idDestino);

        if (origem == null || destino == null) {
            System.out.println("Vertice nao encontrado.");
            return;
        }

        Ligacao ligacao = buscarAresta(grafo, origem, destino);

        if (ligacao == null) {
            System.out.println("Aresta nao encontrada.");
            return;
        }

        System.out.println("Peso atual: " + ligacao.getPeso());

        int peso = lerInteiro("Novo peso: ");

        ligacao.setPeso(peso);

        System.out.println("Aresta alterada.");
    }

    public static Node buscarVerticePorId(
            Grafo grafo,
            int id) {

        for (Node vertice :
                grafo.getListaDeAdjacencia()) {

            if (vertice.getId() == id) {
                return vertice;
            }
        }

        return null;
    }

    public static Ligacao buscarAresta(
            Grafo grafo,
            Node origem,
            Node destino) {

        for (Node vertice :
                grafo.getListaDeAdjacencia()) {

            for (Ligacao ligacao :
                    vertice.getAdjacencia()) {

                if (grafo.isDirecionada()) {

                    if (ligacao.getOrigem() == origem
                            && ligacao.getDestino() == destino) {

                        return ligacao;
                    }

                } else {

                    if ((ligacao.getOrigem() == origem
                            && ligacao.getDestino() == destino)
                            ||
                            (ligacao.getOrigem() == destino
                                    && ligacao.getDestino() == origem)) {

                        return ligacao;
                    }
                }
            }
        }

        return null;
    }

    public static void removerVerticeSeguro(
            Grafo grafo,
            Node vertice) {

        List<Ligacao> linhasParaRemover =
                new ArrayList<>();

        for (Node atual :
                grafo.getListaDeAdjacencia()) {

            for (Ligacao ligacao :
                    atual.getAdjacencia()) {

                if (ligacao.getOrigem() == vertice
                        || ligacao.getDestino() == vertice) {

                    if (!linhasParaRemover
                            .contains(ligacao)) {

                        linhasParaRemover.add(ligacao);
                    }
                }
            }
        }

        for (Ligacao ligacao :
                linhasParaRemover) {

            ligacao.getOrigem()
                    .removeAdjacencia(ligacao);

            ligacao.getDestino()
                    .removeAdjacencia(ligacao);
        }

        grafo.getListaDeAdjacencia()
                .remove(vertice);

        grafo.setVertices(
                grafo.getListaDeAdjacencia().size()
        );

        grafo.setLinhas(
                Math.max(
                        0,
                        grafo.getLinhas()
                                - linhasParaRemover.size()
                )
        );
    }

    public static void mostrarGrafo(Grafo grafo) {

        if (grafo.getVertices() == 0) {
            System.out.println("O grafo esta vazio.");
            return;
        }

        GraphStreamView.exibir(grafo);
    }

    public static void mostrarMatrizes(Grafo grafo) {

        if (grafo.getVertices() == 0) {
            System.out.println("O grafo esta vazio.");
            return;
        }

        System.out.println(
                "\n--- MATRIZ DE ADJACENCIA ---"
        );

        int[][] matrizAdjacencia =
                Matrizes.matrizAdjacencia(grafo);

        imprimirMatriz(
                matrizAdjacencia
        );

        System.out.println(
                "\n--- MATRIZ DE INCIDENCIA ---"
        );

        int[][] matrizIncidencia =
                Matrizes.matrizIncidencia(grafo);

        imprimirMatriz(
                matrizIncidencia
        );
    }

    public static void imprimirMatriz(
            int[][] matriz) {

        for (int[] linha : matriz) {

            for (int valor : linha) {
                System.out.print(
                        valor + "\t"
                );
            }

            System.out.println();
        }
    }

    public static void executarAGM(Grafo grafo) {

        if (grafo.getVertices() == 0) {
            System.out.println(
                    "O grafo esta vazio."
            );
            return;
        }

        if (grafo.isDirecionada()) {

            System.out.println(
                    "AGM nao pode ser executada em grafo direcionado."
            );

            return;
        }

        System.out.println(
                "Calculando AGM..."
        );

        Grafo agm =
                Agm.calcularAgm(grafo);

        System.out.println(
                "AGM calculada."
        );

        System.out.println(
                "Peso total da AGM: "
                        + agm.getPesoTotal()
        );

        GraphStreamView.exibirAgm(grafo, agm);
    }

    public static void executarDFS(Grafo grafo) {

        if (grafo.getVertices() == 0) {

            System.out.println(
                    "O grafo esta vazio."
            );

            return;
        }

        System.out.println(
                "\n--- BUSCA EM PROFUNDIDADE ---"
        );

        int id =
                lerInteiro("ID inicial: ");

        Node vertice =
                buscarVerticePorId(
                        grafo,
                        id
                );

        if (vertice == null) {

            System.out.println(
                    "Vertice nao encontrado."
            );

            return;
        }

        int indice =
                grafo.getListaDeAdjacencia()
                        .indexOf(vertice);

        List<Node> ordem =
                BuscaProfundidade.executar(
                        grafo,
                        indice
                );

        System.out.println(
                "DFS executada."
        );

        GraphStreamView.exibirDFS(grafo, ordem);
    }

    public static void executarRoy(Grafo grafo) {

        if (grafo.getVertices() == 0) {

            System.out.println(
                    "O grafo esta vazio."
            );

            return;
        }

        System.out.println(
                "Executando Roy..."
        );

        List<List<Node>> componentes =
                Roy.executar(grafo);

        System.out.println(
                "Roy executado. Componentes encontrados: "
                        + componentes.size()
        );

        GraphStreamView.exibirRoy(grafo, componentes);
    }

    public static void executarDsatur(Grafo grafo) {

        if (grafo.getVertices() == 0) {
            System.out.println("O grafo esta vazio.");
            return;
        }

        // O enunciado pede o grafo ORIGINAL e o grafo COLORIDO: abre as duas janelas
        GraphStreamView.exibir(grafo);

        Dsatur dsatur = new Dsatur();
        dsatur.dsatur(null, grafo, null);

        int cores = dsatur.getQuantidadeCores();
        System.out.println("DSATUR executado. Cores usadas: " + cores);
        for (Node v : grafo.getListaDeAdjacencia()) {
            System.out.println("  " + v.getNome() + " -> cor " + v.getCor());
        }

        GraphStreamView.exibirColoracao(grafo, cores);
    }

    public static int lerInteiro(
            String mensagem) {

        while (true) {

            System.out.print(
                    mensagem
            );

            String entrada =
                    scanner.nextLine();

            try {

                return Integer.parseInt(
                        entrada
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Digite um numero inteiro."
                );
            }
        }
    }

    public static String lerTexto(
            String mensagem) {

        while (true) {

            System.out.print(
                    mensagem
            );

            String texto =
                    scanner.nextLine()
                            .trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            System.out.println(
                    "O texto nao pode ficar vazio."
            );
        }
    }
}