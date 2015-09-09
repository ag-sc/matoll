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

public class SparqlPattern_DE_4 extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_4.class.getName());
	
        /*
        Transitive
        */
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?additional_lemma ?e1_arg ?e2_arg  WHERE {"
                            + "?e1 <conll:deprel> \"subj\" . "
                            + "?e1 <conll:head> ?verb. "
                            + "?verb <conll:lemma> ?lemma . "
                            + "?verb <conll:cpostag> \"V\" . "
                            + "{?e2 <conll:deprel> \"obja\" . }"
                            + "UNION"
                            + "{?e2 <conll:deprel> \"objd\" . }"
                            + "?e2 <conll:head> ?verb. "
                            + "OPTIONAL "
                            + "{?lemma_addition <connl:head> ?verb . "
                            + "?lemma_addidtion <conll:deprel> \"obji\". "
                            + "?lemma_addition <connl:lemma> ?additional_lemma .} "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_4";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {

		
                model.enterCriticalSection(Lock.READ) ;
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;
                String additional_lemma = "";

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 verb = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
                                 try{
                                     additional_lemma = qs.get("?additional_lemma").toString();
                                 }
                                 catch (Exception e){}
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
                    if(!additional_lemma.equals("")){
                        Templates.getTransitiveVerb(model, lexicon, sentence, additional_lemma +" "+verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
                    }
                    else Templates.getTransitiveVerb(model, lexicon, sentence,verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
	}

}
