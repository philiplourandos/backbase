package com.backbase.mancala.game;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {    
    private static final Logger LOG = LogManager.getLogger(GameTest.class);

    private static final int DEFAULT_STONES = 6;
    private static final int INPUT_START_PIT_INDEX = 14;
    private static final int INPUT_CURRENT_PLAYER_INDEX = 15;
    
    private final Game successfulGame = new Game(1, DEFAULT_STONES);
    
    @Test
    public void when_invalidPositionGiven_thenDoNotPlayTurn() {
        final Game mancala = new Game(1, DEFAULT_STONES);
        boolean turnPlayed = mancala.playTurn(1000);

        assertFalse(turnPlayed);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void when_houseUsedAsStartingPoint_thenDoNotPlayTurn(final int houseIndex) {
        final Game mancala = new Game(2, DEFAULT_STONES);
        boolean turnPlayed = mancala.playTurn(houseIndex);

        assertFalse(turnPlayed);
    }
    
    @Test
    public void when_emptyPitSelectedEmpty_thenDoNotPlayTurn() {
        final Game mancala = new Game(2, 0);
        
        boolean turnPlayed = mancala.playTurn(1);
        assertFalse(turnPlayed);
    }
    
    @Test
    public void when_gamePlayedToConclusion_thenEndWithExpectedOutCome() throws Exception {
        final ClassPathResource testFile = new ClassPathResource("full-game.csv");

        final Path filePath = Paths.get(testFile.getURI());
        
        Files.readAllLines(filePath, Charset.forName("UTF-8")).stream()
                .skip(2)
                .forEach(this::processInput);
    }
    
    private void processInput(final String gameInput) {
        final String[] values = gameInput.split(",");

        assertEquals(Players.valueOf(values[INPUT_CURRENT_PLAYER_INDEX]), successfulGame.getCurrentPlayer());
        final boolean turnPlayed = successfulGame.playTurn(Integer.valueOf(values[INPUT_START_PIT_INDEX]));
        assertTrue(turnPlayed);
        
        LOG.info("{}", successfulGame);
        
        final Map<Integer, GameAction> board = successfulGame.getBoard();
        
        for(int index = 0; index < 14; index++) {
            assertEquals(Integer.valueOf(values[index]), Integer.valueOf(board.get(index + 1).countStones()));
        }
    }
    
    @Test
    public void when_allPitsEmpty_thenGameOver() {
        final Game emptyGame = new Game(33, 0);
        assertTrue(emptyGame.hasGameEnded());
    }
    
    @Test
    public void when_pitsHaveStones_thenGameNotOver() {
        assertFalse(successfulGame.isGameOver());
    }
}
