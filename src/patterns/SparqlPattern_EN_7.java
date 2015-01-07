package patterns;

import com.hp.hpl.jena.rdf.model.Model;

import core.LexiconWithFeatures;

public class SparqlPattern_EN_7 implements SparqlPattern {

	/*
	 * PropSubj:University of Colombo
PropObj:Professor
sentence:Deshabandu Professor Nandadasa Kodagoda MRCP , MD was the former Vice Chancellor of the University of Colombo . 
1	Deshabandu	_	NNP	NNP	_	5	nn
2	Professor	_	NNP	NNP	_	5	nn
3	Nandadasa	_	NNP	NNP	_	5	nn
4	Kodagoda	_	NNP	NNP	_	5	nn
5	MRCP	_	NNP	NNP	_	12	nsubj
6	,	_	,	,	_	5	punct
7	MD	_	NN	NN	_	5	appos
8	was	_	VBD	VBD	_	12	cop
9	the	_	DT	DT	_	12	det
10	former	_	JJ	JJ	_	12	dep
11	Vice	_	NNP	NNP	_	12	dep
12	Chancellor	_	NNP	NNP	_	0	null
13	of	_	IN	IN	_	12	prep
14	the	_	DT	DT	_	15	det
15	University	_	NNP	NNP	_	13	pobj
16	of	_	IN	IN	_	15	prep
17	Colombo	_	NNP	NNP	_	16	pobj
18	.	_	.	.	_	12	punct
----------------------
	 */
	
	
// pci: Why is this not the same as Pattern 4 ???	
	
	String query = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
			+ "{ "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			+ "FILTER regex(?e1_grammar, \"subj\") ."
			+ "?e1 <conll:cpostag> ?e1_pos . "
			//+ "FILTER regex(?e1_pos, \"NN\") ."
			+ "?e1 <conll:head> ?y . "
			+ "?y <conll:cpostag> ?lemma_pos . "
			+"{?y <conll:cpostag> \"NN\" . }"
			+ "UNION"
			+"{?y <conll:cpostag> \"NNS\" . }"
			+ "?y <conll:deprel> ?lemma_grammar . "
			+ "?y <conll:form> ?lemma . "
			+"OPTIONAL{"
			+ "?lemma_nn <conll:head> ?y. "
			+ "?lemma_nn <conll:form> ?lemma_addition. "
			+ "?lemma_nn <conll:cpostag> ?lemma_postag."
			+ "FILTER regex(?lemma_postag, \"NNP\") ."
			+ "?lemma_nn <conll:deprel> \"dep\" ."
			+"} "
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"prep\" . "
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			+ "FILTER regex(?e2_grammar, \"obj\") ."
			//+ "?o <is> ?e2 ."
			+ "?e2 <conll:form> ?e2_form . "
			+ "?y <own:partOf> ?class. "
			+ "?class <own:subj> ?propSubj. "
			+ "?class <own:obj> ?propObj. "
			+ "}";
	
	@Override
	public void extractLexicalEntries(Model model, String reference, LexiconWithFeatures lexicon) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
