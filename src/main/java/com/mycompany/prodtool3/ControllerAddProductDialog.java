package com.mycompany.prodtool3;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerAddProductDialog {

    private Stage stage;
    private TextField productNameField;
    private Button addButton;
    private Button cancelButton;
    private CategoryClass parentCategory;

    public ControllerAddProductDialog(Stage ownerStage, CategoryClass parentCategory) {
        this.parentCategory = parentCategory;
        stage = new Stage();
        stage.initOwner(ownerStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label("Product Name:");
        productNameField = new TextField();

        addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String productName = productNameField.getText();
            if (productName != null && !productName.isEmpty()) {
                parentCategory.addProduct(new Product(productName));
            }
            stage.close();
        });

        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            stage.close();
        });

        HBox buttonBox = new HBox(10, addButton, cancelButton);
        VBox vbox = new VBox(10, label, productNameField, buttonBox);
        Scene scene = new Scene(vbox, 300, 150);
        stage.setScene(scene);
    }

    public void show() {
        stage.showAndWait();
    }
}
