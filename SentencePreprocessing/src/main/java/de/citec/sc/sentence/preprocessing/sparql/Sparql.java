package de.citec.sc.sentence.preprocessing.sparql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class Sparql {

	public static List<String> retrieveEntities(String endpoint, String uri, String language){
		List<String> entities = new ArrayList<String>();
		/*
		 * If label is given, this is the case for the dbpedia object property
		 */
		entities.addAll(getValues(endpoint, getQueryLabel(language,uri),language,uri));
		
		/*
		 * if no label is found try to get everything from the right side, e.g. if datatype property is given
		 */
		if (entities.size()==0){
			entities.addAll(getValues(endpoint, getQueryData(language,uri),language,uri));
		}
		return entities;
		
	}
	
	private static String getQueryLabel(String language, String uri){
            
                String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "SELECT DISTINCT ?y ?subj ?obj ?x WHERE {"
				+ "?y <"+uri+"> ?x." 
				+ "{?y rdfs:label ?subj. FILTER (lang(?subj) = '"+language+"')} UNION" 
                                + "{?y rdfs:label ?subj. FILTER (lang(?subj) = 'en')}" 
                                + "{?x rdfs:label ?obj. FILTER (lang(?obj) = '"+language+"')} UNION" 
                                + "{?x rdfs:label ?obj. FILTER (lang(?obj) = 'en')}" 
				+ "}";
                
		return query;
	}
	
	private static String getQueryData(String language, String uri){

                String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "SELECT DISTINCT ?y ?subj ?obj WHERE "
				+ "{?y <"+uri+"> ?obj. "
                                + "{?y rdfs:label ?subj. FILTER (lang(?subj) = '"+language+"')} UNION" 
                                + "{?y rdfs:label ?subj. FILTER (lang(?subj) = 'en')}" 
				+ "}";
		return query;
	}
	
	private static List<String> getValues(String endpoint, String queryString, String language, String uri){
		//System.out.println(queryString);
                int number_entityPairs = getNumberEntityPairs(uri,endpoint);
                
                List<String> entities = new ArrayList<String>();
                if(number_entityPairs<10000)
                    entities.addAll(executeQuery(queryString,endpoint,language));
                else{
                    int limit = 2000;
                    int offset = 0;
                    for(int i = 0; i<number_entityPairs/2000;i++){
                        offset = i*2000;
                        String tmp_query = queryString+" LIMIT "+limit+" OFFSET "+offset;
                        entities.addAll(executeQuery(tmp_query,endpoint,language));
                    }
                    if((offset+limit)<number_entityPairs){
                        String tmp_query = queryString+" OFFSET "+offset;
                        entities.addAll(executeQuery(tmp_query,endpoint,language));
                    }
                }
		
		return entities;
	}

    private static int getNumberEntityPairs(String property, String endpoint) {
        int numbers = 0;
        String queryString = "SELECT (COUNT(DISTINCT *) as ?count) WHERE{?x <"+property+"> ?y}";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);
        ResultSet results = qexec.execSelect();
        while ( results.hasNext() ) {
             QuerySolution qs = results.next();
             numbers = Integer.valueOf(qs.get("?count").toString().replace("^^http://www.w3.org/2001/XMLSchema#integer",""));
        }
        return numbers;
    }

    private static List<String> executeQuery(String queryString, String endpoint, String language) {
        System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        List<String> entities = new ArrayList<String>();
        QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);
        try {
            ResultSet results = qexec.execSelect();
            while ( results.hasNext() ) {
                 QuerySolution qs = results.next();
                 try{
                                String subj = qs.get("?subj").toString();
                                String subj_uri = qs.get("?y").toString();
                                String obj_uri = "";
                                try{
                                        obj_uri = qs.get("?x").toString();
                                }
                                catch(Exception e){
                                        obj_uri = qs.get("?obj").toString();
                                }
                        String obj = qs.get("?obj").toString();
                        String entityPair = subj_uri+"\t"+subj+"\t"+obj+"\t"+obj_uri;
                        entityPair = entityPair.replace("@en", "");
                        entityPair = entityPair.replace("@"+language, "");
                        //System.out.println("entityPair:"+entityPair);
                        entities.add(entityPair);
                 }
                 catch(Exception e){
                        e.printStackTrace();
                                 //ignore those without Frequency TODO:Check Source of Error
                 }
            }
        }
         catch(Exception e){
        e.printStackTrace();
         //ignore those without Frequency TODO:Check Source of Error
         }

        return entities;
    }
	
}
