package aydin.firebasedemo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SecondaryController {
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private Button readButton, writeButton, backButton;

    @FXML
    void readButtonClicked() {
        readFirebase();
    }

    @FXML
    void writeButtonClicked() {
        addData();
    }

    @FXML
    void backButtonClicked() throws IOException {
        DemoApp.setRoot("primary");
    }

    private void readFirebase() {
        try {
            ApiFuture<QuerySnapshot> future = DemoApp.fstore.collection("Persons").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            if (documents.isEmpty()) {
                showAlert("Info", "No data available");
                return;
            }

            outputTextArea.clear();
            for (QueryDocumentSnapshot document : documents) {
                String name = document.getString("Name");
                Object ageObj = document.get("Age");
                String phone = document.getString("Phone");

                String ageText;
                if (ageObj instanceof Number) {
                    ageText = String.valueOf(((Number) ageObj).intValue());
                }
                else if (ageObj instanceof String) {
                    ageText = (String) ageObj;
                }
                else {
                    ageText = "Unknown";
                }
                outputTextArea.appendText(name + ", Age: " + ageText + ", Phone: " + phone + "\n");
            }
        }
        catch (InterruptedException | ExecutionException e) {
            showAlert("Error", "Failed to read data from Firestore");
        }
    }

    //Append data
    private void addData() {
        try {
            String name = nameTextField.getText().trim();
            String ageText = ageTextField.getText().trim();
            String phone = phoneTextField.getText().trim();

            if (name.isEmpty() || ageText.isEmpty() || phone.isEmpty()) {
                showAlert("Error", "All fields (Name, Age, Phone) are required.");
                return;
            }

            int age = Integer.parseInt(ageText);

            DocumentReference docRef = DemoApp.fstore.collection("Persons").document(UUID.randomUUID().toString());
            Map<String, Object> data = new HashMap<>();
            data.put("Name", name);
            data.put("Age", age);
            data.put("Phone", phone);

            ApiFuture<WriteResult> result = docRef.set(data);
            showAlert("Success", "Data added to Firestore");
        }
        catch (NumberFormatException e) {
            showAlert("Error", "Age must be a number");
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
