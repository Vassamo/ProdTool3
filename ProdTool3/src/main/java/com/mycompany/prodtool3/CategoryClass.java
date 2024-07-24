package com.mycompany.prodtool3;

import java.util.ArrayList;
import java.util.List;

public class CategoryClass {
    private String name;
    private List<Product> products;
    private List<CategoryClass> subcategories;
    private CategoryClass parentCategory;
    private String imagePath; // Ścieżka do obrazu

    public CategoryClass(String name) {
        this(name, null, null);
    }

    public CategoryClass(String name, CategoryClass parentCategory) {
        this(name, parentCategory, null);
    }

    public CategoryClass(String name, CategoryClass parentCategory, String imagePath) {
        System.out.println("category created " + name);
        this.name = name;
        this.products = new ArrayList<>();
        this.subcategories = new ArrayList<>();
        this.parentCategory = parentCategory;
        this.imagePath = imagePath;
    }

    // Gettery i settery
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Product> getProducts() { return products; }
    public void addProduct(Product product) { products.add(product); }
    public List<CategoryClass> getSubcategories() { return subcategories; }
    public void addSubcategory(CategoryClass subcategory) {
        subcategory.setParentCategory(this);
        subcategories.add(subcategory);
    }
    public CategoryClass getParentCategory() { return parentCategory; }
    public void setParentCategory(CategoryClass parentCategory) { this.parentCategory = parentCategory; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public List<String> getProductsNames() {
        List<String> productNames = new ArrayList<>();
        for (Product product : products) {
            productNames.add(product.getName());
        }
        return productNames;
    }

    // Metody konwersji
    public CategoryDTO toCategoryDTO() {
        CategoryDTO dto = new CategoryDTO(name);
        for (Product product : products) {
            dto.getProducts().add(new ProductDTO(product.getName()));
        }
        for (CategoryClass subcategory : subcategories) {
            dto.getSubcategories().add(subcategory.toCategoryDTO());
        }
        return dto;
    }

    public static CategoryClass fromCategoryDTO(CategoryDTO dto) {
        CategoryClass category = new CategoryClass(dto.getName());
        for (ProductDTO productDTO : dto.getProducts()) {
            category.addProduct(new Product(productDTO.getName()));
        }
        for (CategoryDTO subcategoryDTO : dto.getSubcategories()) {
            category.addSubcategory(fromCategoryDTO(subcategoryDTO));
        }
        return category;
    }
}
