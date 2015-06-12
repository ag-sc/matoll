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

public class SparqlPattern_ES_9 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_9.class.getName());
	
	
	/*
	 * sentence:Actualmente Glebova vive en Tailandia y está casada con Paradorn Srichaphan . 
1	Actualmente	_	r	RG	_	3	MOD
2	Glebova	_	n	NP00000	_	3	SUBJ
3	vive	_	v	VMIP3S0	_	0	ROOT
4	en	_	s	SPS00	_	3	MOD
5	Tailandia	_	n	NP00000	_	4	COMP
Verb+prep

Neuer parse:
1	Actualmente	actualmente	r	RG	_	3	MOD	_	_
2	Glebova	glebova	n	NP00000	_	3	SUBJ	_	_
3	vive	vivir	v	VMIP3S0	_	0	ROOT	_	_
4	en	en	s	SPS00	_	3	MOD	_	_
5	Tailandia	tailandia	n	NP00000	_	4	COMP	_	_
6	y	y	c	CC	_	3	COORD	_	_
7	está	estar	v	VAIP3S0	_	6	CONJ	_	_
8	casada	casar	v	VMP00SF	_	7	ATR	_	_
9	con	con	s	SPS00	_	8	OBLC	_	_
10	Paradorn_Srichaphan	paradorn_srichaphan	n	NP00000	_	9	COMP	_	_
11	.	.	f	Fp	_	10	punct	_	_


	 */
	
	// eigentlich estar casado con....
	
	String query = "SELECT ?verb_lemma ?e1_arg ?e2_arg ?prep:form  WHERE {"
			+ "?verb <conll:postag> ?verb_pos . "
			+ "FILTER regex(?lemma_pos, \"VMIP\") ."
			+ "?verb <conll:form> ?verb_lemma . "
			
			+ "?e1 <conll:head> ?verb . "
			+ "?e1 <conll:deprel> ?e1_deprel. "
			+ "FILTER regex(?e1_deprel, \"SUBJ\") ."
			
			+ "?prep <conll:head> ?verb . "
			+ "?prep <conll:postag> ?prep_pos . "
			+ "FILTER regex(?prep_pos, \"SPS\") ."
			+ "?prep <conll:form> ?prep_form . "
			+ "?prep <conll:deprel> \"OBLC\" . "
			
			+ "?e2 <conll:head> ?prep . "
			+ "?e2 <conll:deprel> \"COMP\" . "
			
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
			
	@Override
	public String getID() {
		return "SPARQLPattern_ES_9";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();

		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getIntransitiveVerb(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(),Language.ES);
		
		
	}

}
