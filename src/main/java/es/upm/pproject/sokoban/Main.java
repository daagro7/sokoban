package es.upm.pproject.sokoban;

import es.upm.pproject.sokoban.models.Game;

import es.upm.pproject.sokoban.controller.SokobanUI;
import es.upm.pproject.sokoban.controller.SokobanTUI;
import es.upm.pproject.sokoban.controller.SokobanGUI;

public class Main {
  public static void main(String[] args) {
    Game game = new Game();
    SokobanUI ui;

    // If users want tui or gui
    if (args.length > 0 && args[0].equalsIgnoreCase("tui")) {
      ui = new SokobanTUI(game);
    } else {
      ui = new SokobanGUI(game);
    }

    ui.start();
  }
}
