package de.citec.sc.matoll.patterns.german;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;

public class SparqlPattern_DE_2 extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_2.class.getName());
	
	/*
	 * PropSubj:Islamisch Republikanische Partei
PropObj:1979 Februar 
sentence:Am 19. Februar 1979 gründeten die Gefolgsleute Chomeinis die Islamisch Republikanische Partei . 
1	Am	am	PREP	APPRART	Dat	5	pp	_	_ 
2	19.	19.	ADJA	ADJA	_|Masc|Dat|Sg|_|	3	attr	_	_ 
3	Februar	Februar	N	NN	Masc|Dat|Sg	1	pn	_	_ 
4	1979	1979	CARD	CARD	_	3	app	_	_ 
5	gründeten	gründen	V	VVFIN	3|Pl|Past|_	0	root	_	_ 
6	die	die	ART	ART	Def|Masc|Nom|Pl	7	det	_	_ 
7	Gefolgsleute	Gefolgsmann	N	NN	Masc|Nom|Pl	5	subj	_	_ 
8	Chomeinis	Chomeinis	N	NN	Masc|Nom|Pl	7	app	_	_ 
9	die	die	ART	ART	Def|_|_|_	10	det	_	_ 
10	Islamisch	islamisch	N	NN	Fem|Akk|Sg	5	obja	_	_ 
11	Republikanische	republikanisch	ADJA	ADJA	Pos|Fem|Akk|Sg|_|	12	attr	_	_ 
12	Partei	Partei	N	NN	Fem|Akk|Sg	10	app	_
	 */
	
	
	String query = "SELECT ?class ?lemma ?dobj_lemma ?advmod_lemma ?lemma_pos ?dobj_lemma ?lemma_grammar ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
			+ "{?y <conll:cpostag> ?lemma_pos . "
			+ "?y <conll:cpostag> \"V\" ."
			//VVFIN
			+ "?y <conll:lemma> ?lemma . "
			+ "?y <conll:deprel> ?lemma_grammar. "
			+ "?e1 <conll:head> ?p . "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			+ "?e1 <conll:deprel> ?deprel. "
			+ "FILTER regex(?deprel, \"pn\") ."
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"pp\" . "
			+" ?p <conll:postag> \"APPRART\". "
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?y . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			+ "?e2 <conll:form> ?e2_form . "
			+ "?e2 <conll:deprel> \"obja\" . "
			//+"?e2 <conll:deprel>  \"pn\" ."
			//+ "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"subj\") || regex(?e2_grammar, \"pn\"))"
			//+ "{?e2 <conll:deprel>  \"pn\" .}"
			//+ "UNION"
			//+ "{FILTER regex(?e2_grammar, \"obj\") .}"
			+ "?y <own:partOf> ?class. "
			+ "?class <own:subj> ?propSubj. "
			+ "?class <own:obj> ?propObj. "
			+ "}";
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_2";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		// TODO Auto-generated method stub
		
	}

}
