package es.upm.pproject.sokoban.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LevelManagerTest {
    private Game game;

    @BeforeEach   // Inicializamos el juego y cargamos el nivel 1 limpio antes de cada test
    public void setUp() {
        game = new Game();
        game.loadCurrentLevel();
    }

   @Test
    public void testInitialState() {
        assertNotNull(game.getBoard(), "The board should be loaded.");
        assertEquals(1, game.getCurrentLevel(), "Should start at level 1.");
        assertEquals(0, game.getLevelSteps(), "Steps should initially be 0.");
    }

    @Test
    public void testMoveValidRight() {
        // En el nivel 1, a la derecha (1, 0) el WarehouseMan puede moverse.
        // Como game.move es void, llamamos al método y luego comprobamos si sumó el paso.
        game.move(1, 0);
        assertEquals(1, game.getLevelSteps(), "Steps should increase by 1 upon valid movement.");
    }

    @Test
    public void testMoveHitWallLeft() {
        // En el nivel 1, a la izquierda (-1, 0) hay un muro. 
        // El movimiento debe bloquearse y los pasos deben seguir en 0.
        game.move(-1, 0);
        assertEquals(0, game.getLevelSteps(), "Steps should not increase when hitting a wall.");
    }


}