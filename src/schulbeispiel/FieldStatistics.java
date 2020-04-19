package schulbeispiel;

import java.awt.Color;
import java.util.HashMap;

/**
 * Diese Klasse sammelt und liefert statistische Daten über den Zustand eines
 * Feldes. Auf sehr flexible Weise: Es wird ein Zähler angelegt und gepflegt für
 * jede Objektklasse, die im Feld gefunden wird.
 * 
 * @author David J. Barnes und Michael Kölling
 * @version 2016.02.29
 */
public class FieldStatistics {
    // Die Zähler für die jeweiligen Akteurstypen (Fuchs, Hase, etc.)
    // in der Simulation.
    private HashMap<Class, Counter> counter;
    // Sind die Zählerstände momentan aktuell?
    private boolean counterNow;

    /**
     * Erzeuge ein FeldStatistik-Objekt.
     */
    public FieldStatistics() {
        // Wir legen eine Sammlung für die Zähler an, die wir für
        // die gefundenen Tierarten erzeugen.
        counter = new HashMap<>();
        counterNow = false;
    }

    /**
     * Liefere Informationen über die Bewohner im Feld.
     * 
     * @return eine Beschreibung, welche Tiere das Feld bevölkern
     */
    public String getResidentInfo(Field field) {
        StringBuffer buffer = new StringBuffer();
        if (!counterNow) {
            determineCountingNumbers(field);
        }
        for (Class key : counter.keySet()) {
            Counter info = counter.get(key);
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        return buffer.toString();
    }

    /**
     * Liefere die Anzahl der Individuen in der Population einer gegebenen Klasse.
     * 
     * @return einen int-Wert mit der Anzahl für diese Klasse
     */
    public int getResidentCount(Field field, Class key) {
        if (!counterNow) {
            determineCountingNumbers(field);
        }

        Counter info = counter.get(key);
        return info.getCount();
    }

    /**
     * Verwerfe alle bisher gesammelten Daten; setze alle Zähler auf null zurück.
     */
    public void reset() {
        counterNow = false;
        for (Class key : counter.keySet()) {
            Counter singleCounter = counter.get(key);
            singleCounter.reset();
        }
    }

    /**
     * Erhöhe den Zähler für eine Tierklasse.
     * 
     * @param organism Klasse der Tierart, für die erhöht werden soll
     */
    public void increaseCounter(Class organism) {
        Counter singleCounter = counter.get(organism);
        if (singleCounter == null) {
            // Wir haben noch keinen Zähler für
            // diese Spezies - also neu anlegen
            singleCounter = new Counter(organism.getName());
            counter.put(organism, singleCounter);
        }
        singleCounter.increase();
    }

    /**
     * Signalisiere, dass eine Tierzählung beendet ist.
     */
    public void countingFinished() {
        counterNow = true;
    }

    /**
     * Stelle fest, ob die Simulation noch aktiv ist, also ob sie weiterhin laufen
     * sollte.
     * 
     * @return true wenn noch mehr als eine Spezies lebt
     */
    public boolean istAktiv(Field field) {
        // Wie viele Zähler sind nicht null.
        int notNull = 0;
        if (!counterNow) {
            determineCountingNumbers(field);
        }
        for (Class key : counter.keySet()) {
            Counter info = counter.get(key);
            if (info.getCount() > 0) {
                notNull++;
            }
        }
        return notNull > 1;
    }

    /**
     * Erzeuge Zähler für die Anzahl der Füchse und Hasen. Diese werden nicht
     * ständig aktuell gehalten, während Füchse und Hasen in das Feld gesetzt
     * werden, sondern jeweils bei der Abfrage der Zählerstände berechnet.
     * 
     * @param field das Feld, für das die Statistik erstellt werden soll
     */
    private void determineCountingNumbers(Field field) {
        reset();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int column = 0; column < field.getWidth(); column++) {
                Object tier = field.getObjectAt(row, column);
                if (tier != null) {
                    increaseCounter(tier.getClass());
                }
            }
        }
        counterNow = true;
    }
}
