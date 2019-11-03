package kmeans;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public class FuzzyKMeans {

    private int numOfClusters;
    //private int maxIterations;
    /** Fuzziness factor */
    private double fuzziness;
    private double epsilon;
    private int maxIterations;
    private int currentIteration;

    private List<Point> points;
    private List<Point> centers;

    private WeightsMatrix partition;
    private WeightsMatrix oldPartition;

    private boolean initialized;

    public FuzzyKMeans(List<Point> points, List<Point> centers) {
        this.points = points;
        this.centers = centers;
        this.numOfClusters = centers.size();
        // ak bol zadany len pocet centroidov, tak sa asi inicializuje nahodne. Ak mam prednastavene centroidy, tak to mozem inicializovat rovno vypocitanim prislusnosti
        this.partition = new WeightsMatrix(numOfClusters, points.size());
        initMissingParameters();
        oldPartition = this.partition.update(fuzziness, points, centers);
        this.currentIteration = 0;
    }

    public FuzzyKMeans(List<Point> points, List<Point> centers, double fuzziness, double epsilon) {
        this(points, centers);
        this.fuzziness = fuzziness;
        this.epsilon = epsilon;
    }

    public void execute() {
        while (!endCondition()) {
            makeStep();
        }
    }

    public void makeStep() {
        updateCentroids();
        oldPartition = this.partition.update(fuzziness, points, centers);
        currentIteration++;
    }

    private boolean endCondition() {
        double sum = 0.0;
        for(int line = 0; line < this.numOfClusters; line++){
            double[] matrixUCol = new double[this.points.size()];
            double[] oldMatrixUCol = new double[this.points.size()];

            for(int col = 0; col < this.points.size(); col++){
                matrixUCol[col] = this.partition.valueAt(line, col);
                oldMatrixUCol[col] = this.oldPartition.valueAt(line, col);
            }

            sum += WeightsMatrix.euclideanDistance(matrixUCol, oldMatrixUCol);
        }
        return (sum < this.epsilon) && this.currentIteration < this.maxIterations;
    }


    /**
     * Step 2: Calculate cluster centers
     */
    private void updateCentroids() {
        ArrayList<Point> list = new ArrayList<>(this.numOfClusters);

        for(int clusterCenter = 0; clusterCenter < this.numOfClusters; clusterCenter++){
            double sumX = 0.0, sumY = 0.0, amount = 0.0;

            for(int point = 0; point < this.points.size(); point++){
                sumX += Math.pow(this.partition.valueAt(clusterCenter, point), this.fuzziness) * this.points.get(point).getX();
                sumY += Math.pow(this.partition.valueAt(clusterCenter, point), this.fuzziness) * this.points.get(point).getY();

                amount += Math.pow(this.partition.valueAt(clusterCenter, point), this.fuzziness);
            }

            /* Create the new centroid */
            Point centroid = new Point(sumX/amount, sumY/amount);
            list.add(centroid);
        }
        /* Update list of centroids */
        this.centers = list;
    }

    /** There is some possibility, that some parameters will not be set in gui.
     *
     */
    private void initMissingParameters() {
        if (maxIterations == 0) {
            maxIterations = 100;
        }
        if (fuzziness == 0) {
            fuzziness = 2;
        }
        if (epsilon == 0) {
            epsilon = 0.0001;
        }
    }

    public List<Point> getCenters() {
        return this.centers;
    }

}
