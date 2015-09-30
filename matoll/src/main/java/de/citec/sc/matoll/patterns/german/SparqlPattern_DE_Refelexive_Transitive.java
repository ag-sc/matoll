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

public class SparqlPattern_DE_Refelexive_Transitive extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_Refelexive_Transitive.class.getName());
	
        /*
        Intransitive + pp
        */
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?prep ?particle ?e1_arg ?e2_arg  WHERE {"
                            + "?e1 <conll:deprel> \"subj\" . "
                            + "?e1 <conll:head> ?verb. "
                            + "?verb <conll:lemma> ?lemma . "
                            + "?verb <conll:cpostag> \"V\" . "
                            + "?blank <conll:head> ?verb. "
                            + "?blank <conll:deprel> \"obja\" . "
                            + "{?blank <conll:form> \"sich\" . } UNION "
                            + "{?blank <conll:form> \"mich\" . } UNION "
                            + "{?blank <conll:form> \"dich\" . } UNION "
                            + "{?blank <conll:form> \"uns\" . } "
                            + "OPTIONAL{ "
                            + "?blankparticle <conll:head> ?verb . "
                            + "?blankparticle <conll:deprel> \"avz\" ."
                            + "?blankparticle <conll:form> ?particle .}"
                            + "?preposition <conll:head> ?verb ."
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
		return "SPARQLPattern_DE_6_b";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {

                model.enterCriticalSection(Lock.READ) ;
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;
                String prep = null;
                String particle = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 verb = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
                                 prep = qs.get("?prep").toString();
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
    
		if(verb!=null && e1_arg!=null && e2_arg!=null && prep!=null) {
                    Sentence sentence = this.returnSentence(model);
                    if(particle!=null){
                        System.out.println(particle+verb+"  "+getID());
                        Templates.getReflexiveTransitiveVerb(model, lexicon, sentence,particle+verb, e1_arg, e2_arg,prep, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
                    }else
                        Templates.getReflexiveTransitiveVerb(model, lexicon, sentence,verb, e1_arg, e2_arg,prep, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
	}

}
