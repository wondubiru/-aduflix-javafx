package media;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import user.User;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class MediaAppGUI extends Application {

    private MediaLibrary library;
    private User user;
    private String currentUsername;

    private ListView<MediaItems> mediaListView;
    private TextArea outputArea;

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
        ArrayList<MediaItems> recs = user.getRecommendations(library.getAllMedia(), 7.0, 180);
        if (recs.isEmpty()) {
            outputArea.setText("No recommendations at this time.");
        } else {
            StringBuilder sb = new StringBuilder("\uD83C\uDFAF Recommended for You:\n");
            for (MediaItems item : recs) {
                sb.append("- ").append(item).append("\n");
            }
            outputArea.setText(sb.toString());
        }
    }

    private void handleHistory() {
        if (user.getWatchHistory().isEmpty()) {
            outputArea.setText("Watch history is empty.");
        } else {
            StringBuilder sb = new StringBuilder("\uD83D\uDCDC Your Watch History:\n");
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
        TextField extra1 = new TextField();
        TextField extra2 = new TextField();

        grid.add(new Label("Type:"), 0, 0); grid.add(typeBox, 1, 0);
        grid.add(new Label("Title:"), 0, 1); grid.add(titleField, 1, 1);
        grid.add(new Label("Genre:"), 0, 2); grid.add(genreField, 1, 2);
        grid.add(new Label("Rating:"), 0, 3); grid.add(ratingField, 1, 3);
        grid.add(new Label("Duration:"), 0, 4); grid.add(durationField, 1, 4);
        grid.add(new Label("Extra 1:"), 0, 5); grid.add(extra1, 1, 5);
        grid.add(new Label("Extra 2 (for Series):"), 0, 6); grid.add(extra2, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String type = typeBox.getValue();
                    String title = titleField.getText();
                    String genre = genreField.getText();
                    double rating = Double.parseDouble(ratingField.getText());
                    int duration = Integer.parseInt(durationField.getText());

                    return switch (type) {
                        case "movie" -> new Movies(title, genre, rating, duration, extra1.getText());
                        case "series" -> new Series(title, genre, rating, duration, Integer.parseInt(extra1.getText()), Integer.parseInt(extra2.getText()));
                        case "documentary" -> new Documentary(title, genre, rating, duration, extra1.getText());
                        default -> null;
                    };

                } catch (Exception e) {
                    showAlert("Input Error", "Please enter valid data.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(media -> {
            library.getAllMedia().add(media);
            mediaListView.getItems().add(media);
            appendToMediaFile(media);
        });
    }

    private void appendToMediaFile(MediaItems media) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/media.txt", true))) {
            if (media instanceof Movies m) {
                writer.write("movie," + m.getTitle() + "," + m.getGenre() + "," + m.getRating() + "," + m.getDuration() + "," + m.getDirector());
            } else if (media instanceof Series s) {
                writer.write("series," + s.getTitle() + "," + s.getGenre() + "," + s.getRating() + "," + s.getDuration() + "," + s.getNumberOfEpisodes() + "," + s.getSeasons());
            } else if (media instanceof Documentary d) {
                writer.write("documentary," + d.getTitle() + "," + d.getGenre() + "," + d.getRating() + "," + d.getDuration() + "," + d.getSubject());
            }
            writer.newLine();
        } catch (IOException e) {
            showAlert("File Error", "Could not save to media.txt");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ADUflix Info");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}