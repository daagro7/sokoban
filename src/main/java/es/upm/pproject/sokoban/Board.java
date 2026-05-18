package es.upm.pproject.sokoban;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;


// import java.util.List;

// import javax.swing.JFrame;
// import java.awt.event.KeyAdapter;
// import java.awt.event.KeyEvent;


public class Board {

    static WarehouseMan wMan;
    static Cells[][] cellMap;
    static int totalGoals;
    static int totalBoxes;
    static int stepsOnLevel;

    public Board(int width, int height) {
        cellMap = new Cells[width][height];
    }

    // This method displays the current state of the game map
    public static void viewMap(int level) {
        System.out.println("Level: " + level + " Steps: " + stepsOnLevel);
        for (Cells[] row : cellMap) {
            for (Cells cell : row) {
                if (cell != null)
                    System.out.print(cell.toString());
            }
            System.out.println();
        }
    }

    // This method returns the type of cell at the given position
    public int getCell(int row, int column) {
        return cellMap[row][column].cellType;
    }

    // This method checks if the warehouse man can move to the given position
    // and performs the move if possible
    public void canWarehouseManMove(int newRow, int newColumn) {
        switch (getCell(newRow, newColumn)) {
            case Cells.EMPTY:
                // Move the warehouse man to the new position
                moveWMan(newRow, newColumn);
                break;
            case Cells.BOX:
                // Attempt to move the box in the same direction
                int newBoxRow = newRow + (newRow - wMan.getX());
                int newBoxColumn = newColumn + (newColumn - wMan.getY());

                // If the box can be moved, move the box and then move the warehouse man
                // else, do nothing (the warehouse man cannot move in that direction)
                if (canBoxMove(newBoxRow, newBoxColumn)) {
                    // If the box was moved from a goal position, we substract one from the count of boxes in goal
                    if (cellMap[newRow][newColumn].isGoal) {
                        totalGoals++;
                    }

                    // Move the warehouse man to the new position
                    moveWMan(newRow, newColumn);
                }
                break;
            case Cells.WALL:
                // Cannot move the warehouse man this way
                break;
            case Cells.GOAL_POSITION:
                // Move the warehouse man to a goal position
                moveWMan(newRow, newColumn);
                break;
            default:
                break;
        }
    }

    // This method moves the warehouse man to the given position
    // updating the cell map
    public void moveWMan(int row, int column) {
        // Move the warehouse man to the new position
        cellMap[row][column].cellType = Cells.WAREHOUSE_MAN;

        // Restore the previous cell to its original state (only in two options)
        if (cellMap[wMan.getX()][wMan.getY()].isGoal) {
            cellMap[wMan.getX()][wMan.getY()].cellType = Cells.GOAL_POSITION;
        } else {
            cellMap[wMan.getX()][wMan.getY()].cellType = Cells.EMPTY;
        }

        // Update the warehouse man position and add one step to the counter
        wMan.setPosition(row, column);
        stepsOnLevel++;
    }

    // This method checks if the box can move to the given position
    // and performs the move if possible
    public boolean canBoxMove(int newRow, int newColumn) {
        switch (getCell(newRow, newColumn)) {
            case Cells.EMPTY:
                // Move the box to the new position
                moveBox(newRow, newColumn);
                return true;
            case Cells.WALL:
                // Cannot move the box this way
                return false;
            case Cells.BOX:
                // Cannot move the box this way
                return false;
            case Cells.GOAL_POSITION:
                // Move the box to the goal position
                moveBox(newRow, newColumn);
                totalGoals--;
                return true;
            default:
                return false;
        }
    }

    // This method moves the box to the given position
    public void moveBox(int row, int column) {
        // Move the box to the new position
        cellMap[row][column].cellType = Cells.BOX;
        
        /*
         * NO change is needed to the previous cell because the warehouse man
         * will move to that position 
         */
    }
}
