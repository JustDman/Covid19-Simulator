
package schulbeispiel;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;

/**
 * Die Ansicht zeigt die Entwicklung zweier Populationen als Liniengraph über
 * die Zeit. Die aktuelle Version stellt immer genau zwei unterschiedliche
 * Tierklassen dar. Falls weitere Tiere eingeführt werden, werden diese nicht
 * automatisch berücksichtigt.
 * 
 * @author Michael Kölling und David J. Barnes
 * @version 2016.03.18
 */
public class DiagrammView implements SimulationView {
    private static final Color LIGHTGREY = new Color(0, 0, 0, 40);

    private static JFrame window;
    private static GraphPanel graph;
    private static JLabel stepLabel;
    private static JLabel countLabel;

    // Die Klassen, die von dieser Ansicht beobachtet werden
    private Set<Class> classes;
    // Eine Map für die Farben der Simulationsteilnehmer
    private Map<Class, Color> colors;
    // Ein Statistik-Objekt zur Berechnung und Speicherung
    // von Simulationsdaten
    private FieldStatistics stats;

    /**
     * Konstruktor.
     * 
     * @param width    die Breite des Plotterfensters(in Pixel)
     * @param height   die Höhe des Plotterfensters(in Pixel)
     * @param startMax der anfängliche Maximalwert für die y-Achse
     */
    public DiagrammView(int width, int height, int startMax) {
        stats = new FieldStatistics();
        classes = new HashSet<>();
        colors = new HashMap<>();

        if (window == null) {
            window = createWindow(width, height, startMax);
        } else {
            graph.newRun();
        }

        // zeigeStatus(0, null);
    }

    /**
     * Definiere eine Farbe für die gegebene Tierklasse.
     * 
     * @param organismClass das Klassenobjekt der Tierklasse
     * @param color         die zu benutzende Farbe für die Tierklasse
     */
    public void setColor(Class organismClass, Color color) {
        colors.put(organismClass, color);
        classes = colors.keySet();
    }

    /**
     * Zeige den aktuellen Zustand des Feldes. Der Status wird durch einen
     * Liniengraphen für zwei Klassen im Feld dargestellt. Diese Ansicht
     * funktioniert auf dem aktuellen Stand nur für genau zwei Klassen (nicht mehr
     * und nicht weniger). Wenn das Feld mehr als zwei unterschiedliche Tierspezies
     * enthält, werden dennoch nur zwei Klassen dargestellt.
     * 
     * @param step  welcher Iterationsschritt ist dies?
     * @param field das Feld, das angezeigt werden soll
     */
    public void showStatus(int step, Field field) {
        graph.refresh(step, field, stats);
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
        graph.newRun();
    }

    /**
     * Bereite das Fenster für die Darstellung des Graphen vor.
     */
    private JFrame createWindow(int width, int height, int startMax) {
        JFrame window = new JFrame("Diagrammview");
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        Container contentPane = window.getContentPane();

        graph = new GraphPanel(width, height, startMax);
        contentPane.add(graph, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.add(new JLabel("Step:"));
        stepLabel = new JLabel("");
        footer.add(stepLabel);
        countLabel = new JLabel(" ");
        footer.add(countLabel);
        contentPane.add(footer, BorderLayout.SOUTH);

        window.pack();
        window.setLocation(20, 600);

        window.setVisible(true);

        return window;
    }

    // ============================================================================
    /**
     * Eingebettete Klasse: eine Komponente zum Anzeigen des Graphen.
     */
    class GraphPanel extends JComponent {
        private static final double SCALINGS_FACTOR = 0.8;

        // Ein interner Bildpuffer, der zum Zeichnen benutzt wird. Für die
        // eigentliche Anzeige wird dieser Bildpuffer dann auf den Bildschirm kopiert.
        private BufferedImage graphPicture;
        private int lastValue1, lastValue2;
        private int yMax;

        /**
         * Erzeuge einen neuen leeren GraphPanel.
         */
        public GraphPanel(int width, int height, int startMax) {
            graphPicture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            deletePicture();
            lastValue1 = height;
            lastValue2 = height;
            yMax = startMax;
        }

        /**
         * Stelle eine neue Simulation auf dem Panel dar.
         */
        public void newRun() {
            int height = graphPicture.getHeight();
            int width = graphPicture.getWidth();

            Graphics g = graphPicture.getGraphics();
            g.copyArea(4, 0, width - 4, height, -4, 0);
            g.setColor(Color.BLACK);
            g.drawLine(width - 4, 0, width - 4, height);
            g.drawLine(width - 2, 0, width - 2, height);
            lastValue1 = height;
            lastValue2 = height;
            repaint();
        }

        /**
         * Zeige eine neuen Datenpunkt an.
         */
        public void refresh(int step, Field field, FieldStatistics stats) {
            if (classes.size() >= 2) {
                Iterator<Class> it = classes.iterator();
                Class klasse1 = it.next();
                Class klasse2 = it.next();

                stats.reset();
                int count1 = stats.getResidentCount(field, klasse1);
                int count2 = stats.getResidentCount(field, klasse2);

                Graphics g = graphPicture.getGraphics();

                int height = graphPicture.getHeight();
                int width = graphPicture.getWidth();

                // verschiebe den Graphen um einen Pixel nach links
                g.copyArea(1, 0, width - 1, height, -1, 0);

                // berechne y, prüfe, ob es außerhalb des Bildschirms liegt. Skaliere notfalls
                // herunter.
                int y = height - ((height * count1) / yMax) - 1;
                while (y < 0) {
                    scaleDown();
                    y = height - ((height * count1) / yMax) - 1;
                }
                g.setColor(LIGHTGREY);
                g.drawLine(width - 2, y, width - 2, height);
                g.setColor(colors.get(klasse1));
                g.drawLine(width - 3, lastValue1, width - 2, y);
                lastValue1 = y;

                y = height - ((height * count2) / yMax) - 1;
                while (y < 0) {
                    scaleDown();
                    y = height - ((height * count2) / yMax) - 1;
                }
                g.setColor(LIGHTGREY);
                g.drawLine(width - 2, y, width - 2, height);
                g.setColor(colors.get(klasse2));
                g.drawLine(width - 3, lastValue2, width - 2, y);
                lastValue2 = y;

                repaint();

                stepLabel.setText("" + step);
                countLabel.setText(stats.getResidentInfo(field));
            }
        }

        /**
         * Skaliere den aktuellen Graphen vertikal etwas herunter, so dass im oberen
         * Bereich mehr Platz bleibt.
         */
        public void scaleDown() {
            Graphics g = graphPicture.getGraphics();
            int hoehe = graphPicture.getHeight();
            int breite = graphPicture.getWidth();

            BufferedImage tmpBild = new BufferedImage(breite, (int) (hoehe * SCALINGS_FACTOR),
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D gtmp = (Graphics2D) tmpBild.getGraphics();

            gtmp.scale(1.0, SCALINGS_FACTOR);
            gtmp.drawImage(graphPicture, 0, 0, null);

            int header = (int) (hoehe * (1.0 - SCALINGS_FACTOR));

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, breite, header);
            g.drawImage(tmpBild, 0, header, null);

            yMax = (int) (yMax / SCALINGS_FACTOR);
            lastValue1 = header + (int) (lastValue1 * SCALINGS_FACTOR);
            lastValue2 = header + (int) (lastValue2 * SCALINGS_FACTOR);

            repaint();
        }

        /**
         * Lösche das Bild im Panel.
         */
        public void deletePicture() {
            Graphics g = graphPicture.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, graphPicture.getWidth(), graphPicture.getHeight());
            repaint();
        }

        // Die folgenden Methoden sind Redefinitionen von Methoden, die von
        // den Superklassen geerbt wurden.

        /**
         * Teile dem Layout-Manager mit, wie groß wir sein möchten. (Diese Methode wird
         * automatisch vom Layout-Manager beim Platzieren der Kompoennten aufgerufen.)
         * 
         * @return die bevorzugten Abmaße für diese Komponente
         */
        public Dimension getPreferredSize() {
            return new Dimension(graphPicture.getWidth(), graphPicture.getHeight());
        }

        /**
         * Diese Komponente ist undurchsichtig.
         */
        public boolean isOpaque() {
            return true;
        }

        /**
         * Diese Komponente muss neu gezeichnet werden. Kopiere das interne Bild auf den
         * Bildschirm. (Diese Methode wird automatisch vom Swing-Bildschirmmanager
         * aufgerufen, wenn er möchte, dass die Komponente neu gezeichnet wird.)
         * 
         * @param g der Grafikkontext, der zum Zeichnen dieser Komponente verwendet
         *          werden kann
         */
        public void paintComponent(Graphics g) {
            if (graphPicture != null) {
                g.drawImage(graphPicture, 0, 0, null);
            }
        }
    }
}
