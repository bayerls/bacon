package de.uop.code.cubemerging.service;

import java.util.LinkedList;
import java.util.List;

public class BalloonSameAsResponse {

    private String response;
    private String status;
    private String query;
    private List<String> sameAs = new LinkedList<String>();
    private String endpoints;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getSameAs() {
        return sameAs;
    }

    public void setSameAs(List<String> sameAs) {
        this.sameAs = sameAs;
    }

    public String getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(String endpoints) {
        this.endpoints = endpoints;
    }
}
