/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resourceTest;

import de.citec.sc.sentence.preprocessing.lucene.ReadIndex;
import de.citec.sc.sentence.preprocessing.process.Language;
import de.citec.sc.sentence.preprocessing.process.OntologyImporter;
import de.citec.sc.sentence.preprocessing.sparql.Resources;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author swalter
 */
public class Test1 {
    
    public static void main(String[] args) throws IOException{
//        String input = "\"125px\"^^<http://www.w3.org/2001/XMLSchema#string>";
//        System.out.println(Resources.cleanEntity(input).equals("125px"));
//        
//        input = "\"110px|center|alt=Oval shaped shoulder patch with a deep blue background. At the top is a red circle and blue star, the patch of the Army Service Forces. It is surrounded by a white oval, representing a mushroom cloud. Below it is a white lightning bolt cracking a yellow circle, representing an atom.\"^^<http://www.w3.org/2001/XMLSchema#string>";
//        System.out.println(Resources.cleanEntity(input).equals("110px"));
//        
//        
//        input = "-60.0";
//        System.out.println(Resources.cleanEntity(input).equals("-60"));
//        
//        input = "GV (Apr 1939 - Sep 1939)\"^^<http://www.w3.org/2001/XMLSchema#string>";
//        System.out.println(Resources.cleanEntity(input).equals("GV"));
//        
//        input = "\"ASU Chilliwack\"^^<http://www.w3.org/2001/XMLSchema#string>";
//        System.out.println(Resources.cleanEntity(input).equals("ASU Chilliwack"));
//        
//        
//        input = "1963-05-18+02:00^^http://www.w3.org/2001/XMLSchema#date";
//        System.out.println(Resources.cleanEntity(input).equals("1963"));
//        
//        input = "\"1963-asd\"^^<http://dbpedia.org/datatype/";
//        System.out.println(Resources.cleanEntity(input));
//        
//        input = "\"1963[/dbpedia.org/datatype/";
//        System.out.println(Resources.cleanEntity(input));
//        
//        OntologyImporter onto_import = new OntologyImporter("../dbpedia_2014.owl","RDF/XML");
//        System.out.println(onto_import.getProperties().size());
        
        ReadIndex index = new ReadIndex("/Users/swalter/Index/GermanIndexReduced",Language.DE);
        List<List<String>> input = new ArrayList<>();
        
        List<String> one = new ArrayList<>();
        one.add("Michelle Obama");
        one.add("Barack Obama");
        input.add(one);
        for(List<String> bla: index.search(input)){
            System.out.println(bla.get(0));
            //System.out.println(bla.get(1));
            //System.out.println(bla.get(2));
        }
        
    }
}
