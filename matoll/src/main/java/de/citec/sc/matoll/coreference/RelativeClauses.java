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
                  
        if(language.equals("EN")) coref = new RelativeClauses_en();
        else if(language.equals("DE")) coref = new RelativeClauses_de();
        else if(language.equals("ES")) coref = new RelativeClauses_es();
        else if(language.equals("JA")) coref = new RelativeClauses_ja();

    }
    
    public Set<Set<RDFNode>> computeCoreference(Model model) {
 
        return coref.getCoreferenceSets(model);
        
    }
}
