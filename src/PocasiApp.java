import javax.swing.*;

public class PocasiApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AplikacePocasiGui().setVisible(true);
        });
    }
}
