package de.citec.sc.matoll.patterns.german;

import org.apache.jena.rdf.model.Model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.citec.sc.lemon.core.Lexicon;
import de.citec.sc.matoll.patterns.SparqlPattern;


public class SparqlPattern_DE_Transitive_Passive_optional extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_Transitive_Passive_optional.class.getName());
	
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
		return "SparqlPattern_DE_Transitive_Passive_optional";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {

//                model.enterCriticalSection(Lock.READ) ;
//		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
//                ResultSet rs = qExec.execSelect() ;
//                String verb = null;
//                String e1_arg = null;
//                String e2_arg = null;

//                try {
//                 while ( rs.hasNext() ) {
//                         QuerySolution qs = rs.next();
//
//
//                         try{
//                                 verb = qs.get("?lemma").toString();
//                                 e1_arg = qs.get("?e1_arg").toString();
//                                 e2_arg = qs.get("?e2_arg").toString();	
//                          }
//	        	 catch(Exception e){
//	     	    	e.printStackTrace();
//                        }
//                     }
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }
//                qExec.close() ;
//                model.leaveCriticalSection() ;
//    
//		if(verb!=null && e1_arg!=null && e2_arg!=null) {
//                    /*
//                    //Not needed in the Moment
//                    Sentence sentence = this.returnSentence(model);
//                    Templates.getTransitiveVerb(model, lexicon, sentence,verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());*/
//            } 
		
	}

}
