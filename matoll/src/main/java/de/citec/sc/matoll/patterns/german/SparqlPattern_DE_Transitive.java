package de.citec.sc.matoll.patterns.german;

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

public class SparqlPattern_DE_Transitive extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_Transitive.class.getName());
	
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
                            + "{?lemma_addidtion <conll:deprel> \"obji\".} UNION {?lemma_addidtion <conll:deprel> \"avz\". } "
                            + "?lemma_addition <connl:lemma> ?additional_lemma .} "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	
	@Override
	public String getID() {
		return "SparqlPattern_DE_Transitive";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {

		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;
                String additional_lemma = "";

                int number = 0;
                while ( rs.hasNext() ) {
                    QuerySolution qs = rs.next();
                    number+=1;


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

                qExec.close() ;
    
		if(verb!=null && e1_arg!=null && e2_arg!=null && number == 1) {
                    Sentence sentence = this.returnSentence(model);
                    if(!additional_lemma.equals("")){
                        Templates.getTransitiveVerb(model, lexicon, sentence, additional_lemma +" "+verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
                    }
                    else Templates.getTransitiveVerb(model, lexicon, sentence,verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
	}

}
