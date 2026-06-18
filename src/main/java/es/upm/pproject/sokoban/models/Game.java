package es.upm.pproject.sokoban.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Represents the Sokoban game, managing levels and player interactions.
 */
public class Game {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private LevelManager levelManager;
    private int currentLevel;
    private int stepsOnGame;
    private int lastLevelSteps;
    private boolean isGameCompleted;

    /**
     * Constructs a new Game instance, with the initial level set to 1 and steps
     * initialized to 0.
     */
    public Game() {
        logger.info("Starting Sokoban game from level: {}", 1);
        currentLevel = 1;
        stepsOnGame = 0;
        isGameCompleted = false;
    }

    // Getters

    /**
     * Returns the current level number.
     *
     * @return current level number
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Returns the LevelManager instance managing the current level.
     *
     * @return LevelManager instance
     */
    public Board getBoard() {
        return levelManager.getBoard();
    }

    /**
     * Returns the number of steps taken in the last completed level.
     *
     * @return steps taken in the last level
     */
    public int getLastLevelSteps() {
        return lastLevelSteps;
    }

    /**
     * Returns the number of steps taken in the current level.
     *
     * @return steps taken in the current level
     */
    public int getLevelSteps() {
        return levelManager.getStepsOnLevel();
    }

    /**
     * Returns the total number of steps taken in the game.
     *
     * @return total steps taken in the game
     */
    public int getGameSteps() {
        return stepsOnGame;
    }

    /**
     * Returns the total number of goals in the current level.
     *
     * @return total goals in the current level
     */
    public int getTotalGoals() {
        return levelManager.getTotalGoals();
    }

    /**
     * Returns the movement history of the current level
     *
     * @return movement history of the current level
     */
    public List<State> getMovementHistory() {
        return levelManager.getMovementHistory();
    }

    /**
     * Returns the name of the current level.
     *
     * @return name of the current level
     */
    public String getLevelName() {
        return levelManager.getLevelName();
    }

    /**
     * Returns whether the game has been completed.
     *
     * @return true if the game is completed, false if not
     */
    public boolean isGameCompleted() {
        return isGameCompleted;
    }

    // Setters

    /**
     * Sets the current level number.
     *
     * @param level new level number
     */
    public void setCurrentLevel(int level) {
        currentLevel = level;
    }

    /**
     * Sets the total number of steps taken in the game.
     *
     * @param steps new total steps
     */
    public void setStepsOnGame(int steps) {
        this.stepsOnGame = steps;
    }

    /**
     * Sets the game completion status.
     *
     * @param completed true if the game is completed, false if not
     */
    public void setGameCompleted(boolean completed) {
        isGameCompleted = completed;
    }

    // Movemente and interaction

    /**
     * Moves the WarehouseMan in the specified direction.
     *
     * @param dx x direction
     * @param dy y direction
     */
    public void move(int dx, int dy) {
        levelManager.moveWMan(dx, dy);
    }

    /**
     * Restarts the current level, creating a new LevelManager instance for the
     * current level.
     */
    public void restartLevel() {
        levelManager = LevelLoader.loadLevel(currentLevel);
        logger.info("Level restarted.");
    }

    /**
     * Undo the last move made in the current level.
     */
    public void undoMove() {
        levelManager.undoMovement();
    }

    /**
     * Loads the current level, creating a LevelManager instance for the
     * current level.
     */
    public void loadCurrentLevel() {
        levelManager = LevelLoader.loadLevel(currentLevel);
    }

    /**
     * Loads the next level, skipping any levels that are not properly formatted.
     *
     * @return true if next level was loaded successfully, false if no more
     *         levels to load
     */
    public boolean loadNextLevel() {
        try {
            currentLevel++;
            while ((levelManager = LevelLoader.loadLevel(currentLevel)) == null) {
                currentLevel++;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Game state

    /**
     * Checks if the current level is completed. If completed, logs the completion
     * and loads the next level.
     *
     * @return true if the level is completed, false if not
     */
    public boolean levelCompleted() {
        if (levelManager.getTotalGoals() == 0) {
            logger.info("Level {} successfully completed in {} steps!", currentLevel, levelManager.getStepsOnLevel());

            lastLevelSteps = levelManager.getStepsOnLevel();
            stepsOnGame += levelManager.getStepsOnLevel();
            if (!loadNextLevel()) {
                isGameCompleted = true;
            }

            return true;
        }

        return false;
    }

    /**
     * Loads a saved game from the file path. If the game is loaded
     * successfully, it initializes the current level, steps, and movements.
     *
     * @param filePath path to the saved game file
     *
     * @return true if the game was loaded successfully, false if not
     */
    public boolean loadGame(String filePath) {
        GameLoader.SaveData saveData = GameLoader.load(filePath);
        if (saveData == null) {
            logger.error("Failed to load game from file: {}", filePath);
            return false;
        }

        this.currentLevel = saveData.levelNumber;
        this.stepsOnGame = saveData.gamePoints;
        this.levelManager = LevelLoader.loadLevel(currentLevel);

        for (char move : saveData.movements.toCharArray()) {
            switch (move) {
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
                default:
                    logger.warn("Unknown movement character '{}' when loading game.", move);
            }
        }

        this.isGameCompleted = false;

        logger.info("Game loaded successfully from file: {}", filePath);
        return true;
    }

    /**
     * Saves the current game to the specified file path.
     *
     * @param filePath path to save the game
     */
    public void saveGame(String filePath) {
        GameSaver.save(this, filePath);
    }
}
