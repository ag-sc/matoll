package de.citec.sc.matoll.coreference;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author cunger
 */
public class RelativeClauses_en implements CorefResolver {
    
    String sparql = "SELECT ?noun ?pron WHERE { "
            + " { ?v <conll:deprel> \"rcmod\" . }"
            + " UNION "
            + " { ?v <conll:deprel> \"relcl\" . } "
            + " ?v <conll:head> ?noun . "
            + " ?pron <conll:deprel> ?deprel ."
            + " FILTER regex(?deprel,\"subj\",\"i\") "
            + " ?pron <conll:head> ?v . "
            + " }";

    public Set<Set<RDFNode>> getCoreferenceSets(Model model) {
                
        Set<Set<RDFNode>> coreferenceSets = new HashSet<Set<RDFNode>>();
        
        Query query = QueryFactory.create(sparql);
        QueryExecution qe = QueryExecutionFactory.create(query,model);
        ResultSet results = qe.execSelect();

        while (results.hasNext()) {

            Set<RDFNode> coreferenceSet = new HashSet<RDFNode>();
            
            QuerySolution solution = results.nextSolution();           
            coreferenceSet.add(solution.get("noun"));
            coreferenceSet.add(solution.get("pron"));
            
            coreferenceSets.add(coreferenceSet);
    }
        
        return coreferenceSets;
    }
    
}
