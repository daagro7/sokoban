package es.upm.pproject.sokoban.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

 class LevelManagerTest {
    private Game game;

    @BeforeEach   
    void setUp() {
        game = new Game();
        game.loadCurrentLevel();
    }

   @Test
    void testInitialState() {
        assertNotNull(game.getBoard(), "The board should be loaded.");
        assertEquals(1, game.getCurrentLevel(), "Should start at level 1.");
        assertEquals(0, game.getLevelSteps(), "Steps should initially be 0.");
    }

    @Test
    void testMoveValidRight() {
        game.move(1, 0);
        assertEquals(1, game.getLevelSteps(), "Steps should increase by 1 upon valid movement.");
    }

    @Test
    void testMoveHitWallLeft() {
        game.move(-1, 0);
        assertEquals(0, game.getLevelSteps(), "Steps should not increase when hitting a wall.");
    }

    @Test
    void testPushBoxToEmptyCell() {
        
        game.move(1, 0); 
        game.move(0, -1); 
        game.move(1, 0); 
        game.move(1, 0); 
        game.move(1, 0); 
        game.move(0, 1); 
        game.move(0, 1);
        game.move(-1, 0);
        game.move(0, -1); 
        assertEquals(9, game.getLevelSteps(), "Steps should increase when pushing a box.");
    }

    @Test
    void testPushBoxToWall() {
        
        game.move(1, 0); 
        game.move(0, -1); 
        game.move(1, 0); 
        game.move(1, 0); 
        game.move(1, 0); 
        game.move(0, 1); 
        game.move(-1, 0);
        assertEquals(6, game.getLevelSteps(), "Steps shouldn't increase when pushing a box.");
    }

    @Test
    void testMovementBlockedByWallAndBoxes() {
        int steps = game.getLevelSteps();
        game.move(-1, 0); 
        game.move(0, -1); 
        
        assertEquals(steps, game.getLevelSteps(), "Steps must not change when movements are completely blocked.");
    }

    @Test
    void testCompleteLevel() {
        
        game.move(1, 0); 
        game.move(0, -1); 
        game.move(1, 0); 
        game.move(1, 0); 
        game.move(1, 0); 
        game.move(0, 1); 
        game.move(0, 1);
        game.move(-1, 0);
        game.move(0, -1); 
        game.move(1, 0);
        game.move(0, -1);
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(0, 1);
        game.move(-1, 0);
        game.move(0, -1);
        game.move(-1, 0);
        game.move(0, -1);
        game.move(0, -1);
        game.move(1, 0);
        game.move(0, 1);
        game.move(0, 1);
        game.move(0, 1);
        game.move(1, 0);
        game.move(0, 1);
        game.move(0, 1);
        game.move(-1, 0);
        game.move(-1, 0);
        game.move(0, -1);
        game.move(1, 0);
        game.move(0, 1);
        game.move(1, 0);
        game.move(0, -1);
        assertTrue(game.isLevelComplete(), "Level should be complete.");
    }

}