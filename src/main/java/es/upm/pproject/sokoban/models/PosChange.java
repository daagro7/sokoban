package es.upm.pproject.sokoban.models;

public class PosChange {
    private int previous_x, previous_y;
    private int next_x, next_y;


    public PosChange(int prevX, int prevY, int nextX, int nextY) {

    }

    public int getPreviousX() {
        return previous_x;
    }

    public int getPreviousY() {
        return previous_y;
    }

    public int getNextX() {
        return next_x;
    }

    public int getNextY() {
        return next_y;
    }
}
