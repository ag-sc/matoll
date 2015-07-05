package de.citec.sc.matoll.adjectiveApproach;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import de.citec.sc.matoll.utils.Levenshtein;
import de.citec.sc.matoll.utils.Wordnet;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class ExtractData {
	
	private static Wordnet wordnet; 
	private static HashMap<String, String> cache;
	private static HashSet<String> stopwords;
	
	public ExtractData(String path_to_wordnet) throws IOException{
		ExtractData.wordnet = new Wordnet(path_to_wordnet);
		ExtractData.cache = new HashMap<String, String>();
		ExtractData.stopwords = new HashSet<String>();
		String everything = "";
		FileInputStream inputStream = new FileInputStream("resources/adjectivelist_en.txt");
	    try {
	        everything = IOUtils.toString(inputStream);
	    } finally {
	        inputStream.close();
	    }
	    
	    String[] adjectives = everything.split("\n");
	    for(String adjective : adjectives){
	    	
	    	if(adjective.equals("in")){
	    	}
	    	else{
	    		ExtractData.cache.put(adjective.toLowerCase(), "");
	    	}
	    }
	    
	    inputStream = new FileInputStream("resources/englishST.txt");
	    try {
	        everything = IOUtils.toString(inputStream);
	    } finally {
	        inputStream.close();
	    }
	    
	    for(String stopword : everything.split("\n")){
	    	ExtractData.stopwords.add(stopword.toLowerCase());
	    }
	    
	}
	
	public List<AdjectiveObject> start(String path_to_resourceFolder, String uri,MaxentTagger tagger,Morphology mp) throws Exception{
		
           
		
		List<AdjectiveObject> list_adjectiveobject = new ArrayList<AdjectiveObject>();
                 /*
                ignore URI:http://dbpedia.org/ontology/abstract
                */
                if(uri.equals("http://dbpedia.org/ontology/abstract")
                        || uri.equals("http://dbpedia.org/ontology/slogan")
                        || uri.equals("http://dbpedia.org/ontology/identificationSymbol")) return list_adjectiveobject;
                List<List<String>> results = getRawObjects(path_to_resourceFolder,uri,"en");
		if(results.isEmpty()){
			System.out.println("No properties for "+uri);
			return list_adjectiveobject;
		}

		for (List<String> property_objects: results){
			//System.out.println(property_object);
			AdjectiveObject adjectiveobject = new AdjectiveObject();
			String property_object = property_objects.get(0).toLowerCase();
			adjectiveobject.setObject(property_object);
			adjectiveobject.setObjectURI(property_objects.get(1));
			adjectiveobject.setUri(uri);
			if(property_object.contains(" ")){
				String[] object_tmp = property_object.split(" ");
				for(String term : object_tmp){
					//System.out.println("term:"+term);
					if((wordnet.checkForAdjective(term.toLowerCase())|| cache.containsKey(term.toLowerCase()))
							&&!stopwords.contains(term.toLowerCase())){
						adjectiveobject.setAdjectiveTerm(term);
						adjectiveobject.setAdjective(true);
						adjectiveobject.setSublabel(getSublabel(term));
						adjectiveobject.setPattern(property_object.replace(term, "<adj>"));
						adjectiveobject.setPos_Pattern(getPosPattern(property_object,tagger));
						adjectiveobject.setPos_adj_Pattern(getPosPattern(property_object.replace(term, "<adj>"),tagger));
					}
				}
			}
			else {
				if((wordnet.checkForAdjective(property_object.toLowerCase())|| cache.containsKey(property_object.toLowerCase())
						&&!stopwords.contains(property_object.toLowerCase()))){
					adjectiveobject.setAdjectiveTerm(property_object);
					adjectiveobject.setAdjective(true);
					adjectiveobject.setSublabel(getSublabel(property_object));
					adjectiveobject.setPattern("<adj>");
					adjectiveobject.setPos_adj_Pattern("<adj>");
					adjectiveobject.setPos_Pattern(getPosPattern(property_object,tagger));
				}
			}
			//System.out.println(adjectiveobject.toString());
			updateList(list_adjectiveobject,adjectiveobject);			
		}
		
		
		double ratio = calculateAndSetRatio(list_adjectiveobject);
		setPositionAdjectives(list_adjectiveobject);
		setNLD(list_adjectiveobject);
		setPatternRatio(list_adjectiveobject);
		setNormalizedFrequency(list_adjectiveobject);
		setNormalizedObjectFrequency(list_adjectiveobject);
		setNormalizedObjectOccurrences(list_adjectiveobject);
		return list_adjectiveobject;
	}




	
	private List<List<String>> getRawObjects(String resourceFolder,String uri, String language) throws IOException {
		/*
		 * In the moment only consider those adjective, which have an URI and a label as object.
		 * 
		 */
		String ontologyName =findOntologyName(uri);
    	String[] tmp = uri.split("/");
    	String name = tmp[tmp.length-1];
    	String namespace = tmp[tmp.length-2];
    	List<String> property = new ArrayList<String>();
    	property.add(uri);
    	property.add(ontologyName);
    	property.add(namespace);
    	property.add(language);
    	property.add(name);
		
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
	    	String obj = x.split("\t")[2];
	    	String obj_uri = x.split("\t")[3];
	    	//System.out.println("obj_uri:"+obj_uri);
	    	//System.out.println("obj:"+obj);
	    	obj = cleanEntity(obj);
	    	/*
			 * In the moment only consider those adjective, which have an URI and a label as object.
			 * With other words ignore those properties, with only literals on the right side
			 */
	    	//if(obj_uri.contains("http://dbpedia.org/")){
	    		List<String> tmp_entity = new ArrayList<String>();
	    		tmp_entity.add(obj);
	    		tmp_entity.add(obj_uri);
	    		entities.add(tmp_entity);
	    	//}
	    }
		
		return entities;
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
				term = term.substring(0,term.lastIndexOf(" ")-1);
			}
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
		return term;
	}

	private void setNormalizedObjectFrequency(
			List<AdjectiveObject> list_adjectiveobject) {
		int overall = 0;
		for(AdjectiveObject adjectiveobject : list_adjectiveobject) overall+=adjectiveobject.getFrequency();
		for(AdjectiveObject adjectiveobject : list_adjectiveobject) {
			adjectiveobject.setNormalizedObjectFrequency((double) adjectiveobject.getFrequency()/overall);
			double p = adjectiveobject.getFrequency()/(double)overall;
			double entropy =-(p)*Math.log(p);
			adjectiveobject.setEntropy(entropy);
		}
		
	}

	private void setNormalizedObjectOccurrences(
			List<AdjectiveObject> list_adjectiveobject) {
		int overall = 0;
		for(AdjectiveObject adjectiveobject : list_adjectiveobject) overall+=adjectiveobject.getFrequency();
		for(AdjectiveObject adjectiveobject : list_adjectiveobject){
			int tmp_counter = 0;
			for(AdjectiveObject adj : list_adjectiveobject){
				if(adj.getObject().contains(adjectiveobject.getObject())) tmp_counter+=adj.getFrequency();
			}
			adjectiveobject.setNormalizedObjectOccurrences((double)tmp_counter/overall);
		}
		
	}

	private void setNormalizedFrequency(
			List<AdjectiveObject> list_adjectiveobject) {
		int overall = 0;
		for(AdjectiveObject adjectiveobject : list_adjectiveobject) overall+=adjectiveobject.getFrequency();
		for(AdjectiveObject adjectiveobject : list_adjectiveobject){
			int tmp_counter = 0;
			for(AdjectiveObject adj : list_adjectiveobject){
				if(adjectiveobject.getAdjectiveTerm().equals(adj.getAdjectiveTerm())) tmp_counter+=adj.getFrequency();
			}
			adjectiveobject.setNormalizedFrequency((double)tmp_counter/overall);
		}
		
		
	}

	private void updateList(List<AdjectiveObject> list_adjectiveobject,
			AdjectiveObject adjectiveobject) {
		String signature = adjectiveobject.getAdjectiveTerm()+" "+adjectiveobject.getObject();
		
		boolean addToList = true;
		for(AdjectiveObject object : list_adjectiveobject){
			String tmp_signature = object.getAdjectiveTerm()+" "+object.getObject();
			if(tmp_signature.equals(signature)){
				object.setFrequency(object.getFrequency()+1);
				addToList = false;
				break;
			}
		}
		
		if(addToList)list_adjectiveobject.add(adjectiveobject);
		
	}

	private void setPatternRatio(List<AdjectiveObject> list_adjectiveobject) {
		HashMap<String, Integer> pattern = new HashMap<String, Integer>();
		HashMap<String, Integer> pos_pattern = new HashMap<String, Integer>();
		for(AdjectiveObject adjectiveobject : list_adjectiveobject){
			if(adjectiveobject.isAdjective()){
				updateHashMap(pattern,adjectiveobject.getPattern());
				updateHashMap(pos_pattern,adjectiveobject.getPos_Pattern());
			}
		}
		
		for(AdjectiveObject adjectiveobject : list_adjectiveobject){
			if(adjectiveobject.isAdjective()){
				adjectiveobject.setRatio_pattern((double) 1/pattern.size());
				adjectiveobject.setRatio_pos_pattern((double) 1/pos_pattern.size());
			}
		}
		
		
	}
	
	private void updateHashMap(HashMap<String, Integer> list_pattern,
			String pattern) {
		if(list_pattern.containsKey(pattern)){
			int value = list_pattern.get(pattern);
			value += 1;
			list_pattern.put(pattern, value);							
		}
		else{
			list_pattern.put(pattern, 1);
		}
	}

	private void setNLD(List<AdjectiveObject> list_adjectiveobject) {
		for(AdjectiveObject adjectiveobject:list_adjectiveobject){
			if(adjectiveobject.isAdjective()) adjectiveobject.setNld(Levenshtein.normalizedLevenshteinDistance(adjectiveobject.getAdjectiveTerm(),adjectiveobject.getObject()));
		}
	}

	private double calculateAndSetRatio(List<AdjectiveObject> list_adjectiveobject) {
		int counter = 0;
		for(AdjectiveObject adjectiveobject: list_adjectiveobject){
			if(adjectiveobject.isAdjective())counter++;
		}
		double ratio = (double) counter/list_adjectiveobject.size();
		for(AdjectiveObject adjectiveobject: list_adjectiveobject){
			adjectiveobject.setRatio(ratio);
		}
		return ratio;
	}
	
	
	private void setPositionAdjectives(List<AdjectiveObject> list_adjectiveobject) {
	
		for(AdjectiveObject adjectiveobject: list_adjectiveobject){
			if(adjectiveobject.isAdjective()){
				//position starts with 1!!!
				int position = findPosition(adjectiveobject.getAdjectiveTerm(),adjectiveobject.getObject());
				adjectiveobject.setPosition(position);
				if (position==1)adjectiveobject.setFirstPosition(true);
				if(adjectiveobject.getObject().contains(" ")){
					String[] tmp = adjectiveobject.getObject().split(" ");
					if(tmp.length==position) adjectiveobject.setLastPosition(true);
				}
				else{
					adjectiveobject.setLastPosition(true);
				}
			}
		}
		
	}
	

	private int findPosition(String adjectiveTerm, String object) {
		if(!object.contains(" ")) return 1;
		else{
			String[] tmp = object.split(" ");
			int counter = 0;
			int position = 0;
			for(String x:tmp){
				counter+=1;
				if(adjectiveTerm.equals(x)) position = counter;
			}
			
			return position;
		}
	}



	private String getSublabel(String x) {
		/*
		 * add last x characters of string.
		 */
		int value = 3;
		String sublabel = "";
		if(x.length()>=value) sublabel=x.toLowerCase().substring(x.length()-value);
		else sublabel=x.toLowerCase();
		return sublabel;
	}

	private String getPosPattern(String input,MaxentTagger tagger) {
		String output = "";
		for (String x:tagger.tagString(input).split(" ")){
			if (x.contains("<adj>")){
				output+= " "+x.split("_")[0];
			}
			else output+=" "+x.split("_")[1];
		}
		return output.substring(1);
	}



}
