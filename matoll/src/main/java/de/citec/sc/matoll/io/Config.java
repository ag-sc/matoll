package de.citec.sc.matoll.io;


import de.citec.sc.matoll.core.Language;
import static de.citec.sc.matoll.core.Language.DE;
import static de.citec.sc.matoll.core.Language.EN;
import static de.citec.sc.matoll.core.Language.ES;
import static de.citec.sc.matoll.core.Language.JA;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_DatatypeNoun;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_DatatypeNoun_2;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Intransitive_PP;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Noun_PP_appos;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Noun_PP_copulative;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Noun_PP_possessive;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Noun_PP_player;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Predicative_Participle_passive;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Transitive_Verb;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Predicative_Participle_copulative;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Transitive_Passive;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Predicative_Adjective;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Noun_PP;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Noun_Possessive;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Noun_Possessive_b;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Transitive;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Transitive_Passive;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Intransitive_PP;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Refelexive_Transitive_PP;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Noun_PP_appos;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Noun_Possessive_appos;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Transitive;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Noun_PP_copulative_b;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Noun_PP_copulative_withHop;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Noun_PP_copulative;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Noun_PP_appos_b;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Noun_PP_appos;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Intransitive_PP;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Predicative_Participle_Copulative;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Predicative_Participle_Passive;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Reflexive_Transitive_PP;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Transitive_Reciprocal;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Transitive_passive;

public class Config {

	Logger logger = LogManager.getLogger(Config.class.getName());
	
	HashMap<String,String> params;
	
	String Model = "model";
	String GoldStandardLexicon = null;
	String OutputLexicon = "lexicon";
	String Output = "eval";
	Boolean Coreference = false;
        Boolean Statistics = true;
	String Classifier = "de.citec.sc.matoll.classifiers.FreqClassifier";
	Language Language = EN;
	Integer numItems;
	String Frequency;
        String BaseUri = "http://dblexipedia.org/";
        
        boolean RemoveStopwords = false;
        
        List<File> files = new ArrayList<>();

   

	
	List<SparqlPattern> Patterns = null;
	
	public Config()
	{
	}
	
	public void loadFromFile(String configFile) throws ParserConfigurationException, SAXException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, Exception {
	
		// add logger here...
		System.out.print("Reading configuration from: "+configFile+"\n");
		
		File fXmlFile = new File(configFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
	 
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
	 
		NodeList nList = doc.getDocumentElement().getChildNodes();
		
	
		System.out.println("----------------------------");
		
		for (int i = 0; i < nList.getLength(); i++) {
			 
			Node node = nList.item(i);
									
			if (node.getNodeName().equals("Language"))
			{
				this.Language = mapToLanguage(node.getTextContent());
				
				if (Language.equals(EN))
				{
					Patterns = new ArrayList<SparqlPattern>();
					
					Patterns.add(new SparqlPattern_EN_Intransitive_PP());
					Patterns.add(new SparqlPattern_EN_Noun_PP_appos());
					Patterns.add(new SparqlPattern_EN_Noun_PP_copulative());
					Patterns.add(new SparqlPattern_EN_Predicative_Participle_passive());
					Patterns.add(new SparqlPattern_EN_Transitive_Verb());
					Patterns.add(new SparqlPattern_EN_Predicative_Participle_copulative());
                                        Patterns.add(new SparqlPattern_EN_Transitive_Passive());
                                        Patterns.add(new SparqlPattern_EN_Noun_PP_possessive());
//                                        Patterns.add(new SparqlPattern_EN_DatatypeNoun());
//					Patterns.add(new SparqlPattern_EN_DatatypeNoun_2());
//                                        Patterns.add((new SparqlPattern_EN_Noun_PP_player()));
					logger.info("Adding patterns 1-9 (EN) to pattern library \n");
				}
				if (Language.equals(DE))
				{
					Patterns = new ArrayList<SparqlPattern>();
					
					Patterns.add(new SparqlPattern_DE_Predicative_Adjective());
                                        Patterns.add(new SparqlPattern_DE_Noun_PP());
                                        Patterns.add(new SparqlPattern_DE_Noun_Possessive());
                                        Patterns.add(new SparqlPattern_DE_Noun_Possessive_b());
                                        Patterns.add(new SparqlPattern_DE_Transitive());
                                        Patterns.add(new SparqlPattern_DE_Transitive_Passive());
                                        //Patterns.add(new SparqlPattern_DE_Transitive_Passive_optional());
                                        Patterns.add(new SparqlPattern_DE_Intransitive_PP());
                                        Patterns.add(new SparqlPattern_DE_Refelexive_Transitive_PP());
                                        Patterns.add(new SparqlPattern_DE_Noun_PP_appos());
                                        Patterns.add(new SparqlPattern_DE_Noun_Possessive_appos());
					
					logger.info("Adding patterns 1-10 (DE) to pattern library \n");
				}
				
				if (Language.equals(ES))
				{
					Patterns = new ArrayList<SparqlPattern>();
					
					Patterns.add(new SparqlPattern_ES_Transitive());
                                        Patterns.add(new SparqlPattern_ES_Noun_PP_copulative_b());
                                        Patterns.add(new SparqlPattern_ES_Noun_PP_copulative_withHop());
                                        Patterns.add(new SparqlPattern_ES_Noun_PP_copulative());
                                        Patterns.add(new SparqlPattern_ES_Noun_PP_appos_b());
                                        Patterns.add(new SparqlPattern_ES_Noun_PP_appos());
                                        Patterns.add(new SparqlPattern_ES_Predicative_Participle_Copulative());
                                        Patterns.add(new SparqlPattern_ES_Predicative_Participle_Passive());
                                        Patterns.add(new SparqlPattern_ES_Intransitive_PP());
                                        Patterns.add(new SparqlPattern_ES_Transitive_Reciprocal());
                                        Patterns.add(new SparqlPattern_ES_Reflexive_Transitive_PP());
                                        Patterns.add(new SparqlPattern_ES_Transitive_passive());
                                        
					
					logger.info("Adding patterns 1-9 (ES) to pattern library \n");
				}
			}
			
			if (node.getNodeName().equals("Coreference"))
			{				
				if (node.getTextContent().equals("True")) this.Coreference = true;
				if (node.getTextContent().equals("False")) this.Coreference = false;
			}
                        
                        if (node.getNodeName().equals("RemoveStopwords"))
			{				
				if (node.getTextContent().equals("True")) this.RemoveStopwords = true;
				if (node.getTextContent().equals("False")) this.RemoveStopwords = false;
			}
                        
                        if (node.getNodeName().equals("Statistics"))
			{				
				if (node.getTextContent().equals("True")) this.Statistics = true;
				if (node.getTextContent().equals("False")) this.Statistics = false;
			}
			
			if (node.getNodeName().equals("GoldStandardLexicon"))
			{
				this.GoldStandardLexicon = node.getTextContent();
			}
			
			if (node.getNodeName().equals("Classifier"))
			{
				this.Classifier = node.getTextContent();
			}
                        
                        if (node.getNodeName().equals("BaseURI"))
			{
				this.BaseUri = node.getTextContent();
			}
			
			if (node.getNodeName().equals("OutputLexicon"))
			{
				this.OutputLexicon = node.getTextContent();
			}
			
			if (node.getNodeName().equals("MinFrequency"))
			{
				this.Frequency = node.getTextContent();
			}
			
			
			if (node.getNodeName().equals("Output"))
			{
				this.Output = node.getTextContent();
			}
			
			if (node.getNodeName().equals("NumLexItems"))
			{
				this.numItems = new Integer(node.getTextContent());
			}
			
			if (node.getNodeName().equals("Model"))
			{
				this.Model = node.getTextContent();
			}
			
			if (node.getNodeName().equals("Patterns"))
			{
				Patterns = new ArrayList<SparqlPattern>();
				
				NodeList patterns = node.getChildNodes();
				
				for (int j = 0; j <  patterns.getLength(); j++) {
			
					Node pattern = patterns.item(j);
					
					if (pattern.getNodeName().equals("Pattern"))
					{
						Patterns.add((SparqlPattern) Class.forName(pattern.getTextContent()).newInstance());
						
					}	
						
				}

			}
                        
                        if (node.getNodeName().equals("Files"))
			{				
				NodeList patterns = node.getChildNodes();
				
				for (int j = 0; j <  patterns.getLength(); j++) {
			
					Node pattern = patterns.item(j);
					
					if (pattern.getNodeName().equals("File"))
					{
						files.add(new File(pattern.getTextContent()));
						
					}	
						
				}

			}
                        
		}
		
	}

        
        private Language mapToLanguage(String s) throws Exception {
            
            if      (s.toLowerCase().equals("en") || s.toLowerCase().equals("eng")) return EN;
            else if (s.toLowerCase().equals("de") || s.toLowerCase().equals("ger")) return DE;
            else if (s.toLowerCase().equals("es") || s.toLowerCase().equals("spa")) return ES;
            else if (s.toLowerCase().equals("ja") || s.toLowerCase().equals("jpn")) return JA;
            else throw new Exception("Language '" + s + "' unknown.");
        }
	

	public String getModel() {
		return Model;
	}



	public void setModel(String model) {
		Model = model;
	}



	public String getGoldStandardLexicon() {
		return GoldStandardLexicon;
	}



	public void setGoldStandardLexicon(String goldStandardLexicon) {
		GoldStandardLexicon = goldStandardLexicon;
	}



	public String getOutputLexicon() {
		return OutputLexicon;
	}



	public void setOutputLexicon(String outputLexicon) {
		OutputLexicon = outputLexicon;
	}



	public String getOutput() {
		return Output;
	}



	public void setOutput(String output) {
		Output = output;
	}



	public Boolean getCoreference() {
		return Coreference;
	}
        
        public Boolean removeStopwords() {
		return RemoveStopwords;
	}

	public String getFrequency()
	{
		return Frequency;
	}

	public void setCoreference(Boolean coreference) {
		Coreference = coreference;
	}



	public Language getLanguage() {
		return Language;
	}



	public void setLanguage(Language language) {
		Language = language;
	}

	public String getClassifier()
	{
		return Classifier;
	}


	public List<SparqlPattern> getPatterns()
	{
		if (Patterns.size() > 0)
		
		return Patterns;
		
		return null;
	}
        
        public String getBaseUri() {
            return BaseUri;
        }
         public List<File> getFiles() {
            return files;
        }

    public boolean doStatistics() {
        return Statistics;
    }

}
