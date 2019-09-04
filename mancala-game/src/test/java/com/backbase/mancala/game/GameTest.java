package com.backbase.mancala.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class GameTest {
    @Test
    public void when_invalidPositionGiven_thenDoNotPlayTurn() {
        final Game mancala = new Game(1);
        boolean turnPlayed = mancala.playTurn(1000);

        assertFalse(turnPlayed);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void when_houseUsedAsStartingPoint_thenDoNotPlayTurn(final int houseIndex) {
        final Game mancala = new Game(2);
        boolean turnPlayed = mancala.playTurn(houseIndex);

        assertFalse(turnPlayed);
    }
    
    @Test
    public void when_emptyPitSelectedEmpty_thenDoNotPlayTurn() {
        fail();
    }
    
    @Test
    public void when_gamePlayedToConclusion_thenEndWithExpectedOutCome() {
        fail();
    }
}
