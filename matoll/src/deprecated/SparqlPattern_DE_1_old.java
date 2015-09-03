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

public class SparqlPattern_DE_1_old extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_1_old.class.getName());
	
	/*
PropSubj:Lakshmi Mittal
PropObj:Goldman Sachs
sentence:Lakshmi Niwas Mittal sitzt im Board of Directors von Goldman Sachs . 
1	Lakshmi	Lakshmi	N	NE	Neut|Nom|Sg	4	subj	_	_ 
2	Niwas	Niwas	N	NE	Neut|Nom|Sg	1	app	_	_ 
3	Mittal	Mittal	N	NN	Neut|Nom|Sg	2	app	_	_ 
4	sitzt	sitzen	V	VVFIN	3|Sg|Pres|Ind	0	root	_	_ 
5	im	im	PREP	APPRART	Dat	4	pp	_	_ 
6	Board	Board	N	NN	Neut|Dat|Sg	5	pn	_	_ 
Assuming Board is obj
*/
	//Verb + prep
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?prep ?dobj_form ?e1_arg ?e2_arg  WHERE {"
                            + "?y <conll:cpostag> ?lemma_pos . "
                            + "?y <conll:cpostag> \"V\" ."
                            //Filter auf nicht VA
                            + "?y <conll:lemma> ?lemma . "
                            + "?e1 <conll:head> ?y . "
                            + "?e1 <conll:deprel> ?deprel. "
                            + "FILTER regex(?deprel, \"subj\") ."
                            + "?p <conll:head> ?y . "
                            + "?p <conll:deprel> \"pp\" . "
                            + "?p <conll:form> ?prep . "
                            + "?e2 <conll:head> ?p . "
                            + "?e2 <conll:deprel> ?e2_grammar . "
                            + "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_1";
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
