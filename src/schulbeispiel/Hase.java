package schulbeispiel;
import java.util.List;
import java.util.Random;

/**
 * Ein einfaches Modell eines Hasen. Ein Hase altert, bewegt sich, gebärt
 * Nachwuchs und stirbt.
 * 
 * @author David J. Barnes und Michael Kölling
 * @version 2016.03.18
 */
public class Hase extends Organism {
    // Eigenschaften aller Hasen (Klassenvariablen).

    // Das Alter, in dem ein Hase gebärfähig wird.
    private static final int GEBAER_ALTER = 5;
    // Das Höchstalter eines Hasen.
    private static final int MAX_ALTER = 40;
    // Die Wahrscheinlichkeit, mit der ein Hase Nachwuchs gebürt.
    private static final double GEBAER_WAHRSCHEINLICHKEIT = 0.12;
    // Die maximale Größe eines Wurfes (Anzahl der Jungen)
    private static final int MAX_WURFGROESSE = 4;
    // Ein gemeinsamer Zufallsgenerator, der die Geburten steuert.
    private static final Random rand = Randomnumbergenerator.getRNG();

    // Individuelle Eigenschaften eines Hasen (Instanzfelder).

    // Das Alter dieses Hasen.
    private int alter;
    // Ist dieser Hase noch lebendig?

    /**
     * Erzeuge einen neuen Hasen. Ein neuer Hase kann das Alter 0 (neugeboren) oder
     * ein zufälliges Alter haben.
     *
     * @param zufaelligesAlter soll der Hase ein zufälliges Alter haben?
     * @param feld             das aktuelle belegte Feld
     * @param position         die Position im Feld
     */
    public Hase(boolean zufaelligesAlter, Field feld, Position position) {
        super(feld, position);
        alter = 0;
        if (zufaelligesAlter) {
            alter = rand.nextInt(MAX_ALTER);
        }
    }

    /**
     * Das ist was ein Hase die meiste Zeit tut - er läuft herum. Manchmal gebärt er
     * Nachwuchs und irgendwann stirbt er an Altersschwäche.
     * 
     * @param neueHasen eine Liste zum Zurückliefern der neugeborenen Hasen
     */
    public void act(List<Organism> neueHasen) {
        alterErhoehen();
        if (isAlive()) {
            gebaereNachwuchs(neueHasen);
            // nur in das nächste Feld setzen, wenn eine Position frei ist
            Position neuePosition = getField().freeNeighborPosition(getPosition());
            if (neuePosition != null) {
                setPosition(neuePosition);
            } else {
                // Überpopulation
                die();
            }
        }
    }

    /**
     * Erhöhe das Alter. Dies kann zum Tod des Hasen führen.
     */
    private void alterErhoehen() {
        alter++;
        if (alter > MAX_ALTER) {
            die();
        }
    }

    /**
     * Prüfe, ob dieser Hase in diesem Schritt gebären kann. Neugeborene kommen in
     * freie Nachbarpositionen.
     * 
     * @param neueHasen eine Liste zum Zurückliefern der neugeborenen Hasen
     */
    private void gebaereNachwuchs(List<Organism> neueHasen) {
        // Neugeborene kommen in freie Nachbarpositionen.
        // Freie Nachbarpositionen abfragen.
        Field feld = getField();
        List<Position> frei = feld.freeNeighborPositions(getPosition());
        int geburten = traechtig();
        for (int b = 0; b < geburten && frei.size() > 0; b++) {
            Position pos = frei.remove(0);
            Hase jung = new Hase(false, feld, pos);
            neueHasen.add(jung);
        }
    }

    /**
     * Erzeuge eine Zahl für die Wurfgroesse, wenn der Hase gebären kann.
     * 
     * @return Wurfgroesse (kann null sein)
     */
    private int traechtig() {
        int wurfgroesse = 0;
        if (kannGebaeren() && rand.nextDouble() <= GEBAER_WAHRSCHEINLICHKEIT) {
            wurfgroesse = rand.nextInt(MAX_WURFGROESSE) + 1;
        }
        return wurfgroesse;
    }

    /**
     * Ein Hase kann gebären, wenn er das gebärfähige Alter erreicht hat.
     * 
     * @return true wenn der Hase gebärfähig, sonst false
     */
    private boolean kannGebaeren() {
        return alter >= GEBAER_ALTER;
    }
}
