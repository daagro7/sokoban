package es.upm.pproject.sokoban.models;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for loading game data from a file.
 */
public class GameLoader {

  private static final Logger logger = LoggerFactory.getLogger(GameLoader.class);

  /**
   * Private constructor to prevent instantiation of the GameLoader class.
   */
  private GameLoader() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Class representing the data loaded from a save file.
   */
  public static class SaveData {
    public final int levelNumber;
    public final int gamePoints;
    public final String movements;

    /**
     * Constructs a SaveData instance
     *
     * @param levelNumber level number
     * @param gamePoints  game points
     * @param movements   movements made in the game
     */
    public SaveData(int levelNumber, int gamePoints, String movements) {
      this.levelNumber = levelNumber;
      this.gamePoints = gamePoints;
      this.movements = movements;
    }
  }

  /**
   * Loads game data from a specified file path.
   * 
   * @param filePath path to the save file
   * @return SaveData object with the game data, null if error
   */
  public static SaveData load(String filePath) {
    try {
      Path path = Paths.get(filePath);
      List<String> lines = Files.readAllLines(path);

      int level = Integer.parseInt(lines.get(0).trim());
      int points = Integer.parseInt(lines.get(1).trim());
      String moves = lines.size() >= 3 ? lines.get(2).trim() : "";

      logger.info("Loaded game from file: Level {}, Points {}, Moves {}", level, points, moves);

      return new SaveData(level, points, moves);

    } catch (Exception e) {
      logger.error("Error loading game from file: {}", e.getMessage());
      return null;
    }
  }
}
