package es.upm.pproject.sokoban.view;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.sokoban.core.Game;

public class SokobanTUI implements SokobanUI {
  private static final Logger logger = LoggerFactory.getLogger(SokobanTUI.class);
  public Game game;

  public SokobanTUI(Game game) {
    this.game = game;
  }

  private void keyboardInput(char input) {
    switch (input) {
      case 'w':
        game.move(0, -1);
        break;
      case 's':
        game.move(0, 1);
        break;
      case 'a':
        game.move(-1, 0);
        break;
      case 'd':
        game.move(1, 0);
        break;
      case 'r':
        game.restartLevel();
        logger.info("Level restarted.");
        break;
      case 'u':
        game.undoMove();
        break;
      default:
        System.out.println("Invalid input");
        logger.warn("Invalid input.");
    }
  }

  public void loop() {
    Scanner scanner = new Scanner(System.in);
    int currentLevel = game.getCurrentLevel();
    while (currentLevel < 6) {
      System.out.println("Loading level " + currentLevel + "...");
      System.out.println("-------------------------------");
      game.loadCurrentLevel();
      System.out.println(game.getBoard());
      while (!game.levelCompleted()) {
        System.out.println("Enter your move (w/a/s/d to move, r to restart, u to undo) > ");
        String input = scanner.nextLine();
        keyboardInput(input.isEmpty() ? ' ' : input.charAt(0));
        System.out.println("-------------------------------");
        System.out.println(game.getBoard());
      }

      logger.info("Level {} successfully completed in {} steps!", currentLevel, game.getLastLevelSteps());
      System.out.println(
          "Congratulations! You've completed the level " + currentLevel + " in "
              + game.getLevelSteps() + " steps.");

    }

    System.out.println("Congratulations! You've completed all the levels in the game in " + game.getGameSteps());
    scanner.close();
  }

  @Override
  public void start() {
    loop();
  }

}
