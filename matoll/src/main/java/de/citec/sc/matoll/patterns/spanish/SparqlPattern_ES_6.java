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

public class SparqlPattern_ES_6 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_6.class.getName());
	

	/*Neuer Parse:
	 * 1	Su	su	d	DP3CS0	_	2	SPEC	_	_
2	primo	primo	n	NCMS000	_	4	SUBJ	_	_
3	Felipe_de_Suabia	felipe_de_suabia	n	NP00000	_	2	MOD	_	_
4	estaba	estar	v	VAII1S0	_	0	ROOT	_	_
5	casado	casar	v	VMP00SM	_	4	ATR	_	_
6	con	con	s	SPS00	_	5	OBLC	_	_
7	Irene_Angelina	irene_angelina	n	NP00000	_	6	COMP	_	_

// create 6b
 * 
 * 1	«	«	f	Fz	_	4	MOD
2	Baby	baby	n	NP00000	_	1	MOD
3	»	»	f	Fz	_	4	SUBJ
4	fue	ser	v	VSIS3S0	_	0	ROOT
5	escrita	escribir	v	VMP00SF	_	4	ATR
6	por	por	s	SPS00	_	5	BYAG
7	el	el	d	DA0MS0	_	8	SPEC
8	cantante	cantante	n	NCCS000	_	6	COMP
9	Justin_Bieber	justin_bieber	n	NP00000	_	8	MOD
10	con	con	s	SPS00	_	8	MOD
11	"	"	f	Fe	_	10	COMP
12	Christopher	christopher	n	NP00000	_	8	MOD

// create 6c
 * 
 * 3	Beyoncé	beyoncé	n	NP00000	_	4	MOD
14	había	haber	v	VAII1S0	_	15	AUX
15	escrito	escribir	v	VMP00SM	_	12	COMP
16	para	para	s	SPS00	_	15	MOD
17	"	"	f	Fe	_	16	COMP
18	Survivor	survivor	n	NP00000	_	15	DO

	 * 
	 */
        @Override
        public String getQuery() {
            String query= "SELECT ?lemma ?e1_arg ?e2_arg ?prep WHERE {"

                            + "?e1 <conll:head> ?estar ."
                            + "?e1 <conll:deprel> \"SUBJ\"."

                            + "?estar <conll:lemma> \"estar\" ."
                            + "?estar <conll:cpostag> ?pos ."
                            + "FILTER regex(?pos, \"VAIS\") ."

                            + "?participle <conll:lemma> ?lemma ."
                            + "?participle <conll:head> ?estar ."
                            + "?participle <conll:deprel> \"ATR\". "

                            + "?p <conll:lemma> ?prep ."
                            + "?p <conll:head> ?participle ."
                            + "?p <conll:deprel> \"OBLC\" ."


                            + "?e2 <conll:head> ?p ."
                            + "?e2 <conll:deprel> \"COMP\"."


                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
			
	@Override
	public String getID() {
		return "SPARQLPattern_ES_6";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		List<String> sentences = this.getSentences(model);
		
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
    
		if(adjective!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Templates.getAdjective(model, lexicon, sentences, adjective, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
            }
		
	}

}
