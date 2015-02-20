package classifiers;

import java.util.Set;

import de.citec.sc.bimmel.core.Dataset;
import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.bimmel.learning.Classifier;

public class FreqClassifier implements Classifier {

	String Feature;
	
	double Threshold;
	
	public FreqClassifier(String feature, double threshold)
	{
		Feature = feature;
		Threshold = threshold;
	}
	
	public int predict(FeatureVector vector)
	{
		double value;
		
		value=  vector.getValueOfFeature(Feature);
		
		if (value >= Threshold)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	public void train(Dataset dataset) {
		
	}

	public double predict(FeatureVector vector, int label) {
		
		double value;
		
		value=  vector.getValueOfFeature(Feature);
		
		if (value >= Threshold)
		{
			return value;
		}
		else
		{
			return 0;
		}
		
	}

	public void saveModel(String file) {
		// TODO Auto-generated method stub
		
	}

	public void loadModel(String file) {
		// TODO Auto-generated method stub
		
	}


}
