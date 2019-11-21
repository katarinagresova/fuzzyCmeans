package gui;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FcmApplication extends Application {

    private Stage primaryStage;
    private DemoScene demoScene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Fuzzy C-Mean");

        BorderPane initBorder = new BorderPane();
        this.demoScene = new DemoScene(initBorder, 1300, 700);

        this.primaryStage.setScene(this.demoScene);
        this.primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}