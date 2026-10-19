import Coloracao.Dsatur;
import aplicacoes.Agm;
import aplicacoes.BuscaProfundidade;
import aplicacoes.Matrizes;
import aplicacoes.Roy;
import grafo.Grafo;
import grafo.Ligacao;
import grafo.Node;
import visual.GraphStreamView;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    // Grafo que o menu esta usando agora (e o nome dele, para mostrar no menu)
    static Grafo grafo = new Grafo(new ArrayList<>(), false);
    static String nomeGrafo = "vazio";

    // Resultado esperado dos arquivos de teste do DSATUR (numero minimo de cores conhecido)
    static final Map<String, Integer> CORES_ESPERADAS = new LinkedHashMap<>();
    static {
        CORES_ESPERADAS.put("dsatur_triangulo.json", 3);
        CORES_ESPERADAS.put("dsatur_k5_completo.json", 5);
        CORES_ESPERADAS.put("dsatur_bipartido.json", 2);
        CORES_ESPERADAS.put("dsatur_ciclo_impar.json", 3);
        CORES_ESPERADAS.put("dsatur_roda.json", 4);
        CORES_ESPERADAS.put("dsatur_petersen.json", 3);
        CORES_ESPERADAS.put("dsatur_desconexo.json", 3);
    }

    public static void main(String[] args) {

        int opcao;
        do {
            mostrarMenu();
            opcao = lerInteiro("Opcao: ");
            switch (opcao) {
                // ARQUIVO
                case 1 -> carregarArquivo();
                case 2 -> salvarArquivo();
                case 3 -> novoGrafo();
                case 4 -> {
                    grafo = GrafoTeste.criar(lerSimNao("Direcionado? (s/n): "));
                    nomeGrafo = "grafo de teste (codigo)";
                    System.out.println("Grafo de teste carregado.");
                }
                // EDITAR
                case 5 -> adicionarVertice();
                case 6 -> removerVertice();
                case 7 -> editarVertice();
                case 8 -> adicionarAresta();
                case 9 -> removerAresta();
                case 10 -> editarAresta();
                // VISUALIZAR
                case 11 -> mostrarGrafo();
                case 12 -> listarGrafo();
                case 13 -> mostrarMatrizes();
                // ALGORITMOS
                case 14 -> executarAGM();
                case 15 -> executarDFS(false);
                case 16 -> executarRoy();
                case 17 -> executarDsatur();
                // TESTES
                case 18 -> testarArquivo();
                case 19 -> bateriaDsatur();
                case 0 -> System.out.println("Programa encerrado.");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);

        scanner.close();
        System.exit(0); // fecha tambem as janelas do GraphStream que ficaram abertas
    }

    public static void mostrarMenu() {
        System.out.println("\n================ GRAFO ================");
        System.out.println("Atual: " + nomeGrafo);
        System.out.println("Vertices: " + grafo.getVertices()
                + " | Arestas: " + grafo.getLinhas()
                + " | " + (grafo.isDirecionada() ? "Direcionado" : "Nao direcionado"));
        System.out.println("---------------------------------------");
        System.out.println(" ARQUIVO");
        System.out.println("  1  - Carregar grafo de arquivo (pasta " + ArquivoGrafo.PASTA + ")");
        System.out.println("  2  - Salvar grafo em arquivo");
        System.out.println("  3  - Novo grafo vazio");
        System.out.println("  4  - Grafo de teste (do codigo)");
        System.out.println(" EDITAR");
        System.out.println("  5  - Adicionar vertice     8  - Adicionar aresta");
        System.out.println("  6  - Remover vertice       9  - Remover aresta");
        System.out.println("  7  - Editar vertice        10 - Editar aresta");
        System.out.println(" VISUALIZAR");
        System.out.println("  11 - Desenhar grafo");
        System.out.println("  12 - Listar vertices e arestas (texto)");
        System.out.println("  13 - Mostrar matrizes");
        System.out.println(" ALGORITMOS");
        System.out.println("  14 - AGM (Prim)");
        System.out.println("  15 - DFS (busca em profundidade)");
        System.out.println("  16 - Roy (componentes fortemente conexos)");
        System.out.println("  17 - DSATUR (coloracao)");
        System.out.println(" TESTES");
        System.out.println("  18 - Testar um arquivo (carrega e roda o algoritmo do nome)");
        System.out.println("  19 - Bateria de testes do DSATUR (esperado x obtido)");
        System.out.println("  0  - Sair");
        System.out.println("=======================================");
    }

    // =====================================================================
    // ARQUIVO
    // =====================================================================

    /** Mostra os .json da pasta e deixa escolher por numero ou nome. Devolve o nome escolhido. */
    public static String escolherArquivo() {
        List<String> arquivos;
        try {
            arquivos = ArquivoGrafo.listarArquivos();
        } catch (Exception e) {
            System.out.println("Nao foi possivel ler a pasta " + ArquivoGrafo.PASTA + ": " + e.getMessage());
            return null;
        }

        if (arquivos.isEmpty()) {
            System.out.println("Nenhum .json na pasta " + ArquivoGrafo.pasta());
        } else {
            System.out.println("Arquivos em " + ArquivoGrafo.pasta() + ":");
            for (int i = 0; i < arquivos.size(); i++) {
                System.out.printf("  %2d - %s%n", i + 1, arquivos.get(i));
            }
        }

        String escolha = lerTexto("Numero ou nome do arquivo: ");
        try {
            int numero = Integer.parseInt(escolha);
            if (numero >= 1 && numero <= arquivos.size()) return arquivos.get(numero - 1);
        } catch (NumberFormatException e) {
            // nao e numero: usa o texto como nome
        }
        return escolha;
    }

    /** Carrega o arquivo e troca o grafo atual. Devolve o nome carregado (ou null se falhou). */
    public static String carregarArquivo() {
        System.out.println("\n--- CARREGAR GRAFO ---");
        String nome = escolherArquivo();
        if (nome == null) return null;

        try {
            grafo = ArquivoGrafo.carregar(nome);
            nomeGrafo = nome;
            System.out.println("Grafo carregado (" + nome + "): " + grafo.getVertices() + " vertices, "
                    + grafo.getLinhas() + " arestas, "
                    + (grafo.isDirecionada() ? "direcionado." : "nao direcionado."));
            return nome;
        } catch (Exception e) {
            System.out.println("Nao foi possivel carregar: " + e.getMessage());
            return null;
        }
    }

    public static void salvarArquivo() {
        System.out.println("\n--- SALVAR GRAFO ---");
        String nome = lerTexto("Nome do arquivo (vai para a pasta " + ArquivoGrafo.PASTA + "): ");
        try {
            System.out.println("Grafo salvo em " + ArquivoGrafo.salvar(grafo, nome));
            nomeGrafo = nome;
        } catch (Exception e) {
            System.out.println("Nao foi possivel salvar: " + e.getMessage());
        }
    }

    public static void novoGrafo() {
        boolean direcionado = lerSimNao("O grafo sera direcionado? (s/n): ");
        grafo = new Grafo(new ArrayList<>(), direcionado);
        nomeGrafo = "novo (nao salvo)";
        System.out.println("Grafo vazio criado.");
    }

    // =====================================================================
    // EDITAR
    // =====================================================================

    public static void adicionarVertice() {
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

    public static void removerVertice() {
        System.out.println("\n--- REMOVER VERTICE ---");
        if (grafoVazio()) return;

        imprimirVertices();
        Node vertice = buscarVerticePorId(grafo, lerInteiro("ID: "));
        if (vertice == null) {
            System.out.println("Vertice nao encontrado.");
            return;
        }

        removerVerticeSeguro(grafo, vertice);
        System.out.println("Vertice removido.");
    }

    public static void editarVertice() {
        System.out.println("\n--- EDITAR VERTICE ---");
        if (grafoVazio()) return;

        imprimirVertices();
        Node vertice = buscarVerticePorId(grafo, lerInteiro("ID: "));
        if (vertice == null) {
            System.out.println("Vertice nao encontrado.");
            return;
        }

        System.out.println("Nome atual: " + vertice.getNome());
        vertice.setNome(lerTexto("Novo nome: "));
        System.out.println("Vertice alterado.");
    }

    public static void adicionarAresta() {
        System.out.println("\n--- ADICIONAR ARESTA ---");
        if (grafo.getVertices() < 1) {
            System.out.println("E necessario ter vertices antes.");
            return;
        }

        imprimirVertices();
        Node origem = buscarVerticePorId(grafo, lerInteiro("ID origem: "));
        if (origem == null) {
            System.out.println("Origem nao encontrada.");
            return;
        }

        Node destino = buscarVerticePorId(grafo, lerInteiro("ID destino: "));
        if (destino == null) {
            System.out.println("Destino nao encontrado.");
            return;
        }

        grafo.addAresta(origem, destino, lerInteiro("Peso: "));
        System.out.println("Aresta adicionada.");
    }

    public static void removerAresta() {
        System.out.println("\n--- REMOVER ARESTA ---");
        Ligacao ligacao = pedirAresta();
        if (ligacao == null) return;

        grafo.removeAresta(ligacao);
        System.out.println("Aresta removida.");
    }

    public static void editarAresta() {
        System.out.println("\n--- EDITAR ARESTA ---");
        Ligacao ligacao = pedirAresta();
        if (ligacao == null) return;

        System.out.println("Peso atual: " + ligacao.getPeso());
        ligacao.setPeso(lerInteiro("Novo peso: "));
        System.out.println("Aresta alterada.");
    }

    /** Pede origem e destino e devolve a aresta entre eles (ou null). */
    private static Ligacao pedirAresta() {
        if (grafo.getLinhas() == 0) {
            System.out.println("Nao existem arestas.");
            return null;
        }

        imprimirVertices();
        Node origem = buscarVerticePorId(grafo, lerInteiro("ID origem: "));
        Node destino = buscarVerticePorId(grafo, lerInteiro("ID destino: "));
        if (origem == null || destino == null) {
            System.out.println("Vertice nao encontrado.");
            return null;
        }

        Ligacao ligacao = buscarAresta(grafo, origem, destino);
        if (ligacao == null) System.out.println("Aresta nao encontrada.");
        return ligacao;
    }

    public static Node buscarVerticePorId(Grafo grafo, int id) {
        for (Node vertice : grafo.getListaDeAdjacencia()) {
            if (vertice.getId() == id) return vertice;
        }
        return null;
    }

    public static Ligacao buscarAresta(Grafo grafo, Node origem, Node destino) {
        for (Node vertice : grafo.getListaDeAdjacencia()) {
            for (Ligacao ligacao : vertice.getAdjacencia()) {
                boolean ida = ligacao.getOrigem() == origem && ligacao.getDestino() == destino;
                boolean volta = ligacao.getOrigem() == destino && ligacao.getDestino() == origem;
                if (ida || (!grafo.isDirecionada() && volta)) return ligacao;
            }
        }
        return null;
    }

    public static void removerVerticeSeguro(Grafo grafo, Node vertice) {
        List<Ligacao> linhasParaRemover = new ArrayList<>();

        for (Node atual : grafo.getListaDeAdjacencia()) {
            for (Ligacao ligacao : atual.getAdjacencia()) {
                if ((ligacao.getOrigem() == vertice || ligacao.getDestino() == vertice)
                        && !linhasParaRemover.contains(ligacao)) {
                    linhasParaRemover.add(ligacao);
                }
            }
        }

        for (Ligacao ligacao : linhasParaRemover) {
            ligacao.getOrigem().removeAdjacencia(ligacao);
            ligacao.getDestino().removeAdjacencia(ligacao);
        }

        grafo.getListaDeAdjacencia().remove(vertice);
        grafo.setVertices(grafo.getListaDeAdjacencia().size());
        grafo.setLinhas(Math.max(0, grafo.getLinhas() - linhasParaRemover.size()));
    }

    // =====================================================================
    // VISUALIZAR
    // =====================================================================

    public static void mostrarGrafo() {
        if (grafoVazio()) return;
        GraphStreamView.exibir(grafo);
    }

    public static void listarGrafo() {
        if (grafoVazio()) return;
        System.out.println("\n--- VERTICES ---");
        imprimirVertices();
        System.out.println("\n--- ARESTAS ---");
        grafo.mostrarLista();
    }

    public static void mostrarMatrizes() {
        if (grafoVazio()) return;

        System.out.println("\n--- MATRIZ DE ADJACENCIA ---");
        imprimirMatriz(Matrizes.matrizAdjacencia(grafo));

        System.out.println("\n--- MATRIZ DE INCIDENCIA ---");
        imprimirMatriz(Matrizes.matrizIncidencia(grafo));
    }

    public static void imprimirMatriz(int[][] matriz) {
        for (int[] linha : matriz) {
            for (int valor : linha) System.out.print(valor + "\t");
            System.out.println();
        }
    }

    /** Mostra "id - nome" de cada vertice (os ids de arquivo sao gerados 1, 2, 3...). */
    public static void imprimirVertices() {
        for (Node v : grafo.getListaDeAdjacencia()) {
            System.out.printf("  %3d - %s%n", v.getId(), v.getNome());
        }
    }

    // =====================================================================
    // ALGORITMOS
    // =====================================================================

    public static void executarAGM() {
        if (grafoVazio()) return;
        if (grafo.isDirecionada()) {
            System.out.println("AGM nao pode ser executada em grafo direcionado.");
            return;
        }

        Grafo agm = Agm.calcularAgm(grafo);
        System.out.println("AGM calculada. Peso total: " + agm.getPesoTotal());
        GraphStreamView.exibirAgm(grafo, agm);
    }

    /** @param automatico true = comeca pelo primeiro vertice sem perguntar (usado nos testes) */
    public static void executarDFS(boolean automatico) {
        if (grafoVazio()) return;
        System.out.println("\n--- BUSCA EM PROFUNDIDADE ---");

        Node inicio;
        if (automatico) {
            inicio = grafo.getListaDeAdjacencia().get(0);
            System.out.println("Comecando por: " + inicio.getNome());
        } else {
            imprimirVertices();
            inicio = buscarVerticePorId(grafo, lerInteiro("ID inicial: "));
            if (inicio == null) {
                System.out.println("Vertice nao encontrado.");
                return;
            }
        }

        int indice = grafo.getListaDeAdjacencia().indexOf(inicio);
        List<Node> ordem = BuscaProfundidade.executar(grafo, indice);
        System.out.println("DFS executada.");
        GraphStreamView.exibirDFS(grafo, ordem);
    }

    public static void executarRoy() {
        if (grafoVazio()) return;
        List<List<Node>> componentes = Roy.executar(grafo);
        System.out.println("Roy executado. Componentes encontrados: " + componentes.size());
        GraphStreamView.exibirRoy(grafo, componentes);
    }

    public static void executarDsatur() {
        if (grafoVazio()) return;

        // O enunciado pede o grafo ORIGINAL e o grafo COLORIDO: abre as duas janelas
        GraphStreamView.exibir(grafo);

        Dsatur dsatur = new Dsatur();
        dsatur.dsatur(null, grafo, null);
        int cores = dsatur.getQuantidadeCores();

        System.out.println("DSATUR executado. Cores usadas: " + cores
                + (coloracaoValida(grafo) ? " (coloracao valida)" : " (ATENCAO: vizinhos com a mesma cor!)"));
        for (Node v : grafo.getListaDeAdjacencia()) {
            System.out.println("  " + v.getNome() + " -> " + nomeCor(v.getCor()));
        }

        GraphStreamView.exibirColoracao(grafo, cores);
    }

    // =====================================================================
    // TESTES
    // =====================================================================

    /**
     * Carrega um arquivo e roda o algoritmo indicado pelo comeco do nome:
     * dsatur_ -> DSATUR | agm_ -> AGM | dfs_ -> DFS | roy_ -> Roy | outro -> so desenha
     */
    public static void testarArquivo() {
        System.out.println("\n--- TESTAR ARQUIVO ---");
        String nome = carregarArquivo();
        if (nome == null || grafoVazio()) return;

        String n = nome.toLowerCase();
        if (n.startsWith("dsatur_")) {
            executarDsatur();
            Integer esperado = CORES_ESPERADAS.get(n.endsWith(".json") ? n : n + ".json");
            if (esperado != null) System.out.println("Esperado: " + esperado + " cores.");
        } else if (n.startsWith("agm_")) {
            executarAGM();
        } else if (n.startsWith("dfs_")) {
            executarDFS(true);
        } else if (n.startsWith("roy_")) {
            executarRoy();
        } else {
            System.out.println("Nome sem prefixo de algoritmo: so desenhando o grafo.");
            mostrarGrafo();
        }
    }

    /** Roda o DSATUR em todos os dsatur_*.json e compara com o numero de cores esperado. */
    public static void bateriaDsatur() {
        System.out.println("\n--- BATERIA DE TESTES DO DSATUR ---");
        List<String> arquivos;
        try {
            arquivos = ArquivoGrafo.listarArquivos().stream()
                    .filter(a -> a.toLowerCase().startsWith("dsatur_")).toList();
        } catch (Exception e) {
            System.out.println("Nao foi possivel ler a pasta: " + e.getMessage());
            return;
        }
        if (arquivos.isEmpty()) {
            System.out.println("Nenhum dsatur_*.json na pasta " + ArquivoGrafo.PASTA + ".");
            return;
        }

        System.out.printf("%-26s %4s %4s %8s %8s %8s%n", "Arquivo", "V", "E", "Esperado", "Obtido", "Valida");
        int ok = 0;
        for (String nome : arquivos) {
            try {
                Grafo g = ArquivoGrafo.carregar(nome);
                Dsatur dsatur = new Dsatur();
                dsatur.dsatur(null, g, null);
                int obtido = dsatur.getQuantidadeCores();
                Integer esperado = CORES_ESPERADAS.get(nome);
                boolean valida = coloracaoValida(g);
                if (valida && (esperado == null || esperado == obtido)) ok++;
                System.out.printf("%-26s %4d %4d %8s %8d %8s%n", nome, g.getVertices(), g.getLinhas(),
                        esperado == null ? "-" : esperado, obtido, valida ? "sim" : "NAO");
            } catch (Exception e) {
                System.out.printf("%-26s ERRO: %s%n", nome, e.getMessage());
            }
        }
        System.out.println(ok + " de " + arquivos.size() + " testes OK.");
        System.out.println("(Use a opcao 18 para ver qualquer um deles desenhado.)");
    }

    /** Confere se nenhuma aresta liga dois vertices da mesma cor (lacos sao ignorados). */
    public static boolean coloracaoValida(Grafo g) {
        for (Node v : g.getListaDeAdjacencia()) {
            for (Ligacao l : v.getAdjacencia()) {
                Node a = l.getOrigem(), b = l.getDestino();
                if (a != b && a.getCor() != null && a.getCor().equals(b.getCor())) return false;
            }
        }
        return true;
    }

    private static String nomeCor(Color c) {
        if (c == null) return "sem cor";
        if (c.equals(Color.RED)) return "vermelho";
        if (c.equals(Color.BLUE)) return "azul";
        if (c.equals(Color.GREEN)) return "verde";
        if (c.equals(Color.YELLOW)) return "amarelo";
        if (c.equals(Color.ORANGE)) return "laranja";
        if (c.equals(Color.MAGENTA)) return "magenta";
        if (c.equals(Color.CYAN)) return "ciano";
        return String.format("rgb(%d,%d,%d)", c.getRed(), c.getGreen(), c.getBlue());
    }

    // =====================================================================
    // ENTRADA
    // =====================================================================

    private static boolean grafoVazio() {
        if (grafo.getVertices() == 0) {
            System.out.println("O grafo esta vazio.");
            return true;
        }
        return false;
    }

    public static boolean lerSimNao(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String r = scanner.nextLine().trim().toLowerCase();
            if (r.startsWith("s")) return true;
            if (r.startsWith("n")) return false;
            System.out.println("Responda s ou n.");
        }
    }

    public static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro.");
            }
        }
    }

    public static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) return texto;
            System.out.println("O texto nao pode ficar vazio.");
        }
    }
}