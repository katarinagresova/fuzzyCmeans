package kmeans;

import java.util.List;

public final class WeightsMatrix {

    private final double[][] content;

    public WeightsMatrix(int x, int y) {
        this.content = new double[x][y];
    }

    public WeightsMatrix(WeightsMatrix clone){
        this.content = new double[clone.getNumRows()][clone.getNumColumns()];
        for (int i = 0; i < clone.getNumRows(); i++){
            for (int j = 0; j < clone.getNumColumns(); j++){
                this.content[i][j] = clone.valueAt(i, j);
            }
        }
    }

    /**
     * Obtain the current value within the matrix at the provided co-ordinates.
     * @param row The row to lookup, indexed from 0.
     * @param col The column to lookup, indexed from 0.
     * @return The value located at the position {@code [row][col]}.
     */
    public double valueAt(int row, int col) {
        return this.content[row][col];
    }

    /**
     * Get the number of rows within the matrix.
     * @return The number of rows.
     */
    public int getNumRows() {
        return this.content.length;
    }

    /**
     * Get the number of columns within the matrix.
     * @return The number of columns.
     */
    public int getNumColumns() {
        return this.content[0].length;
    }

    /**
     *
     * @param fuziness
     * @param points
     * @param centers
     * @return          old matrix
     */
    public WeightsMatrix update(double fuziness, List<Point> points, List<Point> centers) {

        WeightsMatrix oldMatrix = new WeightsMatrix(this);

        double exponent = 2.0 / (fuziness - 1);
        /* loop over clusters */
        for(int line = 0; line < centers.size(); line++){
            /* loop over features */
            for(int col = 0; col < points.size(); col++){
                double denominator = 0.0;
                /* dik = d(xk - vi) */
                double dik = euclideanDistance(points.get(col), centers.get(line));
                if(dik != 0.0){
                    for(int s = 0; s < centers.size(); s++){
                        double dsk = euclideanDistance(points.get(col), centers.get(s));
                        double ratio = Math.pow((dik/dsk), exponent);
                        denominator += ratio;
                    }
                    content[line][col] = 1.0/denominator;
                }else{
                    content[line][col] = 0.0;
                }
            }
        }

        return oldMatrix;
    }

    /**
     * Compute euclidian distance
     *
     * @param a
     * @param b
     * @return Euclidian distance between two points
     */
    public static double euclideanDistance(Point a, Point b) {
        double sum = 0.0;
        sum += Math.pow(a.getX() - b.getX(), 2);
        sum += Math.pow(a.getY() - b.getY(), 2);
        return Math.sqrt(sum);
    }

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
}
