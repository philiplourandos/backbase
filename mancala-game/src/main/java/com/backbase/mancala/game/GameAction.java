package com.backbase.mancala.game;

public interface GameAction {
    void addStone();
    
    int countStones();
    
    boolean isOwner(final Players player);
}
