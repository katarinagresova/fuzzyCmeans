package javafx_drawoncanvas;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextField;
import kmeans.Point;

import java.util.List;

public class InitScene extends Scene implements IScene {

    private GridPane menu;
    private FcmCanvas canvas;
    private List<Point> points;
    private List<Point> centroids;
    // flag for switching between points and centroids
    private boolean drawPoint;

    public InitScene(BorderPane parent, int width, int height) {
        super(parent, width, height);

        menu = createParametersFormPane();
        addUIControls(menu);
        parent.setLeft(menu);

        canvas = new FcmCanvas(this, 400, 400);
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
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    public void addUIControls(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("Algorithm parameters");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Add Fuzziness Label
        Label fuzzinessLabel = new Label("Fuzziness : ");
        gridPane.add(fuzzinessLabel, 0,1);

        // Add Fuzziness Text Field
        TextField fuzzinessField = new TextField();
        fuzzinessField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,2}([\\.]\\d{0,4})?")) {
                    fuzzinessField.setText(oldValue);
                }
            }
        });
        fuzzinessField.setPrefHeight(40);
        gridPane.add(fuzzinessField, 1,1);
    }

    public void handleNewPoint(Point point) {
        if (drawPoint) {
            this.canvas.drawPoint(point, Color.BLACK);
            this.points.add(point);
        } else {
            this.canvas.drawPoint(point, Color.RED);
            this.centroids.add(point);
        }
    }
}
