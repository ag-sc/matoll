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

public class SparqlPattern_ES_6 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_6.class.getName());
	

	/*Neuer Parse:
	 * 1	Su	su	d	DP3CS0	_	2	SPEC	_	_
2	primo	primo	n	NCMS000	_	4	SUBJ	_	_
3	Felipe_de_Suabia	felipe_de_suabia	n	NP00000	_	2	MOD	_	_
4	estaba	estar	v	VAII1S0	_	0	ROOT	_	_
5	casado	casar	v	VMP00SM	_	4	ATR	_	_
6	con	con	s	SPS00	_	5	OBLC	_	_
7	Irene_Angelina	irene_angelina	n	NP00000	_	6	COMP	_	_
	 * 
	 */
	String query= "SELECT ?participle_lemma ?e1_arg ?e2_arg ?prep_form WHERE {"

			+ "?e1 <conll:head> ?estar ."
			+ "?e1 <conll:deprel> \"SUBJ\"."
			
			+ "?estar <conll:lemma> \"estar\" ."
			
			+ "?partciciple <conll:form> ?participle_lemma"
			
			+ "?prep <conll:form> ?prep_form ."
			+ "?prep <conll:head> ?participle ."
			+ "?prep <conll:deprel> \"OBLC\" ."
			
			
			+ "?e2 <conll:head> ?prep ."
			+ "?e2 <conll:deprel> \"COMP\"."
			
			
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
			
	@Override
	public String getID() {
		return "SPARQLPattern_ES_6";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();

		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getAdjective(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(),Language.ES);
		
	}

}
