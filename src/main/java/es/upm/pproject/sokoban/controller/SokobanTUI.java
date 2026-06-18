package es.upm.pproject.sokoban.controller;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.sokoban.models.Game;

/**
 * A TUI for the Sokoban game.
 */
public class SokobanTUI implements SokobanUI {
  private static final Logger logger = LoggerFactory.getLogger(SokobanTUI.class);
  private Game game;

  /**
   * Constructs a SokobanTUI with the specified game.
   *
   * @param game game instance to be controlled
   */
  public SokobanTUI(Game game) {
    this.game = game;
  }

  /**
   * Processes input from the keyboard and does it's corresponding action in the
   * game.
   *
   * @param input character input
   */
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
        logger.warn("Invalid input.");
    }
  }

  /**
   * TUI game loop that handles user input and game state until all levels are
   * completed.
   */
  public void loop() {
    Scanner scanner = new Scanner(System.in);
    int currentLevel = game.getCurrentLevel();
    while (currentLevel <= 1) {
      logger.info("Loading level {}...", currentLevel);
      logger.info("-------------------------------");
      game.loadCurrentLevel();
      if (logger.isInfoEnabled()) {
        logger.info(game.getBoard().toString());
      }
      while (!game.levelCompleted()) {
        logger.info("Enter your move (w/a/s/d to move, r to restart, u to undo) > ");
        String input = scanner.nextLine();
        keyboardInput(input.isEmpty() ? ' ' : input.charAt(0));
        logger.info("-------------------------------");
        if (logger.isInfoEnabled()) {
          logger.info(game.getBoard().toString());
        }
      }

      logger.info("Level {} successfully completed in {} steps!", currentLevel, game.getLastLevelSteps());

      currentLevel++;
    }

    logger.info("Congratulations! You've completed all the levels in the game in {}", game.getGameSteps());
    scanner.close();
  }

  /**
   * Starts the TUI game loop.
   */
  @Override
  public void start() {
    loop();
  }

}
