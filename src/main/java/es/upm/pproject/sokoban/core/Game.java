package es.upm.pproject.sokoban.core;

import es.upm.pproject.sokoban.models.Cell.CellType;
import es.upm.pproject.sokoban.models.WarehouseMan;
import es.upm.pproject.sokoban.models.Board;
// import es.upm.pproject.sokoban.models.State;
// import es.upm.pproject.sokoban.models.PosChange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Game {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private LevelManager levelManager;
    private int currentLevel;
    private int stepsOnGame;

    public Game() {
        logger.info("Starting Sokoban game from level: {}", 1);
        currentLevel = 1;
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
                logger.info("Level restarted.");
                break;
            case 'u':
                levelManager.undoMovement();
                break;
            default:
                System.out.println("Invalid input");
                logger.warn("Invalid input.");
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
            logger.error("Failed to read level file at path: {}", levelPath, e);
        }

        return lineas;
    }

    public LevelManager loadLevel(int num) {
        List<String> levelLines = null;
        try {
            levelLines = readLevelFiles(num);
        } catch (Exception e) {
            logger.error("Error loading level: {}", num, e);
            System.exit(1);
        }

        if (levelLines == null || levelLines.size() < 3) {
            logger.error("Level error: Not enough lines in level: {}", num);
            System.exit(1);
        }

        int width = 0, height = 0;
        try {
            String[] dimensions = levelLines.get(1).split(",");
            height = Integer.parseInt(dimensions[0]);
            width = Integer.parseInt(dimensions[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            logger.error("Invalid level dimensions in level: {}", num, e);
            System.exit(1);
        }

        if (levelLines.size() < height + 2) {
            logger.error("Level error: Not enough lines for the specified height ({}) in level: {}", height, num);
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
                                logger.error("Level error: Multiple warehouse men found in level: {}", num);
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
                            logger.error("Level error: Invalid character '{}' found at row {}, column {} in level: {}", cellChar, row, column, num);
                            cellType = CellType.ERROR;
                            break;

                    }

                    board.setCell(column, row, cellType);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            logger.error("Level error: dimensions provided do not match the map in level: {}", num);
            System.exit(1);
        }

        if (!warehouseManFound) {
            logger.error("Level error: No warehouse man found in level: {}", num);
            System.exit(1);
        }

        if (boxCount != totalGoals) {
            logger.error("Level error: Number of boxes does not match number of goals, level: {}", num);
            System.exit(1);
        }

        if (boxCount == 0 && totalGoals == 0) {
            logger.error("Level error: No boxes or goals found in level: {}", num);
            System.exit(1);
        }

        logger.info("Level {} successfully loaded. Dimensions: {}x{}. Total boxes/goals: {}", num, width, height, totalGoals);
        return new LevelManager(board, warehouseMan, totalGoals);
    }

    public void loop() {
        Scanner scanner = new Scanner(System.in);
        while (currentLevel < 6) {
            System.out.println("Loading level " + currentLevel + "...");
            System.out.println("-------------------------------");
            levelManager = loadLevel(currentLevel);
            System.out.println(levelManager.getBoard());
            while (levelManager.getTotalGoals() > 0) {
                System.out.println("Enter your move (w/a/s/d to move, r to restart) > ");
                String input = scanner.nextLine();
                keyboardInput(input.isEmpty() ? ' ' : input.charAt(0));
                System.out.println("-------------------------------");
                System.out.println(levelManager.getBoard());
            }

            logger.info("Level {} successfully completed in {} steps!", currentLevel, levelManager.getStepsOnLevel());
            System.out.println(
                    "Congratulations! You've completed the level " + currentLevel + " in " + levelManager.getStepsOnLevel() + " steps.");
            currentLevel++;
            stepsOnGame += levelManager.getStepsOnLevel();
        }
        System.out.println("Congratulations! You've completed all the levels in the game in " + stepsOnGame);
        scanner.close();
    }
}
