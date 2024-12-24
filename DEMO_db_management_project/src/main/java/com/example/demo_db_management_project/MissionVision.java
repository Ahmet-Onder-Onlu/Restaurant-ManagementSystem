package com.example.demo_db_management_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.*;

public class MissionVision {
    @FXML
    TextArea notions;
    @FXML
    Label id;
    // MSSQL bağlantı bilgileri
    String url = "jdbc:sqlserver://localhost:1433;databaseName=RestaurantManagement;encrypt=false;";
    String user = "sa";
    String password = "Password1";

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

    @FXML
    private void send() {
        String text = notions.getText(); // Kullanıcıdan alınan metin
        if (text == null || text.trim().isEmpty()) {
            System.out.println("Feedback mesajı boş olamaz!");
            id.setText("Message can't be empty");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Son eklenen CustomerID'yi al
            String query = "SELECT TOP 1 CustomerID FROM Customers ORDER BY CustomerID DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int customerID = rs.getInt("CustomerID"); // Son eklenen CustomerID

                // Feedback tablosuna yeni kayıt ekle
                String insertQuery = "INSERT INTO Feedback (CustomerID, feedback_message) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, customerID);
                insertStmt.setString(2, text);

                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Feedback başarıyla kaydedildi!");
                    id.setText("Successfull");
                }
            } else {
                System.out.println("Customer tablosunda kayıt bulunamadı.");
                id.setText("Not Found Customer");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
