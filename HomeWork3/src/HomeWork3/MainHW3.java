package HomeWork3;

import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainHW3 {

    private static int[] k_values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
    private static int[] Lp_distance = {1, 2, 3, Integer.MAX_VALUE};
    private static int XFolds = 10;
    private static String[] dataset = {"original", "scaled"};
    private static Knn.DistanceCheck[] distanceChecks = {Knn.DistanceCheck.Regular, Knn.DistanceCheck.Efficient};
    private static Knn.WeightingScheme[] schemes = {Knn.WeightingScheme.Weighted, Knn.WeightingScheme.Uniform};

    public static BufferedReader readDataFile(String filename) {
        BufferedReader inputReader = null;

        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }

        return inputReader;
    }

    public static Instances loadData(String fileName) throws IOException {
        BufferedReader datafile = readDataFile(fileName);
        Instances data = new Instances(datafile);
        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }

    public static void main(String[] args) throws Exception {
        //TODO: complete the Main method

        Instances[] data = new Instances[2];
        data[0] = loadData("auto_price.txt");
        data[1] = FeatureScaler.scaleData(data[0]);

        int[] x_Folds = {data[0].size(), 50, 10, 5, 3};
        Knn knn = new Knn();

        double bestError = Double.MAX_VALUE;
        double currentError;
        int bestK = 0;
        int bestLP = 0;
        Knn.WeightingScheme majorityFunction = null;
        long totalTime = 0;
        long startTime = 0;

        for (int l = 0; l < dataset.length; l++) {
            bestError = Double.MAX_VALUE;
            knn.buildClassifier(data[l]);
            for (int i = 0; i < k_values.length; i++) {
                knn.setK(k_values[i]);
                for (int j = 0; j < Lp_distance.length; j++) {
                    knn.setLp(Lp_distance[j]);
                    for (int k = 0; k < schemes.length; k++) {
                        knn.setWeightingScheme(schemes[k]);
                        currentError = knn.crossValidationError(data[l], XFolds);
                        if (currentError < bestError) {
                            bestError = currentError;
                            bestK = k_values[i];
                            bestLP = Lp_distance[j];
                            majorityFunction = schemes[k];
                        }
                    }
                }
            }
            System.out.println("--------------------------------------");
            System.out.println("Results for " + dataset[l] + " dataset:");
            System.out.println("--------------------------------------");
            System.out.println("Cross validation error with K = " + bestK + ", lp = " + bestLP + ", majority function");
            System.out.println("= " + majorityFunction + " for auto_price data is: " + bestError);
        }
        System.out.println();

        // Sets the values found in the previous stage in our knn algorithm
        knn.setK(bestK);
        knn.setLp(bestLP);
        knn.setWeightingScheme(majorityFunction);
        for (int l = 0; l < x_Folds.length; l++) {
            System.out.println("--------------------------------------");
            System.out.println("Results for " + x_Folds[l] + " folds:");
            System.out.println("--------------------------------------");
            for (int m = 0; m < distanceChecks.length; m++) {
                knn.setDistanceMethod(distanceChecks[m]);
                startTime = System.nanoTime();
                currentError = knn.crossValidationError(data[1], x_Folds[l]);
                totalTime = System.nanoTime() - startTime;
                if (currentError < bestError) {
                    bestError = currentError;
                }
                printResults(x_Folds[l], totalTime, bestError, distanceChecks[m]);
            }
        }
    }


    private static void printResults(int x_Folds, long totalTime, double bestError, Knn.DistanceCheck distanceCheck) {
        String distanceMethod = distanceCheck == Knn.DistanceCheck.Efficient ? "efficient" : "regular";

        System.out.println("Cross validation error of " + distanceMethod + " knn on auto_price dataset is " + bestError + " and");
        System.out.println("the average elapsed time is " + totalTime / x_Folds);
        System.out.println("The total elapsed time is " + totalTime);
        System.out.println();
    }

}
