/**
 * Diese Klasse definiert Zähler für die Akteurstypen in einer Simulation. Ein
 * Zähler wird über einen Namen identifiziert und zählt, wieviele Akteure des
 * Typs innerhalb der Simulation jeweils existieren.
 * 
 * @author David J. Barnes und Michael Kölling
 * @version 2016.02.29
 */

package schulbeispiel;

public class Counter {
    // Ein Name für den Akteurstyp in dieser Simulation
    private String name;
    // Wie viele von diesem Typ existieren in der Simulation.
    private int counter;

    /**
     * Initialisiere mit dem Namen des Typs.
     * 
     * @param name ein Name, z.B. "Fuchs"
     */
    public Counter(String name) {
        this.name = name;
        counter = 0;
    }

    /**
     * @return den Namen des Typs dieses Zählers
     */
    public String getName() {
        return name;
    }

    /**
     * @return den aktuellen Zählerstand dieses Typs
     */
    public int getCount() {
        return counter;
    }

    /**
     * Erhöhe diesen Zähler um eins.
     */
    public void increase() {
        counter++;
    }

    /**
     * Setze diesen Zähler auf null zurück.
     */
    public void reset() {
        counter = 0;
    }
}
