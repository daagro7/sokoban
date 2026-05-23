package es.upm.pproject.sokoban.models;

public class Boxes {

    int x, y;
    boolean isOnGoal;
    // TODO: Create a list of movements made by the box

    public Boxes(int x, int y) {
        this.x = x;
        this.y = y;
        this.isOnGoal = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean getOnGoal() {
        return isOnGoal;
    }

    public void setOnGoal(boolean onGoal) {
        isOnGoal = onGoal;
    }
}
