package de.citec.sc.matoll.coreference;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ResourceFactory;

import de.citec.sc.matoll.core.Language;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author cunger
 */
public class Coreference {
    
    public void computeCoreference(Model model, Language language) throws Exception {
    
        Set<Set<RDFNode>> coreferenceSets = new HashSet<Set<RDFNode>>();
        
        // 1. Select corefering tokens
        
        // Relative clauses 
        RelativeClauses relcl = new RelativeClauses(language);
        
        coreferenceSets.addAll(relcl.computeCoreference(model));
        
        // Anaphora 
        
        // ... 
        
        // 2. Copy senseArg annotations between them
        
        for (Set<RDFNode> coreferenceSet : coreferenceSets) {

             Set<RDFNode> senseArgs = new HashSet<RDFNode>();

             for (RDFNode token : coreferenceSet) {
                  String sparql = "SELECT ?senseArg WHERE { "
                         + "<"+token.toString()+">" + " <own:senseArg> ?senseArg . "
                         + " }";
                                    
                  Query query = QueryFactory.create(sparql);
                  QueryExecution qe = QueryExecutionFactory.create(query,model);
                  ResultSet results = qe.execSelect();

                  while (results.hasNext()) {
           
                    QuerySolution solution = results.nextSolution();
                    senseArgs.add(solution.get("senseArg"));
                  }
             }
             
             for (RDFNode token : coreferenceSet) {
                for (RDFNode senseArg : senseArgs) {
                     Statement s = ResourceFactory.createStatement(token.asResource(), model.createProperty("<own:senseArg>"), senseArg);
                     if (!model.contains(s)) { 
                          model.add(s);
                     }
                }
             }    
        }
    }
    
}
