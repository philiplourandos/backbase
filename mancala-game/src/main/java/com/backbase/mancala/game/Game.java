package com.backbase.mancala.game;

import com.backbase.mancala.game.board.House;
import com.backbase.mancala.game.board.Pit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game {
    private static final Logger LOG = LogManager.getLogger(Game.class);

    private static final int INDEX_PLAYER_1_HOUSE = 7;
    private static final int INDEX_PLAYER_2_HOUSE = 14;

    private final int id;

    private Players currentPlayer;

    private final Map<Integer, Object> board;

    public Game(final int id) {
        this.id = id;

        currentPlayer = Players.PLAYER_1;

        this.board = Map.ofEntries(Map.entry(1, new Pit()),
            Map.entry(2, new Pit()), Map.entry(3, new Pit()), Map.entry(4, new Pit()),
            Map.entry(5, new Pit()), Map.entry(6, new Pit()), Map.entry(INDEX_PLAYER_1_HOUSE, new House(Players.PLAYER_1)),
            Map.entry(8, new Pit()), Map.entry(9, new Pit()), Map.entry(10, new Pit()), 
            Map.entry(11, new Pit()), Map.entry(12, new Pit()), Map.entry(13, new Pit()), 
            Map.entry(INDEX_PLAYER_2_HOUSE, new House(Players.PLAYER_2)));
    }
    
    /**
     * Play a turn in the game by specifying the pit whose stones we will use
     * 
     * @param startingPitIndex Pit to use
     * 
     * @return True returned if stated position was valid, else false
     */
    public boolean playTurn(final int startingPitIndex) {
        boolean turnPlayed = false;
        
        if (startingPitIndex % 7 == 0) {
            LOG.error("Cannot take stones from house");
        } else {
            final Pit selected = (Pit) board.get(startingPitIndex);

            if (selected.isEmpty()) {
                LOG.error("Pit has no stones");
            } else {
                final int pickedUpStones = selected.removeAll();
                
                final List<GameAction> boardElements = getAffectedBoardElements(startingPitIndex, pickedUpStones);
                boardElements.stream().forEach(a -> a.addStone());
                
                final GameAction lastBoardElementUsed = boardElements.get(pickedUpStones - 1);
                
                if (House.class.isAssignableFrom(lastBoardElementUsed.getClass())) {
                    LOG.info("Player: [{}], has another turn", currentPlayer);
                }
                if (lastBoardElementUsed.countStones() == 1) {
                    currentPlayer = currentPlayer.equals(Players.PLAYER_1) ? Players.PLAYER_2 : Players.PLAYER_1;
                    
                    LOG.info("Last stone was placed in an empty pit. Next player is: [{}]", currentPlayer);
                }
                
                turnPlayed = true;
            }
        }

        return turnPlayed;
    }

    protected List<GameAction> getAffectedBoardElements(final int startingPitIndex, final int stonesToDeposit) {
        boolean elementsNotAssembled = true;

        int index = startingPitIndex;

        final List<GameAction> requiredBoardElements = new ArrayList<>(stonesToDeposit);

        final House player1House = (House) board.get(INDEX_PLAYER_1_HOUSE);
        final House player2House = (House) board.get(INDEX_PLAYER_2_HOUSE);

        while (elementsNotAssembled) {
            boolean addBoardElement = true;

            if (index == INDEX_PLAYER_2_HOUSE && !player2House.isOwner(currentPlayer)) {
                index = 1;

                addBoardElement = false;
            }

            if (index == INDEX_PLAYER_1_HOUSE && !player1House.isOwner(currentPlayer)) {
                addBoardElement = false;
            }
            
            if (addBoardElement) {
                requiredBoardElements.add((GameAction) this.board.get(index));
                
                index++;
            }

            if (requiredBoardElements.size() == stonesToDeposit) {
                elementsNotAssembled = false;
            }
        }

        return requiredBoardElements;
    }
    
    public int getId() {
        return id;
    }
}
