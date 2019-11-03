package javafx_drawoncanvas;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kmeans.*;

import java.util.ArrayList;
import java.util.List;

public class FcmApplication extends Application {

    private Stage primaryStage;
    private DemoScene demoScene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Fuzzy C-Mean Classification");

        BorderPane initBorder = new BorderPane();
        this.demoScene = new DemoScene(initBorder, 1300, 700);

        this.primaryStage.setScene(this.demoScene);
        this.primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}