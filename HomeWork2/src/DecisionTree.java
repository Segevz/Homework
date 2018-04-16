import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemoveWithValues;

enum function {
    Gini,
    Entropy
}

class Node {
    Node[] children;
    Node parent;
    int attributeIndex;
    double returnValue;
}

public class DecisionTree implements Classifier {
    private Node rootNode;
    private function impurityMeasure;

    public void setImpurityMeasure(function mode) {
        this.impurityMeasure = mode;
    }

    @Override
    public void buildClassifier(Instances arg0) throws Exception {
        rootNode = new Node();
        rootNode.returnValue = calcReturnValue(arg0);
        buildTree(arg0, rootNode);
    }

    private void buildTree(Instances data, Node current) {
        if (checkDichotomy(data) || data.numAttributes() < 2) {
            return;
        }
        double maxGain = 0, currentGain;
        int bestAttribute = -1;
        for (int i = 0; i < data.numAttributes() - 1; i++) {
            currentGain = calcGain(data, i, impurityMeasure);
            if (currentGain > maxGain) {
                maxGain = currentGain;
                bestAttribute = i;
            }
        }
        current.attributeIndex = bestAttribute;
        System.out.println(bestAttribute);
        makeChildren(data, current, impurityMeasure);
    }

    private void buildTree(Instances data) {
        double entropyError, giniError;
        Node rootEntropy = new Node();
        Node rootGini = new Node();
//		buildEntropyTree(data, rootEntropy);
//		buildGiniTree(data, rootGini);
        rootNode = rootEntropy;
        entropyError = calcAvgError(data);
        rootNode = rootGini;
        giniError = calcAvgError(data);
        rootNode = giniError > entropyError ? rootGini : rootEntropy;

    }

    private boolean checkDichotomy(Instances data) {
        Instance previous = data.get(0);
        Instance current;
        for (int i = 1; i < data.size(); i++) {
            current = data.get(i);
            if (current.classValue() != previous.classValue()) {
                return false;
            }
            previous = current;
        }
        return true;
    }

    private void makeChildren(Instances data, Node parent, function mode) {
        if (parent.attributeIndex == -1) {
            return;
        }
        Instances[] subsetsByAttribute = splitData(data, parent.attributeIndex);
        parent.children = new Node[data.attribute(parent.attributeIndex).numValues()];
        for (int i = 0; i < data.attribute(parent.attributeIndex).numValues(); i++) {
            if (subsetsByAttribute[i] != null) {
                parent.children[i] = new Node();
                parent.children[i].parent = parent;
                parent.children[i].returnValue = calcReturnValue(subsetsByAttribute[i]);
                subsetsByAttribute[i].deleteAttributeAt(parent.attributeIndex);
                buildTree(subsetsByAttribute[i], parent.children[i]);
            }
        }
    }

    private double calcInformationGain(Instances data, int attributeIndex) {
        return 0;
    }

    private double calcGain(Instances data, int attributeIndex, function mode) {
        switch (mode) {
            case Gini:
                return calcGiniGain(data, attributeIndex);
            case Entropy:
                return calcInfoGain(data, attributeIndex);
        }
        return -1;
    }

    private double calcGiniGain(Instances data, int attributeIndex) {
        return 0;
    }

    private double calcGini(double p1, double p2) {
        return 1 - (p1 * p1 + p2 * p2);
    }

    private void findBestInfoGain(Node node, Instances data) {
        double maxGain = 0, currentGain;
        int bestAttributeIndex = 0;
//		double firstEntropy = calcFirstEntropy(data);
        for (int i = 0; i < data.numAttributes() - 1; i++) {
            //TODO: write a function for calculating entropy for root node
//			currentGain = calcInfoGain(data, firstEntropy, i);
//			if (currentGain > maxGain){
//				maxGain = currentGain;
//				bestAttributeIndex = i;
//			}
//		}
//		node.attributeIndex = bestAttributeIndex;
        }
    }

    private double calcInfoGain(Instances data, int indexAttribute) {
        Attribute attribute = data.attribute(indexAttribute);
        double positivesRatio = positivesRatio(data);
        double gain = entropyFormula(positivesRatio(data));
        Instances[] subsets = splitData(data, indexAttribute);
        for (int i = 0; i < attribute.numValues(); i++) {
            if (subsets[i] != null) {
//                System.out.println("|S" + i + "|/|S| is :" + (double)subsets[i].size() / data.size());
//                System.out.println("H(S" + i + ") is " + entropyFormula(positivesRatio(subsets[i])));
                //System.out.println(((double)subsets[i].size() / data.size()) * entropyFormula(positivesRatio(subsets[i])));
                gain -= ((double)subsets[i].size() / data.size()) * entropyFormula(positivesRatio(subsets[i]));
            }
        }
        return gain;
    }

    public double positivesRatio(Instances data) {
        int countPositives = 0;
        for (int i = 0; i < data.size(); i++) {
//            if (data.get(i).classAttribute().equals(data.classAttribute().value(0))) {
            if (data.get(i).stringValue(data.classAttribute()).equals(data.classAttribute().value(0))) {
                countPositives++;
            }
        }
        return (double) countPositives / data.size();
    }

    private double calcEntropy(Instances data, String attributeValue, int attributeIndex) {
        int dataSize = data.size();
        int classIndex = data.classIndex();
        Instance currentInstace;
//        for (int i = 0; i < dataSize; i++) {
//            currentInstace = data.get(i);
//            if (currentInstace.stringValue(attributeIndex).equals(attributeValue)) {
//                if (currentInstace.stringValue(classIndex).equals("recurrence-events")) {
//                    hasCancer++;
//                }
//            }
//        }

        return entropyFormula(positivesRatio(data));
    }

    public int countClass(Instances data) {
        int classcount = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).classValue() == 0) {
                classcount++;
            }
        }
        return classcount;
    }

    private double entropyFormula(double p) {
        if (p == 0) {
            return 1;
        }
        return -(p * Math.log(p) / Math.log(2) + (1 - p) * Math.log(1 - p) / Math.log(2));
    }

    private Instances[] splitData(Instances data, int attributeIndex) {
        RemoveWithValues splitFilter = new RemoveWithValues();

        String[] options = new String[4];
        Instances[] splitData = new Instances[data.attribute(attributeIndex).numValues()];


        for (int i = 0; i < data.attribute(attributeIndex).numValues(); i++) {
            try {
                options[0] = "-C";   // Choose attribute to be used for selection
                options[1] = String.valueOf(attributeIndex+1); // Attribute number
                options[2] = "-L";
                options[3] = String.valueOf(i+1);
                splitFilter.setInvertSelection(true);
                splitFilter.setOptions(options);
                splitFilter.setInputFormat(data);
                splitData[i] = Filter.useFilter(data, splitFilter);
//                splitData[i].deleteAttributeAt(attributeIndex);
                System.out.println(splitData[i].size());
            } catch (Exception e) {
                System.err.println("[splitData] Error : " + e);
            }
        }
        return splitData;
    }


    @Override
    public double classifyInstance(Instance instance) {
        Node current = rootNode;
        double valueIndex;
        while (current.children != null) {
            valueIndex = instance.value(current.attributeIndex);
            current = current.children[(int) valueIndex];
        }
        return current.returnValue;
    }


    private double calcReturnValue(Instances data) {
        int counter = 0;
        for (int i = 0; i < data.size(); i++) {
            counter += data.get(i).classValue() * 2 - 1;
        }
        return counter > 0 ? 1 : 0;
    }

    public double calcAvgError(Instances data) {
        double countErrors = 0;
        for (int i = 0; i < data.size(); i++) {
            countErrors += classifyInstance(data.get(i)) == data.get(i).classValue() ? 1 : 0;
        }
        return countErrors / data.size();
    }

    @Override
    public double[] distributionForInstance(Instance arg0) throws Exception {
        // Don't change
        return null;
    }

    @Override
    public Capabilities getCapabilities() {
        // Don't change
        return null;
    }

}
