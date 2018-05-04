package HomeWork3;

import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainHW3 {


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
        int[] k_values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        int[] Lp_distance = {1, 2, 3, Integer.MAX_VALUE};
        int XFolds = 3;

        Knn.DistanceCheck[] distanceChecks = {Knn.DistanceCheck.Regular, Knn.DistanceCheck.Efficient};
        Knn.WeightingScheme[] schemes = {Knn.WeightingScheme.Weighted, Knn.WeightingScheme.Uniform};
        String[] dataset = {"original", "scaled"};
        Instances data = loadData("auto_price.txt");
        data.setClassIndex(data.numAttributes() - 1);
        int[] x_Folds = {data.size(), 50, 10, 5, 3};

        Instances scaledData = FeatureScaler.scaleData(data);
        scaledData.setClassIndex(scaledData.numAttributes() - 1);

        Knn knn = new Knn();
        knn.buildClassifier(data);
        double bestError = Double.MAX_VALUE;
        double currentError;
        int bestK = 0;
        int bestLP = 0;
        Knn.WeightingScheme majorityFunction = null;
        knn.setDistanceMethod(Knn.DistanceCheck.Efficient);
        long totalTime = 0;
        long startTime = 0;
        for (int l = 0; l < x_Folds.length; l++) {
            for (int i = 1; i < k_values.length; i++) {
                knn.setK(k_values[i]);
                for (int j = 0; j < Lp_distance.length; j++) {
                    knn.setLp(Lp_distance[j]);
                    for (int k = 0; k < schemes.length; k++) {
                        knn.setWeightingScheme(schemes[k]);
                        startTime = System.nanoTime();
                        currentError = knn.crossValidationError(scaledData, x_Folds[l]);
                        totalTime = System.nanoTime() - startTime;
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
            System.out.println("Results for " + x_Folds[l] + "folds:");
            System.out.println("--------------------------------------");
            System.out.println("Cross Validation error of regular knn on auto_price dataset is " + bestError);
            System.out.println("The average elapsed time is " + totalTime / x_Folds[l]);
            System.out.println("The total time elapsed time is " + totalTime);
        }
    }

}
