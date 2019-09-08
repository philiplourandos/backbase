package com.backbase.mancala.service.domain;

public class CreateGameResponse {
    private final Integer id;
    
    private final String uri;

    public CreateGameResponse(Integer id, String uri) {
        this.id = id;
        this.uri = uri;
    }

    public Integer getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }
}
