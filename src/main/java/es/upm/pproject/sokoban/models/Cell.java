package es.upm.pproject.sokoban.models;

public class Cell {
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

    public Cell(CellType cellType) {
        this.cellType = cellType;
        this.isGoal = (cellType == CellType.GOAL_POSITION);
    }

    public void updateType(CellType cellType) {
        this.cellType = cellType;
    }

    public CellType getCellType() {
        return this.cellType;
    }

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
