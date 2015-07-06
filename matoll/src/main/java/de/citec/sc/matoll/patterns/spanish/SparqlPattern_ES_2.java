package de.citec.sc.matoll.patterns.spanish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_ES_2 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_2.class.getName());
	
	
//	ID:1e7
//	property subject: Olibrio
//	property object: Placidia
//	sentence:: 
//	1	Placidia	placidia	n	NP00000	_	2	SUBJ
//	2	fue	ser	v	VSIS3S0	_	0	ROOT
//	3	una	uno	d	DI0FS0	_	4	SPEC
//	4	consorte	consorte	n	NCCS000	_	2	ATR
//	5	,	,	f	Fc	_	4	punct
//	6	esposa	esposo	n	NCFS000	_	2	ATR
//	7	de	de	s	SPS00	_	6	COMP
//	8	el	el	d	DA0MS0	_	9	SPEC
//	9	de	de	s	SPS00	_	7	COMP
//	10	Occidente	occidente	n	NP00000	_	9	COMP
//	11	,	,	f	Fc	_	10	punct
//	12	Olibrio	olibrio	n	NP00000	_	10	MOD
//	13	.	.	f	Fp	_	12	punct


	
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
			String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
					+ "?noun <conll:lemma> ?lemma . "
					+ "?noun <conll:head> ?copula . "
					+ "?noun <conll:cpostag> \"n\" . "
					+ "?noun <conll:deprel> \"ATR\" ."
					
					// can be also COMP
					+ "?prep <conll:deprel> \"MOD\" . "
					+ "?prep <conll:postag> ?prep_pos ."
					+ "FILTER regex(?prep_pos, \"SPS\") ."
					+ "?prep <conll:head> ?noun . "
					+ "?prep <conll:lemma> ?prep . "
					
					+ "?copula <conll:postag> ?pos . "
					// can be VSII1S0 or "v"
					+ "FILTER regex(?pos, \"VSI\") ."
					+ "?copula <conll:lemma> \"ser\" ."
					
					+ "?subj <conll:head> ?copula . "
					+ "?subj <conll:deprel> \"SUBJ\" . "
					
					+ "?pobj <conll:head> ?prep . "
					+ "?pobj <conll:postag> \"n\" . "
					+ "?pobj <conll:deprel> \"COMP\" . "
					
					+ "?subj <own:senseArg> ?e1_arg. "
					+ "?pobj <own:senseArg> ?e2_arg. "
					+ "}";
	
	@Override
	public String getID() {
		return "SPARQLPattern_ES_2";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();

		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getNounWithPrep(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
		
	}

}
