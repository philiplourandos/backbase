package com.backbase.mancala.game;

import com.backbase.mancala.game.board.House;
import com.backbase.mancala.game.board.Pit;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game {

    private static final Logger LOG = LogManager.getLogger(Game.class);

    private static final int TOTAL_BOARD_ELEMENTS_PER_PLAYER = 7;
    private static final int INDEX_PLAYER_1_HOUSE = 7;
    private static final int INDEX_PLAYER_2_HOUSE = 14;

    private static final List<Integer> PLAYER_1_INDEXES = List.of(1, 2, 3, 4, 5, 6);
    private static final List<Integer> PLAYER_2_INDEXES = List.of(8, 9, 10, 11, 12, 13);
    private static final Map<Players, Integer> PLAYER_HOUSE_INDEX = new EnumMap<Players, Integer>(Players.class);

    static {
        PLAYER_HOUSE_INDEX.put(Players.PLAYER_1, INDEX_PLAYER_1_HOUSE);
        PLAYER_HOUSE_INDEX.put(Players.PLAYER_2, INDEX_PLAYER_2_HOUSE);
    }

    private final int id;

    private Players currentPlayer;

    private final Map<Integer, GameAction> board = new TreeMap<>();

    private boolean gameOver;

    /**
     * Constructor
     *
     * @param id Id of this game
     * @param initialStonesPerPit Default starting stones for each pit
     */
    public Game(final int id, final int initialStonesPerPit) {
        this.id = id;

        currentPlayer = Players.PLAYER_1;

        PLAYER_1_INDEXES.stream().forEach(a -> board.put(a, new Pit(initialStonesPerPit, Players.PLAYER_1)));
        board.put(INDEX_PLAYER_1_HOUSE, new House(Players.PLAYER_1));

        PLAYER_2_INDEXES.stream().forEach(a -> board.put(a, new Pit(initialStonesPerPit, Players.PLAYER_2)));
        board.put(INDEX_PLAYER_2_HOUSE, new House(Players.PLAYER_2));
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

        if (gameOver) {
            LOG.warn("Game has ended");
        } else if (startingPitIndex % TOTAL_BOARD_ELEMENTS_PER_PLAYER == 0) {
            LOG.error("Cannot take stones from house");
        } else if (startingPitIndex > INDEX_PLAYER_2_HOUSE) {
            LOG.error("Invalid position on board selected: [{}]", startingPitIndex);
        } else if (!isPlayersPit(startingPitIndex)) {
            LOG.error("Pit selected does not belong to owner: [{}]", currentPlayer);
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
                } else {
                    if (lastBoardElementUsed.countStones() == 1 && lastBoardElementUsed.isOwner(currentPlayer)) {
                        // start index + 1(when we start sowing stones) + number of stones
                        int lastElementIndex = startingPitIndex + pickedUpStones;
                        int capturedStones = ((Pit) lastBoardElementUsed).removeAll();

                        if (lastElementIndex < INDEX_PLAYER_1_HOUSE) {
                            lastElementIndex += TOTAL_BOARD_ELEMENTS_PER_PLAYER;
                        } else {
                            lastElementIndex -= TOTAL_BOARD_ELEMENTS_PER_PLAYER;
                        }

                        final Pit oppositePit = (Pit) board.get(lastElementIndex);
                        capturedStones += oppositePit.removeAll();

                        final House house = (House) board.get(PLAYER_HOUSE_INDEX.get(currentPlayer));
                        house.addStones(capturedStones);
                    }

                    currentPlayer = currentPlayer.equals(Players.PLAYER_1) ? Players.PLAYER_2 : Players.PLAYER_1;

                    LOG.info("Next player is: [{}]", currentPlayer);
                }

                gameOver = hasGameEnded();
                
                turnPlayed = true;
            }
        }

        return turnPlayed;
    }

    
    private boolean isPlayersPit(final int pitIndex) {
        final GameAction pit = board.get(pitIndex);
        
        return pit.isOwner(currentPlayer);
    }
    
    /**
     * The game is over if the player has no stones in their pit
     *
     * @return True returned if player has no more stones in their pit
     */
    public boolean hasGameEnded() {
        final Function<Players, Boolean> checkEndGame= (player) -> {
        
            final Optional<Integer> stones = board.entrySet().stream()
                    .map(c -> c.getValue())
                    .filter(f -> Pit.class.isAssignableFrom(f.getClass()))
                    .map(c -> (Pit) c)
                    .filter(f -> f.isOwner(player))
                    .map(c -> c.countStones())
                    .reduce(Integer::sum);

            LOG.info("Player: [{}], has [{}] stones left", player, stones);

            return stones.map(m -> m == 0).orElse(false);
        };
        
        final Boolean player1Complete = checkEndGame.apply(Players.PLAYER_1);
        final Boolean player2Complete = checkEndGame.apply(Players.PLAYER_2);
        
        return player1Complete || player2Complete;
    }

    /**
     * We need to get a list of the board elements where we will deposit stones
     * in
     *
     * @param startingPitIndex Starting pit that stones were removed from
     * @param stonesToDeposit Number of stones in the pit we started on
     *
     * @return List of board elements returned
     */
    List<GameAction> getAffectedBoardElements(final int startingPitIndex, final int stonesToDeposit) {
        boolean elementsNotAssembled = true;

        int index = startingPitIndex + 1;

        final List<GameAction> requiredBoardElements = new ArrayList<>(stonesToDeposit);

        final House player1House = (House) board.get(INDEX_PLAYER_1_HOUSE);
        final House player2House = (House) board.get(INDEX_PLAYER_2_HOUSE);

        while (elementsNotAssembled) {
            boolean addBoardElement = true;

            if (index == INDEX_PLAYER_2_HOUSE && !player2House.isOwner(currentPlayer)) {
                addBoardElement = false;
            }

            if (index == INDEX_PLAYER_1_HOUSE && !player1House.isOwner(currentPlayer)) {
                addBoardElement = false;
            }

            if (addBoardElement) {
                requiredBoardElements.add((GameAction) this.board.get(index));
            }

            if (index == INDEX_PLAYER_2_HOUSE) {
                index = 1;
            } else {
                index++;
            }

            if (requiredBoardElements.size() == stonesToDeposit) {
                elementsNotAssembled = false;
            }
        }

        return requiredBoardElements;
    }

    public Players getCurrentPlayer() {
        return currentPlayer;
    }

    public int getId() {
        return id;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public String toString() {
        final StringBuilder output = new StringBuilder(60);
        output.append("\n|   Player1   |   Player2   |\n");

        final String boardState = board.entrySet().stream()
                .map(a -> String.valueOf(a.getValue().countStones()))
                .collect(Collectors.joining(","));
        output.append('|').append(boardState).append('|');

        return output.toString();
    }

    public Map<Integer, GameAction> getBoard() {
        return board;
    }
}
