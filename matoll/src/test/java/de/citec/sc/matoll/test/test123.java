package de.citec.sc.matoll.test;


import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.io.LexiconSerialization;
import de.citec.sc.matoll.io._LexiconLoaderGreaterK_;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author swalter
 */
public class test123 {
    public static void main(String[] args) throws FileNotFoundException {
		// This test class checks if the equals method works...
		
                Lexicon lexicon = new Lexicon();
		_LexiconLoaderGreaterK_ loader = new _LexiconLoaderGreaterK_(1);
		
                lexicon = loader.loadFromFile("/Users/swalter/Downloads/new_adjectives.ttl");
                
                System.out.println("#entries:"+lexicon.size());
                
                Model model = ModelFactory.createDefaultModel();
		
		LexiconSerialization serializer = new LexiconSerialization(false);
		
		serializer.serialize(lexicon, model);
		
		FileOutputStream out = new FileOutputStream(new File("new_adjectives_reduced.ttl"));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;

			
		
		
	}
}
