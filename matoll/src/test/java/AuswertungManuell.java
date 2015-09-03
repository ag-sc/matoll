import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.utils.OntologyImporter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class AuswertungManuell {

	public static void main(String[] args) throws FileNotFoundException {
		// This test class checks if the equals method works...
		
            /*
            http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
            */
                Lexicon lexicon = new Lexicon();
		LexiconLoader loader = new LexiconLoader();
                
                String language = "en_adj";
		
               String path = "/Users/swalter/Documents/results_matoll_july_2015/";
//                Lexicon set1 = loader.loadFromFile(path+"set1.ttl");
//                Lexicon set2 = loader.loadFromFile(path+"set2.ttl");
//                Lexicon set3 = loader.loadFromFile(path+"set3.ttl");
//                Lexicon set4 = loader.loadFromFile(path+"set4.ttl");
//                for(LexicalEntry entry : set1.getEntries()) lexicon.addEntry(entry);
//                for(LexicalEntry entry : set2.getEntries()) lexicon.addEntry(entry);
//                for(LexicalEntry entry : set3.getEntries()) lexicon.addEntry(entry);
//                for(LexicalEntry entry : set4.getEntries()) lexicon.addEntry(entry);
                
                lexicon = loader.loadFromFile("/Users/swalter/Desktop/adjectives.ttl");
                
                List<String> properties = new ArrayList<String>();
                properties.add("http://dbpedia.org/ontology/spouse");
                properties.add("http://dbpedia.org/ontology/builder");
                properties.add("http://dbpedia.org/ontology/capital");
                properties.add("http://dbpedia.org/ontology/causeOfDeath");
                properties.add("http://dbpedia.org/ontology/hometown");
                properties.add("http://dbpedia.org/ontology/nationality");
                properties.add("http://dbpedia.org/ontology/headquarter");
                properties.add("http://dbpedia.org/ontology/killedBy");
                properties.add("http://dbpedia.org/ontology/leaderName");
                properties.add("http://dbpedia.org/ontology/movement");
                
                HashMap<String,Integer> hm = new HashMap<String,Integer>();
                
                HashMap<String,Integer> property0 = new HashMap<String,Integer>();
                HashMap<String,Integer> property1 = new HashMap<String,Integer>();
                HashMap<String,Integer> property2 = new HashMap<String,Integer>();
                HashMap<String,Integer> property3 = new HashMap<String,Integer>();
                HashMap<String,Integer> property4 = new HashMap<String,Integer>();
                HashMap<String,Integer> property5 = new HashMap<String,Integer>();
                HashMap<String,Integer> property6 = new HashMap<String,Integer>();
                HashMap<String,Integer> property7 = new HashMap<String,Integer>();
                HashMap<String,Integer> property8 = new HashMap<String,Integer>();
                HashMap<String,Integer> property9 = new HashMap<String,Integer>();
                
                for(LexicalEntry entry : lexicon.getEntries()){
                    for(Sense sense : entry.getSenseBehaviours().keySet()){
                        Restriction reference = (Restriction) sense.getReference();
                        String uri = reference.getProperty();
                        //String uri = sense.getReference().getURI();
                        if(properties.contains(uri)){
                            String signature = entry.getCanonicalForm()+"#"+uri;
                            int frequency = entry.getProvenance(sense).getFrequency();
                            if(hm.containsKey(signature)){
                                int value = hm.get(signature);
                                hm.put(signature, value+frequency);
                            }
                            else hm.put(signature, frequency);
                        }
                    }
                }
                
                for(String key : hm.keySet()){
                    if(key.contains(properties.get(0))) property0.put(key, hm.get(key));
                    if(key.contains(properties.get(1))) property1.put(key, hm.get(key));
                    if(key.contains(properties.get(2))) property2.put(key, hm.get(key));
                    if(key.contains(properties.get(3))) property3.put(key, hm.get(key));
                    if(key.contains(properties.get(4))) property4.put(key, hm.get(key));
                    if(key.contains(properties.get(5))) property5.put(key, hm.get(key));
                    if(key.contains(properties.get(6))) property6.put(key, hm.get(key));
                    if(key.contains(properties.get(7))) property7.put(key, hm.get(key));
                    if(key.contains(properties.get(8))) property8.put(key, hm.get(key));
                    if(key.contains(properties.get(9))) property9.put(key, hm.get(key));
                }
                
                System.out.println("property0.size():"+property0.size());
                System.out.println("property1.size():"+property1.size());
                System.out.println("property2.size():"+property2.size());
                System.out.println("property3.size():"+property3.size());
                System.out.println("property4.size():"+property4.size());
                System.out.println("property5.size():"+property5.size());
                System.out.println("property6.size():"+property6.size());
                System.out.println("property7.size():"+property7.size());
                System.out.println("property8.size():"+property8.size());
                System.out.println("property9.size():"+property9.size());
                 writeSortedList("spouse",language,property0);
                 writeSortedList("publisher",language,property1);
                 writeSortedList("religion",language,property2);
                 writeSortedList("influenced",language,property3);
                 writeSortedList("hometown",language,property4);
                 writeSortedList("nationality",language,property5);
                 writeSortedList("location",language,property6);
                 writeSortedList("city",language,property7);
                 writeSortedList("artist",language,property8);
                 writeSortedList("releaseDate",language,property9);
                 
                
                
   
			
		
		
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

    private static void writeSortedList(String name, String language, HashMap<String, Integer> property) {
        String path = name+"_"+language+".csv";
        ValueComparator bvc =  new ValueComparator(property);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
        sorted_map.putAll(property);
        String output = "";
        int counter = 0;
        for(String key : sorted_map.keySet()){
            counter ++;
            if (counter <=10){
                String[] tmp = key.split("#");
                output+=tmp[0]+";"+tmp[1]+";"+property.get(key)+"\n";
            }
            
        }
        
        PrintWriter writer;
        try {
                writer = new PrintWriter(path);
                writer.println(output);
                writer.close();
        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
    }

}

class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}