package de.citec.sc.matoll.patterns.spanish;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;

public class SparqlPattern_ES_8 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_8.class.getName());
	
	
	/*
	 * PropSubj:Theo de Raadt
PropObj:Calgary
sentence:El proyecto está liderado por Theo de Raadt , residente en Calgary . 
1	El	_	d	DA0MS0	_	2	SPEC
2	proyecto	_	n	NCMS000	_	3	SUBJ
3	está	_	v	VAIP3S0	_	0	ROOT
4	liderado	_	v	VMP00SM	_	3	ATR
5	por	_	s	SPS00	_	4	BYAG
6	Theo	_	n	NCMS000	_	5	COMP
7	de	_	s	SPS00	_	6	MOD
8	Raadt	_	n	NP00000	_	7	COMP
9	,	_	f	Fc	_	8	punct
10	residente	_	r	RG	_	4	MOD
11	en	_	s	SPS00	_	4	MOD
12	Calgary	_	n	NP00000	_	11	COMP
13	.	_	f	Fp	_	12	punct
residente en 

New Parse:
1	El	el	d	DA0MS0	_	2	SPEC	_	_
2	proyecto	proyecto	n	NCMS000	_	3	SUBJ	_	_
3	está	estar	v	VAIP3S0	_	0	ROOT	_	_
4	liderado	liderar	v	VMP00SM	_	3	ATR	_	_
5	por	por	s	SPS00	_	4	BYAG	_	_
6	Theo_de_Raadt	theo_de_raadt	n	NP00000	_	5	COMP	_	_
7	,	,	f	Fc	_	6	punct	_	_
8	residente	residente	n	NCCS000	_	6	_	_	_
9	en	en	s	SPS00	_	8	MOD	_	_
10	Calgary	calgary	n	NP00000	_	9	COMP	_	_
11	.	.	f	Fp	_	10	punct	_	_

	 */
	String query = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
			+ "{?y <conll:cpostag> ?lemma_pos . "
			+ "?y <conll:cpostag> \"n\" . "
			+" ?y <conll:postag> \"NCCS000\" ."
			+ "?y <conll:deprel> ?lemma_grammar . "
			+ "?y <conll:form> ?lemma . "
			+ "?y <conll:head> ?e1 . "
			+ "?y <conll:deprel> \"_\" . "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			+ "?e1 <conll:cpostag> \"n\" . "
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"MOD\" . "
			+ "?p <conll:postag> \"SPS00\" ."
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:cpostag> \"n\" . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			+ "FILTER regex(?e2_grammar, \"COMP\") ."
			+ "?e2 <conll:form> ?e2_form . "
			+ "?y <own:partOf> ?class. "
			+ "?class <own:subj> ?propSubj. "
			+ "?class <own:obj> ?propObj. "
			+ "}";
			
	@Override
	public String getID() {
		return "SPARQLPattern_ES_8";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		// TODO Auto-generated method stub
		
	}

}