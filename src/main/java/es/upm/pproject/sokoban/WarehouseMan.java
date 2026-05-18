package es.upm.pproject.sokoban;

public class WarehouseMan {

    int x, y;
    // TODO: Create a list of movements made by the warehouse man

    public WarehouseMan(int x, int y) {
        this.x = x;
        this.y = y;
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
}