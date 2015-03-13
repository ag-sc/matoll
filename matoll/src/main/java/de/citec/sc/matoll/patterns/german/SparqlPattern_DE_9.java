package de.citec.sc.matoll.patterns.german;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;

public class SparqlPattern_DE_9 extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_9.class.getName());
	
	
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
	String query = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
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
	
			

	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_9";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		// TODO Auto-generated method stub
		
	}

}
