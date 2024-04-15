import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;

public class AplikacePocasiGui extends JFrame {
    private JSONObject dataPocasi;

    public AplikacePocasiGui(){
        super("Počasí");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 400);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.getHSBColor(180,36,100));

        pridejKomponenty();
    }

    private void pridejKomponenty(){
        JTextField vyhledavaciPole = new JTextField("Zadej město");
        vyhledavaciPole.setBounds(15, 15, 351, 45);
        vyhledavaciPole.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(vyhledavaciPole);

        JLabel obrazekStavuPocasi = new JLabel(nactiObrazek("src/assets/zatazeno.png"));
        obrazekStavuPocasi.setBounds(400, 45, 450, 217);
        add(obrazekStavuPocasi);

        JLabel teplotniText = new JLabel("-- C");
        teplotniText.setBounds(400, 250, 450, 54);
        teplotniText.setFont(new Font("Dialog", Font.BOLD, 48));
        teplotniText.setHorizontalAlignment(SwingConstants.CENTER);
        add(teplotniText);

        JLabel popisStavuPocasi = new JLabel("--");
        popisStavuPocasi.setBounds(400, 305, 450, 36);
        popisStavuPocasi.setFont(new Font("Dialog", Font.PLAIN, 32));
        popisStavuPocasi.setHorizontalAlignment(SwingConstants.CENTER);
        add(popisStavuPocasi);

        JLabel obrazekVlhkosti = new JLabel(nactiObrazek("src/assets/vlhkost.png"));
        obrazekVlhkosti.setBounds(15, 150, 74, 66);
        add(obrazekVlhkosti);

        JLabel vlhkostText = new JLabel("<html><b>Vlhkost</b> --%</html>");
        vlhkostText.setBounds(95, 150, 150, 55);
        vlhkostText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(vlhkostText);

        JLabel obrazekRychlostiVetru = new JLabel(nactiObrazek("src/assets/rychlost.png"));
        obrazekRychlostiVetru.setBounds(10, 250, 74, 66);
        add(obrazekRychlostiVetru);

        JLabel rychlostVetruText = new JLabel("<html><b>Vítr </b> --km/h</html>");
        rychlostVetruText.setBounds(100, 250, 85, 55);
        rychlostVetruText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(rychlostVetruText);

        JButton tlacitkoVyhledat = new JButton(nactiObrazek("src/assets/hledej.png"));
        tlacitkoVyhledat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tlacitkoVyhledat.setBounds(375, 13, 47, 45);
        tlacitkoVyhledat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String zadanyText = odstranDiakritiku(vyhledavaciPole.getText());

                dataPocasi = AplikacePocasi.ziskatData(zadanyText);

                assert dataPocasi != null;
                String stavPocasi = (String) dataPocasi.get("stav_pocasi");

                switch(stavPocasi){
                    case "Jasno":
                        obrazekStavuPocasi.setIcon(nactiObrazek("src/assets/slunecno.png"));
                        break;
                    case "Zataženo":
                        obrazekStavuPocasi.setIcon(nactiObrazek("src/assets/zatazeno.png"));
                        break;
                    case "Déšť":
                        obrazekStavuPocasi.setIcon(nactiObrazek("src/assets/dest.png"));
                        break;
                    case "Sníh":
                        obrazekStavuPocasi.setIcon(nactiObrazek("src/assets/snih.png"));
                        break;
                }

                double teplota = (double) dataPocasi.get("teplota");
                teplotniText.setText(teplota + " C");

                popisStavuPocasi.setText(stavPocasi);

                long vlhkost = (long) dataPocasi.get("vlhkost");
                vlhkostText.setText("<html><b>Vlhkost</b> " + vlhkost + "%</html>");

                double rychlostVetru = (double) dataPocasi.get("rychlost_vetru");
                rychlostVetruText.setText("<html><b>Vítr </b> " + rychlostVetru + "km/h</html>");
            }
        });
        add(tlacitkoVyhledat);
    }

    public static String odstranDiakritiku(String text) {
        String s = text == null ? null :
                Normalizer.normalize(text, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        System.out.println(s);
        return s;

    }

    private ImageIcon nactiObrazek(String cesta){
        try{
            BufferedImage obrazek = ImageIO.read(new File(cesta));
            return new ImageIcon(obrazek);
        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Nepodařilo se najít zdroj");
        return null;
    }
}
