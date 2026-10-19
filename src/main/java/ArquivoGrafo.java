import grafo.Grafo;
import grafo.Ligacao;
import grafo.Node;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Le e grava o grafo em JSON, no formato:
 *
 * {
 *   "orientado": false,
 *   "vertices": [ {"id": "Curitiba", "latitude": -25.42, "longitude": -49.27}, ... ],
 *   "arestas":  [ {"id": 1, "origem": "Curitiba", "destino": "Paranaguá", "peso": 90}, ... ],
 *   "proximo_id_aresta": 15
 * }
 *
 * O "id" do vertice no arquivo e um texto (nome). No programa ele vira o NOME do Node,
 * e o id numerico do Node e gerado em ordem: 1, 2, 3...
 * latitude/longitude podem ser null (grafos que nao sao mapa).
 */
public class ArquivoGrafo {

    /** Pasta onde ficam os grafos (na raiz do projeto, ao lado do pom.xml). */
    public static final String PASTA = "arquivos";

    /** Lista os .json da pasta "arquivos", em ordem alfabetica. Cria a pasta se nao existir. */
    public static List<String> listarArquivos() throws IOException {
        Path pasta = Path.of(PASTA);
        Files.createDirectories(pasta);
        try (var arquivos = Files.list(pasta)) {
            return arquivos.map(a -> a.getFileName().toString())
                    .filter(n -> n.toLowerCase().endsWith(".json"))
                    .sorted()
                    .toList();
        }
    }

    /**
     * Transforma o que o usuario digitou no caminho do arquivo:
     * "mapa" ou "mapa.json"  -> arquivos/mapa.json
     * caminho completo (C:\...\x.json) -> usa como esta
     */
    public static Path resolver(String nome) {
        String n = limparCaminho(nome);
        if (!n.toLowerCase().endsWith(".json")) n += ".json";
        Path p = Path.of(n);
        if (p.isAbsolute() || p.getParent() != null) return p; // ja veio com pasta
        return Path.of(PASTA, n);
    }

    // ------------------------------------------------------------------
    // LEITURA
    // ------------------------------------------------------------------

    /** Carrega pelo nome (procura na pasta "arquivos") ou por um caminho completo. */
    public static Grafo carregar(String nomeArquivo) throws IOException {
        Path arquivo = resolver(nomeArquivo);
        if (!Files.exists(arquivo)) throw new IOException("arquivo nao encontrado: " + arquivo.toAbsolutePath());
        String texto = Files.readString(arquivo, StandardCharsets.UTF_8);
        Object raiz = new LeitorJson(texto).ler();

        if (!(raiz instanceof Map)) throw new IOException("O arquivo nao e um objeto JSON { ... }");
        Map<?, ?> json = (Map<?, ?>) raiz;

        boolean orientado = Boolean.TRUE.equals(json.get("orientado"));
        Grafo grafo = new Grafo(new ArrayList<>(), orientado);

        // nome do vertice no arquivo -> Node criado
        Map<String, Node> porNome = new LinkedHashMap<>();
        int proximoId = 1;

        for (Object o : lista(json, "vertices")) {
            Map<?, ?> v = (Map<?, ?>) o;
            String nome = String.valueOf(v.get("id"));
            if (porNome.containsKey(nome)) throw new IOException("Vertice repetido: " + nome);

            Node node = new Node(proximoId++, nome);
            if (v.get("latitude") instanceof Number lat) node.setLatitude(lat.doubleValue());
            if (v.get("longitude") instanceof Number lon) node.setLongitude(lon.doubleValue());

            grafo.addVertice(node);
            porNome.put(nome, node);
        }

        for (Object o : lista(json, "arestas")) {
            Map<?, ?> a = (Map<?, ?>) o;
            Node origem = porNome.get(String.valueOf(a.get("origem")));
            Node destino = porNome.get(String.valueOf(a.get("destino")));
            if (origem == null || destino == null) {
                Object id = a.get("id") instanceof Number n ? n.intValue() : a.get("id");
                throw new IOException("Aresta " + id + " usa um vertice que nao existe: "
                        + a.get("origem") + " -> " + a.get("destino"));
            }
            int peso = a.get("peso") instanceof Number p ? p.intValue() : 1;
            grafo.addAresta(origem, destino, peso);
        }
        return grafo;
    }

    // ------------------------------------------------------------------
    // GRAVACAO (mesmo formato, para salvar grafos montados no menu)
    // ------------------------------------------------------------------

    /** Salva na pasta "arquivos" (ou no caminho completo, se for dado). Devolve onde salvou. */
    public static Path salvar(Grafo grafo, String nomeArquivo) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("    \"orientado\": ").append(grafo.isDirecionada()).append(",\n");

        sb.append("    \"vertices\": [\n");
        List<Node> vertices = grafo.getListaDeAdjacencia();
        for (int i = 0; i < vertices.size(); i++) {
            Node v = vertices.get(i);
            boolean semCoordenada = v.getLatitude() == 0 && v.getLongitude() == 0;
            sb.append("        {\"id\": ").append(texto(v.getNome()))
                    .append(", \"latitude\": ").append(semCoordenada ? "null" : String.valueOf(v.getLatitude()))
                    .append(", \"longitude\": ").append(semCoordenada ? "null" : String.valueOf(v.getLongitude()))
                    .append("}").append(i + 1 < vertices.size() ? "," : "").append("\n");
        }
        sb.append("    ],\n");

        // Em grafo nao direcionado a mesma Ligacao esta nos dois vertices: grava so uma vez
        List<Ligacao> arestas = new ArrayList<>();
        Set<Ligacao> vistas = Collections.newSetFromMap(new IdentityHashMap<>());
        for (Node v : vertices) {
            for (Ligacao l : v.getAdjacencia()) {
                if (vistas.add(l)) arestas.add(l);
            }
        }

        sb.append("    \"arestas\": [\n");
        for (int i = 0; i < arestas.size(); i++) {
            Ligacao l = arestas.get(i);
            sb.append("        {\"id\": ").append(i + 1)
                    .append(", \"origem\": ").append(texto(l.getOrigem().getNome()))
                    .append(", \"destino\": ").append(texto(l.getDestino().getNome()))
                    .append(", \"peso\": ").append(l.getPeso())
                    .append("}").append(i + 1 < arestas.size() ? "," : "").append("\n");
        }
        sb.append("    ],\n");
        sb.append("    \"proximo_id_aresta\": ").append(arestas.size() + 1).append("\n");
        sb.append("}\n");

        Path arquivo = resolver(nomeArquivo);
        if (arquivo.getParent() != null) Files.createDirectories(arquivo.getParent());
        Files.writeString(arquivo, sb.toString(), StandardCharsets.UTF_8);
        return arquivo;
    }

    // ------------------------------------------------------------------
    // Auxiliares
    // ------------------------------------------------------------------

    /** Tira aspas e espacos (o Windows coloca aspas no "Copiar como caminho"). */
    private static String limparCaminho(String caminho) {
        String c = caminho.trim();
        if (c.length() >= 2 && c.startsWith("\"") && c.endsWith("\"")) c = c.substring(1, c.length() - 1);
        return c;
    }

    private static List<?> lista(Map<?, ?> json, String chave) throws IOException {
        Object o = json.get(chave);
        if (!(o instanceof List)) throw new IOException("Faltou a lista \"" + chave + "\" no arquivo");
        return (List<?>) o;
    }

    private static String texto(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    /**
     * Leitor de JSON bem simples (objeto, lista, texto, numero, true/false/null).
     * Objeto vira Map, lista vira List, numero vira Double.
     */
    private static class LeitorJson {
        private final String s;
        private int pos = 0;

        LeitorJson(String s) {
            this.s = s;
        }

        Object ler() throws IOException {
            Object valor = valor();
            pularEspacos();
            if (pos < s.length()) erro("texto sobrando depois do JSON");
            return valor;
        }

        private Object valor() throws IOException {
            pularEspacos();
            if (pos >= s.length()) erro("arquivo terminou antes do esperado");
            char c = s.charAt(pos);
            if (c == '{') return objeto();
            if (c == '[') return listaJson();
            if (c == '"') return textoJson();
            if (s.startsWith("true", pos))  { pos += 4; return Boolean.TRUE; }
            if (s.startsWith("false", pos)) { pos += 5; return Boolean.FALSE; }
            if (s.startsWith("null", pos))  { pos += 4; return null; }
            return numero();
        }

        private Map<String, Object> objeto() throws IOException {
            Map<String, Object> mapa = new LinkedHashMap<>();
            pos++; // {
            pularEspacos();
            if (s.charAt(pos) == '}') { pos++; return mapa; }
            while (true) {
                pularEspacos();
                String chave = textoJson();
                pularEspacos();
                esperar(':');
                mapa.put(chave, valor());
                pularEspacos();
                if (s.charAt(pos) == ',') { pos++; continue; }
                esperar('}');
                return mapa;
            }
        }

        private List<Object> listaJson() throws IOException {
            List<Object> lista = new ArrayList<>();
            pos++; // [
            pularEspacos();
            if (s.charAt(pos) == ']') { pos++; return lista; }
            while (true) {
                lista.add(valor());
                pularEspacos();
                if (s.charAt(pos) == ',') { pos++; continue; }
                esperar(']');
                return lista;
            }
        }

        private String textoJson() throws IOException {
            esperar('"');
            StringBuilder sb = new StringBuilder();
            while (pos < s.length()) {
                char c = s.charAt(pos++);
                if (c == '"') return sb.toString();
                if (c == '\\') {
                    char e = s.charAt(pos++);
                    switch (e) {
                        case 'n': sb.append('\n'); break;
                        case 't': sb.append('\t'); break;
                        case 'r': sb.append('\r'); break;
                        case 'b': sb.append('\b'); break;
                        case 'f': sb.append('\f'); break;
                        case 'u': sb.append((char) Integer.parseInt(s.substring(pos, pos + 4), 16)); pos += 4; break;
                        default:  sb.append(e); // \" \\ \/
                    }
                } else {
                    sb.append(c);
                }
            }
            erro("texto sem aspas de fechamento");
            return null;
        }

        private Double numero() throws IOException {
            int inicio = pos;
            while (pos < s.length() && "+-0123456789.eE".indexOf(s.charAt(pos)) >= 0) pos++;
            if (inicio == pos) erro("valor invalido");
            return Double.parseDouble(s.substring(inicio, pos));
        }

        private void esperar(char c) throws IOException {
            if (pos >= s.length() || s.charAt(pos) != c) erro("esperava '" + c + "'");
            pos++;
        }

        private void pularEspacos() {
            // ﻿ = BOM que alguns editores do Windows colocam no inicio do arquivo
            while (pos < s.length() && (Character.isWhitespace(s.charAt(pos)) || s.charAt(pos) == '﻿')) pos++;
        }

        private void erro(String msg) throws IOException {
            int linha = 1;
            for (int i = 0; i < Math.min(pos, s.length()); i++) if (s.charAt(i) == '\n') linha++;
            throw new IOException("JSON invalido na linha " + linha + ": " + msg);
        }
    }
}