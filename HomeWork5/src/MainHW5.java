import java.io.*;
import java.util.Random;

import sun.awt.SunHints;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;

public class MainHW5 {

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
        Instances instances = loadData("/Users/nmiran/Documents/Repositories/Homework/HomeWork5/src/cancer.txt");
        Instances[] splitted = splitData(instances);
        Instances trainingData = splitted[0];
        Instances testData = splitted[1];

        double[] values;
        double truePositive, falsePositive;
        double polyBestValue, RBFBestValue;
        double alpha = 1.5;

        SVM svm = new SVM();

        Kernel bestKernel = null;
        double currentKernelValue, bestKernelValue = 0, bestTPR = 0, bestFPR = 0, bestParameter = 0;

        // Calculate PolyKernel values
        PolyKernel polyKernel = new PolyKernel();
        svm.setKernel(polyKernel);
        for (int i = 2; i <= 4; i++) {
            polyKernel.setExponent(i);
            svm.buildClassifier(trainingData);
            values = svm.calculateKernelValue(testData);
            System.out.println("For PolyKernel with degree " + i + " the rates are:");
            System.out.println("TPR = " + values[0]);
            System.out.println("FPR = " + values[1]);
            printSeperator();
            currentKernelValue = alpha * (values[0] - values[1]);

            if (bestKernelValue < currentKernelValue) {
                bestKernelValue = currentKernelValue;
                bestKernel = polyKernel;
                bestTPR = values[0];
                bestFPR = values[1];
                bestParameter = i;
            }

        }

        // Calculate RBFKernel values
        double gamma;
        RBFKernel RBFkernel = new RBFKernel();
        svm.setKernel(RBFkernel);
        for (int i = 0; i < 3; i++) {
            gamma = 0.5 / Math.pow(10, i);
            RBFkernel.setGamma(gamma);
            svm.m_smo.buildClassifier(trainingData);
            values = svm.calculateKernelValue(testData);
            System.out.println("For RBFKernel with gamma " + gamma + " the rates are:");
            System.out.println("TPR = " + values[0]);
            System.out.println("FPR = " + values[1]);
            printSeperator();
            currentKernelValue = alpha * (values[0] - values[1]);
            if (bestKernelValue < currentKernelValue) {
                bestKernelValue = currentKernelValue;
                bestKernel = RBFkernel;
                bestTPR = values[0];
                bestFPR = values[1];
                bestParameter = gamma;
            }
        }

        String kernelType = (bestKernel instanceof PolyKernel) ? "Polynomial Kernel" : "RBF Kernel";
        System.out.println("The best kernel is: " + kernelType + " with parameter: " + bestParameter);
        System.out.println("TPR = " + bestTPR);
        System.out.println("FPR = " + bestFPR);
        printSeperator();

        svm.setKernel(bestKernel);

        // finding best C value
        double cValue;

        for (int i = 1; i >= -4; i--) {
            for (int j = 3; j >= 1; j--) {
                cValue = Math.pow(10, i) * ((double) j / 3);
                svm.setC(cValue);
                svm.m_smo.buildClassifier(trainingData);
                values = svm.calculateKernelValue(testData);
                System.out.println("For C  " + cValue + " the rates are:");
                System.out.println("TPR = " + values[0]);
                System.out.println("FPR = " + values[1]);
                printSeperator();
            }
        }
    }


    public static Instances[] splitData(Instances instances) {
        Instances[] splittedData = new Instances[2];
        int instanceAmountForTraining = (int) (instances.size() * 0.8);
        for (int i = 0; i < splittedData.length; i++) {
            splittedData[i] = new Instances(instances, 0, 0);
        }
        for (int i = 0; i < instances.size(); i++) {
//            if (i < instanceAmountForTraining) {
            if (i % 5 != 0) {
                splittedData[0].add(instances.get(i));
            } else {
                splittedData[1].add(instances.get(i));
            }
        }
        return splittedData;
    }


    public static Instances[] splitDataRandomly(Instances instances) {
//		instances.randomize(new java.util.Random(0));
        Instances[] splittedData = new Instances[2];
        int trainSize = (int) (instances.size() * 0.8);
        int testSize = instances.size() - trainSize;
        splittedData[0] = new Instances(instances, 0, trainSize);
        splittedData[1] = new Instances(instances, trainSize, testSize);
        splittedData[0].randomize(new Random(0));
        splittedData[1].randomize(new Random(0));
        return splittedData;
    }

    public static void printSeperator() {
        System.out.println("-------------------------");
    }
}
