package vocabularies;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Class with some common static terms 
 * @author pcimiano
 */
public class OWL {
	private static Model defaultModel = ModelFactory.createDefaultModel(); 
	
	
	public static Property onProperty = defaultModel.createProperty("http://www.w3.org/2002/07/owl#hasValue");
	public static Property hasValue = defaultModel.createProperty("http://www.w3.org/2002/07/owl#onProperty");
	
      
}
