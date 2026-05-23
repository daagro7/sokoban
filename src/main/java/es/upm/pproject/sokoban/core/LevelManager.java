package es.upm.pproject.sokoban.core;

import es.upm.pproject.sokoban.models.Board;
import es.upm.pproject.sokoban.models.WarehouseMan;
import es.upm.pproject.sokoban.models.Cell.CellType;

public class LevelManager {
  Board board;
  WarehouseMan warehouseMan;
  int totalGoals;
  // int totalBoxes;
  int stepsOnLevel;

  public LevelManager(Board board, WarehouseMan warehouseMan, int totalGoals) {
    this.totalGoals = totalGoals;
    this.stepsOnLevel = 0;
    this.board = board;
    this.warehouseMan = warehouseMan;
  }

  private void executeWManMovement(int dirX, int dirY) {

    int oldX = warehouseMan.getX();
    int oldY = warehouseMan.getY();

    if (board.getCell(oldX, oldY).isGoal()) {
      board.setCellType(oldX, oldY, CellType.GOAL_POSITION);
    } else {
      board.setCellType(oldX, oldY, CellType.EMPTY);
    }

    board.setCellType(oldX + dirX, oldY + dirY, CellType.WAREHOUSE_MAN);
    warehouseMan.move(dirX, dirY);
    stepsOnLevel++;
  }

  // This method moves the box to the given position
  private void executeBoxMovement(int x, int y, int dirX, int dirY) {
    // Move the box to the new position

    if (board.getCell(x, y).isGoal()) {
      totalGoals++;
    }

    if (board.getCell(x + dirX, y + dirY).isGoal()) {
      totalGoals--;
    }

    board.setCellType(x + dirX, x + dirY, CellType.BOX);

    /*
     * NO change is needed to the previous cell because the warehouse man
     * will move to that position
     */
  }

  private boolean moveBox(int x, int y, int dirX, int dirY) {
    int newRow = x + dirX;
    int newColumn = y + dirY;
    switch (board.getCell(newRow, newColumn).getCellType()) {
      case EMPTY:
        // Move the box to the new position
        executeBoxMovement(x, y, dirX, dirY);
        return true;
      case WALL:
        // Cannot move the box this way
        return false;
      case BOX:
        // Cannot move the box this way
        return false;
      case GOAL_POSITION:
        // Move the box to the goal position
        executeBoxMovement(x, y, dirX, dirY);
        return true;
      default:
        return false;
    }
  }

  public boolean moveWMan(int dirX, int dirY) {
    int newRow = warehouseMan.getX() + dirX;
    int newColumn = warehouseMan.getY() + dirY;
    switch (board.getCell(newRow, newColumn).getCellType()) {
      case EMPTY:
      case GOAL_POSITION:
        executeWManMovement(dirX, dirY);
        return true;
      case BOX:
        // If the box can be moved, move the box and then move the warehouse man
        // else, do nothing (the warehouse man cannot move in that direction)
        if (moveBox(newRow, newColumn, dirX, dirY)) {
          // If the box was moved from a goal position, we substract one from the count of
          // boxes in goal

          // Move the warehouse man to the new position
          executeWManMovement(dirX, dirY);
          return true;
        }

        return false;
      case WALL:
        return false;
      default:
        return false;
    }
  }

  // Getters
  public int getTotalGoals() {
    return totalGoals;
  }

  public int getStepsOnLevel() {
    return stepsOnLevel;
  }
}
