package es.upm.pproject.sokoban.models;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameLoader {

  private static final Logger logger = LoggerFactory.getLogger(GameLoader.class);

  public static class SaveData {
    public int levelNumber;
    public int gamePoints;
    public String movements;

    public SaveData(int levelNumber, int gamePoints, String movements) {
      this.levelNumber = levelNumber;
      this.gamePoints = gamePoints;
      this.movements = movements;
    }
  }

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
