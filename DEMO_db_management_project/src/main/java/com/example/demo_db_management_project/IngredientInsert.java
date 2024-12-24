package com.example.demo_db_management_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IngredientInsert {
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
}
