import javax.swing.*;

public class PocasiApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // zobraz gui
            new AplikacePocasiGui().setVisible(true);
        });
    }
}
