package es.upm.pproject.sokoban.models;

/**
 * Class representing a cell in the Sokoban game board.
 */
public class Cell {
    /**
     * Enum representing the different types of cells in the game.
     */
    public enum CellType {
        EMPTY,
        WALL,
        WAREHOUSE_MAN,
        BOX,
        GOAL_POSITION,
        ERROR
    }

    CellType cellType;
    boolean isGoal;

    /**
     * Constructs a Cell
     *
     * @param cellType type of the cell
     */
    public Cell(CellType cellType) {
        this.cellType = cellType;
        this.isGoal = (cellType == CellType.GOAL_POSITION);
    }

    /**
     * Changes the type of the cell to the given type
     *
     * @param cellType new type
     */
    public void updateType(CellType cellType) {
        this.cellType = cellType;
    }

    /**
     * Returns the type of the cell
     *
     * @return cell type
     */
    public CellType getCellType() {
        return this.cellType;
    }

    /**
     * Returns if the cell is a goal position.
     *
     * @return true if the cell is a goal position, false if not
     */
    public boolean isGoal() {
        return this.isGoal;
    }

    public String toString() {
        switch (this.cellType) {
            case WALL:
                return "+";
            case EMPTY:
                return " ";
            case WAREHOUSE_MAN:
                return "W";
            case BOX:
                return "#";
            case GOAL_POSITION:
                return "*";
            default:
                return "?";
        }
    }
}
