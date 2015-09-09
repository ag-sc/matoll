package de.citec.sc.matoll.patterns.spanish;

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

public class SparqlPattern_ES_1 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_1.class.getName());
	
	
	/*
1	Aníbal	aníbal	n	NP00000	_	2	SUBJ	_	_
2	cruza	cruzar	v	VMIP3S0	_	0	ROOT	_	_
3	los	el	d	DA0MP0	_	4	SPEC	_	_
4	Alpes	alpes	n	NP00000	_	2	DO	_	_
5	a	a	s	SPS00	_	2	OBLC	_	_
6	el	el	d	DA0MS0	_	7	SPEC	_	_
7	mando	mando	n	NCMS000	_	5	COMP	_	_
8	de	de	s	SPS00	_	7	MOD	_	_
9	el	el	d	DA0MS0	_	10	SPEC	_	_
10	ejército	ejército	n	NCMS000	_	8	COMP	_	_
11	cartaginés	cartaginés	a	AQ0MS0	_	10	MOD	_	_
12	.	.	f	Fp	_	11	punct	_	_

1	Pese_a	pese_a	s	SPS00	_	4	MOD
2	que	que	p	PR0CN000	_	1	COMP
3	Buckethead	buckethead	n	NP00000	_	4	SUBJ
4	escribió	escribir	v	VMIS3S0	_	0	ROOT
5	"	"	f	Fe	_	4	punct
6	Jordan	jordan	n	NP00000	_	12	SUBJ
7	"	"	f	Fe	_	6	punct


x verb y - ohne preposition
	 */
        @Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?e2_arg  WHERE {"
                            + "?verb <conll:postag> ?pos . "
                            //POSTAG nach VM prüfen Verbos principales (Hauptverb)
                            + "FILTER regex(?pos, \"VMI\") ."
                            + "?verb <conll:lemma> ?lemma . "
                            + "?subj <conll:head> ?verb . "
                            + "?subj <conll:deprel> \"SUBJ\". "
                            + "?dobj <conll:head> ?verb . "
                            + "?dobj <conll:deprel> \"DO\" . "

                            + "?subj <own:senseArg> ?e1_arg. "
                            + "?dobj <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	@Override
	public String getID() {
		return "SPARQLPattern_ES_1";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String verb = null;
                String e1_arg = null;
                String e2_arg = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();


                         try{
                                 verb = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
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
    
		if(verb!=null && e1_arg!=null && e2_arg!=null) {
                    Sentence sentence = this.returnSentence(model);
                    Templates.getTransitiveVerb(model, lexicon, sentence, verb, e1_arg, e2_arg, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
            } 
		
		
	}

}
