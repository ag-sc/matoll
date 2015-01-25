package patterns;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import core.FeatureVector;
import core.LexicalEntry;
import core.LexiconWithFeatures;
import core.Sense;
import core.SenseArgument;
import core.SyntacticArgument;
import core.SyntacticBehaviour;

public class SparqlPattern_EN_4 extends SparqlPattern {

	/*################################
entity1_form:shriram
entity2_form:google
propSubject:ram shriram
propObject:google
--------------
entity1_grammar:nsubj
entity2_grammar:pobj
--------------
lemma:member
lemma_grammar:null
lemma_pos:nn
depending dobj:
depending advmod:
--------------
query_name:query5
intended_lexical_type:Noun
entry:RelationalNoun("board member",<http://dbpedia.org/ontology/board>, propSubj = PrepositionalObject("of"), propObj = CopulativeArg)
--------------
sentence:Kavitark Ram Shriram is a board member of Google and one of the first investors in Google . 
1	Kavitark	_	NNP	NNP	_	3	nn
2	Ram	_	NNP	NNP	_	3	nn
3	Shriram	_	NNP	NNP	_	7	nsubj
4	is	_	VBZ	VBZ	_	7	cop
5	a	_	DT	DT	_	7	det
6	board	_	NN	NN	_	7	nn
7	member	_	NN	NN	_	0	null
8	of	_	IN	IN	_	7	prep
9	Google	_	NNP	NNP	_	8	pobj
=>works only in fuzzy mode.

PropSubj:Oxford Brookes University
PropObj:Janet Beer
sentence:Professor Janet Beer is the Vice-Chancellor of Oxford Brookes University . 
1	Professor	_	NNP	NNP	_	3	nn
2	Janet	_	NNP	NNP	_	3	nn
3	Beer	_	NNP	NNP	_	6	nsubj
4	is	_	VBZ	VBZ	_	6	cop
5	the	_	DT	DT	_	6	det
6	Vice-Chancellor	_	NNP	NNP	_	0	null
7	of	_	IN	IN	_	6	prep
8	Oxford	_	NNP	NNP	_	10	nn
9	Brookes	_	NNP	NNP	_	10	nn
10	University	_	NNP	NNP	_	7	pobj
11	.	_	.	.	_	6	punct
----------------------


*/
		
		String query = "SELECT ?lemma ?prefix ?e1_arg ?e2_arg ?prep WHERE"
				+"{ "
				+"{ ?y <conll:cpostag> \"NN\" . } "
				+ " UNION "
				+"{ ?y <conll:cpostag> \"NNS\" . } "
				+ " UNION "
				+"{ ?y <conll:cpostag> \"NNP\" . } "
				+ "?y <conll:form> ?lemma . "
				+"OPTIONAL{"
				+ "?lemma_nn <conll:head> ?y. "
				+ "?lemma_nn <conll:form> ?prefix. "
				+ "?lemma_nn <conll:deprel> \"nn\"."
				+"} "
				+ "?verb <conll:head> ?y . "
				+ "?verb <conll:deprel> \"cop\" ."
				+ "?e1 <conll:head> ?y . "
				+ "?e1 <conll:deprel> \"nsubj\" . "
				+ "?p <conll:head> ?y . "
				+ "?p <conll:deprel> \"prep\" . "
				+ "?p <conll:form> ?prep . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:deprel> \"pobj\" . "
				+ "?e1 <own:senseArg> ?e1_arg. "
				+ "?e2 <own:senseArg> ?e2_arg. "
				+ "}";
		
		

	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
	    ResultSet rs = qExec.execSelect() ;
	    
	    String noun;
	    String prefix;
	    String e1_arg;
	    String e2_arg;
	    String preposition;
	    
	    FeatureVector vector = new FeatureVector();
	    
	    vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
	    try {
	    	 while ( rs.hasNext() ) {
	        	 QuerySolution qs = rs.next();
	        	 try{
	        		 noun = qs.get("?lemma").toString();
	        		 
	        		 if(qs.get("?prefix") != null )
	        		 {
	        			 prefix = qs.get("?prefix").toString();
	        			 noun = prefix +" " +noun;
	        		 }
	        		 e1_arg = qs.get("?e1_arg").toString();
	        		 e2_arg = qs.get("?e2_arg").toString();
	        		 
	        		 preposition = qs.get("?prep").toString();
	        		 
	        		 System.out.print("Found\n");
	        		 
	        		 	LexicalEntry entry = new LexicalEntry();
	        			
	        		 	Sense sense = new Sense();
	        		 	
	        		 	sense.setReference(this.getReference(model));
	        		 	
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
		return "SPARQLPattern_EN_4";
	}

}
