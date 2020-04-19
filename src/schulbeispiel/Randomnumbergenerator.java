package schulbeispiel;

import java.util.Random;

/**
 * Steuerung der zufallsbasierten Elemente der Simulation. Indem dieser
 * gemeinsame Randomisierer mit festgelegtem Initialisierungswert (SEED) benutzt
 * wird, sind wiederholte Durchläufe exakt gleich (was beim Testen hilfreich
 * sein kann). Setzen Sie 'nutzeGemeinsam' auf 'false', um jedes Mal ein anderes
 * zufälliges Verhalten zu bekommen.
 * 
 * @author David J. Barnes und Michael Kölling
 * @version 2016.02.29
 */
public class Randomnumbergenerator {
    // Vorgabe für den SEED-Wert, der die Erzeugung der Zufallszahlen steuert
    private static final int SEED = 1111;
    // Ein gemeinsam genutztes Random-Objekt, falls benötigt
    private static final Random rand = new Random(SEED);
    // Bestimmt, ob ein gemeinsam genutzer Zufallsgenerator zur Verfügung gestellt
    // wird.
    private static final boolean useTogether = true;

    /**
     * Konstruktor für Objekte der Klasse Zufallssteuerung
     */
    public Randomnumbergenerator() {
    }

    /**
     * Liefert einen Zufallsgenerator.
     * 
     * @return ein Random-Objekt
     */
    public static Random getRNG() {
        if (useTogether) {
            return rand;
        } else {
            return new Random();
        }
    }

    /**
     * Setzt die Zufallssteuerung zurück. Hat keinen Effekt, wenn für die
     * Zufallssteuerung kein gemeinsam genutzer Zufallsgenerator verwendet wird.
     */
    public static void reset() {
        if (useTogether) {
            rand.setSeed(SEED);
        }
    }
}
