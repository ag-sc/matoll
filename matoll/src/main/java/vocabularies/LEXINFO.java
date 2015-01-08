package vocabularies;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Class with some common static terms 
 * @author pcimiano
 */
public class LEXINFO {
	private static Model defaultModel = ModelFactory.createDefaultModel(); 
	public static Property subject = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#subject");
	public static Property object = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject");
	public static Property pobject = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObj");
	public static Property copulativeArg = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeArg");
	public static Property possessiveAdjunct = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#possessiveAdjunct");
	public static Property partOfSpeech = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#partOfSpeech");

	public static Property verb = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
	public static Property commonNoun = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");
	public static Property adjective = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");
	
	public static Property transitiveFrame = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
	public static Property adjectivePPFrame = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePPFrame");
	public static Property nounPossessiveFrame = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPossessiveFrame");
	public static Property nounPredicateFrame = defaultModel.createProperty("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPredicateFrame");
	

	
}
