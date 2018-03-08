package com.andreea.popularmovies.model;


import java.util.ArrayList;
import java.util.List;

public class ReviewResponse {
    private long id;
    private int page;
    private int totalResults;
    private int totalPages;
    private List<Review> results;

    public ReviewResponse() {
        results = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
