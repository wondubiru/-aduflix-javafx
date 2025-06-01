package logic;

import media.MediaItems;
import java.util.ArrayList;

public class RecommendationEngine {

    public static ArrayList<MediaItems> recommend(ArrayList<MediaItems> allMedia, ArrayList<MediaItems> watchHistory) {

        // This method generates recommendations based on the user's watch history and preferences.
        // It filters media items based on genre, rating, duration, and whether they have already been watched.

        ArrayList<MediaItems> recommendations = new ArrayList<>();
        ArrayList<String> likedGenres = new ArrayList<>();

        for (MediaItems item : watchHistory) {
            String genre = item.getGenre();
            if (!likedGenres.contains(genre)) {
                likedGenres.add(genre);
            }
        }

        for (MediaItems media : allMedia) {
            boolean genreMatch = likedGenres.contains(media.getGenre());
            boolean goodRating = media.getRating() >= 3.5;
            boolean shortEnough = media.getDuration() <= 10.4;
            boolean alreadyWatched = false;

            for (MediaItems watched : watchHistory) {
                if (watched.getTitle().equalsIgnoreCase(media.getTitle())) {
                    alreadyWatched = true;
                    break;
                }
            }

            if (genreMatch && goodRating && shortEnough && !alreadyWatched) {
                recommendations.add(media);
            }
        }

        return recommendations;
    }
}
