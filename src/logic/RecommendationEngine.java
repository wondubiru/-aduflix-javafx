package logic;

import media.MediaItems;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RecommendationEngine {

    public static ArrayList<MediaItems> recommend(
            ArrayList<MediaItems> allMedia,
            ArrayList<MediaItems> watchHistory
    ) {
        ArrayList<MediaItems> recommendations = new ArrayList<>();
        Set<String> watchedTitles = new HashSet<>();
        Set<String> preferredGenres = new HashSet<>();

        // 1. Gather watched titles and genres
        for (MediaItems watched : watchHistory) {
            watchedTitles.add(watched.getTitle());
            preferredGenres.add(watched.getGenre());
        }

        // 2. Recommend items NOT in history, but matching genre + filters
        for (MediaItems media : allMedia) {
            if (watchedTitles.contains(media.getTitle())) continue;

            boolean matchGenre = preferredGenres.contains(media.getGenre());
            boolean matchRating = media.getRating() >= 3.5;
            boolean matchDuration = media.getDuration() <= 180;

            if (matchGenre && matchRating && matchDuration) {
                recommendations.add(media);
            }
        }

        return recommendations;
    }
}
