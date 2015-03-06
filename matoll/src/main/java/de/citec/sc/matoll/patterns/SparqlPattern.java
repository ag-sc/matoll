package de.citec.sc.matoll.patterns;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.utils.Lemmatizer;

public abstract class SparqlPattern {
	
	protected Lemmatizer Lemmatizer;
	
	public abstract String getID();
	
	public abstract void extractLexicalEntries(Model model, LexiconWithFeatures lexicon); 

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
	        // System.out.print(stmt.getObject().toString()+"\n");
	    }
		
		return sentences;

	}	
	
	protected String getReference(Model model) {
		
		StmtIterator iter = model.listStatements(null,model.createProperty("conll:reference"), (RDFNode) null);
		
		Statement stmt;
		
		while (iter.hasNext()) {
						
			stmt = iter.next();
			
	        return stmt.getObject().toString();

	    }
		
		return null;

	}	
	
}
