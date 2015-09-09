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

public class SparqlPattern_DE_2 extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_2.class.getName());
	
        /*
        NounPP
        */
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?prep ?e1_arg ?e2_arg  WHERE {"
                            + "?e1 <conll:deprel> \"subj\" . "
                            + "?e1 <conll:head> ?sein. "
                            + "?sein <conll:lemma> \"sein\". "
                            + "?noun <conll:lemma> ?lemma . "
                            + "?noun <conll:head> ?sein . "
                            + "?noun <conll:cpostag> \"N\" . "
                            + "?noun <conll:deprel> \"pred\" . "
                            + "?preposition <conll:head> ?noun ."
                            + "?preposition <conll:cpostag> \"PREP\" . "
                            + "?preposition <conll:deprel> \"pp\" ."
                            + "?preposition <conll:lemma> ?prep ."
                            + "?e2 <conll:deprel> \"pn\" . "
                            + "?e2 <conll:head> ?preposition. "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_2";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {

		
                model.enterCriticalSection(Lock.READ) ;
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String noun = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 noun = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
                                 preposition = qs.get("?prep").toString();	
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
    
		if(noun!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Sentence sentence = this.returnSentence(model);
                    Templates.getNounWithPrep(model, lexicon, sentence, noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
	}

}
