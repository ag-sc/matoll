package process;

import io.LexiconLoader;
import io.LexiconSerialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import patterns.PatternLibrary;
import patterns.SparqlPattern_EN_1;
import patterns.SparqlPattern_EN_2;
import patterns.SparqlPattern_EN_3;
import patterns.SparqlPattern_EN_4;
import patterns.SparqlPattern_EN_6;
import preprocessor.ModelPreprocessor;
import classifiers.FreqClassifier;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import core.Dataset;
import core.FeatureVector;
import core.LexicalEntry;
import core.Lexicon;
import core.LexiconWithFeatures;
import core.Provenance;
import evaluation.LexiconEvaluation;

public class Matoll {

	public static void main(String[] args) throws IOException {
			
		
		String directory;
		String mode;
		String gold_standard_lexicon;
		String model_file;
		String output_lexicon;
		boolean coreference = false;
		int no_entries = 1000;
		String output;
		
		Provenance provenance;
		
		Pattern p = Pattern.compile("^--(\\w+)=(\\w+|\\d+)$");
		  
		Matcher matcher;
		 
		if (args.length < 6)
		{
			System.out.print("Usage: Matoll --mode=train/test <DIRECTORY> <GOLD_STANDARD_LEXICON> <MODEL_FILE> <OUTPUT_LEXICON> <OUT> --coreference=true/false --no_entries=n\n");
			return;
		
		}
		
		directory = args[1];
		gold_standard_lexicon = args[2];
		model_file = args[3];
		output_lexicon = args[4];
		output = args[5];
				
	

		for (int i=0; i < args.length; i++)
		{
			matcher = p.matcher(args[i]);
				 
			if (matcher.matches())
			{
			    if (i== 0 && matcher.group(1).equals("mode"))
			    {
			    	mode = matcher.group(2);
			    	System.out.print("Starting MATOLL with mode: "+mode+"\n");
			    	System.out.print("Processing directory: "+directory+"\n");
			    	System.out.print("Using gold standard: "+gold_standard_lexicon+"\n");
			    	System.out.print("Using model file: "+model_file+"\n");
			    	System.out.print("Output lexicon: "+output_lexicon+"\n");
			    }
			    else
			    {
			    	System.out.print("Usage: Matoll --mode=train/test <DIRECTORY> <GOLD_STANDARD_LEXICON> <MODEL_FILE> <OUTPUT_LEXICON> <OUT> --coreference=true/false --no_entries=n\n");
			    	return;
			    }
			    if (matcher.group(1).equals("coreference"))
			    {
			    	if (matcher.group(2).equals("true"))
			    	{
			    		coreference = true;
			    		System.out.print("Using coreference!!!\n");
			    	}
			    }
			    if (matcher.group(1).equals("no_entries"))
			    {
			    	no_entries = (new Integer(matcher.group(2))).intValue();
			    	System.out.print("No. entries"+no_entries+"\n");
			    }
			}		
		}
		
		LexiconLoader loader = new LexiconLoader();
		
		Lexicon gold = loader.loadFromFile(gold_standard_lexicon);
		
		FreqClassifier classifier = new FreqClassifier("freq",1.0);
		
		File folder = new File(directory);
		
		Lexicon lexicon;
		
		// Creating preprocessor and setting coreference
		
		ModelPreprocessor preprocessor = new ModelPreprocessor();
		
		preprocessor.setCoreferenceResolution(true);
		
		LexiconWithFeatures lexiconwithFeatures = new LexiconWithFeatures();
		
		// Creating library and pattern
		
		PatternLibrary library = new PatternLibrary();
		
		library.addPattern(new SparqlPattern_EN_6());
				
		
		String subj = null;
		String obj = null;
		
		String reference = null;
		
		List<Model> sentences;
		
		for (final File file : folder.listFiles()) {
			
			if (file.isFile() && file.toString().endsWith(".ttl")) {	

			System.out.print("Processing: "+file.toString()+"\n");	
				
			 Model model = RDFDataMgr.loadModel(file.toString());
			 
			 sentences = getSentences(model);
			 
			 for (Model sentence: sentences)
			 {

				 obj = getObject(sentence);
			 
				 subj = getSubject(sentence);
			 
				 reference = getReference(sentence);
			
				 preprocessor.preprocess(sentence,subj,obj);
			
				 library.extractLexicalEntries(sentence, lexiconwithFeatures);
			 }
			
			 // FileOutputStream output = new FileOutputStream(new File(file.toString().replaceAll(".ttl", "_pci.ttl")));
			
			 // RDFDataMgr.write(output, model, RDFFormat.TURTLE) ;
			
			}
		}
		
		
		LexiconSerialization serializer = new LexiconSerialization();
		
		List<LexicalEntry> entries = new ArrayList<LexicalEntry>();
		
		FeatureVector vector;
		
		// Training
		
		Dataset trainingSet = new Dataset();
		
		
		// process features
		
		for (LexicalEntry entry: lexiconwithFeatures.getEntries())
		{
			// System.out.println(entry);
			
			vector = lexiconwithFeatures.getFeatureVector(entry);
			
			// preprocessing vector
			
			if (gold.contains(entry))
			{
				trainingSet.addInstance(vector, 1);
			}
			else
			{
				trainingSet.addInstance(vector, 0);
			}
			
		}
		
		classifier.train(trainingSet);
		
		
		for (LexicalEntry entry: lexiconwithFeatures.getEntries())
		{
			// System.out.println(entry);
			
			vector = lexiconwithFeatures.getFeatureVector(entry);
			
			if (classifier.predict(vector)==1)
			{
				provenance = new Provenance();
				
				provenance.setConfidence(classifier.predict(vector, 1));
				
				provenance.setAgent("http://sc.citec.uni-bielefeld.de/matoll");

				provenance.setEndedAtTime(new Date());
				
				entry.setProvenance(provenance);
							
				entries.add(entry);
			}
			
		}
		
		
		
		Collections.sort(entries, new Comparator<LexicalEntry>() {
			 
			            public int compare(LexicalEntry one, LexicalEntry two) {
				                return (((LexicalEntry) one).getProvenance().getConfidence() > ((LexicalEntry) two).getProvenance().getConfidence()) ? 1 : -1;
				            }
				             
				        });
		
		
		lexicon = new Lexicon();
		
		LexiconEvaluation eval = new LexiconEvaluation();
		
		FileWriter writer = new FileWriter(output);
		
		for (int i=0; i < entries.size(); i++)
		{
			lexicon.addEntry(entries.get(i));
			
			eval.setReferences(lexicon.getReferences());
			
			eval.evaluate(lexicon,gold);
			
			writer.write(i+"\t"+eval.getPrecision("lemma")+"\t"+eval.getRecall("lemma")+"\t"+eval.getFMeasure("lemma")+"\t"+eval.getPrecision("syntax")+"\t"+eval.getRecall("syntax")+"\t"+eval.getFMeasure("syntax")+"\t"+eval.getPrecision("mapping")+"\t"+eval.getRecall("mapping")+"\t"+eval.getFMeasure("mapping"));
			writer.flush();
		
			
		}
		
		writer.close();
		
		Set<String> references = lexicon.getReferences();
	
		
		
		for (String ref: references)
		{
			writer = new FileWriter(ref+".lex");
			entries = lexicon.getEntriesForReference(ref);
			
			for (LexicalEntry entry: entries)
			{
				writer.write(entry.toString()+"\n");
				writer.flush();
			}
			
			writer.close();
			
			
			
		}
	
		Model model = ModelFactory.createDefaultModel();
		
		serializer.serialize(lexiconwithFeatures, model);
		
		FileOutputStream out = new FileOutputStream(new File(output_lexicon));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
		
		System.out.print("Lexicon: "+output.toString()+" written out\n");
		
		
		// re-engineer the lexicon evaluation to store evaluation measures as a hashmap
		// extend the pattern matching to get the actual reference
		
	
			
	}

	private static String getReference(Model model) {
		StmtIterator iter = model.listStatements(null,model.getProperty("own:reference"), (RDFNode) null);
		
		Statement stmt;
		
		while (iter.hasNext()) {
						
			stmt = iter.next();
			
	        return stmt.getObject().toString();
	    }
		
		return null;
	}

	private static List<Model> getSentences(Model model) throws FileNotFoundException {
		
		// get all ?res <conll:sentence> 
		
		List<Model> sentences = new ArrayList<Model>();
		
		StmtIterator iter, iter2, iter3;
		
		Statement stmt, stmt2, stmt3;
		
		Resource resource;
		
		Resource token;
		
		iter = model.listStatements(null,model.getProperty("conll:language"), (RDFNode) null);
		
		while (iter.hasNext()) {
					
			Model sentence = ModelFactory.createDefaultModel();
			
			stmt = iter.next();
			
			resource = stmt.getSubject();
			
			iter2 = model.listStatements(resource , null, (RDFNode) null);
			
			while (iter2.hasNext())
			{
				stmt2 = iter2.next();
				
				sentence.add(stmt2);
				
			}
			
			iter2 = model.listStatements(null , model.getProperty("own:partOf"), (RDFNode) resource);
			
			while (iter2.hasNext())
			{
				stmt2 = iter2.next();
				
				token = stmt2.getSubject();
				
				iter3 = model.listStatements(token , null, (RDFNode) null);
				
				while (iter3.hasNext())
				{
					stmt3 = iter3.next();
					
					sentence.add(stmt3);
					
				}
			}
			
			sentences.add(sentence);
			
			RDFDataMgr.write(new FileOutputStream(new File(resource+".ttl")), sentence, RDFFormat.TURTLE) ;
			
		}
		

		return sentences;
		
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
