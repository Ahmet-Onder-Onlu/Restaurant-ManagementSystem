package com.example.demo_db_management_project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelloController {
    @FXML
    TextField Name;
    @FXML
    TextField Phone;
    @FXML
    TextField Mail;

    @FXML
    protected void login() {
        // Kullanıcıdan alınan bilgiler
        String name = Name.getText();
        String phone = Phone.getText();
        String mail = Mail.getText();

        // MSSQL bağlantı bilgileri
        String url = "jdbc:sqlserver://localhost:1433;databaseName=RestaurantManagement;encrypt=false;";
        String user = "sa";
        String password = "Password1";

        // Veri tabanı bağlantısı ve veri ekleme işlemi
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // MSSQL veri tabanına bağlantı kur
            connection = DriverManager.getConnection(url, user, password);

            // SQL INSERT sorgusunu hazırla
            String sql = "INSERT INTO Customers (Name, Phone, Email) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            // Parametreleri yerine koy
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, mail);

            // Sorguyu çalıştır
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Kayıt başarıyla eklendi!");
            }
        } catch (SQLException e) {
            // Hata durumunda çıktıyı göster
            e.printStackTrace();
        } finally {
            // Kaynakları kapat
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            switchToMenu();
        }
    }


    private void switchToMenu() {
        try {
            // Menu.fxml dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            Parent root = loader.load();

            // Mevcut pencereyi al
            Stage stage = (Stage) Name.getScene().getWindow();

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
