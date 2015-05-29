package de.citec.sc.matoll.coreference;

import com.hp.hpl.jena.rdf.model.Model;

/**
 *
 * @author cunger
 */
public class RelativeClauses { // TODO implements Coreference
   
    // TODO This should be a factory?
   
    public void computeCoreference(Model model, String language) {
        
        // 1. Read SPARQL queries from file: "RelativeClause_" + language + ".sparql"
        
        // 2. Execute each query against model and copy senseArgs between corefering tokens
        
    }
}
