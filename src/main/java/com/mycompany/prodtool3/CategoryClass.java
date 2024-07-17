package com.mycompany.prodtool3;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class CategoryClass {
    private String name;
    private List<Product> products;
    private List<CategoryClass> subcategories;
    private CategoryClass parentCategory;

    public CategoryClass(String name) {
        System.out.println("category created " + name);
        this.name = name;
        this.products = new ArrayList<>();
        this.subcategories = new ArrayList<>();
        this.parentCategory = null;
    }

    public CategoryClass(String name, CategoryClass parentCategory) {
        this(name);
        this.parentCategory = parentCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<CategoryClass> getSubcategories() {
        return subcategories;
    }

    public void addSubcategory(CategoryClass subcategory) {
        subcategory.setParentCategory(this);
        subcategories.add(subcategory);
    }

    public CategoryClass getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(CategoryClass parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<CategoryClass> getPath() {
        List<CategoryClass> path = new ArrayList<>();
        CategoryClass current = this;
        while (current != null) {
            path.add(0, current);
            current = current.getParentCategory();
        }
        return path;
    }
    
    public List<String> getProductsNames() {
        List<String> productNames = new ArrayList<>();
        for (Product product : products) {
            productNames.add(product.getName());
        }
        return productNames;
    }
       
    
    @FXML
    private Button button;
    @FXML
    private GridPane mainGrid;

    @FXML
    private void handleButtonClick() {
        try {
            System.out.println("add category clicked"); 
            //App.setRoot("CategoryClass");
                    handleAddButtonClick();
            ControllerAddCategoryDialog dialog = new ControllerAddCategoryDialog(getStage(), this);
            dialog.show();          
            //com.mycompany.prodtool3.App.setRoot("CategoryClass");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error!"); 
        }
    }
    
    @FXML
    private void handleAdminRequest(){                  //AdminToImplement do przycisku
        App.getInstance().AdminRequest("testString");
    }
    

     private int buttonCount = 0;
     
    private int currentRow = 0;
    private int currentColumn = 0;
    private final int maxColumns = 3; // Maksymalna liczba kolumn
     
    @FXML
     public void handleAddButtonClick() {
        Button newButton = new Button("Button " + (++buttonCount));
        newButton.setMaxWidth(Double.MAX_VALUE);
        newButton.setMaxHeight(Double.MAX_VALUE);

        // Dodanie przycisku do GridPane
        mainGrid.add(newButton, currentColumn, currentRow);

        // Rozciąganie przycisku na całą komórkę
        GridPane.setFillWidth(newButton, true);
        GridPane.setFillHeight(newButton, true);

        // Aktualizacja kolumny i wiersza
        currentColumn++;
        if (currentColumn >= maxColumns) {
            currentColumn = 0;
            currentRow++;
            addNewRowConstraints();
        }
     }
     
      private void addNewRowConstraints() {
        RowConstraints rowConst = new RowConstraints();
        rowConst.setPercentHeight(100.0 / (currentRow + 1));
        mainGrid.getRowConstraints().add(rowConst);

        // Aktualizacja istniejących RowConstraints, aby podzielić przestrzeń równomiernie
        for (RowConstraints rc : mainGrid.getRowConstraints()) {
            rc.setPercentHeight(100.0 / (currentRow + 1));
        }
    }
    
        private Stage getStage() {
        return (Stage) button.getScene().getWindow();
    }
    
    
}
