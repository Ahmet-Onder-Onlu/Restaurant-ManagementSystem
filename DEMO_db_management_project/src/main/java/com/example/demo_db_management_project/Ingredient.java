package com.example.demo_db_management_project;

public class Ingredient {
    private String ingredientName;
    private double quantityInStock;

    // Constructor
    public Ingredient(String ingredientName, double quantityInStock) {
        this.ingredientName = ingredientName;
        this.quantityInStock = quantityInStock;
    }

    // Getter ve Setter metodları
    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public double getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}

