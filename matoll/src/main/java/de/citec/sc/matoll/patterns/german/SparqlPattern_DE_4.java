package de.citec.sc.matoll.patterns.german;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;

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
	String query = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
			+ "{"
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			+ "?e1 <conll:cpostag> \"N\" . "
			+ "?y <conll:cpostag> ?lemma_pos . "
			+ "?y <conll:deprel> ?lemma_grammar . "
			+ "?y <conll:deprel> \"app\" . "
			+ "?y <conll:cpostag> \"N\" . "
			+ "?y <conll:lemma> ?lemma . "
			+ "?y <conll:head> ?e1 . "
			+ "?e2 <conll:head> ?y . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			+ "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
			+ "?e2 <conll:cpostag> \"N\". "
			+ "?e2 <conll:form> ?e2_form . "
			+ "?y <own:partOf> ?class. "
			+ "?class <own:subj> ?propSubj. "
			+ "?class <own:obj> ?propObj. "
			+ "}";
	
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_4";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		// TODO Auto-generated method stub
		
	}

}
