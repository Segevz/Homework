package HomeWork3;

import weka.classifiers.Classifier;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Queue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

class DistanceCalculator {
    /**
     * We leave it up to you wheter you want the distance method to get all relevant
     * parameters(lp, efficient, etc..) or have it has a class variables.
     */
    public static double distance(Instance one, Instance two) {

        return 0.0;
    }

    /**
     * Returns the Lp distance between 2 instances.
     *
     * @param one
     * @param two
     */
    private double lpDistance(Instance one, Instance two, int p) {
        //TODO: Check whether we should run on all attributes, or ignore last.
        double distance = 0;
        for (int i = 0; i < one.numAttributes() - 1; i++) {
            distance += Math.pow(one.value(i) - one.value(i), p);
        }
        distance = Math.pow(distance, 1.0 / p);
        return distance;
    }

    /**
     * Returns the L infinity distance between 2 instances.
     *
     * @param one
     * @param two
     * @return
     */
    private double lInfinityDistance(Instance one, Instance two) {
        double maxDistance = 0;
        double currentDistance;
        for (int i = 0; i < one.numAttributes() - 1; i++) {
            currentDistance = Math.abs(one.value(i) - two.value(i));
//            if (currentDistance > maxDistance){
//                maxDistance = currentDistance;
//            }
            maxDistance = currentDistance > maxDistance ? currentDistance : maxDistance;
        }
        return maxDistance;
    }

    /**
     * Returns the Lp distance between 2 instances, while using an efficient distance check.
     *
     * @param one
     * @param two
     * @return
     */
    private double efficientLpDisatnce(Instance one, Instance two) {
        return 0.0;
    }

    /**
     * Returns the Lp distance between 2 instances, while using an efficient distance check.
     *
     * @param one
     * @param two
     * @return
     */
    private double efficientLInfinityDistance(Instance one, Instance two) {
        return 0.0;
    }
}

public class Knn implements Classifier {

    public enum DistanceCheck {Regular, Efficient}

    private Instances m_trainingInstances;
    private int p;
    private int k;

    @Override
    /**
     * Build the knn classifier. In our case, simply stores the given instances for 
     * later use in the prediction.
     * @param instances
     */
    public void buildClassifier(Instances instances) throws Exception {
        m_trainingInstances = instances;
    }

    /**
     * Returns the knn prediction on the given instance.
     *
     * @param instance
     * @return The instance predicted value.
     */
    public double regressionPrediction(Instance instance) {
        Collection nearest = findNearestNeighbors(instance);
        double result = getWeightedAverageValue(nearest);
        double divider = 0;
        Instance current;
        for (Iterator iteraor : nearest){
            divider += DistanceCalculator.distance(instance, iteraor.next());
        }
        return result/divider;
    }

    /**
     * Caclcualtes the average error on a give set of instances.
     * The average error is the average absolute error between the target value and the predicted
     * value across all insatnces.
     *
     * @param instances
     * @return
     */
    public double calcAvgError(Instances instances) {
        double avgError = 0;
        double instancePrediction;
        int totalInstances = instances.size();
        Instance currentInstance;

        for (int i = 0; i < totalInstances; i++) {
            currentInstance = instances.get(i);
            instancePrediction = regressionPrediction(currentInstance);
            avgError += Math.abs(instancePrediction - currentInstance.classValue());
        }
        avgError = avgError / totalInstances;
        return avgError;
    }

    /**
     * Calculates the cross validation error, the average error on all folds.
     *
     * @param Instances    Instances used for the cross validation
     * @param num_of_folds The number of folds to use.
     * @return The cross validation error.
     */
    public double crossValidationError(Instances Instances, int num_of_folds) {
        double avgError = 0;
        Instances[] splittedData = splitData(Instances, num_of_folds);
        for (int i = 0; i < splittedData.length; i++) {
            m_trainingInstances = mergeData(splittedData, i);
            avgError += calcAvgError(splittedData[i]);
        }
        return avgError / num_of_folds;
    }

    public Instances mergeData(Instances[] splittedData, int indexToIgnore) {
        Instances mergedData = new Instances(splittedData[0], 0, 0);
        for (int i = 0; i < splittedData.length; i++) {
            if (i != indexToIgnore) {
                addSubsetInstances(mergedData, splittedData[i]);
            }
        }
        return mergedData;
    }

    public void addSubsetInstances(Instances instances, Instances splittedData) {
        for (int i = 0; i < splittedData.size(); i++) {
            instances.add(splittedData.get(i));
        }
    }

    public Instances[] splitData(Instances instances, int num_of_folds) {
        int sizeOfEachSubset = instances.size() / num_of_folds;
        Instances[] splittedData = new Instances[num_of_folds];

        for (int i = 0; i < splittedData.length; i++) {
            splittedData[i] = new Instances(instances, 0, 0);
        }
        for (int i = 0; i < instances.size(); i++) {
            splittedData[i].add(instances.get(i % num_of_folds));
        }
        return splittedData;
    }

    /**
     * Finds the k nearest neighbors.
     *
     * @param instance
     */
    public Collection/* Collection of your choice */ findNearestNeighbors(Instance instance) {
        return null;
    }

    /**
     * Cacluates the average value of the given elements in the collection.
     *
     * @param
     * @return
     */
    public double getAverageValue(/* Collection of your choice */) {
        return 0.0;
    }

    /**
     * Calculates the weighted average of the target values of all the elements in the collection
     * with respect to their distance from a specific instance.
     *
     * @return
     */
    public double getWeightedAverageValue(/* Collection of your choice */) {
        return 0.0;
    }


    @Override
    public double[] distributionForInstance(Instance arg0) throws Exception {
        // TODO Auto-generated method stub - You can ignore.
        return null;
    }

    @Override
    public Capabilities getCapabilities() {
        // TODO Auto-generated method stub - You can ignore.
        return null;
    }

    @Override
    public double classifyInstance(Instance instance) {
        // TODO Auto-generated method stub - You can ignore.
        return 0.0;
    }
}
