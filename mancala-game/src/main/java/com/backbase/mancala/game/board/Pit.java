package com.backbase.mancala.game.board;

import com.backbase.mancala.game.GameAction;

public class Pit implements GameAction {
    private int stones;

    public Pit(final int stones) {
        this.stones = stones;
    }
    
    @Override
    public void addStone() {
        stones++;
    }

    public int removeAll() {
        int response = stones;

        stones = 0;

        return response;
    }

    public boolean isEmpty() {
        return stones == 0;
    }
    
    @Override
    public int countStones() {
        return stones;
    }
}
