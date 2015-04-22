package de.citec.sc.matoll.patterns.spanish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_ES_4 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_4.class.getName());
	
	
	/*
	 //Old parse:
	PropSubj:Eva Braun
	PropObj:Adolf Hitler
	sentence:Hermann Fegelein es más conocido por ser cuñado de la esposa de Adolf Hitler , Eva Braun . 
	1	Hermann	_	n	NCMS000	_	0	ROOT
	2	Fegelein	_	n	NP00000	_	1	MOD
	3	es	_	v	VSIP3S0	_	0	ROOT
	4	más	_	r	RG	_	5	SPEC
	5	conocido	_	v	VMP00SM	_	3	ATR
	6	por	_	s	SPS00	_	5	BYAG
	7	ser	_	v	VSN0000	_	6	COMP
	8	cuñado	_	v	VMP00SM	_	3	MOD
	9	de	_	s	SPS00	_	3	MOD
	10	la	_	d	DA0FS0	_	11	SPEC
	11	esposa	_	n	NCFS000	_	9	COMP
	12	de	_	s	SPS00	_	11	COMP
	13	Adolf	_	n	NP00000	_	12	COMP
	14	Hitler	_	n	NP00000	_	11	MOD
	15	,	_	f	Fc	_	14	punct
	16	Eva	_	n	NP00000	_	11	MOD
	17	Braun	_	n	NP00000	_	16	MOD
	18	.	_	f	Fp	_	17	punct
	New parse:
	(basically new pattern...)
	1	Hermann_Fegelein	hermann_fegelein	n	NP00000	_	2	SUBJ	_	_
	2	es	ser	v	VSIP3S0	_	0	ROOT	_	_
	3	más	más	r	RG	_	4	SPEC	_	_
	4	conocido	conocer	v	VMP00SM	_	2	ATR	_	_
	5	por	por	s	SPS00	_	4	BYAG	_	_
	6	ser	ser	v	VSN0000	_	5	COMP	_	_
	7	cuñado	cuñado	n	NCMS000	_	6	ATR	_	_
	8	de	de	s	SPS00	_	7	COMP	_	_
	9	la	el	d	DA0FS0	_	10	SPEC	_	_
	10	esposa	esposo	n	NCFS000	_	8	COMP	_	_
	11	de	de	s	SPS00	_	10	COMP	_	_
	12	Adolf_Hitler	adolf_hitler	n	NP00000	_	11	COMP	_	_
	13	,	,	f	Fc	_	12	punct	_	_
	14	Eva_Braun	eva_braun	n	NP00000	_	12	MOD	_	_
	15	.	.	f	Fp	_	14	punct	_	_

	TODO:Check Query

		 */
			String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
					+ "?y <conll:postag> ?lemma_pos . "
					+ "FILTER regex(?lemma_pos, \"NC\") ."
					+ "?y <conll:deprel> ?lemma_grammar . "
					//check if nois goes away, if COMP is set as relation of the lemma to the head
					+ "?y <conll:deprel> \"COMP\" . "
					+ "?y <conll:form> ?lemma . "
					//has a preposition, but is not between the entities
					+ "{?p <conll:head> ?y . "
					+ "?e1 <conll:head> ?y . "
					+ "?e2 <conll:head> ?y . }"
					+ "UNION"
					+ "{?p <conll:head> ?y . "
					+ "?e1 <conll:head> ?p . "
					+ "?e2 <conll:head> ?e1 . }"
					+ "?p <conll:postag> ?p_pos . "
					+ "?p <connl:form> ?prep ."
					+ "FILTER regex(?p_pos, \"SP\") ."
					+ "?e1 <conll:form> ?e1_form . "
					+ "?e1 <conll:postag> ?e1_pos . "
					+ "FILTER regex(?e1_pos, \"NP0\") ."
					+ "?e1 <conll:deprel> ?e1_grammar . "
					+ "FILTER regex(?e1_grammar, \"MOD\") ."
					+ "?e2 <conll:postag> ?e2_pos . "
					+ "FILTER regex(?e2_pos, \"NP0\") ."
					+ "?e2 <conll:deprel> ?e2_grammar . "
					+ "FILTER regex(?e2_grammar, \"MOD\") ."
					+ "?e2 <conll:form> ?e2_form . "
					+ "?y <own:partOf> ?class. "
					+ "?e1 <own:senseArg> ?e1_arg. "
					+ "?e2 <own:senseArg> ?e2_arg. "
					+ "}";
	@Override
	public String getID() {
		return "SPARQLPattern_ES_4";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();

		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getNounWithPrep(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(), this.getDebugger());		
	}

}
