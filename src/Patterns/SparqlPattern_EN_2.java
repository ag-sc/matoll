package Patterns;

import com.hp.hpl.jena.rdf.model.Model;

import core.LexiconWithFeatures;

public class SparqlPattern_EN_2 implements SparqlPattern {

	String query = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
			+ "{?y <conll:cpostag> ?lemma_pos . "
			+ "{?y <conll:deprel> \"appos\".} UNION {?y <conll:deprel> \"dep\".}"
			+ "?y <conll:form> ?lemma . "
			+ "?y <conll:deprel> ?lemma_grammar. "
			+ "{?y <conll:cpostag> \"NN\" . }"
			+ "UNION"
			+ "{?y <conll:cpostag> \"NNS\" . }"
			+"OPTIONAL{"
			+ "?lemma_nn <conll:head> ?y. "
			+ "?lemma_nn <conll:form> ?lemma_addition. "
			+ "?lemma_nn <conll:deprel> \"nn\"."
			+"} "
			+ "?y <conll:head> ?e1 . "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"prep\" . "
			// pci: Should ?prep not always be "of" ?
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			+ "?e2 <conll:form> ?e2_form . "
			+ "?e2 <conll:deprel>  \"pobj\". "
			+ "?y <own:partOf> ?class. "
			+ "?class <own:subj> ?propSubj. "
			+ "?class <own:obj> ?propObj. "
			+ "}";
	
	/*
	 * 
entity1_form:murdoch
entity2_form:company
propSubject:rupert murdoch
propObject:fox broadcasting company
--------------
entity1_grammar:nsubj
entity2_grammar:pobj
--------------
lemma:creator
lemma_grammar:appos
lemma_pos:nn
depending dobj:
depending advmod:
--------------
query_name:query2
intended_lexical_type:Noun
entry:RelationalNoun("creator",<http://dbpedia.org/ontology/board>, propSubj = PrepositionalObject("of"), propObj = CopulativeArg)
--------------
sentence:Also featured in the episode is Rupert Murdoch , creator of the Fox Broadcasting Company . 
1       Also    _       RB      RB      _       2       advmod
2       featured        _       VBN     VBN     _       0       null
3       in      _       IN      IN      _       2       prep
4       the     _       DT      DT      _       5       det
5       episode _       NN      NN      _       3       pobj
6       is      _       VBZ     VBZ     _       2       dep
7       Rupert  _       NNP     NNP     _       8       nn
8       Murdoch _       NNP     NNP     _       6       nsubj
9       ,       _       ,       ,       _       8       punct
10      creator _       NN      NN      _       8       appos
11      of      _       IN      IN      _       10      prep
12      the     _       DT      DT      _       15      det
13      Fox     _       NNP     NNP     _       15      nn
14      Broadcasting    _       NNP     NNP     _       15      nn
15      Company _       NNP     NNP     _       11      pobj
16      .       _       .       .       _       2       punct
PropSubj:Anne Hyde
PropObj:James II of England
sentence:Ann was later named after Lady Anne Hyde the first wife of King James II of England . 
1	Ann	_	NNP	NNP	_	4	nsubjpass
2	was	_	VBD	VBD	_	4	auxpass
3	later	_	RB	RB	_	4	advmod
4	named	_	VBN	VBN	_	0	null
5	after	_	IN	IN	_	4	prep
6	Lady	_	NNP	NNP	_	8	nn
7	Anne	_	NNP	NNP	_	8	nn
8	Hyde	_	NNP	NNP	_	5	pobj
9	the	_	DT	DT	_	11	det
10	first	_	JJ	JJ	_	11	amod
11	wife	_	NN	NN	_	8	dep
12	of	_	IN	IN	_	11	prep
13	King	_	NNP	NNP	_	15	nn
14	James	_	NNP	NNP	_	15	nn
15	II	_	NNP	NNP	_	12	pobj
16	of	_	IN	IN	_	15	prep
17	England	_	NNP	NNP	_	16	pobj
18	.	_	.	.	_	4	punct
	 */
	
	@Override
	public void extractLexicalEntries(Model model, String reference, LexiconWithFeatures lexicon) {
		
		// match query to model
		
		// create PossNounFrame

	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
