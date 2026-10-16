package visual;

import grafo.Grafo;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class JanelaGrafo {

    public static void exibir(Grafo grafo) {

        SwingUtilities.invokeLater(() -> {

            JFrame janela = new JFrame("Grafo");

            janela.setDefaultCloseOperation(
                    JFrame.DISPOSE_ON_CLOSE
            );

            janela.setSize(1200, 1000);

            janela.setLocationRelativeTo(null);

            PainelGrafo painel = new PainelGrafo(grafo);

            janela.add(painel);

            janela.setVisible(true);
        });
    }
}