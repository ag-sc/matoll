package de.citec.sc.matoll.evaluation;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import static de.citec.sc.matoll.evaluation.LexiconEvaluation.references;
import de.citec.sc.matoll.io.LexiconLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LexiconEvaluationSimple {
    
        static Set<String> references;
       
        static double recall_sum_lemma = 0.0;
        static double precision_sum_lemma = 0.0;
        
        static double recall_sum_syntactic = 0.0;
        static double precision_sum_syntactic = 0.0;
        
        static double recall_sum_mapping = 0.0;
        static double precision_sum_mapping = 0.0;

        static int total = 0;
        static int gold_total = 0;
        static int comparisons = 0;
        
	
	public static void main(String[] args) throws IOException {	
		
		if (args.length < 2)
		{
			System.out.print("Usage: LexiconEvaluation <Lexicon.rdf> <GoldLexicon.rdf> (--onlyReferences <FileWithReferences>)?\n");
			return;
		}
				
		if (args.length > 2)
		{
			if (args[2].equals("--onlyReferences"))
			{
				String referenceFile = args[3];
				readReferenceFile(referenceFile);
			}
		}
			
		String lexiconFile = args[0];
		String goldFile = args[1];
		
		LexiconLoader loader = new LexiconLoader();
		
		Lexicon lexicon = loader.loadFromFile(lexiconFile);
		Lexicon gold = loader.loadFromFile(goldFile);
		
		evaluate(lexicon,gold);
		
	}

	private static void readReferenceFile(String referenceFile) throws IOException {
				
		BufferedReader br = new BufferedReader(new FileReader(referenceFile));
                String line;
                while ((line = br.readLine()) != null) {
                        references.add(line);
                }
        }

	public static void evaluate(Lexicon lexicon, Lexicon gold) {
            
                total = lexicon.size();
                gold_total = gold.size();
            
                double recall_sum = 0.0;
                
		for (LexicalEntry entry : lexicon.getEntries()){
                    
                    String pos = entry.getPOS();
                    pos = pos.replace("http://www.lexinfo.net/ontology/2.0/lexinfo#noun", "http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");
                    String cannonicalForm = entry.getCanonicalForm().replace("@en","");
                    Set<Reference> refs = entry.getReferences();
                    
                    boolean occursInGold = false; 
                    
                    for (LexicalEntry gold_entry : gold.getEntries()){
                        
                        String pos_gold = gold_entry.getPOS();
                        pos_gold = pos_gold.replace("http://www.lexinfo.net/ontology/2.0/lexinfo#noun", "http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");
                        String cannonicalForm_gold = gold_entry.getCanonicalForm().replace("@en","");
//                        System.out.println("cannonicalForm_gold:"+cannonicalForm_gold);
//                        System.out.println("cannonicalForm:"+cannonicalForm);
//                        System.out.println("pos_gold:"+pos_gold);
//                        System.out.println("pos:"+pos);
//                        System.out.println();
                        Set<Reference> references_gold = gold_entry.getReferences();
                        List<String> refs_gold_string = new ArrayList<String>();
                        for (Reference reference : references_gold){
                            try{
                                refs_gold_string.add(reference.getURI());
                            }
                            catch(Exception e){

                            }
                        }

                        if (pos_gold.equals(pos) && cannonicalForm_gold.equals(cannonicalForm)) {
                            
                            int counter = 0;
                            for (Reference reference : refs){
                                try{
                                    if (refs_gold_string.contains(reference.getURI())) counter +=1; occursInGold = true;
                                }
                                catch (Exception e){
                                    System.out.println("Reference missing");
                                }
                                
                            }
                            
                            if (!refs_gold_string.isEmpty()) {
                                //System.out.println((double) counter/refs_gold_string.size());
                                recall_sum += (double) counter/refs_gold_string.size();
                            }
                            
                            if (counter > 0) {
                                //evaluateSyntactic(entry,gold_entry);
                                //evaluateMapping(entry,gold_entry);
                                comparisons++;
                                break;
                            } 
                        }
                    }   
                }
                
                recall_sum_lemma = recall_sum;
                precision_sum_lemma = recall_sum;			
        }

//    private static void evaluateSyntactic(LexicalEntry entry, LexicalEntry gold_entry) {
//        int correct_behaviours = 0;
//        for(SyntacticBehaviour behave : entry.getSenseBehaviours()){
//            String frame = behave.getFrame();
//            for(SyntacticBehaviour gold_behave : gold_entry.getSenseBehaviours()){
//                // compare frames
//                if(gold_behave.getFrame().equals(frame)){
//
//                   // compare arguments (without values)
//                   Set<SyntacticArgument> args = new HashSet<SyntacticArgument>();
//                   for (SyntacticArgument arg : behave.getSynArgs()) {
//                        args.add(new SyntacticArgument(arg.getArgumentType(),"",arg.getPreposition()));
//                   }
//                   
//                   Set<SyntacticArgument> gold_args = new HashSet<SyntacticArgument>();
//                   for (SyntacticArgument gold_arg : gold_behave.getSynArgs()) {
//                        gold_args.add(new SyntacticArgument(gold_arg.getArgumentType(),"",gold_arg.getPreposition()));
//                   }
//                   
//                   if (args.equals(gold_args)) correct_behaviours += 1;                   
//                }
//            }
//                      
//        }
//        
//        double recall = 0.0;
//        double precision = 0.0;
//        if (correct_behaviours > 0) {
//            recall = (double) correct_behaviours/gold_entry.getSenseBehaviours().size();
//            precision = (double) correct_behaviours/entry.getSenseBehaviours().size();            
//        } 
//        recall_sum_syntactic += recall;
//        precision_sum_syntactic += precision;
//    }
    
    private static void evaluateMapping(LexicalEntry entry, LexicalEntry gold_entry) {

        int correctlyMappedSynBehaviours = 0;
        
        // determine variable mappings (entry -> gold_entry), based on senses
        
        Map<String,String> mapping = new HashMap<String,String>();
        
        for (Sense sense : entry.getSenseBehaviours().keySet()) {
             String reference = sense.getReference().getURI();
             for (Sense gold_sense : gold_entry.getSenseBehaviours().keySet()) {
                  if (gold_sense.getReference().getURI().equals(reference)) {
                      for (SenseArgument arg : sense.getSenseArgs()) {
                           for (SenseArgument gold_arg : gold_sense.getSenseArgs()) {
                                if (gold_arg.getArgumenType().equals(arg.getArgumenType())) {
                                    mapping.put(arg.getValue(),gold_arg.getValue());
                                }
                           }
                      }
                  }
             }
        }
        
        // check for each synBehaviour in entry, whether renamed synBehaviour is in gold
        // (if so, mapping is correct)

//        loop:
//        for (SyntacticBehaviour syn : entry.getSenseBehaviours()) {
//             SyntacticBehaviour renamed_syn = new SyntacticBehaviour();
//             renamed_syn.setFrame(syn.getFrame());
//             for (SyntacticArgument arg : syn.getSynArgs()) {
//                  if (!mapping.containsKey(arg.getValue())) {
//                      continue loop;
//                  }
//                  renamed_syn.add(new SyntacticArgument(arg.getArgumentType(),mapping.get(arg.getValue()),arg.getPreposition()));
//             }
//             if (gold_entry.getSenseBehaviours().contains(renamed_syn)) {
//                 correctlyMappedSynBehaviours += 1;
//             }
//        }

        double recall = 0.0;
        double precision = 0.0;
        if (correctlyMappedSynBehaviours > 0) {
            recall = (double) correctlyMappedSynBehaviours/gold_entry.getSenseBehaviours().size();
            precision = (double) correctlyMappedSynBehaviours/entry.getSenseBehaviours().size();
            
        }
        recall_sum_mapping += recall;
        precision_sum_mapping += precision;   
    }


	public double getFMeasure(String key) {
            
            double precision = getPrecision(key);
            double recall    = getRecall(key);
            
            if (precision == 0.0 || recall == 0.0) {
                return 0.0;
            }
            return (2*recall*precision)/(recall+precision);	
	}
	
        public void setReferences(Set<Reference> set) {
		this.references = references;
		
	}
	public double getPrecision(String key) {
                        
            if (key.equals("lemma"))     return precision_sum_lemma/total;
            if (key.equals("syntactic")) return precision_sum_syntactic/comparisons;
            if (key.equals("mapping"))   return precision_sum_mapping/comparisons;          
            return 0.0;
	}
	
	public double getRecall(String key) {
//            System.out.println("##########");
//            System.out.println("recall_sum_lemma:"+recall_sum_lemma);
//            System.out.println("gold_total:"+gold_total);
//            System.out.println("comparisons:"+comparisons);
//            System.out.println("##########");
            if (key.equals("lemma"))     return recall_sum_lemma/gold_total;            
            if (key.equals("syntactic")) return recall_sum_syntactic/comparisons;
            if (key.equals("apping"))    return recall_sum_mapping/comparisons;
            return 0.0;	
	}
        
}
