import logic.RecommendationEngine;
import media.MediaFactory;
import media.MediaItems;
import media.MediaLibrary;
import user.AuthManager;
import user.InvalidMediaException;
import user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainTest {
    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser;
    private static MediaLibrary library;

    public static void main(String[] args) {
        library = new MediaLibrary();
        library.loadMediaFromFile("src/data/media.txt"); // Load media from file

        System.out.println("Welcome to ADUflix!");
        displayLoginSignUpMenu();
    }

    private static void displayLoginSignUpMenu() {
        while (currentUser == null) {
            System.out.println("\n--- ADUflix Login/Sign Up ---");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        handleLogin();
                        break;
                    case 2:
                        handleSignUp();
                        break;
                    case 3:
                        System.out.println("Exiting ADUflix. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

        if (currentUser != null) {
            displayMainMenu();
        }
    }

    private static void handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (AuthManager.signIn(username, password)) {
            currentUser = new User(username, username + "@aduflix.com"); // Assuming email format
            currentUser.importHistory("Watch_history_" + username + ".txt");
            System.out.println("Login successful! Welcome, " + currentUser.getName() + ".");
        } else {
            System.out.println("Login failed. Invalid username or password.");
        }
    }

    private static void handleSignUp() {
        System.out.print("Enter desired username: ");
        String username = scanner.nextLine();
        System.out.print("Enter desired password: ");
        String password = scanner.nextLine();

        try {
            AuthManager.signUp(username, password);
            System.out.println("Account created successfully! You can now log in.");
        } catch (Exception e) {
            System.out.println("Sign up failed: " + e.getMessage());
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n--- ADUflix Main Menu ---");
        System.out.println("Logged in as: " + currentUser.getName());

        int choice;
        do {
            System.out.println("\n1. View All Media");
            System.out.println("2. Watch Media");
            System.out.println("3. Rate Media");
            System.out.println("4. View Watch History");
            System.out.println("5. Get Recommendations");
            System.out.println("6. Create Media");
            System.out.println("7. Logout"); // Logout is now option 7

            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewAllMedia();
                        break;
                    case 2:
                        watchMedia();
                        break;
                    case 3:
                        rateMedia();
                        break;
                    case 4:
                        currentUser.viewHistory();
                        break;
                    case 5:
                        getRecommendations();
                        break;
                    case 6:
                        createMedia();
                        break;
                    case 7: // Changed from 6 to 7
                        handleLogout();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
                choice = 0; // Keep loop running
            }
        } while (choice != 7); // Changed from 6 to 7
    }

    private static void viewAllMedia() {
        if (library.getAllMedia().isEmpty()) {
            System.out.println("No media available in the library.");
            return;
        }
        System.out.println("\n--- All Available Media ---");
        int i = 1;
        for (MediaItems item : library.getAllMedia()) {
            System.out.println(i++ + ". " + item.getDetails());
        }
    }

    private static void watchMedia() {
        viewAllMedia();
        if (library.getAllMedia().isEmpty()) {
            return;
        }

        System.out.print("Enter the number of the media you want to watch: ");
        try {
            int mediaIndex = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (mediaIndex > 0 && mediaIndex <= library.getAllMedia().size()) {
                MediaItems selectedMedia = library.getAllMedia().get(mediaIndex - 1);
                currentUser.watch(selectedMedia);
                System.out.println("You are now watching: " + selectedMedia.getTitle());
                System.out.println("Total items watched across all users: " + MediaItems.getTotalWatched());
            } else {
                System.out.println("Invalid media number.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    private static void rateMedia() {
        viewAllMedia();
        if (library.getAllMedia().isEmpty()) {
            return;
        }

        System.out.print("Enter the number of the media you want to rate: ");
        try {
            int mediaIndex = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (mediaIndex > 0 && mediaIndex <= library.getAllMedia().size()) {
                MediaItems selectedMedia = library.getAllMedia().get(mediaIndex - 1);
                System.out.print("Enter your rating (0-10): ");
                double rating = scanner.nextDouble();
                scanner.nextLine(); // Consume newline

                selectedMedia.setRating(rating);
            } else {
                System.out.println("Invalid media number.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number for rating.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    private static void getRecommendations() {
        ArrayList<MediaItems> recommendations = RecommendationEngine.recommend(
                library.getAllMedia(),
                currentUser.getWatchHistory()
        );

        if (recommendations.isEmpty()) {
            System.out.println("No recommendations available based on your watch history yet.");
            System.out.println("Watch more media to get better recommendations!");
        } else {
            System.out.println("\n--- Recommended for You ---");
            int i = 1;
            for (MediaItems item : recommendations) {
                System.out.println(i++ + ". " + item.getDetails());
            }
        }
    }

    private static void handleLogout() {
        if (currentUser != null) {
            currentUser.exportHistory("Watch_history_" + currentUser.getName() + ".txt");
            System.out.println("Logged out successfully. Your watch history has been saved.");
            currentUser = null; // Clear current user
            displayLoginSignUpMenu(); // Return to login/signup menu
        }
    }

    private static void createMedia() {
        System.out.println("\n--- Create New Media ---");
        MediaFactory factory = new MediaFactory(scanner);
        try {
            MediaItems newMedia = factory.createMediaFromInput(); // This call can now throw
            // If createMediaFromInput is successful, newMedia will not be null here

            library.addMedia(newMedia);
            library.saveMediaToFile(newMedia); // Ensure this handles its potential IOExceptions
            System.out.println("Media added successfully: " + newMedia.getTitle());

            System.out.print("Do you want to watch it now? (yes/no): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("yes") || answer.equals("y")) {
                if (currentUser != null) { // Make sure currentUser is available
                    currentUser.watch(newMedia);
                    System.out.println("Now watching: " + newMedia.getTitle());
                } else {
                    System.out.println("No user logged in to watch media.");
                }
            }
        } catch (InvalidMediaException e) {
            System.err.println("Error creating media: " + e.getMessage());
            // You could also print e.getCause() if it's not null for more details
        } catch (Exception e) { // Catch other potential unexpected exceptions
            System.err.println("An unexpected error occurred during media creation: " + e.getMessage());
            e.printStackTrace(); // Good for debugging
        }
    }

}
