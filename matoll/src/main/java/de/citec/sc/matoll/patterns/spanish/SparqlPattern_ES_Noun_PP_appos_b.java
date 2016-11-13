package de.citec.sc.matoll.patterns.spanish;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.citec.sc.lemon.core.Language;
import de.citec.sc.lemon.core.Lexicon;
import de.citec.sc.lemon.core.Sentence;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_ES_Noun_PP_appos_b extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_Noun_PP_appos_b.class.getName());
	
	

//	New parse:
//	(basically new pattern...)
//	1	Hermann_Fegelein	hermann_fegelein	n	NP00000	_	2	SUBJ	_	_
//	2	es	ser	v	VSIP3S0	_	0	ROOT	_	_
//	3	más	más	r	RG	_	4	SPEC	_	_
//	4	conocido	conocer	v	VMP00SM	_	2	ATR	_	_
//	5	por	por	s	SPS00	_	4	BYAG	_	_
//	6	ser	ser	v	VSN0000	_	5	COMP	_	_
//	7	cuñado	cuñado	n	NCMS000	_	6	ATR	_	_
//	8	de	de	s	SPS00	_	7	COMP	_	_
//	9	la	el	d	DA0FS0	_	10	SPEC	_	_
//	10	esposa	esposo	n	NCFS000	_	8	COMP	_	_
//	11	de	de	s	SPS00	_	10	COMP	_	_
//	12	Adolf_Hitler	adolf_hitler	n	NP00000	_	11	COMP	_	_
//	13	,	,	f	Fc	_	12	punct	_	_
//	14	Eva_Braun	eva_braun	n	NP00000	_	12	MOD	_	_
//	15	.	.	f	Fp	_	14	punct	_	_
	
// Constraint: not embedded in a further prepositional phrase, too noisy
	
	@Override
        public String getQuery() {
            String query = "SELECT ?lemma ?adjective_lemma ?lemma_wordnumber ?adjective_wordnumber ?e1_arg ?e2_arg ?prep  WHERE {"

                            + "?noun <conll:postag> ?lemma_pos . "
                            + "FILTER regex(?lemma_pos, \"NC\") ."
                            + "?noun <conll:lemma> ?lemma . "
                            + "?noun <conll:wordnumber> ?lemma_wordnumber ."

                            + " OPTIONAL {"
                            + "?adjective <conll:form> ?adjective_lemma . "
                            + "?adjective <conll:head> ?noun . "
                            + "?adjective <conll:wordnumber> ?adjective_wordnumber ."
                            + "?adjective <conll:deprel> \"MOD\" . "
                            + "?adjective <conll:cpostag> \"a\".  }"

                            + "?p <conll:head> ?noun ."
                            + "?p <conll:postag> ?prep_pos ."
                            + "FILTER regex(?prep_pos, \"SPS\") ."
                            + "?p <conll:deprel> \"COMP\" ."
                            + "?p <conll:lemma> ?prep ."

                            + "?e1 <conll:head> ?p ."
                            + "?e1 <conll:deprel> \"COMP\" ."
                            + "?e2 <conll:head> ?e1."
                            + "{?e2 <conll:deprel> \"MOD\".} UNION "
                            + "{?e2 <conll:deprel> \"COMP\".}"
                            + "?e1 <own:senseArg> ?e1_arg. "
                            + "?e2 <own:senseArg> ?e2_arg. "
                            + "}";
            return query;
        }
	@Override
	public String getID() {
		return "SparqlPattern_ES_Noun_PP_appos_b";
	}

	@Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {
		
		
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String noun = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;
                String adjective_lemma = null;
                int adjective_wordnumber = 0;
                int lemma_wordnumber = 0;

                 while ( rs.hasNext() ) {
                     QuerySolution qs = rs.next();

                     try{
                             noun = qs.get("?lemma").toString();
                             e1_arg = qs.get("?e1_arg").toString();
                             e2_arg = qs.get("?e2_arg").toString();	
                             preposition = qs.get("?prep").toString();
                             try{
                                 adjective_lemma = qs.get("?adjective_lemma").toString();
                                 adjective_wordnumber = Integer.getInteger(qs.get("?adjective_wordnumber").toString());
                                 lemma_wordnumber = Integer.getInteger(qs.get("?lemma_wordnumber").toString());
                             }
                             catch(Exception e){}
                             if(noun!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                                 Sentence sentence = this.returnSentence(model);
                                 if(adjective_lemma!=null){
                                     if(lemma_wordnumber<adjective_wordnumber)
                                        Templates.getNounWithPrep(model, lexicon, sentence, noun+" "+adjective_lemma, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
                                     else
                                        Templates.getNounWithPrep(model, lexicon, sentence, adjective_lemma+" "+noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());

                                 }
                                 else
                                    Templates.getNounWithPrep(model, lexicon, sentence, noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
                             }
                     }
                     catch(Exception e){
                    e.printStackTrace();
                    }
                 }

                qExec.close() ;
    

	}

}
