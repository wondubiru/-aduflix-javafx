package media;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import logic.RecommendationEngine;
import user.User;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;


public class MediaAppGUI extends Application {

    /**
     * Main entry point for the ADUflix JavaFX application.
     * Initializes the media library, user, and GUI components.
     *
     * @param username The username of the logged-in user.
     */
    private MediaLibrary library;
    private User user;
    private String currentUsername;

    private ListView<MediaItems> mediaListView;
    private TextArea outputArea;

    /**
     * Constructor for MediaAppGUI.
     * Initializes the GUI with the given username.
     *
     * @param username The username of the user currently logged in.
     */

    public MediaAppGUI(String username) {
        this.currentUsername = username;
    }

    @Override

    public void start(Stage primaryStage) {
        System.out.println("\uD83C\uDF89 JavaFX is starting!");

        library = new MediaLibrary();
        library.loadMediaFromFile("src/data/media.txt");
        user = new User(currentUsername, currentUsername + "@aduflix.com");

        mediaListView = new ListView<>();
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setPrefHeight(150);

        Button watchButton = new Button("\u25B6 Watch");
        Button rateButton = new Button("\u2B50 Rate");
        Button recommendButton = new Button("\uD83C\uDFAF Recommend");
        Button historyButton = new Button("\uD83D\uDCDC History");
        Button addMediaButton = new Button("+ Add Media");
        Button logoutBtn = new Button("\uD83D\uDEAA Logout");

        library.sortByRating();
        mediaListView.getItems().addAll(library.getAllMedia());
        Collections.reverse(mediaListView.getItems());

        mediaListView.setPlaceholder(new Label("\uD83D\uDCEC No media available. Check media.txt."));
        if (!mediaListView.getItems().isEmpty()) {
            mediaListView.getSelectionModel().selectFirst();
        }

        Label userInfoLabel = new Label("\uD83D\uDC64 Logged in as: " + user.getName() + " (" + user.getEmail() + ")");
        Label sortLabel = new Label("\uD83D\uDCCA Sorted by rating (highest first)");
        Label listLabel = new Label("\uD83D\uDCC2 Select a Media Item Below:");


        HBox buttonBar = new HBox(10, watchButton, rateButton, recommendButton, historyButton, addMediaButton, logoutBtn);
        VBox root = new VBox(10, userInfoLabel, sortLabel, listLabel, mediaListView, buttonBar, outputArea);
        root.setStyle("-fx-padding: 20;");

        watchButton.setOnAction(e -> handleWatch());
        rateButton.setOnAction(e -> handleRate());
        recommendButton.setOnAction(e -> handleRecommendations());
        historyButton.setOnAction(e -> handleHistory());
        logoutBtn.setOnAction(e -> handleLogout());
        addMediaButton.setOnAction(e -> openAddMediaDialog());

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setTitle("ADUflix - JavaFX Edition");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleWatch() {
        MediaItems selected = mediaListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            user.watch(selected);
            outputArea.setText("Now watching: " + selected.getTitle() +
                    "\n\uD83D\uDCCA Total items watched across all users: " + MediaItems.getTotalWatched());
        } else {
            showAlert("No Media Selected", "Please choose a media item from the list before clicking Watch.");
        }
    }

    private void handleRate() {
        MediaItems selected = mediaListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            TextInputDialog dialog = new TextInputDialog("8.5");
            dialog.setTitle("Rate Media");
            dialog.setHeaderText("Enter your rating (0–10):");
            dialog.setContentText("Rating:");

            dialog.showAndWait().ifPresent(input -> {
                try {
                    double rating = Double.parseDouble(input);
                    selected.setRating(rating);
                    outputArea.setText("Rated '" + selected.getTitle() + "' as " + rating + " stars.");
                } catch (NumberFormatException ex) {
                    showAlert("Invalid Rating", "Please enter a valid number.");
                }
            });
        } else {
            showAlert("No Media Selected", "Select something before rating.");
        }
    }
    private void handleRecommendations() {
        ArrayList<MediaItems> recs = RecommendationEngine.recommend(
                library.getAllMedia(),
                user.getWatchHistory()
        );

        if (recs.isEmpty()) {
            outputArea.setText("");
        } else {
            StringBuilder sb = new StringBuilder("\uD83C\uDFAF Recommended for You:\n");
            for (MediaItems item : recs) {
                sb.append("- ").append(item).append("\n");
            }
            outputArea.setText(sb.toString());
        }
    }


    private void handleHistory() {
        user.importHistory("Watch_history_" + currentUsername + ".txt");

        if (user.getWatchHistory().isEmpty()) {
            outputArea.setText("Watch history is empty.");
        } else {
            StringBuilder sb = new StringBuilder(" Your Watch History:\n");
            for (MediaItems item : user.getWatchHistory()) {
                sb.append("- ").append(item).append("\n");
            }
            outputArea.setText(sb.toString());
        }
    }


    private void handleLogout() {
        user.exportHistory("Watch_history_" + currentUsername + ".txt");
        Stage currentStage = (Stage) mediaListView.getScene().getWindow();
        currentStage.close();
        try {
            user.LoginUI loginScreen = new user.LoginUI();
            Stage loginStage = new Stage();
            loginScreen.start(loginStage);
        } catch (Exception ex) {
            System.out.println(" Failed to return to login: " + ex.getMessage());
        }
    }

    private void openAddMediaDialog() {
        Dialog<MediaItems> dialog = new Dialog<>();
        dialog.setTitle("Add New Media");
        dialog.setHeaderText("Enter media details:");
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("movie", "series", "documentary");
        typeBox.setValue("movie");

        TextField titleField = new TextField();
        TextField genreField = new TextField();
        TextField ratingField = new TextField();
        TextField durationField = new TextField();
        Label extra1Label = new Label("Extra 1:");
        TextField extra1 = new TextField();

        Label extra2Label = new Label("Extra 2:");
        TextField extra2 = new TextField();

        // Layout
        grid.add(new Label("Type:"), 0, 0); grid.add(typeBox, 1, 0);
        grid.add(new Label("Title:"), 0, 1); grid.add(titleField, 1, 1);
        grid.add(new Label("Genre:"), 0, 2); grid.add(genreField, 1, 2);
        grid.add(new Label("Rating:"), 0, 3); grid.add(ratingField, 1, 3);
        grid.add(new Label("Duration:"), 0, 4); grid.add(durationField, 1, 4);
        grid.add(extra1Label, 0, 5); grid.add(extra1, 1, 5);
        grid.add(extra2Label, 0, 6); grid.add(extra2, 1, 6);

        // === Dynamic label & field logic ===
        typeBox.setOnAction(e -> {
            String selected = typeBox.getValue();
            switch (selected) {
                case "movie" -> {
                    extra1Label.setText("Director:");
                    extra1.setPromptText("e.g. Johan");
                    extra1Label.setVisible(true);
                    extra1.setVisible(true);
                    extra2Label.setVisible(false);
                    extra2.setVisible(false);
                }
                case "series" -> {
                    extra1Label.setText("Episodes:");
                    extra1.setPromptText("e.g. 10");
                    extra2Label.setText("Seasons:");
                    extra2.setPromptText("e.g. 3");
                    extra1Label.setVisible(true);
                    extra1.setVisible(true);
                    extra2Label.setVisible(true);
                    extra2.setVisible(true);
                }
                case "documentary" -> {
                    extra1Label.setText("Subject:");
                    extra1.setPromptText("e.g. Space");
                    extra1Label.setVisible(true);
                    extra1.setVisible(true);
                    extra2Label.setVisible(false);
                    extra2.setVisible(false);
                }
            }
        });

        // Initial trigger to show correct labels
        typeBox.getOnAction().handle(null);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String type = typeBox.getValue();
                    String title = titleField.getText();
                    String genre = genreField.getText();
                    double rating = Double.parseDouble(ratingField.getText());
                    double duration = Double.parseDouble(durationField.getText());

                    if (title.isBlank() || genre.isBlank()) {
                        showAlert("Input Error", "Title and genre must not be empty.");
                        return null;
                    }

                    return switch (type) {
                        case "movie" -> new Movie(title, genre, rating, duration, extra1.getText());
                        case "series" -> new Series(
                                title, genre, rating, duration,
                                Integer.parseInt(extra1.getText()),
                                Integer.parseInt(extra2.getText()));
                        case "documentary" -> new Documentary(title, genre, rating, duration, extra1.getText());
                        default -> null;
                    };
                } catch (Exception e) {
                    showAlert("Input Error", "Please enter valid values (e.g. numeric rating/duration).");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(media -> {

            library.getAllMedia().add(media);
            mediaListView.getItems().add(media);
            library.saveMediaToFile(media);
        });
    }

    /**
     * Displays an alert dialog with the given title and message.
     *
     * @param title   The title of the alert dialog.
     * @param message The message to display in the alert dialog.
     */

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ADUflix Info");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}