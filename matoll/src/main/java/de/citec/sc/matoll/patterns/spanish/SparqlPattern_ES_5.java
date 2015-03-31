package de.citec.sc.matoll.patterns.spanish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_ES_5 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_5.class.getName());
	
	
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
			String query= "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
					+ "?y <conll:cpostag> ?lemma_pos . "
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
					+ "?e1 <own:senseArg> ?e1_arg. "
					+ "?e2 <own:senseArg> ?e2_arg. "
					+ "}";
			
	@Override
	public String getID() {
		return "SPARQLPattern_ES_5";
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
