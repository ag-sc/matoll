package de.citec.sc.matoll.patterns.english;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;
import de.citec.sc.matoll.process.Matoll;
import de.citec.sc.matoll.utils.Lemmatizer;

public class SparqlPattern_EN_6 extends SparqlPattern {

	Logger logger = LogManager.getLogger(SparqlPattern_EN_6.class.getName());
	
	
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
	String query = "SELECT ?lemma ?e1_arg ?e2_arg WHERE"
			+ "{ "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			// pci: eigentlich nur subj und nsubj denke ich
			+ "FILTER regex(?e1_grammar, \"subj\") ."
			+ "?e1 <conll:cpostag> ?e1_pos . "
			+ "?e1 <conll:head> ?y . "
			+ "?y <conll:cpostag> ?lemma_pos . "
			// pci: Warum nicht nur VBD und VBZ?
			+ "FILTER regex(?lemma_pos, \"VB\") ."
			+ "?y <conll:deprel> ?lemma_grammar . "
			+ "?y <conll:form> ?lemma . "
			+ "?e2 <conll:head> ?y . "
			+ "?e2 <conll:deprel>  \"dobj\" . "
			+ "?e2 <conll:form> ?e2_form . "
			+ "?y <own:partOf> ?class. "
			+ "?e1 <own:senseArg> ?e1_arg . "
			+ "?e2 <own:senseArg> ?e2_arg . "
			+ "}";
	
	

	
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		
		FeatureVector vector = new FeatureVector();
		
		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getTransitiveVerb(model, lexicon, vector, sentences, query, this.getReference(model), logger, Lemmatizer);

	}
	
	


	public String getID() {
		return "SPARQLPattern_EN_6";
	}

}
