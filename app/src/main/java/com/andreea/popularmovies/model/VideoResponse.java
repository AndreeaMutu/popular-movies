package com.andreea.popularmovies.model;


import java.util.ArrayList;
import java.util.List;

public class VideoResponse {
    private long id;
    private List<Video> results;

    public VideoResponse() {
        results = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
