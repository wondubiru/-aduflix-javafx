package user;

import media.Documentary;
import media.MediaItems;
import media.Movie;
import media.Series;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class User {
    private String name;
    private String email;
    private ArrayList<MediaItems> watchHistory;
    /**
     * Constructor for User class.
     * Initializes the user's name, email, and watch history.
     *
     * @param name  The name of the user.
     * @param email The email of the user.
     */

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.watchHistory = new ArrayList<>();
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public ArrayList<MediaItems> getWatchHistory() { return watchHistory; }

    // Watch a media item
    public void watch(MediaItems item) {
        watchHistory.add(item);
        item.play();
        MediaItems.incrementWatched();
    }

    // View watch history
    public void viewHistory() {
        if (watchHistory.isEmpty()) {
            System.out.println(name + "'s watch history is empty.");
            return;
        }

        System.out.println(" Watch History for " + name + ":");
        for (MediaItems item : watchHistory) {
            System.out.println("- " + item);
        }
    }

    // Export watch history to file
    /**
     * Exports the user's watch history to a specified file.
     *
     * @param filename The name of the file to export the history to.
     */
    public void exportHistory(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Watch History for " + name + ":\n");
            for (MediaItems item : watchHistory) {
                writer.write(item.getClass().getSimpleName().toLowerCase() + ","
                        + item.getTitle() + ","
                        + item.getGenre() + ","
                        + item.getRating() + ","
                        + item.getDuration() + ","
                        + getExtra(item) + "\n");
            }
        } catch (IOException e) {
            System.out.println(" Error exporting history: " + e.getMessage());
        }
    }


    private String getExtra(MediaItems item) {
        if (item instanceof Movie m) {
            return m.getDirector();
        } else if (item instanceof Series s) {
            return s.getNumberOfEpisodes() + "," + s.getSeasons();
        } else if (item instanceof Documentary d) {
            return d.getSubject();
        }
        return "";
    }



    public void importHistory(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Watch History")) continue;
                MediaItems item = MediaItems.parse(line);
                if (item != null) {
                    watchHistory.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println(" No previous history file found.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User otherUser = (User) obj;
        // Assuming email is a unique identifier for a user for equality
        return Objects.equals(email, otherUser.email);
    }

    @Override
    public int hashCode() {
        // Use the same fields as in equals()
        return Objects.hash(email);
    }

    @Override
    public String toString() { // Ensure this provides a good representation
        return name + " (" + email + ")";
    }


}
