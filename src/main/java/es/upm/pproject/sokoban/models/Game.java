package es.upm.pproject.sokoban.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Game {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private LevelManager levelManager;
    private int currentLevel;
    private int stepsOnGame;
    private int lastLevelSteps;
    private boolean isGameCompleted;

    public Game() {
        logger.info("Starting Sokoban game from level: {}", 1);
        currentLevel = 1;
        stepsOnGame = 0;
        isGameCompleted = false;
    }

    // Getters
    public int getCurrentLevel() {
        return currentLevel;
    }

    public Board getBoard() {
        return levelManager.getBoard();
    }

    public int getLastLevelSteps() {
        return lastLevelSteps;
    }

    public int getLevelSteps() {
        return levelManager.getStepsOnLevel();
    }

    public int getGameSteps() {
        return stepsOnGame;
    }

    public int getTotalGoals() {
        return levelManager.getTotalGoals();
    }

    public List<State> getMovementHistory() {
        return levelManager.getMovementHistory();
    }

    public String getLevelName() {
        return levelManager.getLevelName();
    }

    public boolean isGameCompleted() {
        return isGameCompleted;
    }

    // Setters
    public void setCurrentLevel(int level) {
        currentLevel = level;
    }

    public void setStepsOnGame(int steps) {
        this.stepsOnGame = steps;
    }

    // Movemente and interaction
    public void move(int dx, int dy) {
        levelManager.moveWMan(dx, dy);
    }

    public void restartLevel() {
        levelManager = LevelLoader.loadLevel(currentLevel);
        logger.info("Level restarted.");
    }

    public void undoMove() {
        levelManager.undoMovement();
    }

    public void loadCurrentLevel() {
        levelManager = LevelLoader.loadLevel(currentLevel);
    }

    // Load next level, skipping any wrong formatted levels
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

        logger.info("Game loaded successfully from file: {}", filePath);
        return true;
    }

    public void saveGame(String filePath) {
        GameSaver.save(this, filePath);
    }
}
