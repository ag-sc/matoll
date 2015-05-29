
import com.hp.hpl.jena.rdf.model.Model;
import de.citec.sc.matoll.coreference.RelativeClauses;
import org.apache.jena.riot.RDFDataMgr;

/**
 *
 * @author cunger
 */
public class CoreferenceTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        RelativeClauses coref = new RelativeClauses();
        
        String f = "matoll/src/test/resources/coref/coref_en_ex1.ttl";
        
        Model model = RDFDataMgr.loadModel(f);
        coref.computeCoreference(model,"en");
        
    }
    
}
