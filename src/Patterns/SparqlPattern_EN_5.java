package Patterns;

import com.hp.hpl.jena.rdf.model.Model;

import core.LexiconWithFeatures;

public class SparqlPattern_EN_5 implements SparqlPattern {

	/*
	 * 
	PropSubj:Joe Wright
	PropObj:Anoushka Shankar
	sentence:Joe Wright is married to sitarist Anoushka Shankar , daughter of Ravi Shankar and half-sister of Norah Jones . 
	1	Joe	_	NNP	NNP	_	2	nn
	2	Wright	_	NNP	NNP	_	4	nsubjpass
	3	is	_	VBZ	VBZ	_	4	auxpass
	4	married	_	VBN	VBN	_	0	null
	5	to	_	TO	TO	_	6	aux
	6	sitarist	_	VB	VB	_	4	xcomp
	7	Anoushka	_	NNP	NNP	_	8	nn
	8	Shankar	_	NNP	NNP	_	6	dobj
	9	,	_	,	,	_	8	punct
	10	daughter	_	NN	NN	_	8	appos
	11	of	_	IN	IN	_	10	prep
	12	Ravi	_	NNP	NNP	_	13	nn
	13	Shankar	_	NNP	NNP	_	11	pobj
	14	and	_	CC	CC	_	10	cc
	15	half-sister	_	NN	NN	_	10	conj
	16	of	_	IN	IN	_	10	prep
	17	Norah	_	NNP	NNP	_	18	nn
	18	Jones	_	NNP	NNP	_	16	pobj
	19	.	_	.	.	_	4	punct
	----------------------
	IGONORE sitarist: so x is married to y
	 */
			String query = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
					+ "{ "
					+ "?e1 <conll:form> ?e1_form . "
					+ "?e1 <conll:deprel> ?e1_grammar . "
					+ "FILTER regex(?e1_grammar, \"subj\") ."
					+ "?e1 <conll:cpostag> ?e1_pos . "
					//+ "FILTER regex(?e1_pos, \"NN\") ."
					+ "?e1 <conll:head> ?y . "
					+ "?y <conll:cpostag> ?lemma_pos . "
					+ "{?y <conll:cpostag> \"VBN\" . }"
					+ "UNION"
					+ "{?y <conll:cpostag> \"VBG\" . }"
					+ "?y <conll:deprel> ?lemma_grammar . "
					+ "?y <conll:form> ?lemma . "
					+"OPTIONAL{"
					+ "?lemma_nn <conll:head> ?y. "
					+ "?lemma_nn <conll:form> ?lemma_addition. "
					+ "?lemma_nn <conll:deprel> \"nn\"."
					+"} "
					+ "?verb <conll:head> ?y . "
					+ "?verb <conll:deprel> \"auxpass\" . "
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

		// generate an Adjective Frame
		
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
