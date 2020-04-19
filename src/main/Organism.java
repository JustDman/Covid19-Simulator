package main;

public abstract class Organism {

    private boolean alive;

    public Organism() {
        alive = true;

    }

    public boolean isAlive() {
        return alive;
    }

    abstract public void act();

}