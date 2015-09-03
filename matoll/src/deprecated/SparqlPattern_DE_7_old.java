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

public class SparqlPattern_DE_7_old extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_7_old.class.getName());
	
	/*
	 * 
	PropSubj:Tom Hayden
	PropObj:Barbara Williams
	sentence:Barbara Williams ist verheiratet mit dem sozialpolitischen Aktivisten Tom Hayden , mit dem sie gemeinsam in Los Angeles lebt . 
	1	Barbara	Barbara	N	NE	_|Nom|Sg	3	subj	_	_ 
	2	Williams	Williams	N	NE	_|Nom|Sg	1	app	_	_ 
	3	ist	sein	V	VAFIN	3|Sg|Pres|Ind	0	root	_	_ 
	4	verheiratet	verheiratet	ADV	ADJD	Pos|_	3	pred	_	_ 
	5	mit	mit	PREP	APPR	Dat	3	pp	_	_ 
	6	dem	das	ART	ART	Def|Masc|Dat|Sg	8	det	_	_ 
	7	sozialpolitischen	sozialpolitisch	ADJA	ADJA	Pos|Masc|Dat|Sg|_|	8	attr	_	_ 
	8	Aktivisten	Aktivist	N	NN	Masc|Dat|Sg	5	pn	_	_ 
	9	Tom	Tom	N	NE	Masc|Dat|Sg	8	app	_	_ 
	10	Hayden	Hayden	N	NE	Masc|Dat|Sg	9	app	_	_ 
	11	,	,	$,	$,	_	0	root	_	_ 
	12	mit	mit	PREP	APPR	Dat	19	pp	_	_ 
	13	dem	das	PRO	PRELS	Masc|Dat|Sg	12	pn	_	_ 
	14	sie	sie	PRO	PPER	3|Sg|Fem|Nom	19	subj	_	_ 
	15	gemeinsam	gemeinsam	ADV	ADJD	Pos|	16	adv	_	_ 
	16	in	in	PREP	APPR	_	19	pp	_	_ 
	17	Los	Los	N	NN	Neut|_|Sg	16	pn	_	_ 
	18	Angeles	Angeles	N	NE	Neut|_|Sg	17	app	_	_ 
	19	lebt	leben	V	VVFIN	3|Sg|Pres|Ind	10	rel	_	_ 
	20	.	.	$.	$.	_	0	root	_	_ 	
	----------------------
	 */
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?prep ?e1_arg ?e2_arg WHERE{"
                            + "?e1 <conll:deprel> ?e1_grammar . "
                            + "FILTER regex(?e1_grammar, \"subj\") ."
                            + "?e1 <conll:head> ?verb . "
                            + "?verb <conll:cpostag> \"V\" . "
                            + "?y <conll:head> ?verb . "
                            + "?y <conll:postag> ?lemma_pos . "
                            + "FILTER regex(?lemma_pos, \"ADJ\") ."
                            + "?y <conll:lemma> ?lemma . "
                            + "?p <conll:head> ?verb . "
                            + "?p <conll:deprel> \"pp\" . "
                            + "?p <conll:form> ?prep . "
                            + "?e2 <conll:head> ?p . "
                            + "?e2 <conll:deprel> ?e2_grammar . "
                            + "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
                            //+ "{?e2 <conll:deprel> \"pn\" . }"
                            //+ "UNION"
                            //+ "FILTER regex(?e2_grammar, \"obj\") ."
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
			
	
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_7";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		List<String> sentences = this.getSentences(model);
		
                model.enterCriticalSection(Lock.READ) ;
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String adjective = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 adjective = qs.get("?lemma").toString();
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
    
		if(adjective!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Templates.getAdjective(model, lexicon, sentences, adjective, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.DE,getID());
            } 
		
		
	}

}
