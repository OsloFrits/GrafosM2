import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImagemGrafo {

    public static void salvar(
            Lista lista,
            String caminho) {

        int largura = 800;
        int altura = 550;

        BufferedImage imagem =
                new BufferedImage(
                        largura,
                        altura,
                        BufferedImage.TYPE_INT_RGB
                );

        Graphics2D g2 =
                imagem.createGraphics();

        PainelGrafo painel =
                new PainelGrafo(lista);

        painel.setSize(
                largura,
                altura
        );

        painel.paint(g2);

        g2.dispose();

        salvarImagem(
                imagem,
                caminho
        );
    }

    public static void salvarAgm(
            Lista lista,
            String caminho) {

        int largura = 800;
        int altura = 550;

        BufferedImage imagem =
                new BufferedImage(
                        largura,
                        altura,
                        BufferedImage.TYPE_INT_RGB
                );

        Graphics2D g2 =
                imagem.createGraphics();

        PainelGrafo painel =
                new PainelGrafo(
                        lista,
                        "AGM"
                );

        painel.setSize(
                largura,
                altura
        );

        painel.paint(g2);

        g2.dispose();

        salvarImagem(
                imagem,
                caminho
        );
    }

    public static void salvarDFS(
            Lista lista,
            List<Node> ordem,
            String caminho) {

        int largura = 800;
        int altura = 550;

        BufferedImage imagem =
                new BufferedImage(
                        largura,
                        altura,
                        BufferedImage.TYPE_INT_RGB
                );

        Graphics2D g2 =
                imagem.createGraphics();

        PainelGrafo painel =
                new PainelGrafo(
                        lista,
                        ordem,
                        true
                );

        painel.setSize(
                largura,
                altura
        );

        painel.paint(g2);

        g2.dispose();

        salvarImagem(
                imagem,
                caminho
        );
    }

    public static void salvarRoy(
            Lista lista,
            List<List<Node>> componentes,
            String caminho) {

        int largura = 800;
        int altura = 550;

        BufferedImage imagem =
                new BufferedImage(
                        largura,
                        altura,
                        BufferedImage.TYPE_INT_RGB
                );

        Graphics2D g2 =
                imagem.createGraphics();

        PainelGrafo painel =
                new PainelGrafo(
                        lista,
                        componentes
                );

        painel.setSize(
                largura,
                altura
        );

        painel.paint(g2);

        g2.dispose();

        salvarImagem(
                imagem,
                caminho
        );
    }

    private static void salvarImagem(
            BufferedImage imagem,
            String caminho) {

        try {
            File pasta =
                    new File("imgs");

            if (!pasta.exists()) {
                pasta.mkdirs();
            }

            File arquivo =
                    new File(
                            pasta,
                            caminho
                    );

            ImageIO.write(
                    imagem,
                    "png",
                    arquivo
            );

            System.out.println(
                    "Imagem salva em: "
                            + arquivo.getPath()
            );

        } catch (IOException e) {
            System.out.println(
                    "Erro ao salvar a imagem."
            );

            e.printStackTrace();
        }
    }
}