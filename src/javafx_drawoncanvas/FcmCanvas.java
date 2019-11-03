package javafx_drawoncanvas;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import kmeans.Point;
import kmeans.WeightsMatrix;

import java.util.List;

public class FcmCanvas extends Canvas {

    private GraphicsContext graphicsContext;
    private IScene parent;

    public FcmCanvas(IScene parent, int width, int height) {
        super(width, height);
        this.parent = parent;
        graphicsContext = this.getGraphicsContext2D();

        this.clear();
        this.addEventHandler(MouseEvent.MOUSE_PRESSED,
            event -> {
                parent.handleNewPoint(new Point(event.getX(), event.getY()));
            });
    }

    public void clear(){
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0,              //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,    //width of the rectangle
                canvasHeight);  //height of the rectangle

        graphicsContext.setFill(Color.LIGHTGRAY);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(5);

        graphicsContext.fill();
        graphicsContext.strokeRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,    //width of the rectangle
                canvasHeight);  //height of the rectangle

        graphicsContext.setFill(Color.RED);
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(1);
    }

    public void drawPoint(Point point, Color color) {
        graphicsContext.setFill(color);
        graphicsContext.fillOval(point.getX() - point.getR(), point.getY() - point.getR(), 2*point.getR(), 2*point.getR());
    }

    public void drawCluster() {

    }

    public void drawCluster(WeightsMatrix partition, List<Point> points) {

      /*  for (int clusterId = 0; clusterId < partition.getNumRows(); clusterId++) {

        }


        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setStroke(Color.BLACK);
        double[] x = {100, 120, 120, 100, 80, 80};
        double[] y = {0, 20, 40, 60, 80, 100};
        graphicsContext.fillPolygon(new double[]{10, 40, 10, 40},
                new double[]{210, 210, 240, 240}, 4);*/
    }
}
