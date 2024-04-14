import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class AplikacePocasi {

    // Získání dat o počasí pro zadanou lokalitu
    public static JSONObject ziskatData(String nazevLokality){
        // Získání souřadnic lokality pomocí geolokačního API
        JSONArray dataLokality = ziskatDataLokality(nazevLokality);

        // data o změpisné šířce a délce
        JSONObject lokalita = (JSONObject) dataLokality.get(0);
        double sirka = (double) lokalita.get("latitude");
        double delka = (double) lokalita.get("longitude");

        // Sestavení URL API požadavku s lokálními souřadnicemi
        String urlAdresa = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + sirka + "&longitude=" + delka +
                "&hourly=temperature_2m,relativehumidity_2m,weathercode,windspeed_10m&timezone=America%2FLos_Angeles";

        try{
            // Volání API a získání odpovědi
            HttpURLConnection spojeni = ziskatOdpovedAPI(urlAdresa);

            // Kontrola stavu odpovědi
            // 200 - úspěšné připojení
            if(spojeni.getResponseCode() != 200){
                System.out.println("Chyba: Nelze se připojit k API");
                return null;
            }

            // Uložení výsledných JSON dat
            StringBuilder vysledneJson = new StringBuilder();
            Scanner scanner = new Scanner(spojeni.getInputStream());
            while(scanner.hasNext()){
                // Čtení a ukládání do string builderu
                vysledneJson.append(scanner.nextLine());
            }

            // Zavření scanneru
            scanner.close();

            // Uzavření URL spojení
            spojeni.disconnect();

            // Procházení dat
            JSONParser parser = new JSONParser();
            JSONObject vysledneJsonObj = (JSONObject) parser.parse(String.valueOf(vysledneJson));

            // Získání hodinových dat
            JSONObject hodinove = (JSONObject) vysledneJsonObj.get("hourly");

            //  získat data aktuální hodin
            JSONArray cas = (JSONArray) hodinove.get("time");
            int index = najitIndexAktualnihoCasu(cas);

            // Získání teploty
            JSONArray dataTeploty = (JSONArray) hodinove.get("temperature_2m");
            double teplota = (double) dataTeploty.get(index);

            // Získání kódu počasí
            JSONArray kodPocasi = (JSONArray) hodinove.get("weathercode");
            String stavPocasi = prevodKoduPocasi((long) kodPocasi.get(index));

            // Získání vlhkosti
            JSONArray relativniVlhkost = (JSONArray) hodinove.get("relativehumidity_2m");
            long vlhkost = (long) relativniVlhkost.get(index);

            // Získání rychlosti větru
            JSONArray dataRychlostiVetru = (JSONArray) hodinove.get("windspeed_10m");
            double rychlostVetru = (double) dataRychlostiVetru.get(index);

            // Vytvoření objektu JSON s počasím
            JSONObject dataPocasi = new JSONObject();
            dataPocasi.put("teplota", teplota);
            dataPocasi.put("stav_pocasi", stavPocasi);
            dataPocasi.put("vlhkost", vlhkost);
            dataPocasi.put("rychlost_vetru", rychlostVetru);

            return dataPocasi;
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    // Získání geografických souřadnic pro zadaný název lokality
    public static JSONArray ziskatDataLokality(String nazevLokality){
        // Nahrazení mezer v názvu lokality znakem + dle formátu požadavku API
        nazevLokality = nazevLokality.replaceAll(" ", "+");

        // Sestavení URL API s parametrem názvu lokality
        String urlAdresa = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                nazevLokality + "&count=10&language=en&format=json";

        try{
            // Volání API a získání odpovědi
            HttpURLConnection spojeni = ziskatOdpovedAPI(urlAdresa);

            // Kontrola stavu odpovědi
            if(spojeni.getResponseCode() != 200){
                System.out.println("Chyba: Nelze se připojit k API");
                return null;
            }else{
                // Uložení výsledků API
                StringBuilder vysledneJson = new StringBuilder();
                Scanner scanner = new Scanner(spojeni.getInputStream());

                // Čtení a ukládání výsledných JSON dat do string builderu
                while(scanner.hasNext()){
                    vysledneJson.append(scanner.nextLine());
                }

                scanner.close();
                spojeni.disconnect();

                // Parsování JSON řetězce do objektu JSON
                JSONParser parser = new JSONParser();
                JSONObject vysledkyJsonObj = (JSONObject) parser.parse(String.valueOf(vysledneJson));

                // Získání seznamu dat o lokacích, která API vygenerovalo z názvu lokality
                JSONArray dataLokality = (JSONArray) vysledkyJsonObj.get("results");
                return dataLokality;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection ziskatOdpovedAPI(String urlAdresa){
        try{
            URL url = new URL(urlAdresa);
            HttpURLConnection spojeni = (HttpURLConnection) url.openConnection();

            // požadavek GET
            spojeni.setRequestMethod("GET");

            // Připojení k API
            spojeni.connect();
            return spojeni;
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }

    private static int najitIndexAktualnihoCasu(JSONArray seznamCasu){
        String aktualniCas = ziskatAktualniCas();

        // Procházení seznamu času a hledání shody s aktuálním časem
        for(int i = 0; i < seznamCasu.size(); i++){
            String cas = (String) seznamCasu.get(i);
            if(cas.equalsIgnoreCase(aktualniCas)){

                return i;
            }
        }

        return 0;
    }

    private static String ziskatAktualniCas(){
        // Získání aktuálního data a času
        LocalDateTime aktualniDatumACas = LocalDateTime.now();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        String formatovanyDatumACas = aktualniDatumACas.format(format);
        System.out.println(formatovanyDatumACas);

        return formatovanyDatumACas;
    }

    // Převod kódu počasí na čitelnější formu
    private static String prevodKoduPocasi(long kodPocasi){
        String stavPocasi = "";
        if(kodPocasi == 0L){
            stavPocasi = "Jasno";
        }else if(kodPocasi > 0L && kodPocasi <= 3L){
            stavPocasi = "Zatazeno";
        }else if((kodPocasi >= 51L && kodPocasi <= 67L)
                || (kodPocasi >= 80L && kodPocasi <= 99L)){
            stavPocasi = "Dest";
        }else if(kodPocasi >= 71L && kodPocasi <= 77L){
            stavPocasi = "Snih";
        }

        return stavPocasi;
    }
}