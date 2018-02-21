package com.andreea.popularmovies.model;

public class Movie {
    private String originalTitle;
    private String posterUrl;
    private String plotSynopsis;
    private String userRating;
    private String releaseDate;

    public Movie() {
    }

    public Movie(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "originalTitle='" + originalTitle + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", userRating='" + userRating + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
