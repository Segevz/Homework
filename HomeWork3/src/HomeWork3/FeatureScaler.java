package HomeWork3;

import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Standardize;

public class FeatureScaler {
	/**
	 * Returns a scaled version (using standarized normalization) of the given dataset.
	 * @param instances The original dataset.
	 * @return A scaled instances object.
	 */
	public static Instances scaleData(Instances instances) throws Exception {
		Normalize normalize = new Normalize();
//		Standardize standardize = new Standardize();
		Filter standardize = new Standardize();
		//can throw an exception
//		normalize.setInputFormat(instances);
		standardize.setInputFormat(instances);
		instances = Filter.useFilter(instances,standardize);

		return instances;
	}
}
