package com.mycompany.prodtool3;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerAddCategoryDialog {

    private Stage stage;
    private TextField categoryNameField;
    private Button addButton;
    private Button cancelButton;
    private CategoryClass parentCategory;

    public ControllerAddCategoryDialog(Stage ownerStage, CategoryClass parentCategory) {
        this.parentCategory = parentCategory;
        stage = new Stage();
        stage.initOwner(ownerStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label("Category Name:");
        categoryNameField = new TextField();

        addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String categoryName = categoryNameField.getText();
            if (categoryName != null && !categoryName.isEmpty()) {
                CategoryClass newCategory = new CategoryClass(categoryName);
                parentCategory.addSubcategory(newCategory);
            }
            stage.close();
        });

        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            stage.close();
        });

        HBox buttonBox = new HBox(10, addButton, cancelButton);
        VBox vbox = new VBox(10, label, categoryNameField, buttonBox);
        Scene scene = new Scene(vbox, 300, 150);
        stage.setScene(scene);
    }

    public void showAndWait() {
        stage.showAndWait();
    }
}
