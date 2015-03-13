package de.citec.sc.matoll.patterns.german;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;

public class SparqlPattern_DE_8 extends SparqlPattern{

	
	Logger logger = LogManager.getLogger(SparqlPattern_DE_8.class.getName());
	
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
			String query = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
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
			

			

	
	
	@Override
	public String getID() {
		return "SPARQLPattern_DE_8";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		// TODO Auto-generated method stub
		
	}

}
