package de.citec.sc.matoll.patterns.english;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.process.Matoll;
import de.citec.sc.matoll.utils.Lemmatizer;

public class SparqlPattern_EN_7 extends SparqlPattern {

	
	Logger logger = LogManager.getLogger(SparqlPattern_EN_7.class.getName());

	
	
	/*
	 * PropSubj:University of Colombo
PropObj:Professor
sentence:Deshabandu Professor Nandadasa Kodagoda MRCP , MD was the former Vice Chancellor of the University of Colombo . 
1	Deshabandu	_	NNP	NNP	_	5	nn
2	Professor	_	NNP	NNP	_	5	nn
3	Nandadasa	_	NNP	NNP	_	5	nn
4	Kodagoda	_	NNP	NNP	_	5	nn
5	MRCP	_	NNP	NNP	_	12	nsubj
6	,	_	,	,	_	5	punct
7	MD	_	NN	NN	_	5	appos
8	was	_	VBD	VBD	_	12	cop
9	the	_	DT	DT	_	12	det
10	former	_	JJ	JJ	_	12	dep
11	Vice	_	NNP	NNP	_	12	dep
12	Chancellor	_	NNP	NNP	_	0	null
13	of	_	IN	IN	_	12	prep
14	the	_	DT	DT	_	15	det
15	University	_	NNP	NNP	_	13	pobj
16	of	_	IN	IN	_	15	prep
17	Colombo	_	NNP	NNP	_	16	pobj
18	.	_	.	.	_	12	punct
----------------------
	 */
	
	
// pci: Why is this not the same as Pattern 4 ???	
	
	String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep WHERE"
			+ "{ "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			+ "FILTER regex(?e1_grammar, \"nsubj\") ."
			+ "?e1 <conll:cpostag> ?e1_pos . "
			//+ "FILTER regex(?e1_pos, \"NN\") ."
			+ "?e1 <conll:head> ?y . "
			+ "?y <conll:cpostag> ?lemma_pos . "
			+"{?y <conll:cpostag> \"NN\" . }"
			+ "UNION"
			+"{?y <conll:cpostag> \"NNS\" . }"
			+ "?y <conll:deprel> ?lemma_grammar . "
			+ "?y <conll:form> ?lemma . "
			+"OPTIONAL{"
			+ "?lemma_nn <conll:head> ?y. "
			+ "?lemma_nn <conll:form> ?lemma_addition. "
			+ "?lemma_nn <conll:cpostag> ?lemma_postag."
			+ "FILTER regex(?lemma_postag, \"NNP\") ."
			+ "?lemma_nn <conll:deprel> \"dep\" ."
			+"} "
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"prep\" . "
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			+ "FILTER regex(?e2_grammar, \"pobj\") ."
			//+ "?o <is> ?e2 ."
			+ "?e2 <conll:form> ?e2_form . "
			+ "?y <own:partOf> ?class. "
			+ "?class <own:subj> ?propSubj. "
			+ "?class <own:obj> ?propObj. "
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
	
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		
		QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
	    ResultSet rs = qExec.execSelect() ;
	    
	    String noun;
	    String prefix;
	    String e1_arg ="http://lemon-model.net/lemon#subjfOfProp";
	    String e2_arg = "http://lemon-model.net/lemon#objfOfProp";
	    String preposition;
	    
	    FeatureVector vector = new FeatureVector();
	    
	    vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
	    try {
	    	 while ( rs.hasNext() ) {
	        	 QuerySolution qs = rs.next();
	        	 // System.out.print("Query 2 matched\n!!!");
	        	 try{
	        		 noun = qs.get("?lemma").toString();
	        		 
	        		 if(qs.get("?prefix") != null )
	        		 {
	        			 prefix = qs.get("?prefix").toString();
	        			 noun = prefix +" " +noun;
	        		 }
	        		 // e1_arg = qs.get("?e1_arg").toString();
	        		 // e2_arg = qs.get("?e2_arg").toString();
	        		 
	        		 preposition = qs.get("?prep").toString();
	        		 
	        		 // System.out.print("Found: "+noun+"\n");
	        		 
	        		 	LexicalEntry entry = new LexicalEntry();
	        			
	        		 	Sense sense = new Sense();
	        		 	
	           		 	sense.setReference(new SimpleReference(this.getReference(model)));
	           		 	
	        		 	entry.setSense(sense);
	        		 	
	        		 	SyntacticBehaviour behaviour = new SyntacticBehaviour();
	        		 	
	        		 	entry.setSyntacticBehaviour(behaviour);
	        			
	        			if (Lemmatizer != null)
	        			{
	        				entry.setCanonicalForm(Lemmatizer.getLemma(noun)+"@en");
	        			}
	        			else
	        			{
	        				entry.setCanonicalForm(noun+"@en");
	        			}
	        				
	        			entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");
	        			
	        			behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame");
	        			
	        			// System.out.print(entry+"\n");
	        			
	        			for (String sentence: sentences)
	        			{
	        				entry.addSentence(sentence);
	        			}
	        			
	        			if (e1_arg.equals("http://lemon-model.net/lemon#subjfOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","1",preposition));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","1",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        				logger.info("Found entry:"+entry+"/n");
	        				
	        			}	
	        			
	        			if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#adpositionalObject","1",preposition));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","2",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","2"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","1"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        				logger.info("Found entry:"+entry+"/n");
	        				
	        			}	
	        			 
	        	 }
	        	 catch(Exception e){
	     	    	e.printStackTrace();
	        		 //ignore those without Frequency TODO:Check Source of Error
	     	    }
	    	 }
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    qExec.close() ;
		
	}
	

	public String getID() {
		return "SPARQLPattern_EN_7";
	}

}
