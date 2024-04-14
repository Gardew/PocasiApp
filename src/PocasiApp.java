import javax.swing.*;

public class PocasiApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                // display our weather app gui
                new AplikacePocasiGui().setVisible(true);
            }
        });
    }
}
