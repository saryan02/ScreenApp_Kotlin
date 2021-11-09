module com.example.windowapps {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires java.desktop;
    requires javafx.swing;
    requires java.logging;


    opens com.example.windowapps to javafx.fxml;
    exports com.example.windowapps;
}