package patterns;

import com.hp.hpl.jena.rdf.model.Model;

import core.LexiconWithFeatures;

public class SparqlPattern_EN_1 implements SparqlPattern {

	String query = "SELECT ?class ?lemma ?dobj_lemma ?advmod_lemma ?lemma_pos ?dobj_lemma ?lemma_grammar ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
			+ "{?y <conll:cpostag> ?lemma_pos . "
			+ "{?y <conll:cpostag> \"VB\" .}"
			+ "UNION"
			+ "{?y <conll:cpostag> \"VBD\" .}"
			+ "UNION"
			+ "{?y <conll:cpostag> \"VBP\" .}"
			+ "UNION"
			+ "{?y <conll:cpostag> \"VBZ\" .}"
			+ "?y <conll:form> ?lemma . "
			+ "?y <conll:deprel> ?lemma_grammar. "
			+"OPTIONAL{"
			+ "?lemma_nn <conll:head> ?y. "
			+ "?lemma_nn <conll:form> ?lemma_addition. "
			+ "?lemma_nn <conll:deprel> \"nn\"."
			+"} "
			+ "?e1 <conll:head> ?y . "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			+ "?e1 <conll:deprel> ?deprel. "
			+ "FILTER regex(?deprel, \"subj\") ."
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"prep\" . "
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			+ "?e2 <conll:form> ?e2_form . "
			+ "?e2 <conll:deprel> ?deprel2. "
			+ "FILTER regex(?deprel2, \"obj\") ."
			+ "?y <own:partOf> ?class. "
			+ "?class <own:subj> ?propSubj. "
			+ "?class <own:obj> ?propObj. "
			+ "}";
	
	/*
entity1_form:jobs
entity2_form:inc.
propSubject:steve jobs
propObject:apple inc
--------------
entity1_grammar:nsubj
entity2_grammar:pobj
--------------
lemma:attempted
lemma_grammar:null
lemma_pos:vbd
depending dobj (if exists):coups
--------------
query_name:query1
intended_lexical_type:StateVerb
entry:StateVerb("attempt",<http://dbpedia.org/ontology/board>, propSubj = PrepositionalObject("at"), propObj = Subject)
--------------
sentence:Steve Jobs attempted management coups twice at Apple Inc. ; first in 1985 when he unsuccessfully tried to oust John Sculley and then again in 1997 which successfully forced Gil Amelio to resign . 
1       Steve   _       NNP     NNP     _       2       nn
2       Jobs    _       NNP     NNP     _       3       nsubj
3       attempted       _       VBD     VBD     _       0       null
4       management      _       NN      NN      _       5       nn
5       coups   _       NNS     NNS     _       3       dobj
6       twice   _       RB      RB      _       3       advmod
7       at      _       IN      IN      _       3       prep
8       Apple   _       NNP     NNP     _       9       nn
9       Inc.    _       NNP     NNP     _       7       pobj
10      ;       _       :       :       _       3       punct
11      first   _       RB      RB      _       3       conj
12      in      _       IN      IN      _       11      dep
13      1985    _       CD      CD      _       12      pobj
14      when    _       WRB     WRB     _       17      advmod
15      he      _       PRP     PRP     _       17      nsubj
16      unsuccessfully  _       RB      RB      _       17      advmod
17      tried   _       VBD     VBD     _       11      dep
18      to      _       TO      TO      _       19      aux
19      oust    _       VB      VB      _       17      xcomp
20      John    _       NNP     NNP     _       21      nn
21      Sculley _       NNP     NNP     _       19      dobj
22      and     _       CC      CC      _       17      cc
23      then    _       RB      RB      _       24      advmod
24      again   _       RB      RB      _       17      conj
25      in      _       IN      IN      _       24      dep
26      1997    _       CD      CD      _       25      pobj
27      which   _       WDT     WDT     _       29      nsubj
28      successfully    _       RB      RB      _       29      advmod
29      forced  _       VBD     VBD     _       26      rcmod
30      Gil     _       NNP     NNP     _       31      nn
31      Amelio  _       NNP     NNP     _       33      nsubj
32      to      _       TO      TO      _       33      aux
33      resign  _       VB      VB      _       29      xcomp
34      .       _       .       .       _       3       punct

*/
	
	public void extractLexicalEntries(Model model, String reference, LexiconWithFeatures lexicon) {
		
		// match the query against the model and for each match create a lexical entry and feature Vector

		// generate an intransitive with prepositional object
		
	}

	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
