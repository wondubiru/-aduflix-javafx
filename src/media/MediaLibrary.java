package media;

import media.MediaItems;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MediaLibrary {
    private ArrayList<MediaItems> mediaList;

    public MediaLibrary() {
        mediaList = new ArrayList<>();
    }

    public ArrayList<MediaItems> getAllMedia() {
        return mediaList;
    }


    // Load media from a file
    public void loadMediaFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: type,title,genre,rating,duration,special_field
                // Example: movie,Inception,Action,9.0,148,Christopher Nolan
                String[] parts = line.split(",");
                String type = parts[0].trim().toLowerCase();

                switch (type) {
                    case "movie":
                        mediaList.add(new Movies(parts[1], parts[2],
                                Double.parseDouble(parts[3]),
                                Integer.parseInt(parts[4]),
                                parts[5]));
                        break;
                    case "series":
                        mediaList.add(new Series(parts[1], parts[2],
                                Double.parseDouble(parts[3]),
                                Integer.parseInt(parts[4]),
                                Integer.parseInt(parts[5]),
                                Integer.parseInt(parts[6])));
                        break;
                    case "documentary":
                        mediaList.add(new Documentary(parts[1], parts[2],
                                Double.parseDouble(parts[3]),
                                Integer.parseInt(parts[4]),
                                parts[5]));
                        break;
                    default:
                        System.out.println(" Unknown media type: " + type);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading media file: " + e.getMessage());
        }
    }

    public ArrayList<MediaItems> filterByGenre(String genre) {
        ArrayList<MediaItems> filtered = new ArrayList<>();
        for (MediaItems item : mediaList) {
            if (item.getGenre().equalsIgnoreCase(genre)) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public void sortByRating() {
        Collections.sort(mediaList); // Uses compareTo in MediaItem
    }

    public MediaItems getTopRated() {
        if (mediaList.isEmpty()) return null;
        sortByRating();
        return mediaList.get(mediaList.size() - 1);
    }
}
