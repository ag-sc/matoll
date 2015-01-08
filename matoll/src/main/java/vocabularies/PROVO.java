package vocabularies;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Class with some common static terms 
 * @author pcimiano
 */
public class PROVO {
	private static Model defaultModel = ModelFactory.createDefaultModel(); 
	public static Property associatedWith = defaultModel.createProperty("http://www.w3.org/ns/prov#associatedWith");
	public static Property generatedBy = defaultModel.createProperty("http://www.w3.org/ns/prov#generatedBy");
	public static Property startedAtTime = defaultModel.createProperty("http://www.w3.org/ns/prov#startedAtTime");
	public static Property endedatTime = defaultModel.createProperty("http://www.w3.org/ns/prov#endedAtTime");
	
	
	public static Resource Activity = defaultModel.createProperty("http://www.w3.org/ns/prov#Activity");
	public static Resource Entity = defaultModel.createProperty("http://www.w3.org/ns/prov#Entity");
	public static Resource Agent = defaultModel.createProperty("http://www.w3.org/ns/prov#Agent");
    
     
     
       
     
}
