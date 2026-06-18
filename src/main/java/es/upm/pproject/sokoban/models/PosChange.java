package es.upm.pproject.sokoban.models;

/**
 * Represents a change in position in the board
 */
public class PosChange {
    private int previousX;
    private int previousY;
    private int nextX;
    private int nextY;

    /**
     * Constructs a PosChange
     *
     * @param prevX previous x coordinate
     * @param prevY previous y coordinate
     * @param nextX next x coordinate
     * @param nextY next y coordinate
     */
    public PosChange(int prevX, int prevY, int nextX, int nextY) {
        this.previousX = prevX;
        this.previousY = prevY;
        this.nextX = nextX;
        this.nextY = nextY;
    }

    /**
     * Returns the previous x coordinate
     *
     * @return previous x coordinate
     */
    public int getPreviousX() {
        return previousX;
    }

    /**
     * Returns the previous y coordinate
     *
     * @return previous y coordinate
     */
    public int getPreviousY() {
        return previousY;
    }

    /**
     * Returns the next x coordinate
     *
     * @return next x coordinate
     */
    public int getNextX() {
        return nextX;
    }

    /**
     * Returns the next y coordinate
     *
     * @return next y coordinate
     */
    public int getNextY() {
        return nextY;
    }
}
