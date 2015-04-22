package de.citec.sc.matoll.patterns.german;
import java.util.ArrayList;
import java.util.List;


public class GermanQuery {
	
	
	
public static List<List<String>> getQueries() {
		

	
		//Map<String, String> queries = new HashMap<String, String>();
		List<List<String>> queries = new ArrayList<List<String>>();

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
		String query1 = "SELECT ?class ?lemma ?dobj_lemma ?advmod_lemma ?lemma_pos ?dobj_lemma ?lemma_grammar ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
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
		
		
		String x_gründen_am_x = "SELECT ?class ?lemma ?dobj_lemma ?advmod_lemma ?lemma_pos ?dobj_lemma ?lemma_grammar ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
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
		
		
		
		
	/*
PropSubj:Iman Abdulmajid
PropObj:David Bowie
sentence:Haywood war zudem mit dem Model Iman Abdulmajid , der heutigen Ehefrau von Popstar David Bowie , verheiratet . 
1	Haywood	Haywood	N	NE	_|Nom|Sg	2	subj	_	_ 
2	war	sein	V	VAFIN	3|Sg|Past|Ind	0	root	_	_ 
3	zudem	zudem	ADV	ADV	_	18	adv	_	_ 
4	mit	mit	PREP	APPR	Dat	18	pp	_	_ 
5	dem	das	ART	ART	Def|Neut|Dat|Sg	6	det	_	_ 
6	Model	Model	N	NN	Neut|Dat|Sg	4	pn	_	_ 
7	Iman	Iman	N	NN	Neut|Dat|Sg	6	app	_	_ 
8	Abdulmajid	Abdulmajid	N	NE	Neut|Dat|Sg	7	app	_	_ 
9	,	,	$,	$,	_	0	root	_	_ 
10	der	der	ART	ART	Def|Fem|Dat|Sg	12	det	_	_ 
11	heutigen	heutig	ADJA	ADJA	Pos|Fem|Dat|Sg|_|	12	attr	_	_ 
12	Ehefrau	Ehefrau	N	NN	Fem|Dat|Sg	6	app	_	_ 
13	von	von	PREP	APPR	Dat	12	pp	_	_ 
14	Popstar	Popstar	N	NN	Masc|Dat|Sg	13	pn	_	_ 
15	David	David	N	NE	Masc|Dat|Sg	14	app	_	_ 
16	Bowie	Bowie	N	NE	Masc|Dat|Sg	15	app	_	_ 
17	,	,	$,	$,	_	0	root	_	_ 
18	verheiratet	verheiraten	V	VVPP	_	2	aux	_	_ 
19	.	.	$.	$.	_	0	root	_	_ 	
----------------------
	*/
		
		String query2 = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
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
				+ "?p <conll:form> ?prep ."
				+ "?p <conll:deprel> \"pp\". "
				+ "?p <conll:head> ?y ."
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
				+ "?e2 <conll:cpostag> \"N\". "
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
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
		String query2a = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
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
		
		
		/*
PropSubj:Oswald Mosley
PropObj:Diana Mitford
sentence:Nach ihrem frühen Tod heiratete Oswald Mosley seine damalige Geliebte Diana Mitford , Ehefrau des Brauereierben Bryan Walter Guinness , am 6. Oktober 1936 in Berlin . 
1	Nach	nach	PREP	APPR	Dat	5	pp	_	_ 
2	ihrem	ihr	ART	PPOSAT	Masc|Dat|Sg	4	det	_	_ 
3	frühen	früh	ADJA	ADJA	Pos|Masc|Dat|Sg|_|	4	attr	_	_ 
4	Tod	Tod	N	NN	Masc|Dat|Sg	1	pn	_	_ 
5	heiratete	heiraten	V	VVFIN	3|Sg|Past|_	0	root	_	_ 
6	Oswald	Oswald	N	NE	_|Nom|Sg	5	subj	_	_ 
7	Mosley	Mosley	N	NE	_|Nom|Sg	6	app	_	_ 
8	seine	sein	ART	PPOSAT	_|Akk|_	10	det	_	_ 
9	damalige	damalig	ADJA	ADJA	Pos|_|Akk|_|_|	10	attr	_	_ 
10	Geliebte	Gelieben	N	NN	_|Akk|_	5	obja	_	_ 
11	Diana	Diana	N	NE	Masc|Akk|Sg	10	app	_	_ 
12	Mitford	Mitford	N	NE	Masc|Akk|Sg	11	app	_	_ 
13	,	,	$,	$,	_	0	root	_	_ 
14	Ehefrau	Ehefrau	N	NN	Fem|Akk|Sg	12	app	_	_ 
15	des	das	ART	ART	Def|Masc|Gen|Sg	16	det	_	_ 
16	Brauereierben	Brauereierbe	N	NN	Masc|Gen|Sg	14	gmod	_	_ 
17	Bryan	Bryan	N	NE	Masc|_|Sg	16	app	_	_ 
18	Walter	Walter	N	NE	Masc|_|Sg	17	app	_	_ 
19	Guinness	Guinness	N	NN	Masc|_|Sg	18	app	_	_ 
		
		PropSubj:Carla Bruni
PropObj:Nicolas Sarkozy
sentence:Alessandra Martines ist eine Cousine zweiten Grades von Carla Bruni , der Ehefrau des ehemaligen französischen Staatspräsidenten Nicolas Sarkozy , und wuchs ab ihrem fünften Lebensjahr in Frankreich auf . 
1	Alessandra	Alessandra	N	NE	_|Nom|Sg	3	subj	_	_ 
2	Martines	Martines	N	NE	_|Nom|Sg	1	app	_	_ 
3	ist	sein	V	VAFIN	3|Sg|Pres|Ind	0	root	_	_ 
4	eine	ein	ART	ART	Indef|Fem|Nom|Sg	5	det	_	_ 
5	Cousine	Cousine	N	NN	Fem|Nom|Sg	3	pred	_	_ 
6	zweiten	zweit	ADJA	ADJA	_|Masc|Gen|Sg|_|	7	attr	_	_ 
7	Grades	Grad	N	NN	Masc|Gen|Sg	5	gmod	_	_ 
8	von	von	PREP	APPR	Dat	7	pp	_	_ 
9	Carla	Carla	N	NE	Fem|Dat|Sg	8	pn	_	_ 
10	Bruni	Bruni	N	NE	Fem|Dat|Sg	9	app	_	_ 
11	,	,	$,	$,	_	0	root	_	_ 
12	der	der	ART	ART	Def|Fem|Dat|Sg	13	det	_	_ 
13	Ehefrau	Ehefrau	N	NN	Fem|Dat|Sg	9	app	_	_ 
14	des	das	ART	ART	Def|Masc|Gen|Sg	17	det	_	_ 
15	ehemaligen	ehemalig	ADJA	ADJA	Pos|Masc|Gen|Sg|_|	17	attr	_	_ 
16	französischen	französisch	ADJA	ADJA	Pos|Masc|Gen|Sg|_|	17	attr	_	_ 
17	Staatspräsidenten	Staatspräsident	N	NN	Masc|Gen|Sg	13	gmod	_	_ 
18	Nicolas	Nicolas	N	NE	Masc|Gen|Sg	17	app	_	_ 
19	Sarkozy	Sarkozy	N	NE	Masc|Gen|Sg	18	app	_	_ 
20	,	,	$,	$,	_	0	root	_	_ 
21	und	und	KON	KON	_	3	kon	_	_ 
22	wuchs	wachsen	V	VVFIN	3|Sg|Past|Ind	21	cj	_	_ 
23	ab	ab	PREP	APPR	Dat	22	pp	_	_ 
24	ihrem	ihr	ART	PPOSAT	_|Dat|Sg	25	det	_	_ 
25	fünften	fünften	N	NN	_|Dat|Sg	23	pn	_	_ 
26	Lebensjahr	Lebensjahr	N	NN	Neut|Nom|Sg	22	subj	_	_ 
27	in	in	PREP	APPR	_	22	pp	_	_ 
28	Frankreich	Frankreich	N	NE	Neut|_|Sg	27	pn	_	_ 
29	auf	auf	PTKVZ	PTKVZ	_	22	avz	_	_ 
30	.	.	$.	$.	_	0	root	_	_ 	



		// query 3 ok*/
		
		String query3 = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{"
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "?y <conll:cpostag> ?lemma_pos . "
				+ "?y <conll:deprel> ?lemma_grammar . "
				+ "?y <conll:deprel> \"app\" . "
				+ "?y <conll:cpostag> \"N\" . "
				+ "?y <conll:lemma> ?lemma . "
				+ "{?y <conll:head> ?e1 . }"
				+ "UNION"
				+ "{?e1 <conll:head> ?y }. "
				+ "?e2 <conll:head> ?y . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
				//+ "FILTER regex(?e2_grammar, \"obj\") ."
				//+ "UNION"
				//+ "{FILTER regex(?e2_grammar, \"gmod\") .}"
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		
		
		/*################################
PropSubj:Adelheid von Meißen
PropObj:Ottokar I Přemysl
sentence:Adelheid von Meißen tschechisch Adléta Míšeňská * nach 1160 ; † 2. Februar 1211 in Meißen war die erste Ehefrau des böhmischen Fürsten und Königs Ottokar I . Přemysl . 
1	Adelheid	Adelheid	N	NE	_	0	root	_	_ 
2	von	von	PREP	APPR	Dat	1	pp	_	_ 
3	Meißen	Meißen	N	NE	Neut|Dat|Sg	2	pn	_	_ 
4	tschechisch	tschechisch	ADV	ADJD	Pos|	6	adv	_	_ 
5	Adléta	Adléta	ADJA	ADJA	_	6	attr	_	_ 
6	Míšeňská	Míšeňská	N	NN	Neut|Dat|Sg	3	app	_	_ 
7	*	*	XY	XY	_	0	root	_	_ 
8	nach	nach	PREP	APPR	Dat	2	kon	_	_ 
9	1160	1160	CARD	CARD	_	8	pn	_	_ 
10	;	;	$.	$.	_	0	root	_	_ 
11	†	†	ADJA	ADJA	_|Masc|Nom|Sg|_	13	attr	_	_ 
12	2.	2.	ADJA	ADJA	_|Masc|Nom|Sg|_|	13	attr	_	_ 
13	Februar	Februar	N	NN	Masc|Nom|Sg	17	subj	_	_ 
14	1211	1211	CARD	CARD	_	13	app	_	_ 
15	in	in	PREP	APPR	_	14	pp	_	_ 
16	Meißen	Meißen	N	NE	Neut|_|Sg	15	pn	_	_ 
17	war	sein	V	VAFIN	3|Sg|Past|Ind	0	root	_	_ 
18	die	die	ART	ART	Def|Fem|Nom|Sg	20	det	_	_ 
19	erste	erst	ADJA	ADJA	_|Fem|Nom|Sg|_|	20	attr	_	_ 
20	Ehefrau	Ehefrau	N	NN	Fem|Nom|Sg	17	pred	_	_ 
21	des	das	ART	ART	Def|Masc|Gen|Sg	23	det	_	_ 
22	böhmischen	böhmisch	ADJA	ADJA	Pos|Masc|Gen|Sg|_|	23	attr	_	_ 
23	Fürsten	Fürst	N	NN	Masc|Gen|Sg	20	gmod	_	_ 
24	und	und	KON	KON	_	23	kon	_	_ 
25	Königs	König	N	NN	Masc|Gen|Sg	24	cj	_	_ 
26	Ottokar	Ottokar	N	NE	Masc|Gen|Sg	25	app	_	_ 
27	I	I	FM	FM	Masc|Gen|Sg	26	app	_	_ 
28	.	.	$.	$.	_	0	root	_	_ 
29	Přemysl	Přemysl	N	NE	_	0	root	_	_ 
30	.	.	$.	$.	_	0	root	_	_ 	
----------------------

PropSubj:Kim Jong-un
PropObj:Ri Sol-ju
sentence:Ri Sol-ju , oder ; * Januar 1985–1989 ist die Ehefrau des nordkoreanischen Führers Kim Jong-un . 
1	Ri	Ri	ADJA	ADJA	_	2	attr	_	_ 
2	Sol-ju	Sol-ju	N	NN	_	0	root	_	_ 
3	,	,	$,	$,	_	0	root	_	_ 
4	oder	oder	KON	KON	_	0	root	_	_ 
5	;	;	$.	$.	_	0	root	_	_ 
6	*	*	XY	XY	_	0	root	_	_ 
7	Januar	Januar	N	NE	Masc|Nom|Sg	9	subj	_	_ 
8	1985–1989	1985–1989	N	NN	Masc|Nom|Sg	7	app	_	_ 
9	ist	sein	V	VAFIN	3|Sg|Pres|Ind	0	root	_	_ 
10	die	die	ART	ART	Def|Fem|Nom|Sg	11	det	_	_ 
11	Ehefrau	Ehefrau	N	NN	Fem|Nom|Sg	9	pred	_	_ 
12	des	das	ART	ART	Def|Masc|Gen|Sg	14	det	_	_ 
13	nordkoreanischen	Nordkoreanisch	ADJA	ADJA	Pos|Masc|Gen|Sg|_|	14	attr	_	_ 
14	Führers	Führer	N	NN	Masc|Gen|Sg	11	gmod	_	_ 
15	Kim	Kim	N	NE	Masc|Gen|Sg	14	app	_	_ 
16	Jong-un	Jong-un	N	NE	Masc|Gen|Sg	15	app	_	_ 
17	.	.	$.	$.	_	0	root	_	_ 	
----------------------

PropSubj:Minnesota River
PropObj:Big Stone Lake
sentence:Der Big Stone Lake ist die Quelle des Minnesota River , einem 534 km langer Nebenfluss des Mississippi River . 
1	Der	der	ART	ART	Def|Fem|Dat|Sg	4	det	_	_ 
2	Big	Big	ADV	ADJD	_	3	adv	_	_ 
3	Stone	Stone	ADJA	ADJA	_|Fem|Dat|Sg|_	4	attr	_	_ 
4	Lake	Lake	N	NN	Fem|Dat|Sg	5	objd	_	_ 
5	ist	sein	V	VAFIN	3|Sg|Pres|Ind	0	root	_	_ 
6	die	die	ART	ART	Def|Fem|Nom|Sg	7	det	_	_ 
7	Quelle	Quell	N	NN	Fem|Nom|Sg	5	subj	_	_ 
8	des	das	ART	ART	Def|_|Gen|Sg	9	det	_	_ 
9	Minnesota	Minnesota	N	NE	_|Gen|Sg	7	gmod	_	_ 
10	River	River	N	NE	_|Dat|Sg	9	app	_	_ 
11	,	,	$,	$,	_	0	root	_	_ 

*/
		
		String query5 = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{"
				+ "?e1 <conll:head> ?verb . "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				//+ "FILTER regex(?e1_grammar, \"subj\") ."
				+ "FILTER( regex(?e1_grammar, \"obj\") || regex(?e1_grammar, \"subj\"))"
				+ "?y <conll:cpostag> ?lemma_pos . "
				+ "?y <conll:cpostag> \"N\" . "
				+ "?y <conll:deprel> \"pred\" . "
				+ "FILTER( regex(?lemma_grammar, \"subj\") || regex(?lemma_grammar, \"pred\"))"
				+ "?y <conll:lemma> ?lemma . "
				+ "?y <conll:head> ?verb . "
				+ "?verb <conll:cpostag> \"V\" ."
				+ "?verb <conll:lemma> \"sein\" ."
				+ "?e2 <conll:head> ?y . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		
	
/*
 * 
PropSubj:Tom Hayden
PropObj:Barbara Williams
sentence:Barbara Williams ist verheiratet mit dem sozialpolitischen Aktivisten Tom Hayden , mit dem sie gemeinsam in Los Angeles lebt . 
1	Barbara	Barbara	N	NE	_|Nom|Sg	3	subj	_	_ 
2	Williams	Williams	N	NE	_|Nom|Sg	1	app	_	_ 
3	ist	sein	V	VAFIN	3|Sg|Pres|Ind	0	root	_	_ 
4	verheiratet	verheiratet	ADV	ADJD	Pos|_	3	pred	_	_ 
5	mit	mit	PREP	APPR	Dat	3	pp	_	_ 
6	dem	das	ART	ART	Def|Masc|Dat|Sg	8	det	_	_ 
7	sozialpolitischen	sozialpolitisch	ADJA	ADJA	Pos|Masc|Dat|Sg|_|	8	attr	_	_ 
8	Aktivisten	Aktivist	N	NN	Masc|Dat|Sg	5	pn	_	_ 
9	Tom	Tom	N	NE	Masc|Dat|Sg	8	app	_	_ 
10	Hayden	Hayden	N	NE	Masc|Dat|Sg	9	app	_	_ 
11	,	,	$,	$,	_	0	root	_	_ 
12	mit	mit	PREP	APPR	Dat	19	pp	_	_ 
13	dem	das	PRO	PRELS	Masc|Dat|Sg	12	pn	_	_ 
14	sie	sie	PRO	PPER	3|Sg|Fem|Nom	19	subj	_	_ 
15	gemeinsam	gemeinsam	ADV	ADJD	Pos|	16	adv	_	_ 
16	in	in	PREP	APPR	_	19	pp	_	_ 
17	Los	Los	N	NN	Neut|_|Sg	16	pn	_	_ 
18	Angeles	Angeles	N	NE	Neut|_|Sg	17	app	_	_ 
19	lebt	leben	V	VVFIN	3|Sg|Pres|Ind	10	rel	_	_ 
20	.	.	$.	$.	_	0	root	_	_ 	
----------------------
 */
		String verheiratet_mit = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{ "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "FILTER regex(?e1_grammar, \"subj\") ."
				+ "?e1 <conll:cpostag> ?e1_pos . "
				+ "?e1 <conll:head> ?verb . "
				+ "?verb <conll:cpostag> \"V\" . "
				+ "?y <conll:head> ?verb . "
				+ "?y <conll:postag> ?lemma_pos . "
				+ "FILTER regex(?lemma_pos, \"ADJ\") ."
				+ "?y <conll:deprel> ?lemma_grammar . "
				+ "?y <conll:lemma> ?lemma . "
				+ "?p <conll:head> ?verb . "
				+ "?p <conll:deprel> \"pp\" . "
				+ "?p <conll:form> ?prep . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
				//+ "{?e2 <conll:deprel> \"pn\" . }"
				//+ "UNION"
				//+ "FILTER regex(?e2_grammar, \"obj\") ."
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		
/*
 * PropSubj:Barbara Amiel
PropObj:Conrad Black
sentence:Conrad Black heiratete im Juli 1992 die englische Journalistin und Schriftstellerin Barbara Amiel * 4. Dezember 1940 , die damals Kolumnistin der Londoner Times war und vor dieser Ehe mit Conrad Black bereits mit David Graham , George Jonas und Gary Smith verheiratet gewesen war . 
1	Conrad	Conrad	N	NE	_|Nom|Sg	3	subj	_	_ 
2	Black	Black	FM	FM	_|Nom|Sg	1	app	_	_ 
3	heiratete	heiraten	V	VVFIN	3|Sg|Past|_	0	root	_	_ 
4	im	im	PREP	APPRART	Dat	3	pp	_	_ 
5	Juli	Juli	N	NE	Masc|Dat|Sg	4	pn	_	_ 
6	1992	1992	CARD	CARD	_	5	app	_	_ 
7	die	die	ART	ART	Def|Fem|Akk|Sg	9	det	_	_ 
8	englische	englisch	ADJA	ADJA	Pos|Fem|Akk|Sg|_|	9	attr	_	_ 
9	Journalistin	Journalistin	N	NN	Fem|Akk|Sg	3	obja	_	_ 
		----------------------*/
		String query7 = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{ "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "FILTER regex(?e1_grammar, \"subj\") ."
				+ "?e1 <conll:cpostag> ?e1_pos . "
				//+ "FILTER regex(?e1_pos, \"NN\") ."
				+ "?e1 <conll:head> ?y . "
				+ "?y <conll:cpostag> ?lemma_pos . "
				//VA ignorieren
				+ "FILTER regex(?lemma_pos, \"V\") ."
				+ "?y <conll:deprel> ?lemma_grammar . "
				+ "?y <conll:lemma> ?lemma . "
				+ "?e2 <conll:head> ?y . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
				//+ "UNION"
				//+ "{FILTER regex(?e2_grammar, \"pn\") .}"
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		

		

		
		
		
		/*PropSubj:Ballads
		PropObj:John Coltrane
		sentence:Ballads ist ein Jazz-Album von John Coltrane , aufgenommen durch Rudy Van Gelder am 21. Dezember 1961 , sowie am 18. September und 13. November 1962 und veröffentlicht auf Impulse ! 
		1	Ballads	Ballads	N	NN	_|Nom|Sg	2	subj	_	_ 
		2	ist	sein	V	VAFIN	3|Sg|Pres|Ind	0	root	_	_ 
		3	ein	ein	ART	ART	Indef|Neut|Akk|Sg	4	det	_	_ 
		4	Jazz-Album	Jazz-Album	N	NN	Neut|Akk|Sg	2 obja__ 
		5	von	von	PREP	APPR	Dat	4	pp	_	_ 
		6	John	John	N	NE	_|Dat|_	5	pn	_	_ 
		7	Coltrane	Coltrane	N	NN	Neut|Akk|Sg	4	app	_


		PropSubj:Blue Moods
		PropObj:Miles Davis
		sentence:Blue Moods ist ein Jazz-Album von Miles Davis , aufgenommen am 9. Juli 1955 im Rudy Van Gelder Studio in Hackensack New Jersey . 
		1	Blue	Blue	N	NE	_|Nom|Sg	3	subj	_	_ 
		2	Moods	Moods	N	NE	_|Nom|Sg	1	app	_	_ 
		3	ist	sein	V	VAFIN	3|Sg|Pres|Ind	0	root	_	_ 
		4	ein	ein	ART	ART	Indef|Neut|Akk|Sg	5	det	_	_ 
		5	Jazz-Album	Jazz-Album	N	NN	Neut|Akk|Sg	3	obja	_	_ 
		6	von	von	PREP	APPR	Dat	5	pp	_	_ 
		7	Miles	Miles	N	NE	_|Dat|_	6	pn	_	_ 
		8	Davis	Davis	N	NE	_|Dat|_	7	app	_	_ 

		PropSubj:Chattanooga Choo Choo
		PropObj:Mack Gordon
		sentence:Chattanooga Choo Choo ist ein Swing-Titel von Mack Gordon , der den Text schrieb und Harry Warren , der das Lied komponierte . 
		1	Chattanooga	Chattanooga	N	NE	_|Nom|Sg	4	subj	_	_ 
		2	Choo	Choo	N	NE	_|Nom|Sg	1	app	_	_ 
		3	Choo	Choo	N	NE	_|Nom|Sg	2	app	_	_ 
		4	ist	sein	V	VAFIN	3|Sg|Pres|Ind	0	root	_	_ 
		5	ein	ein	ART	ART	Indef|Masc|Nom|Sg	6	det	_	_ 
		6	Swing-Titel	Swing-Titel	N	NN	Masc|Nom|Sg	4	pred	_	_ 
		7	von	von	PREP	APPR	Dat	6	pp	_	_ 
		8	Mack	Mack	N	NE	_|Dat|_	7	pn	_	_ 
		9	Gordon	Gordon	N	NE	Masc|Dat|Sg	8	app	_*/
		//"Ähnlich" Query 5
		String query13 = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{"
				+ "?e1 <conll:head> ?verb . "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "FILTER regex(?e1_grammar, \"subj\") ."
				+ "?y <conll:cpostag> ?lemma_pos . "
				+ "?y <conll:cpostag> \"N\" . "
				+ "?y <conll:deprel> ?lemma_grammar . "
				+ "FILTER( regex(?lemma_grammar, \"pred\") || regex(?lemma_grammar, \"obj\"))"
				+ "?y <conll:lemma> ?lemma . "
				+ "?y <conll:head> ?verb . "
				+ "?verb <conll:cpostag> \"V\" ."
				+ "?verb <conll:lemma> \"sein\" ."
				+ "?p <conll:head> ?y . "
				+ "?p <conll:deprel> \"pp\" . "
				+ "?p <conll:form> ?prep . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		/*
		 * PropSubj:Yangtze River Express
PropObj:2003 Januar 
sentence:Yangtze River Express wurde am 15. Januar 2003 gegründet . 
1	Yangtze	Yangtze	N	NN	_|Nom|Sg	4	subj	_	_ 
2	River	River	N	NE	_|Nom|Sg	1	app	_	_ 
3	Express	Express	N	NE	_|Nom|Sg	2	app	_	_ 
4	wurde	werden	V	VAFIN	3|Sg|Past|Ind	0	root	_	_ 
5	am	am	PREP	APPRART	Dat	9	pp	_	_ 
6	15.	15.	ADJA	ADJA	_|Masc|Dat|Sg|_|	7	attr	_	_ 
7	Januar	Januar	N	NE	Masc|Dat|Sg	5	pn	_	_ 
8	2003	2003	CARD	CARD	_	7	app	_	_ 
9	gegründet	gründen	V	VVPP	_	4	aux	_	_ 
10	.	.	$.	$.	_	0	root	_	_ 	
----------------------
		 */
		String query13a = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{"
				+ "?e1 <conll:head> ?verb . "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "FILTER regex(?e1_grammar, \"subj\") ."
				+ "?y <conll:cpostag> ?lemma_pos . "
				+ "?y <conll:cpostag> \"V\" . "
				+ "?y <conll:deprel> ?lemma_grammar . "
				+ "FILTER( regex(?lemma_grammar, \"aux\"))"
				+ "?y <conll:lemma> ?lemma . "
				+ "?y <conll:head> ?verb . "
				+ "?verb <conll:cpostag> \"V\" ."
				//+ "?verb <conll:lemma> \"sein\" ."
				+ "?p <conll:head> ?y . "
				+ "?p <conll:deprel> \"pp\" . "
				+ "?p <conll:form> ?prep . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER( regex(?e2_grammar, \"obj\") || regex(?e2_grammar, \"gmod\") || regex(?e2_grammar, \"pn\"))"
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
		tmp.add(x_gründen_am_x);
		tmp.add("StateVerb");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query2);
		tmp.add("Noun");
		queries.add(tmp);
		tmp = new ArrayList<String>();
		tmp.add(query2a);
		tmp.add("Noun");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query3);
		tmp.add("Noun");
		queries.add(tmp);

		tmp = new ArrayList<String>();
		tmp.add(query5);
		tmp.add("Noun");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(verheiratet_mit);
		tmp.add("Adjective");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query7);
		tmp.add("StateVerb");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query13);
		tmp.add("Noun");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query13a);
		tmp.add("StateVerb");
		queries.add(tmp);
	

		
		return queries;
	}








}
