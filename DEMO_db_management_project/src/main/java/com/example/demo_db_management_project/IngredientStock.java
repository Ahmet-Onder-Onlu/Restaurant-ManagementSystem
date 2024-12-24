package com.example.demo_db_management_project;

public class IngredientStock {
    private int ingredientID;
    private double quantityInStock;

    // Constructor
    public IngredientStock(int ingredientID, double quantityInStock) {
        this.ingredientID = ingredientID;
        this.quantityInStock = quantityInStock;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public double getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
