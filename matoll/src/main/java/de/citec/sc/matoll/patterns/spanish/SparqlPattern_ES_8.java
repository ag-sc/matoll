package de.citec.sc.matoll.patterns.spanish;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_ES_8 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_8.class.getName());
	
	
	// should be subsumed by 3 or 4 or 6...
	
	// fue + es

// ser o estar
//está casado con / estuvo casado / ha actuado en...
//
//New Parse:
//1	El	el	d	DA0MS0	_	2	SPEC	_	_
//2	proyecto	proyecto	n	NCMS000	_	3	SUBJ	_	_
//3	está	estar	v	VAIP3S0	_	0	ROOT	_	_
//4	liderado	liderar	v	VMP00SM	_	3	ATR	_	_
//5	por	por	s	SPS00	_	4	BYAG	_	_
//6	Theo_de_Raadt	theo_de_raadt	n	NP00000	_	5	COMP	_	_
//7	,	,	f	Fc	_	6	punct	_	_
//8	residente	residente	n	NCCS000	_	6	_	_	_
//9	en	en	s	SPS00	_	8	MOD	_	_
//10	Calgary	calgary	n	NP00000	_	9	COMP	_	_
//11	.	.	f	Fp	_	10	punct	_	_

        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"

                            + "?copula <conll:lemma> \"estar\" ."
                            + "?copula <conll:postag> ?copula_pos ."
                            + "FILTER regex(?copula_pos, \"VAIP\") ."


                            + "?participle <conll:postag> ?participle_pos . "
                            + "FILTER regex(?participle_pos, \"VMP\") ."
                            + "?participle <conll:lemma> ?lemma . "
                            + "?participle <conll:head> ?copula . "
                            + "?participle <conll:deprel> \"ATR\" . "

                            + "?e1 <conll:head> ?copula . "
                            + "?e1 <conll:deprel> \"SUBJ\". "

                            + "?p <conll:head> ?participle . "
                            + "?p <conll:postag> ?prep_pos . "
                            + "FILTER regex(?prep_pos, \"SPS\") ."
                            + "?p <conll:lemma> ?prep . "
                            // can be OBLC as well
                            + "?p <conll:deprel> \"BYAG\" ."

                            + "?e2 <conll:head> ?p . "
                            + "?e2 <conll:deprel> \"COMP\" . "

                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
			
	@Override
	public String getID() {
		return "SPARQLPattern_ES_8";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {

		List<String> sentences = this.getSentences(model);
		
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
    
		if(noun!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Templates.getNounWithPrep(model, lexicon, sentences, noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
            } 
		
	}

}
