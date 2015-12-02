package de.citec.sc.sentence.preprocessing.sparql;

import de.citec.sc.sentence.preprocessing.process.Language;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

public class Resources {

	public static void retrieveEntities(List<List<String>> properties, String resourceFolder, String endpoint, Language language){
            /*
             * Slash works only for Unix-based systems
             */
            if(!resourceFolder.endsWith("/")) resourceFolder+="/";

            List<String> empty_properties = new ArrayList<String>();

            for(List<String> property:properties){
                    String pathToPropertyFile = resourceFolder+property.get(1)+"/"+property.get(2)+"/"+property.get(3)+"/"+property.get(4);
                    boolean check_ontologyfolder = checkFolder(resourceFolder+property.get(1));
                    boolean check_namespacefolder = checkFolder(resourceFolder+property.get(1)+"/"+property.get(2));
                    boolean check_languagefolder = checkFolder(resourceFolder+property.get(1)+"/"+property.get(2)+"/"+property.get(3));
                    boolean check_propertyFile = checkPropertyNameFile(pathToPropertyFile);
                    if(check_ontologyfolder&&check_languagefolder&&check_namespacefolder&&check_propertyFile) getEntitiesFromServer(property,endpoint,pathToPropertyFile,empty_properties);
            }
                
            PrintWriter writer;
            try {
                writer = new PrintWriter("empty_properties"+language.toString()+".txt");
                for(String p: empty_properties) writer.println(p);
                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Resources.class.getName()).log(Level.SEVERE, null, ex);
            }
            
		
	}
	
	public static List<List<String>> loadEntities(List<String> property, String resourceFolder) throws IOException{
		List<List<String>> entities = new ArrayList<List<String>>();
                Set<String> signature_pairs = new HashSet<String>();
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
	    	 * 0,3 are the uri's
	    	 */
                String[] entity_tmp = x.split("\t");
	    	String subj = entity_tmp[1];
	    	String obj = entity_tmp[2];
                String subj_uri = entity_tmp[0];
                String obj_uri = entity_tmp[3];
	    	
	    	List<String> pair = new ArrayList<String>();
	    	
                try{
                    subj = cleanEntity(subj);
                    obj = cleanEntity(obj);

                    pair.add(subj);
                    pair.add(obj);
                    pair.add(subj_uri);
                    pair.add(obj_uri);

                    String signature_1 = subj+obj;
                    String signature_2 = obj+subj;

                    if(!signature_pairs.contains(signature_1)&&!signature_pairs.contains(signature_2) && !subj_uri.equals(obj_uri) && !subj.contains(obj) && !obj.contains(subj)){
                        entities.add(pair);
                        signature_pairs.add(signature_1);
                        signature_pairs.add(signature_2);
                    } 
                    
                    if(obj.contains(".")){
                        pair = new ArrayList<String>();
                        try{
                            obj = obj.replace(".", "");

                            pair.add(subj);
                            pair.add(obj);
                            pair.add(subj_uri);
                            pair.add(obj_uri);

                            signature_1 = subj+obj;
                            signature_2 = obj+subj;

                            if(!signature_pairs.contains(signature_1)&&!signature_pairs.contains(signature_2) && !subj_uri.equals(obj_uri) && !subj.contains(obj) && !obj.contains(subj)){
                                entities.add(pair);
                                signature_pairs.add(signature_1);
                                signature_pairs.add(signature_2);
                            } 
                        }
                        catch(Exception e){
                            e.printStackTrace();
                            System.out.println("Problem with: "+subj+" + "+obj);
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Problem with: "+subj+" + "+obj);
                }
	    	
                
	    	
	    }
		
		return entities;
	}


	public static String cleanEntity(String term) {
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
		}
		
		if (term.contains("[")) {
			term = term.split("\\[")[0];
		}
                
                if (term.contains("<http://dbpedia.org/datatype/")) {
			term = term.split("<http://dbpedia.org/datatype/")[0];
		}
                if (term.contains("|")) {
			term = term.split("\\|")[0];
		}
                
                if(term.contains("^^")){
                    term = term.split("\\^\\^")[0];
                }
                
                if(term.contains(",")){
                    term = term.split(",")[0];
                }
		
		
                term = term.replace("\"","");
                
                
                if(term.contains("*")) return "";
                
                if (term.endsWith(".0")) term = term.substring(0, term.lastIndexOf(".0"));
                
                term = term.trim();
                
		return term;
	}

	private static void getEntitiesFromServer(List<String> property,
			String endpoint, String path, List<String> empty_properties) {
		List<String> entities = Sparql.retrieveEntities(endpoint, property.get(0), property.get(3));
		if(!entities.isEmpty()) writeEntries(entities,path);
                else{
                    empty_properties.add(property.get(0));
                }
	}

	private static void writeEntries(List<String> entities, String path) {
		try{
			StringBuilder string_builder = new StringBuilder();
                        entities.stream().forEach((entity) -> {
                            string_builder.append("\n");
                            string_builder.append(entity);
                    });
			String output=string_builder.toString();
                        output = output.substring(1);
			
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
