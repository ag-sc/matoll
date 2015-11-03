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

public class SparqlPattern_DE_Transitive_Passive extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_Transitive_Passive.class.getName());
	
        /*
        Passive
        */
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?particle ?e1_arg ?e2_arg  WHERE {"
                            + "?e1 <conll:deprel> \"subj\" . "
                            + "?e1 <conll:head> ?werden. "
                            + "?werden <conll:lemma> \"werden\". "
                            + "?verb <conll:lemma> ?lemma . "
                            + "?verb <conll:head> ?werden . "
                            + "?verb <conll:cpostag> \"V\" . "
                            + "?verb <conll:deprel> \"aux\" . "
                            + "OPTIONAL{ "
                            + "?blank <conll:head> ?verb . "
                            + "?blank <conll:deprel> \"avz\" ."
                            + " ?blank <conll:form> ?particle .}"
                            + "?preposition <conll:head> ?verb ."
                            + "?preposition <conll:cpostag> \"PREP\" . "
                            + "?preposition <conll:deprel> \"pp\" ."
                            + "?preposition <conll:form> \"von\" ."
                            + "?e2 <conll:deprel> \"pn\" . "
                            + "?e2 <conll:head> ?preposition. "
                            + "?e1 <own:senseArg> ?e2_arg. "
                            + "?e2 <own:senseArg> ?e1_arg. "
                            + "}";
            return query;
        }
	
	
	@Override
	public String getID() {
		return "SparqlPattern_DE_Transitive_Passive";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {

                model.enterCriticalSection(Lock.READ) ;
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;
                String particle = null;
//                String prep = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 verb = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
//                                 prep = qs.get("?prep").toString();
                                 try{
                                  particle = qs.get("?particle").toString();	   
                                 }
                                 catch(Exception e){}
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
                    if(particle!=null){
                        Templates.getTransitiveVerb(model, lexicon, sentence,particle+verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
                    }else Templates.getTransitiveVerb(model, lexicon, sentence,verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
	}

}
