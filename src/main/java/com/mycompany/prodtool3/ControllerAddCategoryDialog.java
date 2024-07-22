package com.mycompany.prodtool3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ControllerAddCategoryDialog {

    @FXML
    private TextField categoryNameField;

    @FXML
    private Button chooseImageButton;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    private Stage stage;
    private CategoryClass parentCategory;
    private File chosenImageFile;

    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        chosenImageFile = fileChooser.showOpenDialog(stage);
    }

    @FXML
    private void handleAdd() {
        String categoryName = categoryNameField.getText();
        if (categoryName != null && !categoryName.isEmpty()) {
            String imagePath = null;
            if (chosenImageFile != null) {
                try {
                    Path destDir = Paths.get("UserImages");
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
        }
        stage.close();
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParentCategory(CategoryClass parentCategory) {
        this.parentCategory = parentCategory;
    }
}
