package com.mycompany.prodtool3;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class ControllerCategory {
    @FXML   
    private TreeView<String> categoryTreeView;
    @FXML
    private ListView<String> productListView;
    @FXML
    private Button addCategoryButton;

    private CategoryClass rootCategory;

    public void initialize() {
        // Inicjalizacja danych i ustawienie TreeView
        rootCategory = createSampleData();
        TreeItem<String> rootItem = createTreeItem(rootCategory);
        categoryTreeView.setRoot(rootItem);
        categoryTreeView.setShowRoot(true);

        categoryTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedCategoryName = newValue.getValue();
                CategoryClass selectedCategory = findCategoryByName(rootCategory, selectedCategoryName);
                if (selectedCategory != null) {
                    // Ustawienie nazw produktów w ListView
                    List<String> productNames = selectedCategory.getProductsNames(); // Załóżmy, że getProductsNames() zwraca nazwy produktów
                    productListView.setItems(FXCollections.observableArrayList(productNames));
                }
            }
        });
    }

    @FXML
    private void handleAddCategoryButtonClick() {
        CategoryClass selectedCategory = getSelectedCategory();
        if (selectedCategory != null) {
            ControllerAddCategoryDialog dialog = new ControllerAddCategoryDialog(getStage(), selectedCategory);
            dialog.show();
            refreshTreeView();
        }
    }

    private void refreshTreeView() {
        TreeItem<String> rootItem = createTreeItem(rootCategory);
        categoryTreeView.setRoot(rootItem);
        categoryTreeView.setShowRoot(true);
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
        CategoryClass lenovo = new CategoryClass("Lenovo", laptops);
        CategoryClass kiano = new CategoryClass("Kiano", laptops);

        electronics.addSubcategory(laptops);
        electronics.addSubcategory(smartphones);
        laptops.addSubcategory(lenovo);
        laptops.addSubcategory(kiano);

        kiano.addProduct(new Product("Laptop B"));

        smartphones.addProduct(new Product("Smartphone A"));
        smartphones.addProduct(new Product("Smartphone B"));

        return electronics;
    }

    private CategoryClass getSelectedCategory() {
        TreeItem<String> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            return findCategoryByName(rootCategory, selectedItem.getValue());
        }
        return null;
    }

    private Stage getStage() {
        return (Stage) addCategoryButton.getScene().getWindow();
    }
}
