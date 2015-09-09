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

public class SparqlPattern_DE_5_b extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_5_b.class.getName());
	
        /*
        Passive (optional pattern)
        */
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?e2_arg  WHERE {"
                            + "?e1 <conll:deprel> \"subj\" . "
                            + "?e1 <conll:head> ?werden. "
                            + "?werden <conll:lemma> \"werden\". "
                            + "?verb <conll:lemma> ?lemma . "
                            + "?verb <conll:head> ?werden . "
                            + "?verb <conll:cpostag> \"V\" . "
                            + "?verb <conll:deprel> \"aux\" . "
                            + "{?e2 <conll:deprel> \"objd\" .} "
                            + "UNION"
                            + "{?e2 <conll:deprel> \"obja\" .} "
                            + "?e2 <conll:head> ?verb. "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_5_a";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {

		
                model.enterCriticalSection(Lock.READ) ;
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
                model.leaveCriticalSection() ;
    
		if(verb!=null && e1_arg!=null && e2_arg!=null) {
                    Sentence sentence = this.returnSentence(model);
                    Templates.getTransitiveVerb(model, lexicon, sentence,verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
	}

}
