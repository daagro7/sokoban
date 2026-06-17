package es.upm.pproject.sokoban.controller;

import es.upm.pproject.sokoban.models.Game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import es.upm.pproject.sokoban.view.Panel;
import es.upm.pproject.sokoban.view.Window;

import javax.swing.JFileChooser;
import java.io.File;

public class SokobanGUI implements KeyListener, SokobanUI {
  private Game game;
  private Window window;
  private Panel panel;

  public SokobanGUI(Game game) {
    this.game = game;
    this.window = new Window();
    this.panel = window.getPanel();

    this.panel.addKeyListener(this);
  }

  private void handleW() {
    if (window.isMenu()) {
      int currentOption = panel.getSelectedOption();

      // Move to the option above or the lasta one
      currentOption = (currentOption > 0 ? currentOption - 1 : panel.getNumberOptions() - 1);
      panel.setSelectedOption(currentOption);
    } else {
      game.move(0, -1);
    }
  }

  private void handleS() {
    if (window.isMenu()) {
      int currentOption = panel.getSelectedOption();

      // Move to the option below or the first one
      currentOption = (currentOption < panel.getNumberOptions() - 1 ? currentOption + 1 : 0);
      panel.setSelectedOption(currentOption);
    } else {
      game.move(0, 1);
    }
  }

  private void handleA() {
    if (!window.isMenu())
      game.move(-1, 0);
  }

  private void handleD() {
    if (!window.isMenu())
      game.move(1, 0);
  }

  private void handleR() {
    if (!window.isMenu()) {
      game.restartLevel();
      panel.setPanel(game.getBoard());
    }
  }

  private void handleU() {
    if (!window.isMenu())
      game.undoMove();
  }

  private void handleEscape() {
    if (!window.isMenu()) {
      window.newLevel(-1);
      panel.setPanel(null);
      panel.setSelectedOption(0);
    }
  }

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

      game.saveGame(filePath);
      levelMenuOptions(0); // Resume game
    }
  }

  private void handleLoadGame() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select a game file to load");

    int selectedFile = fileChooser.showOpenDialog(window);
    if (selectedFile == JFileChooser.APPROVE_OPTION) {
      File fileToLoad = fileChooser.getSelectedFile();
      String filePath = fileToLoad.getAbsolutePath();

      if (game.loadGame(filePath)) {
        window.setLevelName(game.getLevelName());
        panel.setCurrentLevel(game.getCurrentLevel());
        window.newLevel(game.getCurrentLevel());
        panel.setPanel(game.getBoard());
        panel.repaint();
      }
    }
  }

  private void startingMenuOptions(int currentOption) {
    if (currentOption == 0) { // NEW GAME
      game.setCurrentLevel(1);
      game.loadCurrentLevel();
      window.setLevelName(game.getLevelName());
      panel.setCurrentLevel(game.getCurrentLevel());
      game.setStepsOnGame(0);
      window.newLevel(-2);
    } else if (currentOption == 1) { // LOAD GAME
      handleLoadGame();
    } else if (currentOption == 2) { // EXIT
      window.closeApplication();
    }
  }

  private void levelMenuOptions(int currentOption) {
    if (currentOption == 0) { // RESUME
      window.newLevel(this.game.getCurrentLevel());
      panel.setPanel(this.game.getBoard());
    } else if (currentOption == 1) { // SAVE GAME
      handleSaveGame();
    } else if (currentOption == 2) { // RESTART LEVEL
      game.restartLevel();
      panel.setPanel(game.getBoard());
      levelMenuOptions(0);
    } else if (currentOption == 3) { // UNDO MOVEMENT
      game.undoMove();
      levelMenuOptions(0);
    } else if (currentOption == 4) { // QUIT
      window.newLevel(0);
      panel.setPanel(null);
      panel.setSelectedOption(0);
    }
  }

  private void handleEnterOrSpace() {
    int currentOption = panel.getSelectedOption();
    if (!window.isMenu()) // Skip if not in menu
      return;

    if (window.getLevelPanel() == 0) { // Starting menu
      startingMenuOptions(currentOption);
    } else if (window.getLevelPanel() == -1) { // Pause menu
      levelMenuOptions(currentOption);
    } else if (window.getLevelPanel() == -2) { // Level/Congratulations panel
      if (game.isGameCompleted()) { // Game completed
        window.newLevel(0);
        panel.setPanel(null);
      } else { // Level completed
        window.newLevel(game.getCurrentLevel());
        panel.setPanel(game.getBoard());
      }
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
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

    panel.repaint();
    if (!window.isMenu()) {
      window.setStepsOnGame(game.getGameSteps() + game.getLevelSteps());
      window.updateSteps(game.getLevelSteps());

      // If level completed update the window and panel to the next level
      if (game.levelCompleted()) {
        if (game.isGameCompleted())
          panel.setCompleted(true);

        panel.setCurrentLevel(game.getCurrentLevel());
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

  @Override
  public void start() {
    // There is no need for loop, just start the game
    panel.setPanel(null);
    window.newLevel(0);
  }

}
