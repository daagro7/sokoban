package es.upm.pproject.sokoban.core;

import es.upm.pproject.sokoban.models.Board;
import es.upm.pproject.sokoban.models.Cell.CellType;
import es.upm.pproject.sokoban.models.WarehouseMan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.nio.file.Path;

class LevelLoader {
  private static final Logger logger = LoggerFactory.getLogger(LevelLoader.class);

  private static List<String> readLevelFiles(int i) {
    String levelPath = "src/main/java/es/upm/pproject/sokoban/levels/Level" + i + ".txt";
    Path ruta = Path.of(levelPath);
    List<String> lineas = null;

    try {
      lineas = Files.readAllLines(ruta);

    } catch (IOException e) {
      logger.error("Failed to read level file at path: {}", levelPath, e);
    }

    return lineas;
  }

  public static LevelManager loadLevel(int num) {
    List<String> levelLines = null;
    try {
      // TODO: SI SE LEE DEBE IR AL SIGUIENTE NIVEL
      levelLines = readLevelFiles(num);
    } catch (Exception e) {
      logger.error("Error loading level: {}", num, e);
      System.exit(1);
    }

    if (levelLines == null || levelLines.size() < 3) {
      logger.error("Level error: Not enough lines in level: {}", num);
      System.exit(1);
    }

    int width = 0, height = 0;
    try {
      String[] dimensions = levelLines.get(1).split(",");
      height = Integer.parseInt(dimensions[0]);
      width = Integer.parseInt(dimensions[1]);
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      logger.error("Invalid level dimensions in level: {}", num, e);
      System.exit(1);
    }

    if (levelLines.size() < height + 2) {
      logger.error("Level error: Not enough lines for the specified height ({}) in level: {}", height, num);
      System.exit(1);
    }

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
      System.exit(1);
    }

    if (!warehouseManFound) {
      logger.error("Level error: No warehouse man found in level: {}", num);
      System.exit(1);
    }

    if (boxCount != totalGoals) {
      logger.error("Level error: Number of boxes does not match number of goals, level: {}", num);
      System.exit(1);
    }

    if (boxCount == 0 && totalGoals == 0) {
      logger.error("Level error: No boxes or goals found in level: {}", num);
      System.exit(1);
    }

    logger.info("Level {} successfully loaded. Dimensions: {}x{}. Total boxes/goals: {}", num, width, height,
        totalGoals);

    return new LevelManager(board, warehouseMan, totalGoals);
  }
}
