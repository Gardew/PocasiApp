Aplikace Počasí
Aplikace Počasí je Java aplikace s GUI pro zobrazení aktuálních meteorologických informací. Umožňuje uživatelům zadat název lokality a získat aktuální počasí pro danou lokalitu.

Funkce
Aplikace umožňuje uživatelům vyhledat aktuální počasí pro zadanou lokalitu prostřednictvím vyhledávacího pole. 
Po zadání názvu lokality a stisknutí tlačítka "Vyhledat" aplikace získává a zobrazuje aktuální teplotu, stav počasí, vlhkost vzduchu a rychlost větru v grafické formě.

Komponenty GUI
Grafické uživatelské rozhraní aplikace obsahuje několik klíčových komponent:
Vyhledávací pole: Umožňuje uživatelům zadat název lokality pro vyhledání aktuálního počasí.
Obrázek stavu počasí: Zobrazuje vizuální reprezentaci aktuálního stavu počasí, jako je například oblačno, déšť, sníh atd.
Teplota: Zobrazuje aktuální teplotu v dané lokalitě.
Popis stavu počasí: Poskytuje textový popis aktuálního stavu počasí.
Vlhkost: Poskytuje textovou informaci o aktuální vlhkosti vzduchu v procentech.
Rychlost větru: Poskytuje textovou informaci o aktuální rychlosti větru v kilometrech za hodinu.

Instalace
Naklonujte tento repozitář na svůj počítač.

git clone https://github.com/Gardew/PocasiApp.git

Otevřete projekt ve vašem oblíbeném vývojovém prostředí (IDE) podporujícím Java.
Spusťte aplikaci pomocí hlavní třídy AplikacePocasiGui.

Použité technologie
Java
Swing (pro GUI)
JSON.simple (pro práci s JSON)
Java ImageIO (pro zpracování obrázků)
