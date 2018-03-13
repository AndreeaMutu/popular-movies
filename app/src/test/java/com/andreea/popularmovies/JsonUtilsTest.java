package com.andreea.popularmovies;

import com.andreea.popularmovies.model.Movie;
import com.andreea.popularmovies.utils.JsonUtils;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;


public class JsonUtilsTest {

    @Test
    public void testJsonMovieResponseIsConvertedToMovieList() throws Exception {
        InputStream resourceAsStream = getClass().getResourceAsStream("/sample_movie_response.json");
        String json = new BufferedReader(new InputStreamReader(resourceAsStream))
                .lines().collect(Collectors.joining("\n"));
        System.out.println(json);
        List<Movie> movies = JsonUtils.parseMoviesResponse(json);
        Assert.assertNotNull(movies);
        Assert.assertEquals(2, movies.size());

        Movie firstMovie = movies.get(0);
        long expectedId = 284053;
        String expectedTitle = "Thor: Ragnarok";
        String expectedPosterPath = "/oSLd5GYGsiGgzDPKTwQh7wamO8t.jpg";
        String expectedOverview = "Thor is imprisoned on the other side of the universe and finds himself in a race against time to get back to Asgard to stop Ragnarok, the prophecy of destruction to his homeworld and the end of Asgardian civilization, at the hands of an all-powerful new threat, the ruthless Hela.";
        double expectedVoteAverage = 7.4;
        String expectedReleaseDate = "2017-10-25";
        assertMovie(expectedId, expectedTitle, expectedPosterPath, expectedOverview, expectedVoteAverage, expectedReleaseDate, firstMovie);

        Movie secondMovie = movies.get(1);
        expectedId = 269149;
        expectedTitle = "Zootopia";
        expectedPosterPath = "/sM33SANp9z6rXW8Itn7NnG1GOEs.jpg";
        expectedOverview = "Determined to prove herself, Officer Judy Hopps, the first bunny on Zootopia's police force, jumps at the chance to crack her first case - even if it means partnering with scam-artist fox Nick Wilde to solve the mystery.";
        expectedVoteAverage = 7.7;
        expectedReleaseDate = "2016-02-11";
        assertMovie(expectedId, expectedTitle, expectedPosterPath, expectedOverview, expectedVoteAverage, expectedReleaseDate, secondMovie);
    }

    private static void assertMovie(long id, String title, String posterPath, String overview, double voteAverage, String releaseDate, Movie actualMovie) {
        Assert.assertNotNull(actualMovie);
        Assert.assertEquals(id, actualMovie.getId());
        Assert.assertEquals(title, actualMovie.getTitle());
        Assert.assertEquals(posterPath, actualMovie.getPosterPath());
        Assert.assertEquals(overview, actualMovie.getOverview());
        Assert.assertEquals(voteAverage, actualMovie.getVoteAverage(), 0);
        Assert.assertEquals(releaseDate, actualMovie.getReleaseDate());
        Assert.assertFalse(actualMovie.isFavorite());
    }
}