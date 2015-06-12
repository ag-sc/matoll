package de.citec.sc.matoll.patterns.spanish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_ES_1 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_1.class.getName());
	
	
	/*
1	Aníbal	aníbal	n	NP00000	_	2	SUBJ	_	_
2	cruza	cruzar	v	VMIP3S0	_	0	ROOT	_	_
3	los	el	d	DA0MP0	_	4	SPEC	_	_
4	Alpes	alpes	n	NP00000	_	2	DO	_	_
5	a	a	s	SPS00	_	2	OBLC	_	_
6	el	el	d	DA0MS0	_	7	SPEC	_	_
7	mando	mando	n	NCMS000	_	5	COMP	_	_
8	de	de	s	SPS00	_	7	MOD	_	_
9	el	el	d	DA0MS0	_	10	SPEC	_	_
10	ejército	ejército	n	NCMS000	_	8	COMP	_	_
11	cartaginés	cartaginés	a	AQ0MS0	_	10	MOD	_	_
12	.	.	f	Fp	_	11	punct	_	_

x verb y - ohne preposition
	 */
	String query = "SELECT ?lemma ?subj_arg ?obj_arg  WHERE {"
			+ "?verb <conll:postag> ?pos . "
			//POSTAG nach VM prüfen Verbos principales (Hauptverb)
			+ "FILTER regex(?pos, \"VMIP\") ."
			+ "?verb <conll:deprel> \"ROOT\" ."
			+ "?verb <conll:form> ?lemma . "
			+ "?subj <conll:head> ?verb . "
			+ "?subj <conll:deprel> \"SUBJ\". "
			+ "?dobj <conll:head> ?verb . "
			+ "?dobj <conll:deprel> \"DO\" . "
			+ "?subj <own:senseArg> ?subj_arg. "
			+ "?dobj <own:senseArg> ?obj_arg. "
			+ "}";
	
	@Override
	public String getID() {
		return "SPARQLPattern_ES_1";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();
		
		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getTransitiveVerb(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(),"es");
		
		
	}

}
