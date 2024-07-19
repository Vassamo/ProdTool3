package com.mycompany.prodtool3;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.collections.FXCollections;
import javafx.scene.SnapshotParameters;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
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
    private final int IconsSize = 32;

    public void initialize() {
        // Load the image from the resources directory
        ImageResizer resizer = new ImageResizer();
        Image resizedIcon = resizer.resizeImage(getClass().getResource("/DeafultImages/addFolder.png").toString(), IconsSize, IconsSize);
        ImageView ResizedImg = new ImageView(resizedIcon);
        addCategoryButton.setGraphic(ResizedImg);  
        resizedIcon = resizer.resizeImage(getClass().getResource("/DeafultImages/addFile.png").toString(), IconsSize, IconsSize);
        ResizedImg = new ImageView(resizedIcon);
        addProductButton.setGraphic(ResizedImg);

// Initialize data and set up the TreeView
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
                    System.out.println("Switched to category: " + selectedCategory.getName()); // Log category switch
                    updateProductList(selectedCategory);
                    updateMainGrid();
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
        // Refresh view after adding a new category
        updateTreeView();
        updateMainGrid();
    }

    @FXML
    private void handleAddProductButtonClick() {
        CategoryClass selectedCategory = currentCategory;
        if (selectedCategory != null) {
            ControllerAddProductDialog dialog = new ControllerAddProductDialog(selectedCategory);
            dialog.showAndWait();
            // Refresh view after adding a new product
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

        int columnIndex = 0;

        if (currentCategory.getParentCategory() != null) {
            Button backButton = new Button("Back");
            backButton.setMaxWidth(Double.MAX_VALUE);
            backButton.setOnAction(e -> {
                currentCategory = currentCategory.getParentCategory();
                System.out.println("Switched to category: " + currentCategory.getName()); // Log category switch
                updateMainGrid();
                updateProductList(currentCategory);
            });
            mainGrid.add(backButton, columnIndex++, 0);
        }

        List<CategoryClass> subcategories = currentCategory.getSubcategories();
        for (CategoryClass subcategory : subcategories) {
            Button button = new Button(subcategory.getName());
            button.setMaxWidth(Double.MAX_VALUE);
            button.setOnAction(e -> {
                currentCategory = subcategory;
                System.out.println("Switched to category: " + subcategory.getName()); // Log category switch
                updateMainGrid();
                updateProductList(currentCategory);
            });
            mainGrid.add(button, columnIndex++, 0);
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
        item.setExpanded(true); // Optionally expand items on creation
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
    
   public class ImageResizer {

    /**
     * Resizes an image to the specified width and height.
     *
     * @param imagePath the path to the image file
     * @param width the desired width
     * @param height the desired height
     * @return the resized Image
     */
    public Image resizeImage(String imagePath, double width, double height) {
        // Load the image
        Image originalImage = new Image(imagePath);

        // Create an ImageView for resizing
        ImageView imageView = new ImageView(originalImage);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        // Prepare snapshot parameters to preserve transparency
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        // Create a writable image with the desired size
        WritableImage resizedImage = new WritableImage((int) width, (int) height);

        // Capture the snapshot of the resized image view
        imageView.snapshot(params, resizedImage);

        return resizedImage;
    }
}
}
