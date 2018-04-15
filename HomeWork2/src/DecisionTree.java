import weka.classifiers.Classifier;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;

class Node {
	Node[] children;
	Node parent;
	int attributeIndex;
	double returnValue;
	int dataSize;
}

public class DecisionTree implements Classifier {
	private Node rootNode;

	@Override
	public void buildClassifier(Instances arg0) throws Exception {

	}

	private void buildTree(Instances data) {

	}

	private Node buildGiniTree(Instances data) {
		return null;
	}

	private Node buildEntropyTree(Instances data) {
		Node root = new Node();


		return root;
	}

	private double calcInformationGain(Instances data, int attributeIndex){
		return 0;
	}

	private double calcGiniGain(Instances data, int attributeIndex){
		return 0;
	}

	private double calcGini(double p1, double p2) {
		return 1 - (p1 * p1 + p2 * p2);
	}

	private double calcEntropy(double p1, double p2){
		return -(p1 * Math.log(p1)/Math.log(2) + p2 * Math.log(p2)/Math.log(2));
	}

	private Instances[] splitDataByAttribute(Instances data, int attributeIndex) {
		return null;
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

	private double calcAvgError(Instances data) {
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
