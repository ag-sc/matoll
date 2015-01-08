package core;

import java.util.HashMap;

public class FeatureVector {

	HashMap<String,Double> features;
	
	public void add(String feature, Double value) {
		features.put(feature, value);
		
	}
	
	public FeatureVector add(FeatureVector vector)
	{
		FeatureVector vec = new FeatureVector();
		
		// additive operation on vectors
		
		return vec;
		
	}

}
