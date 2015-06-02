package de.citec.sc.matoll.coreference;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.util.Set;

/**
 *
 * @author cunger
 */
public interface CorefResolver {
    
    public Set<Set<RDFNode>> getCoreferenceSets(Model model);
    
}
