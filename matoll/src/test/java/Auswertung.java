
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.io.LexiconSerialization;
import de.citec.sc.matoll.utils.OntologyImporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;


public class Auswertung {

	public static void main(String[] args) throws FileNotFoundException {
		// This test class checks if the equals method works...
		
                Lexicon lexicon = new Lexicon();
		LexiconLoader loader = new LexiconLoader();
		
                String path = "/Users/swalter/Documents/results_matoll_july_2015/";
                Lexicon set1 = loader.loadFromFile(path+"set1.ttl");
                Lexicon set2 = loader.loadFromFile(path+"set2.ttl");
                Lexicon set3 = loader.loadFromFile(path+"set3.ttl");
                Lexicon set4 = loader.loadFromFile(path+"set4.ttl");
                //Lexicon es_partial = loader.loadFromFile(path+"es_partial.ttl");
                Lexicon es_partial = loader.loadFromFile(path+"dbpedia2014_ES_Full_beforeTraining.ttl");
                
                Lexicon de_full = loader.loadFromFile(path+"de_full.ttl");
                Lexicon adjectives = loader.loadFromFile("/Users/swalter/Desktop/adjectives.ttl");
//                
                System.out.println("Loaded all");
//                System.out.println("#adjectives:"+Integer.toString(adjectives.size()));
//                System.out.println("#entries Corpus:"+Integer.toString(set1.size()+set2.size()+set3.size()+set4.size()));
//                System.out.println("#entries  spanish Corpus:"+Integer.toString(es_partial.size()));
//                System.out.println("#entries  german Corpus:"+Integer.toString(de_full.size()));

                HashSet<String> properties_en = new HashSet<String>();
                HashSet<String> properties_es = new HashSet<String>();
                HashSet<String> properties_de = new HashSet<String>();
                HashSet<String> properties_adjective = new HashSet<String>();
//                for(LexicalEntry entry : en.getEntries()) lexicon.addEntry(entry);
                for(LexicalEntry entry : set1.getEntries()) lexicon.addEntry(entry);
                for(LexicalEntry entry : set2.getEntries()) lexicon.addEntry(entry);
                for(LexicalEntry entry : set3.getEntries()) lexicon.addEntry(entry);
                for(LexicalEntry entry : set4.getEntries()) lexicon.addEntry(entry);
                System.out.println("Added english entries");
                System.out.println("#entries corpus en:"+lexicon.size());
                System.out.println("#entries adjectives en:"+adjectives.size());
                System.out.println("#entries  spanish Corpus:"+Integer.toString(es_partial.size()));
                System.out.println("#entries  german Corpus:"+Integer.toString(de_full.size()));
                for(LexicalEntry entry : es_partial.getEntries()) lexicon.addEntry(entry);
                for(LexicalEntry entry : de_full.getEntries()) lexicon.addEntry(entry);
                for(LexicalEntry entry : adjectives.getEntries()) lexicon.addEntry(entry);
//                for(LexicalEntry entry : adjectives.getEntries()){
//                    for(Sense sense : entry.getSenseBehaviours().keySet()){
//                        if (entry.getProvenance(sense).getConfidence()>0.8) lexicon.addEntry(entry);
//                    }
//                    
//                }
//                
                
                for(LexicalEntry entry : lexicon.getEntries()){
                    Set<Reference> references = entry.getReferences();
                    for(Reference ref:references){
                        if (ref instanceof de.citec.sc.matoll.core.SimpleReference){
                            String uri = ref.getURI();
//                            System.out.println(ref.getURI());
                            if(entry.getLanguage().equals(Language.EN))properties_en.add(uri);
                            if(entry.getLanguage().equals(Language.ES))properties_es.add(uri);
                            if(entry.getLanguage().equals(Language.DE))properties_de.add(uri);
                        }
                        else{
                            Restriction reference = (Restriction) ref;
                            
//                            System.out.println("Get URI:"+reference.getURI());
//                            System.out.println("on Value:"+reference.getValue());
//                            System.out.println("Get property:"+reference.getProperty());
//                            System.out.println();
                            if(entry.getLanguage().equals(Language.EN)){
                                properties_en.add(reference.getProperty());
                                properties_adjective.add(reference.getProperty());
                            }
                            if(entry.getLanguage().equals(Language.ES))properties_es.add(reference.getProperty());
                            if(entry.getLanguage().equals(Language.DE))properties_de.add(reference.getProperty());
                        }
                        
                    }
                }
                
                System.out.println("#entries:"+lexicon.size());
                
                Model model = ModelFactory.createDefaultModel();
		
		LexiconSerialization serializer = new LexiconSerialization(false);
		
		serializer.serialize(lexicon, model);
		
		FileOutputStream out = new FileOutputStream(new File("full_dbpedia.ttl"));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
                
                System.out.println("new lexicon is written");
                System.out.println("#properties en:"+properties_en.size());
                System.out.println("#properties_adjective:"+properties_adjective.size());
                System.out.println("#properties es:"+properties_es.size());
                System.out.println("#properties de:"+properties_de.size());
                properties_en.addAll(properties_de);
                properties_en.addAll(properties_es);
                properties_en.addAll(properties_adjective);
                System.out.println("#overallProperties:"+properties_en.size());
                
//                List<List<String>> properties = new ArrayList<List<String>>();
//            try {
//                loadOntology("/Users/swalter/Downloads/dbpedia_2014.owl",properties,"en");
//            } catch (IOException ex) {
//                Logger.getLogger(Auswertung.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            int counter = 0;
//            for(List<String> property_element : properties){
//                if(property_element.get(0).contains("http://dbpedia.org/ontology/"))counter+=1;
//            }
//            System.out.println(counter);
			
		
		
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

}
