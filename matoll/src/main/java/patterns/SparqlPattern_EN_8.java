package patterns;

import com.hp.hpl.jena.rdf.model.Model;

import core.LexiconWithFeatures;

public class SparqlPattern_EN_8 implements SparqlPattern {

	// für Adjective mit JJ, drunter hängende Verb erst einmal ignorieren
			String query = "SELECT ?class ?lemma_pos ?dobj_lemma ?lemma_grammar ?advmod_lemma ?lemma ?e1 ?e2 ?e1_form ?e2_form ?e1_grammar ?e2_grammar ?prep ?propSubj ?propObj ?lemma_addition WHERE"
					+ "{ "
					+ "?e1 <conll:form> ?e1_form . "
					+ "?e1 <conll:deprel> ?e1_grammar . "
					+ "FILTER regex(?e1_grammar, \"subj\") ."
					+ "?e1 <conll:cpostag> ?e1_pos . "
					//+ "FILTER regex(?e1_pos, \"NN\") ."
					+ "?e1 <conll:head> ?y . "
					+ "?y <conll:cpostag> ?lemma_pos . "
					+ "FILTER regex(?lemma_pos,\"JJ\") . "
					+ "?y <conll:deprel> ?lemma_grammar . "
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
					//+ "?o <is> ?e2 ."
					+ "?e2 <conll:form> ?e2_form . "
					+ "?y <own:partOf> ?class. "
					+ "?class <own:subj> ?propSubj. "
					+ "?class <own:obj> ?propObj. "
					+ "}";

	public void extractLexicalEntries(Model model, String reference,  LexiconWithFeatures lexicon) {
		// TODO Auto-generated method stub

	}

	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
