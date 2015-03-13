package de.citec.sc.matoll.patterns.spanish;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;

public class SparqlPattern_ES_2 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_2.class.getName());
	
	
	/*
	1	T'Pel	t'pel	n	NCMS000	_	2	SUBJ	_	_
	2	es	ser	v	VSIP3S0	_	0	ROOT	_	_
	3	la	el	d	DA0FS0	_	4	SPEC	_	_
	4	esposa	esposo	n	NCFS000	_	2	ATR	_	_
	5	de	de	s	SPS00	_	4	MOD	_	_
	6	Tuvok	tuvok	n	NP00000	_	5	COMP	_	_
	7	.	.	f	Fp	_	6	punct	_	_

	//query2 auch ok nach neuem Parse

	 */
			String query = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
					+ "{?y <conll:cpostag> ?lemma_pos . "
					+ "?y <conll:deprel> ?lemma_grammar . "
					+ "?y <conll:form> ?lemma . "
					+ "?y <conll:head> ?verb . "
					+ "?y <conll:deprel> \"ATR\" . "
					+ "?p <conll:postag> \"SPS00\" ."
					+ "?y <conll:cpostag> \"n\" . "
					+" ?y <conll:postag> \"NCFS000\" ."
					+ "?p <conll:deprel> \"MOD\" . "
					+ "?verb <conll:postag> ?verb_pos . "
					+ "FILTER regex(?verb_pos, \"VS\") ."
					+ "?e1 <conll:head> ?verb . "
					+ "?e1 <conll:form> ?e1_form . "
					+ "?e1 <conll:deprel> ?e1_grammar . "
					+ "FILTER regex(?e1_grammar, \"SUBJ\") ."
					+ "?p <conll:head> ?y . "
					+ "?p <conll:form> ?prep . "
					+ "?e2 <conll:head> ?p . "
					+ "?e2 <conll:postag> \"NP00000\" . "
					+ "?e2 <conll:deprel> ?e2_grammar . "
					+ "FILTER regex(?e2_grammar, \"COMP\") ."
					+ "?e2 <conll:form> ?e2_form . "
					+ "?y <own:partOf> ?class. "
					+ "?class <own:subj> ?propSubj. "
					+ "?class <own:obj> ?propObj. "
					+ "}";
	
	@Override
	public String getID() {
		return "SPARQLPattern_ES_2";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		// TODO Auto-generated method stub
		
	}

}
