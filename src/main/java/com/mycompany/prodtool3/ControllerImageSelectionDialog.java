package com.mycompany.prodtool3;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ControllerImageSelectionDialog {

    @FXML
    private Button chooseImageButton;
    
    private Stage dialogStage;  // To jest zmienna przechowująca odniesienie do okna dialogowego
    private File selectedImageFile;

    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().addAll(pngFilter, jpgFilter);

        selectedImageFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedImageFile != null) {
            Path destinationDir = Path.of("ProdTool3", "Session", "UserImages");
            try {
                if (Files.notExists(destinationDir)) {
                    Files.createDirectories(destinationDir);
                }
                Path destinationFile = destinationDir.resolve(selectedImageFile.getName());
                Files.copy(selectedImageFile.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
                showAlert(AlertType.INFORMATION, "Success", "Image has been added successfully.");
            } catch (IOException e) {
                showAlert(AlertType.ERROR, "Error", "Failed to save the image.");
                e.printStackTrace();
            }
        } else {
            showAlert(AlertType.WARNING, "No File Selected", "No file was selected.");
        }
    }
    
    @FXML
private void handleCancel() {
    if (dialogStage != null) {
        dialogStage.close();
    }
}

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public File getSelectedImageFile() {
        return selectedImageFile;
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
