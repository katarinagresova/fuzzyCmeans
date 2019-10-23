package kmeans;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField cislo1TextField;
    @FXML
    private TextField cislo2TextField;
    @FXML
    private ComboBox operaceComboBox;
    @FXML
    private Button initButton;

    @FXML private Canvas canvas;

    @FXML
    private void randomInit(ActionEvent event) {
        System.out.println("button");
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLUE);
        gc.fillRect(0,0,100,100);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        System.out.println("color set to black");
        gc.fillRect(50, 50, 100, 100);
        System.out.println("draw rectangle");
    }

}
