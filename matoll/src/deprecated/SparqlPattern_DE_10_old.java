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
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;
import org.apache.jena.shared.Lock;

public class SparqlPattern_DE_10_old extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_10_old.class.getName());
	
	
	/*
	 * PropSubj:Yangtze River Express
PropObj:2003 Januar 
sentence:Yangtze River Express wurde am 15. Januar 2003 gegründet . 
1	Yangtze	Yangtze	N	NN	_|Nom|Sg	4	subj	_	_ 
2	River	River	N	NE	_|Nom|Sg	1	app	_	_ 
3	Express	Express	N	NE	_|Nom|Sg	2	app	_	_ 
4	wurde	werden	V	VAFIN	3|Sg|Past|Ind	0	root	_	_ 
5	am	am	PREP	APPRART	Dat	9	pp	_	_ 
6	15.	15.	ADJA	ADJA	_|Masc|Dat|Sg|_|	7	attr	_	_ 
7	Januar	Januar	N	NE	Masc|Dat|Sg	5	pn	_	_ 
8	2003	2003	CARD	CARD	_	7	app	_	_ 
9	gegründet	gründen	V	VVPP	_	4	aux	_	_ 
10	.	.	$.	$.	_	0	root	_	_ 	
----------------------
	 */
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep WHERE {"
                            + "?e1 <conll:head> ?verb . "
                            + "?e1 <conll:deprel> ?e1_grammar . "
                            + "FILTER regex(?e1_grammar, \"subj\") ."
                            + "?y <conll:cpostag> \"V\" . "
                            + "?y <conll:deprel> ?lemma_grammar . "
                            + "FILTER( regex(?lemma_grammar, \"aux\"))"
                            + "?y <conll:lemma> ?lemma . "
                            + "?y <conll:head> ?verb . "
                            + "?verb <conll:cpostag> \"V\" ."
                            //+ "?verb <conll:lemma> \"sein\" ."
                            + "?p <conll:head> ?y . "
                            + "?p <conll:deprel> \"pp\" . "
                            + "?p <conll:form> ?prep . "
                            + "?e2 <conll:head> ?p . "
                            + "?e2 <conll:deprel> ?e2_grammar . "
                            + "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
                            + "?e2 <conll:form> ?e2_form . "
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	

	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_10";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		List<String> sentences = this.getSentences(model);
		model.enterCriticalSection(Lock.READ) ;
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 verb = qs.get("?lemma").toString();
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
    
		if(verb!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Templates.getIntransitiveVerb(model, lexicon, sentences, verb, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
	}

}
