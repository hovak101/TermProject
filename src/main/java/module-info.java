module s25.cs151.application {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens s25.cs151.application to javafx.fxml;
    opens s25.cs151.application.CONTROLLER to javafx.fxml;
    
    exports s25.cs151.application;
    exports s25.cs151.application.CONTROLLER;
    exports s25.cs151.application.MODEL;
}