package util;

import media.MediaItems; // Assuming MediaItems is in the 'media' package

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Utility class for file-related operations, such as exporting data.
 */
public class FileUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FileUtils() {
        // This class is not meant to be instantiated.
        // All methods are static.
    }

    /**
     * Exports a list of recommended media items to a specified file.
     *
     * @param recommendations The list of MediaItems to export.
     * @param username        The username of the user for whom the recommendations are made.
     * This is used for personalizing the output file content.
     * @param filePath        The full path (including filename) where the recommendations will be saved.
     */
    public static void exportRecommendationsToFile(ArrayList<MediaItems> recommendations, String username, String filePath) {
        if (recommendations == null || recommendations.isEmpty()) {
            System.out.println("No recommendations to export for " + username + ".");
            // Optionally, you could throw an IllegalArgumentException or return a boolean status
            return;
        }

        // Using try-with-resources to ensure the writer is closed automatically
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Recommendations for " + username + ":");
            writer.newLine();
            writer.newLine(); // Add an extra line for spacing

            int count = 1;
            for (MediaItems item : recommendations) {
                // You can customize the format here. Using toString() is a default.
                // Using item.getDetails() might provide more comprehensive info.
                writer.write(count + ". " + item.toString());
                writer.newLine();
                count++;
            }

            System.out.println("Recommendations for " + username + " successfully exported to: " + filePath);

        } catch (IOException e) {
            // Log the error or inform the user more gracefully, especially in a GUI application
            System.err.println("Error exporting recommendations for " + username + " to " + filePath + ": " + e.getMessage());
            // In a GUI app, you might show an alert dialog here.
        }
    }

    /**
     * Exports a user's watch history to a specified file.
     * This is similar to the method in your User class but centralized here.
     *
     * @param watchHistory The list of MediaItems representing the user's watch history.
     * @param username     The username of the user.
     * @param filePath     The full path (including filename) where the watch history will be saved.
     * @param mediaHelper  A functional interface or helper to get extra details for specific media types.
     */
    public static void exportWatchHistoryToFile(ArrayList<MediaItems> watchHistory, String username, String filePath, MediaDetailsProvider mediaHelper) {
        if (watchHistory == null) { // Allow exporting an empty history
            System.out.println("Watch history for " + username + " is null. Nothing to export.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Watch History for " + username + ":");
            writer.newLine();
            writer.newLine();

            if (watchHistory.isEmpty()) {
                writer.write("Watch history is empty.");
                writer.newLine();
            } else {
                for (MediaItems item : watchHistory) {
                    // Format: type,title,genre,rating,duration,extra1[,extra2]
                    String extraDetails = mediaHelper.getExtraDetails(item);
                    writer.write(item.getClass().getSimpleName().toLowerCase() + "," +
                            item.getTitle() + "," +
                            item.getGenre() + "," +
                            item.getRating() + "," +
                            item.getDuration() +
                            (extraDetails.isEmpty() ? "" : "," + extraDetails));
                    writer.newLine();
                }
            }
            System.out.println("Watch history for " + username + " successfully exported to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting watch history for " + username + " to " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Functional interface to provide a way to get specific extra details from MediaItems
     * without FileUtils needing to know about Movie, Series, Documentary directly.
     */
    @FunctionalInterface
    public interface MediaDetailsProvider {
        String getExtraDetails(MediaItems item);
    }
}
