package com.backbase.mancala.service.domain;

import java.io.Serializable;

public class GamesResponse implements Serializable {
    private Integer id;
    
    private String uri;

    public GamesResponse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
