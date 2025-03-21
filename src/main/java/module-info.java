module s25.cs151.application {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens s25.cs151.application to javafx.fxml;
    exports s25.cs151.application;
}