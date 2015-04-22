package de.citec.sc.matoll.patterns.spanish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
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
	String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
			+ "?y <conll:postag> ?lemma_pos . "
			//POSTAG nach VM prüfen Verbos principales (Hauptverb)
			+ "FILTER regex(?lemma_pos, \"VMIP\") ."
			+ "?y <conll:deprel> \"ROOT\" ."
			+ "?y <conll:form> ?lemma . "
			+ "?e1 <conll:head> ?y . "
			+ "?e1 <conll:postag> \"NP00000\". "
			+ "FILTER regex(?deprel, \"SUBJ\") ."
			+ "?p <conll:head> ?y . "
			+ "?p <conll:postag> \"SPS00\" . "
			+ "?p <conll:form> ?prep . "
			+ "?p <conll:deprel> \"MOD\" . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel> \"COMP\" . "
			+ "?e2 <conll:postag> \"NP00000\". "
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
		
		Templates.getIntransitiveVerb(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(), this.getDebugger());
		
		
	}

}
