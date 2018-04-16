import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
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
	int dataSize;
	function impurityMeasure;
}

public class DecisionTree implements Classifier {
	private Node rootNode;

	@Override
	public void buildClassifier(Instances arg0) throws Exception {
		double entropyError, giniError;
		Node entropyRoot = new Node();
		Node giniRoot = new Node();
		entropyRoot.dataSize = giniRoot.dataSize = arg0.size();
		entropyRoot.returnValue = giniRoot.returnValue = calcReturnValue(arg0);
		entropyRoot.impurityMeasure = function.Entropy;
		giniRoot.impurityMeasure = function.Gini;
		BuildTree(arg0, entropyRoot);
		BuildTree(arg0, giniRoot);
		rootNode = entropyRoot;
		entropyError = calcAvgError(arg0);
		rootNode = giniRoot;
		giniError = calcAvgError(arg0);
		rootNode = giniError < entropyError ? giniRoot : entropyRoot;
	}

	private void BuildTree(Instances data, Node current) {
		if (!checkDichotomy(data) || data.numAttributes() < 2){
			return;
		}
		double maxGain = 0, currentGain;
		int bestAttribute = -1;
		for (int i = 0; i < data.numAttributes() - 1; i++) {
			currentGain = calcGain(data, i, current.impurityMeasure);
			if (currentGain > maxGain) {
				maxGain = currentGain;
				bestAttribute = i;
			}
		}
		current.attributeIndex = bestAttribute;
		makeChildren(data, current, current.impurityMeasure);
	}

	private void buildTree(Instances data) {
		double entropyError, giniError;
		Node rootEntropy = new Node();
		Node rootGini = new Node();
		buildEntropyTree(data, rootEntropy);
		buildGiniTree(data, rootGini);
		rootNode = rootEntropy;
		entropyError = calcAvgError(data);
		rootNode = rootGini;
		giniError = calcAvgError(data);
		rootNode = giniError > entropyError ? rootGini : rootEntropy;

	}

	private void buildGiniTree(Instances data, Node current) {
		if (!checkDichotomy(data) || data.numAttributes() < 2){
			return;
		}
		int bestAttribute = -1;
		double maxGain = 0, currentGain;
		for (int i = 0; i < data.numAttributes() - 1; i++) {
			currentGain = calcGiniGain(data, i);
			if (currentGain > maxGain) {
				maxGain = currentGain;
				bestAttribute = i;
			}
		}
		current.attributeIndex = bestAttribute;
		makeChildren(data, current, function.Gini);
	}


	private void buildEntropyTree(Instances data, Node current) {
		if (!checkDichotomy(data) || data.numAttributes() < 2){
			return;
		}
		int bestAttribute = -1;
		double maxGain = 0, currentGain;
		for (int i = 0; i < data.numAttributes() - 1; i++) {
			currentGain = calcInformationGain(data, i);
			if (currentGain > maxGain) {
				maxGain = currentGain;
				bestAttribute = i;
			}
		}
		current.attributeIndex = bestAttribute;
		makeChildren(data, current, function.Entropy);
	}

	private boolean checkDichotomy(Instances data) {
		Instance previous = data.get(0);
		Instance current;
		for (int i = 1; i < data.size(); i++) {
			current = data.get(i);
			if (current.classValue() != previous.classValue()){
				return false;
			}
		}
		return true;
	}

	private void makeChildren(Instances data, Node parent, function mode ){
		if (parent.attributeIndex == -1){
			return;
		}
		Instances[] subsetsByAttribute = splitDataByAttribute(data, parent.attributeIndex);
		for (int i = 0; i < data.attribute(parent.attributeIndex).numValues(); i++) {
			parent.children[i] = new Node();
			parent.children[i].parent = parent;
			parent.children[i].returnValue = calcReturnValue(subsetsByAttribute[i]);
			parent.children[i].dataSize = subsetsByAttribute[i].size();
			BuildTree(subsetsByAttribute[i], parent.children[i]);
		}
	}

	private double calcInformationGain(Instances data, int attributeIndex){
		return 0;
	}

	private double calcGain(Instances data, int attributeIndex, function mode){
		switch (mode) {
			case Gini:
				return calcGiniGain(data, attributeIndex);
			case Entropy:
				return calcInformationGain(data, attributeIndex);
		}
		return -1;
	}

	private double calcGiniGain(Instances data, int attributeIndex){
		return 0;
	}

	private double calcGini(double p1, double p2) {
		return 1 - (p1 * p1 + p2 * p2);
	}

	private void findBestInfoGain (Node node, Instances data){
		double maxGain = 0, currentGain;
		int bestAttributeIndex = 0;
		double firstEntropy = calcFirstEntropy(data);
		for (int i = 0; i < data.numAttributes() - 1; i++) {
			//TODO: write a function for calculating entropy for root node
			currentGain = calcInfoGain(data, firstEntropy, i);
			if (currentGain > maxGain){
				maxGain = currentGain;
				bestAttributeIndex = i;
			}
		}
		node.attributeIndex = bestAttributeIndex;
	}

	private double calcInfoGain(Instances data, double entropy, int indexAttribute) {
		Instances subset;
		Attribute attribute = data.attribute(indexAttribute);
		double gain = entropy;
		for (int i = 0; i < attribute.numValues(); i++) {
			//TODO: fix split function
			subset = splitDataByAttribute(data, indexAttribute, i);
			gain -= (subset.size() / data.size()) * calcEntropy(subset, attribute.value(i), indexAttribute);
		}
		return gain;
	}
	private double calcEntropy(Instances data, String attributeValue, int attributeIndex) {
		int dataSize = data.size();
		int classIndex = data.classIndex();
		double hasCancer = 0, noCancer = 0;
		Instance currentInstace;
		for (int i = 0; i < dataSize; i++) {
			currentInstace = data.get(i);
			if (currentInstace.stringValue(attributeIndex).equals(attributeValue)) {
				if (currentInstace.stringValue(classIndex).equals("recurrence-events")) {
					hasCancer++;
				} else {
					noCancer++;
				}
			}
		}
		return entropyFormula(hasCancer / dataSize, noCancer / dataSize);
	}

	private double entropyFormula(double p1, double p2){
		return -(p1 * Math.log(p1)/Math.log(2) + p2 * Math.log(p2)/Math.log(2));
	}

	private Instances[] splitDataByAttribute(Instances data, int attributeIndex) {
		return null;
	}

	private Instances[] splitData(Instances data, int attributeIndex) {
		RemoveWithValues splitFilter = new RemoveWithValues();

		String valueIndex = "";
		String[] options = new String[5];
		Instances[] splitData = new Instances[data.attribute(attributeIndex).numValues()];
		options[0] = "-C";   // Choose attribute to be used for selection
		options[1] =Integer.toString(attributeIndex); // Attribute number
		options[2] = "-L";
		options[4] = "-V";

		for (int i = 0; i < data.attribute(attributeIndex).numValues(); i++) {
			options[3] = Integer.toString(i);
			try {
				splitFilter.setOptions(options);
				splitFilter.setInputFormat(data);
				splitData[i] = splitFilter.useFilter(data, splitFilter);
				splitData[i].deleteAttributeAt(attributeIndex);
			} catch (Exception e) {
				System.err.println("Error : " + e);
			}
		}
		return splitData;
	}

	private String makeRange(int n) {
		String result = "0";
		for (int i = 0; i < n; i++) {
			result += "," + Integer.toString(i);
		}
		return result;
	}


    @Override
	public double classifyInstance(Instance instance) {
		Node current = rootNode;
		double valueIndex;
		while(current.children != null) {
			valueIndex = instance.value(current.attributeIndex);
			current = current.children[(int)valueIndex];
		}
		return current.returnValue;
    }


    private double calcReturnValue(Instances data){
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
