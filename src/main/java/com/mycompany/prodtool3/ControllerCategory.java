package com.mycompany.prodtool3;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.collections.FXCollections;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControllerCategory {
    @FXML   
    private TreeView<String> categoryTreeView;
    @FXML
    private ListView<String> productListView;
    @FXML
    private GridPane mainGrid;
    @FXML
    private Button addCategoryButton;
    @FXML
    private Button addProductButton;

    private CategoryClass rootCategory;
    private CategoryClass currentCategory;

    public void initialize() {
        // Inicjalizacja danych i ustawianie TreeView
        rootCategory = createSampleData();
        currentCategory = rootCategory;
        updateTreeView();
        updateMainGrid();
        updateProductList(currentCategory);
        
        categoryTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedCategoryName = newValue.getValue();
                CategoryClass selectedCategory = findCategoryByName(rootCategory, selectedCategoryName);
                if (selectedCategory != null) {
                    currentCategory = selectedCategory;
                    updateProductList(selectedCategory);
                }
            }
        });
    }

    @FXML
    private void handleAddCategoryButtonClick() {
        CategoryClass selectedCategory = currentCategory;
        Stage stage = (Stage) addCategoryButton.getScene().getWindow();
        ControllerAddCategoryDialog dialog = new ControllerAddCategoryDialog(stage, selectedCategory);
        dialog.showAndWait();
        // Po dodaniu nowej kategorii odśwież widok
        updateTreeView();
        updateMainGrid();
    }

    @FXML
    private void handleAddProductButtonClick() {
        CategoryClass selectedCategory = currentCategory;
        if (selectedCategory != null) {
            ControllerAddProductDialog dialog = new ControllerAddProductDialog(selectedCategory);
            dialog.showAndWait();
            // Po dodaniu nowego produktu odśwież widok
            updateProductList(selectedCategory);
        }
    }

    private void updateTreeView() {
        TreeItem<String> rootItem = createTreeItem(rootCategory);
        categoryTreeView.setRoot(rootItem);
        categoryTreeView.setShowRoot(true);
    }

    private void updateProductList(CategoryClass selectedCategory) {
        if (selectedCategory != null) {
            List<String> productNames = selectedCategory.getProductsNames();
            productListView.setItems(FXCollections.observableArrayList(productNames));
        } else {
            productListView.setItems(FXCollections.observableArrayList());
        }
    }

    private void updateMainGrid() {
        mainGrid.getChildren().clear();

        Button backButton = new Button("Back");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setOnAction(e -> {
            if (currentCategory.getParentCategory() != null) {
                currentCategory = currentCategory.getParentCategory();
                updateMainGrid();
                updateProductList(currentCategory);
            }
        });
        mainGrid.add(backButton, 0, 0);

        List<CategoryClass> subcategories = currentCategory.getSubcategories();
        for (int i = 0; i < subcategories.size(); i++) {
            CategoryClass subcategory = subcategories.get(i);
            Button button = new Button(subcategory.getName());
            button.setMaxWidth(Double.MAX_VALUE);
            button.setOnAction(e -> {
                currentCategory = subcategory;
                updateMainGrid();
                updateProductList(currentCategory);
            });
            mainGrid.add(button, i + 1, 0);
        }
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
        item.setExpanded(true); // Opcjonalnie, aby rozwijać elementy po dodaniu
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
