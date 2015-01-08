package preprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

public class ModelPreprocessor {

	HashMap<String,String> Resource2Lemma;
	HashMap<String,String> Resource2Head;
	HashMap<String,String> Resource2Dependency;
	
	public void preprocess(Model model, String subjectEntity,
			String objectEntity) {
		
		Resource2Lemma = getResource2Lemma(model);
		Resource2Head = getResource2Head(model);
		Resource2Dependency = getResource2Dependency(model);
		
		List<List<String>> objectResources = getResources(model,objectEntity);
		List<List<String>> subjectResources = getResources(model,subjectEntity);
		
		List<Hypothesis> hypotheses = getHypotheses(objectResources);
		
		String root;
	
		for (Hypothesis hypo: hypotheses)
		{
			root = hypo.checkValidAndReturnRoot(Resource2Head);
				
		}
		
		hypotheses = getHypotheses(objectResources);
			
		for (Hypothesis hypo: hypotheses)
		{
			root = hypo.checkValidAndReturnRoot(Resource2Head);
				
		}
		
	}

	private List<Hypothesis> getHypotheses(List<List<String>> resources) {
		
		List<Hypothesis> hypotheses = new ArrayList<Hypothesis>();
		
		List<Hypothesis> expanded_hypotheses;
		
		hypotheses.add(new Hypothesis());
		
		for (List<String> nodes: resources)
		{
			expanded_hypotheses = new ArrayList<Hypothesis>();
			
			for (Hypothesis hypo: hypotheses)
			{
				expanded_hypotheses.addAll(hypo.expand(nodes));
			}
			
			hypotheses = expanded_hypotheses;
		}
		
		return hypotheses;
		
	}

	private List<List<String>> getResources(Model model,
			String subjectEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	private HashMap<String, String> getResource2Dependency(Model model) {
		// TODO Auto-generated method stub
		return null;
	}

	private HashMap<String, String> getResource2Head(Model model) {
		// TODO Auto-generated method stub
		return null;
	}

	private HashMap<String, String> getResource2Lemma(Model model) {
		// TODO Auto-generated method stub
		return null;
	}

}
