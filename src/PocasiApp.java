import javax.swing.*;

public class PocasiApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){

            public void run(){
                // zobraz gui
                new AplikacePocasiGui().setVisible(true);
            }
        });
    }
}
