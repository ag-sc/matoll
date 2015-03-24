package de.citec.sc.matoll.patterns.english;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

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

public class SparqlPattern_EN_1 extends SparqlPattern {

	Logger logger = LogManager.getLogger(SparqlPattern_EN_1.class.getName());
	
	String query = "SELECT ?lemma ?prep ?dobj_form ?e1_arg ?e2_arg  WHERE {"
			+ "{?y <conll:cpostag> \"VB\" .}"
			+ "UNION"
			+ "{?y <conll:cpostag> \"VBD\" .}"
			+ "UNION"
			+ "{?y <conll:cpostag> \"VBP\" .}"
			+ "UNION"
			+ "{?y <conll:cpostag> \"VBZ\" .}"
			+ "?y <conll:form> ?lemma . "
			+ "?e1 <conll:head> ?y . "
			+ "?e1 <conll:deprel> ?deprel. "
			+ "FILTER regex(?deprel, \"subj\") ."
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"prep\" . "
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			+ "?e2 <conll:deprel> \"pobj\". "
			+"OPTIONAL{"
			+ "?e2 <conll:head> ?y. "
			+ "?e2 <conll:form> ?dobj_form. "
			+ "?e2 <conll:deprel> \"dobj\"."
			+"} "
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
	
	/*
entity1_form:jobs
entity2_form:inc.
propSubject:steve jobs
propObject:apple inc
--------------
entity1_grammar:nsubj
entity2_grammar:pobj
--------------
lemma:attempted
lemma_grammar:null
lemma_pos:vbd
depending dobj (if exists):coups
--------------
query_name:query1
intended_lexical_type:StateVerb
entry:StateVerb("attempt",<http://dbpedia.org/ontology/board>, propSubj = PrepositionalObject("at"), propObj = Subject)
--------------
sentence:Steve Jobs attempted management coups twice at Apple Inc. ; first in 1985 when he unsuccessfully tried to oust John Sculley and then again in 1997 which successfully forced Gil Amelio to resign . 
1       Steve   _       NNP     NNP     _       2       nn
2       Jobs    _       NNP     NNP     _       3       nsubj
3       attempted       _       VBD     VBD     _       0       null
4       management      _       NN      NN      _       5       nn
5       coups   _       NNS     NNS     _       3       dobj
6       twice   _       RB      RB      _       3       advmod
7       at      _       IN      IN      _       3       prep
8       Apple   _       NNP     NNP     _       9       nn
9       Inc.    _       NNP     NNP     _       7       pobj
10      ;       _       :       :       _       3       punct
11      first   _       RB      RB      _       3       conj
12      in      _       IN      IN      _       11      dep
13      1985    _       CD      CD      _       12      pobj
14      when    _       WRB     WRB     _       17      advmod
15      he      _       PRP     PRP     _       17      nsubj
16      unsuccessfully  _       RB      RB      _       17      advmod
17      tried   _       VBD     VBD     _       11      dep
18      to      _       TO      TO      _       19      aux
19      oust    _       VB      VB      _       17      xcomp
20      John    _       NNP     NNP     _       21      nn
21      Sculley _       NNP     NNP     _       19      dobj
22      and     _       CC      CC      _       17      cc
23      then    _       RB      RB      _       24      advmod
24      again   _       RB      RB      _       17      conj
25      in      _       IN      IN      _       24      dep
26      1997    _       CD      CD      _       25      pobj
27      which   _       WDT     WDT     _       29      nsubj
28      successfully    _       RB      RB      _       29      advmod
29      forced  _       VBD     VBD     _       26      rcmod
30      Gil     _       NNP     NNP     _       31      nn
31      Amelio  _       NNP     NNP     _       33      nsubj
32      to      _       TO      TO      _       33      aux
33      resign  _       VB      VB      _       29      xcomp
34      .       _       .       .       _       3       punct

*/
	
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		
		FeatureVector vector = new FeatureVector();
		
		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getIntransitiveVerb(model, lexicon, vector, sentences, query, this.getReference(model), logger, Lemmatizer);
		
	}

	public String getID() {
		return "SPARQLPattern_EN_1";
	}

}
