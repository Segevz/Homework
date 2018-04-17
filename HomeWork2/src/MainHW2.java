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
	 * @param fileName
	 * @return Instances data
	 * @throws IOException
	 */
	public static Instances loadData(String fileName) throws IOException{
		BufferedReader datafile = readDataFile(fileName);

		Instances data = new Instances(datafile);
		data.setClassIndex(data.numAttributes() - 1);
		return data;
	}
	
	public static void main(String[] args) throws Exception {
		Instances trainingCancer = loadData("cancer_train.txt");
		Instances testingCancer = loadData("cancer_test.txt");
		Instances validationCancer = loadData("cancer_validation.txt");
//		Instances tennisTraining = loadData("C:\\dev\\Homework\\HomeWork2\\tenis_train.txt");

        //TODO: complete the Main method

//		System.out.println(entropyTree.positivesRatio(testingCancer));
//		Instances[] splitData = entropyTree.splitData(trainingCancer, 0);
//		for (int i = 0; i < splitData.length; i++) {
//			System.out.println("For attribute value of" + trainingCancer.attribute(0).value(i));
//			for (int j = 0; j < splitData[i].size(); j++) {
//				System.out.println(splitData[i].get(j));
//			}
//		}
//		System.out.println(entropyTree.countClass(trainingCancer));
//		entropyTree.buildClassifier(trainingCancer);
//		System.out.println(entropyTree.calcAvgError(validationCancer));
//		System.out.println(entropyTree.toString());
		DecisionTree entropyTree = new DecisionTree();
		entropyTree.setImpurityMeasure(function.Entropy);
		entropyTree.setpValue(1);
		entropyTree.buildClassifier(trainingCancer);
		DecisionTree giniTree = new DecisionTree();
		giniTree.setImpurityMeasure(function.Gini);
		giniTree.setpValue(1);
		giniTree.buildClassifier(trainingCancer);
		double giniError = giniTree.calcAvgError(validationCancer);
		double entropyError = entropyTree.calcAvgError(validationCancer);
		DecisionTree mainTree = giniError > entropyError ? giniTree : entropyTree;
		System.out.println("Validation error using Entropy: " + entropyError );
		System.out.println("Validation error using Entropy: " + giniError);
		System.out.println("------------------------------------------------------------------");
		for (int i = 0; i < mainTree.pIndex.length; i++) {
			mainTree.setpValue(mainTree.pIndex[i]);
			System.out.println("Decision Tree with p_value of: " + mainTree.pIndex[i]);
			System.out.println("The training error of the decision tree is: " + mainTree.calcAvgError(trainingCancer));
			System.out.println("Max height on validation data: " + 0);
		}
	}
}
