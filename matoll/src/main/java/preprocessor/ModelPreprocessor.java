package preprocessor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class ModelPreprocessor {

	HashMap<String,String> Resource2Lemma;
	HashMap<String,String> Resource2Head;
	HashMap<String,String> Resource2Dependency;
	
	public void preprocess(Model model, String subjectEntity,
			String objectEntity) {
		
		Resource2Lemma = getResource2Lemma(model);
		Resource2Head = getResource2Head(model);
		Resource2Dependency = getResource2Dependency(model);
		
		List<Hypothesis> hypotheses;
		
		String root;
		
		if (objectEntity != null)
		{
			List<List<String>> objectResources = getResources(model,objectEntity);
			hypotheses = getHypotheses(objectResources);
			
			for (Hypothesis hypo: hypotheses)
			{
				// System.out.print("Final hypo: "+hypo.toString());
				
				root = hypo.checkValidAndReturnRoot(Resource2Head,Resource2Dependency);
				
				// if (root != null) System.out.print("Root:"+root);
				
				model.add(model.getResource(root), model.createProperty("own:senseArg"), model.createResource("http://lemon-model.net/lemon#objOfProp"));
					
			}
		}
		
		if (subjectEntity != null)
		{
			List<List<String>> subjectResources = getResources(model,subjectEntity);
			 hypotheses = getHypotheses(subjectResources);
			for (Hypothesis hypo: hypotheses)
			{
				
				// System.out.print("Final hypo: "+hypo.toString());
				
				root = hypo.checkValidAndReturnRoot(Resource2Head,Resource2Dependency);
				
				// if (root != null) System.out.print("Root:"+root);
				model.add(model.getResource(root), model.createProperty("own:senseArg"), model.createResource("http://lemon-model.net/lemon#subjOfProp"));
				
					
			}
		}
		
	}

	private List<Hypothesis> getHypotheses(List<List<String>> resources) {
		
		List<Hypothesis> hypotheses = new ArrayList<Hypothesis>();
		
		List<Hypothesis> expanded_hypotheses;
		
		hypotheses.add(new Hypothesis());
				
		for (List<String> nodes: resources)
		{
			// System.out.print("Checking nodes: "+nodes+"\n");
			
			expanded_hypotheses = new ArrayList<Hypothesis>();
			
			
			for (Hypothesis hypo: hypotheses)
			{
				// System.out.print("Expanding: "+hypo.toString());
				
				for (Hypothesis hypot: hypo.expand(nodes))
				{
			
					// System.out.print("Adding: "+hypot.toString());
					expanded_hypotheses.add(hypot);
				}
				
			}
			
			hypotheses = expanded_hypotheses;
		}
		
		return hypotheses;
		
	}

	private List<List<String>> getResources(Model model,
			String string) {
		
		String[] tokens = string.split(" ");
		
		ArrayList<List<String>> resourceList = new ArrayList<List<String>>();
		
		ArrayList<String> wordResources;
		
		StmtIterator iter;
		
		Statement stmt;
		
		for (int i=0; i < tokens.length; i++)
		{
			wordResources = new ArrayList<String>();
			
			iter = model.listStatements(null,model.getProperty("conll:form"), (RDFNode) null);
		
			while (iter.hasNext()) {
						
				stmt = iter.next();
				
				if (stmt.getObject().toString().equals(tokens[i]))
				{
					wordResources.add(stmt.getSubject().toString());
					// System.out.println(stmt.getSubject().toString()+" has form "+stmt.getObject().toString());
				}
				
			}
			
			resourceList.add(wordResources);
							
		}	

		 return resourceList;
	}

	private HashMap<String, String> getResource2Dependency(Model model) {
		
		HashMap<String,String> resource2Dep = new HashMap<String,String>();
		
		StmtIterator iter;
		
		Statement stmt;
		
		iter = model.listStatements(null,model.getProperty("conll:deprel"), (RDFNode) null);
		
		while (iter.hasNext()) {
					
			stmt = iter.next();
			
			resource2Dep.put(stmt.getSubject().toString(), stmt.getObject().toString());
			
			// System.out.println(stmt.getSubject().toString()+" has dependency "+stmt.getObject().toString());
		
			
		}
		
		return resource2Dep;
	   
	}

	private HashMap<String, String> getResource2Head(Model model) {
		
		HashMap<String,String> resource2Head = new HashMap<String,String>();
		
		StmtIterator iter;
		
		Statement stmt;
		
		iter = model.listStatements(null,model.getProperty("conll:head"), (RDFNode) null);
		
		while (iter.hasNext()) {
					
			stmt = iter.next();
			
			resource2Head.put(stmt.getSubject().toString(), stmt.getObject().toString());
			
			// System.out.println(stmt.getSubject().toString()+" has head "+stmt.getObject().toString());
						
		}
		
		return resource2Head;
	}

	private HashMap<String, String> getResource2Lemma(Model model) {
		
		HashMap<String,String> resource2Lemma = new HashMap<String,String>();
		
		StmtIterator iter;
		
		Statement stmt;
		
		iter = model.listStatements(null,model.getProperty("conll:lemma"), (RDFNode) null);
		
		while (iter.hasNext()) {
					
			stmt = iter.next();
			
			resource2Lemma.put(stmt.getSubject().toString(), stmt.getObject().toString());
			
			// System.out.println(stmt.getSubject().toString()+" has lemma "+stmt.getObject().toString());
			
		}
		
		return resource2Lemma;
	}

}