
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.io.LexiconSerialization;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
public class TestLexiconLoader {
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        LexiconLoader loader = new LexiconLoader();
        
        Lexicon lexicon = loader.loadFromFile("spouse.ttl");
        
        System.out.println("Loaded "+lexicon.size()+" entries");
        for(LexicalEntry entry : lexicon.getEntries()){
            for(Sense sense: entry.getSenseBehaviours().keySet()){
                Provenance provenance = entry.getProvenance(sense);
                for (Sentence s: provenance.getSentences()) {
                    System.out.println(s.getSentence());
                    System.out.println(s.getSubjOfProp());
                    System.out.println(s.getObjOfProp());
                    System.out.println();
                }
            }
            System.out.println(entry.toString());
            System.out.println("#################");
            System.out.println();
        }
        
        LexiconSerialization serial = new LexiconSerialization(true);
        Model model = ModelFactory.createDefaultModel();
        serial.serialize(lexicon, model);		
        FileOutputStream out = new FileOutputStream(new File("spouse_new.ttl"));
        RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
        out.close();
        
    }
    
    
}
