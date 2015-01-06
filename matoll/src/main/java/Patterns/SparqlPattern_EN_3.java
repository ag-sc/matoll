package patterns;

import com.hp.hpl.jena.rdf.model.Model;

import core.LexiconWithFeatures;

public class SparqlPattern_EN_3 implements SparqlPattern {


	/*
	 * ################################
entity1_form:akerson
entity2_form:motors
propSubject:daniel akerson
propObject:general motors
--------------
entity1_grammar:appos
entity2_grammar:pobj
--------------
lemma:chairman
lemma_grammar:nsubj
lemma_pos:nn
depending dobj:
depending advmod:
--------------
query_name:query3
intended_lexical_type:Noun
entry:RelationalNoun("chairman",<http://dbpedia.org/ontology/board>, propSubj = PrepositionalObject("of"), propObj = CopulativeArg)
--------------
sentence:In July 2011 , the chairman and CEO of General Motors , Daniel Akerson , stated that while the cost of hydrogen fuel cell cars is decreasing : `` The car is still too expensive and probably wo n't be practical until the 2020-plus period , I do n't know . '' 
1	In	_	IN	IN	_	16	prep
2	July	_	NNP	NNP	_	1	pobj
3	2011	_	CD	CD	_	2	num
4	,	_	,	,	_	16	punct
5	the	_	DT	DT	_	6	det
6	chairman	_	NN	NN	_	16	nsubj
7	and	_	CC	CC	_	6	cc
8	CEO	_	NN	NN	_	6	conj
9	of	_	IN	IN	_	6	prep
10	General	_	NNP	NNP	_	11	nn
11	Motors	_	NNPS	NNPS	_	9	pobj
12	,	_	,	,	_	6	punct
13	Daniel	_	NNP	NNP	_	14	nn
14	Akerson	_	NNP	NNP	_	6	appos
15	,	_	,	,	_	6	punct
16	stated	_	VBD	VBD	_	0	null

	 */
	
	String query = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
			+ "{?y <conll:cpostag> ?lemma_pos . "
			+ "?y <conll:deprel> ?lemma_grammar . "
			+ "{?y <conll:cpostag> \"NN\" . }"
			+ "UNION"
			+ "{?y <conll:cpostag> \"NNS\" . }"
			+ "?y <conll:form> ?lemma . "
			+"OPTIONAL{"
			+ "?lemma_nn <conll:head> ?y. "
			+ "?lemma_nn <conll:form> ?lemma_addition. "
			+ "?lemma_nn <conll:deprel> \"nn\"."
			+"} "
			+ "?e1 <conll:head> ?y . "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			+ "FILTER regex(?e1_grammar, \"appos\") ."
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"prep\" . "
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			// pci: Why not only pobj?
			+ "FILTER regex(?e2_grammar, \"obj\") ."
			+ "?e2 <conll:form> ?e2_form . "
			+ "?y <own:partOf> ?class. "
			+ "?class <own:subj> ?propSubj. "
			+ "?class <own:obj> ?propObj. "
			+ "}";
	
	
	@Override
	public void extractLexicalEntries(Model model, String reference, LexiconWithFeatures lexicon) {
		// TODO Auto-generated method stub
		
		// generate a NounPPFrame

	}


	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
