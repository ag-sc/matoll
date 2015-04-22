package de.citec.sc.matoll.patterns.spanish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

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
	String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
			+ "?y <conll:form> ?lemma . "
			+ "?y <conll:head> ?blank . "
			+ "?y <conll:deprel> \"MOD\" . "
			+ "?y <conll:postag> \"AQ0FS0\". "
			+ "?blank <conll:head> ?verb. "
			+ "?blank <conll:deprel> \"ATR\". "
			+ "?verb <conll:postag> ?verb_pos . "
			+ "FILTER regex(?verb_pos, \"VS\") ."
			+ "?e1 <conll:head> ?verb . "
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
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
	
	@Override
	public String getID() {
		return "SPARQLPattern_ES_3";
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
