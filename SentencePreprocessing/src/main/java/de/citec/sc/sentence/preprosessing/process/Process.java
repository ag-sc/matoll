package de.citec.sc.sentence.preprosessing.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import de.citec.sc.sentence.preprosessing.lucene.IndexReader;

public class Process {

	public static void main(String[] args) throws IOException {
		/*
		 * examples for properties can be found under /src/test/resources/
		 */
		if (args.length == 0) {
			System.out.println("Run: java Process ontology/pathToPropertyFile");
			System.exit(1);
		}
		String endpoint = "http://dbpedia.org/sparql";
		Boolean with_sentences = false;
		/*
		 *in pathToIndex only one index for one language can be found 
		 */
		String pathToIndex = "/Users/swalter/Dropbox/Diss/Index/EnglishIndex";
		String folderToSaveResourcesSentences = "/Users/swalter/Desktop/Resources";
		String language = "en";
		
		IndexReader index = new IndexReader(pathToIndex);
		List<List<String>> properties = new ArrayList<List<String>>();
		try {
			if(args[0].endsWith(".owl")){
				loadOntology(args[0],properties,language);
			}
			else{
				loadPropertyList(args[0],properties,language);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!folderToSaveResourcesSentences.endsWith("/")) folderToSaveResourcesSentences+="/";
		System.out.println("Retrieve Entities");
		de.citec.sc.sentence.preprosessing.sparql.Resources.retrieveEntities(properties, folderToSaveResourcesSentences, endpoint);
		System.out.println("Done");
		System.out.println();
		
		
		if(with_sentences){
			String pathWriteSentences = folderToSaveResourcesSentences+"Sentences/";
			if(checkFolder(pathWriteSentences)){
				for (List<String> property: properties){
					try {
						String pathToSentenceModel = pathWriteSentences+property.get(1)+"/"+property.get(2)+"/"+property.get(3)+"/"+property.get(4);
						boolean check_ontologyfolder = checkFolder(pathWriteSentences+property.get(1));
						boolean check_namespacefolder = checkFolder(pathWriteSentences+property.get(1)+"/"+property.get(2));
						boolean check_languagefolder = checkFolder(pathWriteSentences+property.get(1)+"/"+property.get(2)+"/"+property.get(3));
						boolean check_modelfolder = checkFolder(pathToSentenceModel);
						if(check_ontologyfolder&&check_languagefolder&&check_namespacefolder&&check_modelfolder){
							System.out.println("Processing:"+property.get(0));
							List<List<String>> entities = de.citec.sc.sentence.preprosessing.sparql.Resources.loadEntities(property, folderToSaveResourcesSentences);
							List<List<String>> sentences = index.search(entities);
							int value = 10000;
							if(sentences.size()<=value){
								de.citec.sc.sentence.preprosessing.rdf.RDF.writeModel(sentences, pathToSentenceModel, language, property.get(0));
							}
							else{
								int begin = 0;
								int end = 0;
								for(int i= 0; i<Math.floor((double)sentences.size()/value);i++){
									begin = i*value;
									end = begin+value;
									de.citec.sc.sentence.preprosessing.rdf.RDF.writeModel(sentences.subList(begin, end), pathToSentenceModel, language, property.get(0));
								}
								de.citec.sc.sentence.preprosessing.rdf.RDF.writeModel(sentences.subList(end, sentences.size()), pathToSentenceModel, language, property.get(0));
							}
							
							System.out.println("Done");
							System.out.println();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		

	}

	private static void loadPropertyList(String pathToProperties,
			List<List<String>> properties, String language) throws IOException {
		String properties_raw = "";
		/*
		 * each line contains one property
		 */
		FileInputStream inputStream = new FileInputStream(pathToProperties);
	    try {
	        properties_raw = IOUtils.toString(inputStream);
	    } finally {
	        inputStream.close();
	    }
	    
	    for(String p: properties_raw.split("\n")){
	    	String ontologyName =findOntologyName(p);
	    	String[] tmp = p.split("/");
	    	String name = tmp[tmp.length-1];
	    	String namespace = tmp[tmp.length-2];
	    	List<String> property = new ArrayList<String>();
	    	property.add(p);
	    	property.add(ontologyName);
	    	property.add(namespace);
	    	property.add(language);
	    	property.add(name);
	    	properties.add(property);
	    	//System.out.println(property.toString());
	    	
	    }
		
	}
	
	private static void loadOntology(String pathToOntology,
			List<List<String>> properties, String language) throws IOException {
		OntologyImporter importer = new OntologyImporter(pathToOntology,"RDF/XML");
	    
	    for(String p: importer.getProperties()){
	    	String ontologyName =findOntologyName(p);
	    	String[] tmp = p.split("/");
	    	String name = tmp[tmp.length-1];
	    	String namespace = tmp[tmp.length-2];
	    	List<String> property = new ArrayList<String>();
	    	property.add(p);
	    	property.add(ontologyName);
	    	property.add(namespace);
	    	property.add(language);
	    	property.add(name);
	    	properties.add(property);
	    	//System.out.println(property.toString());
	    	
	    }
		
	}

	private static String findOntologyName(String p) {
		 // String to be scanned to find the pattern.
	      String pattern ="^http://(\\w*).*\\W.*";
	      String ontologyName = "";
	      // Create a Pattern object
	      Pattern r = Pattern.compile(pattern);

	      // Now create matcher object.
	      Matcher m = r.matcher(p);
	      if (m.find( )) {
	         ontologyName = m.group(1);
	      } else {
	         System.out.println("NO MATCH");
	      }
		return ontologyName;
	}
	
	private static boolean checkFolder(String path) {
		File f = new File(path);
		if(f.exists()){
			return true;
		}
		else{
			File newDir = new File(path);
			try{
				newDir.mkdir();
				return true;
			}
			catch(Exception e){
				System.err.println("Could not create "+path);
				return false;
			}
		}
	}

}
