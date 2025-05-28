package user;

import java.io.*;

public class AuthManager {
    private static final String USER_FILE = "C:\\Users\\wondu\\IdeaProjects\\fx_app\\src\\data\\ media.txt"; // Fix path

    // === Sign up ===
    public static void signUp(String userName, String password) throws Exception {
        if (userExists(userName)) {
            throw new Exception("Username already exists: " + userName);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(userName + ":" + password); // simple colon-delimited
            writer.newLine();
        }
    }

    // === Sign in ===
    public static boolean signIn(String userName, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equalsIgnoreCase(userName)
                        && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading user file: " + ex.getMessage());
        }
        return false;
    }

    // === Check existence ===
    public static boolean userExists(String userName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length > 0 && parts[0].equalsIgnoreCase(userName)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error checking user existence: " + ex.getMessage());
        }
        return false;
    }
}
