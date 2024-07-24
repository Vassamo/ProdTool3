package com.mycompany.prodtool3;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerAddProductDialog {
    private Stage stage;
    private TextField productNameField;
    private Button addButton;
    private Button cancelButton;
    
    public ControllerAddProductDialog(CategoryClass category) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label("Product Name:");
        productNameField = new TextField();
        
        addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String productName = productNameField.getText();
            if (!productName.isEmpty()) {
                category.addProduct(new Product(productName));
                stage.close();
            }
        });

        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> stage.close());

        VBox vbox = new VBox(10, label, productNameField, addButton, cancelButton);
        Scene scene = new Scene(vbox, 300, 150);
        stage.setScene(scene);
    }

    public void showAndWait() {
        stage.showAndWait();
    }

}

