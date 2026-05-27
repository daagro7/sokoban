package es.upm.pproject.sokoban.core;

import es.upm.pproject.sokoban.models.Board;
import es.upm.pproject.sokoban.models.WarehouseMan;
import es.upm.pproject.sokoban.models.Cell.CellType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelManager {
  private static final Logger logger = LoggerFactory.getLogger(LevelManager.class);
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

    board.setCellType(x + dirX, y + dirY, CellType.BOX);

    /*
     * NO change is needed to the previous cell because the warehouse man
     * will move to that position
     */
  }

  private boolean moveBox(int x, int y, int dirX, int dirY) {
    int newX = x + dirX;
    int newY = y + dirY;
    switch (board.getCell(newX, newY).getCellType()) {
      case EMPTY:
        // Move the box to the new position
        executeBoxMovement(x, y, dirX, dirY);
        return true;
      case WALL:
        // Cannot move the box this way
        logger.warn("Cannot move box: Destination cell ({}, {}) is a WALL.", newX, newY);
        return false;
      case BOX:
        // Cannot move the box this way
        logger.warn("Cannot move box: Destination cell ({}, {}) is already occupied by another box.", newX, newY);
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
    int newX = warehouseMan.getX() + dirX;
    int newY = warehouseMan.getY() + dirY;
    switch (board.getCell(newX, newY).getCellType()) {
      case EMPTY:
      case GOAL_POSITION:
        executeWManMovement(dirX, dirY);
        return true;
      case BOX:
        // If the box can be moved, move the box and then move the warehouse man
        // else, do nothing (the warehouse man cannot move in that direction)
        if (moveBox(newX, newY, dirX, dirY)) {
          // If the box was moved from a goal position, we substract one from the count of
          // boxes in goal

          // Move the warehouse man to the new position
          executeWManMovement(dirX, dirY);
          return true;
        }
        logger.warn("Cannot move warehouse man: Destination cell ({}, {}) is occupied by a box.", newX, newY);
        return false;
      case WALL:
        logger.warn("Cannot move warehouse man: Destination cell ({}, {}) is a WALL.", newX, newY);
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

  public Board getBoard() {
    return board;
  }
}
