package javafx_drawoncanvas;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import kmeans.FuzzyKMeans;
import kmeans.Point;

import java.util.ArrayList;
import java.util.List;

public class DemoScene extends Scene implements IScene {

    private GridPane menu;
    private FcmCanvas canvas;
    private TextField fuzzinessField;
    private TextField epsilonField;
    private RadioButton clickPoints;
    private RadioButton clickCentroids;
    private TextField centroidField;
    private List<Point> points;
    private List<Point> centroids;
    // flag for switching between points and centroids
    private boolean drawPoint;
    private boolean clickedCentroids;
    private boolean drawingEnabled;
    private FuzzyKMeans fuzzyKMeans;

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
        ColumnConstraints columnOneConstraints = new ColumnConstraints(150, 220, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(150,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    private void addUIControls(GridPane gridPane) {
        addAlgorithmControls(gridPane);
        addCentroidsControls(gridPane);
        addPointCentroidSwitchingControls(gridPane);
        addFlowControls(gridPane);
    }

    private void addAlgorithmControls(GridPane gridPane) {
        Label headerLabel = new Label("Algorithm parameters");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(headerLabel, 0,0,2,1);
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
        fuzzinessField = new TextField("2");
        fuzzinessField.setMaxWidth(100);
        fuzzinessField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d([\\.]\\d{0,4})?")) {
                    fuzzinessField.setText(oldValue);
                }
            }
        });
        fuzzinessField.setPrefHeight(40);
        gridPane.add(fuzzinessField, 1,1);
    }

    private void addEpsilonControl(GridPane gridPane) {
        // Add Epsilon Label
        Label epsilonLabel = new Label("Epsilon : ");
        gridPane.add(epsilonLabel, 0,2);

        // Add Epsilon Text Field with default value 0.001
        epsilonField = new TextField("0.001");
        epsilonField.setMaxWidth(100);
        epsilonField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("0([\\.]\\d{0,4})?")) {
                    epsilonField.setText(oldValue);
                }
            }
        });
        epsilonField.setPrefHeight(40);
        gridPane.add(epsilonField, 1,2);
    }

    private void addCentroidsControls(GridPane gridPane) {
        Label centroidsLabel = new Label("Centroids controls");
        centroidsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(centroidsLabel, 0,4,2,1);
        GridPane.setHalignment(centroidsLabel, HPos.CENTER);
        GridPane.setMargin(centroidsLabel, new Insets(20, 0,10,0));

        Label centroidsSubLabel = new Label("Set number of centroids to generate or \"click\" your own centroids on canvas.");
        centroidsSubLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        gridPane.add(centroidsSubLabel, 0,5,2,1);
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

        gridPane.add(r1, 0, 6);
        gridPane.add(r2, 0, 7);

        centroidField = new TextField("5");
        centroidField.setMaxWidth(100);
        centroidField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d?")) {
                    centroidField.setText(oldValue);
                }
            }
        });
        centroidField.setPrefHeight(40);
        gridPane.add(centroidField, 1,6);

    }

    private void addPointCentroidSwitchingControls(GridPane gridPane) {
        Label centroidsLabel = new Label("Point/centroid controls");
        centroidsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(centroidsLabel, 0,8,2,1);
        GridPane.setHalignment(centroidsLabel, HPos.CENTER);
        GridPane.setMargin(centroidsLabel, new Insets(20, 0,10,0));

        Label centroidsSubLabel = new Label("Select what type of object will be created after clicking on canvas.");
        centroidsSubLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        gridPane.add(centroidsSubLabel, 0,9,2,1);
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

        gridPane.add(clickPoints, 0, 10);
        gridPane.add(clickCentroids, 1, 10);
    }

    private void addFlowControls(GridPane gridPane) {
        Label centroidsLabel = new Label("Flow controls");
        centroidsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(centroidsLabel, 0,11,2,1);
        GridPane.setHalignment(centroidsLabel, HPos.CENTER);
        GridPane.setMargin(centroidsLabel, new Insets(20, 0,10,0));

        addFlowButtons(gridPane);
    }

    private void addFlowButtons(GridPane gridPane) {
        GridPane subGridPane = new GridPane();


        Button reset = new Button("Reset");
        reset.setOnAction(e -> {
            points.clear();
            centroids.clear();
            canvas.clear();
            drawingEnabled = true;
        });
        subGridPane.add(reset, 0, 0);

        Button step = new Button("Step");
        step.setOnAction(e -> {
            getFuzzyKMeans().makeStep();
            updateCanvas();
            drawingEnabled = false;
            //canvas.drawFirstCluster();
        });
        subGridPane.add(step, 1, 0);

        Button run = new Button("Run");
        run.setOnAction(e -> {
            getFuzzyKMeans().execute();
            updateCanvas();
            drawingEnabled = false;
        });
        subGridPane.add(run, 2, 0);

        GridPane.setMargin(gridPane, new Insets(0, 50,0,100));
        gridPane.add(subGridPane, 0, 12, 2, 1);
    }

    public void handleNewPoint(Point point) {
        if (drawingEnabled) {
            if (drawPoint) {
                this.canvas.drawPoint(point, Color.BLACK);
                this.points.add(point);
            } else {
                this.canvas.drawPoint(point, Color.RED);
                this.centroids.add(point);
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
            fuzzyKMeans = new FuzzyKMeans(points, centroids, getFuzziness(), getEpsilon());
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
        setCentroids(getFuzzyKMeans().getCenters());
        canvas.clear();
        drawCurrentPoints();
    }

    private double getFuzziness() {
        return Double.parseDouble(this.fuzzinessField.getText());
    }

    private double getEpsilon() {
        return Double.parseDouble(this.epsilonField.getText());
    }
}
