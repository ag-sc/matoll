package de.citec.sc.matoll.patterns;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.StmtIterator;

import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.utils.Lemmatizer;
import org.apache.jena.rdf.model.Statement;

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
         * Extracts plain sentences out of the RDF-Model. Each model contains only one sentence!
         * @param model Model, containing the parsed plain_sentence
         * @return List of plain sentences
         */
	protected Sentence returnSentence(Model model) {
            /* In the model is always only one plain_sentence*/
		
		String plain_sentence = "";
                String subjOfProp = "";
                String objOfProp = "";
		
		StmtIterator iter = model.listStatements(null,model.createProperty("conll:sentence"), (RDFNode) null);
		Statement stmt;
		stmt = iter.next();
	        plain_sentence = stmt.getObject().toString();
                
                iter = model.listStatements(null,model.createProperty("own:subj"), (RDFNode) null);
		stmt = iter.next();
	        subjOfProp = stmt.getObject().toString();
                
                iter = model.listStatements(null,model.createProperty("own:obj"), (RDFNode) null);
		stmt = iter.next();
	        objOfProp = stmt.getObject().toString();
                
                Sentence sentence = new Sentence(plain_sentence,subjOfProp,objOfProp);
                
                try{
                    iter = model.listStatements(null,model.createProperty("own:subjuri"), (RDFNode) null);
                    stmt = iter.next();
                    sentence.setSubjOfProp_uri(stmt.getObject().toString());
                }catch (Exception e){}
                
                
                try{
                    iter = model.listStatements(null,model.createProperty("own:objuri"), (RDFNode) null);
                    stmt = iter.next();
                    sentence.setObjOfProp_uri(stmt.getObject().toString());
                }catch (Exception e){}
	    
                return sentence;

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
