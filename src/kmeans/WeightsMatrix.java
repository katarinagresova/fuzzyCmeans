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
     * Computes new partition values
     * @param fuzziness fuzziness coefficient
     * @param points    points that are clustered
     * @param centers   centers of clusters
     * @return          old matrix
     */
    public WeightsMatrix update(double fuzziness, List<Point> points, List<Point> centers) {

        WeightsMatrix oldMatrix = new WeightsMatrix(this);

        double exponent = 2.0 / (fuzziness - 1);
        /* loop over clusters */
        for(int line = 0; line < centers.size(); line++){
            /* loop over features */
            for(int col = 0; col < points.size(); col++){
                double denominator = 0.0;
                /* dik = d(xk - vi) */
                double dik = Point.euclideanDistance(points.get(col), centers.get(line));
                if(dik != 0.0){
                    for(int s = 0; s < centers.size(); s++){
                        double dsk = Point.euclideanDistance(points.get(col), centers.get(s));
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
}
