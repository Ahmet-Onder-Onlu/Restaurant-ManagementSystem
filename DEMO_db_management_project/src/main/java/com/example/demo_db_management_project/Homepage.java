package com.example.demo_db_management_project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Homepage {


    @FXML
    private void switchToGiveOrder(ActionEvent event) {
        try {
            // Menu.fxml dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("give-order.fxml"));
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
    private void switchToIngredientsInsert(ActionEvent event) {
        try {
            // Menu.fxml dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ingredient-insert.fxml"));
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
    private void switchToStockInsert(ActionEvent event) {
        try {
            // Menu.fxml dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("stock-insert.fxml"));
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
    private void switchToMissionVision(ActionEvent event) {
        try {
            // Menu.fxml dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mission-vision.fxml"));
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
}
