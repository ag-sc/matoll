package de.citec.sc.matoll.patterns.german;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_DE_5 extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_5.class.getName());
	
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
30	.	.	$.	$.	_	0	root	_	_ 	*/


	String query = "SELECT ?lemma ?e1_arg ?e2_arg WHERE{"
			+ "?e1 <conll:form> ?e1_form . "
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
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
	
	
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_5";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();
		
		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getNoun(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(), this.getDebugger());
		
		
	}

}
