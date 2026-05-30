module com.example.cinebook {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;


    opens com.example.cinebook to javafx.fxml;
    opens com.example.cinebook.controller to javafx.fxml;
    opens com.example.cinebook.model to javafx.base;

    exports com.example.cinebook;
    exports com.example.cinebook.controller;
    exports com.example.cinebook.model;
    exports com.example.cinebook.dao;
    exports com.example.cinebook.util;
}