/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prodtool3;

public class Product {
    private String name;
    private CategoryClass parentCategory;
    
    public Product(String name) {
        System.out.println("Created product: " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
      public CategoryClass getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(CategoryClass parentCategory) {
        this.parentCategory = parentCategory;
    }
}
