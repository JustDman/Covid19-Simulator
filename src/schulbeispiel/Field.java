
package schulbeispiel;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Ein rechteckiges Gitter von Feldpositionen. Jede Position kann ein einzelnes
 * Tier aufnehmen.
 * 
 * @author David J. Barnes und Michael Kölling
 * @version 2016.02.29
 */
public class Field {
    // Ein Zufallsgenerator für zufällige Positionen
    private static final Random rand = Randomnumbergenerator.getRNG();

    // Die Tiefe und die Breite des Feldes
    private int depth, width;
    // Speicher für die Tiere
    private Object[][] field;

    /**
     * Erzeuge ein Feld mit den angegebenen Dimensionen.
     * 
     * @param depth die Tiefe des Feldes
     * @param width die Breite des Feldes
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Object[depth][width];
    }

    /**
     * Räume das Feld.
     */
    public void clear() {
        for (int zeile = 0; zeile < depth; zeile++) {
            for (int spalte = 0; spalte < width; spalte++) {
                field[zeile][spalte] = null;
            }
        }
    }

    /**
     * Räume die gegebene Position.
     * 
     * @param position die zu leerende Position
     */
    public void clear(Position position) {
        field[position.getRow()][position.getColumn()] = null;
    }

    /**
     * Platziere das gegebene Tier an der angegebenen Position. Wenn an der Position
     * bereits ein Tier eingetragen ist, geht es verloren.
     * 
     * @param orgamism das Tier das platziert werden soll
     * @param row      die Zeilenkoordinate der Position
     * @param column   die Spaltenkoordinate der Position
     */
    public void place(Object orgamism, int row, int column) {
        place(orgamism, new Position(row, column));
    }

    /**
     * Platziere das gegebene Tier an der angegebenen Position. Wenn an der Position
     * bereits ein Tier eingetragen ist, geht es verloren.
     * 
     * @param organism das Tier, das platziert werden soll
     * @param position die Position, an der das Tier platziert werden soll
     */
    public void place(Object organism, Position position) {
        field[position.getRow()][position.getColumn()] = organism;
    }

    /**
     * Liefere das Tier an der angegebenen Position, falls vorhanden.
     * 
     * @param position die gewünschte Position
     * @return das Tier an der angegebenen Position oder null, wenn dort kein Tier
     *         eingetragen ist
     */
    public Object getObjectAt(Position position) {
        return getObjectAt(position.getRow(), position.getColumn());
    }

    /**
     * Liefere das Tier an der angegebenen Position, falls vorhanden.
     * 
     * @param row    die gewünschte Zeile
     * @param column die gewünschte Spalte
     * @return das Tier an der angegebenen Position oder null, wenn dort kein Tier
     *         eingetragen ist
     */
    public Object getObjectAt(int row, int column) {
        return field[row][column];
    }

    /**
     * Wähle zufällig eine der Positionen, die an die gegebene Position angrenzen,
     * oder die gegebene Position selbst. Die gelieferte Position liegt innerhalb
     * der gültigen Grenzen dieses Feldes.
     * 
     * @param position die Position, von der ein Nachbar zu wählen ist
     * @return eine gültige Position innerhalb dieses Feldes
     */
    public Position randomNeighborPosition(Position position) {
        List<Position> neighbor = neighborPositions(position);
        return neighbor.get(0);
    }

    /**
     * Liefert eine gemischte Liste von freien Nachbarposition.
     * 
     * @param position die Position, für die Nachbarpositionen zu liefern ist
     * @return eine Liste freier Nachbarpositionen
     */
    public List<Position> freeNeighborPositions(Position position) {
        List<Position> free = new LinkedList<>();
        List<Position> neighbor = neighborPositions(position);
        for (Position next : neighbor) {
            if (getObjectAt(next) == null) {
                free.add(next);
            }
        }
        return free;
    }

    /**
     * Versuche, eine freie Nachbarposition zur gegebenen Position zu finden. Wenn
     * es keine gibt, liefere null. Die gelieferte Position liegt innerhalb der
     * Feldgrenzen.
     * 
     * @param position die Position, für die eine Nachbarposition zu liefern ist
     * @return eine gültige Position innerhalb der Feldgrenzen
     */
    public Position freeNeighborPosition(Position position) {
        // Die verfügbaren freien Nachbarpositionen
        List<Position> free = freeNeighborPositions(position);
        if (free.size() > 0) {
            return free.get(0);
        } else {
            return null;
        }
    }

    /**
     * Liefert eine gemischte Liste von Nachbarpositionen zu der gegebenen Position.
     * Diese Liste enthält nicht die gegebene Position selbst. Alle Positionen
     * liegen innerhalb des Feldes.
     * 
     * @param position die Position, für die Nachbarpositionen zu liefern sind
     * @return eine Liste der Nachbarpositionen zur gegebenen Position
     */
    public List<Position> neighborPositions(Position position) {
        assert position != null : "No position handed over to neighborPosition";
        // Die Liste der zurueckzuliefernden Positionen
        List<Position> positions = new LinkedList<>();
        if (position != null) {
            int row = position.getRow();
            int column = position.getColumn();
            for (int zDiff = -1; zDiff <= 1; zDiff++) {
                int nextRow = row + zDiff;
                if (nextRow >= 0 && nextRow < depth) {
                    for (int sDiff = -1; sDiff <= 1; sDiff++) {
                        int nextColumn = column + sDiff;
                        // Ungueltige Positionen und Ausgangsposition ausschliessen.
                        if (nextColumn >= 0 && nextColumn < width && (zDiff != 0 || sDiff != 0)) {
                            positions.add(new Position(nextRow, nextColumn));
                        }
                    }
                }
            }
            // Mische die Liste. Verschiedene andere Methoden verlassen sich darauf,
            // dass die Liste ungeordnet ist.
            Collections.shuffle(positions, rand);
        }
        return positions;
    }

    /**
     * Liefere die Tiefe dieses Feldes.
     * 
     * @return die Tiefe dieses Feldes
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Liefere die Breite dieses Feldes.
     * 
     * @return die Breite dieses Feldes
     */
    public int getWidth() {
        return width;
    }
}
