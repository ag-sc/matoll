package de.citec.sc.matoll.patterns.spanish;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;

public class SparqlPattern_ES_3 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_3.class.getName());
	
	
	/*
	 * 
propSubject:flickr
propObject:ludicorp
--------------
sentence:Ludicorp es la empresa creadora de Flickr , sitio web de organizacin de fotografas digitales y red social . 
1	Ludicorp	ludicorp	n	NP00000	_	2	SUBJ
2	es	ser	v	VSIP3S0	_	0	ROOT
3	la	el	d	DA0FS0	_	4	SPEC
4	empresa	empresa	n	NCFS000	_	2	ATR
5	creadora	creador	a	AQ0FS0	_	4	MOD
6	de	de	s	SPS00	_	4	MOD
7	Flickr	flickr	n	NP00000	_	6	COMP
	 */
	String query = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
			+ "{?y <conll:cpostag> ?lemma_pos . "
			+ "?y <conll:deprel> ?lemma_grammar . "
			+ "?y <conll:form> ?lemma . "
			+ "?y <conll:head> ?blank . "
			+ "?y <conll:deprel> \"MOD\" . "
			+ "?y <conll:postag> \"AQ0FS0\". "
			+ "?blank <conll:head> ?verb. "
			+ "?blank <conll:deprel> \"ATR\". "
			+ "?verb <conll:postag> ?verb_pos . "
			+ "FILTER regex(?verb_pos, \"VS\") ."
			+ "?e1 <conll:head> ?verb . "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			+ "FILTER regex(?e1_grammar, \"SUBJ\") ."
			+ "?p <conll:head> ?blank . "
			+ "?p <conll:deprel> \"MOD\". "
			+ "?p <conll:postag> \"SPS00\". "
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
		return "SPARQLPattern_ES_3";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		// TODO Auto-generated method stub
		
	}

}
