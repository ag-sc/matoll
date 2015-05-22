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
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.io.LexiconLoader;

public class LexiconEvaluationSimple {

	static Set<String> references;
	
	static boolean onlyGoldReferences = false;
	static boolean filterReferences = false;
        
        static double recall_lemma = 0.0;
        static double precision_lemma = 0.0;
        
        static double recall_syntactic = 0.0;
        static double precision_syntactic = 0.0;

        static double syntactic_counter = 0;
	

	
	public static void main(String[] args) throws IOException {
		
		
		if (args.length < 2)
		{
			System.out.print("Usage: LexiconEvaluation <Lexicon.rdf> <GoldLexicon.rdf> (--onlyGoldReferences|<FileWithReferences>)?\n");
			return;
		}
		
		String referenceFile = null;
		
		if (args.length >2)
		{
			if (args[2].equals("--onlyGoldReferences"))
			{
				onlyGoldReferences = true;
			}
			else
			{
				referenceFile = args[2];
				filterReferences = true;
				references = readReferenceFile(referenceFile);
			}
		}
		
		
		
		String lexiconFile = args[0];
		String goldFile = args[1];
		
		LexiconLoader loader = new LexiconLoader();
		
		Lexicon lexicon = loader.loadFromFile(lexiconFile);
		Lexicon gold = loader.loadFromFile(goldFile);
		
		evaluate(lexicon,gold);
		
	}

	private static Set<String> readReferenceFile(String referenceFile) throws IOException {
		
		Set<String> references = new HashSet<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(referenceFile));
        String line;
        while((line = br.readLine()) != null) {
             references.add(line);
        }
        return references;
	}

	public static void evaluate(Lexicon lexicon, Lexicon gold) {
                int correct_entries = 0;
                double recall_sum = 0.0;
                
		for (LexicalEntry entry : lexicon.getEntries()){
                    String pos = entry.getPOS();
                    String cannonicalForm = entry.getCanonicalForm();
                    Set<Reference> references = entry.getReferences();
                    for (LexicalEntry gold_entry : gold.getEntries()){
                        String pos_gold = gold_entry.getPOS();
                        String cannonicalForm_gold = gold_entry.getCanonicalForm();
                        Set<Reference> references_gold = gold_entry.getReferences();
                        /*
                        What if references_gold.isEmty()?
                        */
                        if(pos_gold.equals(pos)&&cannonicalForm_gold.equals(cannonicalForm)){
                            
                            int counter = 0;
                            for(Reference reference:references){
                                if(references_gold.contains(reference)) counter +=1;
                            }
                            recall_sum += (double) counter/references_gold.size();
                            
                            
                            
                            /*
                            Mapping and SynBehaviour
                            */
                            if(counter>0){
                                //evaluateMapping(entry,gold_entry);
                                evaluateSyntactic(entry,gold_entry);
                            }
                            
                        }
                    }
                    
                }
                
                recall_lemma = recall_sum/gold.size();
                precision_lemma = recall_sum/lexicon.size();

			
        }

//    private static void evaluateMapping(LexicalEntry entry, LexicalEntry gold_entry) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    private static void evaluateSyntactic(LexicalEntry entry, LexicalEntry gold_entry) {
        int correct_behaviours = 0;
        for(SyntacticBehaviour behave : entry.getBehaviours()){
            String frame = behave.getFrame();
            for(SyntacticBehaviour gold_behave : gold_entry.getBehaviours()){
                // compare frames
                if(gold_behave.getFrame().equals(frame)){

                   // compare arguments (without values)
                   Set<SyntacticArgument> args = new HashSet<SyntacticArgument>();
                   for (SyntacticArgument arg : behave.getSynArgs()) {
                        args.add(new SyntacticArgument(arg.getArgumentType(),"",arg.getPreposition()));
                   }
                   
                   Set<SyntacticArgument> gold_args = new HashSet<SyntacticArgument>();
                   for (SyntacticArgument gold_arg : gold_behave.getSynArgs()) {
                        gold_args.add(new SyntacticArgument(gold_arg.getArgumentType(),"",gold_arg.getPreposition()));
                   }
                   
                   if (args.equals(gold_args)) correct_behaviours += 1;
                    
                }
            }
                    
        }
        double recall = 0.0;
        double precision = 0.0;
        if(correct_behaviours>0){
            recall = (double)correct_behaviours/gold_entry.getBehaviours().size();
        
            precision = (double)correct_behaviours/entry.getBehaviours().size();
            syntactic_counter+=1;
        }
        
        recall_syntactic += recall;
        precision_syntactic += precision;
        
    }
    
    

	public double getFMeasure(String key) {
            if(key.equals("lemma")) {
                 return (2*recall_lemma*precision_lemma)/(precision_lemma+recall_lemma);
            }
            
            if(key.equals("syntactic")) {
                 return (2*recall_syntactic/syntactic_counter*precision_syntactic/syntactic_counter)/(precision_syntactic/syntactic_counter+recall_syntactic/syntactic_counter);
            }
            
            
            
            return 0.0;
		
	}
	
	public double getPrecision(String key) {
            if(key.equals("lemma")) return precision_lemma;
            if(key.equals("syntactic")) {
                System.out.println("syntactic_counter:"+syntactic_counter);
                System.out.println("precision_syntactic:"+precision_syntactic);
                return precision_syntactic/syntactic_counter;
            }
            
            return 0.0;
		
	}
	
	public double getRecall(String key) {
            if(key.equals("lemma")) return recall_lemma;
            
            if(key.equals("syntactic")) return recall_syntactic/syntactic_counter;
            
            return 0.0;
		
	}
        

	
}
