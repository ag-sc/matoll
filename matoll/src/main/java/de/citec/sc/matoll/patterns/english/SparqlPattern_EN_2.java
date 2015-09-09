package de.citec.sc.matoll.patterns.english;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_EN_2 extends SparqlPattern {

	Logger logger = LogManager.getLogger(SparqlPattern_EN_2.class.getName());
	
    @Override
    public String getQuery() {
	String query = "SELECT ?prefix ?prep ?lemma ?e1_arg ?e2_arg WHERE {"
			
			+ "?y <conll:form> ?lemma . "
			+ "{?y <conll:cpostag> \"NN\" . }"
			+ "UNION"
			+ "{?y <conll:cpostag> \"NNS\" . }"
			+"OPTIONAL{"
			+ "?modifier <conll:head> ?y. "
			+ "?modifier <conll:form> ?prefix. "
			+ "?modifier <conll:deprel> \"nn\"."
			+"} "
                        + "{?e1 <conll:head> ?y . "
                        + "?e1 <conll:deprel> \"appos\"."
                        + "}"
                        + "UNION"
                        + "{?y <conll:head> ?e1 . "
                        + "{?y <conll:deprel> \"appos\".} UNION {?y <conll:deprel> \"dep\".}"
                        + "}"
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"prep\" . "
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel>  \"pobj\". "
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
        return query;
    }
	

	
        @Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
                
                QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String noun = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;
                String modifier = "";

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 noun = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
                                 preposition = qs.get("?prep").toString();
                                 try{
                                     modifier = qs.get("?prefix").toString();
                                 }
                                 catch(Exception e){
                                     
                                 }
                                 
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
    
		if(noun!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Sentence sentence = this.returnSentence(model);
                    if (!modifier.equals("")){
                        Templates.getNounWithPrep(model, lexicon, sentence, modifier +" "+noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
                    }
                    else Templates.getNounWithPrep(model, lexicon, sentence, noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
            } 
                
				
	}

        @Override
	public String getID() {
		return "SPARQLPattern_EN_2";
	}

}
