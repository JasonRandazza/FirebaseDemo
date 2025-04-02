package aydin.firebasedemo;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PrimaryController {
    //Link user interface fields from primary.fxml
    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    //Register button click event handler
    @FXML
    void registerButtonClicked(ActionEvent event) {
        registerUser();
    }

    //Sign-in button click event handler
    @FXML
    void signinButtonClicked(ActionEvent event) {
        signinUser();
    }

    //Register new user with Firebase Authentication
    private void registerUser() {
        String email = emailTextField.getText().trim();
        String password = passwordField.getText();

        //Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert("Error", "Invalid email format");
            return;
        }

        //Validate non-empty fields
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email and password are required");
            return;
        }

        //Create user account in Firebase
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(email.split("@")[0]);

                     UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
                     showAlert("Success", "User Registered: " + userRecord.getUid());
        }
        catch (FirebaseAuthException e) {
            showAlert("Error", "Error registering user: " + e.getMessage());
        }
    }

    //Handle user sign-n
    private void signinUser() {
        String email = emailTextField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email and password are required");
            return;
        }

        try {
            //Verify if user already exists on firebase
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            showAlert("Success", "User Signed In: " + userRecord.getDisplayName());

            //Navigate to secondary screen
            DemoApp.setRoot("secondary");
        }
        catch (FirebaseAuthException | IOException e) {
            showAlert("Error", "Invalid credentials");
        }
    }

    //Display an alert box
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
