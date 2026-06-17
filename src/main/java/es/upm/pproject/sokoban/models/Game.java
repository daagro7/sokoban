package es.upm.pproject.sokoban.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private LevelManager levelManager;
    private int currentLevel;
    private int stepsOnGame;
    private int lastLevelSteps;

    public Game() {
        logger.info("Starting Sokoban game from level: {}", 1);
        currentLevel = 1;
        stepsOnGame = 0;
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

    // Setters
    public void setCurrentLevel(int level) {
        currentLevel = level;
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

    // Game state
    public boolean levelCompleted() {
        if (levelManager.getTotalGoals() == 0) {
            logger.info("Level {} successfully completed in {} steps!", currentLevel, levelManager.getStepsOnLevel());

            stepsOnGame += levelManager.getStepsOnLevel();
            currentLevel++;
            if (currentLevel > 2) {
                // stepsOnGame += levelManager.getStepsOnLevel();
                // logger.info("Congratulations! You've completed all the levels in the game in
                // {} steps!", stepsOnGame + levelManager.getStepsOnLevel());
                // currentLevel = 1;
                // window.newLevel(0);
                // panel.setPanel(null);
                return true;
            }

            lastLevelSteps = levelManager.getStepsOnLevel();
            levelManager = LevelLoader.loadLevel(currentLevel);
            return true;
        }

        return false;
    }

    public void setStepsOnGame(int steps) {
        this.stepsOnGame = steps;
    }
}
