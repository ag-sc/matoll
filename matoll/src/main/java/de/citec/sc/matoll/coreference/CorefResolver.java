package de.citec.sc.matoll.coreference;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import java.util.Set;

/**
 *
 * @author cunger
 */
public interface CorefResolver {
    
    public Set<Set<RDFNode>> getCoreferenceSets(Model model);
    
}
