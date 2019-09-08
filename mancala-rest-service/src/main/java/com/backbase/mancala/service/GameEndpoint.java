package com.backbase.mancala.service;

import com.backbase.mancala.game.Game;
import com.backbase.mancala.service.domain.CreateGameResponse;
import com.backbase.mancala.service.domain.MoveResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameEndpoint {
    private static final Logger LOG = LogManager.getLogger(GameEndpoint.class);
    
    private final Map<Integer, Game> games = new HashMap<>();

    private final AtomicInteger idGenerator = new AtomicInteger(1);
    
    @Value("${com.backbase.mancala.initial.stones}")
    private int defaultStones;
    
    @RequestMapping(path = "/games", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity createGame() {
        final Integer gameId = idGenerator.getAndIncrement();

        final Game newGame = new Game(gameId, defaultStones);
        games.put(gameId, newGame);

        final CreateGameResponse response = 
                new CreateGameResponse(gameId, URI.create(String.format("http://localhost:8080/games/%d", gameId)).toString());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @RequestMapping(path = "/games/{gameId}/pits/{pitId}", method = RequestMethod.PUT)
    public ResponseEntity move(@PathVariable("gameId") final Integer gameId, 
            @PathVariable("pitId") final Integer pitId) {
        if (games.containsKey(gameId)) {
            final Game selectedGame = games.get(gameId);
            
            final Boolean turnPlayed = selectedGame.playTurn(pitId);
            
            if (turnPlayed) {
                final MoveResponse response = new MoveResponse();
                response.setId(gameId);
                response.setUri(URI.create(String.format("/games/%d", gameId)).toString());
                
                final String status = selectedGame.getBoard()
                        .entrySet()
                        .stream()
                        .map(a -> String.format("\"%d\": \"%d\"", a.getKey(), a.getValue().countStones()))
                        .collect(Collectors.joining(","));
                
                response.setStatus(status);

                return ResponseEntity.ok().body(response);
            } else {
                return ResponseEntity.badRequest().body("Unable to play turn. Pit invalid");
            }
        } else {
            LOG.error("No Game for id: [{}]", gameId);
            
            return ResponseEntity.notFound().build();
        }
    }
}
