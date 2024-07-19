package com.mycompany.prodtool3;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

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

    private CategoryClass rootCategory;
    private CategoryClass currentCategory;

    private static final int ICON_SIZE = 32;

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
        addCategoryButton.setGraphic(createIcon(resizer, "/DeafultImages/addFolder.png"));
        addProductButton.setGraphic(createIcon(resizer, "/DeafultImages/addFile.png"));
    }

    private ImageView createIcon(ImageResizer resizer, String path) {
        Image resizedIcon = resizer.resizeImage(getClass().getResource(path).toString(), ICON_SIZE, ICON_SIZE);
        return new ImageView(resizedIcon);
    }

    @FXML
    private void handleAddCategoryButtonClick() {
        Stage stage = (Stage) addCategoryButton.getScene().getWindow();
        ControllerAddCategoryDialog dialog = new ControllerAddCategoryDialog(stage, currentCategory);
        dialog.showAndWait();
        updateTreeView();
        updateTileGrid();
    }

    @FXML
    private void handleAddProductButtonClick() {
        if (currentCategory != null) {
            ControllerAddProductDialog dialog = new ControllerAddProductDialog(currentCategory);
            dialog.showAndWait();
            updateProductList(currentCategory);
        }
    }

    private void updateTreeView() {
        categoryTreeView.setRoot(createTreeItem(rootCategory));
        categoryTreeView.setShowRoot(true);
    }

    private void updateProductList(CategoryClass selectedCategory) {
        List<String> productNames = selectedCategory != null ? selectedCategory.getProductsNames() : List.of();
        productListView.setItems(FXCollections.observableArrayList(productNames));
    }

    private void updateTileGrid() {
        tileGrid.getChildren().clear();

        if (currentCategory.getParentCategory() != null) {
            Button backButton = createCategoryButton("Back", currentCategory.getParentCategory());
            tileGrid.getChildren().add(backButton);
        }

        for (CategoryClass subcategory : currentCategory.getSubcategories()) {
            Button button = createCategoryButton(subcategory.getName(), subcategory);
            tileGrid.getChildren().add(button);
        }
    }

    private Button createCategoryButton(String name, CategoryClass category) {
        Button button = new Button(name);
        button.setMaxWidth(Double.MAX_VALUE);
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
