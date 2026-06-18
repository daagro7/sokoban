package es.upm.pproject.sokoban.controller;

import es.upm.pproject.sokoban.models.Game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import es.upm.pproject.sokoban.view.Panel;
import es.upm.pproject.sokoban.view.Window;

import javax.swing.JFileChooser;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A GUI for the Sokoban game.
 */
public class SokobanGUI implements KeyListener, SokobanUI {
  private static final Logger logger = LoggerFactory.getLogger(SokobanGUI.class);

  private Game game;
  private Window window;
  private Panel panel;

  /**
   * Constructs a SokobanGUI with the specified game.
   *
   * @param game game instance to be controlled
   */
  public SokobanGUI(Game game) {
    this.game = game;
    logger.info("Initializing SokobanGUI.");
    this.window = new Window();
    this.panel = window.getPanel();

    this.panel.addKeyListener(this);
    logger.info("SokobanGUI initialized and key listener attached.");
  }

  /**
   * Handles the w key input, moving in the menu or in the game.
   */
  private void handleW() {
    if (window.isMenu()) {
      int currentOption = panel.getSelectedOption();

      // Move to the option above or the lasta one
      currentOption = (currentOption > 0 ? currentOption - 1 : panel.getNumberOptions() - 1);
      panel.setSelectedOption(currentOption);
      logger.debug("Menu selection moved up to option {}.", currentOption);
    } else {
      logger.debug("Moving player up.");
      game.move(0, -1);
    }
  }

  /**
   * Handles the s key input, moving in the menu or in the game.
   */
  private void handleS() {
    if (window.isMenu()) {
      int currentOption = panel.getSelectedOption();

      // Move to the option below or the first one
      currentOption = (currentOption < panel.getNumberOptions() - 1 ? currentOption + 1 : 0);
      panel.setSelectedOption(currentOption);
      logger.debug("Menu selection moved down to option {}.", currentOption);
    } else {
      logger.debug("Moving player down.");
      game.move(0, 1);
    }
  }

  /**
   * Handles the a key input, moving in the game.
   */
  private void handleA() {
    if (!window.isMenu()) {
      logger.debug("Moving player left.");
      game.move(-1, 0);
    }
  }

  /**
   * Handles the d key input, moving in the game.
   */
  private void handleD() {
    if (!window.isMenu()) {
      logger.debug("Moving player right.");
      game.move(1, 0);
    }
  }

  /**
   * Handles the r key input, restarting the level in the game.
   */
  private void handleR() {
    if (!window.isMenu()) {
      logger.info("Restarting current level from keyboard input.");
      game.restartLevel();
      panel.setPanel(game.getBoard());
    }
  }

  /**
   * Handles the u key input, undo to the last move in the game.
   */
  private void handleU() {
    if (!window.isMenu()) {
      logger.info("Undoing last movement from keyboard input.");
      game.undoMove();
    }
  }

  /**
   * Handles the esc key input, showing the pause menu
   */
  private void handleEscape() {
    if (!window.isMenu()) {
      logger.info("Opening pause menu.");
      window.newLevel(-1);
      panel.setPanel(null);
      panel.setSelectedOption(0);
    }
  }

  /**
   * Handles the save game, user choose a file to save the game.
   */
  private void handleSaveGame() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select where to save the game");

    int selectedFile = fileChooser.showSaveDialog(window);
    if (selectedFile == JFileChooser.APPROVE_OPTION) {
      File fileToSave = fileChooser.getSelectedFile();
      String filePath = fileToSave.getAbsolutePath();

      if (!filePath.endsWith(".txt")) {
        filePath += ".txt";
      }

      logger.info("Saving game to {}.", filePath);
      game.saveGame(filePath);
      window.newLevel(this.game.getCurrentLevel());
      panel.setPanel(this.game.getBoard());
      panel.repaint();
    } else {
      logger.debug("Save game dialog cancelled.");
    }
  }

  /**
   * Handles the load game, user choose a file to load the game.
   */
  private void handleLoadGame() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select a game file to load");

    int selectedFile = fileChooser.showOpenDialog(window);
    if (selectedFile == JFileChooser.APPROVE_OPTION) {
      File fileToLoad = fileChooser.getSelectedFile();
      String filePath = fileToLoad.getAbsolutePath();

      logger.info("Loading game from {}.", filePath);

      if (game.loadGame(filePath)) {
        panel.setCompleted(false);
        window.setLevelName(game.getLevelName());
        window.newLevel(game.getCurrentLevel());
        panel.setPanel(game.getBoard());
        panel.repaint();
      } else {
        logger.warn("Game load failed for {}.", filePath);
      }
    } else {
      logger.debug("Load game dialog cancelled.");
    }
  }

  /**
   * Handles the options in the starting menu.
   *
   * @param currentOption currently selected option in the menu
   */
  private void startingMenuOptions(int currentOption) {
    if (currentOption == 0) { // NEW GAME
      logger.info("Starting a new game from the main menu.");
      game.setGameCompleted(false);
      panel.setCompleted(false);
      game.setCurrentLevel(1);
      game.loadCurrentLevel();
      window.setLevelName(game.getLevelName());
      game.setStepsOnGame(0);
      window.newLevel(-2);
    } else if (currentOption == 1) { // LOAD GAME
      logger.info("Opening load game dialog from the main menu.");
      handleLoadGame();
    } else if (currentOption == 2) { // EXIT
      logger.info("Exiting game from the main menu.");
      window.closeApplication();
    }
  }

  /**
   * Handles the options in the level menu.
   *
   * @param currentOption currently selected option in the menu
   */
  private void levelMenuOptions(int currentOption) {
    if (currentOption == 0) { // RESUME
      logger.info("Resuming current level.");
      window.newLevel(this.game.getCurrentLevel());
      panel.setPanel(this.game.getBoard());
    } else if (currentOption == 1) { // SAVE GAME
      logger.info("Opening save game dialog from pause menu.");
      handleSaveGame();
    } else if (currentOption == 2) { // RESTART LEVEL
      logger.info("Restarting current level from pause menu.");
      game.restartLevel();
      panel.setPanel(game.getBoard());
      levelMenuOptions(0);
    } else if (currentOption == 3) { // UNDO MOVEMENT
      logger.info("Undoing movement from pause menu.");
      game.undoMove();
      levelMenuOptions(0);
    } else if (currentOption == 4) { // QUIT
      logger.info("Returning to main menu from pause menu.");
      window.newLevel(0);
      panel.setPanel(null);
      panel.setSelectedOption(0);
    }
  }

  /**
   * Handles the enter or space key input, executing the selected option in the
   * menu.
   */
  private void handleEnterOrSpace() {
    int currentOption = panel.getSelectedOption();
    if (!window.isMenu()) // Skip if not in menu
      return;

    if (window.getLevelPanel() == 0) { // Starting menu
      logger.debug("Enter/space pressed in main menu with selected option {}.", currentOption);
      startingMenuOptions(currentOption);
    } else if (window.getLevelPanel() == -1) { // Pause menu
      logger.debug("Enter/space pressed in pause menu with selected option {}.", currentOption);
      levelMenuOptions(currentOption);
    } else if (window.getLevelPanel() == -2) { // Level/Congratulations panel
      logger.debug("Enter/space pressed in level-completed panel.");
      if (game.isGameCompleted()) { // Game completed
        logger.info("All levels completed. Returning to main menu.");
        window.newLevel(0);
        panel.setPanel(null);
      } else { // Level completed
        logger.info("Proceeding to next level {}.", game.getCurrentLevel());
        window.newLevel(game.getCurrentLevel());
        panel.setPanel(game.getBoard());
      }
    }
  }

  /**
   * Handles the key pressed events
   * 
   * @param e KeyEvent of the key pressed
   */
  @Override
  public void keyPressed(KeyEvent e) {
    logger.debug("Key pressed: {}.", KeyEvent.getKeyText(e.getKeyCode()));
    switch (e.getKeyCode()) {
      case KeyEvent.VK_W:
        handleW();
        break;
      case KeyEvent.VK_S:
        handleS();
        break;
      case KeyEvent.VK_A:
        handleA();
        break;
      case KeyEvent.VK_D:
        handleD();
        break;
      case KeyEvent.VK_R:
        handleR();
        break;
      case KeyEvent.VK_U:
        handleU();
        break;
      case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE:
        handleEnterOrSpace();
        break;
      case KeyEvent.VK_ESCAPE:
        handleEscape();
        break;
      default:
        break;
    }

    // Update the panel and window after each turn if not in menu
    panel.repaint();
    if (!window.isMenu()) {
      logger.debug("Refreshing window state after gameplay input.");
      window.setStepsOnGame(game.getGameSteps() + game.getLevelSteps());
      window.updateSteps(game.getLevelSteps());

      // If level completed update the window and panel to the next level
      if (game.levelCompleted()) {
        logger.info("Level {} completed.", game.getCurrentLevel());
        if (game.isGameCompleted())
          panel.setCompleted(true);

        window.setLevelName(game.getLevelName());
        window.newLevel(-2);
        panel.setPanel(null);
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // Not used in our game. Only needed the use of keyPressed method.
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // Not used in our game. Only needed the use of keyPressed method.
  }

  /**
   * Starts the Sokoban game loading the menu
   */
  @Override
  public void start() {
    // There is no need for loop, just start the game
    logger.info("Starting Sokoban GUI flow.");
    panel.setPanel(null);
    window.newLevel(0);
  }

}
