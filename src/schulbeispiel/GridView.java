package schulbeispiel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Die Ansicht zeigt für jede Position ein gefärbtes Rechteck, das den
 * jeweiligen Inhalt repräsentiert. Die Farben für die verschiedenen Tierarten
 * können mit der Methode setzeFarbe definiert werden.
 * 
 * @author Michael Kölling und David J. Barnes
 * @version 2016.03.18
 */
public class GridView extends JFrame implements SimulationView {
    // Die Farbe für leere Positionen
    private static final Color EMPTY_COLOR = Color.white;

    // Die Farbe für Objekte ohne definierte Farbe
    private static final Color UNDEF_COLOR = Color.gray;

    private final String COUNT_PREFIX = "Count: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel countLabel, population;
    private Fieldview fieldview;

    // Eine Map für die Farben der Simulationsteilnehmer
    private Map<Class<?>, Color> colors;
    // Ein Statistik-Objekt zur Berechnung und Speicherung
    // von Simulationsdaten
    private FieldStatistics stats;

    /**
     * Erzeuge eine Ansicht mit der gegebenen Breite und Höhe.
     * 
     * @param height die Höhe der Simulation
     * @param width  die Breite der Simulation
     */
    public GridView(int height, int width) {
        stats = new FieldStatistics();
        colors = new HashMap<>();

        setTitle("Simulation");
        countLabel = new JLabel(COUNT_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        setLocation(20, 50);

        fieldview = new Fieldview(height, width);

        Container content = getContentPane();
        content.add(countLabel, BorderLayout.NORTH);
        content.add(fieldview, BorderLayout.CENTER);
        content.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    /**
     * Definiere eine Farbe für die gegebene Tierklasse.
     * 
     * @param organism das Klassenobjekt der Tierklasse
     * @param color    die zu benutzende Farbe für die Tierklasse
     */
    public void setColor(Class organism, Color color) {
        colors.put(organism, color);
    }

    /**
     * @return die definierte Farbe für die gegebene Tierklasse
     */
    private Color getColor(Class organism) {
        Color farbe = colors.get(organism);
        if (farbe == null) {
            // für die gegebene Klasse ist keine Farbe definiert
            return UNDEF_COLOR;
        } else {
            return farbe;
        }
    }

    /**
     * Zeige den aktuellen Zustand des Feldes.
     * 
     * @param step  welcher Iterationsschritt ist dies?
     * @param field das Feld, das angezeigt werden soll
     */
    public void showStatus(int step, Field field) {
        if (!isVisible())
            setVisible(true);

        countLabel.setText(COUNT_PREFIX + step);
        stats.reset();

        fieldview.prepareDrawing();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int column = 0; column < field.getWidth(); column++) {
                Object organism = field.getObjectAt(row, column);
                if (organism != null) {
                    stats.increaseCounter(organism.getClass());
                    fieldview.drawMarker(column, row, getColor(organism.getClass()));
                } else {
                    fieldview.drawMarker(column, row, EMPTY_COLOR);
                }
            }
        }
        stats.countingFinished();

        population.setText(POPULATION_PREFIX + stats.getResidentInfo(field));
        fieldview.repaint();
    }

    /**
     * Entscheide, ob die Simulation weiterlaufen soll.
     * 
     * @return true wenn noch mehr als eine Spezies lebendig ist
     */
    public boolean isActive(Field field) {
        return stats.istAktiv(field);
    }

    /**
     * Bereite einen neuen Lauf vor.
     */
    public void reset() {
        stats.reset();
    }

    /**
     * Liefere eine grafische Ansicht eines rechteckigen Feldes. Dies ist eine
     * geschachtelte Klasse (eine Klasse, die innerhalb einer anderen Klasse
     * definiert ist), die eine eigene grafische Komponente für die
     * Benutzungsschnittstelle definiert. Diese Komponente zeigt das Feld an. Dies
     * ist fortgeschrittene GUI-Technik - Sie können sie für Ihr Projekt ignorieren,
     * wenn Sie wollen.
     */
    private class Fieldview extends JPanel {
        private final int STRETCH_FACTOR = 6;

        private int fieldWidth, fieldHeight;
        private int xFactor, yFactor;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Erzeuge eine neue Komponente zur Feldansicht.
         */
        public Fieldview(int height, int width) {
            fieldHeight = height;
            fieldWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Der GUI-Verwaltung mitteilen, wie groß wir sein wollen. Der Name der Methode
         * ist durch die GUI-Verwaltung festgelegt.
         */
        public Dimension getPreferredSize() {
            return new Dimension(fieldWidth * STRETCH_FACTOR, fieldHeight * STRETCH_FACTOR);
        }

        /**
         * Bereite eine neue Zeichenrunde vor. Da die Komponente in der Größe geändert
         * werden kann, muss der Maßstab neu berechnet werden.
         */
        public void prepareDrawing() {
            if (!size.equals(getSize())) { // Größe wurde geändert...
                size = getSize();
                fieldImage = fieldview.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xFactor = size.width / fieldWidth;
                if (xFactor < 1) {
                    xFactor = STRETCH_FACTOR;
                }
                yFactor = size.height / fieldHeight;
                if (yFactor < 1) {
                    yFactor = STRETCH_FACTOR;
                }
            }
        }

        /**
         * Zeichne an der gegebenen Position ein Rechteck mit der gegebenen Farbe.
         */
        public void drawMarker(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xFactor, y * yFactor, xFactor - 1, yFactor - 1);
        }

        /**
         * Die Komponente für die Feldansicht muss erneut angezeigt werden. Kopiere das
         * interne Image in die Anzeige. Der Name der Methode ist durch die
         * GUI-Verwaltung festgelegt.
         */
        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                Dimension currenSize = getSize();
                if (size.equals(currenSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                } else {
                    // Größe des aktuellen Images anpassen.
                    g.drawImage(fieldImage, 0, 0, currenSize.width, currenSize.height, null);
                }
            }
        }
    }
}
