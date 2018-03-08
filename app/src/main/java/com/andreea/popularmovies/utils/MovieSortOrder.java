package com.andreea.popularmovies.utils;

public enum MovieSortOrder {
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    FAVORITE("favorite");
    private final String value;

    MovieSortOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}