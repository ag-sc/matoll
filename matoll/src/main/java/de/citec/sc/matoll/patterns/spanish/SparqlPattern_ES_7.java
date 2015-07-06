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

public class SparqlPattern_ES_7 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_7.class.getName());

// aus spouse	
	
//	ID:291
//	property subject: Joviano
//	property object: Charito
//	sentence:: 
//	1	Charito	charito	n	NP00000	_	3	SUBJ
//	2	se	se	p	P00CN000	_	3	DO
//	3	casó	casar	v	VMIS3S0	_	0	ROOT
//	4	con	con	s	SPS00	_	3	MOD
//	5	Joviano	joviano	n	NP00000	_	4	COMP
//	6	,	,	f	Fc	_	5	punct
//	7	un	uno	d	DI0MS0	_	8	SPEC
//	8	hijo	hijo	n	NCMS000	_	3	DO
//	9	de	de	s	SPS00	_	8	MOD
//	10	Varroniano	varroniano	n	NP00000	_	9	COMP
//	11	.	.	f	Fp	_	10	punct

 
    
   
	String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
			+ "?verb <conll:cpostag> ?verb_pos ."
			+ "?verb <conll:lemma> ?lemma ."
			+ "FILTER regex(?deprel, \"VMIS\") ."
			
			// can be also MPAS
			+ "?se <conll:lemma> \"se\" ."
			+ "?se <conll:deprel> \"DO\" ."
			+ "?se <conll:head> ?verb ."
			
			+ "?e1 <conll:head> ?verb."
			+ "?e1 <conll:deprel> \"SUBJ\" ."

			
			// can be OBLC instead of MOD
			+ "?p <conll:head> ?verb."
			+ "?p <conll:deprel> \"MOD\" ."
			+ "?p <conll:lemma> ?prep. "
		
			+ "?e2 <conll:head> ?p."
			+ "?e2 <conll:deprel> \"COMP \"."
			
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
		
		Templates.getAdjective(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
		
		
	}

}
