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
    
    public RelativeClauses(String language) throws Exception {
                  
        if      (language.toLowerCase().equals("en")) coref = new RelativeClauses_en();
        else if (language.toLowerCase().equals("de")) coref = new RelativeClauses_de();
        else if (language.toLowerCase().equals("es")) coref = new RelativeClauses_es();
        else if (language.toLowerCase().equals("ja")) coref = new RelativeClauses_ja();
        else throw new Exception("Language is '" + language + "' and RelativeClauses doesn't know it.");
    }
    
    public Set<Set<RDFNode>> computeCoreference(Model model) {
 
        return coref.getCoreferenceSets(model);
        
    }
}
