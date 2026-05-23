package es.upm.pproject.sokoban.core;

import es.upm.pproject.sokoban.models.Cell.CellType;
import es.upm.pproject.sokoban.models.WarehouseMan;
import es.upm.pproject.sokoban.models.Board;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Game {

    private LevelManager levelManager;
    private int currentLevel;

    public Game(int startingLevel) {
        levelManager = loadLevel(startingLevel);
        currentLevel = startingLevel;
    }

    private void keyboardInput(char input) {
        switch (input) {
            case 'w':
                levelManager.moveWMan(0, -1);
                break;
            case 's':
                levelManager.moveWMan(0, 1);
                break;
            case 'a':
                levelManager.moveWMan(-1, 0);
                break;
            case 'd':
                levelManager.moveWMan(1, 0);
                break;
            case 'r':
                levelManager = loadLevel(currentLevel);
                System.out.println("-------------------------------");
                System.out.println("Level restarted.");
                System.out.println("-------------------------------");
                break;
            default:
                System.out.println("-------------------------------");
                System.out.println("Invalid input.");
                System.out.println("-------------------------------");
        }
    }

    // Level loading
    private static List<String> readLevelFiles(int i) {
        String levelPath = "src/main/java/es/upm/pproject/sokoban/levels/Level" + i + ".txt";
        Path ruta = Path.of(levelPath);
        List<String> lineas = null;

        try {
            lineas = Files.readAllLines(ruta);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineas;
    }

    private LevelManager loadLevel(int num) {
        List<String> levelLines = readLevelFiles(num);

        try {
            if (levelLines == null || levelLines.size() < 3) {
                throw new IllegalArgumentException("Invalid level: " + num);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        int width = 0, height = 0;
        try {
            String[] dimensions = levelLines.get(1).split(",");
            height = Integer.parseInt(dimensions[0]);
            width = Integer.parseInt(dimensions[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid level dimensions in level: " + num);
            System.exit(1);
        }

        if (levelLines.size() < height + 2) {
            System.out.println("Level error: Not enough lines for the specified height in level: " + num);
            System.exit(1);
        }

        Board board = new Board(width, height);
        WarehouseMan warehouseMan = null;
        int boxCount = 0;
        boolean warehouseManFound = false;
        int totalGoals = 0;

        try {
            for (int row = 0; row < height; row++) {
                String line = levelLines.get(row + 2); // Skip level name and dimensions
                for (int column = 0; column < width; column++) {
                    char cellChar = (column < line.length()) ? line.charAt(column) : ' ';
                    CellType cellType;

                    switch (cellChar) {
                        case '+':
                            cellType = CellType.WALL;
                            break;
                        case ' ':
                            cellType = CellType.EMPTY;
                            break;
                        case 'W':
                            if (warehouseManFound) {
                                System.out.println("Level error: Multiple warehouse men found in level: " + num);
                                System.exit(1);
                            }

                            cellType = CellType.WAREHOUSE_MAN;
                            warehouseMan = new WarehouseMan(column, row);
                            warehouseManFound = true;
                            break;
                        case '#':
                            cellType = CellType.BOX;
                            boxCount++;
                            break;
                        case '*':
                            cellType = CellType.GOAL_POSITION;
                            totalGoals++;
                            break;
                        default:
                            cellType = CellType.ERROR;
                            break;

                    }

                    board.setCell(column, row, cellType);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Level error: dimensions provided do not match the map in level: " + num);
            System.exit(1);
        }

        if (!warehouseManFound) {
            System.out.println("Level error: No warehouse man found in level: " + num);
            System.exit(1);
        }

        if (boxCount != totalGoals) {
            System.out.println("Level error: Number of boxes does not match number of goals, level: " + num);
            System.exit(1);
        }

        if (boxCount == 0 && totalGoals == 0) {
            System.out.println("Level error: No boxes or goals found in level: " + num);
            System.exit(1);
        }

        return new LevelManager(board, warehouseMan, totalGoals);
    }

    public void loop() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(levelManager.getBoard());
        while (levelManager.getTotalGoals() > 0) {
            System.out.println("Enter your move (w/a/s/d to move, r to restart) > ");
            String input = scanner.nextLine();
            keyboardInput(input.isEmpty() ? ' ' : input.charAt(0));
            System.out.println("-------------------------------");
            System.out.println(levelManager.getBoard());
        }

        System.out.println(
                "Congratulations! You've completed the level in " + levelManager.getStepsOnLevel() + " steps.");
        scanner.close();
    }
}
