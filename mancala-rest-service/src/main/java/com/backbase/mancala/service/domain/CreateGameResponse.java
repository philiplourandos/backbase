package com.backbase.mancala.service.domain;

import java.io.Serializable;

public class CreateGameResponse implements Serializable {
    private Integer id;
    
    private String uri;

    public CreateGameResponse() {
    }
    
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
