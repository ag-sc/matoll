package de.citec.sc.matoll.patterns.german;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_DE_4 extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_4.class.getName());
	
	/*
	 * 24	Big	Big	ADV	ADJD	_	25	adv	_	_ 
25	Stone	Stone	ADJA	ADJA	_|_|Gen|Sg|_	26 attr
26	Lake	Lake	N	NN	_|Gen|Sg	22	gmod	_	_ 
27	,	,	$,	$,	_	0	root	_	_ 
28	der	der	ART	ART	Def|_|Gen|_	29	det	_	_ 
29	Quelle	Quell	N	NN	_|Gen|_	26	app	_	_ 
30	des	das	ART	ART	Def|_|Gen|Sg	31	det	_	_ 
31	Minnesota	Minnesota	N	NE	_|Gen|Sg	29	gmod	_	_ 
32	River	River	N	NE	_|Gen|Sg	31	app	_	_ 
	 */
	//ohnePrep
	String query = "SELECT ?lemma ?e1_arg ?e2_arg WHERE{"
			+ "?e1 <conll:cpostag> \"N\" . "
			+ "?y <conll:deprel> \"app\" . "
			+ "?y <conll:cpostag> \"N\" . "
			+ "?y <conll:lemma> ?lemma . "
			+ "?y <conll:head> ?e1 . "
			+ "?e2 <conll:head> ?y . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			+ "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
			+ "?e2 <conll:cpostag> \"N\". "
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
	
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_4";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();
		
		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getNoun(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(), this.getDebugger());
		
		
	}

}
