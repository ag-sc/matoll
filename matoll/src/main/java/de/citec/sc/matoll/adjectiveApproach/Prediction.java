package de.citec.sc.matoll.adjectiveApproach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/*
 * /*
 * Code modified from:
 * http://stackoverflow.com/questions/21674522/get-prediction-percentage-in-weka-using-own-java-code-and-a-model/21678307#21678307
 */
 
public class Prediction {
	
	Classifier cls = null;
	
	public Prediction(String path_to_model) throws Exception{
        this.cls = (Classifier) weka.core.SerializationHelper.read(path_to_model);
	}
	
        
	
	public HashMap<Integer, Double> predict(Instance current) throws Exception{
            HashMap<Integer,Double> hm = new HashMap<Integer,Double>();
		/*
		 * value_to_predict
		 * can be only 0 or 1, as only two classes are given 
		 */
	    //double value=cls.classifyInstance(current.instance(value_to_predict));
		
		double value=cls.classifyInstance(current);
		
		

	    //get the prediction percentage or distribution
	    //double[] percentage=cls.distributionForInstance(current.instance(value_to_predict));
	    double[] percentage=cls.distributionForInstance(current);
//	    String distribution = Double.toString(percentage[(int) value]);
//	    String prediction=current.classAttribute().value((int)value); 
	    

        List<String> result = new ArrayList<String>();
        int prediction = (int)value;
        double distribution = percentage[(int) value];
        hm.put(prediction, distribution);
        
        return hm;
	}


}
