package org.example.mongodb_teht;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.bson.Document;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField nameField;

    private MongoDBUtil mongoDBUtil;

    @FXML
    public void initialize() {
        try {
            mongoDBUtil = new MongoDBUtil();
        } catch (Exception e) {
            welcomeText.setText("Failed to connect to MongoDB: " + e.getMessage());
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        try {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                welcomeText.setText("Please enter a name");
                return;
            }
            mongoDBUtil.createDocument(name.trim());
            welcomeText.setText("Document created with name: " + name);
        } catch (Exception e) {
            welcomeText.setText("Error creating document: " + e.getMessage());
        }
    }

    @FXML
    protected void onReadButtonClick() {
        try {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                welcomeText.setText("Please enter a name");
                return;
            }
            Document document = mongoDBUtil.readDocumentByName(name.trim());
            if (document != null) {
                welcomeText.setText("Document found: " + document.toJson());
            } else {
                welcomeText.setText("No document found with name: " + name);
            }
        } catch (Exception e) {
            welcomeText.setText("Error reading document: " + e.getMessage());
        }
    }

    @FXML
    protected void onUpdateButtonClick() {
        try {
            String id = nameField.getText();
            if (id == null || id.trim().isEmpty()) {
                welcomeText.setText("Please enter an ID");
                return;
            }
            String newName = "Updated Name"; // Example new name
            mongoDBUtil.updateDocument(id.trim(), newName);
            welcomeText.setText("Document updated with new name: " + newName);
        } catch (Exception e) {
            welcomeText.setText("Error updating document: " + e.getMessage());
        }
    }

    @FXML
    protected void onDeleteButtonClick() {
        try {
            String id = nameField.getText();
            if (id == null || id.trim().isEmpty()) {
                welcomeText.setText("Please enter an ID");
                return;
            }
            mongoDBUtil.deleteDocument(id.trim());
            welcomeText.setText("Document deleted with ID: " + id);
        } catch (Exception e) {
            welcomeText.setText("Error deleting document: " + e.getMessage());
        }
    }

    public void close() {
        if (mongoDBUtil != null) {
            try {
                mongoDBUtil.close();
            } catch (Exception e) {
                System.err.println("Error closing MongoDB connection: " + e.getMessage());
            }
        }
    }
}