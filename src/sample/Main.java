package sample;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        primaryStage.setTitle("Everyday CheckList");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.sizeToScene();
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            try {
                MainController.saveStatus();
            } catch (IOException e) {
                System.out.println("Status not saved");
            }
        });
        primaryStage.getIcons().add(new Image("file:favicon.jpg"));

    }


    public static void main(String[] args) {
        launch(args);
    }
}
