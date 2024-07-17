package com.mycompany.prodtool3;



//import com.mycompany.prodtool3.FXMLControllers.MainCategoryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    
    private static App instance;

    public App() {
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    private static Scene scene;
    private String CodePass = "123";
    private boolean IsAdmin = false;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(loadFXML("Category"));
        
        stage.setScene(scene);
        stage.setTitle("Category Viewer");
        stage.show();
    }
        
    static public void setRoot(String fxml) throws Exception {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        //System.out.println("g");
        //CategoryClass g = new CategoryClass("g");
        launch(args);
    }
    
    public void AdminRequest(String Code){
        if(Code == CodePass) {
            IsAdmin = true;
        } else {
            IsAdmin = false;
        }
    }

}
