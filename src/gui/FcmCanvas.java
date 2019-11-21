package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import kmeans.Point;
import kmeans.WeightsMatrix;

import java.util.List;

public class FcmCanvas extends Canvas {

    private GraphicsContext graphicsContext;
    private IScene parent;
    private CentroidColor centroidColor;

    public FcmCanvas(IScene parent, int width, int height) {
        super(width, height);
        this.parent = parent;
        graphicsContext = this.getGraphicsContext2D();

        this.clear();
        this.addEventHandler(MouseEvent.MOUSE_PRESSED,
            event -> {
                this.parent.handleNewPoint(event.getX(), event.getY());
            });

        this.centroidColor = new CentroidColor();
    }

    public void clear(){
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        graphicsContext.clearRect(0,
                0,
                canvasWidth,
                canvasHeight);

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

    public void drawPointsInCentroids(List<Point> points, List<Point> centers, WeightsMatrix weights) {
        double maxDistance = 0;

        for (Point center: centers) {
            for (Point point: points) {
                double distance = Point.euclideanDistance(point, center);
                if (distance > maxDistance) {
                    maxDistance = distance;
                }
            }
        }

        for (int center_index = 0; center_index < centers.size(); center_index++) {
            Point center = centers.get(center_index);
            for (int point_index = 0; point_index < points.size(); point_index++) {
                Point point = points.get(point_index);
                drawArrow(point.getX(),
                        point.getY(),
                        (center.getX()) - ((center.getX() - point.getX()) * (1 -weights.valueAt(center_index, point_index))),
                        (center.getY()) - ((center.getY() - point.getY()) * (1 - weights.valueAt(center_index, point_index))),
                        centroidColor.getColor(center_index)
                );

                drawPoint(point, Color.BLACK);
            }
            drawPoint(center, centroidColor.getColor(center_index));
        }


    }

    private void drawArrow(double startX, double startY, double endX, double endY, Color color) {
        double arrowHeadSize = 3.0;

        graphicsContext.setStroke(color);
        graphicsContext.beginPath();
        graphicsContext.moveTo(startX, startY);

        //ArrowHead
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;

        graphicsContext.lineTo(x1, y1);
        graphicsContext.lineTo(x2, y2);
        graphicsContext.lineTo(endX, endY);

        graphicsContext.stroke();
        graphicsContext.closePath();
    }
}
