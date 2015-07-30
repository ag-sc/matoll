/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.utils;

import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_1;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_2;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_3;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_4;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_5;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_6;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_7;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_8;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_9;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_1;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_10;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_2;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_3;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_4;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_5;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_6;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_7;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_8;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_9;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_1;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_2;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_2b;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_2c;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_3;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_4;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_5;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_6;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_7;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_7b;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_8;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_9;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.*;
import org.jgrapht.graph.*;

/**
 *
 * @author swalter
 */
public class visualizeSPARQL {
    private static int variable_counter = 0;
    public static void main(String[] args){
        List<SparqlPattern> Patterns = new ArrayList<SparqlPattern>();
        Patterns.add(new SparqlPattern_EN_1());
        Patterns.add(new SparqlPattern_EN_2());
        Patterns.add(new SparqlPattern_EN_3());
        Patterns.add(new SparqlPattern_EN_4());
        Patterns.add(new SparqlPattern_EN_5());
        Patterns.add(new SparqlPattern_EN_6());
        Patterns.add(new SparqlPattern_EN_7());
        Patterns.add(new SparqlPattern_EN_8());
        Patterns.add(new SparqlPattern_EN_9());
        Patterns.add(new SparqlPattern_DE_1());
        Patterns.add(new SparqlPattern_DE_2());
        Patterns.add(new SparqlPattern_DE_3());
        Patterns.add(new SparqlPattern_DE_4());
//        Patterns.add(new SparqlPattern_DE_5());
        Patterns.add(new SparqlPattern_DE_6());
        Patterns.add(new SparqlPattern_DE_7());
        Patterns.add(new SparqlPattern_DE_8());
        Patterns.add(new SparqlPattern_DE_9());
        Patterns.add(new SparqlPattern_DE_10());
//        Patterns.add(new SparqlPattern_ES_1());
//        Patterns.add(new SparqlPattern_ES_2());
//        Patterns.add(new SparqlPattern_ES_2b());
//        Patterns.add(new SparqlPattern_ES_2c());
//        Patterns.add(new SparqlPattern_ES_3());
//        Patterns.add(new SparqlPattern_ES_4());
//        Patterns.add(new SparqlPattern_ES_5());
//        Patterns.add(new SparqlPattern_ES_6());
//        Patterns.add(new SparqlPattern_ES_7());
//        Patterns.add(new SparqlPattern_ES_7b());
//        Patterns.add(new SparqlPattern_ES_8());
//        Patterns.add(new SparqlPattern_ES_9());
        
        String prefix = "\\documentclass{scrartcl}\n" +
        "\\usepackage{mathtools}\n" +
        "\\usepackage{tikz}\n" +
        "\\usetikzlibrary{trees,positioning}\n" +
        "\n" +
        "\\begin{document}\n";
        
        String suffix = "\\end{document}";
        String output = "";
        System.out.println("Starting visualisation");
        for(SparqlPattern pattern : Patterns){
            String tmp =doVisual(pattern.getQuery(),pattern.getID().replace("_","\\_"))+"\n\n\n";
            if (tmp!=null)output+=tmp;
            else System.out.println(pattern.getID()+" could not be visualized");
        }
            
        
        PrintWriter writer;
        try {
                writer = new PrintWriter("sparql_tree.tex");
                writer.write(prefix+output+suffix);
                writer.close();
        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        
    }
    
    
    public static String doVisual(String query,String name){
        
        String prefix = "\\begin{figure}\n"
                    +"\\centering\n"
                    +"\\begin{tikzpicture}\n";
        
        String suffix = "\\end{tikzpicture}\n"
                    +"\\caption{Visualisation for "+name+"}\n"
                    +"\\label{fig:"+name.replace("\\_","")+"}\n"
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
            if(s.contains("<conll:head>")&& !s.contains("OPTIONAL") && !s.contains("UNION") &&!s.contains("SELECT")) {
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
            
            if(s.contains("<conll:deprel>")&& !s.contains("OPTIONAL") && !s.contains("UNION") &&!s.contains("SELECT")) {
                String value = (s.replace("}", "")).trim();
                String[] tmp = value.split("<conll:deprel>");
                String subj = tmp[0].trim();
                String obj = tmp[1].trim();
                /*
                TODO: Be aware that sometimes the deprel is hiden in an additional variable.
                In the moment ignore it, but make sure it is taken correctly
                */
                if(!obj.contains("?"))
                deprel.put(subj, obj.replace("\"",""));
            }
        }
        

//
        String head_variable = findRoot(relations);
        if(head_variable!=null){
            variables.put(head_variable,1);
            String latex_output = "\\node[fill = gray!40, "
                    + "shape = rectangle, rounded corners, "
                    +"minimum width = 2cm, font = \\sffamily] "
                    + "("+Integer.toString(variable_counter)+") {"+head_variable+"}\n";
            node_name.put(head_variable, Integer.toString(variable_counter));
            int counter = 0;
            for(List<String> relation:relations){
                String subj = relation.get(0);
                if(!node_name.containsKey(subj)){
                    counter+=1;
                    latex_output+= "child {node[fill = blue!40, shape = rectangle, rounded corners, " 
                            +"minimum width = 1cm, font = \\sffamily]  "
                            + "("+Integer.toString(variable_counter+1)+") {"+subj+"}\n";
                    latex_output+=recursiveLoop(subj,relations,node_name);
                }
            }
//            //Set final number of}
            int oc_1 = StringUtils.countMatches(latex_output+"};\n","}");
            int oc_2 = StringUtils.countMatches(latex_output,"{");
            if(oc_2>oc_1){
                for(int i=0;i<oc_2-oc_1;i++)latex_output+="}";
            }
            String output = "";
            if(oc_1>oc_2){
                output = prefix+latex_output+";\n";
            }
            else{
                output = prefix+latex_output+"};\n";
            }
            
            
            
            
            
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
                output+="child {node[fill = blue!40, shape = rectangle, rounded corners, " 
                      +"minimum width = 1cm, font = \\sffamily] "
                      + "("+Integer.toString(variable_counter+1)+") {"+new_subj+"}\n";
                go_on=true;
                output+=recursiveLoop(new_subj,relations,node_name);
            }
        }
            
            
        if(go_on)return output;
        else return "}\n";
        
    }
}
