package com.backbase.mancala.game.board;

import com.backbase.mancala.game.Players;
import com.backbase.mancala.game.GameAction;

public class House implements GameAction {
    private final Players owner;

    private int stones;

    public House(final Players owner) {
        this.owner = owner;
    }

    @Override
    public void addStone() {
        stones++;
    }

    @Override
    public int countStones() {
        return stones;
    }

    @Override
    public boolean isOwner(final Players player) {
        return owner.equals(player);
    }
    
    public void addStones(final int stones) {
        this.stones += stones;
    }
}
