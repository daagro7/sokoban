package es.upm.pproject.sokoban;

import es.upm.pproject.sokoban.core.Game;

import es.upm.pproject.sokoban.view.SokobanUI;
import es.upm.pproject.sokoban.view.SokobanTUI;
import es.upm.pproject.sokoban.view.SokobanGUI;

public class Main {
  public static void main(String[] args) {
    Game game = new Game();
    SokobanUI tui = new SokobanTUI(game);
    SokobanUI gui = new SokobanGUI(game);

    // If users want tui or gui
    if (args.length > 0 && args[0].equalsIgnoreCase("tui")) {
      tui.start();
    } else {
      gui.start();
    }
  }
}
