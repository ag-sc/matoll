
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.io.LexiconLoader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author swalter
 */
public class TestLexiconLoader {
    
    public static void main(String[] args){
        LexiconLoader loader = new LexiconLoader();
        
        Lexicon lexicon = loader.loadFromFile("spouse_beforeTraining.ttl");
        
        lexicon.getPrepositions().stream().forEach(System.out::println);
        
    }
    
}
