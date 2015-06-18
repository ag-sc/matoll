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
			String query = "SELECT ?lemma ?subj_arg ?pobj_arg ?prep_form  WHERE {"
					+ "?noun <conll:form> ?lemma . "
					+ "?noun <conll:head> ?copula . "
					+ "?noun <conll:cpostag> \"n\" . "
					+ "?noun <conll:deprel \"ATR\""
					
					+ "?prep <conll:deprel> \"MOD\" . "
					+ "?prep <conll:postag> ?prep_pos ."
					+ "FILTER regex(?prep_pos, \"SPS\") ."
					+ "?prep <conll:head> ?noun . "
					+ "?prep <conll:form> ?prep_form . "
					
					+ "?copula <conll:postag> ?pos . "
					+ "FILTER regex(?pos, \"VSIP\") ."
					+ "?copula <conll:lemma> \"ser\" "
					
					+ "?subj <conll:head> ?copula . "
					+ "?subj <conll:deprel> ?dep . "
					+ "FILTER regex(?dep, \"SUBJ\") ."
					
					
					+ "?pobj <conll:head> ?prep . "
					+ "?pobj <conll:postag> \"NP00000\" . "
					+ "?pobj <conll:deprel> ?e2_grammar . "
					+ "FILTER regex(?e2_grammar, \"COMP\") ."
					
					+ "?subj <own:senseArg> ?subj_arg. "
					+ "?pobj <own:senseArg> ?pobj_arg. "
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
