package HomeWork3;

import javafx.util.Pair;
import weka.classifiers.Classifier;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;

import java.util.Comparator;
import java.util.PriorityQueue;

class DistanceCalculator {
    /**
     * We leave it up to you wheter you want the distance method to get all relevant
     * parameters(lp, efficient, etc..) or have it has a class variables.
     */
    public static double distance(Instance one, Instance two, int p, Knn.DistanceCheck distanceMethod, double threshold) {
        if (p < Integer.MAX_VALUE) {
            if (distanceMethod == Knn.DistanceCheck.Regular) {
                return lpDistance(one, two, p);
            }else {
                return efficientLpDisatnce(one, two, p, threshold);
            }
        } else {
            if (distanceMethod == Knn.DistanceCheck.Regular){
                return lpInfinityDistance(one, two);
            }else {
                return efficientLInfinityDistance(one, two, threshold);
            }

        }
    }

    /**
     * Returns the Lp distance between 2 instances.
     *
     * @param one
     * @param two
     */
    private static double lpDistance(Instance one, Instance two, int p) {
        //TODO: Check whether we should run on all attributes, or ignore last.
        double distance = 0;
        for (int i = 0; i < one.numAttributes() - 1; i++) {
            distance += Math.pow( Math.abs(one.value(i) - two.value(i)), p);
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
    private static double lpInfinityDistance(Instance one, Instance two) {
        double maxDistance = 0;
        double currentDistance;
        for (int i = 0; i < one.numAttributes() - 1; i++) {
            currentDistance = Math.abs(one.value(i) - two.value(i));
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
    private static double efficientLpDisatnce(Instance one, Instance two, int p, double thershold) {
        double distance = 0;
        for (int i = 0; i < one.numAttributes() - 1; i++) {
            distance += Math.pow( Math.abs(one.value(i) - two.value(i)), p);
            if (distance > thershold) {
                return Double.MAX_VALUE;
            }
        }
        distance = Math.pow(distance, 1.0 / p);
        return distance;
    }

    /**
     * Returns the Lp distance between 2 instances, while using an efficient distance check.
     *
     * @param one
     * @param two
     * @return
     */
    private static double efficientLInfinityDistance(Instance one, Instance two, double thershold) {
        double maxDistance = 0;
        double currentDistance;
        for (int i = 0; i < one.numAttributes() - 1; i++) {
            currentDistance = Math.abs(one.value(i) - two.value(i));
            maxDistance = currentDistance > maxDistance ? currentDistance : maxDistance;
            if (maxDistance > thershold) {
                return Double.MAX_VALUE;
            }
        }
        return maxDistance;
    }
}

public class Knn implements Classifier {




    public enum DistanceCheck {Regular, Efficient}
    public enum WeightingScheme {Uniform, Weighted}

    private Instances m_trainingInstances;
    private int k;
    private int lp;
    private DistanceCheck distanceMethod;
    private WeightingScheme scheme;

    public void setK(int k) {
        this.k = k;
    }

    public void setLp(int lp) {
        this.lp = lp;
    }

    public void setDistanceMethod (DistanceCheck distanceMethod){
        this.distanceMethod = distanceMethod;
    }

    public void setWeightingScheme (WeightingScheme scheme){
        this.scheme = scheme;
    }

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
        PriorityQueue<Pair<Double, Double>> nearest = findNearestNeighbors(instance);

        return this.scheme == WeightingScheme.Uniform ? getAverageValue(nearest) : getWeightedAverageValue(nearest);
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
     * @param instances    Instances used for the cross validation
     * @param num_of_folds The number of folds to use.
     * @return The cross validation error.
     */
    public double crossValidationError(Instances instances, int num_of_folds) {
        double avgError = 0;
        Instances[] splittedData = splitData(instances, num_of_folds);
        for (int i = 0; i < splittedData.length; i++) {
            m_trainingInstances = mergeData(splittedData, i);
            avgError += calcAvgError(splittedData[i]);
        }
        this.m_trainingInstances = instances;
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
            splittedData[i % num_of_folds].add(instances.get(i));
        }
        return splittedData;
    }

    /**
     * Finds the k nearest neighbors.
     *
     * @param instance
     */
    public PriorityQueue<Pair<Double, Double>> findNearestNeighbors(Instance instance) {
        Pair<Double, Double> pair;
        Comparator<Pair<Double, Double>> comp = (o1, o2) -> (int)(o2.getKey() - o1.getKey());
        PriorityQueue<Pair<Double, Double>> heap = new PriorityQueue(this.k, comp);
        Instance current;
        for (int j = 0; j < this.k; j++) {
            current = m_trainingInstances.get(j);
            heap.add(new Pair(DistanceCalculator.distance(instance, current, this.lp, this.distanceMethod, Double.MAX_VALUE), current.classValue()));
        }
        for (int i = this.k; i < m_trainingInstances.size(); i++) {
            current = m_trainingInstances.get(i);
            pair = new Pair<>(DistanceCalculator.distance(instance, current, this.lp, this.distanceMethod, heap.peek().getKey()), current.classValue());
            if (comp.compare(heap.peek(), pair) < 0) {
                heap.poll();
                heap.add(pair);
            }
        }
        return heap;
    }

    /**
     * Cacluates the average value of the given elements in the collection.
     *
     * @param
     * @return
     */
    public double getAverageValue(PriorityQueue<Pair<Double, Double>> Heap) {
        double result = 0;
        int size = Heap.size();
        while (Heap.size() > 0){
            result += Heap.poll().getValue();
        }
        result = result/size;
        return result;
    }

    /**
     * Calculates the weighted average of the target values of all the elements in the collection
     * with respect to their distance from a specific instance.
     *
     * @return
     */
    public double getWeightedAverageValue(PriorityQueue<Pair<Double, Double>> Heap) {
        double wi;
        double result = 0;
        int size = Heap.size();
        Pair<Double, Double> current;
        double dividor = 0;
        while (Heap.size() > 0){
            current = Heap.poll();
            if (current.getKey() != 0){
                wi = 1.0/Math.pow(current.getKey(), 2);
            }else {
                wi = Math.pow(10,-10);
            }
            result += current.getValue()*wi;
            dividor += wi;
        }

        result = result/dividor;
        return result;
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
