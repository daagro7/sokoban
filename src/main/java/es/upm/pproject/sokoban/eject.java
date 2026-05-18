package es.upm.pproject.sokoban;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class eject {

    public static List<String> readLevelFiles(int i) {
        String levelPath = "src/main/java/es/upm/pproject/sokoban/Levels/Level" + i + ".txt";
        Path ruta = Path.of(levelPath);
        List<String> lineas = null;

        try {
            lineas = Files.readAllLines(ruta);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineas;
    }

    public void createBoard() {}


    public static void main(String[] args) {
        System.out.println("Welcome to sokoban. w, a, s, d to move and r to restart");
        List<String> fileLines = readLevelFiles(1);
        String sizeMap[] = fileLines.get(1).split(",");
        int width = Integer.parseInt(sizeMap[0]);
        int height = Integer.parseInt(sizeMap[1]);
        Game game = new Game(width, height, fileLines);
        game.initializeBoard();
        game.gameLoop(1);
    }
}
