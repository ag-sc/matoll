/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resourceTest;

import de.citec.sc.sentence.preprocessing.sparql.Resources;

/**
 *
 * @author swalter
 */
public class Test1 {
    
    public static void main(String[] args){
        String input = "\"125px\"^^<http://www.w3.org/2001/XMLSchema#string>";
        System.out.println(Resources.cleanEntity(input).equals("125px"));
        
        input = "\"110px|center|alt=Oval shaped shoulder patch with a deep blue background. At the top is a red circle and blue star, the patch of the Army Service Forces. It is surrounded by a white oval, representing a mushroom cloud. Below it is a white lightning bolt cracking a yellow circle, representing an atom.\"^^<http://www.w3.org/2001/XMLSchema#string>";
        System.out.println(Resources.cleanEntity(input).equals("110px"));
        
        
        input = "-60.0";
        System.out.println(Resources.cleanEntity(input).equals("-60"));
        
        input = "GV (Apr 1939 - Sep 1939)\"^^<http://www.w3.org/2001/XMLSchema#string>";
        System.out.println(Resources.cleanEntity(input).equals("GV"));
        
        input = "\"ASU Chilliwack\"^^<http://www.w3.org/2001/XMLSchema#string>";
        System.out.println(Resources.cleanEntity(input).equals("ASU Chilliwack"));
        
        
        input = "1963-05-18+02:00^^http://www.w3.org/2001/XMLSchema#date";
        System.out.println(Resources.cleanEntity(input).equals("1963"));
        
        input = "\"1963-asd\"^^<http://dbpedia.org/datatype/";
        System.out.println(Resources.cleanEntity(input));
        
        input = "\"1963[/dbpedia.org/datatype/";
        System.out.println(Resources.cleanEntity(input));
        
    }
}
