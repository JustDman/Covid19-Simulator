package main;

public class Field {

    private Organism organism;

    public Field() {

    }

    private static Field emptyField = null;

    public void moveOrganismTo(Field newField) {
        newField.organism = this.organism;
        this.organism = null;
    }

    public static Field getEmpty() {
        if (emptyField == null)
            emptyField = new Field();
        return emptyField;
    }

    public boolean isEmpty() {
        return this.equals(emptyField);
    }

    public void set(Organism org) {
        this.organism = org;
    }

    public Organism get() {
        return organism;
    }

}
