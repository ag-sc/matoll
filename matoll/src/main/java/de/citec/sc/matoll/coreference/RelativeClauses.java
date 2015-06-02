package de.citec.sc.matoll.coreference;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.util.Set;

/**
 *
 * @author cunger
 */
public class RelativeClauses { 
   
    CorefResolver coref;
    
    public RelativeClauses(String language) {
                  
        switch (language) {
            case "EN": coref = new RelativeClauses_en();
                       break;
            case "DE": coref = new RelativeClauses_de();
                       break;
            case "ES": coref = new RelativeClauses_es();
                       break;
            case "JP": coref = new RelativeClauses_jp();
                       break;
        }

    }
    
    public Set<Set<RDFNode>> computeCoreference(Model model) {
 
        return coref.getCoreferenceSets(model);
        
    }
}
