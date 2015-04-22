package de.citec.sc.matoll.patterns.english;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;
import de.citec.sc.matoll.process.Matoll;
import de.citec.sc.matoll.utils.Lemmatizer;

public class SparqlPattern_EN_3 extends SparqlPattern {


	Logger logger = LogManager.getLogger(SparqlPattern_EN_3.class.getName());

	
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

	
	
	String query = "SELECT ?lemma ?prefix ?prep ?e1_arg ?e2_arg  WHERE {"
			+ "{?y <conll:cpostag> \"NN\" . }"
			+ "UNION"
			+ "{?y <conll:cpostag> \"NNS\" . }"
			+ "?y <conll:form> ?lemma . "
			+"OPTIONAL{"
			+ "?modifier <conll:head> ?y. "
			+ "?modifier <conll:form> ?prefix. "
			+ "?modifier <conll:deprel> \"nn\"."
			+"} "
			+ "?e1 <conll:head> ?y . "
			+ "?e1 <conll:deprel> \"appos\"."
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"prep\" . "
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel> \"pobj\". "
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
	
	
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();
		
		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getNounWithPrep(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(), this.getDebugger());
	}


	public String getID() {
		return "SPARQLPattern_EN_3";
	}

}
