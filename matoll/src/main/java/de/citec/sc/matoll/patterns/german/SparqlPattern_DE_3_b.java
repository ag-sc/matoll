package de.citec.sc.matoll.patterns.german;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;
import org.apache.jena.shared.Lock;

public class SparqlPattern_DE_3_b extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_3_b.class.getName());
	
        /*
        Noun Possessive
        */
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma  ?e1_arg ?e2_arg  WHERE {"
                            + "?e1 <conll:deprel> \"subj\" . "
                            + "?e1 <conll:head> ?sein. "
                            + "?sein <conll:lemma> \"sein\". "
                            + "?noun1 <conll:lemma> ?lemma . "
                            + "?noun1 <conll:head> ?sein . "
                            + "?noun1 <conll:cpostag> \"N\" . "
                            + "?noun1 <conll:deprel> \"pred\" . "
                            + "?e2 <conll:deprel> \"gmod\" . "
                            + "?e2 <conll:head> ?noun1. "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_3_b";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {

		
                model.enterCriticalSection(Lock.READ) ;
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String noun = null;
                String e1_arg = null;
                String e2_arg = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 noun = qs.get("?lemma").toString();
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
                model.leaveCriticalSection() ;
    
		if(noun!=null && e1_arg!=null && e2_arg!=null) {
                    Sentence sentence = this.returnSentence(model);
                    Templates.getNounPossessive(model, lexicon, sentence, noun, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
	}

}
