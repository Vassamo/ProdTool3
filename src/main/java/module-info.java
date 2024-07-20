module com.mycompany.prodtool3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;

    opens com.mycompany.prodtool3 to javafx.fxml, com.google.gson;
    exports com.mycompany.prodtool3;
}