
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.coreference.Coreference;


/**
 *
 * @author cunger
 */
public class CoreferenceTest {

    Model model; 
    Language lang;
        
    public CoreferenceTest(Language language) {
        
        lang  = language;        
    }
    
    public void loadTestFile(String filename) {

        model = ModelFactory.createDefaultModel();
        model.read(this.getClass().getResourceAsStream(filename),null,"TTL");
    }
    
    public void run() throws Exception {
        
        Coreference coref = new Coreference();
        
        System.out.println("Old model (" + model.size() + "):\n" + model.toString());
        
        coref.computeCoreference(model,lang);
        
        System.out.println("New model (" + model.size() + "):\n" + model.toString());
    }
    
}