module com.example.demo_db_management_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.demo_db_management_project to javafx.fxml;
    exports com.example.demo_db_management_project;
}