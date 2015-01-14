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
import serialization.LexiconSerialization;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import core.Lexicon;
import core.LexiconWithFeatures;

public class Process {

	public static void main(String[] args) throws FileNotFoundException {
			
		List<String> properties = new ArrayList<String>();
		properties.add("http://dbpedia.ontology/spouse");
		
		File folder = new File("/Users/swalter/Desktop/sentences");
		
		Lexicon lexicon;
		
		List<Model> models;
		
		ModelPreprocessor preprocessor = new ModelPreprocessor();
		
		LexiconWithFeatures lexiconwithFeatures = new LexiconWithFeatures();
		
		PatternLibrary library = new PatternLibrary();
		
		library.addPattern(new SparqlPattern_EN_6());
		
		for (String property: properties)
		{
			models = new ArrayList<Model>();
					
			Statement stmt;
			
			String subj = null;
			String obj = null;
			
			List<String> sentences;
			
			
			for (final File file : folder.listFiles()) {
				
				if (file.isFile() && file.toString().endsWith(".ttl")) {	

				System.out.print("Loading: "+file.toString()+"\n");	
					
				 Model model = RDFDataMgr.loadModel(file.toString());	
	
				 obj = getObject(model);
				 
				 subj = getSubject(model);
				 
			 		 
				 preprocessor.preprocess(model,subj,obj);
				
				 library.extractLexicalEntries(model, property, lexiconwithFeatures);
				
				 FileOutputStream output = new FileOutputStream(new File(file.toString().replaceAll(".ttl", "_pci.ttl")));
				
				 RDFDataMgr.write(output, model, RDFFormat.TURTLE) ;
				
				
				}
			}
		}
		
		LexiconSerialization serializer = new LexiconSerialization();
		
		// lexicon = lexiconwithFeatures.filterFeatureGreater("freq",1.0);
		
		Model model = ModelFactory.createDefaultModel();
		
		serializer.serialize(lexiconwithFeatures, model);
		
		FileOutputStream output = new FileOutputStream(new File("lexicon.ttl"));
		
		RDFDataMgr.write(output, model, RDFFormat.TURTLE) ;
		
		System.out.print("Lexicon: "+output.toString()+" written out\n");
		
		System.out.print(lexiconwithFeatures.toString());
		
	}


	private static String getSubject(Model model) {
		
		StmtIterator iter = model.listStatements(null,model.getProperty("own:subj"), (RDFNode) null);
		
		Statement stmt;
		
		while (iter.hasNext()) {
						
			stmt = iter.next();
			
	        return stmt.getObject().toString();
	    }
		
		return null;
	}

	private static String getObject(Model model) {
		StmtIterator iter = model.listStatements(null,model.getProperty("own:obj"), (RDFNode) null);
		
		Statement stmt;
		
		while (iter.hasNext()) {
						
			stmt = iter.next();
	        
	        return stmt.getObject().toString();
	    }
		
		return null;
	}
}
