package kmeans;

import java.util.ArrayList;
import java.util.List;

public class FuzzyKMeans {

    private int numOfClusters;
    private double fuzziness;
    private double epsilon;
    private int maxIterations;
    private int currentIteration;

    private List<Point> points;
    private List<Point> centers;

    private WeightsMatrix partition;
    private WeightsMatrix oldPartition;

    public FuzzyKMeans(List<Point> points, List<Point> centers) {
        this.points = points;
        this.centers = centers;
        this.numOfClusters = centers.size();
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

    public FuzzyKMeans(List<Point> points, int numOfClusters, double fuzziness, double epsilon) {
        this.centers = generateRandomCenters(numOfClusters);
        this.points = points;
        this.numOfClusters = centers.size();
        this.partition = new WeightsMatrix(numOfClusters, points.size());
        initMissingParameters();
        oldPartition = this.partition.update(fuzziness, points, centers);
        this.currentIteration = 0;
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

            sum += Point.euclideanDistance(matrixUCol, oldMatrixUCol);
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
            Point centroid = new Point(sumX/amount, sumY/amount, 6);
            list.add(centroid);
        }
        /* Update list of centroids */
        this.centers = list;
    }

    /**
     * Check that all needed parameters are set or set them default values
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

    private List<Point> generateRandomCenters(int numOfClusters) {

        //maximum number
        int min = 0;
        //minimum number
        int max = 700;

        List<Point> centers = new ArrayList<>();
        for (int index = 0; index < numOfClusters; index++) {
            centers.add(new Point(min + Math.random() * (max - min), min + Math.random() * (max - min), 6));
        }

        return centers;
    }

    public WeightsMatrix getPartition() {
        return this.partition;
    }

}

