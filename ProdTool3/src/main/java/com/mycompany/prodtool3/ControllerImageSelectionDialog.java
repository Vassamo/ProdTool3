package com.mycompany.prodtool3;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ControllerImageSelectionDialog {

    @FXML
    private TilePane tilePane;

    @FXML
    private TextField ImageTitle; // Pole tekstowe do wyświetlania nazwy wybranego pliku

    private Stage dialogStage;
    private File selectedImageFile;
    private static final int IMGres = 64;
    private ImageView currentlySelectedImageView; // Przechowuje obecnie zaznaczony obraz

    public void initialize() {
        loadImages();
        // Dodaj słuchacza sceny, jeśli scena nie jest jeszcze ustawiona
        if (tilePane.getScene() == null) {
            tilePane.sceneProperty().addListener((observable, oldScene, newScene) -> {
                if (newScene != null) {
                    System.out.println("Scene has been set, setting up key handling.");
                    setupKeyHandling(newScene);
                }
            });
        } else {
            System.out.println("Scene is already set, setting up key handling.");
            setupKeyHandling(tilePane.getScene());
        }
    }

    @FXML
    private TextField imageTitleField;

    private void loadImages() {
        try {
            tilePane.getChildren().clear(); // Wyczyszczenie TilePane przed załadowaniem nowych obrazków
            File folder = new File("Session/UserImages");
            if (!folder.exists()) {
                folder.mkdirs(); // Upewnij się, że katalog istnieje
            }
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".gif"));

            if (files != null) {
                for (File file : files) {
                    Image image = new Image(file.toURI().toString(), IMGres, IMGres, true, true);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(IMGres);
                    imageView.setFitHeight(IMGres);
                    imageView.setFocusTraversable(true);

                    imageView.setOnMouseClicked(event -> handleImageSelection(file, imageView));
                    imageView.setOnKeyPressed(event -> {
                        if (event.getCode() == KeyCode.ENTER) {
                            handleImageSelection(file, imageView);
                        }
                    });

                    tilePane.getChildren().add(imageView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupKeyHandling(Scene scene) {
        if (scene == null) {
            System.err.println("Scene is null in setupKeyHandling.");
            return;
        }

        System.out.println("Adding stylesheets and key event filter.");
        String cssResource = getClass().getResource("styles.css").toExternalForm();
        if (cssResource == null) {
            System.err.println("CSS resource not found.");
        } else {
            scene.getStylesheets().add(cssResource);
        }

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                Node focusedNode = scene.getFocusOwner();
                if (focusedNode instanceof ImageView) {
                    ImageView imageView = (ImageView) focusedNode;
                    if (currentlySelectedImageView != null) {
                        currentlySelectedImageView.getStyleClass().remove("selected-image"); // Resetuje styl poprzednio zaznaczonego obrazka
                    }
                    imageView.getStyleClass().add("selected-image"); // Dodaje styl do nowo zaznaczonego obrazka
                    currentlySelectedImageView = imageView;
                }
            }
        });
    }

    private void handleImageSelection(File file, ImageView imageView) {
        selectedImageFile = file;
        ImageTitle.setText(file.getName()); // Ustaw nazwę wybranego pliku w polu tekstowym
        System.out.println("Selected image: " + selectedImageFile.getAbsolutePath());

        if (currentlySelectedImageView != null) {
            currentlySelectedImageView.getStyleClass().remove("selected-image"); // Resetuje styl poprzednio zaznaczonego obrazka
        }
        imageView.getStyleClass().add("selected-image"); // Dodaje styl do nowo zaznaczonego obrazka
        currentlySelectedImageView = imageView;
    }

    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg")
        );
        
        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            try {
                // Ścieżka do katalogu docelowego
                Path destDir = Paths.get("Session/UserImages");
                Files.createDirectories(destDir); // Upewnij się, że katalog istnieje
                
                // Ścieżka do docelowego pliku
                Path destPath = destDir.resolve(selectedFile.getName());
                
                // Kopiowanie pliku
                Files.copy(selectedFile.toPath(), destPath);

                // Odśwież listę obrazków w TilePane
                loadImages();
                
                // Wyświetl komunikat o sukcesie
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Image Added");
                alert.setHeaderText(null);
                alert.setContentText("Image added successfully!");
                alert.showAndWait();
                
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to save image.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public File getSelectedImageFile() {
        return selectedImageFile;
    }
}
