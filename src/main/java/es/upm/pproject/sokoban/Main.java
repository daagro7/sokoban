package es.upm.pproject.sokoban;

import es.upm.pproject.sokoban.core.Game;
import es.upm.pproject.sokoban.view.Window;
import es.upm.pproject.sokoban.view.Panel;

public class Main {
  public static void main(String[] args) {
    Window window = new Window();
    Panel panel = window.getPanel();
    System.out.println("Welcome to sokoban. w, a, s, d to move, r to restart and u to undo a movement");
    Game game = panel.getGame();
    // game.loopTUI(); // TUI
    game.loopGUI(); // GUI
  }
}
