package schulbeispiel;
import java.util.List;

/**
 * Tier ist eine abstrakte Superklasse für Tiere. Sie verwaltet Eigenschaften,
 * die alle Tiere gemein haben.
 * 
 * @author David J. Barnes und Michael Kölling
 * @version 2016.03.18
 */
public abstract class Organism {
    // Ist dieses Tier noch lebendig?
    private boolean alive;
    // Das Feld des Tieres
    private Field field;
    // Die Position dieses Tieres.
    private Position position;

    /**
     * Erzeuge ein neues Tier an der gegebenen Position im Feld.
     * 
     * @param field    das aktuelle belegte Feld
     * @param position die Position im Feld
     */
    public Organism(Field field, Position position) {
        alive = true;
        this.field = field;
        setPosition(position);
    }

    /**
     * Lasse dieses Tier agieren - es soll das tun, was es tun muss oder möchte.
     * 
     * @param newOrganism eine Liste zum Aufnehmen neuer Tiere
     */
    abstract public void act(List<Organism> newOrganism);

    /**
     * Prüfe, ob dieses Tier noch lebendig ist.
     * 
     * @return true wenn dieses Tier noch lebendig ist
     */
    protected boolean isAlive() {
        return alive;
    }

    /**
     * Anzeigen, dass das Tier nicht mehr länger lebendig ist Es wird aus dem Feld
     * entfernt.
     */
    protected void die() {
        alive = false;
        if (position != null) {
            field.clear(position);
            position = null;
            field = null;
        }
    }

    /**
     * Liefere die Position dieses Tieres.
     * 
     * @return die Position dieses Tieres
     */
    protected Position getPosition() {
        return position;
    }

    /**
     * Setze das Tier auf die gegebene Position im aktuellen Feld.
     * 
     * @param newPosition die neue Position des Tieres
     */
    protected void setPosition(Position newPosition) {
        if (position != null) {
            field.clear(position);
        }
        position = newPosition;
        field.place(this, newPosition);
    }

    /**
     * Liefere das Feld des Tieres.
     * 
     * @return das Feld des Tieres
     */
    protected Field getField() {
        return field;
    }
}
