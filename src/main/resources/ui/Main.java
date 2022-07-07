package sample.asm2.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/contact.fxml"));
        Parent root = loader.load();
        stage.setTitle("My Contract");
        stage.setScene(new Scene(root, 700, 575));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}