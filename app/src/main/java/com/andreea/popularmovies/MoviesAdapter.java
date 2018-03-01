package com.andreea.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andreea.popularmovies.model.Movie;
import com.andreea.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.PosterViewHolder> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();
    private final Context context;
    private final List<Movie> movieList;
    private final MoviesAdapter.MovieOnClickHandler onClickHandler;

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView posterImageView;

        public PosterViewHolder(View view) {
            super(view);
            posterImageView = (ImageView) view.findViewById(R.id.movie_poster_iv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = movieList.get(adapterPosition);
            onClickHandler.onClick(movie);
        }
    }

    public MoviesAdapter(Context context, List<Movie> movieList, MoviesAdapter.MovieOnClickHandler onClickHandler) {
        this.context = context;
        this.movieList = movieList;
        this.onClickHandler = onClickHandler;
    }

    @Override
    public MoviesAdapter.PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);

        return new MoviesAdapter.PosterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.PosterViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        String moviePosterUrl = NetworkUtils.buildPosterUrl(movie.getPosterPath());
        Picasso.with(context)
                .load(moviePosterUrl)
                .placeholder(android.R.drawable.progress_horizontal)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.posterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Picasso successfully loaded poster for movie: " + movie);
                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "Picasso failed to load poster for movie: " + movie);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public interface MovieOnClickHandler {
        void onClick(Movie movie);
    }
}
