package com.mycompany.prodtool3;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class ControllerCategory {
    @FXML   
    private TreeView<String> categoryTreeView;
    @FXML
    private ListView<String> productListView;
    @FXML
    private Button addCategoryButton;
    @FXML
    private Button addProductButton;
    @FXML
    private GridPane mainGrid;

    private CategoryClass rootCategory;
    private CategoryClass currentCategory;

    public void initialize() {
        // Inicjalizacja danych i ustawienie TreeView
        rootCategory = createSampleData();
        currentCategory = rootCategory;
        TreeItem<String> rootItem = createTreeItem(rootCategory);
        categoryTreeView.setRoot(rootItem);
        categoryTreeView.setShowRoot(true);
        updateMainGrid();

        categoryTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedCategoryName = newValue.getValue();
                CategoryClass selectedCategory = findCategoryByName(rootCategory, selectedCategoryName);
                if (selectedCategory != null) {
                    // Ustawienie nazw produktów w ListView
                    List<String> productNames = selectedCategory.getProductsNames();
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
            updateMainGrid();
        }
    }

    @FXML
    private void handleAddProductButtonClick() {
        CategoryClass selectedCategory = getSelectedCategory();
        if (selectedCategory != null) {
            ControllerAddProductDialog dialog = new ControllerAddProductDialog(getStage(), selectedCategory);
            dialog.show();
            refreshProductList(selectedCategory);
        }
    }

    private void refreshTreeView() {
        TreeItem<String> rootItem = createTreeItem(rootCategory);
        categoryTreeView.setRoot(rootItem);
        categoryTreeView.setShowRoot(true);
    }

    private void refreshProductList(CategoryClass category) {
        List<String> productNames = category.getProductsNames();
        productListView.setItems(FXCollections.observableArrayList(productNames));
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

private void updateMainGrid() {
    mainGrid.getChildren().clear();
    List<CategoryClass> subcategories = currentCategory.getSubcategories();
    int numCols = 3; // Ustaw liczbę kolumn według potrzeb
    for (int i = 0; i < subcategories.size(); i++) {
        CategoryClass subcategory = subcategories.get(i);
        Button subcategoryButton = new Button(subcategory.getName());
        subcategoryButton.setMaxWidth(Double.MAX_VALUE); // Przyciski zajmują całą dostępną szerokość
        subcategoryButton.setMaxHeight(Double.MAX_VALUE); // Przyciski zajmują całą dostępną wysokość
        subcategoryButton.setOnAction(e -> {
            currentCategory = subcategory;
            updateMainGrid();
            refreshProductList(currentCategory);
        });
        mainGrid.add(subcategoryButton, i % numCols, i / numCols);
    }

    for (int col = 0; col < numCols; col++) {
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setHgrow(Priority.ALWAYS);
        mainGrid.getColumnConstraints().add(colConstraints);
    }

    for (int row = 0; row < (subcategories.size() / numCols) + 1; row++) {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        mainGrid.getRowConstraints().add(rowConstraints);
    }
}

}
