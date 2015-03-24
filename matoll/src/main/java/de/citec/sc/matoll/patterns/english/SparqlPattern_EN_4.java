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

public class SparqlPattern_EN_4 extends SparqlPattern {

	/*################################
entity1_form:shriram
entity2_form:google
propSubject:ram shriram
propObject:google
--------------
entity1_grammar:nsubj
entity2_grammar:pobj
--------------
lemma:member
lemma_grammar:null
lemma_pos:nn
depending dobj:
depending advmod:
--------------
query_name:query5
intended_lexical_type:Noun
entry:RelationalNoun("board member",<http://dbpedia.org/ontology/board>, propSubj = PrepositionalObject("of"), propObj = CopulativeArg)
--------------
sentence:Kavitark Ram Shriram is a board member of Google and one of the first investors in Google . 
1	Kavitark	_	NNP	NNP	_	3	nn
2	Ram	_	NNP	NNP	_	3	nn
3	Shriram	_	NNP	NNP	_	7	nsubj
4	is	_	VBZ	VBZ	_	7	cop
5	a	_	DT	DT	_	7	det
6	board	_	NN	NN	_	7	nn
7	member	_	NN	NN	_	0	null
8	of	_	IN	IN	_	7	prep
9	Google	_	NNP	NNP	_	8	pobj
=>works only in fuzzy mode.

PropSubj:Oxford Brookes University
PropObj:Janet Beer
sentence:Professor Janet Beer is the Vice-Chancellor of Oxford Brookes University . 
1	Professor	_	NNP	NNP	_	3	nn
2	Janet	_	NNP	NNP	_	3	nn
3	Beer	_	NNP	NNP	_	6	nsubj
4	is	_	VBZ	VBZ	_	6	cop
5	the	_	DT	DT	_	6	det
6	Vice-Chancellor	_	NNP	NNP	_	0	null
7	of	_	IN	IN	_	6	prep
8	Oxford	_	NNP	NNP	_	10	nn
9	Brookes	_	NNP	NNP	_	10	nn
10	University	_	NNP	NNP	_	7	pobj
11	.	_	.	.	_	6	punct
----------------------


*/
	
		Logger logger = LogManager.getLogger(SparqlPattern_EN_4.class.getName());

		String query = "SELECT ?lemma ?prefix ?e1_arg ?e2_arg ?prep WHERE"
				+"{ "
				+"{ ?y <conll:cpostag> \"NN\" . } "
				+ " UNION "
				+"{ ?y <conll:cpostag> \"NNS\" . } "
				+ " UNION "
				+"{ ?y <conll:cpostag> \"NNP\" . } "
				+ "?y <conll:form> ?lemma . "
				+"OPTIONAL{"
				+ "?lemma_nn <conll:head> ?y. "
				+ "?lemma_nn <conll:form> ?prefix. "
				+ "?lemma_nn <conll:deprel> \"nn\"."
				+"} "
				+ "?verb <conll:head> ?y . "
				+ "?verb <conll:deprel> \"cop\" ."
				+ "?e1 <conll:head> ?y . "
				+ "?e1 <conll:deprel> \"nsubj\" . "
				+ "?p <conll:head> ?y . "
				+ "?p <conll:deprel> \"prep\" . "
				+ "?p <conll:form> ?prep . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:deprel> \"pobj\" . "
				+ "?e1 <own:senseArg> ?e1_arg. "
				+ "?e2 <own:senseArg> ?e2_arg. "
				+ "}";
		
		

	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();
		
		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getNounWithPrep(model, lexicon, vector, sentences, query, this.getReference(model), logger, Lemmatizer);
		
	     
	}

	public String getID() {
		return "SPARQLPattern_EN_4";
	}

}
