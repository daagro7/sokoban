package es.upm.pproject.sokoban.models;

public class Cell {
    public static enum CellType {
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
        updateType(cellType);
    }

    public void updateType(CellType cellType) {
        this.cellType = cellType;
        updateGoal(this.cellType);
    }

    private void updateGoal(CellType type) {
        this.isGoal = type == CellType.GOAL_POSITION;
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
