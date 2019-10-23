package javafx_drawoncanvas;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kmeans.*;

import java.util.ArrayList;
import java.util.List;

public class FcmApplication extends Application {

    private Boolean drawPoint = true;
    private FuzzyKMeans fuzzyKMeans;
    private List<Point> points = new ArrayList<>();
    private List<Point> centroids = new ArrayList<>();
    private FcmCanvas canvas;

    private Stage primaryStage;
    private InitScene initScene;
    private DemoScene demoScene;

    @Override
    public void start(Stage primaryStage) {


        Button reset = new Button("Reset");
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                points.clear();
                centroids.clear();
                canvas.clear();
            }
        });

        // create a toggle group
        ToggleGroup tg = new ToggleGroup();

        // create radiobuttons
        RadioButton r1 = new RadioButton("Point");
        RadioButton r2 = new RadioButton("Center");
        r1.setSelected(true);


        // add radiobuttons to toggle group
        r1.setToggleGroup(tg);
        r2.setToggleGroup(tg);

        // add a change listener
        tg.selectedToggleProperty().addListener((ob, o, n) -> {

            RadioButton rb = (RadioButton)tg.getSelectedToggle();

            if (rb != null) {
                setDrawType(!drawPoint);
            }
        });

        Button step = new Button("Step");
        step.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                fuzzyKMeans = new FuzzyKMeans(points, centroids);
                fuzzyKMeans.makeStep();
                setCentroids(fuzzyKMeans.getCenters());
                canvas.clear();
                drawCurrentPoints();
            }
        });


        VBox menu = new VBox();

        menu.getChildren().add(reset);
        menu.getChildren().add(r1);
        menu.getChildren().add(r2);
        menu.getChildren().add(step);

        border.setLeft(menu);
        border.setCenter(canvas);

        Scene scene = new Scene(border, 600, 600);




        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Fuzzy C-Mean Classification");

        BorderPane initBorder = new BorderPane();
        this.initScene = new InitScene(initBorder, 600, 400);

        BorderPane demoBorder = new BorderPane();
        this.demoScene = new DemoScene(demoBorder, 400, 600);

        this.primaryStage.setScene(this.initScene);
        this.primaryStage.show();
    }

    public void setInitScene() {
        this.primaryStage.setScene(this.initScene);
    }

    public void setDemoScene() {
        this.primaryStage.setScene(this.demoScene);
    }

    private void setDrawType(boolean type) {
        drawPoint = type;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setCentroids(List<Point> centroids) {
        this.centroids = centroids;
    }



    private void drawCurrentPoints() {
        for (Point point : this.points) {
            canvas.drawPoint(point, Color.BLACK);
        }
        for (Point center : this.centroids) {
            canvas.drawPoint(center, Color.RED);
        }
    }
}