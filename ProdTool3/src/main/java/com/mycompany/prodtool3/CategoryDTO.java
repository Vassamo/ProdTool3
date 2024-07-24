package com.mycompany.prodtool3;

import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {
    private String name;
    private List<ProductDTO> products;
    private List<CategoryDTO> subcategories;

    public CategoryDTO(String name) {
        this.name = name;
        this.products = new ArrayList<>();
        this.subcategories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public List<CategoryDTO> getSubcategories() {
        return subcategories;
    }
}

class ProductDTO {
    private String name;

    public ProductDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
