package de.citec.sc.matoll.evaluation;
import java.util.HashMap;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Reference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VerySimpleLexiconEvaluation {
    


	public static List<Double> evaluate(Lexicon automatic, Lexicon gold, boolean onlyProperties) {
            
            Map<String,List<List<String>>> hm_automatic = new HashMap<>();
            Map<String,List<List<String>>> hm_gold = new HashMap<>();
            
            /*
            First create map with reference as key and an array with pos,form and ref for each sese/ref in each entry
            */
            createMap(automatic,hm_automatic,onlyProperties);
            createMap(gold,hm_gold,onlyProperties);
//            System.out.println("gold");
//            for(String key : hm_gold.keySet()){
//                System.out.println(hm_gold.get(key));
//            }
//            
//            System.out.println("\n\n");
//            System.out.println("automatic");
//            for(String key : hm_automatic.keySet()){
//                System.out.println(hm_automatic.get(key));
//            }
//            System.out.println("\n\n");
            
            /*
            Then do evaluation based on the map
            */
            return doSimpleEvaluation(hm_automatic,hm_gold);

            
        }
        

        
        private static void createMap(Lexicon lexicon, Map<String,List<List<String>>> hm, boolean onlyProperties){
            for(LexicalEntry entry : lexicon.getEntries()){
                String pos = entry.getPOS();
                String form = entry.getCanonicalForm();
                for( Reference ref:entry.getReferences()){
                    try{
                        if(onlyProperties){
                            String uri = ref.getURI();
                            String tmp_uri = uri.replace("http://dbpedia.org/", "");
                            if(!Character.isUpperCase(tmp_uri.charAt(0))){
                                List<String>tmp = new ArrayList<>();
                                tmp.add(pos);
                                tmp.add(form);
                                tmp.add(uri);
                                if(hm.containsKey(uri)){
                                    List<List<String>> tmp_list = hm.get(uri);
                                    tmp_list.add(tmp);
                                    hm.put(uri, tmp_list);
                                }
                                else{
                                    List<List<String>> tmp_list = new ArrayList<>();
                                    tmp_list.add(tmp);
                                    hm.put(uri, tmp_list);
                                }
                            }
                        }
                        else{
                            String uri = ref.getURI();
                            List<String>tmp = new ArrayList<>();
                            tmp.add(pos);
                            tmp.add(form);
                            tmp.add(uri);
                            if(hm.containsKey(uri)){
                                List<List<String>> tmp_list = hm.get(uri);
                                tmp_list.add(tmp);
                                hm.put(uri, tmp_list);
                            }
                            else{
                                List<List<String>> tmp_list = new ArrayList<>();
                                tmp_list.add(tmp);
                                hm.put(uri, tmp_list);
                            }
                        }
                        
                    }
                    catch(Exception e){
                          /*
                        Do nothing, some of the lexicon entries do not have a reference
                        */
                        }
                    
                }
            }
        }

    private static List<Double> doSimpleEvaluation(Map<String, List<List<String>>> hm_automatic, Map<String, List<List<String>>> hm_gold) {
        /*
        Calculate Recall/Precision per Property(from gold) and average at the end over number properties gold
        */
        double recall = 0.0;
        double precision = 0.0;
        
        List<Double> overall_results = new ArrayList<Double>();

        for(String uri : hm_gold.keySet()){
            List<List<String>> entries_gold = hm_gold.get(uri);
            if(hm_automatic.containsKey(uri)){
                List<List<String>> entries_automatic = hm_automatic.get(uri);
                int correct_entries = 0;
                int lenght_gold = entries_gold.size();
                int lenght_automatic = entries_automatic.size();
                for(List<String> entry_gold:entries_gold){
                    for(List<String> entry_automatic:entries_automatic){
                        if(entry_gold.get(0).equals(entry_automatic.get(0)) 
                                && entry_gold.get(1).equals(entry_automatic.get(1)) 
                                && entry_gold.get(2).equals(entry_automatic.get(2))){
                            correct_entries+=1;
                            break;
                        }
                    }
                }
//                System.out.println("URI:"+uri);
                double local_recall = (correct_entries+0.0)/lenght_gold;
                double local_precision = (correct_entries+0.0)/lenght_automatic;
//                System.out.println("lenght_gold:"+lenght_gold);
//                System.out.println("lenght_automatic:"+lenght_automatic);
//                System.out.println("correct_entries:"+correct_entries);
//                System.out.println("local_recall:"+local_recall);
//                System.out.println("local_precision:"+local_precision);
                recall+=local_recall;
                precision+=local_precision;

            }
            else{
                recall+=0.0;
                /*
                if no entry for a uri from gold is found in automatic lexicon, set precision to 1.0
                */
//                System.out.println("URI:"+uri);
//                System.out.println("local_recall:0.0");
//                System.out.println("local_precision:1.0");
                precision+=1.0;
            }
//            System.out.println("\n\n");
        }
        Double global_recall=roundDown3(recall/hm_gold.size());
        Double global_precision=roundDown3(precision/hm_gold.size());
        overall_results.add(global_precision);
        overall_results.add(global_recall);
        overall_results.add(roundDown3((2*global_recall*global_precision)/(global_recall+global_precision)));
        
        return overall_results;
    }
    
    public static double roundDown3(double d) {
        return (long) (d * 1e3) / 1e3;
    }
        
}
