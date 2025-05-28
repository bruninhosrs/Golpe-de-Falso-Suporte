package br.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HunterApiResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData (Data data) {
        this.data = data;
    }
}
