package com.andreea.popularmovies;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.andreea.popularmovies.utils.NetworkUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static com.andreea.popularmovies.utils.NetworkUtils.MovieSortOrder.POPULAR;
import static com.andreea.popularmovies.utils.NetworkUtils.MovieSortOrder.TOP_RATED;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class NetworkUtilsInstrumentedTest {
    private static final String TEST_API_KEY = "test_api_key";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.andreea.popularmovies", appContext.getPackageName());
    }

    @Test
    public void testPopularMoviesUrl() throws Exception {
        URL popularUrl = NetworkUtils.buildMovieListUrl(POPULAR.getValue(), TEST_API_KEY);
        Assert.assertNotNull(popularUrl);
        Assert.assertEquals("https://api.themoviedb.org/3/movie/popular?page=1&language=en_US&api_key=" + TEST_API_KEY, popularUrl.toString());
    }

    @Test
    public void testTopRatedMoviesUrl() throws Exception {
        URL topRatedUrl = NetworkUtils.buildMovieListUrl(TOP_RATED.getValue(), TEST_API_KEY);
        Assert.assertNotNull(topRatedUrl);
        Assert.assertEquals("https://api.themoviedb.org/3/movie/top_rated?page=1&language=en_US&api_key=" + TEST_API_KEY, topRatedUrl.toString());
    }
}
