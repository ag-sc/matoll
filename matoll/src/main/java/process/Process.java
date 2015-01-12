package process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import patterns.PatternLibrary;
import patterns.SparqlPattern_EN_6;
import preprocessor.ModelPreprocessor;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import core.LexiconWithFeatures;

public class Process {

	public static void main(String[] args) throws FileNotFoundException {
		
		List<String> properties = new ArrayList<String>();
		properties.add("http://dbpedia.ontology/spouse");
		
		File folder = new File("/Users/cimiano/test");
		
		List<Model> sentences;
		
		ModelPreprocessor preprocessor = new ModelPreprocessor();
		
		LexiconWithFeatures lexicon = new LexiconWithFeatures();
		
		PatternLibrary library = new PatternLibrary();
		
		library.addPattern(new SparqlPattern_EN_6());
		
		for (String property: properties)
		{
			sentences = new ArrayList<Model>();
					
			Statement stmt;
			
			String subj = null;
			String obj = null;
			
			
			for (final File file : folder.listFiles()) {
				
				if (file.isFile() && file.toString().endsWith(".ttl")) {	

				System.out.print("Loading: "+file.toString()+"\n");	
					
				 Model model = RDFDataMgr.loadModel(file.toString());	
	
				 obj = getObject(model);
				 
				 subj = getSubject(model);
				 		 
				preprocessor.preprocess(model,subj,obj);
				
				library.extractLexicalEntries(model, property, lexicon);
				
				FileOutputStream output = new FileOutputStream(new File(file.toString().replaceAll(".ttl", "_pci.ttl")));
				
				RDFDataMgr.write(output, model, RDFFormat.TURTLE) ;
				
				
				}
			}
		}
	}

	private static String getSubject(Model model) {
		
		StmtIterator iter = model.listStatements(null,model.getProperty("own:subj"), (RDFNode) null);
		
		Statement stmt;
		
		while (iter.hasNext()) {
						
			stmt = iter.next();
			
	        System.out.println("  " + stmt
	                                      .getObject()
	                                      .toString());
	        
	        return stmt.getObject().toString();
	    }
		
		return null;
	}

	private static String getObject(Model model) {
		StmtIterator iter = model.listStatements(null,model.getProperty("own:obj"), (RDFNode) null);
		
		Statement stmt;
		
		while (iter.hasNext()) {
						
			stmt = iter.next();
			
	        System.out.println("  " + stmt
	                                      .getObject()
	                                      .toString());
	        
	        return stmt.getObject().toString();
	    }
		
		return null;
	}
}
