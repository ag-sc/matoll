package de.citec.sc.sentence.preprocessing.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import de.citec.sc.sentence.preprocessing.lucene.ReadIndex;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Process {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, InstantiationException, IllegalAccessException, ClassNotFoundException, Exception {
		/*
		 * examples for properties can be found under /src/test/resources/
		 */
		if (args.length == 0) {
			System.out.println("Run: java Process config.xml");
			System.exit(1);
		}
                
                String configFile = args[0];
                Config config = new Config();
		config.loadFromFile(configFile);
                
                
		String endpoint = config.getEndpoint();
		Boolean with_sentences = config.isWithSentences();
                boolean additionalOutput = config.isAdditionalOutput();
		/*
		 *in pathToIndex only one index for one language can be found 
		 */
		String pathToIndex = config.getIndex();
		String folderToSaveResourcesSentences = config.getOutput();
		Language language = config.getLanguage();
                		
		
		List<List<String>> properties = new ArrayList<List<String>>();
		try {
			if(config.getInput().endsWith(".owl")){
				loadOntology(config.getInput(),properties,language);
			}
			else{
				loadPropertyList(config.getInput(),properties,language);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!folderToSaveResourcesSentences.endsWith("/")) folderToSaveResourcesSentences+="/";
		System.out.println("Retrieve Entities");
		de.citec.sc.sentence.preprocessing.sparql.Resources.retrieveEntities(properties, folderToSaveResourcesSentences, endpoint, language);
		System.out.println("Done");
		System.out.println();
		
		
		if(with_sentences){
                    ReadIndex index = new ReadIndex(pathToIndex,language);
                    String pathWriteSentences = folderToSaveResourcesSentences+"Sentences_"+language+"/";
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
                                                    List<List<String>> entities = de.citec.sc.sentence.preprocessing.sparql.Resources.loadEntities(property, folderToSaveResourcesSentences);
                                                    List<List<String>> sentences = index.search(entities);
                                                    int value = 100000;
                                                    if(sentences.size()<=value){
                                                            de.citec.sc.sentence.preprocessing.rdf.RDF.writeModel(sentences, pathToSentenceModel, language, property.get(0),additionalOutput);
                                                    }
                                                    else{
                                                            int begin = 0;
                                                            int end = 0;
                                                            for(int i= 0; i<Math.floor((double)sentences.size()/value);i++){
                                                                    begin = i*value;
                                                                    end = begin+value;
                                                                    de.citec.sc.sentence.preprocessing.rdf.RDF.writeModel(sentences.subList(begin, end), pathToSentenceModel, language, property.get(0),additionalOutput);
                                                            }
                                                            de.citec.sc.sentence.preprocessing.rdf.RDF.writeModel(sentences.subList(end, sentences.size()), pathToSentenceModel, language, property.get(0),additionalOutput);
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
			List<List<String>> properties, Language language) throws IOException {
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
	    	property.add(language.toString().toLowerCase());
	    	property.add(name);
	    	properties.add(property);
	    	//System.out.println(property.toString());
	    	
	    }
		
	}
	
	private static void loadOntology(String pathToOntology,
			List<List<String>> properties, Language language) throws IOException {
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
	    	property.add(language.toString().toLowerCase());
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
