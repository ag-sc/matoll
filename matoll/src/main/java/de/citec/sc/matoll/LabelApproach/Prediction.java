package de.citec.sc.matoll.LabelApproach;

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
	    //double pred=cls.classifyInstance(current.instance(value_to_predict));
		
		double pred=cls.classifyInstance(current);
	
            double value=cls.classifyInstance(current);

             //get the prediction percentage or distribution
            double[] percentage=cls.distributionForInstance(current);

            //get the name of the class value
            String prediction_string=current.classAttribute().value((int)value); 
            
        

	    //get the prediction percentage or distribution
	    //double[] percentage=cls.distributionForInstance(current.instance(value_to_predict));
	    //double[] percentage=cls.distributionForInstance(current);
//	    String distribution = Double.toString(percentage[(int) pred]);
//	    String prediction=current.classAttribute().pred((int)pred); 
	    
//        for(double x:percentage){
//            System.out.println("x:"+x);
//        }
//        List<String> result = new ArrayList<String>();
//        System.out.println("value:"+pred);
        int prediction = (int)pred;
        double distribution = percentage[(int) pred];
//        if(prediction_string.contains("0")){
//                System.out.println("YEAH");
//                System.out.println("Prediction"+prediction);
//            }
//        System.out.println("prediction:"+prediction);
//        System.out.println("distribution:"+distribution);
        hm.put(prediction, distribution);
        
        return hm;
	}


}
