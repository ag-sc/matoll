package de.citec.sc.matoll.patterns.english;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;


public class SparqlPattern_EN_Predicative_Participle_copulative extends SparqlPattern {

	Logger logger = LogManager.getLogger(SparqlPattern_EN_Predicative_Participle_copulative.class.getName());

	
        @Override
        public String getQuery() {
            String query = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                            + "SELECT ?lemma ?lemma_addition ?prep ?e1_arg ?e2_arg WHERE{"
                            + "?e1 <conll:deprel> \"nsubj\" . "
                            + "?e1 <conll:cpostag> ?e1_pos . "
                            //+ "FILTER regex(?e1_pos, \"NN\") ."
                            + "?e1 <conll:head> ?y . "
                            + "{?y <conll:cpostag> \"JJ\" . }"
                            + " UNION "
                            + "{?y <conll:cpostag> \"VBN\" . }"
                            + "?y <conll:form> ?lemma . "
                            + "?y <conll:wordnumber> ?wordnumber ."
                            +"OPTIONAL{"
                            + "?lemma_nn <conll:head> ?y. "
                            + "?lemma_nn <conll:form> ?lemma_addition. "
                            + "?lemma_nn <conll:deprel> \"nn\"."
                            + "?lemma_nn <conll:wordnumber> ?wordnumber_lemma_nn ."
                            + "FILTER(?wordnumber_lemma_nn<?wordnumber)."
                            +"} "
                            //+ "?verb <conll:head> ?y . "
                            + "?p <conll:head> ?y . "
                            + "?p <conll:deprel> \"prep\" . "
                            + "?p <conll:form> ?prep . "
                            + "?e2 <conll:head> ?p . "
                            + "?e2 <conll:deprel> \"pobj\" . "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }

        @Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {

		
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String adjective = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;
                String lemma_addition = "";

                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();

                         try{
                                 adjective = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
                                 preposition = qs.get("?prep").toString();
                                 try{
                                     lemma_addition = qs.get("?lemma_addition").toString();
                                 }
                                 catch(Exception e){
                                 }
                                 if(adjective!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                                     Sentence sentence = this.returnSentence(model);
                                     if(!lemma_addition.equals("")){
                                         Templates.getAdjective(model, lexicon, sentence, lemma_addition+" "+adjective, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());

                                     }
                                     else Templates.getAdjective(model, lexicon, sentence, adjective, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
                                 }
                         }
	        	 catch(Exception e){
	     	    	e.printStackTrace();
                        }
                     }

                qExec.close() ;
    


	}

        @Override
	public String getID() {
		return "SparqlPattern_EN_Predicative_Participle_copulative";
	}

}
