package com.backbase.mancala.service.domain;

import java.io.Serializable;

public class MoveResponse implements Serializable {
    private Integer id;
    
    private String url;
    private String status;

    public MoveResponse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
