module com.mycompany.prodtool3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.mycompany.prodtool3 to javafx.fxml;
    exports com.mycompany.prodtool3;
    
//    opens com.mycompany.prodtool3.FXMLControllers to javafx.fxml;
//    exports com.mycompany.prodtool3;

}
