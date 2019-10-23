package kmeans;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Point {
    private double x;
    private double y;
    private double r;

    public Point(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public Point(double x, double y) {
        this(x, y, 4);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }
}
