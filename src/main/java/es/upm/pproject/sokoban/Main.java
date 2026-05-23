package es.upm.pproject.sokoban;

import es.upm.pproject.sokoban.core.Game;

public class Main {
  public static void main(String[] args) {
    System.out.println("Welcome to sokoban. w, a, s, d to move and r to restart");
    Game game = new Game(1);
    game.loop();
  }
}
