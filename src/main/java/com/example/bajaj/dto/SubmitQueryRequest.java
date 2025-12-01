package com.example.bajaj.dto;

public class SubmitQueryRequest {
    private String finalQuery;
    public SubmitQueryRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }
    public String getFinalQuery() { return finalQuery; }
    public void setFinalQuery(String finalQuery) { this.finalQuery = finalQuery; }
}