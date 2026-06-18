package es.upm.pproject.sokoban.models;

import es.upm.pproject.sokoban.models.Cell.CellType;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the state and logic of a Sokoban level.
 */
public class LevelManager {
  private static final Logger logger = LoggerFactory.getLogger(LevelManager.class);
  String levelName;
  Board board;
  WarehouseMan warehouseMan;
  int totalGoals;
  int stepsOnLevel;
  Deque<State> movementHistory;
  private static final String STEPSUPDATED = "Warehouse man steps updated: {}";

  /**
   * Constructs a LevelManager for a specific level, initializing the board,
   * WarehouseMan, and total goals.
   * 
   * @param levelName    name of the level
   * @param board        board
   * @param warehouseMan WarehouseMan
   * @param totalGoals   total number of goals in the level
   */
  public LevelManager(String levelName, Board board, WarehouseMan warehouseMan, int totalGoals) {
    this.levelName = levelName;
    this.totalGoals = totalGoals;
    this.stepsOnLevel = 0;
    this.board = board;
    this.warehouseMan = warehouseMan;
    this.movementHistory = new ArrayDeque<>();
  }

  /**
   * Execute the WarehouseMan movement to the given direction
   * changing the cell type of the previous position to EMPTY or GOAL_POSITION
   *
   * @param dirX x direction
   * @param dirY y direction
   */
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

  /**
   * Execute the box movement to the given direction
   * changing the cell type of the previous position to EMPTY or GOAL_POSITION
   *
   * @param x    current x position
   * @param y    current y position
   * @param dirX x direction
   * @param dirY y direction
   */
  private void executeBoxMovement(int x, int y, int dirX, int dirY) {
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

  /**
   * Tries to move a box from its current position in the specified direction.
   * if the destination cell is empty or a goal position, the box is moved
   * 
   * @param x    current x position of the box
   * @param y    current y position of the box
   * @param dirX x direction
   * @param dirY y direction
   * @return true if the box was moved successfully, false if not
   */
  private boolean moveBox(int x, int y, int dirX, int dirY) {
    int newX = x + dirX;
    int newY = y + dirY;
    switch (board.getCell(newX, newY).getCellType()) {
      case EMPTY, GOAL_POSITION:
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

  /**
   * Tries to move the WarehouseMan in the specified direction.
   * If the destination cell is empty or a goal, it's moved.
   * If the destination cell has a box, it tries to move the box too.
   * If the box can be moved both will be moved.
   *
   * @param dirX x direction
   * @param dirY y direction
   * @return true if it was moved successfully, false if not
   */
  public boolean moveWMan(int dirX, int dirY) {
    int newX = warehouseMan.getX() + dirX;
    int newY = warehouseMan.getY() + dirY;
    PosChange posWMan = new PosChange(warehouseMan.getX(), warehouseMan.getY(), newX, newY);
    String direction = getDirectionName(dirX, dirY);
    switch (board.getCell(newX, newY).getCellType()) {
      case EMPTY, GOAL_POSITION:
        State stateWMan = new State(posWMan);
        executeWManMovement(dirX, dirY);
        stepsOnLevel++;
        logger.debug(STEPSUPDATED, stepsOnLevel);
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
          logger.debug(STEPSUPDATED, stepsOnLevel);
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

  /**
   * Undo last player movement, moving the WarehouseMan and the box moved, if
   * there was one, back.
   */
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
    logger.info(STEPSUPDATED, stepsOnLevel);

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

  /**
   * Returns the name of the direction based on the given x and y direction
   * values.
   *
   * @param dirX x direction
   * @param dirY y direction
   * @return ("UP", "DOWN", "LEFT", "RIGHT", or "UNKNOWN")
   */
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

  /**
   * Returns the total number of goals in the level.
   *
   * @return total number of goals
   */
  public int getTotalGoals() {
    return totalGoals;
  }

  /**
   * Returns the total number of steps taken in the level.
   *
   * @return total number of steps
   */
  public int getStepsOnLevel() {
    return stepsOnLevel;
  }

  /**
   * Returns the board of the level.
   *
   * @return Board of the level
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Returns the movement history of the level from the oldest to the most recent
   * movement.
   *
   * @return movement history of the level
   */
  public List<State> getMovementHistory() {
    List<State> hist = new ArrayList<>(movementHistory);
    Collections.reverse(hist);
    return hist;
  }

  /**
   * Returns last State of the movement history
   *
   * @return last State or null if none
   */
  public State getPrevState() {
    if (movementHistory.peek() == null) {
      logger.info("No movement have been done previously at this level");
      return null;
    }
    return movementHistory.pop();
  }

  /**
   * Returns the name of the level.
   *
   * @return name of the level
   */
  public String getLevelName() {
    return levelName;
  }
}
