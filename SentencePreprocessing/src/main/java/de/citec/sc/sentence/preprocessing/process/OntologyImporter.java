package de.citec.sc.sentence.preprocessing.process;

import java.util.HashSet;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OntologyImporter {
	
	final Model ontology_model = ModelFactory.createDefaultModel();
	
	public OntologyImporter(String path_to_ontology, String type){
		/*
		 * type could be for example "RDF/XML" or TURTLE
		 */
		ontology_model.read(path_to_ontology,type);
	}
	
	public HashSet<String> getProperties(){
		HashSet<String> properties = new HashSet<String>();
		
		QueryExecution qExec = QueryExecutionFactory.create("SELECT ?p WHERE{?p ?s ?o.}", ontology_model) ;
	    ResultSet rs = qExec.execSelect() ;
	    try {
	    	while ( rs.hasNext() ) {
	        	 QuerySolution qs = rs.next();
	        	 try{
	        		 String uri = qs.get("?p").toString();
	        		 String[] tmp = uri.split("/");
	        		 if(Character.isLowerCase(tmp[tmp.length-1].charAt(0))){
		        		 properties.add(uri);
	        		 }
	        		
	        	 }
	        	 catch(Exception e){
	        		 e.printStackTrace();
	        	 }
	    	}
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    qExec.close() ;
		
		
		
		return properties;
		
		
	}
	
	
	
	public HashSet<String> getClasses(){
		HashSet<String> properties = new HashSet<String>();
		
		QueryExecution qExec = QueryExecutionFactory.create("SELECT ?p WHERE{?p ?s ?o.}", ontology_model) ;
	    ResultSet rs = qExec.execSelect() ;
	    try {
	    	while ( rs.hasNext() ) {
	        	 QuerySolution qs = rs.next();
	        	 try{
	        		 String uri = qs.get("?p").toString();
	        		 String[] tmp = uri.split("/");
	        		 if(Character.isUpperCase(tmp[tmp.length-1].charAt(0))){
		        		 properties.add(uri);
	        		 }
	        		
	        	 }
	        	 catch(Exception e){
	        		 e.printStackTrace();
	        	 }
	    	}
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	    qExec.close() ;
		
		
		
		return properties;
		
		
	}
	
	

}
