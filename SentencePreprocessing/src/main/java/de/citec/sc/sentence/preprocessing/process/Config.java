/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.sentence.preprocessing.process;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author swalter
 */
public class Config {

    
    
    Language language = Language.EN;
    String index;
    String input;
    String output;
    String endpoint ="http://dbpedia.org/sparql";
    boolean withSentences;
    boolean additionalOutput;
    
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
        
        /*
        <Language>EN</Language>
   <Index>/Users/swalter/Index/EnglishIndex</Index>
   <Input>../dbpedia_2014.owl</Input>
   <Output>/Users/swalter/Desktop/Resources</Output>
   <WithSentences>False</WithSentences>
   <AdditionalOutput>False</AdditionalOutput>
        */
        
        
        for (int i = 0; i < nList.getLength(); i++) {
			 
            Node node = nList.item(i);

            if (node.getNodeName().equals("Language"))
            {
                    this.language = mapToLanguage(node.getTextContent());
            }
            
            if (node.getNodeName().equals("Index"))
            {
                    this.index = node.getTextContent();
            }
            
            if (node.getNodeName().equals("Input"))
            {
                    this.input = node.getTextContent();
            }
            
            if (node.getNodeName().equals("Output"))
            {
                    this.output = node.getTextContent();
            }
            
            if (node.getNodeName().equals("Endpoint"))
            {
                    this.endpoint = node.getTextContent();
            }
            
            if (node.getNodeName().equals("WithSentences"))
            {				
                if (node.getTextContent().equals("True")) this.withSentences = true;
                if (node.getTextContent().equals("False")) this.withSentences = false;
            }

            if (node.getNodeName().equals("AdditionalOutput"))
            {				
                if (node.getTextContent().equals("True")) this.additionalOutput = true;
                if (node.getTextContent().equals("False")) this.additionalOutput = false;
            }
                        
        }
                
                
                
    }
    
    
    
    private Language mapToLanguage(String s) throws Exception {
            
        if      (s.toLowerCase().equals("en") || s.toLowerCase().equals("eng")) return Language.EN;
        else if (s.toLowerCase().equals("de") || s.toLowerCase().equals("ger")) return Language.DE;
        else if (s.toLowerCase().equals("es") || s.toLowerCase().equals("spa")) return Language.ES;
        else if (s.toLowerCase().equals("ja") || s.toLowerCase().equals("jpn")) return Language.JA;
        else throw new Exception("Language '" + s + "' unknown.");
     }
    
    
    public Language getLanguage() {
        return language;
    }

    public String getIndex() {
        return index;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public boolean isWithSentences() {
        return withSentences;
    }

    public boolean isAdditionalOutput() {
        return additionalOutput;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
}
