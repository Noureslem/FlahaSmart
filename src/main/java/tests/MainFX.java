package tests;

import Controler.ThreadController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        new ThreadController().afficher(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}