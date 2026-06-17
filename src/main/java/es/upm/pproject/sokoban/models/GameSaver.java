package es.upm.pproject.sokoban.models;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.lang.StringBuilder;

public class GameSaver {
  private static final Logger logger = LoggerFactory.getLogger(GameSaver.class);

  public static void save(Game game, String filePath) {
    Path path = Paths.get(filePath);
    StringBuilder save = new StringBuilder();

    int levelName = game.getCurrentLevel();
    int gamePoints = game.getGameSteps();
    List<State> movementHistory = game.getMovementHistory();

    // Fill save text
    save.append(String.format("%d\n", levelName));
    save.append(String.format("%d\n", gamePoints));
    for (State s : movementHistory) {
      PosChange wManChange = s.getWMan();
      int dirx = wManChange.getNextX() - wManChange.getPreviousX();
      int diry = wManChange.getNextY() - wManChange.getPreviousY();

      if (dirx == 0 && diry == -1) { // UP
        save.append("w");
      } else if (dirx == 0 && diry == 1) { // DOWN
        save.append("s");
      } else if (dirx == -1 && diry == 0) { // LEFT
        save.append("a");
      } else if (dirx == 1 && diry == 0) { // RIGHT
        save.append("d");
      }
    }

    try {
      Files.write(path, save.toString().getBytes());
    } catch (Exception e) {
      logger.error("Error saving game to file: {}", e.getMessage());
    }
  }
}
