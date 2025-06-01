package media;

import user.InvalidMediaException; // Import your custom exception
import java.util.Scanner;

public class MediaFactory {
    private Scanner scanner;

    public MediaFactory(Scanner scanner) {
        this.scanner = scanner;
    }

    public static MediaItems createMedia(String type, String[] fields) throws InvalidMediaException { // Added throws
        try {
            String title = fields[0];
            String genre = fields[1];
            if (title == null || title.isBlank()) {
                throw new InvalidMediaException("Media title cannot be empty.");
            }
            if (genre == null || genre.isBlank()) {
                throw new InvalidMediaException("Media genre cannot be empty.");
            }
            double rating = Double.parseDouble(fields[2]);
            double duration = Double.parseDouble(fields[3]);

            if (rating < 0 || rating > 10) {
                throw new InvalidMediaException("Rating must be between 0 and 10. Provided: " + rating);
            }
            if (duration <= 0) {
                throw new InvalidMediaException("Duration must be greater than 0 minutes. Provided: " + duration);
            }

            switch (type.toLowerCase()) {
                case "movie":
                    if (fields.length < 5 || fields[4] == null || fields[4].isBlank()) throw new InvalidMediaException("Director cannot be empty for a movie.");
                    return new Movie(title, genre, rating, duration, fields[4]);
                case "series":
                    if (fields.length < 6) throw new InvalidMediaException("Series requires number of episodes and seasons.");
                    int episodes = Integer.parseInt(fields[4]);
                    int seasons = Integer.parseInt(fields[5]);
                    if (episodes <= 0) throw new InvalidMediaException("Number of episodes must be greater than 0.");
                    if (seasons <= 0) throw new InvalidMediaException("Number of seasons must be greater than 0.");
                    return new Series(title, genre, rating, duration, episodes, seasons);
                case "documentary":
                    if (fields.length < 5 || fields[4] == null || fields[4].isBlank()) throw new InvalidMediaException("Subject cannot be empty for a documentary.");
                    return new Documentary(title, genre, rating, duration, fields[4]);
                default:
                    throw new InvalidMediaException("Unknown media type provided: " + type);
            }
        } catch (NumberFormatException e) {
            throw new InvalidMediaException("Invalid numeric value provided for rating, duration, episodes, or seasons.", e);
        }
    }

    public MediaItems createMediaFromInput() throws InvalidMediaException { // Added throws
        System.out.print("Enter media type (movie/series/documentary): ");
        String type = scanner.nextLine().trim().toLowerCase();

        if (!type.equals("movie") && !type.equals("series") && !type.equals("documentary")) {
            throw new InvalidMediaException("Invalid media type entered: " + type + ". Must be 'movie', 'series', or 'documentary'.");
        }

        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        if (title.isBlank()) {
            throw new InvalidMediaException("Title cannot be empty.");
        }

        System.out.print("Enter genre: ");
        String genre = scanner.nextLine().trim();
        if (genre.isBlank()) {
            throw new InvalidMediaException("Genre cannot be empty.");
        }

        double rating;
        try {
            System.out.print("Enter rating (0-10): ");
            rating = Double.parseDouble(scanner.nextLine().trim());
            if (rating < 0 || rating > 10) {
                throw new InvalidMediaException("Rating must be between 0 and 10. You entered: " + rating);
            }
        } catch (NumberFormatException e) {
            throw new InvalidMediaException("Invalid format for rating. Please enter a number.", e);
        }

        double duration;
        try {
            System.out.print("Enter duration (in minutes): ");
            duration = Double.parseDouble(scanner.nextLine().trim());
            if (duration <= 0) {
                throw new InvalidMediaException("Duration must be a positive number. You entered: " + duration);
            }
        } catch (NumberFormatException e) {
            throw new InvalidMediaException("Invalid format for duration. Please enter a number.", e);
        }

        String[] fields;
        switch (type) {
            case "movie":
                System.out.print("Enter director: ");
                String director = scanner.nextLine().trim();
                if (director.isBlank()) {
                    throw new InvalidMediaException("Director cannot be empty for a movie.");
                }
                fields = new String[]{title, genre, String.valueOf(rating), String.valueOf(duration), director};
                break;
            case "series":
                int seasons;
                int episodes;
                try {
                    System.out.print("Enter number of seasons: ");
                    seasons = Integer.parseInt(scanner.nextLine().trim());
                    if (seasons <= 0) {
                        throw new InvalidMediaException("Number of seasons must be a positive integer.");
                    }
                    System.out.print("Enter number of episodes: ");
                    episodes = Integer.parseInt(scanner.nextLine().trim());
                    if (episodes <= 0) {
                        throw new InvalidMediaException("Number of episodes must be a positive integer.");
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidMediaException("Invalid format for seasons or episodes. Please enter whole numbers.", e);
                }
                fields = new String[]{title, genre, String.valueOf(rating), String.valueOf(duration), String.valueOf(episodes), String.valueOf(seasons)}; // Corrected order based on typical Series constructor
                break;
            case "documentary":
                System.out.print("Enter subject: ");
                String subject = scanner.nextLine().trim();
                if (subject.isBlank()) {
                    throw new InvalidMediaException("Subject cannot be empty for a documentary.");
                }
                fields = new String[]{title, genre, String.valueOf(rating), String.valueOf(duration), subject};
                break;
            default:
                // This case should ideally not be reached due to the check at the beginning
                throw new InvalidMediaException("Invalid media type: " + type);
        }
        // The createMedia static method also throws InvalidMediaException
        return createMedia(type, fields);
    }
}