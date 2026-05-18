package es.upm.pproject.sokoban;



public class Cells {
    static final int EMPTY = 0;
    static final int WALL = 1;
    static final int WAREHOUSE_MAN = 2;
    static final int BOX = 3;
    static final int GOAL_POSITION = 4;


    int cellType;
    boolean isGoal;


    public Cells(char character) {
        this.cellType = cellType(character);
        this.isGoal = this.cellType == GOAL_POSITION;
    }


    public int cellType(char character) {
        switch (character) {
            case '+':
                // Handle wall
                return WALL;
            case ' ':
                // Handle empty space
                return EMPTY;
            case 'W':
                // Handle worker
                return WAREHOUSE_MAN;
            case '#':
                // Handle box
                return BOX;
            case '*':
                // Handle target
                return GOAL_POSITION;
            default:
                // Handle unknown character
                return -1;
        }
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