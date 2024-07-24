package com.mycompany.prodtool3;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import javafx.scene.Parent;
import javafx.stage.Modality;

public class ControllerCategory {

    @FXML
    private TreeView<String> categoryTreeView;
    @FXML
    private ListView<String> productListView;
    @FXML
    private TilePane tileGrid;
    @FXML
    private Button addCategoryButton;
    @FXML
    private Button addProductButton;
    @FXML
    private Button exportButton;
    @FXML
    private Button importButton;

    private CategoryClass rootCategory;
    private CategoryClass currentCategory;

    private static final int ICON_SIZE_DEFAULT = 48;
    private static final int ICON_SIZE_ACTION_BUTTONS = 32;

    public void initialize() {
        initializeIcons();
        rootCategory = createSampleData();
        currentCategory = rootCategory;
        updateTreeView();
        updateTileGrid();
        updateProductList(currentCategory);

        categoryTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                CategoryClass selectedCategory = findCategoryByName(rootCategory, newValue.getValue());
                if (selectedCategory != null) {
                    currentCategory = selectedCategory;
                    System.out.println("Switched to category: " + selectedCategory.getName());
                    updateProductList(selectedCategory);
                    updateTileGrid();
                }
            }
        });
    }

    private void initializeIcons() {
        ImageResizer resizer = new ImageResizer();
        addCategoryButton.setGraphic(createIcon(resizer, "/DeafultImages/addFolder.png", ICON_SIZE_ACTION_BUTTONS));
        addProductButton.setGraphic(createIcon(resizer, "/DeafultImages/addFile.png", ICON_SIZE_ACTION_BUTTONS));
        exportButton.setGraphic(createIcon(resizer, "/DeafultImages/export.png", ICON_SIZE_ACTION_BUTTONS));
        importButton.setGraphic(createIcon(resizer, "/DeafultImages/import.png", ICON_SIZE_ACTION_BUTTONS));
    }

    private ImageView createIcon(ImageResizer resizer, String path, int size) {
        Image resizedIcon = resizer.resizeImage(getClass().getResource(path).toString(), size, size);
        return new ImageView(resizedIcon);
    }

@FXML
private void handleAddCategoryButtonClick() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/prodtool3/AddCategoryDialog.fxml"));
        Parent root = loader.load();

        ControllerAddCategoryDialog controller = loader.getController();
        controller.setParentCategory(currentCategory);

        Stage dialogStage = new Stage();
        dialogStage.initOwner(addCategoryButton.getScene().getWindow());
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(new Scene(root));
        controller.setStage(dialogStage);

        dialogStage.showAndWait();

        updateTreeView();
        updateTileGrid();
    } catch (IOException e) {
        e.printStackTrace();
    }
}




    @FXML
    private void handleAddProductButtonClick() {
        if (currentCategory != null) {
            ControllerAddProductDialog dialog = new ControllerAddProductDialog(currentCategory);
            dialog.showAndWait();
            updateProductList(currentCategory);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Categories");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

        if (file != null) {
            try (Writer writer = new FileWriter(file)) {
                Gson gson = new Gson();
                CategoryDTO rootCategoryDTO = rootCategory.toCategoryDTO();
                gson.toJson(rootCategoryDTO, writer);
                showAlert("Export Successful", "The categories have been successfully exported.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Export Error", "An error occurred while exporting categories.");
            }
        }
    }

    @FXML
    private void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Categories");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(importButton.getScene().getWindow());

        if (file != null) {
            try (Reader reader = new FileReader(file)) {
                Gson gson = new Gson();
                CategoryDTO rootCategoryDTO = gson.fromJson(reader, CategoryDTO.class);
                rootCategory = CategoryClass.fromCategoryDTO(rootCategoryDTO);
                currentCategory = rootCategory;
                updateTreeView();
                updateTileGrid();
                updateProductList(currentCategory);
                showAlert("Import Successful", "The categories have been successfully imported.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Import Error", "An error occurred while importing categories.");
            }
        }
    }

    private void updateTreeView() {
        categoryTreeView.setRoot(createTreeItem(rootCategory));
        categoryTreeView.setShowRoot(true);
    }

    private void updateProductList(CategoryClass selectedCategory) {
        List<String> productNames = selectedCategory != null ? selectedCategory.getProductsNames() : Arrays.asList();
        productListView.setItems(FXCollections.observableArrayList(productNames));
    }


    private void updateTileGrid() {
        tileGrid.getChildren().clear();

        if (currentCategory.getParentCategory() != null) {
            Button backButton = createCategoryButton("Back", currentCategory.getParentCategory(), "/DeafultImages/back.png", ICON_SIZE_DEFAULT);
            tileGrid.getChildren().add(backButton);
        }

        for (CategoryClass subcategory : currentCategory.getSubcategories()) {
            String imagePath = subcategory.getImagePath() != null ? subcategory.getImagePath() : "/DeafultImages/folder.png";
            Button button = createCategoryButton(subcategory.getName(), subcategory, imagePath, ICON_SIZE_DEFAULT);
            tileGrid.getChildren().add(button);
        }
    }

    private Button createCategoryButton(String name, CategoryClass category, String imagePath, int iconSize) {
        Button button = new Button(name);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setContentDisplay(ContentDisplay.TOP); // Ensure the image is below the text

        // Set the image
        ImageResizer resizer = new ImageResizer();
        Image resizedIcon;
        if (new File(imagePath).exists()) {
            resizedIcon = resizer.resizeImage("file:" + imagePath, iconSize, iconSize);
        } else {
            resizedIcon = resizer.resizeImage(getClass().getResource(imagePath).toString(), iconSize, iconSize);
        }
        ImageView iconView = new ImageView(resizedIcon);
        button.setGraphic(iconView);

        button.setOnAction(e -> {
            currentCategory = category;
            System.out.println("Switched to category: " + category.getName());
            updateTileGrid();
            updateProductList(currentCategory);
        });

        return button;
    }

    private CategoryClass findCategoryByName(CategoryClass currentCategory, String categoryName) {
        if (currentCategory.getName().equals(categoryName)) {
            return currentCategory;
        }
        for (CategoryClass subcategory : currentCategory.getSubcategories()) {
            CategoryClass foundCategory = findCategoryByName(subcategory, categoryName);
            if (foundCategory != null) {
                return foundCategory;
            }
        }
        return null;
    }

    private TreeItem<String> createTreeItem(CategoryClass category) {
        TreeItem<String> item = new TreeItem<>(category.getName());
        item.setExpanded(true);
        for (CategoryClass subcategory : category.getSubcategories()) {
            item.getChildren().add(createTreeItem(subcategory));
        }
        return item;
    }

    private CategoryClass createSampleData() {
        CategoryClass electronics = new CategoryClass("Electronics");
        CategoryClass laptops = new CategoryClass("Laptops", electronics);
        CategoryClass smartphones = new CategoryClass("Smartphones", electronics);
        CategoryClass lenovo = new CategoryClass("lenovo", laptops);
        CategoryClass kiano = new CategoryClass("kiano", laptops);

        electronics.addSubcategory(laptops);
        electronics.addSubcategory(smartphones);
        laptops.addSubcategory(lenovo);
        laptops.addSubcategory(kiano);

        kiano.addProduct(new Product("Laptop B"));
        smartphones.addProduct(new Product("Smartphone A"));
        smartphones.addProduct(new Product("Smartphone B"));

        return electronics;
    }
}
