package de.citec.sc.matoll.patterns.spanish;
import java.util.ArrayList;
import java.util.List;


public class SpanishQuery {
public static List<List<String>> getQueries() {
		
		//Map<String, String> queries = new HashMap<String, String>();
		List<List<String>> queries = new ArrayList<List<String>>();
		/*
Neuer Parse:
1	Aníbal	aníbal	n	NP00000	_	2	SUBJ	_	_
2	cruza	cruzar	v	VMIP3S0	_	0	ROOT	_	_
3	los	el	d	DA0MP0	_	4	SPEC	_	_
4	Alpes	alpes	n	NP00000	_	2	DO	_	_
5	a	a	s	SPS00	_	2	OBLC	_	_
6	el	el	d	DA0MS0	_	7	SPEC	_	_
7	mando	mando	n	NCMS000	_	5	COMP	_	_
8	de	de	s	SPS00	_	7	MOD	_	_
9	el	el	d	DA0MS0	_	10	SPEC	_	_
10	ejército	ejército	n	NCMS000	_	8	COMP	_	_
11	cartaginés	cartaginés	a	AQ0MS0	_	10	MOD	_	_
12	.	.	f	Fp	_	11	punct	_	_

//query 1 ok, auch bei neunem Parse

x verb y - ohne preposition
		 */
		String query1 = "SELECT ?class ?lemma ?dobj_lemma ?advmod_lemma ?lemma_pos ?dobj_lemma ?lemma_grammar ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{?y <conll:postag> ?lemma_pos . "
				//POSTAG nach VM prüfen Verbos principales (Hauptverb)
				+ "FILTER regex(?lemma_pos, \"VMIP\") ."
				+ "?y <conll:deprel> \"ROOT\" ."
				+ "?y <conll:form> ?lemma . "
				+ "?y <conll:deprel> ?lemma_grammar. "
				+ "?e1 <conll:head> ?y . "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "?e1 <conll:deprel> ?deprel. "
				+ "FILTER regex(?deprel, \"SUBJ\") ."
				+ "?e2 <conll:head> ?y . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "?e2 <conll:form> ?e2_form . "
				+ "?e2 <conll:deprel> ?deprel2. "
				+ "?e2 <conll:deprel> \"DO\" . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
	
		
/*
1	T'Pel	t'pel	n	NCMS000	_	2	SUBJ	_	_
2	es	ser	v	VSIP3S0	_	0	ROOT	_	_
3	la	el	d	DA0FS0	_	4	SPEC	_	_
4	esposa	esposo	n	NCFS000	_	2	ATR	_	_
5	de	de	s	SPS00	_	4	MOD	_	_
6	Tuvok	tuvok	n	NP00000	_	5	COMP	_	_
7	.	.	f	Fp	_	6	punct	_	_

//query2 auch ok nach neuem Parse

 */
		String query2 = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{?y <conll:cpostag> ?lemma_pos . "
				+ "?y <conll:deprel> ?lemma_grammar . "
				+ "?y <conll:form> ?lemma . "
				+ "?y <conll:head> ?verb . "
				+ "?y <conll:deprel> \"ATR\" . "
				+ "?p <conll:postag> \"SPS00\" ."
				+ "?y <conll:cpostag> \"n\" . "
				+" ?y <conll:postag> \"NCFS000\" ."
				+ "?p <conll:deprel> \"MOD\" . "
				+ "?verb <conll:postag> ?verb_pos . "
				+ "FILTER regex(?verb_pos, \"VS\") ."
				+ "?e1 <conll:head> ?verb . "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "FILTER regex(?e1_grammar, \"SUBJ\") ."
				+ "?p <conll:head> ?y . "
				+ "?p <conll:form> ?prep . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:postag> \"NP00000\" . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER regex(?e2_grammar, \"COMP\") ."
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		/*
		 * 
propSubject:flickr
propObject:ludicorp
--------------
sentence:Ludicorp es la empresa creadora de Flickr , sitio web de organizacin de fotografas digitales y red social . 
1	Ludicorp	ludicorp	n	NP00000	_	2	SUBJ
2	es	ser	v	VSIP3S0	_	0	ROOT
3	la	el	d	DA0FS0	_	4	SPEC
4	empresa	empresa	n	NCFS000	_	2	ATR
5	creadora	creador	a	AQ0FS0	_	4	MOD
6	de	de	s	SPS00	_	4	MOD
7	Flickr	flickr	n	NP00000	_	6	COMP
		 */
		String query_creadora = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{?y <conll:cpostag> ?lemma_pos . "
				+ "?y <conll:deprel> ?lemma_grammar . "
				+ "?y <conll:form> ?lemma . "
				+ "?y <conll:head> ?blank . "
				+ "?y <conll:deprel> \"MOD\" . "
				+ "?y <conll:postag> \"AQ0FS0\". "
				+ "?blank <conll:head> ?verb. "
				+ "?blank <conll:deprel> \"ATR\". "
				+ "?verb <conll:postag> ?verb_pos . "
				+ "FILTER regex(?verb_pos, \"VS\") ."
				+ "?e1 <conll:head> ?verb . "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "FILTER regex(?e1_grammar, \"SUBJ\") ."
				+ "?p <conll:head> ?blank . "
				+ "?p <conll:deprel> \"MOD\". "
				+ "?p <conll:postag> \"SPS00\". "
				+ "?p <conll:form> ?prep . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:cpostag> \"n\" . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER regex(?e2_grammar, \"COMP\") ."
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		
/*
 //Old parse:
PropSubj:Eva Braun
PropObj:Adolf Hitler
sentence:Hermann Fegelein es más conocido por ser cuñado de la esposa de Adolf Hitler , Eva Braun . 
1	Hermann	_	n	NCMS000	_	0	ROOT
2	Fegelein	_	n	NP00000	_	1	MOD
3	es	_	v	VSIP3S0	_	0	ROOT
4	más	_	r	RG	_	5	SPEC
5	conocido	_	v	VMP00SM	_	3	ATR
6	por	_	s	SPS00	_	5	BYAG
7	ser	_	v	VSN0000	_	6	COMP
8	cuñado	_	v	VMP00SM	_	3	MOD
9	de	_	s	SPS00	_	3	MOD
10	la	_	d	DA0FS0	_	11	SPEC
11	esposa	_	n	NCFS000	_	9	COMP
12	de	_	s	SPS00	_	11	COMP
13	Adolf	_	n	NP00000	_	12	COMP
14	Hitler	_	n	NP00000	_	11	MOD
15	,	_	f	Fc	_	14	punct
16	Eva	_	n	NP00000	_	11	MOD
17	Braun	_	n	NP00000	_	16	MOD
18	.	_	f	Fp	_	17	punct
New parse:
(basically new pattern...)
1	Hermann_Fegelein	hermann_fegelein	n	NP00000	_	2	SUBJ	_	_
2	es	ser	v	VSIP3S0	_	0	ROOT	_	_
3	más	más	r	RG	_	4	SPEC	_	_
4	conocido	conocer	v	VMP00SM	_	2	ATR	_	_
5	por	por	s	SPS00	_	4	BYAG	_	_
6	ser	ser	v	VSN0000	_	5	COMP	_	_
7	cuñado	cuñado	n	NCMS000	_	6	ATR	_	_
8	de	de	s	SPS00	_	7	COMP	_	_
9	la	el	d	DA0FS0	_	10	SPEC	_	_
10	esposa	esposo	n	NCFS000	_	8	COMP	_	_
11	de	de	s	SPS00	_	10	COMP	_	_
12	Adolf_Hitler	adolf_hitler	n	NP00000	_	11	COMP	_	_
13	,	,	f	Fc	_	12	punct	_	_
14	Eva_Braun	eva_braun	n	NP00000	_	12	MOD	_	_
15	.	.	f	Fp	_	14	punct	_	_

TODO:Check Query

	 */
		String query2a = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{?y <conll:postag> ?lemma_pos . "
				+ "FILTER regex(?lemma_pos, \"NC\") ."
				+ "?y <conll:deprel> ?lemma_grammar . "
				//check if nois goes away, if COMP is set as relation of the lemma to the head
				+ "?y <conll:deprel> \"COMP\" . "
				+ "?y <conll:form> ?lemma . "
				//has a preposition, but is not between the entities
				+ "{?p <conll:head> ?y . "
				+ "?e1 <conll:head> ?y . "
				+ "?e2 <conll:head> ?y . }"
				+ "UNION"
				+ "{?p <conll:head> ?y . "
				+ "?e1 <conll:head> ?p . "
				+ "?e2 <conll:head> ?e1 . }"
				+ "?p <conll:postag> ?p_pos . "
				+ "?p <connl:form> ?prep ."
				+ "FILTER regex(?p_pos, \"SP\") ."
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:postag> ?e1_pos . "
				+ "FILTER regex(?e1_pos, \"NP0\") ."
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "FILTER regex(?e1_grammar, \"MOD\") ."
				+ "?e2 <conll:postag> ?e2_pos . "
				+ "FILTER regex(?e2_pos, \"NP0\") ."
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER regex(?e2_grammar, \"MOD\") ."
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		
/*
PropSubj:Rita Wilson
PropObj:Tom Hanks
sentence:Rita Wilson , esposa de Tom Hanks asistió a la presentación de "“My Big Fat Greek Wedding”" en un teatro de Los Ángeles . 
1	Rita	_	v	VMIP3S0	_	0	ROOT
2	Wilson	_	n	NP00000	_	1	DO
3	,	_	f	Fc	_	2	punct
4	esposa	_	n	NCFS000	_	2	_
5	de	_	s	SPS00	_	4	COMP
6	Tom	_	n	NP00000	_	5	COMP
7	Hanks	_	n	NP00000	_	4	MOD
NEW PARSE:
1	Rita_Wilson	rita_wilson	n	NP00000	_	0	ROOT	_	_
2	,	,	f	Fc	_	1	punct	_	_
3	esposa	esposo	n	NCFS000	_	1	MOD	_	_
4	de	de	s	SPS00	_	3	MOD	_	_
5	Tom_Hanks	tom_hanks	n	NP00000	_	4	COMP	_	_


 
 PropSubj:Félix Yusupov
PropObj:Irina Alexándrovna
sentence:Los visitantes en Seeon incluyeron al príncipe Félix Yusupov , marido de la princesa Irina Alexándrovna de Rusia , que escribió: «Demando categóricamente que ella no es Anastasia Nikoláyevna , es solamente una aventurera , enferma de histeria y una actriz espantosa . 
1	Los	_	d	DA0MP0	_	2	SPEC
2	visitantes	_	n	NCCP000	_	5	SUBJ
3	en	_	s	SPS00	_	2	MOD
4	Seeon	_	n	NP00000	_	3	COMP
5	incluyeron	_	v	VMIS3P0	_	0	ROOT
6	al	_	a	AQ0CS0	_	5	MOD
7	príncipe	_	n	NCMS000	_	5	DO
8	Félix	_	n	NP00000	_	7	MOD
9	Yusupov	_	n	NP00000	_	7	MOD
10	,	_	f	Fc	_	9	punct
11	marido	_	n	NCMS000	_	7	COMP
12	de	_	s	SPS00	_	11	MOD
13	la	_	d	DA0FS0	_	14	SPEC
14	princesa	_	n	NCFS000	_	12	COMP
15	Irina	_	a	AQ0FS0	_	14	MOD
16	Alexándrovna	_	n	NP00000	_	14	MOD

/TODO: check marido
 * 
 * NEW PARSER:
 * 1	Pero	pero	c	CC	_	0	ROOT
2	Morayma	morayma	n	NP00000	_	1	_
3	,	,	f	Fc	_	2	punct
4	esposa	esposo	n	NCFS000	_	2	_
5	de	de	s	SPS00	_	4	COMP
6	Boabdil	boabdil	n	NP00000	_	5	COMP

7	Granada	granada	n	NP00000	_	6	COMP
8	,	,	f	Fc	_	7	punct
9	Morayma	morayma	n	NP00000	_	7	_
10	,	,	f	Fc	_	9	COORD
//Falsch, da es auf das komma zeigt...
11	esposa	esposo	n	NCFS000	_	10	CONJ
12	de	de	s	SPS00	_	11	MOD
13	Boabdil	boabdil	n	NP00000	_	12	COMP

1	Pero	pero	c	CC	_	0	ROOT
2	Morayma	morayma	n	NP00000	_	1	_
3	,	,	f	Fc	_	2	punct
4	esposa	esposo	n	NCFS000	_	2	_
5	de	de	s	SPS00	_	4	COMP
6	Boabdil	boabdil	n	NP00000	_	5	COMP

äquivalent zu englisch query 2
 */
		String query2b= "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{?y <conll:cpostag> ?lemma_pos . "
				+ "{?y <conll:deprel> \"_\"."
				+ "?y <conll:postag> \"NCFS000\".}"
				//for marido example
				+ "UNION"
				+ "{?y <conll:deprel> \"COMP\"."
				+ "?y <conll:postag> \"NCMS000\".}"
				+ "UNION"
				+ "{?y <conll:postag> \"NCFS000\". ?y <conll:deprel> \"_\". ?e1 <conll:deprel> \"_\".}"
				+ "UNION"
				+ "{?y <conll:postag> \"NCFS000\". ?y <conll:deprel> \"CONJ\". ?e1 <conll:deprel> \"_\".}"
				+ "?y <conll:form> ?lemma . "
				+ "?y <conll:deprel> ?lemma_grammar. "
				+ "?y <conll:cpostag> \"n\" . "
				+ "?y <conll:head> ?e1 . "
				+ "?e1 <conll:cpostag> \"n\" . "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				//argument to which the lemma points (e1) can not be root in a correct parse tree and also not a conjunction)
				+ "FILTER( STR(?e1_grammar) != \"ROOT\"). "
				+ "FILTER( STR(?e1_grammar) != \"CONJ\"). "
				+ "?p <conll:head> ?y . "
				+ "{?p <conll:deprel> \"COMP\" . }"
				+ " UNION "
				+ "{?p <conll:deprel> \"MOD\" . }"
				+ "?p <conll:form> ?prep . "
				+ "?p <conll:postag> \"SPS00\" . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "?e2 <conll:form> ?e2_form . "
				+ "?e2 <conll:deprel>  \"COMP\". "
				+ "?e2 <conll:cpostag> \"n\" . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		
		
		
		
		
		/*PropSubj:Felipe de Suabia
		PropObj:Irene Ángelo
		sentence:Su primo Felipe de Suabia estaba casado con Irene Angelina , hija del desposeído emperador bizantino Isaac II Ángelo y sobrina de la segunda esposa de Conrado , Teodora . 
		1	Su	_	d	DP3CS0	_	2	SPEC
		2	primo	_	n	NCMS000	_	6	SUBJ
		3	Felipe	_	n	NP00000	_	2	MOD
		4	de	_	s	SPS00	_	2	MOD
		5	Suabia	_	n	NP00000	_	4	COMP
		6	estaba	_	v	VAII4S0	_	0	ROOT
		7	casado	_	v	VMP00SM	_	6	MOD
		8	con	_	s	SPS00	_	7	OBLC
		9	Irene	_	n	NP00000	_	8	COMP*/
		//SCHLECHTES BEISPIEL, da im Satz Angelina und nicht Ángelo steht...
		//married to -> adjectivet´
		//Überprüfen, estaba und estaban und está und están
		//war verheiratet mit Adjektive als Partizip
		/*Neuer Parse:
		 * 1	Su	su	d	DP3CS0	_	2	SPEC	_	_
2	primo	primo	n	NCMS000	_	4	SUBJ	_	_
3	Felipe_de_Suabia	felipe_de_suabia	n	NP00000	_	2	MOD	_	_
4	estaba	estar	v	VAII1S0	_	0	ROOT	_	_
5	casado	casar	v	VMP00SM	_	4	ATR	_	_
6	con	con	s	SPS00	_	5	OBLC	_	_
7	Irene_Angelina	irene_angelina	n	NP00000	_	6	COMP	_	_
		 * 
		 */
		String query3= "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{?y <conll:cpostag> ?lemma_pos . "
				+ "?y <conll:form> ?lemma . "
				+ "?y <conll:deprel> ?lemma_grammar. "
				//+ "?y <conll:cpostag> \"v\" . "
				+ "?y <conll:deprel>  \"ATR\". "
				//+ "?verb <conll:cpostag> \"v\" . "
				+ "?verb <conll:postag> ?postag . "
				+ "FILTER regex(?postag, \"VA\") ."
				+ "?y <conll:head> ?verb . "
				+ "?e1 <conll:head> ?verb . "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "?e1 <conll:deprel>  \"SUBJ\". "
				+ "?p <conll:head> ?y . "
				+ "?p <conll:deprel> \"OBLC\" . "
				+ "?p <conll:form> ?prep . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "?e2 <conll:form> ?e2_form . "
				+ "?e2 <conll:deprel>  \"COMP\". "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		
		/*
		 * SearchTerm 1:similar
SearchTerm 2:al
sentence:El toso es similar al iwai-zake . 
1	El	_	d	DA0MS0	_	2	SPEC
2	toso	_	a	AQ0MS0	_	3	SUBJ
3	es	_	v	VSIP3S0	_	0	ROOT
4	similar	_	a	AQ0CS0	_	3	MOD
5	al	_	a	AQ0CS0	_	6	MOD
6	iwai-zake	_	n	NCMS000	_	3	DO
7	.	_	f	Fp	_	6	punct
Adjective

NEW PARSE:
1	El	el	d	DA0MS0	_	2	SPEC	_	_
2	toso	toser	v	VMIP1S0	_	3	SUBJ	_	_
3	es	ser	v	VSIP3S0	_	0	ROOT	_	_
4	similar	similar	a	AQ0CS0	_	3	ATR	_	_
5	a	a	s	SPS00	_	4	COMP	_	_
6	el	el	d	DA0MS0	_	7	SPEC	_	_
7	iwai-zake	iwai-zake	n	NCMS000	_	5	COMP	_	_
8	.	.	f	Fp	_	7	punct	_	_


		 */
		String query4 = "SELECT ?class ?lemma ?dobj_lemma ?advmod_lemma ?lemma_pos ?dobj_lemma ?lemma_grammar ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{?y <conll:cpostag> ?lemma_pos . "
				+ "?y <conll:cpostag> \"v\" ."
				+ "?y <conll:deprel> \"ROOT\" ."
				+ "?adjective <conll:form> ?lemma . "
				+ "?adjective <conll:deprel> ?lemma_grammar. "
				+ "?adjective <conll:head> ?y ."
				+ "?adjective <conll:cpostag> \"a\" . "
				//+ "?adjective <conll:deprel> \"MOD\" . "
				+ "?adjective <conll:deprel> \"ATR\" . "
				+ "?e1 <conll:head> ?y . "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "?e1 <conll:deprel> ?deprel. "
				+ "FILTER regex(?deprel, \"SUBJ\") ."
				+ "?e2 <conll:head> ?y . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "?e2 <conll:form> ?e2_form . "
				+ "?e2 <conll:deprel> ?deprel2. "
				+ "?e2 <conll:deprel> \"DO\" . "
				+ "?p <conll:head> ?e2 . "
				//+ "?p <conll:deprel> \"MOD\" . "
				+ "?p <conll:form> ?prep . "
				+ "?p <conll:cpostag> \"a\" . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		
		/*
		 * PropSubj:Cartimandua
PropObj:Vellocatus
sentence:Sofocada la revuelta , Cartimandua tomó como marido a Vellocatus , antiguo compañero de armas de Venutius . 
1	Sofocada	_	v	VMP00SF	_	3	MOD
2	la	_	d	DA0FS0	_	3	SPEC
3	revuelta	_	n	NCFS000	_	0	ROOT
4	,	_	f	Fc	_	3	punct
5	Cartimandua	_	n	NP00000	_	6	SUBJ
6	tomó	_	v	VMIS3S0	_	3	MOD
7	como	_	c	CS	_	6	MOD
8	marido	_	n	NCMS000	_	7	COMP
9	a	_	s	SPS00	_	6	DO
10	Vellocatus	_	n	NP00000	_	9	COMP
11	,	_	f	Fc	_	10	punct
12	antiguo	_	a	AQ0MS0	_	13	MOD
13	compañero	_	n	NCMS000	_	3	COMP
14	de	_	s	SPS00	_	13	MOD
15	armas	_	n	NCFP000	_	14	COMP
16	de	_	s	SPS00	_	15	MOD
17	Venutius	_	n	NP00000	_	16	COMP
18	.	_	f	Fp	_	17	punct
New Parse
1	Sofocada	sofocar	v	VMP00SF	_	3	MOD	_	_
2	la	el	d	DA0FS0	_	3	SPEC	_	_
3	revuelta	revuelta	n	NCFS000	_	0	ROOT	_	_
4	,	,	f	Fc	_	3	punct	_	_
5	Cartimandua	cartimandua	n	NP00000	_	6	SUBJ	_	_
6	tomó	tomar	v	VMIS3S0	_	3	MOD	_	_
7	como	como	c	CS	_	6	MOD	_	_
8	marido	marido	n	NCMS000	_	7	COMP	_	_
9	a	a	s	SPS00	_	6	DO	_	_
10	Vellocatus	vellocatus	n	NP00000	_	9	COMP	_	_
11	,	,	f	Fc	_	10	punct	_	_
12	antiguo	antiguo	a	AQ0MS0	_	13	MOD	_	_
13	compañero	compañero	n	NCMS000	_	3	COMP	_	_
14	de	de	s	SPS00	_	13	MOD	_	_
15	armas	arma	n	NCFP000	_	14	COMP	_	_
16	de	de	s	SPS00	_	15	MOD	_	_
17	Venutius	venutius	n	NP00000	_	16	COMP	_	_
18	.	.	f	Fp	_	17	punct	_	_

		 */

		
		
		/*
		 * PropSubj:Agripinila
PropObj:Claudio
sentence:En el año 49 , Claudio se casó por cuarta vez con Agripinila . 
1	En	_	s	SPS00	_	8	MOD
2	el	_	d	DA0MS0	_	3	SPEC
3	año	_	n	NCMS000	_	1	COMP
4	49	_	z	Z	_	3	MOD
5	,	_	f	Fc	_	4	punct
6	Claudio	_	n	NP00000	_	8	SUBJ
7	se	_	p	P03CN000	_	8	MPAS
8	casó	_	v	VMIS3S0	_	0	ROOT
9	por	_	s	SPS00	_	8	MOD
10	cuarta	_	a	AO0FS0	_	11	MOD
11	vez	_	n	NC00000	_	9	COMP
12	con	_	s	SPS00	_	8	MOD
13	Agripinila	_	n	NP00000	_	12	COMP
14	.	_	f	Fp	_	13	punct
----------------------
New Parse:
1	En	en	s	SPS00	_	7	MOD	_	_
2	el	el	d	DA0MS0	_	3	SPEC	_	_
3	año_49	[??:??/??/49:??.??:??]	w	W	_	1	COMP	_	_
4	,	,	f	Fc	_	3	punct	_	_
5	Claudio	claudio	n	NP00000	_	7	MOD	_	_
6	se	se	p	P00CN000	_	7	SUBJ	_	_
7	casó	casar	v	VMIS3S0	_	0	ROOT	_	_
8	por	por	s	SPS00	_	7	MOD	_	_
9	cuarta	4	a	AO0FS0	_	10	MOD	_	_
10	vez	vez	n	NCFS000	_	8	COMP	_	_
11	con	con	s	SPS00	_	10	MOD	_	_
12	Agripinila	agripinila	n	NP00000	_	11	COMP	_	_
13	.	.	f	Fp	_	12	punct	_	_

caso + se verb ->neues Muster
Im neuen ParseTree ist das Muster bescheiden....
		 */
		
		
		
		
		/*
		 * PropSubj:Theo de Raadt
PropObj:Calgary
sentence:El proyecto está liderado por Theo de Raadt , residente en Calgary . 
1	El	_	d	DA0MS0	_	2	SPEC
2	proyecto	_	n	NCMS000	_	3	SUBJ
3	está	_	v	VAIP3S0	_	0	ROOT
4	liderado	_	v	VMP00SM	_	3	ATR
5	por	_	s	SPS00	_	4	BYAG
6	Theo	_	n	NCMS000	_	5	COMP
7	de	_	s	SPS00	_	6	MOD
8	Raadt	_	n	NP00000	_	7	COMP
9	,	_	f	Fc	_	8	punct
10	residente	_	r	RG	_	4	MOD
11	en	_	s	SPS00	_	4	MOD
12	Calgary	_	n	NP00000	_	11	COMP
13	.	_	f	Fp	_	12	punct
residente en 

New Parse:
1	El	el	d	DA0MS0	_	2	SPEC	_	_
2	proyecto	proyecto	n	NCMS000	_	3	SUBJ	_	_
3	está	estar	v	VAIP3S0	_	0	ROOT	_	_
4	liderado	liderar	v	VMP00SM	_	3	ATR	_	_
5	por	por	s	SPS00	_	4	BYAG	_	_
6	Theo_de_Raadt	theo_de_raadt	n	NP00000	_	5	COMP	_	_
7	,	,	f	Fc	_	6	punct	_	_
8	residente	residente	n	NCCS000	_	6	_	_	_
9	en	en	s	SPS00	_	8	MOD	_	_
10	Calgary	calgary	n	NP00000	_	9	COMP	_	_
11	.	.	f	Fp	_	10	punct	_	_

		 */
		String query_residente_en = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{?y <conll:cpostag> ?lemma_pos . "
				+ "?y <conll:cpostag> \"n\" . "
				+" ?y <conll:postag> \"NCCS000\" ."
				+ "?y <conll:deprel> ?lemma_grammar . "
				+ "?y <conll:form> ?lemma . "
				+ "?y <conll:head> ?e1 . "
				+ "?y <conll:deprel> \"_\" . "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "?e1 <conll:cpostag> \"n\" . "
				+ "?p <conll:head> ?y . "
				+ "?p <conll:deprel> \"MOD\" . "
				+ "?p <conll:postag> \"SPS00\" ."
				+ "?p <conll:form> ?prep . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:cpostag> \"n\" . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "FILTER regex(?e2_grammar, \"COMP\") ."
				+ "?e2 <conll:form> ?e2_form . "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		
		
		
		/*
		 * sentence:Actualmente Glebova vive en Tailandia y está casada con Paradorn Srichaphan . 
1	Actualmente	_	r	RG	_	3	MOD
2	Glebova	_	n	NP00000	_	3	SUBJ
3	vive	_	v	VMIP3S0	_	0	ROOT
4	en	_	s	SPS00	_	3	MOD
5	Tailandia	_	n	NP00000	_	4	COMP
Verb+prep

Neuer parse:
1	Actualmente	actualmente	r	RG	_	3	MOD	_	_
2	Glebova	glebova	n	NP00000	_	3	SUBJ	_	_
3	vive	vivir	v	VMIP3S0	_	0	ROOT	_	_
4	en	en	s	SPS00	_	3	MOD	_	_
5	Tailandia	tailandia	n	NP00000	_	4	COMP	_	_
6	y	y	c	CC	_	3	COORD	_	_
7	está	estar	v	VAIP3S0	_	6	CONJ	_	_
8	casada	casar	v	VMP00SF	_	7	ATR	_	_
9	con	con	s	SPS00	_	8	OBLC	_	_
10	Paradorn_Srichaphan	paradorn_srichaphan	n	NP00000	_	9	COMP	_	_
11	.	.	f	Fp	_	10	punct	_	_


		 */
		String query5 = "SELECT ?class ?lemma ?dobj_lemma ?advmod_lemma ?lemma_pos ?dobj_lemma ?lemma_grammar ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
				+ "{?y <conll:postag> ?lemma_pos . "
				//POSTAG nach VM prüfen Verbos principales (Hauptverb)
				+ "FILTER regex(?lemma_pos, \"VMIP\") ."
				+ "?y <conll:deprel> \"ROOT\" ."
				+ "?y <conll:form> ?lemma . "
				+ "?y <conll:deprel> ?lemma_grammar. "
				+ "?e1 <conll:head> ?y . "
				+ "?e1 <conll:form> ?e1_form . "
				+ "?e1 <conll:deprel> ?e1_grammar . "
				+ "?e1 <conll:deprel> ?deprel. "
				+ "?e1 <conll:postag> \"NP00000\". "
				+ "FILTER regex(?deprel, \"SUBJ\") ."
				+ "?p <conll:head> ?y . "
				+ "?p <conll:postag> \"SPS00\" . "
				+ "?p <conll:form> ?prep . "
				+ "?p <conll:deprel> \"MOD\" . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:deprel> ?e2_grammar . "
				+ "?e2 <conll:form> ?e2_form . "
				+ "?e2 <conll:deprel> ?deprel2. "
				+ "?e2 <conll:deprel> \"COMP\" . "
				+ "?e2 <conll:postag> \"NP00000\". "
				+ "?y <own:partOf> ?class. "
				+ "?class <own:subj> ?propSubj. "
				+ "?class <own:obj> ?propObj. "
				+ "}";
		
		
		List<String> tmp = new ArrayList<String>();
		tmp.add(query1);
		tmp.add("StateVerb");
		queries.add(tmp);
		//checked
		
		tmp = new ArrayList<String>();
		tmp.add(query2);
		tmp.add("Noun");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query_creadora);
		tmp.add("Noun");
		queries.add(tmp);
		
		
		tmp = new ArrayList<String>();
		tmp.add(query2a);
		tmp.add("Noun");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query2b);
		tmp.add("Noun");
		queries.add(tmp);
		//checked
		 
		
		tmp = new ArrayList<String>();
		tmp.add(query3);
		tmp.add("Adjective");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query4);
		tmp.add("Adjective");
		queries.add(tmp);
		//checked
		
		tmp = new ArrayList<String>();
		tmp.add(query_residente_en);
		tmp.add("Noun");
		queries.add(tmp);
		
		tmp = new ArrayList<String>();
		tmp.add(query5);
		tmp.add("StateVerb");
		queries.add(tmp);
		
		
	return queries;
}
}


