module s25.cs151.termproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens s25.cs151.termproject to javafx.fxml;
    exports s25.cs151.termproject;
}