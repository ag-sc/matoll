package de.citec.sc.matoll.vocabularies;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Class with some common static terms 
 * @author pcimiano
 */
public class LEMON {
	private static Model defaultModel = ModelFactory.createDefaultModel(); 
	public static Property lexicalizedSense = defaultModel.createProperty("http://lemon-model.net/lemon#lexicalizedSense");
	public static Property writtenRep = defaultModel.createProperty("http://lemon-model.net/lemon#writtenRep");
	public static Property language = defaultModel.createProperty("http://lemon-model.net/lemon#language");
	public static Property entry = defaultModel.createProperty("http://lemon-model.net/lemon#entry");
	public static Property sense = defaultModel.createProperty("http://lemon-model.net/lemon#sense");
	public static Property canonicalForm = defaultModel.createProperty("http://lemon-model.net/lemon#canonicalForm");
	public static Property constituent = defaultModel.createProperty("http://www.w3.org/ns/decomp#constituent");
	public static Property otherForm = defaultModel.createProperty("http://lemon-model.net/lemon#otherForm");
	public static Property identifies = defaultModel.createProperty("http://www.w3.org/ns/decomp#identifies");
	public static Property definition = defaultModel.createProperty("http://lemon-model.net/lemon#definition");
	public static Property reference = defaultModel.createProperty("http://lemon-model.net/lemon#reference");
	public static Property syntacticBehaviour = defaultModel.createProperty("http://lemon-model.net/lemon#synBehavior");
	public static Property marker = defaultModel.createProperty("http://lemon-model.net/lemon#marker");
	public static Property isA = defaultModel.createProperty("http://lemon-model.net/lemon#isA");
	public static Property objOfProp = defaultModel.createProperty("http://lemon-model.net/lemon#objOfProp");
	public static Property subjOfProp = defaultModel.createProperty("http://lemon-model.net/lemon#subjOfProp");
        public static Property sparqlPattern = defaultModel.createProperty("http://lemon-model.net/lemon#sparqlPattern");

	public static Resource Lexicon = defaultModel.createProperty("http://lemon-model.net/lemon#Lexicon");
	public static Resource LexicalEntry = defaultModel.createProperty("http://lemon-model.net/lemon#LexicalEntry");
    
     
     
       
     
}
