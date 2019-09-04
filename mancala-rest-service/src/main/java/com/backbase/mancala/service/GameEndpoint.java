package com.backbase.mancala.service;

import com.backbase.mancala.game.Game;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameEndpoint {

    private final Map<Integer, Game> games = new HashMap<>();

    private final AtomicInteger idGenerator = new AtomicInteger();
    
    @RequestMapping(path = "/games", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity createGame() {
        final Integer gameId = idGenerator.getAndIncrement();

        final Game newGame = new Game(gameId);
        games.put(gameId, newGame);

        return ResponseEntity.created(URI.create(String.format("/games/%d", gameId))).build();
    }
}
