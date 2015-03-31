package de.citec.sc.matoll.patterns.english;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;
import de.citec.sc.matoll.process.Matoll;
import de.citec.sc.matoll.utils.Lemmatizer;

public class SparqlPattern_EN_8 extends SparqlPattern {

	Logger logger = LogManager.getLogger(SparqlPattern_EN_8.class.getName());

	
	// für Adjective mit JJ, drunter hängende Verb erst einmal ignorieren
	/*
	 * TODO: How to add lemma addition?
	 */
			String query = "SELECT ?lemma ?prep ?e1_arg ?e2_arg WHERE{"
					+ "?e1 <conll:deprel> ?e1_grammar . "
					+ "FILTER regex(?e1_grammar, \"subj\") ."
					+ "?e1 <conll:cpostag> ?e1_pos . "
					//+ "FILTER regex(?e1_pos, \"NN\") ."
					+ "?e1 <conll:head> ?y . "
					+ "?y <conll:cpostag> ?lemma_pos . "
					+ "FILTER regex(?lemma_pos,\"JJ\") . "
					+ "?y <conll:form> ?lemma . "
					+"OPTIONAL{"
					+ "?lemma_nn <conll:head> ?y. "
					+ "?lemma_nn <conll:form> ?lemma_addition. "
					+ "?lemma_nn <conll:deprel> \"nn\"."
					+"} "
					//+ "?verb <conll:head> ?y . "
					+ "?p <conll:head> ?y . "
					+ "?p <conll:deprel> \"prep\" . "
					+ "?p <conll:form> ?prep . "
					+ "?e2 <conll:head> ?p . "
					+ "?e2 <conll:deprel> ?e2_grammar . "
					+ "FILTER regex(?e2_grammar, \"obj\") ."
					+ "?e1 <own:senseArg> ?e1_arg. "
					+ "?e2 <own:senseArg> ?e2_arg. "
					+ "}";

	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();
		
		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getAdjective(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(), this.getDebugger());

	}

	public String getID() {
		return "SPARQLPattern_EN_8";
	}

}
