package com.andreea.popularmovies.utils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class TextFormatUtils {
    private static final DateTimeFormatter FULL_DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    private TextFormatUtils() {
    }

    public static String formatVoteAverage(double score) {
        return String.format("%s/10", score);
    }

    public static String formatReleaseDate(String releaseDate) {
        return LocalDate.parse(releaseDate).format(FULL_DATE_FORMAT);
    }
}
