package HomeWork2;

import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class MainHW2 {

    public static BufferedReader readDataFile(String filename) {
        BufferedReader inputReader = null;

        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }

        return inputReader;
    }

    /**
     * Sets the class index as the last attribute.
     *
     * @param fileName
     * @return Instances data
     * @throws IOException
     */
    public static Instances loadData(String fileName) throws IOException {
        BufferedReader datafile = readDataFile(fileName);

        Instances data = new Instances(datafile);
        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }

    public static void main(String[] args) throws Exception {
        //Load Data
        Instances trainingCancer = loadData("cancer_train.txt");
        Instances testingCancer = loadData("cancer_test.txt");
        Instances validationCancer = loadData("cancer_validation.txt");
        //Create entropy & gini trees (pValue = 1)
        DecisionTree entropyTree = new DecisionTree();
        entropyTree.setImpurityMeasure(impMeasure.Entropy);
        entropyTree.setpValue(1);
        entropyTree.buildClassifier(trainingCancer);
        DecisionTree giniTree = new DecisionTree();
        giniTree.setImpurityMeasure(impMeasure.Gini);
        giniTree.setpValue(1);
        giniTree.buildClassifier(trainingCancer);
        //Calculate error for both trees
        double giniError = giniTree.calcAvgError(validationCancer);
        double entropyError = entropyTree.calcAvgError(validationCancer);
        double validationError;
        double bestError = Double.MAX_VALUE;
        double bestPValue = 0;
        //Choose better error tree
        DecisionTree mainTree = giniError < entropyError ? giniTree : entropyTree;
        System.out.println("Validation error using Entropy: " + entropyError);
        System.out.println("Validation error using Gini: " + giniError);
        System.out.println("------------------------------------------------------------------");

        //Find best lowest error according to given pValues, prints the results.
        for (int i = 0; i < mainTree.pIndex.length; i++) {
            mainTree.setpValue(mainTree.pIndex[i]);
            mainTree.buildClassifier(trainingCancer);
            System.out.println("Decision Tree with p_value of: " + mainTree.pIndex[i]);
            System.out.println("The train error of the decision tree is: " + mainTree.calcAvgError(trainingCancer));
            validationError = mainTree.calcAvgError(validationCancer);
            System.out.println("Max height on validation data: " + (mainTree.getMaxDepth()));
            System.out.println("Average height on validation data: " + mainTree.getAverageDepth());
            if (validationError < bestError) {
                bestError = validationError;
                bestPValue = mainTree.pIndex[i];
            }
            System.out.println("The validation error of the decision tree is: " + validationError);
            System.out.println("------------------------------------------------------------------");
        }
        System.out.println("Best valdiation error at p_value = " + bestPValue);
        mainTree.setpValue(bestPValue);
        mainTree.buildClassifier(trainingCancer);
        System.out.println("Test error with best tree: " + mainTree.calcAvgError(testingCancer));
        System.out.println("------------------------------------------------------------------");
        mainTree.printTree();
    }
}
