
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import de.citec.sc.matoll.coreference.Coreference;
import de.citec.sc.matoll.coreference.RelativeClauses;
import java.util.Set;


/**
 *
 * @author cunger
 */
public class CoreferenceTest {

    Model model; 
    String lang;
        
    public CoreferenceTest(String language) {
        
        lang  = language;        
    }
    
    public void loadTestFile(String filename) {

        model = ModelFactory.createDefaultModel();
        model.read(this.getClass().getResourceAsStream(filename),null,"TTL");
    }
    
    public void run() {
        
        Coreference coref = new Coreference();
        
        coref.computeCoreference(model,lang);

        System.out.println(model.toString());
    }
    
}
