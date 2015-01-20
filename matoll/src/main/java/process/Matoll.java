package process;

import io.LexiconLoader;
import io.LexiconSerialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import patterns.PatternLibrary;
import patterns.SparqlPattern_EN_1;
import patterns.SparqlPattern_EN_2;
import patterns.SparqlPattern_EN_3;
import patterns.SparqlPattern_EN_4;
import patterns.SparqlPattern_EN_6;
import preprocessor.ModelPreprocessor;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import core.FeatureVector;
import core.LexicalEntry;
import core.Lexicon;
import core.LexiconWithFeatures;
import evaluation.LexiconEvaluation;

public class Matoll {

	public static void main(String[] args) throws IOException {
			
		
		if (args.length < 5)
		{
			System.out.print("Usage: Matoll --mode=train/test <DIRECTORY> <GOLD_STANDARD_LEXICON> <MODEL_FILE> <OUTPUT_LEXICON> --coreference=true/false --no_entries=n\n");
			
			return;
		}
		
		File folder = new File(args[1]);
		
		Lexicon lexicon;
		
		// Creating preprocessor and setting coreference
		
		ModelPreprocessor preprocessor = new ModelPreprocessor();
		
		preprocessor.setCoreferenceResolution(true);
		
		LexiconWithFeatures lexiconwithFeatures = new LexiconWithFeatures();
		
		// Creating library and pattern
		
		PatternLibrary library = new PatternLibrary();
		
		library.addPattern(new SparqlPattern_EN_1());
				
		
		String subj = null;
		String obj = null;

		
		for (final File file : folder.listFiles()) {
			
			if (file.isFile() && file.toString().endsWith(".ttl")) {	

			System.out.print("Processing: "+file.toString()+"\n");	
				
			 Model model = RDFDataMgr.loadModel(file.toString());	

			 obj = getObject(model);
			 
			 subj = getSubject(model);
			 
			 preprocessor.preprocess(model,subj,obj);
			
			 library.extractLexicalEntries(model, lexiconwithFeatures);
			
			 // FileOutputStream output = new FileOutputStream(new File(file.toString().replaceAll(".ttl", "_pci.ttl")));
			
			 // RDFDataMgr.write(output, model, RDFFormat.TURTLE) ;
			
			
			}
		}
		
		lexicon = new Lexicon();
		
		LexiconSerialization serializer = new LexiconSerialization();
		
		FeatureVector vector;
		
		for (LexicalEntry entry: lexiconwithFeatures.getEntries())
		{
			System.out.println(entry);
			
			vector = lexiconwithFeatures.getFeatureVector(entry);
			
			System.out.print("Value of feature freq is:" + vector.getValueOfFeature("freq")+"\n");
			
			if (vector.getValueOfFeature("freq") > 1.0)
			{
				lexicon.addEntry(entry);
			}
			
		}
		
		LexiconLoader loader = new LexiconLoader();
		
		Lexicon gold = loader.loadFromFile("/Users/cimiano/Projects/matoll/dbpedia_en.rdf");
		
		LexiconEvaluation eval = new LexiconEvaluation();
		
		// filter out all enries with frequency <= 1 and evaluate them!
		
		Model model = ModelFactory.createDefaultModel();
		
		serializer.serialize(lexiconwithFeatures, model);
		
		FileOutputStream output = new FileOutputStream(new File("lexicon.ttl"));
		
		RDFDataMgr.write(output, model, RDFFormat.TURTLE) ;
		
		System.out.print("Lexicon: "+output.toString()+" written out\n");
		
		// if train
		
		// apply classifier to every example
		// order examples by score
		// add lexical entries one to one to the lexicon
		// evaluate quality of the lexicon
		
		// re-engineer the lexicon evaluation to store evaluation measures as a hashmap
		// extend the pattern matching to get the actual reference
		
		// if test
		
		FileWriter writer = new FileWriter(new File("gold_lex"));
		
		writer.write(gold.toString());
		
		writer.close();
		
		writer = new FileWriter(new File("induced_lex"));
		
		writer.write(lexicon.toString());
		
		writer.close();
		
		eval.setReferences(lexicon.getReferences());
		
		eval.evaluate(lexicon,gold);
		
		
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
