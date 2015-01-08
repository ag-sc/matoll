package patterns;

import java.util.Date;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import core.FeatureVector;
import core.LexicalEntry;
import core.LexiconWithFeatures;
import core.Provenance;
import core.SenseArgument;
import core.SyntacticArgument;

public class SparqlPattern_EN_6 implements SparqlPattern {

	/*	PropSubj:Juana EnrÃ­quez
	PropObj:John II of Aragon
	sentence:Juana EnrÃ­quez , great-granddaughter of the founder of the lineage , married John II of Aragon and was the mother of Ferdinand II . 
	1	Juana	_	NNP	NNP	_	2	nn
	2	EnrÃ­quez	_	NNP	NNP	_	12	nsubj
	3	,	_	,	,	_	2	punct
	4	great-granddaughter	_	NN	NN	_	2	appos
	5	of	_	IN	IN	_	4	prep
	6	the	_	DT	DT	_	7	det
	7	founder	_	NN	NN	_	5	pobj
	8	of	_	IN	IN	_	7	prep
	9	the	_	DT	DT	_	10	det
	10	lineage	_	NN	NN	_	8	pobj
	11	,	_	,	,	_	2	punct
	//married is falsch getagged, müsste VBD seind
	12	married	_	VBN	VBN	_	0	null
	13	John	_	NNP	NNP	_	14	nn
	14	II	_	NNP	NNP	_	12	dobj
	15	of	_	IN	IN	_	14	prep
	16	Aragon	_	NNP	NNP	_	15	pobj
	17	and	_	CC	CC	_	12	cc
	18	was	_	VBD	VBD	_	20	cop
	19	the	_	DT	DT	_	20	det
	20	mother	_	NN	NN	_	12	conj
	21	of	_	IN	IN	_	20	prep
	22	Ferdinand	_	NNP	NNP	_	23	nn
	23	II	_	NNP	NNP	_	21	pobj
	24	.	_	.	.	_	12	punct
	----------------------*/
	String query = "SELECT ?lemma ?e1_arg ?e2_arg WHERE"
			+ "{ "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			// pci: eigentlich nur subj und nsubj denke ich
			+ "FILTER regex(?e1_grammar, \"subj\") ."
			+ "?e1 <conll:cpostag> ?e1_pos . "
			//+ "FILTER regex(?e1_pos, \"NN\") ."
			+ "?e1 <conll:head> ?y . "
			+ "?y <conll:cpostag> ?lemma_pos . "
			// pci: Warum nicht nur VBD und VBZ?
			+ "FILTER regex(?lemma_pos, \"VB\") ."
			+ "?y <conll:deprel> ?lemma_grammar . "
			+ "?y <conll:form> ?lemma . "
			+ "?e2 <conll:head> ?y . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			// pci: Warum nicht hier gleich dobj ?
			+ "FILTER regex(?e2_grammar, \"obj\") ."
			+ "?e2 <conll:form> ?e2_form . "
			+ "?y <own:partOf> ?class. "
			+ "?e1 <own:semArg> ?e1_arg. "
			+ "?e2 <own:semArg> ?e2_arg. "
			+ "}";
	
	

	
	
	@Override
	public void extractLexicalEntries(Model model, String reference, LexiconWithFeatures lexicon) {
		
		// match SPARQL query
		QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
	    ResultSet rs = qExec.execSelect() ;
	    try {
	    	 while ( rs.hasNext() ) {
	        	 QuerySolution qs = rs.next();
	        	 try{
	        		//String tmp = qs.get("?frequency").toString().replace("^^http://www.w3.org/2001/XMLSchema#integer", "");
	        		 
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
		
		FeatureVector vector = new FeatureVector();
		
		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		LexicalEntry entry = new LexicalEntry();
		
		entry.setReference(reference);
		
		entry.setCanonicalForm("?lemma");
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		entry.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
		
		if ("?e1_arg".equals("http://lemon-model.net/lemon#subfOfProp") && "?e2_arg".equals("http://lemon-model.net/lemon#objOfProp"))
		{
			
			entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
			entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
			entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
			entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
		
			lexicon.add(entry, vector);
		}	
		
		if ("?e1_arg".equals("http://lemon-model.net/lemon#objOfProp") && "?e2_arg".equals("http://lemon-model.net/lemon#subfOfProp"))
		{
			
			entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","2",null));
			entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","1",null));
		
			entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
			entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
		
			lexicon.add(entry, vector);
		}	
	
		
	}


	public String getID() {
		return "SPARQLPattern_EN_6";
	}

}
