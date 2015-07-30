package de.citec.sc.matoll.patterns.english;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_EN_9 extends SparqlPattern {

	Logger logger = LogManager.getLogger(SparqlPattern_EN_9.class.getName());
	
        @Override
	public String getQuery() {
            String query = "SELECT ?lemma ?prep ?dobj_form ?e1_arg ?e2_arg  WHERE {"
                            + "{?y <conll:cpostag> \"VB\" .}"
                            + "UNION"
                            + "{?y <conll:cpostag> \"VBD\" .}"
                            + "UNION"
                            + "{?y <conll:cpostag> \"VBP\" .}"
                            + "UNION"
                            + "{?y <conll:cpostag> \"VBZ\" .}"
                            + "?y <conll:form> ?lemma . "
                            + "?e1 <conll:head> ?y . "
                            + "?e1 <conll:deprel> ?deprel. "
                            + "FILTER regex(?deprel, \"subj\") ."
                            + "?p <conll:head> ?y . "
                            + "?p <conll:deprel> \"prep\" . "
                            + "?p <conll:form> ?prep . "
                            + "?e2 <conll:head> ?y. "
                            + "?e2 <conll:form> ?dobj_form. "
                            + "?e2 <conll:deprel> \"dobj\"."
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
                

	
        @Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		List<String> sentences = this.getSentences(model);
		
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 verb = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
                          }
	        	 catch(Exception e){
	     	    	e.printStackTrace();
                        }
                     }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                qExec.close() ;
    
		if(verb!=null && e1_arg!=null && e2_arg!=null) {
                    Templates.getTransitiveVerb(model, lexicon, sentences, verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
            } 
		
	}

        @Override
	public String getID() {
		return "SPARQLPattern_EN_1";
	}

}
