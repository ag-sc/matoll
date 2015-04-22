package de.citec.sc.sentence.preprosessing.sparql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Resources {

	public static void retrieveEntities(List<List<String>> properties, String resourceFolder, String endpoint){
		/*
		 * Slash works only for Unix-based systems
		 */
		if(!resourceFolder.endsWith("/")) resourceFolder+="/";
		
		for(List<String> property:properties){
			String pathToPropertyFile = resourceFolder+property.get(1)+"/"+property.get(2)+"/"+property.get(3)+"/"+property.get(4);
			boolean check_ontologyfolder = checkFolder(resourceFolder+property.get(1));
			boolean check_namespacefolder = checkFolder(resourceFolder+property.get(1)+"/"+property.get(2));
			boolean check_languagefolder = checkFolder(resourceFolder+property.get(1)+"/"+property.get(2)+"/"+property.get(3));
			boolean check_propertyFile = checkPropertyNameFile(pathToPropertyFile);
			if(check_ontologyfolder&&check_languagefolder&&check_namespacefolder&&check_propertyFile) getEntitiesFromServer(property,endpoint,pathToPropertyFile);
		}
		
	}
	
	public static List<List<String>> loadEntities(List<String> property, String resourceFolder) throws IOException{
		List<List<String>> entities = new ArrayList<List<String>>();
		String pathToPropertyFile = resourceFolder+property.get(1)+"/"+property.get(2)+"/"+property.get(3)+"/"+property.get(4);
		String entities_raw = "";
		
		File f = new File(pathToPropertyFile);
		if(f.exists()){
			//
		}
		else{
			return entities;
		}
		/*
		 * each line contains one property
		 */
		FileInputStream inputStream = new FileInputStream(pathToPropertyFile);
	    try {
	        entities_raw = IOUtils.toString(inputStream);
	    } finally {
	        inputStream.close();
	    }
	    for(String x:entities_raw.split("\n")){
	    	/*
	    	 * 1,3 are the uri's
	    	 */
	    	String subj = x.split("\t")[1];
	    	String obj = x.split("\t")[2];
	    	
	    	List<String> pair = new ArrayList<String>();
	    	
	    	subj = cleanEntity(subj);
	    	obj = cleanEntity(obj);
	    	
	    	pair.add(subj);
	    	pair.add(obj);
	    	
	    	entities.add(pair);
	    }
		
		return entities;
	}


	private static String cleanEntity(String term) {
		/*
		 * 1: 1989-04-20+02:00^^http://www.w3.org/2001/XMLSchema#date
		 * Only the year will be considered, in order to avoid noise with the id's from the parsed sentence
		 * same for http://www.w3.org/2001/XMLSchema#gYear
		 */
		if((term.contains("XMLSchema#date")||term.contains("XMLSchema#gYear")) && term.contains("-")){
			term = term.split("-")[0];
		}
		
		/*
		 * 2: Fanny by Gaslight (TV series)
		 */
		if(term.contains("(")){
			term = term.split("\\(")[0];
			if(term.endsWith(" ")){
				// -1 removes not only whitespace, but also last character of term!
				term = term.substring(0,term.lastIndexOf(" ")); //-1);
			}
		}
		
		if (term.contains("[")) {
			term = term.split("\\[")[0];
			term.trim();
		}
		
		/*
		 * 3: 62.0^^http://www.w3.org/2001/XMLSchema#double
		 */
		
		if(term.contains("XMLSchema#double")){
			term = term.replace("^^http://www.w3.org/2001/XMLSchema#double", "");
		}
		
		/*
		 * capacity:A.S.D. Torrecuso Calcio	1500^^http://www.w3.org/2001/XMLSchema#integer
		 */
		if(term.contains("XMLSchema#integer")){
			term = term.replace("^^http://www.w3.org/2001/XMLSchema#integer", "");
		}
		
		/*
		 * ^^httpwww.w3.org2001XMLSchema#decimal
		 */
		if(term.contains("XMLSchema#decimal")){
			term = term.replace("^^http://www.w3.org/2001/XMLSchema#decimal", "");
		}
                
                /*
                0001-12-290200^^httpwww.w3.org2001XMLSchema#gMonthDay
                */
                if(term.contains("200^^httpwww.w3.org2001XMLSchema#gMonthDay")){
			term = term.replace("200^^httpwww.w3.org2001XMLSchema#gMonthDay", "");
		}
		return term;
	}

	private static void getEntitiesFromServer(List<String> property,
			String endpoint, String path) {
		List<String> entities = Sparql.retrieveEntities(endpoint, property.get(0), property.get(3));
		if(!entities.isEmpty()) writeEntries(entities,path);
	}

	private static void writeEntries(List<String> entities, String path) {
		try{
			String output = "";
			for(String entity:entities) output+="\n"+entity;
			output=output.substring(1);
			
			PrintWriter writer = new PrintWriter(path);
			writer.print(output);
			writer.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}

	private static boolean checkPropertyNameFile(String name) {
		/*
		 * returns True if no File with input is found; else it returns false
		 * This means that if we return true, entities have to be retrieved from the endpoint
		 */
		File f = new File(name);
		if(f.exists()){
			return false;
		}
		else{
			return true;
		}
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
