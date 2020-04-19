package schulbeispiel;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * Ein einfacher Jäger-Beute-Simulator, basierend auf einem Feld mit Füchsen und
 * Hasen.
 * 
 * @author David J. Barnes und Michael Kölling
 * @version 2016.03.18
 */
public class Simulator {
    // Konstanten für Konfigurationsinformationen über die Simulation.
    // Die Standardbreite für ein Feld.
    private static final int DEFAULT_WIDTH = 120;
    // Die Standardtiefe für ein Feld.
    private static final int DEAFULT_DEPTH = 80;
    // Die Wahrscheinlichkeit für die Geburt eines Fuchses an
    // einer beliebigen Position im Feld.
    private static final double FUCHSGEBURT_WAHRSCHEINLICH = 0.02;
    // Die Wahrscheinlichkeit für die Geburt eines Hasen an
    // einer beliebigen Position im Feld.
    private static final double HASENGEBURT_WAHRSCHEINLICH = 0.08;

    // Listen der Tiere im Feld. Getrennte Listen vereinfachen das Iterieren.
    private List<Organism> organisms;
    // Der aktuelle Zustand des Feldes
    private Field field;
    // Der aktuelle Schritt der Simulation
    private int step;
    // Eine grafische Ansicht der Simulation
    private List<SimulationView> viewList;

    /**
     * Erzeuge ein Simulationsfeld mit einer Standardgröße.
     */
    public Simulator() {
        this(DEAFULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Erzeuge ein Simulationsfeld mit der gegebenen Größe.
     * 
     * @param depth die Tiefe des Feldes (muss größer als null sein)
     * @param width die Breite des Feldes (muss größer als null sein)
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("Size has to be greather than Zero.");
            System.out.println("Reverting to default values.");
            depth = DEAFULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        organisms = new ArrayList<>();
        field = new Field(depth, width);

        viewList = new ArrayList<>();

        SimulationView simulationView = new GridView(depth, width);
        simulationView.setColor(Hase.class, Color.ORANGE);
        simulationView.setColor(Fuchs.class, Color.BLUE);
        viewList.add(simulationView);

        simulationView = new DiagrammView(500, 150, 500);
        simulationView.setColor(Hase.class, Color.BLACK);
        simulationView.setColor(Fuchs.class, Color.RED);
        viewList.add(simulationView);

        // Einen gültigen Startzustand einnehmen.
        reset();
    }

    /**
     * Starte die Simulation vom aktuellen Zustand aus für einen längeren Zeitraum
     * (4000 Schritte).
     */
    public void starteLangeSimulation() {
        simulate(4000);
    }

    /**
     * Führe vom aktuellen Zustand aus die angegebene Anzahl an Simulationsschritten
     * durch. Brich vorzeitig ab, wenn die Simulation nicht mehr aktiv ist.
     * 
     * @param steps die Anzahl der auszuführenden Schritte
     */
    public void simulate(int steps) {
        simulate(steps, 0);
    }

    public void simulate(int steps, int delay) {
        for (int step = 1; step <= steps && viewList.get(0).isActive(field); step++) {
            simulateOneStep();
            if (delay > 0)
                delay(delay);
        }
    }

    /**
     * Führe einen einzelnen Simulationsschritt aus: Durchlaufe alle Feldpositionen
     * und aktualisiere den Zustand jedes Fuchses und Hasen.
     */
    public void simulateOneStep() {
        step++;

        // Platz für neugeborenes Tier anlegen.
        List<Organism> newOrganisms = new ArrayList<>();
        // Alle Tiere agieren lassen.
        for (Iterator<Organism> iter = organisms.iterator(); iter.hasNext();) {
            Organism organism = iter.next();
            organism.act(newOrganisms);
            if (!organism.isAlive()) {
                iter.remove();
            }
        }

        // Neugeborene Füchse und Hasen in die Hauptliste einfügen.
        organisms.addAll(newOrganisms);

        refreshViews();
    }

    /**
     * Setze die Simulation an den Anfang zurück.
     */
    public void reset() {
        step = 0;
        organisms.clear();
        for (SimulationView view : viewList) {
            view.reset();
        }

        populate();
        refreshViews();
    }

    /**
     * Aktualisiere alle bestehenden Ansichten.
     */
    private void refreshViews() {
        for (SimulationView view : viewList) {
            view.showStatus(step, field);
        }
    }

    /**
     * Bevölkere das Feld mit Füchsen und Hasen.
     */
    private void populate() {
        Random rand = Randomnumbergenerator.getRNG();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int column = 0; column < field.getWidth(); column++) {
                if (rand.nextDouble() <= FUCHSGEBURT_WAHRSCHEINLICH) {
                    Position position = new Position(row, column);
                    Fuchs fuchs = new Fuchs(true, field, position);
                    organisms.add(fuchs);
                } else if (rand.nextDouble() <= HASENGEBURT_WAHRSCHEINLICH) {
                    Position position = new Position(row, column);
                    Hase hase = new Hase(true, field, position);
                    organisms.add(hase);
                }
                // ansonsten die Position leer lassen
            }
        }
    }

    /**
     * Die Simulation für die angegebene Zeit anhalten.
     * 
     * @param ms die zu pausierende Zeit in Millisekunden
     */
    private void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            // aufwachen
        }
    }
}