package de.citec.sc.matoll.adjectiveApproach;

import weka.classifiers.Classifier;
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
	
	
	public void predict(Instances instance, int value_to_predict) throws Exception{
		/*
		 * value_to_predict
		 * can be only 0 or 1, as only two classes are given 
		 */
	    double value=cls.classifyInstance(instance.instance(value_to_predict));

	    //get the prediction percentage or distribution
	    double[] percentage=cls.distributionForInstance(instance.instance(value_to_predict));

	    //get the name of the class value
	    String prediction=instance.classAttribute().value((int)value); 

	    System.out.println("The predicted value of instance "+
	                            Integer.toString(value_to_predict)+
	                            ": "+prediction); 
	    
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

        System.out.println("Distribution:"+ distribution);
	}


}
