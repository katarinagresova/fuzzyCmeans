package gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import kmeans.FuzzyKMeans;
import kmeans.Point;

import java.util.ArrayList;
import java.util.List;

public class DemoScene extends Scene implements IScene {

    private GridPane menu;
    private FcmCanvas canvas;
    private Spinner<Double> fuzzinessField;
    private Spinner<Double> epsilonField;
    private RadioButton clickPoints;
    private RadioButton clickCentroids;
    private Spinner<Integer> centroidField;
    private Spinner<Integer> pointsField;
    private List<Point> points;
    private List<Point> centroids;
    private boolean drawPoint;
    private boolean clickedCentroids;
    private boolean drawingEnabled;
    private FuzzyKMeans fuzzyKMeans;
    private final int maxNumOfCentroids = 10;
    private CentroidColor centroidColor = new CentroidColor();

    public DemoScene(BorderPane parent, int width, int height) {
        super(parent, width, height);
        points = new ArrayList<>();
        centroids = new ArrayList<>();
        drawPoint = true;
        clickedCentroids =false;
        drawingEnabled = true;

        menu = createParametersFormPane();
        addUIControls(menu);
        parent.setLeft(menu);

        canvas = new FcmCanvas(this, 700, 700);
        parent.setRight(canvas);
    }

    private GridPane createParametersFormPane() {
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);
        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        // Set the horizontal gap between columns
        gridPane.setHgap(10);
        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 150, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(100,150, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        // columnTwoConstraints will be applied to all the nodes placed in column three.
        ColumnConstraints columnThreeConstrains = new ColumnConstraints(100,150, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains, columnThreeConstrains);

        return gridPane;
    }

    private void addUIControls(GridPane gridPane) {
        addAlgorithmControls(gridPane);
        addCentroidsControls(gridPane);
        addPointsControls(gridPane);
        addPointCentroidSwitchingControls(gridPane);
        addFlowControls(gridPane);
    }

    private void addAlgorithmControls(GridPane gridPane) {
        Label headerLabel = new Label("Algorithm parameters");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(headerLabel, 0,0,3,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,10,0));

        addFuzzinessControl(gridPane);
        addEpsilonControl(gridPane);
    }

    private void addFuzzinessControl(GridPane gridPane) {
        // Add Fuzziness Label
        Label fuzzinessLabel = new Label("Fuzziness : ");
        gridPane.add(fuzzinessLabel, 0,1);

        // Add Fuzziness Text Field with default value 2
        fuzzinessField = new Spinner<>(1.0, 4.0, 2.0, 0.1);
        fuzzinessField.setEditable(true);
        fuzzinessField.setMaxWidth(100);
        fuzzinessField.setPrefHeight(40);
        gridPane.add(fuzzinessField, 1,1);
    }

    private void addEpsilonControl(GridPane gridPane) {
        // Add Epsilon Label
        Label epsilonLabel = new Label("Epsilon : ");
        gridPane.add(epsilonLabel, 0,2);

        // Add Epsilon Text Field with default value 0.001
        epsilonField = new Spinner<>(0.01, 0.9, 0.01, 0.01);
        epsilonField.setEditable(true);
        epsilonField.setMaxWidth(100);
        epsilonField.setPrefHeight(40);
        gridPane.add(epsilonField, 1,2);
    }

    private void addCentroidsControls(GridPane gridPane) {
        Label centroidsLabel = new Label("Centroids controls");
        centroidsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(centroidsLabel, 0,4,3,1);
        GridPane.setHalignment(centroidsLabel, HPos.CENTER);
        GridPane.setMargin(centroidsLabel, new Insets(20, 0,10,0));

        Label centroidsSubLabel = new Label("Set number of centroids to generate or \"click\" your own centroids on canvas.");
        centroidsSubLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        gridPane.add(centroidsSubLabel, 0,5,3,1);
        GridPane.setHalignment(centroidsSubLabel, HPos.CENTER);
        GridPane.setMargin(centroidsSubLabel, new Insets(0, 0,0,0));

        addCentroidsSelectControls(gridPane);
    }

    private void addCentroidsSelectControls(GridPane gridPane) {
        // create a toggle group
        ToggleGroup tg = new ToggleGroup();

        // create radiobuttons
        RadioButton r1 = new RadioButton("I will set number of centroids: ");
        RadioButton r2 = new RadioButton("I will click my own centroids.  ");
        r1.setSelected(true);


        // add radiobuttons to toggle group
        r1.setToggleGroup(tg);
        r2.setToggleGroup(tg);

        // add a change listener
        tg.selectedToggleProperty().addListener((ob, o, n) -> {

            RadioButton rb = (RadioButton)tg.getSelectedToggle();

            if (rb != null) {
                handleChangeOfCentroidsInput();
            }
        });

        gridPane.add(r1, 0, 6, 2, 1);
        gridPane.add(r2, 0, 7, 2, 1);

        centroidField = new Spinner<>(1, 10, 3, 1);
        centroidField.setEditable(true);
        centroidField.setMaxWidth(100);
        centroidField.setPrefHeight(40);
        gridPane.add(centroidField, 2,6);

    }

    private void addPointsControls(GridPane gridPane) {
        Label centroidsLabel = new Label("Points controls");
        centroidsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(centroidsLabel, 0,8,3,1);
        GridPane.setHalignment(centroidsLabel, HPos.CENTER);
        GridPane.setMargin(centroidsLabel, new Insets(20, 0,10,0));

        Label fuzzinessLabel = new Label("Random points: ");
        gridPane.add(fuzzinessLabel, 0,9);

        pointsField = new Spinner<>(1, 20, 10, 1);
        pointsField.setEditable(true);
        pointsField.setMaxWidth(100);
        pointsField.setPrefHeight(40);
        gridPane.add(pointsField, 1,9);

        Button generate = new Button("Generate");
        generate.setOnAction(e -> {
            generatePoints(pointsField.getValue());
        });
        gridPane.add(generate, 2,9);
    }

    private void addPointCentroidSwitchingControls(GridPane gridPane) {
        Label centroidsLabel = new Label("Point/centroid controls");
        centroidsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(centroidsLabel, 0,10,3,1);
        GridPane.setHalignment(centroidsLabel, HPos.CENTER);
        GridPane.setMargin(centroidsLabel, new Insets(20, 0,10,0));

        Label centroidsSubLabel = new Label("Select what type of object will be created after clicking on canvas.");
        centroidsSubLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        gridPane.add(centroidsSubLabel, 0,11,3,1);
        GridPane.setHalignment(centroidsSubLabel, HPos.CENTER);
        GridPane.setMargin(centroidsSubLabel, new Insets(0, 0,0,0));

        addPointCentroidSwitching(gridPane);
    }

    private void addPointCentroidSwitching(GridPane gridPane) {
        // create a toggle group
        ToggleGroup tg = new ToggleGroup();

        // create radiobuttons
        clickPoints = new RadioButton("Point");
        clickCentroids = new RadioButton("Centroid");
        clickPoints.setSelected(true);
        clickCentroids.setDisable(true);


        // add radiobuttons to toggle group
        clickPoints.setToggleGroup(tg);
        clickCentroids.setToggleGroup(tg);

        // add a change listener
        tg.selectedToggleProperty().addListener((ob, o, n) -> {

            RadioButton rb = (RadioButton)tg.getSelectedToggle();

            if (rb != null) {
                setDrawType(!drawPoint);
            }
        });

        gridPane.add(clickPoints, 0, 12);
        gridPane.add(clickCentroids, 1, 12);
    }

    private void addFlowControls(GridPane gridPane) {
        Label centroidsLabel = new Label("Flow controls");
        centroidsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(centroidsLabel, 0,13,3,1);
        GridPane.setHalignment(centroidsLabel, HPos.CENTER);
        GridPane.setMargin(centroidsLabel, new Insets(20, 0,10,0));

        addFlowButtons(gridPane);
    }

    private void addFlowButtons(GridPane gridPane) {
        Button step = new Button("Step");
        step.setOnAction(e -> {
            getFuzzyKMeans().makeStep();
            updateCanvasInRun();
            drawingEnabled = false;
        });
        gridPane.add(step, 0, 14);

        Button run = new Button("Run");
        run.setOnAction(e -> {
            getFuzzyKMeans().execute();
            updateCanvasInRun();
            drawingEnabled = false;
        });
        gridPane.add(run, 1, 14);

        Button reset = new Button("Reset");
        reset.setOnAction(e -> {
            points.clear();
            centroids.clear();
            canvas.clear();
            drawingEnabled = true;
            fuzzyKMeans = null;
        });
        gridPane.add(reset, 2, 14);
    }

    public void handleNewPoint(double x, double y) {
        if (drawingEnabled) {
            if (drawPoint) {
                Point point = new Point(x, y);
                this.canvas.drawPoint(point, Color.BLACK);
                this.points.add(point);
            } else {
                if (maxNumOfCentroids > this.centroids.size()) {
                    Point point = new Point(x, y, 6);
                    this.canvas.drawPoint(point, centroidColor.getColor(this.centroids.size()));
                    this.centroids.add(point);
                }
            }
        }
    }

    private void handleChangeOfCentroidsInput() {
        clickedCentroids = !clickedCentroids;
        if (clickedCentroids) {
            enableCentroidsClicking();
            disableCentroidsNumber();
        } else {
            disableCentroidsClicking();
            enableCentroidsNumber();
        }
    }

    private void enableCentroidsClicking() {
        clickCentroids.setDisable(false);
    }

    private void disableCentroidsClicking() {
        clickCentroids.setDisable(true);
        clickPoints.setSelected(true);
        setDrawType(true);
        if (drawingEnabled) {
            this.centroids.removeAll(this.centroids);
            updateCanvas();
        }
    }

    private void enableCentroidsNumber() {
        centroidField.setDisable(false);
    }

    private void disableCentroidsNumber() {
        centroidField.setDisable(true);
    }

    private void setDrawType(boolean type) {
        drawPoint = type;
    }

    private FuzzyKMeans getFuzzyKMeans() {
        if (fuzzyKMeans == null) {
            if (clickedCentroids) {
                fuzzyKMeans = new FuzzyKMeans(points, centroids, getFuzziness(), getEpsilon());
            } else {
                fuzzyKMeans = new FuzzyKMeans(points, centroidField.getValue(), getFuzziness(), getEpsilon());
            }
        }
        return fuzzyKMeans;
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

    private void updateCanvas() {
        if (fuzzyKMeans != null) {
            setCentroids(getFuzzyKMeans().getCenters());
        }
        canvas.clear();
        drawCurrentPoints();
    }

    private void updateCanvasInRun() {
        if (fuzzyKMeans != null) {
            setCentroids(getFuzzyKMeans().getCenters());
        }
        canvas.clear();
        canvas.drawPointsInCentroids(points, centroids, getFuzzyKMeans().getPartition());
    }

    private double getFuzziness() {
        return fuzzinessField.getValue();
    }

    private double getEpsilon() {
        return this.epsilonField.getValue();
    }

    private void generatePoints(Integer count) {
        //maximum number
        int min = 0;
        //minimum number
        int max = 700;

        for (int index = 0; index < count; index++) {
            Point point = new Point(min + Math.random() * (max - min), min + Math.random() * (max - min));
            points.add(point);
            canvas.drawPoint(point, Color.BLACK);
        }
    }
}
