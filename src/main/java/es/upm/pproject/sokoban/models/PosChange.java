package es.upm.pproject.sokoban.models;

public class PosChange {
    private int previousX;
    private int previousY;
    private int nextX;
    private int nextY;


    public PosChange(int prevX, int prevY, int nextX, int nextY) {
        this.previousX = prevX;
        this.previousY = prevY;
        this.nextX = nextX;
        this.nextY = nextY;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public int getNextX() {
        return nextX;
    }

    public int getNextY() {
        return nextY;
    }
}
