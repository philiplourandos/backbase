package com.backbase.mancala.service;

import com.backbase.mancala.service.domain.CreateGameResponse;
import com.backbase.mancala.service.domain.MoveResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameEndpoint.class)
@ActiveProfiles("test")
public class MancalaMoveTest {
    private static final String API = "/games/%d/pits/%d";

    private static final Integer[] EXPECTED_VALUES = {0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0};
    private static final Map<Integer, Integer> EXPECTED_STATUS = new TreeMap<>();
    
    @Autowired
    private MockMvc mvc;
    
    private Integer gameId;
    
    private final ObjectMapper jsonMapper = new ObjectMapper();
    
    @BeforeAll
    public static void setup() {
        final AtomicInteger index = new AtomicInteger(1);

        Arrays.stream(EXPECTED_VALUES).forEach(a -> EXPECTED_STATUS.put(index.getAndIncrement(), a));
    }
    
    @BeforeEach
    public void createGame() throws Exception {
        if (Objects.isNull(gameId)) {
            final MvcResult result = mvc.perform(post("/games")).andReturn();
            final CreateGameResponse response = 
                    jsonMapper.readValue(result.getResponse().getContentAsString(), CreateGameResponse.class);

            this.gameId = response.getId();
        }
    }
    
    @Test
    public void when_validMoveMade_thenRespondSuccessfully() throws Exception {
        final MvcResult result = mvc.perform(put(String.format(API, gameId, 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.url").isNotEmpty())
                .andExpect(jsonPath("$.status").isNotEmpty())
                .andReturn();
        
        final MoveResponse responseValue = jsonMapper.readValue(
                result.getResponse().getContentAsString(), MoveResponse.class);
        assertNotNull(responseValue);
        
        assertEquals(EXPECTED_STATUS, responseValue.getStatus());
    }
    
    @Test
    public void when_moveMadeOnOpposingPlayer_thenFail() throws Exception {
        mvc.perform(put(String.format(API, gameId, 8))).andExpect(status().isBadRequest());
    }
    
    @Test
    public void when_houseSelected_thenFail() throws Exception {
        mvc.perform(put(String.format(API, gameId, 7))).andExpect(status().isBadRequest());
    }
    
    @Test
    public void when_nonExistantPitSelected_thenFail() throws Exception {
        mvc.perform(put(String.format(API, gameId, 1000))).andExpect(status().isBadRequest());
    }
    
    @Test
    public void when_nonExistantGameSelected_thenFail() throws Exception {
        mvc.perform(put(String.format(API, 6000, 1))).andExpect(status().isNotFound());
    }
}
