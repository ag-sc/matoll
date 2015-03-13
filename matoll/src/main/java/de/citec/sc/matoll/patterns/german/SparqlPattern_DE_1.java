package de.citec.sc.matoll.patterns.german;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;

public class SparqlPattern_DE_1 extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_1.class.getName());
	
	/*
PropSubj:Lakshmi Mittal
PropObj:Goldman Sachs
sentence:Lakshmi Niwas Mittal sitzt im Board of Directors von Goldman Sachs . 
1	Lakshmi	Lakshmi	N	NE	Neut|Nom|Sg	4	subj	_	_ 
2	Niwas	Niwas	N	NE	Neut|Nom|Sg	1	app	_	_ 
3	Mittal	Mittal	N	NN	Neut|Nom|Sg	2	app	_	_ 
4	sitzt	sitzen	V	VVFIN	3|Sg|Pres|Ind	0	root	_	_ 
5	im	im	PREP	APPRART	Dat	4	pp	_	_ 
6	Board	Board	N	NN	Neut|Dat|Sg	5	pn	_	_ 
Assuming Board is obj
*/
	//Verb + prep
	String query = "SELECT ?class ?lemma ?dobj_lemma ?advmod_lemma ?lemma_pos ?dobj_lemma ?lemma_grammar ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
			+ "{?y <conll:cpostag> ?lemma_pos . "
			+ "?y <conll:cpostag> \"V\" ."
			//Filter auf nicht VA
			+ "?y <conll:lemma> ?lemma . "
			+ "?y <conll:deprel> ?lemma_grammar. "
			+ "?e1 <conll:head> ?y . "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			+ "?e1 <conll:deprel> ?deprel. "
			+ "FILTER regex(?deprel, \"subj\") ."
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"pp\" . "
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			+ "?e2 <conll:form> ?e2_form . "
			//+"?e2 <conll:deprel>  \"pn\" ."
			+ "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
			//+ "{?e2 <conll:deprel>  \"pn\" .}"
			//+ "UNION"
			//+ "{FILTER regex(?e2_grammar, \"obj\") .}"
			+ "?y <own:partOf> ?class. "
			+ "?class <own:subj> ?propSubj. "
			+ "?class <own:obj> ?propObj. "
			+ "}";
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_1";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		// TODO Auto-generated method stub
		
	}

}
