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
		String[] weighting_scheme = {"uniform", "weighted"};
		String[] dataset = {"original", "scaled"};
		Instances data = loadData("auto_price.txt");
		Instances scaledData = FeatureScaler.scaleData(data);

		for (int i = 0; i < k_values.length; i++) {
			for (int j = 0; j < Lp_distance.length; j++) {
				for (int k = 0; k < weighting_scheme.length; k++) {

				}
			}
		}

	}

}
