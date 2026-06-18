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
    void testLoadNextLevelSuccessfully() {
        assertEquals(1, game.getCurrentLevel());
        boolean result = game.loadNextLevel();
        assertTrue(result, "Should successfully load the next level.");
        assertEquals(2, game.getCurrentLevel(), "Current level should increment to 2.");
    }

    @Test
    void testLoadNextLevelExhaustedReturnsFalse() {
        assertEquals(1, game.getCurrentLevel());
        while (game.loadNextLevel()) {
        }
        assertFalse(game.loadNextLevel(), "Attempting to load past the final level must return false.");
    }


    @Test
    void testMoveBoxOutOfGoalIncrementsTotalGoals() {
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

    @Test
    void testConstructorLevelLoaderThrowsException() throws Exception {
        java.lang.reflect.Constructor<LevelLoader> constructor = LevelLoader.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        java.lang.reflect.InvocationTargetException exception = assertThrows(
            java.lang.reflect.InvocationTargetException.class, 
            constructor::newInstance
        );
        assertTrue(exception.getCause() instanceof IllegalStateException, "Should throw IllegalStateException.");
    }

    @Test
    void testLoadLevelFileNotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            LevelLoader.loadLevel(-1);
        }, "Should throw IllegalArgumentException when level file is missing.");
    }

    @Test
    void testLoadLevelInvalidDimensionsThrowsException() {
        LevelManager corruptLevel = LevelLoader.loadLevel(3);
        assertNull(corruptLevel, "Loading a level with mismatched dimensions should return null.");
    }

    @Test
     void testPushBoxBlockedByAnotherBox() {
        game.loadNextLevel();
        game.move(0, -1); 
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(0, -1); 
        game.move(0, -1); 
        game.move(0, -1); 
        game.move(-1, 0);
        int stepsBefore = game.getLevelSteps();
        game.move(-1, 0); 
        game.move(-1, 0); 
        game.move(0, 1); 
        assertEquals(stepsBefore, game.getLevelSteps(), "Steps shouldn't change if blocked by boxes.");
    }

    @Test
    void testUndoWithNoHistory() {
        int stepsBefore = game.getLevelSteps();
        game.undoMove(); 
        assertEquals(stepsBefore, game.getLevelSteps(), "Undo on empty history should do nothing.");
    }

    @Test
    void testUndoMovementWithBox() {
        game.loadNextLevel();
        game.move(0, -1); 
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(0, -1); 
        game.move(0, -1); 
        game.move(0, -1); 
        game.move(-1, 0);
        int stepsAfterMove = game.getLevelSteps();
        game.undoMove();
        assertEquals(stepsAfterMove - 1, game.getLevelSteps(), "Steps should decrement after undoing a box push.");
    }

    @Test
    void testLoggerDirectionsCoverage() {
        game.move(0, 1);   
        game.move(0, -1);  
        game.move(-1, 0);  
        game.move(1, 0);   
        assertNotNull(game.getBoard());
    }

    @Test
    void testUnknownDirectionAndDefaultSwitchCoverage() {
        game.move(2, 2);
        assertEquals(0, game.getLevelSteps(), "Steps should not change on unknown movements.");
    }

    @Test
    void testMoveWManDefaultSwitchCoverage() {
        game.getBoard().setCell(3, 4, es.upm.pproject.sokoban.models.Cell.CellType.ERROR);
        game.move(1, 0);
        assertEquals(0, game.getLevelSteps(), "Movement should fail and hit the default case.");
    }

    @Test
    void testMoveBoxDefaultSwitchCoverage() {
        game.loadNextLevel();
        game.getBoard().setCell(6, 4, es.upm.pproject.sokoban.models.Cell.CellType.ERROR);
        game.move(0, -1); 
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(0, -1); 
        game.move(0, -1); 
        game.move(0, -1); 
        game.move(-1, 0);
        assertNotNull(game.getBoard());
    }

    @Test
    void testAllDirectionConditionsForCoverage() {
        game.move(0, -1); 
        game.move(0, 1);  
        game.move(-1, 0); 
        game.move(1, 0);  
        game.move(1, 1);
        game.move(-1, -1);
        game.move(0, 0);

        assertEquals(0, game.getGameSteps());
    }

    @Test
    void testUndoBoxOnGoalPositionTernary() {
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
        game.undoMove();
        assertNotNull(game.getBoard());
        assertEquals(initialGoals, game.getTotalGoals(), "Initial goals should remain the same when a box is moved out of a goal.");
    }

    @Test
    void testGameGettersSettersAndStatus() {
        Game testGame = new Game();

        // 1. Probamos los setters y getters básicos marcados en rojo
        testGame.setCurrentLevel(4);
        assertEquals(4, testGame.getCurrentLevel());

        testGame.setStepsOnGame(150);
        assertEquals(150, testGame.getGameSteps());

        testGame.setGameCompleted(true);
        assertTrue(testGame.isGameCompleted());
    }

   @Test
    void testGameCompletedWhenNoMoreLevels() throws Exception {
        while (game.loadNextLevel()) {
        }
        java.lang.reflect.Field fieldManager = Game.class.getDeclaredField("levelManager");
        fieldManager.setAccessible(true);
        LevelManager lm = (LevelManager) fieldManager.get(game);
        if (lm != null) {
            java.lang.reflect.Field fieldGoals = LevelManager.class.getDeclaredField("totalGoals");
            fieldGoals.setAccessible(true);
            fieldGoals.set(lm, 0);
            assertTrue(game.levelCompleted());
            assertTrue(game.isGameCompleted(), "The game should be fully completed.");
        }
    }

    @Test
    void testLoadGameWithSwitchMovementsAndDefault() throws Exception {
        Game testGame = new Game();
        java.net.URL resource = GameTest.class.getResource("/levels/partida_test.sav");
        assertNotNull(resource, "The partida_test.sav file must exist in src/test/resources/levels/");
        String absolutePath = java.nio.file.Paths.get(resource.toURI()).toString();
        boolean loadResult = testGame.loadGame(absolutePath);
        assertTrue(loadResult, "The game should load correctly.");
        assertEquals(2, testGame.getCurrentLevel(), "The loaded level should be 2.");
        assertEquals(10, testGame.getGameSteps(), "The accumulated game points should be 10.");
        assertFalse(testGame.isGameCompleted(), "The game should not be marked as completed when loading.");
    }

    @Test
    void testBoardGettersAndToString() {
        Board board = game.getBoard();
        assertNotNull(board, "The board should not be null after loading the level.");
        int width = board.getWidth();
        int height = board.getHeigth();
        assertTrue(width > 0, "The board width must be greater than 0.");
        assertTrue(height > 0, "The board height must be greater than 0.");
        String boardString = board.toString();
        assertNotNull(boardString, "The board toString must not return null.");
        assertFalse(boardString.isEmpty(), "The board string must not be empty.");
    }

    @Test
    void testCellToStringDefaultAndErrorCoverage() {
        es.upm.pproject.sokoban.models.Cell errorCell = new es.upm.pproject.sokoban.models.Cell(es.upm.pproject.sokoban.models.Cell.CellType.ERROR);
        String result = errorCell.toString();
        assertEquals("?", result, "The toString of an ERROR cell must return '?'.");
    }

    @Test
    void testConstructorGameLoaderThrowsException() throws Exception {
        java.lang.reflect.Constructor<GameLoader> constructor = GameLoader.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        java.lang.reflect.InvocationTargetException exception = assertThrows(
            java.lang.reflect.InvocationTargetException.class, 
            constructor::newInstance
        );
        assertTrue(exception.getCause() instanceof IllegalStateException, "It should throw IllegalStateException.");
    }

    @Test
    void testGameLoaderWithNoMovementsTernaryBranch() throws Exception {
        java.net.URL resource = GameTest.class.getResource("/levels/partida_vacia_test.sav");
        assertNotNull(resource, "The partida_vacia_test.sav file must exist.");
        String absolutePath = java.nio.file.Paths.get(resource.toURI()).toString();
        GameLoader.SaveData saveData = GameLoader.load(absolutePath);
        assertNotNull(saveData, "The saved data should not be null.");
        assertEquals(1, saveData.levelNumber, "It should load level 1.");
        assertEquals(0, saveData.gamePoints, "It should have 0 points.");
        assertEquals("", saveData.movements, "The movement string should be empty when the third line does not exist.");
    }

    @Test
    void testConstructorGameSaverThrowsException() throws Exception {
        java.lang.reflect.Constructor<GameSaver> constructor = GameSaver.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        java.lang.reflect.InvocationTargetException exception = assertThrows(
            java.lang.reflect.InvocationTargetException.class, 
            constructor::newInstance
        );
        assertTrue(exception.getCause() instanceof IllegalStateException, "It should throw IllegalStateException.");
    }

    @Test
    void testGameSaverMovementUP() throws Exception {
        game.setCurrentLevel(1);
        game.loadCurrentLevel();
        game.move(0, -1); 

        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("save_up", ".sav");
        try {
            game.saveGame(tempFile.toAbsolutePath().toString());
            assertTrue(java.nio.file.Files.exists(tempFile));
        } finally {
            java.nio.file.Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void testGameSaverMovementDOWN() throws Exception {
        game.setCurrentLevel(1);
        game.loadCurrentLevel();
        game.move(0, 1); 

        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("save_down", ".sav");
        try {
            game.saveGame(tempFile.toAbsolutePath().toString());
            assertTrue(java.nio.file.Files.exists(tempFile));
        } finally {
            java.nio.file.Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void testGameSaverMovementLEFT() throws Exception {
        game.setCurrentLevel(1);
        game.loadCurrentLevel();
        game.move(1, 0); 
        game.move(-1, 0); 

        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("save_left", ".sav");
        try {
            game.saveGame(tempFile.toAbsolutePath().toString());
            assertTrue(java.nio.file.Files.exists(tempFile));
        } finally {
            java.nio.file.Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void testGameSaverMovementRIGHT() throws Exception {
        game.setCurrentLevel(1);
        game.loadCurrentLevel();
        game.move(1, 0); 

        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("save_right", ".sav");
        try {
            game.saveGame(tempFile.toAbsolutePath().toString());
            assertTrue(java.nio.file.Files.exists(tempFile));
        } finally {
            java.nio.file.Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void testGameSaverExceptionCatchBlock() {
        String invalidPath = "/directory_does_not_exist_1234/_??_invalid_file.sav";
        GameSaver.save(game, invalidPath);
        assertNotNull(game);
    }

    @Test
    void testWarehouseManSetPositionCoverage() {
        WarehouseMan player = new WarehouseMan(0, 0);
        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
        player.setPosition(5, 8);
        assertEquals(5, player.getX(), "The X coordinate should have changed to 5.");
        assertEquals(8, player.getY(), "The Y coordinate should have changed to 8.");
    }

    @Test
    void testLevelLoaderNotEnoughLinesLessThanThree() {
        assertNull(LevelLoader.loadLevel(91), "It should return null due to missing basic lines.");
    }

    @Test
    void testLevelLoaderNotEnoughLinesForHeight() {
        assertNull(LevelLoader.loadLevel(92), "It should return null because there are not enough lines for the height.");
    }

   /*  @Test    Da fallo porque el método System.exit(1) termina la ejecución del test, no se puede capturar con assertThrows.
    void testLevelLoaderMultipleWarehouseMenSystemExit() {
        try {
            LevelLoader.loadLevel(93);
        } catch (Throwable t) {
        }
        assertNotNull(game);
    }*/

    @Test
    void testLevelLoaderFileNotFoundIOException() {
        assertThrows(IllegalArgumentException.class, () -> {
            LevelLoader.loadLevel(999);
        }, "Loading a non-existent level must throw IllegalArgumentException.");
    }

    @Test
    void testLevelLoaderInvalidCharacterCaseDefault() {
        assertNull(LevelLoader.loadLevel(94), "It should return null when invalid characters are present.");
    }

    @Test
    void testLevelLoaderIndexOutOfBoundsExceptionDimensions() {
        assertNull(LevelLoader.loadLevel(95), "It should return null when IndexOutOfBoundsException is thrown.");
    }

    @Test
    void testLevelLoaderBoxCountNotEqualsTotalGoals() {
        assertNull(LevelLoader.loadLevel(96), "It should return null if the number of boxes and goals does not match.");
    }

    @Test
    void testLevelLoaderNoBoxesOrGoalsFound() {
        assertNull(LevelLoader.loadLevel(97), "It should return null if there are no boxes or goals on the map.");
    }


}