package de.citec.sc.matoll.patterns.spanish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_ES_7 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_7.class.getName());
	
	
	/*
	 * SearchTerm 1:similar
SearchTerm 2:al
sentence:El toso es similar al iwai-zake . 
1	El	_	d	DA0MS0	_	2	SPEC
2	toso	_	a	AQ0MS0	_	3	SUBJ
3	es	_	v	VSIP3S0	_	0	ROOT
4	similar	_	a	AQ0CS0	_	3	MOD
5	al	_	a	AQ0CS0	_	6	MOD
6	iwai-zake	_	n	NCMS000	_	3	DO
7	.	_	f	Fp	_	6	punct
Adjective

NEW PARSE:
1	El	el	d	DA0MS0	_	2	SPEC	_	_
2	toso	toser	v	VMIP1S0	_	3	SUBJ	_	_
3	es	ser	v	VSIP3S0	_	0	ROOT	_	_
4	similar	similar	a	AQ0CS0	_	3	ATR	_	_
5	a	a	s	SPS00	_	4	COMP	_	_
6	el	el	d	DA0MS0	_	7	SPEC	_	_
7	iwai-zake	iwai-zake	n	NCMS000	_	5	COMP	_	_
8	.	.	f	Fp	_	7	punct	_	_


	 */
	String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
			+ "?y <conll:cpostag> \"v\" ."
			+ "?y <conll:deprel> \"ROOT\" ."
			+ "?adjective <conll:form> ?lemma . "
			+ "?adjective <conll:deprel> ?lemma_grammar. "
			+ "?adjective <conll:head> ?y ."
			+ "?adjective <conll:cpostag> \"a\" . "
			//+ "?adjective <conll:deprel> \"MOD\" . "
			+ "?adjective <conll:deprel> \"ATR\" . "
			+ "?e1 <conll:head> ?y . "
			+ "?e1 <conll:deprel> ?deprel. "
			+ "FILTER regex(?deprel, \"SUBJ\") ."
			+ "?e2 <conll:head> ?y . "
			+ "?e2 <conll:deprel> \"DO\" . "
			+ "?p <conll:head> ?e2 . "
			//+ "?p <conll:deprel> \"MOD\" . "
			+ "?p <conll:form> ?prep . "
			+ "?p <conll:cpostag> \"a\" . "
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
			
	@Override
	public String getID() {
		return "SPARQLPattern_ES_7";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();

		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getAdjective(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(), this.getDebugger());
		
		
	}

}
