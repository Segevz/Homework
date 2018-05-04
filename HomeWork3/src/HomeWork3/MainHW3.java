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
		int [] k_values = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		int [] Lp_distance = {1,2,3, Integer.MAX_VALUE};
		Knn.WeightingScheme[] schemes = {Knn.WeightingScheme.Weighted, Knn.WeightingScheme.Uniform};
		int XFolds = 10;
		String[] dataset = {"original", "scaled"};
		Instances data = loadData("auto_price.txt");
		Instances scaledData = FeatureScaler.scaleData(data);

		Knn knn = new Knn ();
		knn.buildClassifier(data);
		double bestError = Double.MAX_VALUE;
		double currentError;
		int bestK=0;
		int bestLP=0;
		Knn.WeightingScheme majorityFunction = null;
		knn.setDistanceMethod(Knn.DistanceCheck.Regular);

		for (int i = 1; i < k_values.length; i++) {
			knn.setK(k_values[i]);
			for (int j = 0; j < Lp_distance.length; j++) {
				knn.setLp(Lp_distance[j]);
				for (int k = 0; k < schemes.length; k++) {
					knn.setWeightingScheme(schemes[k]);

					currentError = knn.crossValidationError(scaledData, XFolds);
					if (currentError < bestError){
						bestError = currentError;
						bestK = k_values[i];
						bestLP = Lp_distance[j];
						majorityFunction = schemes[k];
					}
				}
			}
		}
		System.out.println("Best K is: " + bestK);
		System.out.println("Best LP is: " + bestLP);
		System.out.println("Best majorityFunction is: " + majorityFunction);
		System.out.println("the error is " + bestError);

	}

}
