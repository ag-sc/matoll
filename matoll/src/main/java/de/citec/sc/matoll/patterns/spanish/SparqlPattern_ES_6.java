package de.citec.sc.matoll.patterns.spanish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_ES_6 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_6.class.getName());
	
	
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
	String query= "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
			+ "?y <conll:cpostag> ?lemma_pos . "
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
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
			
	@Override
	public String getID() {
		return "SPARQLPattern_ES_6";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();

		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getAdjective(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(), this.getDebugger());
		
	}

}
