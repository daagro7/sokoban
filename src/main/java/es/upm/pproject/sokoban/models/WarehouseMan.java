package es.upm.pproject.sokoban.models;

/**
 * Represents the player in the Sokoban game, with its position on the game
 * board.
 */
public class WarehouseMan {

    private int x;
    private int y;

    /**
     * Constructs a WarehouseMan with the specified initial position.
     *
     * @param x initial x coordinate of the WarehouseMan
     * @param y initial y coordinate of the WarehouseMan
     */
    public WarehouseMan(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the current x coordinate of the WarehouseMan.
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the current y coordinate of the WarehouseMan.
     *
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Moves the WarehouseMan in the direction specified
     *
     * @param dx x direction
     * @param dy y direction
     */
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * Sets the position of the WarehouseMan to the specified
     *
     * @param x new x coordinate
     * @param y new y coordinate
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
