package core;

import java.util.HashMap;

public class FeatureVector {

	HashMap<String,Double> features;
	
	public FeatureVector()
	{
		features = new HashMap<String,Double>();
	}
	
	public void add(String feature, Double value) {
		features.put(feature, value);
		
	}
	
	public FeatureVector add(FeatureVector vector)
	{
		FeatureVector vec = new FeatureVector();
		
		HashMap<String,Double> map = vector.getFeatures();
		
		for (String feature: features.keySet())
		{
			if (map.containsKey(feature))
			{
				vec.put(feature, features.get(feature)+map.get(feature));
			}
			else
			{
				vec.put(feature,features.get(feature));
			}
		}
		
		for (String feature: map.keySet())
		{
			if (!features.containsKey(feature))
			{
				vec.put(feature, map.get(feature));
			}
		}
		
		
		return vec;
		
	}

	private void put(String feature, Double d) {
		features.put(feature, d);
		
	}

	private HashMap<String, Double> getFeatures() {
		return features;
	}

	public double getValueOfFeature(String feature) {

		if (features.containsKey(feature))
		{
			return features.get(feature);
		}
		else
		{
			return 0.0;
		}

	}
	
	public String toString()
	{
		String string ="";
		for (String feature: features.keySet())
		{
			string+= feature +" => " + features.get(feature) +"\t";
		}
		return string;
	}

}
