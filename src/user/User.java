package user;

import media.MediaItems;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private ArrayList<MediaItems> watchHistory;


    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.watchHistory = new ArrayList<>();
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public ArrayList<MediaItems> getWatchHistory() { return watchHistory; }

    // Simulate watching media
    public void watch(MediaItems item) {
        watchHistory.add(item);
        item.play();
        item.play();
        MediaItems.incrementWatched();
    }

    // View watch history
    public void viewHistory() {
        if (watchHistory.isEmpty()) {
            System.out.println(name + "'s watch history is empty.");
            return;
        }

        System.out.println("📜 Watch History for " + name + ":");
        for (MediaItems item : watchHistory) {
            System.out.println("- " + item);
        }
    }

    // Get recommendations based on genre + filter by rating and duration
    public ArrayList<MediaItems> getRecommendations(ArrayList<MediaItems> allMedia, double minRating, int maxDuration) {
        ArrayList<MediaItems> recs = new ArrayList<>();

        // Find favorite genres from watch history
        ArrayList<String> likedGenres = new ArrayList<>();
        for (MediaItems watched : watchHistory) {
            if (!likedGenres.contains(watched.getGenre())) {
                likedGenres.add(watched.getGenre());
            }
        }

        // Recommend items that match genre + filter by rating and duration
        for (MediaItems item : allMedia) {
            if (likedGenres.contains(item.getGenre())
                    && item.getRating() >= minRating
                    && item.getDuration() <= maxDuration
                    && !watchHistory.contains(item)) {
                recs.add(item);
            }
        }

        return recs;
    }

    // Export watch history to file
    public void exportHistory(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Watch History for " + name + ":\n");
            for (MediaItems item : watchHistory) {
                writer.write(item.toString() + "\n");
            }
            System.out.println(" Watch history exported to " + filename);
        } catch (IOException e) {
            System.out.println(" Error exporting history: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}
