package com.backbase.mancala.game.board;

import com.backbase.mancala.game.GameAction;
import com.backbase.mancala.game.Players;

public class Pit implements GameAction {
    private int stones;
    
    private final Players owner;

    public Pit(final int stones, final Players owner) {
        this.stones = stones;
        this.owner = owner;
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
    
    @Override
    public boolean isOwner(final Players player) {
        return owner.equals(player);
    }
}
