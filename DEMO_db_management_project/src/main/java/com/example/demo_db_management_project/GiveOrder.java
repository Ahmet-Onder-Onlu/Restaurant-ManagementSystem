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


public class GiveOrder {



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
    private CheckBox makaronCheckBox, lazanyaCheckBox, waffleCheckBox;
    @FXML
    private TextField makaronAmountField, lazanyaAmountField, waffleAmountField, totalPriceField;
    @FXML
    private Label CName_, makaron, lazanya, waffle;


    // MSSQL bağlantı bilgileri
    String url = "jdbc:sqlserver://localhost:1433;databaseName=RestaurantManagement;encrypt=false;";
    String user = "sa";
    String password = "Password1";

    // Aktif müşteri ID'si
    private int currentCustomerId;

    public void initialize() {
        // Sayfa yüklendiğinde toplam fiyatı sıfırla
        totalPriceField.setText("0");
        setCurrentCustomer();
    }

    // Aktif müşteriyi veritabanından çek
    public void setCurrentCustomer() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT TOP 1 CustomerID, Name FROM Customers ORDER BY CustomerID DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currentCustomerId = rs.getInt("CustomerID");
                System.out.println(currentCustomerId);
                CName_.setText(String.valueOf(currentCustomerId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            CName_.setText("ERROR");
        }
    }


    // Sipariş toplam fiyatını hesapla
    @FXML
    public void calculateTotalPrice() {
        int totalPrice = 0;

        if (makaronCheckBox.isSelected()) {
            int amount = Integer.parseInt(makaronAmountField.getText());
            totalPrice += amount * 300; // Makaron birim fiyatı: 300
        }
        if (lazanyaCheckBox.isSelected()) {
            int amount = Integer.parseInt(lazanyaAmountField.getText());
            totalPrice += amount * 200; // Lazanya birim fiyatı: 200
        }
        if (waffleCheckBox.isSelected()) {
            int amount = Integer.parseInt(waffleAmountField.getText());
            totalPrice += amount * 100; // Waffle birim fiyatı: 100
        }

        totalPriceField.setText(String.valueOf(totalPrice));
    }

    Boolean flagmakaron = false;
    Boolean flaglazanya = false;
    Boolean flagwaffle = false;
    @FXML
    public void processOrder() {
        if (makaronCheckBox.isSelected()) {
            int amount = Integer.parseInt(makaronAmountField.getText()); // Kullanıcının girdiği miktar
            int makaronID = 1; // Makaron yemeği için DishID
            String query = "SELECT IngredientID, QuantityNeeded FROM DishIngredients WHERE DishID = ?";

            try (Connection connection = DriverManager.getConnection(url, user, password);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                // İlk sorgu için DishID parametresini bağla
                statement.setInt(1, makaronID);
                ResultSet resultSet = statement.executeQuery();

                boolean flag = true; // Stok durumu kontrolü için
                List<IngredientStock> ingredientsToUpdate = new ArrayList<>(); // Stok güncellemeleri için liste

                while (resultSet.next()) {
                    int ingredientId = resultSet.getInt("IngredientID");
                    double quantityNeeded = resultSet.getDouble("QuantityNeeded") * amount;

                    // İkinci sorgu: Stoktaki miktarı kontrol et
                    String query2 = "SELECT QuantityInStock FROM Stock WHERE IngredientID = ?";
                    try (PreparedStatement statement2 = connection.prepareStatement(query2)) {
                        statement2.setInt(1, ingredientId);
                        ResultSet resultSet2 = statement2.executeQuery();

                        if (resultSet2.next()) {
                            double quantityInStock = resultSet2.getDouble("QuantityInStock");

                            // Stok yeterli mi?
                            if (quantityInStock < quantityNeeded) {
                                flag = false; // Yeterli değilse flag'i false yap
                                System.out.println("Yetersiz stok! IngredientID: " + ingredientId);
                                break; // Stok yetersizse döngüyü durdur
                            } else {
                                // Stok güncellemesi için listeye ekle
                                ingredientsToUpdate.add(new IngredientStock(ingredientId, quantityInStock - quantityNeeded));
                            }
                        } else {
                            flag = false; // Malzeme bulunamazsa da false yap
                            System.out.println("IngredientID " + ingredientId + " için stok bilgisi bulunamadı.");
                            break;
                        }
                    }
                }

                if (flag) {
                    // Stok yeterliyse güncelleme yap
                    String updateQuery = "UPDATE Stock SET QuantityInStock = ? WHERE IngredientID = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        for (IngredientStock ingredient : ingredientsToUpdate) {
                            updateStatement.setDouble(1, ingredient.getQuantityInStock());
                            updateStatement.setInt(2, ingredient.getIngredientID());
                            updateStatement.executeUpdate(); // Her stok için güncelleme yap
                        }
                    }
                    System.out.println("Yemek hazır!");
                    makaron.setText("Done!");
                } else {
                    System.out.println("Sipariş için yeterli stok yok!");
                    makaron.setText("No Stock");
                    flagmakaron = true;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        if (lazanyaCheckBox.isSelected()) {
            int amount = Integer.parseInt(lazanyaAmountField.getText()); // Kullanıcının girdiği miktar
            int lazanyaID = 2; // Makaron yemeği için DishID
            String query = "SELECT IngredientID, QuantityNeeded FROM DishIngredients WHERE DishID = ?";

            try (Connection connection = DriverManager.getConnection(url, user, password);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                // İlk sorgu için DishID parametresini bağla
                statement.setInt(1, lazanyaID);
                ResultSet resultSet = statement.executeQuery();

                boolean flag = true; // Stok durumu kontrolü için
                List<IngredientStock> ingredientsToUpdate = new ArrayList<>(); // Stok güncellemeleri için liste

                while (resultSet.next()) {
                    int ingredientId = resultSet.getInt("IngredientID");
                    double quantityNeeded = resultSet.getDouble("QuantityNeeded") * amount;

                    // İkinci sorgu: Stoktaki miktarı kontrol et
                    String query2 = "SELECT QuantityInStock FROM Stock WHERE IngredientID = ?";
                    try (PreparedStatement statement2 = connection.prepareStatement(query2)) {
                        statement2.setInt(1, ingredientId);
                        ResultSet resultSet2 = statement2.executeQuery();

                        if (resultSet2.next()) {
                            double quantityInStock = resultSet2.getDouble("QuantityInStock");

                            // Stok yeterli mi?
                            if (quantityInStock < quantityNeeded) {
                                flag = false; // Yeterli değilse flag'i false yap
                                System.out.println("Yetersiz stok! IngredientID: " + ingredientId);
                                break; // Stok yetersizse döngüyü durdur
                            } else {
                                // Stok güncellemesi için listeye ekle
                                ingredientsToUpdate.add(new IngredientStock(ingredientId, quantityInStock - quantityNeeded));
                            }
                        } else {
                            flag = false; // Malzeme bulunamazsa da false yap
                            System.out.println("IngredientID " + ingredientId + " için stok bilgisi bulunamadı.");
                            break;
                        }
                    }
                }

                if (flag) {
                    // Stok yeterliyse güncelleme yap
                    String updateQuery = "UPDATE Stock SET QuantityInStock = ? WHERE IngredientID = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        for (IngredientStock ingredient : ingredientsToUpdate) {
                            updateStatement.setDouble(1, ingredient.getQuantityInStock());
                            updateStatement.setInt(2, ingredient.getIngredientID());
                            updateStatement.executeUpdate(); // Her stok için güncelleme yap
                        }
                    }
                    System.out.println("Yemek hazır!");
                    lazanya.setText("Done!");

                } else {
                    System.out.println("Sipariş için yeterli stok yok!");
                    flaglazanya = true;
                    lazanya.setText("No Stock");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (waffleCheckBox.isSelected()) {
            int amount = Integer.parseInt(waffleAmountField.getText()); // Kullanıcının girdiği miktar
            int waffleID = 3; // Makaron yemeği için DishID
            String query = "SELECT IngredientID, QuantityNeeded FROM DishIngredients WHERE DishID = ?";

            try (Connection connection = DriverManager.getConnection(url, user, password);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                // İlk sorgu için DishID parametresini bağla
                statement.setInt(1, waffleID);
                ResultSet resultSet = statement.executeQuery();

                boolean flag = true; // Stok durumu kontrolü için
                List<IngredientStock> ingredientsToUpdate = new ArrayList<>(); // Stok güncellemeleri için liste

                while (resultSet.next()) {
                    int ingredientId = resultSet.getInt("IngredientID");
                    double quantityNeeded = resultSet.getDouble("QuantityNeeded") * amount;

                    // İkinci sorgu: Stoktaki miktarı kontrol et
                    String query2 = "SELECT QuantityInStock FROM Stock WHERE IngredientID = ?";
                    try (PreparedStatement statement2 = connection.prepareStatement(query2)) {
                        statement2.setInt(1, ingredientId);
                        ResultSet resultSet2 = statement2.executeQuery();

                        if (resultSet2.next()) {
                            double quantityInStock = resultSet2.getDouble("QuantityInStock");

                            // Stok yeterli mi?
                            if (quantityInStock < quantityNeeded) {
                                flag = false; // Yeterli değilse flag'i false yap
                                System.out.println("Yetersiz stok! IngredientID: " + ingredientId);
                                break; // Stok yetersizse döngüyü durdur
                            } else {
                                // Stok güncellemesi için listeye ekle
                                ingredientsToUpdate.add(new IngredientStock(ingredientId, quantityInStock - quantityNeeded));
                            }
                        } else {
                            flag = false; // Malzeme bulunamazsa da false yap
                            System.out.println("IngredientID " + ingredientId + " için stok bilgisi bulunamadı.");
                            break;
                        }
                    }
                }

                if (flag) {
                    // Stok yeterliyse güncelleme yap
                    String updateQuery = "UPDATE Stock SET QuantityInStock = ? WHERE IngredientID = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        for (IngredientStock ingredient : ingredientsToUpdate) {
                            updateStatement.setDouble(1, ingredient.getQuantityInStock());
                            updateStatement.setInt(2, ingredient.getIngredientID());
                            updateStatement.executeUpdate(); // Her stok için güncelleme yap
                        }
                    }
                    System.out.println("Yemek hazır!");
                    waffle.setText("Done!");
                } else {
                    System.out.println("Sipariş için yeterli stok yok!");
                    waffle.setText("No Stock");
                    flagwaffle = true;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Veritabanı bağlantısı kur
            connection = DriverManager.getConnection(url, user, password);

            // 1. En son CustomerID'yi al
            String queryCustomerID = "SELECT TOP 1 CustomerID FROM Customers ORDER BY CustomerID DESC;";
            preparedStatement = connection.prepareStatement(queryCustomerID);
            resultSet = preparedStatement.executeQuery();

            int customerID = 0;
            if (resultSet.next()) {
                customerID = resultSet.getInt("CustomerID");
            }

            // 2. Eğer customerID geçerli değilse, hata mesajı ver
            if (customerID == 0) {
                System.out.println("Geçerli bir müşteri bulunamadı!");
                return;
            }

            // 3. Orders tablosuna yeni bir sipariş ekle
            String queryInsertOrder = "INSERT INTO Orders (CustomerID, OrderDate, TotalAmount) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(queryInsertOrder);

            // Verileri belirle
            // 3. Sistemdeki güncel tarihi ve saati al
            LocalDateTime currentDateTime = LocalDateTime.now();
            // SQL formatına dönüştür
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String orderDate = currentDateTime.format(formatter); // SQL formatına uygun
            double totalAmount = Double.parseDouble(totalPriceField.getText()); // Toplam tutar

            preparedStatement.setInt(1, customerID);
            preparedStatement.setString(2, orderDate);
            preparedStatement.setDouble(3, totalAmount);

            // Siparişi ekle
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Sipariş başarıyla eklendi. CustomerID: " + customerID);
            } else {
                System.out.println("Sipariş eklenirken bir hata oluştu.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları kapat
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        if (makaronCheckBox.isSelected() && !flagmakaron) {
            int amount = Integer.parseInt(makaronAmountField.getText());
            int price = amount*300;

            try {
                // Veritabanı bağlantısı kur
                connection = DriverManager.getConnection(url, user, password);

                // 1. En son CustomerID'yi al
                String orderQueryID = "SELECT TOP 1 OrderID FROM Orders ORDER BY OrderID DESC ;";
                preparedStatement = connection.prepareStatement(orderQueryID);
                resultSet = preparedStatement.executeQuery();

                int orderID=0;
                if (resultSet.next()) {
                    orderID = resultSet.getInt("OrderID");
                }
                // 3. Orders tablosuna yeni bir sipariş ekle
                String queryInsertOrder = "INSERT INTO OrderItems (orderID, DishID, Quantity, Price) VALUES (?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(queryInsertOrder);


                preparedStatement.setInt(1, orderID);
                preparedStatement.setInt(2, 1);
                preparedStatement.setDouble(3, amount);
                preparedStatement.setDouble(4, price);

                // Siparişi ekle
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Sipariş başarıyla eklendi. CustomerID: " + orderID);
                } else {
                    System.out.println("Sipariş eklenirken bir hata oluştu.");
                }


            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Kaynakları kapat
                try {
                    if (resultSet != null) resultSet.close();
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (lazanyaCheckBox.isSelected() && !flaglazanya) {
            int amount = Integer.parseInt(lazanyaAmountField.getText());
            int price = amount*200;

            try {
                // Veritabanı bağlantısı kur
                connection = DriverManager.getConnection(url, user, password);

                // 1. En son CustomerID'yi al
                String orderQueryID = "SELECT TOP 1 OrderID FROM Orders ORDER BY OrderID DESC ;";
                preparedStatement = connection.prepareStatement(orderQueryID);
                resultSet = preparedStatement.executeQuery();

                int orderID=0;
                if (resultSet.next()) {
                    orderID = resultSet.getInt("OrderID");
                }
                // 3. Orders tablosuna yeni bir sipariş ekle
                String queryInsertOrder = "INSERT INTO OrderItems (orderID, DishID, Quantity, Price) VALUES (?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(queryInsertOrder);


                preparedStatement.setInt(1, orderID);
                preparedStatement.setInt(2, 2);
                preparedStatement.setDouble(3, amount);
                preparedStatement.setDouble(4, price);

                // Siparişi ekle
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Sipariş başarıyla eklendi. CustomerID: " + orderID);
                } else {
                    System.out.println("Sipariş eklenirken bir hata oluştu.");
                }


            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Kaynakları kapat
                try {
                    if (resultSet != null) resultSet.close();
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (waffleCheckBox.isSelected() && !flagwaffle) {
            int amount = Integer.parseInt(waffleAmountField.getText());
            int price = amount*100;

            try {
                // Veritabanı bağlantısı kur
                connection = DriverManager.getConnection(url, user, password);

                // 1. En son CustomerID'yi al
                String orderQueryID = "SELECT TOP 1 OrderID FROM Orders ORDER BY OrderID DESC ;";
                preparedStatement = connection.prepareStatement(orderQueryID);
                resultSet = preparedStatement.executeQuery();

                int orderID=0;
                if (resultSet.next()) {
                    orderID = resultSet.getInt("OrderID");
                }
                // 3. Orders tablosuna yeni bir sipariş ekle
                String queryInsertOrder = "INSERT INTO OrderItems (orderID, DishID, Quantity, Price) VALUES (?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(queryInsertOrder);


                preparedStatement.setInt(1, orderID);
                preparedStatement.setInt(2, 3);
                preparedStatement.setDouble(3, amount);
                preparedStatement.setDouble(4, price);

                // Siparişi ekle
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Sipariş başarıyla eklendi. CustomerID: " + orderID);
                } else {
                    System.out.println("Sipariş eklenirken bir hata oluştu.");
                }


            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Kaynakları kapat
                try {
                    if (resultSet != null) resultSet.close();
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
