package de.citec.sc.matoll.patterns.german;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_DE_3 extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_3.class.getName());
	
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
		
		String query = "SELECT ?lemma ?prep ?e1_arg ?e2_arg WHERE{"
				+ "?e1 <conll:cpostag> \"N\" . "
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
				+ "?e1 <own:senseArg> ?e1_arg. "
				+ "?e2 <own:senseArg> ?e2_arg. "
				+ "}";
	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_3";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();
		
		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getNounWithPrep(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(), this.getDebugger());
		
	}

}
