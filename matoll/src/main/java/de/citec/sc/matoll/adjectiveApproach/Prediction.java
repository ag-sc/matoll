package de.citec.sc.matoll.adjectiveApproach;

import java.util.ArrayList;
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
	
	
	public List<String> predict(Instance current) throws Exception{
		/*
		 * value_to_predict
		 * can be only 0 or 1, as only two classes are given 
		 */
	    //double value=cls.classifyInstance(current.instance(value_to_predict));
		
		double value=cls.classifyInstance(current);

	    //get the prediction percentage or distribution
	    //double[] percentage=cls.distributionForInstance(current.instance(value_to_predict));
	    double[] percentage=cls.distributionForInstance(current);
	    //get the name of the class value
	    String prediction=current.classAttribute().value((int)value); 
	    
	    String distribution="";
        for(int i=0; i <percentage.length; i=i+1)
        {
            if(i==value)
            {
                distribution=distribution+"*"+Double.toString(percentage[i])+",";
            }
            else
            {
                distribution=distribution+Double.toString(percentage[i])+",";
            }
        }
        distribution=distribution.substring(0, distribution.length()-1);

        /*
         * TODO: Work on distribution
         */
        
        List<String> result = new ArrayList<String>();
        /*
         * add prediction
         */
        result.add(prediction);
        
        /*
         * add distribution
         */
        result.add(distribution);
        
        return result;
	}


}
