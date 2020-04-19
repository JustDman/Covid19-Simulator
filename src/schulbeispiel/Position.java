package schulbeispiel;
/**
 * Objekte dieser Klasse repräsentieren Positionen in einem rechteckigen Feld.
 * 
 * @author David J. Barnes und Michael Kölling
 * @version 2016.02.29
 */
public class Position {
    // Zeilen- und Spaltenposition.
    private int row;
    private int column;

    /**
     * Repräsentiere eine Zeile und eine Spalte.
     * 
     * @param zeile  die Zeile
     * @param spalte die Spalte
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Prüfung auf Datengleichheit.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position otherPos = (Position) obj;
            return row == otherPos.getRow() && column == otherPos.getColumn();
        } else {
            return false;
        }
    }

    /**
     * Liefere einen String in der Form 'Zeile,Spalte'
     * 
     * @return eine Stringdarstellung dieser Position
     */
    public String toString() {
        return row + "," + column;
    }

    /**
     * Benutze die 16 höherwertigen Bits für den den Zeilenwert und die 16
     * niederwertigen Bits für den Spaltenwert. Außer für sehr große Felder sollte
     * dies einen eindeutigen Hashwert für jedes Zeile-Spalte-Paar geben.
     * 
     * @return einen Hash-Code für diese Position
     */
    public int hashCode() {
        return (row << 16) + column;
    }

    /**
     * @return die Zeile
     */
    public int getRow() {
        return row;
    }

    /**
     * @return die Spalte
     */
    public int getColumn() {
        return column;
    }
}
