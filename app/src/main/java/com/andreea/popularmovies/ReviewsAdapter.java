package com.andreea.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreea.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private List<Review> reviews = new ArrayList<>();

    public void refreshReviews(List<Review> reviews) {
        this.reviews = reviews;
        this.notifyDataSetChanged();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        final Review review = reviews.get(position);
        holder.reviewAuthor.setText(review.getAuthor());
        holder.reviewContent.setText(review.getContent());
        String linkText = String.format("<a href=\"%s\">Show more</a>", review.getUrl());
        holder.reviewLink.setText(Html.fromHtml(linkText));
        holder.reviewLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView reviewAuthor;
        private TextView reviewContent;
        private TextView reviewLink;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewContent = (TextView) itemView.findViewById(R.id.review_content_tv);
            reviewAuthor = (TextView) itemView.findViewById(R.id.review_author_tv);
            reviewLink = (TextView) itemView.findViewById(R.id.review_link_tv);
        }
    }
}
