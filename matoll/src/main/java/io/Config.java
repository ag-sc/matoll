package io;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Config {

	HashMap<String,String> params;
	
	String Model ="model";
	String GoldStandardLexicon = null;
	String OutputLexicon = "lexicon";
	String Output = "eval";
	Boolean Coreference = false;
	String Language = "EN";
	Integer numItems;
	
	List<String> Patterns;
	
	public void loadFromFile(String configFile) throws ParserConfigurationException, SAXException, IOException {
	
		File fXmlFile = new File(configFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
	 
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
	 
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	 
		NodeList nList = doc.getDocumentElement().getChildNodes();
		
	
		System.out.println("----------------------------");
		
		for (int i = 0; i < nList.getLength(); i++) {
			 
			Node node = nList.item(i);
									
			if (node.getNodeName().equals("Language"))
			{
				this.Language = node.getNodeValue();
			}
			
			if (node.getNodeName().equals("Coreference"))
			{
				if (node.getNodeValue().equals("True")) this.Coreference = true;
				if (node.getNodeValue().equals("False")) this.Coreference = false;
			}
			
			if (node.getNodeName().equals("GoldLexicon"))
			{
				this.GoldStandardLexicon = node.getNodeValue();
			}
			
			if (node.getNodeName().equals("OutputLexicon"))
			{
				this.OutputLexicon = node.getNodeValue();
			}
			
			if (node.getNodeName().equals("NumLexItems"))
			{
				this.numItems = new Integer(node.getNodeValue());
			}
			
			if (node.getNodeName().equals("Model"))
			{
				this.Model = node.getNodeValue();
			}
			
			if (node.getNodeName().equals("Patterns"))
			{
				NodeList patterns = node.getChildNodes();
				
				for (int j = 0; j <  patterns.getLength(); j++) {
			
					Node pattern = patterns.item(j);
					
					if (node.getNodeName().equals("Pattern"))
					
						Patterns.add(node.getNodeValue());
				}

			}
		}
		
	}

	
	
	private String getValueOfAttribute(Node node, String string) {
		// TODO Auto-generated method stub
		return null;
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



	public void setCoreference(Boolean coreference) {
		Coreference = coreference;
	}



	public String getLanguage() {
		return Language;
	}



	public void setLanguage(String language) {
		Language = language;
	}



	public List<String> getPatterns()
	{
		return Patterns;
	}

}
