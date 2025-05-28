package user;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import media.MediaAppGUI;

public class LoginUI extends Application {

    private TextField usernameField;
    private PasswordField passwordField;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ADUflix Login / Sign Up");

        usernameField = new TextField();
        passwordField = new PasswordField();
        statusLabel = new Label();

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login");
        Button signupBtn = new Button("Sign Up");



        loginBtn.setOnAction(e -> handleLogin());
        signupBtn.setOnAction(e -> handleSignup());

        HBox buttons = new HBox(10, loginBtn, signupBtn);
        VBox layout = new VBox(10,
                new Label("👋 Welcome to ADUflix"),
                usernameField,
                passwordField,
                buttons,
                statusLabel
        );

        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        Scene scene = new Scene(layout, 350, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean success = AuthManager.signIn(username, password);
        if (success) {
            statusLabel.setText("✅ Login successful!");
            launchMainApp(username);
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private void handleSignup() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            AuthManager.signUp(username, password);
            statusLabel.setText("✅ Account created. You can now log in.");
        } catch (Exception e) {
            showAlert("Signup Failed", e.getMessage());
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void launchMainApp(String username) {
        MediaAppGUI app = new MediaAppGUI(username);
        try {
            Stage mainStage = new Stage();
            app.start(mainStage);
            ((Stage) usernameField.getScene().getWindow()).close();
        } catch (Exception e) {
            System.out.println("Error launching MediaAppGUI: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
