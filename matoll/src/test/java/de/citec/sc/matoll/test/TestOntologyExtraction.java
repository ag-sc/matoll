package de.citec.sc.matoll.test;

import de.citec.sc.matoll.utils.OntologyImporter;


public class TestOntologyExtraction {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String path = "/Users/swalter/Downloads/dbpedia_2014.owl";
		
		OntologyImporter importer = new OntologyImporter(path,"RDF/XML");
		
		for(String x: importer.getProperties()) System.out.println(x);
		
		for(String x: importer.getClasses()) System.out.println(x);
	}

}
