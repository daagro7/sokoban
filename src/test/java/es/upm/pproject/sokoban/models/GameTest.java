package es.upm.pproject.sokoban.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.loadCurrentLevel();
    }

    @Test
    void testRestartLevelResetsSteps() {
        game.move(0, 1);
        assertEquals(1, game.getLevelSteps(), "Steps on level should be 1.");
        game.restartLevel();
        assertEquals(0, game.getLevelSteps(), "Steps should reset to 0 after restart.");
        assertEquals(1, game.getCurrentLevel(), "Should remain on level 1.");
    }

    @Test
    void testUndoMovement() {
        assertEquals(0, game.getLevelSteps());
        game.move(0, 1);
        assertEquals(1, game.getLevelSteps(), "Steps should be 1 after moving.");
        game.undoMove();
        assertEquals(0, game.getLevelSteps(), "Steps should return to 0 after undo.");
    }

    @Test
    void testSaveAndLoadGame() {
        game.move(0, 1);
        int stepsBeforeSave = game.getLevelSteps();
        int levelBeforeSave = game.getCurrentLevel();
        String testFilePath = "target/test_save_game.txt";
        game.saveGame(testFilePath);
        Game newGameInstance = new Game();
        boolean loadResult = newGameInstance.loadGame(testFilePath);
        assertTrue(loadResult, "The game should be loaded successfully.");
        assertEquals(levelBeforeSave, newGameInstance.getCurrentLevel(), "Loaded level should match saved level.");
        assertEquals(stepsBeforeSave, newGameInstance.getLevelSteps(), "Loaded steps should match saved steps.");
    }

    @Test
    void testLoadInvalidFileReturnsFalse() {
        boolean result = game.loadGame("target/non_existent_save_file.txt");
        assertFalse(result, "Loading a non-existing file should return false cleanly.");
    }

    @Test
    void testGameInitialSteps() {
        assertEquals(0, game.getGameSteps(), "Game steps should start at 0.");
    }

    @Test
    void testLevelCompletedReturnsFalseInitially() {
        assertFalse(game.levelCompleted(), "Level 1 should not be completed initially.");
    }

    @Test
    void testGetLevelName() {
        assertEquals("Level 1", game.getLevelName(), "The level name should be parsed correctly.");
    }

    @Test
    void testTotalGoalsCount() {
        assertTrue(game.getTotalGoals() >= 0, "Total goals should be a valid non-negative number.");
    }

    @Test
    public void testLoadNextLevelSuccessfully() {
        assertEquals(1, game.getCurrentLevel());
        boolean result = game.loadNextLevel();
        assertTrue(result, "Should successfully load the next level.");
        assertEquals(2, game.getCurrentLevel(), "Current level should increment to 2.");
    }

    @Test
    public void testLoadNextLevelExhaustedReturnsFalse() {
        assertEquals(1, game.getCurrentLevel());
        while (game.loadNextLevel()) {
        }
        assertFalse(game.loadNextLevel(), "Attempting to load past the final level must return false.");
    }


    @Test
    public void testMoveBoxOutOfGoalIncrementsTotalGoals() {
        game.loadNextLevel();
        int initialGoals = game.getTotalGoals();
        game.move(0, -1); 
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(0, -1); 
        game.move(0, -1); 
        game.move(0, -1); 
        game.move(-1, 0);
        game.move(0, -1); 
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(0, 1);
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(0, 1);
        game.move(0, 1);
        game.move(0, 1);
        game.move(1, 0);
        game.move(1, 0);
        game.move(1, 0);
        game.move(1, 0);
        game.move(1, 0);
        game.move(1, 0);
        game.move(1, 0);
        game.move(1, 0);
        game.move(1, 0);
        game.move(1, 0);
        game.move(1, 0);
        game.move(1, 0);
        assertEquals(initialGoals-1, game.getTotalGoals(), "Total goals should decrement when a box is in a goal.");
        game.move(0, 1);
        game.move(1, 0);
        game.move(1, 0);
        game.move(0, -1);
        game.move(-1, 0);
        assertEquals(initialGoals, game.getTotalGoals(), "Initial goals should remain the same when a box is moved out of a goal.");
    }

}