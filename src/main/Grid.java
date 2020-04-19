package main;

public class Grid {

    public static int DEFAULT_W = 128;
    public static int DEFAULT_H = 128;

    private Field[][] grid;

    public Grid() {
        this(DEFAULT_W, DEFAULT_H);
    }

    public Grid(int width, int height) {
        grid = new Field[width][height];
    }

    public void setup() {
        for (int x = 0; x < DEFAULT_H; x++) {
            for (int y = 0; y < DEFAULT_H; y++) {
                grid[x][y] = new Field();
            }
        }
    }

    public void clear() {
        for (int row = 0; row < DEFAULT_H; row++) {
            for (int column = 0; column < DEFAULT_H; column++) {
                grid[row][column] = Field.getEmpty();
            }
        }
    }

    public void clear(Position p) {
        grid[p.getRow()][p.getColumn()] = Field.getEmpty();
    }

    public void place(Organism org, Position p) {
        getField(p).set(org);
    }

    public Field getField(Position p) {
        return grid[p.getRow()][p.getColumn()];
    }

    public Organism getOrg(Position p) {
        return getField(p).get();
    }

}