package de.citec.sc.matoll.patterns.spanish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
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
			String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
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
					+ "?e1 <conll:deprel> ?e1_grammar . "
					+ "FILTER regex(?e1_grammar, \"SUBJ\") ."
					+ "?p <conll:head> ?y . "
					+ "?p <conll:form> ?prep . "
					+ "?e2 <conll:head> ?p . "
					+ "?e2 <conll:postag> \"NP00000\" . "
					+ "?e2 <conll:deprel> ?e2_grammar . "
					+ "FILTER regex(?e2_grammar, \"COMP\") ."
					+ "?e1 <own:senseArg> ?e1_arg. "
					+ "?e2 <own:senseArg> ?e2_arg. "
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
		
		Templates.getNounWithPrep(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(), this.getDebugger());
		
	}

}
