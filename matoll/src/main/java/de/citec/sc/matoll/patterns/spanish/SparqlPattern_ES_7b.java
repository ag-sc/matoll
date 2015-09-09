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

public class SparqlPattern_ES_7b extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_7b.class.getName());
	
	// X y Y se casaron
	
        // X und Y verheiraten sich
        // template needs to be changed!
        
//	ID:11
//	property subject: Constantino VI
//	property object: Teodote
//	sentence:: 
//	1	En	en	s	SPS00	_	8	MOD
//	2	septiembre_de_795	[??:??/9/795:??.??:??]	w	W	_	1	COMP
//	3	,	,	f	Fc	_	2	punct
//	4	Constantino	constantino	n	NP00000	_	8	SUBJ
//	5	y	y	c	CC	_	4	COORD
//	6	Teodote	teodote	n	NP00000	_	5	CONJ
//	7	se	se	p	P00CN000	_	8	DO
//	8	casaron	casar	v	VMIS3P0	_	0	ROOT
//	9	.	.	f	Fp	_	8	punct
//        
	
//	ID:5f
//	property subject: Claudio
//	property object: Agripinila
//	sentence:: 
//	1	Poco	poco	r	RG	_	2	SPEC
//	2	después	después	r	RG	_	7	MOD
//	3	Claudio	claudio	n	NP00000	_	7	SUBJ
//	4	y	y	c	CC	_	3	COORD
//	5	Agripinila	agripinila	n	NP00000	_	4	CONJ
//	6	se	se	p	P00CN000	_	7	DO
//	7	casaron	casar	v	VMIS3P0	_	0	ROOT
//	8	y	y	c	CC	_	7	COORD
//	9	éste	este	p	PD0MS000	_	10	SUBJ
//	10	adoptó	adoptar	v	VMIS3S0	_	8	CONJ
//	11	a	a	s	SPS00	_	10	DO
//	12	el	el	d	DA0MS0	_	13	SPEC
//	13	hijo	hijo	n	NCMS000	_	11	COMP
//	14	de	de	s	SPS00	_	13	MOD
//	15	Agripinila	agripinila	n	NP00000	_	14	COMP
//	16	,	,	f	Fc	_	15	punct
//	17	Nerón	nerón	n	NP00000	_	13	MOD
//	18	,	,	f	Fc	_	17	punct
//	19	como	como	c	CS	_	10	MOD
//	20	su	su	d	DP3CS0	_	21	SPEC
//	21	heredero	heredero	n	NCMS000	_	19	COMP
//	22	por	por	s	SPS00	_	21	MOD
//	23	encima_de	encima_de	s	SPS00	_	22	COMP
//	24	Británico	británico	n	NP00000	_	23	COMP
//	25	.	.	f	Fp	_	24	punct
	
	
	// omit ?prep
	
	@Override
        public String getQuery() {
            String query = "SELECT ?lemma ?e1_arg ?e2_arg WHERE {"

                            + "?verb <conll:postag> ?verb_pos ."
                            + "FILTER regex(?verb_pos, \"VMIS\") ."
                + "?verb <conll:lemma> ?lemma ."

                            + "?se <conll:lemma> \"se\" ."
                            + "?se <conll:deprel> \"DO\" ."
                            + "?se <conll:head> ?verb ."

                            + "?e1 <conll:head> ?verb."
                            + "?e1 <conll:deprel> \"SUBJ\" ."

                            + "?coord <conll:head> ?e1 ."
                            + "?coord <conll:deprel> \"COORD\" ."
                            + "?coord <conll:lemma> \"y\" ."

                            + "?e2 <conll:head> ?coord ."
                            + "?e2 <conll:deprel> \"CONJ\" ."

                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	
	// we need a new construction for casar+se, wrong type currently selected
        //https://en.wiktionary.org/wiki/casarse
        
        //wenn "se" vorkommt, mit ans lemma hängen
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_ES_7b";
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
