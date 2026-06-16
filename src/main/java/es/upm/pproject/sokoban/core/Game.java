package es.upm.pproject.sokoban.core;

import es.upm.pproject.sokoban.models.Cell.CellType;
import es.upm.pproject.sokoban.view.Window;
import es.upm.pproject.sokoban.view.Panel;
import es.upm.pproject.sokoban.models.WarehouseMan;
import es.upm.pproject.sokoban.models.Board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Game implements KeyListener {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private LevelManager levelManager;
    private int currentLevel;
    private int stepsOnGame;
    private Panel panel;
    private Window window;

    public Game(Window window, Panel panel) {
        this.window = window;
        this.panel = panel;
        logger.info("Starting Sokoban game from level: {}", 1);
        currentLevel = 1;
    }

    private void keyboardInput(char input) {
        if (window.getLevelPanel() == 0 || window.getLevelPanel() == -1) { // Menus
            int currentOption = panel.getSelectedOption();
            switch (input) {
                case 'w': // Move to the option above or the lasta one
                    currentOption = (currentOption > 0 ? currentOption - 1 : panel.getNumberOptions()-1);
                    panel.setSelectedOption(currentOption);
                    break;
                case 's': // Move to the option below or the first one
                    currentOption = (currentOption < panel.getNumberOptions()-1 ? currentOption + 1 : 0);
                    panel.setSelectedOption(currentOption);
                    break;
                case ' ': // Confirm option
                    if (window.getLevelPanel() == 0) {
                        startingMenuOptions(currentOption);
                    }
                    else {
                        levelMenuOptions(currentOption);
                    }
                    break;
                default:
            }
        }
        else if (window.getLevelPanel() > 0) { // Levels movements
            switch (input) {
                case 'w': // Move up
                    levelManager.moveWMan(0, -1);
                    break;
                case 's': // Move  down
                    levelManager.moveWMan(0, 1);
                    break;
                case 'a': // Move left
                    levelManager.moveWMan(-1, 0);
                    break;
                case 'd': // Move rigth
                    levelManager.moveWMan(1, 0);
                    break;
                case 'r': // Restart level
                    levelManager = loadLevel(currentLevel);
                    panel.setPanel(levelManager.getBoard());
                    logger.info("Level restarted.");
                    break;
                case 'u': // Undo movement
                    levelManager.undoMovement();
                    break;
                case 'm': // Open level menu
                    window.newLevel(-1);
                    panel.setPanel(null);
                    break;
                default:
                    System.out.println("Invalid input");
                    logger.warn("Invalid input.");
            }
        }
    }

    public void startingMenuOptions(int currentOption) {
        if (currentOption == 0) { // NEW GAME
            levelManager = loadLevel(currentLevel);
            window.newLevel(currentLevel);
            panel.setPanel(levelManager.getBoard());
        }
        else if (currentOption == 1) { // LOAD GAME
            // TODO: Load game logic
        }
        else if (currentOption == 2) { // CLOSE SOKOBAN
            // TODO: Close game logic
            System.exit(1);
        }
    }

    public void levelMenuOptions(int currentOption) {
        if (currentOption == 0) { // RESUME
            window.newLevel(currentLevel);
            panel.setPanel(levelManager.getBoard());
        }
        else if (currentOption == 1) { // SAVE GAME
            // TODO: Save game logic
        }
        else if (currentOption == 2) { // QUIT
            currentLevel = 1;
            window.newLevel(0);
            panel.setPanel(null);
            panel.setSelectedOption(0);
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
                            logger.error("Level error: Invalid character '{}' found at row {}, column {} in level: {}",
                                    cellChar, row, column, num);
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

        logger.info("Level {} successfully loaded. Dimensions: {}x{}. Total boxes/goals: {}", num, width, height,
                totalGoals);
        return new LevelManager(board, warehouseMan, totalGoals);
    }

    public void loopTUI() {
        Scanner scanner = new Scanner(System.in);
        while (currentLevel < 6) {
            System.out.println("Loading level " + currentLevel + "...");
            System.out.println("-------------------------------");
            levelManager = loadLevel(currentLevel);
            System.out.println(levelManager.getBoard());
            panel.setPanel(levelManager.getBoard());
            while (levelManager.getTotalGoals() > 0) {
                System.out.println("Enter your move (w/a/s/d to move, r to restart, u to undo) > ");
                String input = scanner.nextLine();
                keyboardInput(input.isEmpty() ? ' ' : input.charAt(0));
                System.out.println("-------------------------------");
                System.out.println(levelManager.getBoard());
            }

            logger.info("Level {} successfully completed in {} steps!", currentLevel, levelManager.getStepsOnLevel());
            System.out.println(
                    "Congratulations! You've completed the level " + currentLevel + " in "
                            + levelManager.getStepsOnLevel() + " steps.");
            currentLevel++;
            stepsOnGame += levelManager.getStepsOnLevel();
        }
        System.out.println("Congratulations! You've completed all the levels in the game in " + stepsOnGame);
        scanner.close();
    }

    /*******
     * GUI *
     *******/
    @Override
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();
        char key = 'p';

        switch (tecla) {
            case KeyEvent.VK_W:
                key = 'w';
                break;
            case KeyEvent.VK_S:
                key = 's';
                break;
            case KeyEvent.VK_A:
                key = 'a';
                break;
            case KeyEvent.VK_D:
                key = 'd';
                break;
            case KeyEvent.VK_R:
                key = 'r';
                break;
            case KeyEvent.VK_U:
                key = 'u';
                break;
            case KeyEvent.VK_ESCAPE:
                key = 'm';
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                key = ' ';
            default:
        }

        keyboardInput(key);
        panel.repaint();
        if (window.getLevelPanel() > 0) {
            window.updateSteps(levelManager.getStepsOnLevel());
            levelCompleted();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}


    public void loopGUI() {
        // TODO: Make the start menu
        panel.setPanel(null);
        window.newLevel(0);
    }

    public boolean levelCompleted() {
        if (levelManager.getTotalGoals() == 0) {
            logger.info("Level {} successfully completed in {} steps!", currentLevel, levelManager.getStepsOnLevel());
            // TODO: Change prints
            System.out.println(
                    "Congratulations! You've completed the level " + currentLevel + " in "
                            + levelManager.getStepsOnLevel() + " steps.");
            currentLevel++;
            stepsOnGame += levelManager.getStepsOnLevel();
            
            if (currentLevel > 5) {
                System.out.println("Congratulations! You've completed all the levels in the game in " + stepsOnGame);
                System.exit(1);
            }

            levelManager = loadLevel(currentLevel);
            window.newLevel(currentLevel);
            panel.setPanel(levelManager.getBoard());
            return true;
        }
        return false;
    }
}
