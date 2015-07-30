package de.citec.sc.matoll.patterns;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.utils.Lemmatizer;

public abstract class SparqlPattern {
	
	protected Lemmatizer Lemmatizer;
        
	
	public abstract String getID();
        
        public abstract String getQuery();
	
	public abstract void extractLexicalEntries(Model model, Lexicon lexicon); 

	public void setLemmatizer(Lemmatizer lemmatizer) {
		
		Lemmatizer = lemmatizer;
		
	}
        
	
	public Lemmatizer getLemmatizer() {
		return Lemmatizer;
	}
	
        /**
         * Extracts plain sentences out of the RDF-Model
         * @param model Model, containing the parsed sentence
         * @return List of plain sentences
         */
	protected List<String> getSentences(Model model) {
		
		List<String> sentences = new ArrayList<String>();
		
		StmtIterator iter = model.listStatements(null,model.createProperty("conll:sentence"), (RDFNode) null);
		
		Statement stmt;
		
		while (iter.hasNext()) {
						
			stmt = iter.next();
			
	        sentences.add(stmt.getObject().toString());
	    }
		
		return sentences;

	}	
	
        /**
         * Returns for a given RDF-Model the reference encoded in the model.
         * @param model Model
         * @return Reference/URI
         */
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
