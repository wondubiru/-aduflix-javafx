import media.MediaItems;
import media.MediaLibrary;
import user.AuthManager;
import user.User;

import java.util.ArrayList;
import java.util.Scanner;

public class MainTest {
    public static void main(String[] args) {
        try {

            AuthManager.signUp("wondu", "secreet");
            boolean loginSuccess = AuthManager.signIn("Wondu", "secreet");
            System.out.println("LOgin Success? " + loginSuccess);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
