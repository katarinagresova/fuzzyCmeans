package kmeans;

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

    /**
     * Compute euclidian distance
     *
     * @param a point a
     * @param b point b
     * @return  Euclidian distance between two points
     */
    public static double euclideanDistance(Point a, Point b) {
        double sum = 0.0;
        sum += Math.pow(a.getX() - b.getX(), 2);
        sum += Math.pow(a.getY() - b.getY(), 2);
        return Math.sqrt(sum);
    }

    /**
     * Compute euclidian distance between two points specified by array of dimensions
     *
     * @param a point a
     * @param b point b
     * @return  Euclidian distance between two points
     */
    public static double euclideanDistance(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("The dimensions have to be equal!");
        }

        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }

        return Math.sqrt(sum);
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
