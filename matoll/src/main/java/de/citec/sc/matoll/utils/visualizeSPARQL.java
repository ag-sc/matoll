/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jgrapht.*;
import org.jgrapht.graph.*;

/**
 *
 * @author swalter
 */
public class visualizeSPARQL {
    private static int variable_counter = 0;
    public static void main(String[] args){
        String query = "SELECT ?lemma ?prefix ?e1_arg ?e2_arg ?prep WHERE"
				+"{ "
				+"{ ?y <conll:cpostag> \"NN\" . } "
				+ " UNION "
				+"{ ?y <conll:cpostag> \"NNS\" . } "
				+ " UNION "
				+"{ ?y <conll:cpostag> \"NNP\" . } "
				+ "?y <conll:form> ?lemma . "
				+"OPTIONAL{"
				+ "?lemma_nn <conll:head> ?y. "
				+ "?lemma_nn <conll:form> ?prefix. "
				+ "?lemma_nn <conll:deprel> \"nn\"."
				+"} "
				+ "?verb <conll:head> ?y . "
				+ "?verb <conll:deprel> \"cop\" ."
				+ "?e1 <conll:head> ?y . "
				+ "?e1 <conll:deprel> \"nsubj\" . "
				+ "?p <conll:head> ?y . "
				+ "?p <conll:deprel> \"prep\" . "
				+ "?p <conll:form> ?prep . "
				+ "?e2 <conll:head> ?p . "
				+ "?e2 <conll:deprel> \"pobj\" . "
				+ "?e1 <own:senseArg> ?e1_arg. "
				+ "?e2 <own:senseArg> ?e2_arg. "
				+ "}";
        
        System.out.println("Starting visualisation");
        System.out.println(doVisual(query,"Patternx"));
        
    }
    
    
    public static String doVisual(String query,String name){
        
        String prefix = "\\begin{figure}\n"
                    +"\\centering\n"
                    +"\\begin{tikzpicture}\n";
        
        String suffix = "\\end{tikzpicture}\n"
                    +"\\caption{Visualisation for "+name+"}\n"
                    +"\\label{fig:"+name+"}\n"
                    +"\\end{figure}";
        
        /*
        Split into parts; each triple is seperated with a .
        */
        String[] parts = query.split("\\.");        

        List<List<String>> relations = new ArrayList<>();
        Map<String,Integer> variables = new HashMap<>();
        Map<String,String> deprel = new HashMap<>();
        Map<String,String> node_name = new HashMap<>();
        
        
        for(String s: parts){
            /*
            ignore everything which starts with optional or union (TODO: fix this)
            */
            if(s.contains("<conll:head>")&& !s.contains("OPTIONAL") && !s.contains("UNION")) {
                String value = (s.replace("}", "")).trim();
                String[] tmp = value.split("<conll:head>");
                String subj = tmp[0].trim();
                String obj = tmp[1].trim();
                variables.put(subj, 0);
                variables.put(obj, 0);
                List<String> relation = new ArrayList<>();
                relation.add(subj);
                relation.add(obj);
                relations.add(relation);
            }
            
            if(s.contains("<conll:deprel>")&& !s.contains("OPTIONAL") && !s.contains("UNION")) {
                String value = (s.replace("}", "")).trim();
                String[] tmp = value.split("<conll:deprel>");
                String subj = tmp[0].trim();
                String obj = tmp[1].trim();
                deprel.put(subj, obj.replace("\"",""));
            }
        }
        

//
        String head_variable = findRoot(relations);
        if(head_variable!=null){
            variables.put(head_variable,1);
            String latex_output = "\\node[fill = gray!40, "
                    + "shape = rectangle, rounded corners,\n"
                    +"minimum width = 2cm, font = \\sffamily] "
                    + "("+Integer.toString(variable_counter)+") {"+head_variable+"}\n";
            node_name.put(head_variable, Integer.toString(variable_counter));
            for(List<String> relation:relations){
                String subj = relation.get(0);
                if(!node_name.containsKey(subj)){
                    latex_output+= "child {node[fill = blue!40, shape = rectangle, rounded corners,\n" 
                            +"minimum width = 1cm, font = \\sffamily]  "
                            + "("+Integer.toString(variable_counter+1)+") {"+subj+"}";
                    latex_output+=recursiveLoop(subj,relations,node_name);
                }
                
            }
            
            String output = prefix+latex_output+"};\n";
            
            output+="\\begin{scope}[nodes = {draw = none}]\n";
            for(List<String> relation:relations){
                String subj = node_name.get(relation.get(0));
                String obj = node_name.get(relation.get(1));
                String dep = deprel.get(relation.get(0));
                output+= "\\path ("+subj+")     -- ("+obj+") node [near start, left]  {\\text{"+dep+"}};\n";
            }
            output+="\\draw[densely dashed, rounded corners, thin];\n";
            output+="\\end{scope} \n";
            
            
// \begin{scope}[nodes = {draw = none}]
//    \path (3)     -- (0) node [near start, left]  {\text{prep}};
//    \path (4)     -- (3) node [near start, right] {\text{nsubj}};
//    \path (1)     -- (0) node [near start, right] {\text{cop}};
//    \draw[densely dashed, rounded corners, thin];
//  \end{scope}     
            
            
            return output+suffix;
        }
        else{
            return null;
        }
    }

    /*
    find the root variable;
    that the variable, which is not on the subject side, but only on the object side
    */
    private static String findRoot(List<List<String>> relations) {
        String head = "";
        Set<String> subj_list = new HashSet<>();
        Set<String> obj_list = new HashSet<>();
        relations.stream().map((list) -> {
            subj_list.add(list.get(0));
            return list;
        }).forEach((list) -> {
            obj_list.add(list.get(1));
        });
        obj_list.removeAll(subj_list);
        if(obj_list.size()==1){
            return obj_list.toString().replace("[","").replace("]","");
        }
        else{
            return null;
        }
    }

    public static String recursiveLoop(String subj,List<List<String>>relations,Map<String,String> node_name){
        variable_counter+=1;
        node_name.put(subj, Integer.toString(variable_counter));
        String output = "";
        boolean go_on = false;
        for(List<String> r: relations){
            String obj = r.get(1);
            if(subj.equals(obj)){
                String new_subj = r.get(0);
                output+="child {node[fill = blue!40, shape = rectangle, rounded corners,\n" 
                      +"minimum width = 1cm, font = \\sffamily] "
                      + "("+Integer.toString(variable_counter+1)+") {"+new_subj+"}";
                go_on=true;
                output+=recursiveLoop(new_subj,relations,node_name);
            }
        }
            
            
        if(go_on)return output;
        else return "}\n";
        
    }
}
