package de.citec.sc.matoll.coreference;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

import de.citec.sc.lemon.core.Language;
import java.util.HashSet;
import java.util.Set;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

/**
 *
 * @author cunger
 */
public class RelativeClauses { 
   
    String sparql;
    
    public RelativeClauses(Language language) throws Exception {
                  
        switch (language) {
            
            case EN: 
                
                sparql = "SELECT ?noun ?pron WHERE { "
                       + " { ?v <conll:deprel> \"rcmod\" . }"
                       + " UNION "
                       + " { ?v <conll:deprel> \"relcl\" . } "
                       + " ?v <conll:head> ?noun . "
                       + " ?pron <conll:deprel> ?deprel ."
                       + " FILTER regex(?deprel,\"(subj)|(obj)\",\"i\") "
                       + " ?pron <conll:head> ?v . "
                       + " }";
                break;
                
            case DE: 
                
                sparql = "SELECT ?noun ?pron WHERE { "
                       + " ?v <conll:deprel> \"rel\" . "
                       + " ?v <conll:head> ?noun . "
                       + " ?pron <conll:deprel> ?deprel ."
                       + " FILTER regex(?deprel,\"(subj)|(obj)\",\"i\") "
                       + " ?pron <conll:head> ?v . "
                       + " }";           
                break;
                
            case ES: 
                
                sparql = "SELECT ?noun ?pron WHERE { "
                       + " ?v <conll:deprel> \"MOD\" ."
                       + " ?v <conll:head> ?noun . "
                       + " ?pron <conll:deprel> ?deprel ."
                       + " FILTER regex(?deprel,\"(SUBJ)|(DO)\",\"i\") "
                       + " ?pron <conll:head> ?v . " 
                       + " ?v <conll:postag> \"VMII3S0\" ." // TODO or cpostag? 
                       + " }";           
                break;
                
            default: 
                
                sparql = null;            
        }
    }
    
    public Set<Set<RDFNode>> computeCoreference(Model model) {
                
        Set<Set<RDFNode>> coreferenceSets = new HashSet<>();
        
        if (sparql == null) return coreferenceSets;
        
        Query query = QueryFactory.create(sparql);
        QueryExecution qe = QueryExecutionFactory.create(query,model);
        ResultSet results = qe.execSelect();

        while (results.hasNext()) {

            Set<RDFNode> coreferenceSet = new HashSet<>();
            
            QuerySolution solution = results.nextSolution();           
            coreferenceSet.add(solution.get("noun"));
            coreferenceSet.add(solution.get("pron"));
            
            coreferenceSets.add(coreferenceSet);
        }
        
        return coreferenceSets;
    }
}
