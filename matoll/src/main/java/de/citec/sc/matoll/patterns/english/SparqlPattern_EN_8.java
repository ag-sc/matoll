package de.citec.sc.matoll.patterns.english;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;
import de.citec.sc.matoll.process.Matoll;
import de.citec.sc.matoll.utils.Lemmatizer;

public class SparqlPattern_EN_8 extends SparqlPattern {

	Logger logger = LogManager.getLogger(SparqlPattern_EN_8.class.getName());

	
	// für Adjective mit JJ, drunter hängende Verb erst einmal ignorieren
	/*
	 * TODO: How to add lemma addition?
	 */
			String query = "SELECT ?lemma ?prep ?e1_arg ?e2_arg WHERE{"
					+ "?e1 <conll:deprel> ?e1_grammar . "
					+ "FILTER regex(?e1_grammar, \"subj\") ."
					+ "?e1 <conll:cpostag> ?e1_pos . "
					//+ "FILTER regex(?e1_pos, \"NN\") ."
					+ "?e1 <conll:head> ?y . "
					+ "?y <conll:cpostag> ?lemma_pos . "
					+ "FILTER regex(?lemma_pos,\"JJ\") . "
					+ "?y <conll:form> ?lemma . "
					+"OPTIONAL{"
					+ "?lemma_nn <conll:head> ?y. "
					+ "?lemma_nn <conll:form> ?lemma_addition. "
					+ "?lemma_nn <conll:deprel> \"nn\"."
					+"} "
					//+ "?verb <conll:head> ?y . "
					+ "?p <conll:head> ?y . "
					+ "?p <conll:deprel> \"prep\" . "
					+ "?p <conll:form> ?prep . "
					+ "?e2 <conll:head> ?p . "
					+ "?e2 <conll:deprel> ?e2_grammar . "
					+ "FILTER regex(?e2_grammar, \"obj\") ."
					+ "?e1 <own:senseArg> ?e1_arg. "
					+ "?e2 <own:senseArg> ?e2_arg. "
					+ "}";

	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();
		
		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
                ResultSet rs = qExec.execSelect() ;
                String adjective = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();

                         // System.out.print("Query 3 matched\n!!!");

                         try{
                                 adjective = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
                                 preposition = qs.get("?prep").toString();	
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
    
		if(adjective!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Templates.getAdjective(model, lexicon, vector, sentences, adjective, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
            } 

	}

	public String getID() {
		return "SPARQLPattern_EN_8";
	}

}
