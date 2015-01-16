package patterns;

import java.util.ArrayList;
import java.util.List;

import utils.Lemmatizer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import core.LexiconWithFeatures;

public abstract class SparqlPattern {
	
	Lemmatizer Lemmatizer;
	
	public abstract String getID();
	
	public abstract void extractLexicalEntries(Model model,String reference, LexiconWithFeatures lexicon); 

	public void setLemmatizer(Lemmatizer lemmatizer) {
		
		Lemmatizer = lemmatizer;
		
	}
	
	protected static List<String> getSentences(Model model) {
		
		List<String> sentences = new ArrayList<String>();
		
		StmtIterator iter = model.listStatements(null,model.createProperty("conll:sentence"), (RDFNode) null);
		
		Statement stmt;
		
		while (iter.hasNext()) {
						
			stmt = iter.next();
			
	        sentences.add(stmt.getObject().toString());
	        System.out.print(stmt.getObject().toString()+"\n");
	    }
		
		return sentences;

		
	}	
	
}
