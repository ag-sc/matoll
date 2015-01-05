package query;
import java.util.ArrayList;
import java.util.List;


public class EnglishQuery {
	
	
	
public static List<List<String>> getQueries() {
		
		//Map<String, String> queries = new HashMap<String, String>();
		List<List<String>> queries = new ArrayList<List<String>>();

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
		//Verb + prep
		String query1 = "SELECT ?class ?lemma ?dobj_lemma ?advmod_lemma ?lemma_pos ?dobj_lemma ?lemma_grammar ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
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
		// query2 ok
		
		String query2= "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
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
		 * PropSubj:Trip Hawkins
PropObj:Electronic Arts
sentence:In October 2012 , founder of Electronic Arts , Trip Hawkins , joined Extreme Reality 's board of directors . 
1       In      _       IN      IN      _       13      prep
2       October _       NNP     NNP     _       1       pobj
3       2012    _       CD      CD      _       2       num
4       ,       _       ,       ,       _       13      punct
5       founder _       NN      NN      _       13      nsubj
6       of      _       IN      IN      _       5       prep
7       Electronic      _       JJ      JJ      _       8       amod
8       Arts    _       NNS     NNS     _       6       pobj
9       ,       _       ,       ,       _       5       punct
10      Trip    _       NNP     NNP     _       11      nn
11      Hawkins _       NNP     NNP     _       5       appos
12      ,       _       ,       ,       _       5       punct
13      joined  _       VBD     VBD     _       0       null
14      Extreme _       NNP     NNP     _       15      nn
15      Reality _       NN      NN      _       17      poss
16      's      _       POS     POS     _       15      possessive
17      board   _       NN      NN      _       13      dobj
18      of      _       IN      IN      _       17      prep
19      directors       _       NNS     NNS     _       18      pobj
20      .       _       .       .       _       13      punct
----------------------

		 */
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
		
		// query 3 ok
		
		String query3 = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
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
				+ "FILTER regex(?e2_grammar, \"obj\") ."
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		

		
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


todo: is einbauen x is bla of y 
*/
		
		String query4 = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
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
		String query5 = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
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
		
		
	/*	PropSubj:Juana EnrÃ­quez
		PropObj:John II of Aragon
		sentence:Juana EnrÃ­quez , great-granddaughter of the founder of the lineage , married John II of Aragon and was the mother of Ferdinand II . 
		1	Juana	_	NNP	NNP	_	2	nn
		2	EnrÃ­quez	_	NNP	NNP	_	12	nsubj
		3	,	_	,	,	_	2	punct
		4	great-granddaughter	_	NN	NN	_	2	appos
		5	of	_	IN	IN	_	4	prep
		6	the	_	DT	DT	_	7	det
		7	founder	_	NN	NN	_	5	pobj
		8	of	_	IN	IN	_	7	prep
		9	the	_	DT	DT	_	10	det
		10	lineage	_	NN	NN	_	8	pobj
		11	,	_	,	,	_	2	punct
		//married is falsch getagged, müsste VBD seind
		12	married	_	VBN	VBN	_	0	null
		13	John	_	NNP	NNP	_	14	nn
		14	II	_	NNP	NNP	_	12	dobj
		15	of	_	IN	IN	_	14	prep
		16	Aragon	_	NNP	NNP	_	15	pobj
		17	and	_	CC	CC	_	12	cc
		18	was	_	VBD	VBD	_	20	cop
		19	the	_	DT	DT	_	20	det
		20	mother	_	NN	NN	_	12	conj
		21	of	_	IN	IN	_	20	prep
		22	Ferdinand	_	NNP	NNP	_	23	nn
		23	II	_	NNP	NNP	_	21	pobj
		24	.	_	.	.	_	12	punct
		----------------------*/
		String query6 = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{ "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "FILTER regex(?e1_grammar, \"subj\") ."
				+ "?e1 <conll:cpostag> ?e1_pos . "
				//+ "FILTER regex(?e1_pos, \"NN\") ."
				+ "?e1 <conll:head> ?y . "
				+ "?y <conll:cpostag> ?lemma_pos . "
				+ "FILTER regex(?lemma_pos, \"VB\") ."
				+ "?y <conll:deprel> ?lemma_grammar . "
				+ "?y <conll:form> ?lemma . "
				+ "?e2 <conll:head> ?y . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER regex(?e2_grammar, \"obj\") ."
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		
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
		
		
		String query7 = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
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
		
		
	
		// für Adjective mit JJ, drunter hängende Verb erst einmal ignorieren
		String query8 = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{ "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "FILTER regex(?e1_grammar, \"subj\") ."
				+ "?e1 <conll:cpostag> ?e1_pos . "
				//+ "FILTER regex(?e1_pos, \"NN\") ."
				+ "?e1 <conll:head> ?y . "
				+ "?y <conll:cpostag> ?lemma_pos . "
				+ "FILTER regex(?lemma_pos,\"JJ\") . "
				+ "?y <conll:deprel> ?lemma_grammar . "
				+ "?y <conll:form> ?lemma . "
				+"OPTIONAL{"
				+ "?lemma_nn <conll:head> ?y. "
				+ "?lemma_nn <conll:form> ?lemma_addition. "
				+ "?lemma_nn <conll:deprel> \"nn\"."
				+"} "
				//+ "?verb <conll:head> ?y . "
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
		
		
		
		List<String> tmp = new ArrayList<String>();
		tmp.add(query1);
		tmp.add("StateVerb");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query2);
		tmp.add("Noun");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query3);
		tmp.add("Noun");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query4);
		tmp.add("Noun");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query5);
		tmp.add("Adjective");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query6);
		tmp.add("StateVerb");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query7);
		tmp.add("Noun");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query8);
		tmp.add("Adjective");
		queries.add(tmp);


		
		
		
		return queries;
	}








}
