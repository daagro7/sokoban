package es.upm.pproject.sokoban.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        // Inicializamos el juego y cargamos el nivel 1 antes de cada prueba
        game = new Game();
        game.loadCurrentLevel();
    }

    @Test
    void testRestartLevelResetsSteps() {
        //Da un paso hacia abajo (0, 1) en el Nivel 1
        game.move(0, 1);
        assertEquals(1, game.getLevelSteps(), "Steps on level should be 1.");

        //Reinicia el nivel
        game.restartLevel();

        //Verifica que los pasos del nivel vuelven a 0
        assertEquals(0, game.getLevelSteps(), "Steps should reset to 0 after restart.");
        assertEquals(1, game.getCurrentLevel(), "Should remain on level 1.");
    }

    @Test
    void testUndoMovement() {
        //Comprueba el estado inicial de pasos
        assertEquals(0, game.getLevelSteps());

        //Se mueve hacia abajo (0, 1)
        game.move(0, 1);
        assertEquals(1, game.getLevelSteps(), "Steps should be 1 after moving.");

        //Aplica el Undo 
        game.undoMove();

        //Verifica que los pasos han vuelto a ser 0
        assertEquals(0, game.getLevelSteps(), "Steps should return to 0 after undo.");
    }
}