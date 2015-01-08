package patterns;

import com.hp.hpl.jena.rdf.model.Model;

import core.LexiconWithFeatures;

public class SparqlPattern_EN_4 implements SparqlPattern {

	/*################################
entity1_form:shriram
entity2_form:google
propSubject:ram shriram
propObject:google
--------------
entity1_grammar:nsubj
entity2_grammar:pobj
--------------
lemma:member
lemma_grammar:null
lemma_pos:nn
depending dobj:
depending advmod:
--------------
query_name:query5
intended_lexical_type:Noun
entry:RelationalNoun("board member",<http://dbpedia.org/ontology/board>, propSubj = PrepositionalObject("of"), propObj = CopulativeArg)
--------------
sentence:Kavitark Ram Shriram is a board member of Google and one of the first investors in Google . 
1	Kavitark	_	NNP	NNP	_	3	nn
2	Ram	_	NNP	NNP	_	3	nn
3	Shriram	_	NNP	NNP	_	7	nsubj
4	is	_	VBZ	VBZ	_	7	cop
5	a	_	DT	DT	_	7	det
6	board	_	NN	NN	_	7	nn
7	member	_	NN	NN	_	0	null
8	of	_	IN	IN	_	7	prep
9	Google	_	NNP	NNP	_	8	pobj
=>works only in fuzzy mode.

PropSubj:Oxford Brookes University
PropObj:Janet Beer
sentence:Professor Janet Beer is the Vice-Chancellor of Oxford Brookes University . 
1	Professor	_	NNP	NNP	_	3	nn
2	Janet	_	NNP	NNP	_	3	nn
3	Beer	_	NNP	NNP	_	6	nsubj
4	is	_	VBZ	VBZ	_	6	cop
5	the	_	DT	DT	_	6	det
6	Vice-Chancellor	_	NNP	NNP	_	0	null
7	of	_	IN	IN	_	6	prep
8	Oxford	_	NNP	NNP	_	10	nn
9	Brookes	_	NNP	NNP	_	10	nn
10	University	_	NNP	NNP	_	7	pobj
11	.	_	.	.	_	6	punct
----------------------


*/
		
		String query = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{?y <conll:cpostag> ?lemma_pos . "
				+"{?y <conll:cpostag> \"NN\" . }"
				+ "UNION"
				+"{?y <conll:cpostag> \"NNS\" . }"
				+ "?y <conll:deprel> ?lemma_grammar . "
				+ "?y <conll:form> ?lemma . "
				+"OPTIONAL{"
				+ "?lemma_nn <conll:head> ?y. "
				+ "?lemma_nn <conll:form> ?lemma_addition. "
				+ "?lemma_nn <conll:deprel> \"nn\"."
				+"} "
				+ "?verb <conll:head> ?y . "
				+ "?verb <conll:deprel> \"cop\" ."
				+ "?e1 <conll:head> ?y . "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "FILTER regex(?e1_grammar, \"subj\") ."
				+ "?p <conll:head> ?y . "
				+ "?p <conll:deprel> \"prep\" . "
				+ "?p <conll:form> ?prep . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER regex(?e2_grammar, \"obj\") ."
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		

	public void extractLexicalEntries(Model model, String reference,  LexiconWithFeatures lexicon) {
		// TODO Auto-generated method stub

		// Generate a NounPPFrame
		
	}



	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
