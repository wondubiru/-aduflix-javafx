package media;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class MediaLibrary {

    private ArrayList<MediaItems> mediaList;

    // === Constructor ===
    public MediaLibrary() {
        mediaList = new ArrayList<>();
    }

    // === Public: Get all media ===
    public ArrayList<MediaItems> getAllMedia() {
        return mediaList;
    }

    // === Public: Add and Save ===
    public void addMedia(MediaItems item) {
        if (item != null) {
            mediaList.add(item);
        } else {
            System.out.println("Cannot add null media item.");
        }
    }

    // === Public: Save a new media item to a .txt file ===
    public void saveMediaToFile(MediaItems media, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            if (media instanceof Movie m) {
                writer.write("movie," + m.getTitle() + "," + m.getGenre() + "," + m.getRating() + "," + m.getDuration() + "," + m.getDirector());
            } else if (media instanceof Series s) {
                writer.write("series," + s.getTitle() + "," + s.getGenre() + "," + s.getRating() + "," + s.getDuration() + "," + s.getNumberOfEpisodes() + "," + s.getSeasons());
            } else if (media instanceof Documentary d) {
                writer.write("documentary," + d.getTitle() + "," + d.getGenre() + "," + d.getRating() + "," + d.getDuration() + "," + d.getSubject());
            }
            writer.newLine();
        } catch (IOException e) {
            System.out.println(" Could not write media to file: " + e.getMessage());
        }
    }

    public void saveMediaToFile(MediaItems media) {
        // Overloaded method — defaults to main file
        saveMediaToFile(media, "src/data/media.txt");
    }



    // === Load media from file ===
    public void loadMediaFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0].trim().toLowerCase();

                switch (type) {
                    case "movie":
                        mediaList.add(new Movie(parts[1], parts[2],
                                Double.parseDouble(parts[3]),
                                Double.parseDouble(parts[4]),
                                parts[5]));
                        break;
                    case "series":
                        mediaList.add(new Series(parts[1], parts[2],
                                Double.parseDouble(parts[3]),
                                Double.parseDouble(parts[4]),
                                Integer.parseInt(parts[5]),
                                Integer.parseInt(parts[6])));
                        break;
                    case "documentary":
                        mediaList.add(new Documentary(parts[1], parts[2],
                                Double.parseDouble(parts[3]),
                                Double.parseDouble(parts[4]),
                                parts[5]));
                        break;
                    default:
                        System.out.println(" Unknown media type: " + type);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println(" Error reading media file: " + e.getMessage());
        }
    }

    // === Filter ===
    public ArrayList<MediaItems> filterByGenre(String genre) {
        ArrayList<MediaItems> filtered = new ArrayList<>();
        for (MediaItems item : mediaList) {
            if (item.getGenre().equalsIgnoreCase(genre)) {
                filtered.add(item);
            }
        }
        return filtered;
    }

   // === Sort by rating ===
    public void sortByRating() {
        Collections.sort(mediaList); // Uses compareTo in MediaItem
    }

    public MediaItems getTopRated() {
        if (mediaList.isEmpty()) return null;
        sortByRating();
        return mediaList.get(mediaList.size() - 1);
    }
}
