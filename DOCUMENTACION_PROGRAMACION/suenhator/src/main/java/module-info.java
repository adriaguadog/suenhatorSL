module org.example.suenhator {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens org.example.suenhator to javafx.fxml;
    exports org.example.suenhator;
    exports org.example.suenhator.controller;
    opens org.example.suenhator.controller to javafx.fxml;
}