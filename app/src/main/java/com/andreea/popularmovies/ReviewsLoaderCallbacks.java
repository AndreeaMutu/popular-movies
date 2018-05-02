package com.andreea.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.andreea.popularmovies.model.Review;
import com.andreea.popularmovies.utils.JsonUtils;
import com.andreea.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.andreea.popularmovies.utils.MovieConstants.API_KEY;
import static com.andreea.popularmovies.utils.MovieConstants.MOVIE_ID_KEY;


public class ReviewsLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Review>> {
    private static final String TAG = ReviewsLoaderCallbacks.class.getSimpleName();
    private final Context context;
    private final ReviewsAdapter reviewsAdapter;

    public ReviewsLoaderCallbacks(Context context, ReviewsAdapter reviewsAdapter) {
        this.context = context;
        this.reviewsAdapter = reviewsAdapter;
    }

    @Override
    public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
        return new ReviewsAsyncTaskLoader(context, args);
    }

    @Override
    public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
        Log.d(TAG, "onLoadFinished: " + data);
        reviewsAdapter.refreshReviews(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Review>> loader) {

    }

    private static class ReviewsAsyncTaskLoader extends AsyncTaskLoader<List<Review>> {
        private static final String TAG = ReviewsAsyncTaskLoader.class.getSimpleName();
        private final Bundle args;
        private List<Review> reviews;

        public ReviewsAsyncTaskLoader(Context context, Bundle args) {
            super(context);
            this.args = args;
            if (reviews != null) {
                deliverResult(reviews);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<Review> loadInBackground() {
//            long movieId = args.getLong(MOVIE_ID_KEY);
//            URL reviewsUrl = NetworkUtils.buildMovieReviewsUrl(movieId, API_KEY);
//
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder()
//                    .url(reviewsUrl)
//                    .get()
//                    .build();
//            try {
//                Response response = client.newCall(request).execute();
//                try {
//                    ResponseBody body = response.body();
//                    if (response.isSuccessful() && body != null) {
//                        String json = body.string();
//                        Log.d(TAG, "loadInBackground: " + json);
//                        return JsonUtils.parseReviewsResponse(json);
//                    } else {
//                        Log.e(TAG, String.format("loadInBackground: Reviews request to %s was not successful.", reviewsUrl));
//                    }
//                } finally {
//                    response.close();
//                }
//            } catch (IOException e) {
//                Log.e(TAG, "Failed to parse reviews json response: ", e);
//            }
//            return Collections.emptyList();
String json = "{\n" +
        "  \"id\": 269149,\n" +
        "  \"page\": 1,\n" +
        "  \"results\": [\n" +
        "    {\n" +
        "      \"id\": \"56e4290b92514172c7001002\",\n" +
        "      \"author\": \"Andres Gomez\",\n" +
        "      \"content\": \"One of the best movies Disney has created in the last years. Smart plot with a great background topic talking about the differences, stereotypes, prejudices and joining the tendency of giving women more important roles.\\r\\n\\r\\nIt has still several gaps to fill and enhance on the latest point but it is, IMHO, a milestone in the right direction.\\r\\n\\r\\nThe characters work pretty well and it is funny when needed and not too full of cheesy songs.\",\n" +
        "      \"url\": \"https://www.themoviedb.org/review/56e4290b92514172c7001002\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": \"5752fa5d925141187e0021a6\",\n" +
        "      \"author\": \"Reno\",\n" +
        "      \"content\": \"> Try everything (but differently). So Disney has done it again.\\r\\n\\r\\nThis beautiful animation came to exist because of coming together of the directors of 'Tangled' and 'Wreck-it-Ralp'. It is Disney who had once again done it, since their rival Pixer is going down in a rapid speed. As a Disney fan since my childhood, I'm very happy for their success in live-shot films and animations, especially for this one.\\r\\n\\r\\nOkay, since the revolution of 3D animation over 20 years ago after overthrowing the 2D animation, most of the big productions like Disney, Pixer and Dreamworks with few others never failed to deliver. Believe me, I was not interested in this film when I first saw the teaser and trailer. But they have done great promotions and so the film did awesomely at screens worldwide. I was totally blown away after seeing it, Disney's another unique universal charactered story. From the little children to the grown ups, everybody definitely going to enjoy it.\\r\\n\\r\\nAll kinds of animals coming together happens only in cinemas, and that too mostly in animations. But todays kids are very sharp who ask lots of questions, so they had a fine explanation for the doubts regarding putting animals in a same society. It was like the United States, where everyone came from different continents and represents different race. And so in this film every animal came from different land to live together peacefully in a city called Zootopia.\\r\\n\\r\\nSo the story begins when Judy the rabbit follows her dream to become a police officer in Zootopia. There she meets Nick the fox, who are actually arch-rival species in the wild, but it was thousands of years ago before adapting the civilisation. So trust is what not promised between them, but they're forced to work together after a small missing person case becomes their prime agenda. Solving the mystery is what brings the end to this wonderful tale.\\r\\n\\r\\nThese days animations are not just concentrated on comedies, trying to get us emotionally as well. Maybe that's how they're grabbing the adult audience, especially the families. Shakira's cameo was the highlight, and her song 'Try Everything' helped the get attention from all the corners.\\r\\n\\r\\nThe Oscars was concluded just a couple of months ago, but it already feels like the fever is gripping again for the next edition and looks like this film is leading the way for the animation category. I know it's too early, but I hope it wins it. And finally a request for the Disney, bring it on a sequel as soon as possible.\\r\\n\\r\\n8/10\",\n" +
        "      \"url\": \"https://www.themoviedb.org/review/5752fa5d925141187e0021a6\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": \"57a718b3c3a368255b0017d8\",\n" +
        "      \"author\": \"whatowatch\",\n" +
        "      \"content\": \"Zootopia is the latest family animation movie from Disney about a young bunny determined to be a cop at all costs  – in a land where only 'predators' are cops, she would be the first ‘prey’ cop. Seriously? Anyway, she puts on the hard work and makes it to the land where anything is possible.\\r\\n\\r\\nZootopia makes some good real life comparisons: Zootopia itself seems to look like New York, even including a hustle by a sly fox.  Racism seems to be ever present in both color and speech. But the best thing about Zootopia is Judy’s positive attitude and relentless determination to succeed.\\r\\n\\r\\nThe animation is as good as a Pixar animation – perfectly made  – and the story is well told, full of action scenes and funny moments. Kids will definitely love this one and grown up kids will enjoy the deep thought provoking tones set in this movie. \\r\\n\\r\\nThe best performance in this movie arguably comes from Police Chief Bogo whose voice is played by Idris Elba. His performance should earn him an Oscar this year! No wonder it has grossed 1 Billion dollars worldwide despite little marketing. \\r\\n\\r\\nRated 9 out of 10 only because we cannot give it a 10.\",\n" +
        "      \"url\": \"https://www.themoviedb.org/review/57a718b3c3a368255b0017d8\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": \"57acf08792514158770001ed\",\n" +
        "      \"author\": \"Austin Singleton\",\n" +
        "      \"content\": \"A great movie with a great message. Read my full review here.\\r\\n\\r\\nhttp://www.hweird1reviews.com/allreviews/zootopia-review\",\n" +
        "      \"url\": \"https://www.themoviedb.org/review/57acf08792514158770001ed\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": \"57cc1a8fc3a3680a58003dab\",\n" +
        "      \"author\": \"Gimly\",\n" +
        "      \"content\": \"_Zootopia_ leans pretty hard on a few tropes that I absolutely detest, but even I can't deny that this animated family movie succeeds spectacularly at what it set out to do: Entertain all audiences, young & old, and deliver an important moral centre without detracting from its engaging plot. Something rarely achieved by the medium outside of Pixar. And even Pixar has had a few missteps the past years.\\r\\n\\r\\n_Final rating:★★★½ - I strongly recommend you make the time._\",\n" +
        "      \"url\": \"https://www.themoviedb.org/review/57cc1a8fc3a3680a58003dab\"\n" +
        "    }\n" +
        "  ],\n" +
        "  \"total_pages\": 2,\n" +
        "  \"total_results\": 8\n" +
        "}";
            return JsonUtils.parseReviewsResponse(json);
        }

        @Override
        public void deliverResult(List<Review> data) {
            reviews = data;
            super.deliverResult(data);
        }
    }
}
