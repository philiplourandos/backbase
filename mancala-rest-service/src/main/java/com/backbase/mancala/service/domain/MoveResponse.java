package com.backbase.mancala.service.domain;

import java.io.Serializable;
import java.util.Map;

public class MoveResponse implements Serializable {
    private Integer id;
    
    private String url;
    private Map<Integer, Integer> status;

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

    public Map<Integer, Integer> getStatus() {
        return status;
    }

    public void setStatus(Map<Integer, Integer> status) {
        this.status = status;
    }
}
