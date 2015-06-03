package de.citec.sc.matoll.coreference;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import de.citec.sc.matoll.core.Language;
import java.util.Set;

/**
 *
 * @author cunger
 */
public class RelativeClauses { 
   
    CorefResolver coref;
    
    public RelativeClauses(Language language) throws Exception {
                  
        switch (language) {
            case EN: coref = new RelativeClauses_en(); break;
            case DE: coref = new RelativeClauses_de(); break;
            case ES: coref = new RelativeClauses_es(); break;
            case JA: coref = new RelativeClauses_ja(); break;
        }
    }
    
    public Set<Set<RDFNode>> computeCoreference(Model model) {
 
        return coref.getCoreferenceSets(model);
        
    }
}
