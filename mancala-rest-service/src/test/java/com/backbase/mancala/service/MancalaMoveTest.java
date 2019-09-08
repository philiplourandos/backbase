package com.backbase.mancala.service;

import com.backbase.mancala.service.domain.CreateGameResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameEndpoint.class)
@ActiveProfiles("test")
public class MancalaMoveTest {
    private static final String API = "/games/%d/pits/%d";
    @Autowired
    private MockMvc mvc;
    
    private Integer gameId;
    
    private final ObjectMapper jsonMapper = new ObjectMapper();
    
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
        fail();
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
