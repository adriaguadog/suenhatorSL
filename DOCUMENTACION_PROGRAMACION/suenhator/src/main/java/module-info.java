module org.example.suenhator {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;


    opens org.example.suenhator to javafx.fxml;
    exports org.example.suenhator;
    exports org.example.suenhator.controller;
    opens org.example.suenhator.controller to javafx.fxml;
    opens org.example.suenhator.dao to java.sql;
    exports org.example.suenhator.dao;

}