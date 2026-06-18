package es.upm.pproject.sokoban.models;

import es.upm.pproject.sokoban.models.Cell.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Class for loading Sokoban levels from level files.
 */
class LevelLoader {
  private static final Logger logger = LoggerFactory.getLogger(LevelLoader.class);

  /**
   * Private constructor to prevent instantiation of the LevelLoader class.
   */
  private LevelLoader() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Reads the level file given the level number
   *
   * @param i the level number
   * @return list of lines of the file, null if error
   */
  private static List<String> readLevelFiles(int i) {
    List<String> list = null;
    String levelPath = "/levels/level_" + i + ".txt";

    try (InputStream is = LevelLoader.class.getResourceAsStream(levelPath)) {
      if (is == null) {
        logger.error("Failed to find level file at path: {}", levelPath);
        return list;
      }
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
        list = reader.lines().toList();
        return list;
      }
    } catch (IOException e) {
      logger.error("Failed to read level file at path: {}", levelPath, e);
      return list;
    }
  }

  /**
   * Loads a level given the level number
   *
   * @param num level number
   * @return LevelManager instance for the level, null if error
   */
  public static LevelManager loadLevel(int num) {
    List<String> levelLines = null;
    try {
      levelLines = readLevelFiles(num);
    } catch (Exception e) {
      logger.error("Error loading level: {}", num, e);
      System.exit(1);
    }

    if (levelLines == null) {
      logger.error("Level error: Level file not found for level: {}", num);
      throw new IllegalArgumentException("Level file not found for level: " + num);
    }

    if (levelLines.size() < 3) {
      logger.error("Level error: Not enough lines in level: {}", num);
      return null;
    }

    int width = 0;
    int height = 0;
    String levelName = levelLines.get(0).trim();
    try {
      String[] dimensions = levelLines.get(1).split(",");
      height = Integer.parseInt(dimensions[0]);
      width = Integer.parseInt(dimensions[1]);
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      logger.error("Invalid level dimensions in level: {}", num, e);
      return null;
    }

    if (levelLines.size() < height + 2) {
      logger.error("Level error: Not enough lines for the specified height ({}) in level: {}", height, num);
      return null;
    }

    return buildLevelManager(levelLines, num, width, height, levelName);
  }

  /**
   * Builds a LevelManager instance from the level lines.
   *
   * @param levelLines list of lines from the level file
   * @param num        level number
   * @param width      width of the level
   * @param height     height of the level
   * @param levelName  name of the level
   * @return LevelManager instance for the level, null if error
   */
  private static LevelManager buildLevelManager(List<String> levelLines, int num, int width, int height,
      String levelName) {
    Board board = new Board(width, height);
    WarehouseMan warehouseMan = null;
    int boxCount = 0;
    boolean warehouseManFound = false;
    int totalGoals = 0;

    try {
      for (int row = 0; row < height; row++) {
        String line = levelLines.get(row + 2); // Skip level name and dimensions
        for (int column = 0; column < width; column++) {
          char cellChar = (column < line.length()) ? line.charAt(column) : ' ';
          CellType cellType;

          switch (cellChar) {
            case '+':
              cellType = CellType.WALL;
              break;
            case ' ':
              cellType = CellType.EMPTY;
              break;
            case 'W':
              if (warehouseManFound) {
                logger.error("Level error: Multiple warehouse men found in level: {}", num);
                System.exit(1);
              }
              cellType = CellType.WAREHOUSE_MAN;
              warehouseMan = new WarehouseMan(column, row);
              warehouseManFound = true;
              break;
            case '#':
              cellType = CellType.BOX;
              boxCount++;
              break;
            case '*':
              cellType = CellType.GOAL_POSITION;
              totalGoals++;
              break;
            default:
              logger.error("Level error: Invalid character '{}' found at row {}, column {} in level: {}",
                  cellChar, row, column, num);
              cellType = CellType.ERROR;
              break;
          }
          board.setCell(column, row, cellType);
        }
      }
    } catch (IndexOutOfBoundsException e) {
      logger.error("Level error: dimensions provided do not match the map in level: {}", num);
      return null;
    }

    if (!isValidLevel(warehouseManFound, boxCount, totalGoals, num)) {
      return null;
    }

    logger.info("Level {} successfully loaded. Dimensions: {}x{}. Total boxes/goals: {}", num, width, height,
        totalGoals);

    return new LevelManager(levelName, board, warehouseMan, totalGoals);

  }

  private static boolean isValidLevel(boolean warehouseManFound, int boxCount, int totalGoals, int num) {
    if (!warehouseManFound) {
      logger.error("Level error: No warehouse man found in level: {}", num);
      return false;
    }

    if (boxCount != totalGoals) {
      logger.error("Level error: Number of boxes does not match number of goals, level: {}", num);
      return false;
    }

    if (boxCount == 0) { // No boxes or goals found
      logger.error("Level error: No boxes or goals found in level: {}", num);
      return false;
    }

    return true;
  }
}
