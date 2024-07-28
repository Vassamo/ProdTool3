package com.mycompany.prodtool3;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class ControllerAddCategoryDialog {

    @FXML
    private TextFlow categoryNameFlow; // Zmieniono z categoryNameField na categoryNameFlow

    @FXML
    private Button chooseImageButton;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextFlow imageTitleFlow; // Zmieniono z imageTitleField na imageTitleFlow

    private Stage stage;
    private CategoryClass parentCategory;
    private File chosenImageFile;
    
    private Stage dialogStage;
    
    @FXML
    private void handleOk() {
        String categoryName = getTextFromTextFlow(categoryNameFlow);
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            if (parentCategory != null) {
                parentCategory.addSubcategory(new CategoryClass(categoryName, parentCategory));
            }
            if (dialogStage != null) {
                dialogStage.close();
            }
        } else {
            showAlert("Invalid Input", "Category name cannot be empty.");
        }
    }

    @FXML
    private void handleCancel() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    public void setParentCategory(CategoryClass parentCategory) {
        this.parentCategory = parentCategory;
    }

    public void setStage(Stage stage) {
        this.dialogStage = stage;
        this.stage = stage; // Ustawienie głównego okna aplikacji
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleChooseImage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/prodtool3/ImageSelectionDialog.fxml"));
            VBox root = loader.load();
            
            ControllerImageSelectionDialog controller = loader.getController();
            if (controller == null) {
                throw new RuntimeException("Controller is null. Check FXML file and controller class.");
            }

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(this.stage); // Ustawienie właściciela okna
            dialogStage.setTitle("Select Image");
            dialogStage.setScene(new Scene(root));
            
            controller.setDialogStage(dialogStage); // Ustawiamy `Stage` w kontrolerze
            
            dialogStage.showAndWait();
            
            // Uzyskaj wybrany plik obrazu
            File selectedFile = controller.getSelectedImageFile();
            if (selectedFile != null) {
                this.chosenImageFile = selectedFile; // Przypisz wybrany plik do zmiennej
                updateImageTitleFlow(selectedFile.getName()); // Wyświetl nazwę pliku w TextFlow
                System.out.println("Selected image: " + selectedFile.getAbsolutePath());
            } else {
                System.out.println("No image selected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateImageTitleFlow(String fileName) {
        Text text = new Text(fileName);
        imageTitleFlow.getChildren().clear();
        imageTitleFlow.getChildren().add(text);
    }

    @FXML
    private void handleAdd() {
        String categoryName = getTextFromTextFlow(categoryNameFlow);
        if (categoryName != null && !categoryName.isEmpty()) {
            String imagePath = null;
            if (chosenImageFile != null) {
                try {
                    Path destDir = Paths.get("Session/UserImages");
                    Files.createDirectories(destDir);
                    Path destPath = destDir.resolve(chosenImageFile.getName());
                    Files.copy(chosenImageFile.toPath(), destPath);
                    imagePath = destPath.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            CategoryClass newCategory = new CategoryClass(categoryName, parentCategory, imagePath);
            parentCategory.addSubcategory(newCategory);
            if (this.dialogStage != null) {
                this.dialogStage.close(); // Zamknij okno dialogowe
            }
        }
    }

    private String getTextFromTextFlow(TextFlow textFlow) {
        StringBuilder sb = new StringBuilder();
        for (javafx.scene.Node node : textFlow.getChildren()) {
            if (node instanceof Text) {
                sb.append(((Text) node).getText());
            }
        }
        return sb.toString();
    }
}
