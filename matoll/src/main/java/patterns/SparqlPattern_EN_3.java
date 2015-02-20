package patterns;

import utils.Lemmatizer;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import core.LexicalEntry;
import core.LexiconWithFeatures;
import core.Sense;
import core.SenseArgument;
import core.SimpleReference;
import core.SyntacticArgument;
import core.SyntacticBehaviour;
import de.citec.sc.bimmel.core.FeatureVector;

public class SparqlPattern_EN_3 extends SparqlPattern {


	/*
	 * ################################
entity1_form:akerson
entity2_form:motors
propSubject:daniel akerson
propObject:general motors
--------------
entity1_grammar:appos
entity2_grammar:pobj
--------------
lemma:chairman
lemma_grammar:nsubj
lemma_pos:nn
depending dobj:
depending advmod:
--------------
query_name:query3
intended_lexical_type:Noun
entry:RelationalNoun("chairman",<http://dbpedia.org/ontology/board>, propSubj = PrepositionalObject("of"), propObj = CopulativeArg)
--------------
sentence:In July 2011 , the chairman and CEO of General Motors , Daniel Akerson , stated that while the cost of hydrogen fuel cell cars is decreasing : `` The car is still too expensive and probably wo n't be practical until the 2020-plus period , I do n't know . '' 
1	In	_	IN	IN	_	16	prep
2	July	_	NNP	NNP	_	1	pobj
3	2011	_	CD	CD	_	2	num
4	,	_	,	,	_	16	punct
5	the	_	DT	DT	_	6	det
6	chairman	_	NN	NN	_	16	nsubj
7	and	_	CC	CC	_	6	cc
8	CEO	_	NN	NN	_	6	conj
9	of	_	IN	IN	_	6	prep
10	General	_	NNP	NNP	_	11	nn
11	Motors	_	NNPS	NNPS	_	9	pobj
12	,	_	,	,	_	6	punct
13	Daniel	_	NNP	NNP	_	14	nn
14	Akerson	_	NNP	NNP	_	6	appos
15	,	_	,	,	_	6	punct
16	stated	_	VBD	VBD	_	0	null

	 */

	
	
	String query = "SELECT ?lemma ?prefix ?prep  WHERE {"
			+ "{?y <conll:cpostag> \"NN\" . }"
			+ "UNION"
			+ "{?y <conll:cpostag> \"NNS\" . }"
			+ "?y <conll:form> ?lemma . "
			+"OPTIONAL{"
			+ "?modifier <conll:head> ?y. "
			+ "?modifier <conll:form> ?prefix. "
			+ "?modifier <conll:deprel> \"nn\"."
			+"} "
			+ "?e1 <conll:head> ?y . "
			+ "?e1 <conll:deprel> \"appos\"."
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"prep\" . "
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel> \"pobj\" "
			// + "?e1 <own:senseArg> ?e1_arg. "
			// + "?e2 <own:senseArg> ?e2_arg. "
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
		
	    try {
	    	 while ( rs.hasNext() ) {
	        	 QuerySolution qs = rs.next();
	        	 
	        	 System.out.print("Query 3 matched\n!!!");
	        	 
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
	        		 
	        		 System.out.print("Found: "+noun+"\n");
	        		 
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
	        			
	        			System.out.print(entry+"\n");
	        			
	        			
	        			if (e1_arg.equals("http://lemon-model.net/lemon#subjfOfProp") && e2_arg.equals("http://lemon-model.net/lemon#objOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","1",preposition));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","1",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
	        			
	        				lexicon.add(entry, vector);
	        				
	        			}	
	        			
	        			if (e1_arg.equals("http://lemon-model.net/lemon#objOfProp") && e2_arg.equals("http://lemon-model.net/lemon#subjOfProp"))
	        			{
	        				
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/adpositionalObject","1",preposition));
	        				behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg","2",null));
	        			
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","2"));
	        				sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","1"));
	        			
	        				lexicon.add(entry, vector);
	        				
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
		return "SPARQLPattern_EN_3";
	}

}
