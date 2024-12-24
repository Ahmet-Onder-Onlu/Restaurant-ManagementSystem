package com.example.demo_db_management_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StockInsert {

    @FXML
    private TableView<Ingredient> ingredientTableView;
    @FXML
    private TableColumn<Ingredient, String> ingredientNameColumn;
    @FXML
    private TableColumn<Ingredient, Double> quantityInStockColumn;
    @FXML
    TextField ingredientName, ingredientAmount;


    // MSSQL bağlantı bilgileri
    String url = "jdbc:sqlserver://localhost:1433;databaseName=RestaurantManagement;encrypt=false;";
    String user = "sa";
    String password = "Password1";

    public void initialize() {
        loadDataFromDatabase();
        // Column'ların PropertyValueFactory ile bağlanması
        ingredientNameColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        quantityInStockColumn.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
    }

    @FXML
    private void switchToHomePage(ActionEvent event) {
        try {
            // Menu.fxml dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            Parent root = loader.load();

            // Mevcut pencereyi al
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Yeni sahneyi ayarla
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Pencereyi güncelle
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromDatabase() {
        String query = "SELECT IngredientName, QuantityInStock FROM Stock AS S INNER JOIN Ingredients AS I ON I.IngredientID=S.IngredientID;";
        ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Veritabanı sonuçlarını ObservableList'e ekle
            while (resultSet.next()) {
                String ingredientName = resultSet.getString("IngredientName");
                double quantityInStock = resultSet.getDouble("QuantityInStock");
                Ingredient ingredient = new Ingredient(ingredientName, quantityInStock);
                ingredientList.add(ingredient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // TableView'a listeyi bağlama
        ingredientTableView.setItems(ingredientList);
    }

    @FXML
    private void insert() {
        String ingName = ingredientName.getText(); // Kullanıcıdan alınan ingredient adı
        String ingAmountStr = ingredientAmount.getText(); // Kullanıcıdan alınan miktar

        // Girdi kontrolü
        if (ingName == null || ingName.trim().isEmpty() || ingAmountStr == null || ingAmountStr.trim().isEmpty()) {
            System.out.println("Ingredient adı veya miktarı boş olamaz!");
            return;
        }

        int ingAmount;
        try {
            ingAmount = Integer.parseInt(ingAmountStr); // Miktarı integer'a çevir
        } catch (NumberFormatException e) {
            System.out.println("Miktar geçerli bir sayı olmalıdır!");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // IngredientID'yi kontrol et ve al
            String findIngredientQuery = "SELECT IngredientID FROM Ingredients WHERE IngredientName = ?";
            PreparedStatement findIngredientStmt = conn.prepareStatement(findIngredientQuery);
            findIngredientStmt.setString(1, ingName);
            ResultSet rs = findIngredientStmt.executeQuery();

            if (rs.next()) {
                // IngredientID bulundu
                int ingredientID = rs.getInt("IngredientID");

                // Stock tablosunda ilgili IngredientID için miktarı güncelle
                String updateStockQuery = "UPDATE Stock SET QuantityInStock = QuantityInStock + ? WHERE IngredientID = ?";
                PreparedStatement updateStockStmt = conn.prepareStatement(updateStockQuery);
                updateStockStmt.setInt(1, ingAmount);
                updateStockStmt.setInt(2, ingredientID);

                int rowsUpdated = updateStockStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Miktar başarıyla güncellendi!");
                    loadDataFromDatabase();
                } else {
                    System.out.println("Stock tablosunda bu IngredientID bulunamadı!");
                }
            } else {
                // IngredientName bulunamadı, yeni kayıt ekle
                String insertIngredientQuery = "INSERT INTO Ingredients (IngredientName) OUTPUT INSERTED.IngredientID VALUES (?)";
                PreparedStatement insertIngredientStmt = conn.prepareStatement(insertIngredientQuery);
                insertIngredientStmt.setString(1, ingName);
                ResultSet insertRs = insertIngredientStmt.executeQuery();

                if (insertRs.next()) {
                    int newIngredientID = insertRs.getInt("IngredientID");

                    // Stock tablosuna yeni kayıt ekle
                    String insertStockQuery = "INSERT INTO Stock (IngredientID, QuantityInStock) VALUES (?, ?)";
                    PreparedStatement insertStockStmt = conn.prepareStatement(insertStockQuery);
                    insertStockStmt.setInt(1, newIngredientID);
                    insertStockStmt.setInt(2, ingAmount);

                    int rowsInserted = insertStockStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Yeni malzeme ve stok başarıyla eklendi!");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
