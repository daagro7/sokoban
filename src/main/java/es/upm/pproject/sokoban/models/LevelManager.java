package es.upm.pproject.sokoban.models;

import es.upm.pproject.sokoban.models.Cell.CellType;

import java.util.Deque;
import java.util.ArrayDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelManager {
  private static final Logger logger = LoggerFactory.getLogger(LevelManager.class);
  Board board;
  WarehouseMan warehouseMan;
  int totalGoals;
  // int totalBoxes;
  int stepsOnLevel;
  Deque<State> movementHistory;

  public LevelManager(Board board, WarehouseMan warehouseMan, int totalGoals) {
    this.totalGoals = totalGoals;
    this.stepsOnLevel = 0;
    this.board = board;
    this.warehouseMan = warehouseMan;
    this.movementHistory = new ArrayDeque<>();
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
  }

  // This method moves the box to the given position
  private void executeBoxMovement(int x, int y, int dirX, int dirY) {
    // Move the box to the new position

    if (board.getCell(x, y).isGoal()) {
      totalGoals++;
      logger.info("A box was moved out of a goal position. Remaining goals: {}", totalGoals);
    }

    if (board.getCell(x + dirX, y + dirY).isGoal()) {
      totalGoals--;
      logger.info("A box was moved into a goal position. Remaining goals: {}", totalGoals);
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
      case GOAL_POSITION:
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
      default:
        return false;
    }
  }

  public boolean moveWMan(int dirX, int dirY) {
    int newX = warehouseMan.getX() + dirX;
    int newY = warehouseMan.getY() + dirY;
    PosChange posWMan = new PosChange(warehouseMan.getX(), warehouseMan.getY(), newX, newY);
    String direction = getDirectionName(dirX, dirY);
    switch (board.getCell(newX, newY).getCellType()) {
      case EMPTY:
      case GOAL_POSITION:
        State stateWMan = new State(posWMan);
        executeWManMovement(dirX, dirY);
        stepsOnLevel++;
        logger.debug("Warehouse man steps updated: {}", stepsOnLevel);
        logger.debug("Warehouse man moved to the {}", direction);
        movementHistory.push(stateWMan);
        return true;
      case BOX:
        // If the box can be moved, move the box and then move the warehouse man
        // else, do nothing (the warehouse man cannot move in that direction)
        if (moveBox(newX, newY, dirX, dirY)) {
          // If the box was moved from a goal position, we substract one from the count of
          // boxes in goal

          PosChange posBox = new PosChange(newX, newY, dirX + newX, dirY + newY);
          State stateWManBox = new State(posWMan, posBox);
          // Move the warehouse man to the new position
          executeWManMovement(dirX, dirY);
          stepsOnLevel++;
          logger.debug("Warehouse man steps updated: {}", stepsOnLevel);
          logger.info("Warehouse man successfully pushed a box to the {}", direction);
          movementHistory.push(stateWManBox);
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

  public void undoMovement() {
    State state = getPrevState();
    if (state == null) {
      return;
    }

    PosChange posWMan = state.getWMan();
    int reverseDirX = posWMan.getPreviousX() - posWMan.getNextX();
    int reverseDirY = posWMan.getPreviousY() - posWMan.getNextY();
    executeWManMovement(reverseDirX, reverseDirY);
    stepsOnLevel -= 1;
    logger.info("Warehouse man steps updated: {}", stepsOnLevel);

    PosChange posBox = state.getBox();
    if (posBox != null) {
      int reverseBoxDirX = posBox.getPreviousX() - posBox.getNextX();
      int reverseBoxDirY = posBox.getPreviousY() - posBox.getNextY();
      executeBoxMovement(posBox.getNextX(), posBox.getNextY(), reverseBoxDirX, reverseBoxDirY);

      // After moving the box back, update the cell type of the previous box position
      board.setCellType(posBox.getNextX(), posBox.getNextY(),
          board.getCell(posBox.getNextX(), posBox.getNextY()).isGoal() ? CellType.GOAL_POSITION : CellType.EMPTY);

    }
  }

  // Converts directional movement coordinates into a readable string for the
  // logger
  private String getDirectionName(int dirX, int dirY) {
    if (dirX == 0 && dirY == -1)
      return "UP";
    if (dirX == 0 && dirY == 1)
      return "DOWN";
    if (dirX == -1 && dirY == 0)
      return "LEFT";
    if (dirX == 1 && dirY == 0)
      return "RIGHT";
    return "UNKNOWN";
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

  public State getPrevState() {
    if (movementHistory.peek() == null) {
      System.out.println("No movement have been done previously at this level");
      logger.info("No movement have been done previously at this level");
      return null;
    }
    return movementHistory.pop();
  }
}
